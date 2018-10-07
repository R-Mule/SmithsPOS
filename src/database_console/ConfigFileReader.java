package database_console;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author A.Smith
 */
public class ConfigFileReader {

    private String printerName;//= "EPSON TM-T20II Receipt";
    private String registerID;// = "D";
    private String hostName;
    private String userName;
    private String password;
    private String remoteDrivePath;
    private String registerReportPath;
    private String displayComPort;
    private String cardReaderURL;
    private String pharmacyName;
    private String mailPassword;

    public ConfigFileReader() {
        try {

            BufferedReader in = new BufferedReader(new FileReader("C:\\POS\\Config.txt"));

            String line;

            while ((line = in.readLine()) != null) {
                String[] tokens = line.split(":", 2);   //limits the split to 2 array elements ie only the first occurance so it will keep any colons in the value portion
                //  for (String s : tokens) {

                //System.out.println(s);
                // }
                
                //*******************Levi
                if(tokens.length < 2)   //not all data is there just move along
                    continue;
                /*if(tokens.length != 2)  //more tokens than expected (most likey due to colons in value portion of config) combine them all back together and place colons between them to get original value
                {   
                    for(int i = 2; i < tokens.length; i++)
                    {
                        tokens[1] += ":" + tokens[i];
                    }
                }*///backup code for string split with colons in value portion
                if (tokens[0].contains("Register ID")) {
                    registerID = tokens[1].trim();
                } else if (tokens[0].contains("Printer Name")) {
                    printerName = tokens[1].trim();
                } else if (tokens[0].contains("Database Hostname")) {
                    hostName = tokens[1].trim();
                } else if (tokens[0].contains("Database Username")) {
                    userName = tokens[1].trim();
                } else if (tokens[0].contains("Database Password")) {
                    password = tokens[1].trim();
                } else if (tokens[0].contains("Remote Drive Path")) {
                    remoteDrivePath = tokens[1].trim();
                } else if (tokens[0].contains("Register Report Path")) {
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("MMddyy");
                    registerReportPath = tokens[1].trim() + dateFormat.format(date);
                }else if (tokens[0].contains("Display Com Port")) {
                    displayComPort = tokens[1].trim();
                   // System.out.println(displayComPort);
                }else if(tokens[0].contains("Card Terminal Address")){
                    cardReaderURL = tokens[1].trim();
                    
                }else if(tokens[0].contains("Pharmacy Name")){
                    pharmacyName = tokens[1].trim();
                }else if(tokens[0].contains("Mail Password")){
                    mailPassword = tokens[1].trim();
                }
                //******************Levi
                
                /*if (line.contains("Register ID:")) {
                    registerID = line.substring(13).trim();
                } else if (line.contains("Printer Name:")) {
                    printerName = line.substring(14).trim();
                } else if (line.contains("Database Hostname:")) {
                    hostName = line.substring(19).trim();
                } else if (line.contains("Database Username:")) {
                    userName = line.substring(19).trim();
                } else if (line.contains("Database Password:")) {
                    password = line.substring(19).trim();
                } else if (line.contains("Remote Drive Path:")) {
                    remoteDrivePath = line.substring(19).trim();
                } else if (line.contains("Register Report Path:")) {
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("MMddyy");
                    registerReportPath = line.substring(22).trim()+dateFormat.format(date);
                }else if (line.contains("Display Com Port:")) {
                    displayComPort = line.substring(18).trim();
                   // System.out.println(displayComPort);
                }else if(line.contains("Card Terminal Address:")){
                    cardReaderURL = line.substring(22).trim();
                    
                }else if(line.contains("Pharmacy Name:")){
                    pharmacyName = line.substring(14).trim();
                }else if(line.contains("Mail Password:")){
                    mailPassword = line.substring(14).trim();
                }*/

            }//end while


        } catch (FileNotFoundException e) {
            //System.out.println("The file could not be found or opened");
        } catch (IOException e) {
            //System.out.println("Error reading the file");
        }
    }
    
    public String getPharmacyName(){
        return pharmacyName;
    }

    public String getCardReaderURL(){
        return cardReaderURL;
    }
    public String getDisplayComPort(){
        return displayComPort;
    }
    public String getRegisterReportPath() {
        return registerReportPath;
    }

    public String getRegisterID() {
        return registerID;
    }

    public String getPrinterName() {
        return printerName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRemoteDrivePath() {
        return remoteDrivePath;
    }
    public String getMailPassword() {
        return mailPassword;
    }
}
