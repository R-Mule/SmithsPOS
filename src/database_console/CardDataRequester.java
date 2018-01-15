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
    String authCardType;
    String transID;
    String authAmt;
    String responseCode;
    String approvalCode;
    String last4ofCard;
    String cardType;
    String responseText;
    String authAmtReq;
    
    boolean transTerminate=false;

    CardDataRequester() {

    }

    public void postRequest(String deviceURL, String amount) {
        try {
            URL url = new URL(deviceURL);
            String tran_type = "CCR1";
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
                if (responseCode.contentEquals("00")) {
                    authAmt = doc.getElementsByTagName("AUTH_AMOUNT").item(0).getTextContent();
                    authAmtReq = doc.getElementsByTagName("AUTH_AMOUNT_REQUESTED").item(0).getTextContent();
                    if (authAmt.contentEquals(authAmtReq)) {
                        authCardType = doc.getElementsByTagName("AUTH_CARD_TYPE").item(0).getTextContent();
                        transID = doc.getElementsByTagName("AUTH_GUID").item(0).getTextContent();

                        approvalCode = doc.getElementsByTagName("AUTH_CODE").item(0).getTextContent();
                        last4ofCard = doc.getElementsByTagName("AUTH_MASKED_ACCOUNT_NBR").item(0).getTextContent();
                        cardType = doc.getElementsByTagName("AUTH_CARD_TYPE").item(0).getTextContent();
                        switch (cardType) {
                            case "R":
                                cardType = "Discover";
                                break;
                            case "S":
                                cardType = "American Express";
                                break;
                            case "M":
                                cardType = "MasterCard";
                                break;
                            case "V":
                                cardType = "Visa";
                                break;
                            default:
                                cardType = "Unknown";
                                break;
                        }
                        last4ofCard = last4ofCard.substring(11);
                        approvalCodeHandler ach = new approvalCodeHandler(cardType, responseCode);
                        responseText = ach.getResponseText();

                        System.out.println(responseText);

                    } else {
                        System.out.println("MUST BE AN ERROR!!");
                    }
                } else {
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
        }
    }

    public boolean transTerminated(){
        return transTerminate;
    }
    public boolean hasReceivedData() {
        return hasReceivedData;
    }
}
