package database_console;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**

 @author A.Smith
 */
public class CardDataRequester {

    boolean hasReceivedData = false;
    String merchantID;
    String transID;
    String authAmt;
    String responseCode;
    String approvalCode;
    String last4ofCard;
    String cardType;
    String responseText = "DECLINED";
    String authAmtReq;
    String cardEntryMethod;
    String AID = "";
    String TVR = "";
    String TSI = "";
    boolean transTerminate = false;

    public CardDataRequester() {

    }

    public void postRequest(String deviceURL, String amount, String tran_type) {
        System.out.println("AMOUNT BEING REQUESTED: " + amount);
        try
        {
            URL url = new URL(deviceURL);
            Double d = Double.parseDouble(amount);
            amount = String.format("%.2f", d);
            URLConnection con = url.openConnection();
            // specify that we will send output and accept input
            con.setDoInput(true);
            con.setDoOutput(true);
            // con.setConnectTimeout(999999999);  // long timeout, but not infinite
            //con.setReadTimeout(999999999);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            // tell the web server what we are sending
            con.setRequestProperty("Content-Type", "text/xml");
            try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream()))
            {
                writer.write("<DETAIL><TRAN_TYPE>" + tran_type + "</TRAN_TYPE><AMOUNT>" + amount + "</AMOUNT></DETAIL>");
                writer.flush();
            }
            // reading the response
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            StringBuilder buf = new StringBuilder();
            char[] cbuf = new char[2048];
            int num;
            while (-1 != (num = reader.read(cbuf)))
            {
                buf.append(cbuf, 0, num);
            }
            String result = buf.toString();
            //System.err.println("\nResponse from server after POST:\n" + result);
            readXML(result);
            hasReceivedData = true;//ITS OVER! YA!
        }
        catch (IOException | NumberFormatException t)
        {
            responseText = "CARD Error code: 1. Please report error to manager, then try again.";
            transTerminate = true;
            t.printStackTrace(System.out);
        }
    }

    public void readXML(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try
        {
            db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            StringReader sr = new StringReader(xml);
            is.setCharacterStream(sr);
            try
            {
                doc = db.parse(is);
                //String message = doc.getDocumentElement().getTextContent();
                merchantID = "8788290392911";
                responseCode = doc.getElementsByTagName("AUTH_RESP").item(0).getTextContent();
                if (!responseCode.contentEquals("S0"))
                {
                    cardType = doc.getElementsByTagName("AUTH_CARD_TYPE").item(0).getTextContent();
                    switch (cardType)
                    {
                        case "R":
                            cardType = "DISCOVER";
                            break;
                        case "S":
                            cardType = "AMERICAN EXPRESS";
                            break;
                        case "M":
                            cardType = "MASTERCARD";
                            break;
                        case "V":
                            cardType = "VISA";
                            break;
                        case "O":
                            cardType = "";//This is ALL Debit Cards
                            break;
                        default:
                            cardType = "SPECIAL";
                            break;
                    }
                    approvalCodeHandler ach = new approvalCodeHandler(cardType, responseCode);
                    responseText = ach.getResponseText();
                    if (ach.isApproved())
                    {//this checks to make sure the trans is approved before we parse stuff that doesn't exisit.
                        authAmt = doc.getElementsByTagName("AUTH_AMOUNT").item(0).getTextContent();
                        authAmtReq = doc.getElementsByTagName("AUTH_AMOUNT_REQUESTED").item(0).getTextContent();
                        if (Double.parseDouble(authAmt) == Double.parseDouble(authAmtReq))
                        {
                            transID = doc.getElementsByTagName("AUTH_GUID").item(0).getTextContent();
                            //gets here
                            approvalCode = doc.getElementsByTagName("AUTH_CODE").item(0).getTextContent();
                            last4ofCard = doc.getElementsByTagName("AUTH_MASKED_ACCOUNT_NBR").item(0).getTextContent();
                            last4ofCard = last4ofCard.substring(last4ofCard.lastIndexOf('X'));
                            //gets here too
                            cardEntryMethod = doc.getElementsByTagName("CARD_ENT_METH").item(0).getTextContent();
                            if (cardEntryMethod.contentEquals("G"))
                            {//TOOK THIS OUT ||cardEntryMethod.contentEquals("D")
                                //and here
                                AID = cardEntryMethod = doc.getElementsByTagName("SI_EMV_AID").item(0).getTextContent();
                                TVR = cardEntryMethod = doc.getElementsByTagName("SI_EMV_TVR").item(0).getTextContent();
                                TSI = cardEntryMethod = doc.getElementsByTagName("SI_EMV_TSI").item(0).getTextContent();
                            }
                            System.out.println(responseText);

                        }
                        else
                        {
                            responseText = "CARD Error code: 2. Please report error to manager, then try again.";
                            transTerminate = true;
                            System.out.println("MUST BE AN ERROR!!");
                        }
                    }
                    else
                    {
                        responseText = "CARD Error code: 3. Transcation Declined.";
                        transTerminate = true;
                        System.out.println("TRANS DECLINED1??");
                    }
                }
                else
                {
                    responseText = "CARD Error code: 4. Most likely a timeout issue.\n (Card was not swiped or inserted soon enough after button was pressed)";
                    transTerminate = true;
                    System.out.println("TRANS DECLINED2??");
                }
            }
            catch (SAXException | IOException e)
            {
                responseText = "CARD Error code: 5. Please report error to manager, then try again.";
                transTerminate = true;
                ErrorLogger.writeErrorMsg(e, "Response:+\n\n" + xml +"\n\nDBF:\n\n" + dbf + "\n\nDB:\n\n" + db +"\n\nIS:\n\n" + is + "\n\n" + responseText);
                // handle SAXException
            }
            // handle IOException

        }
        catch (ParserConfigurationException e)
        {
            responseText = "CARD Error code: 6. Please report error to manager, then try again.";
            transTerminate = true;
            ErrorLogger.writeErrorMsg(e, "Response:+\n\n" + xml +"\n\nDBF:\n\n" + dbf + "\n\nDB:\n\n" + db + "\n\n" + responseText);
            // handle ParserConfigurationException
        }
        catch (NullPointerException e)
        {
            transTerminate = true;
            responseText = "FAILED TO FIND AUTH RETRY Error code: 7";
            System.out.println("TRANS DECLINED3??");
            ErrorLogger.writeErrorMsg(e, "Response:+\n\n" + xml +"\n\nDBF:\n\n" + dbf + "\n\nDB:\n\n" + db + "\n\n" +  responseText);
        }
    }

    public boolean transTerminated() {
        return transTerminate;
    }

    public boolean hasReceivedData() {
        return hasReceivedData;
    }
}
