package database_console;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**

 @author A.Smith
 */
public class ConfigFileReader {

    private static String printerName;//= "EPSON TM-T20II Receipt";
    private static String registerID;// = "D";
    private static String hostName;
    private static String userName;
    private static String password;
    private static String remoteDrivePath;
    private static String registerReportPath;
    private static String displayComPort;
    private static String cardReaderURL;
    private static String pharmacyName;
    private static String mailPassword;
    private static String rxReportPath;
    private static String dmeReportPath;
    private static String errorLogPath;
    private static Boolean ageNotificatonEnabled;

    private ConfigFileReader() //do not instantiate
    {

    }

    public static void loadConfiguration() throws FileNotFoundException, IOException {
        try
        {

            BufferedReader in = new BufferedReader(new FileReader("C:\\POS\\Config.txt"));

            String line;

            while ((line = in.readLine()) != null)
            {
                String[] tokens = line.split(":", 2);   //limits the split to 2 array elements ie only the first occurance so it will keep any colons in the value portion

                if (tokens.length < 2) //not all data is there just move along
                {
                    continue;
                }
                System.out.println(tokens[0] + " " + tokens[1]);
                if (tokens[0].contains("Register ID"))
                {
                    registerID = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Printer Name"))
                {
                    printerName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Database Hostname"))
                {
                    hostName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Database Username"))
                {
                    userName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Database Password"))
                {
                    password = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Remote Drive Path"))
                {
                    remoteDrivePath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Register Report Path"))
                {
                    registerReportPath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Display Com Port"))
                {
                    displayComPort = tokens[1].trim();
                    // System.out.println(displayComPort);
                }
                else if (tokens[0].contentEquals("Card Terminal Address"))
                {
                    cardReaderURL = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Pharmacy Name"))
                {
                    pharmacyName = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Mail Password"))
                {
                    mailPassword = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("RX Report Path"))
                {
                    rxReportPath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("DME Report Path"))
                {
                    dmeReportPath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Error Log Path"))
                {
                    errorLogPath = tokens[1].trim();
                }
                else if (tokens[0].contentEquals("Age Notification Enabled"))
                {
                    ageNotificatonEnabled = tokens[1].trim().toUpperCase().contains("TRUE");
                }
            }//end while

        }
        catch (FileNotFoundException e)
        {
            throw e;
            //System.out.println("The file could not be found or opened");
        }
        catch (IOException e)
        {
            throw e;
            //System.out.println("Error reading the file");
        }

    }

    public static String getErrorLogPath() {
        return errorLogPath;
    }

    public static String getPharmacyName() {
        return pharmacyName;
    }

    public static String getCardReaderURL() {
        return cardReaderURL;
    }

    public static String getDisplayComPort() {
        return displayComPort;
    }

    public static String getRegisterReportPath() {
        return registerReportPath;
    }

    public static String getDmeReportPath() {
        return dmeReportPath;
    }

    public static String getRxReportPath() {
        return rxReportPath;
    }

    public static String getRegisterID() {
        return registerID;
    }

    public static String getPrinterName() {
        return printerName;
    }

    public static String getHostName() {
        return hostName;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getRemoteDrivePath() {
        return remoteDrivePath;
    }

    public static String getMailPassword() {
        return mailPassword;
    }
        public static Boolean isAgeNotificationEnabled() {
        return ageNotificatonEnabled;
    }
}
