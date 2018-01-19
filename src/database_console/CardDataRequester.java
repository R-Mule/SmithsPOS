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
 *
 * @author A.Smith
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
    String responseText="DECLINED";
    String authAmtReq;
    String cardEntryMethod;
    String AID="";
    String TVR="";
    String TSI="";
    boolean transTerminate=false;

    CardDataRequester() {

    }

    public void postRequest(String deviceURL, String amount,String tran_type) {
        System.out.println("AMOUNT BEING REQUESTED: "+amount);
        try {
            URL url = new URL(deviceURL);
            Double d = Double.parseDouble(amount);
            amount = String.format("%.2f", d);
            URLConnection con = url.openConnection();
            // specify that we will send output and accept input
            con.setDoInput(true);
            con.setDoOutput(true);
            //con.setConnectTimeout(200000000);  // long timeout, but not infinite
            //con.setReadTimeout(200000000);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            // tell the web server what we are sending
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write("<DETAIL><TRAN_TYPE>" + tran_type + "</TRAN_TYPE><AMOUNT>" + amount + "</AMOUNT></DETAIL>");
            writer.flush();
            writer.close();
            // reading the response
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            StringBuilder buf = new StringBuilder();
            char[] cbuf = new char[2048];
            int num;
            while (-1 != (num = reader.read(cbuf))) {
                buf.append(cbuf, 0, num);
            }
            String result = buf.toString();
            System.err.println("\nResponse from server after POST:\n" + result);
            readXML(result);
            hasReceivedData = true;//ITS OVER! YA!
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }

    public void readXML(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            try {
                Document doc = db.parse(is);
                //String message = doc.getDocumentElement().getTextContent();
                merchantID = "8788290392911";
                responseCode = doc.getElementsByTagName("AUTH_RESP").item(0).getTextContent();
                if(!responseCode.contentEquals("S0")){
                cardType = doc.getElementsByTagName("AUTH_CARD_TYPE").item(0).getTextContent();
                        switch (cardType) {
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
                if (ach.isApproved()) {//this checks to make sure the trans is approved before we parse stuff that doesn't exisit.
                    authAmt = doc.getElementsByTagName("AUTH_AMOUNT").item(0).getTextContent();
                    authAmtReq = doc.getElementsByTagName("AUTH_AMOUNT_REQUESTED").item(0).getTextContent();
                    if (Double.parseDouble(authAmt)==Double.parseDouble(authAmtReq)) {
                        transID = doc.getElementsByTagName("AUTH_GUID").item(0).getTextContent();

                        approvalCode = doc.getElementsByTagName("AUTH_CODE").item(0).getTextContent();
                        last4ofCard = doc.getElementsByTagName("AUTH_MASKED_ACCOUNT_NBR").item(0).getTextContent();
                        last4ofCard = last4ofCard.substring(last4ofCard.lastIndexOf('X'));
                        
                        cardEntryMethod=doc.getElementsByTagName("CARD_ENT_METH").item(0).getTextContent();
                        if(cardEntryMethod.contentEquals("G")){
                            AID=cardEntryMethod=doc.getElementsByTagName("SI_EMV_AID").item(0).getTextContent();
                            TVR=cardEntryMethod=doc.getElementsByTagName("SI_EMV_TVR").item(0).getTextContent();
                            TSI=cardEntryMethod=doc.getElementsByTagName("SI_EMV_TSI").item(0).getTextContent();
                        }
                        System.out.println(responseText);

                    } else {
                        transTerminate=true;
                        System.out.println("MUST BE AN ERROR!!");
                    }
                } else {
                    transTerminate=true;
                    System.out.println("TRANS DECLINED??");
                }
                }else{
                    transTerminate=true;
                    System.out.println("TRANS DECLINED??");
                }
            } catch (SAXException e) {
                // handle SAXException
            } catch (IOException e) {
                // handle IOException
            }
        } catch (ParserConfigurationException e1) {
            // handle ParserConfigurationException
        } catch (NullPointerException ex) {
            transTerminate=true;
            responseText="FAILED TO FIND AUTH RETRY";
            System.out.println("TRANS DECLINED??");
        }
    }

    public boolean transTerminated(){
        return transTerminate;
    }
    public boolean hasReceivedData() {
        return hasReceivedData;
    }
}
