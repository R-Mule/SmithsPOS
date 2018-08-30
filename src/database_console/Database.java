package database_console;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author A.Smith
 */
public class Database {

    private String host;
    private String userName;
    private String password;
    private String driverPath = "com.mysql.jdbc.Driver";
    private ConfigFileReader reader;

    Database() {
        reader = new ConfigFileReader();
        host = reader.getHostName();
        userName = reader.getUserName();
        password = reader.getPassword();
    }//end databaseCtor

    public ArrayList<String> getEmployeesAndWinLossMM() {
        try {
            ArrayList<String> data = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees order by wins desc,losses,empname;");
            while (rs.next()) {
                data.add(rs.getString(2) + " : " + rs.getInt(4) + " : " + rs.getInt(5));

            }//end while
            con.close();
            return data;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String getQuote() {
        try {
            ArrayList<String> quotes = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from quotes;");
            while (rs.next()) {
                quotes.add(rs.getString(2));
            }//end while
            int index = (int) (Math.random() * (quotes.size() - 1 - 0) + 0);
            con.close();
            return quotes.get(index);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String getReceiptString(String receiptNum) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from receiptsFull where receiptNum = '" + receiptNum + "';");
            while (rs.next()) {
                String temp = rs.getString(3);
                con.close();
                return temp;
            }//end while       
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void storeReceiptString(String receiptNum, String receipt) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            receipt = receipt.replaceAll("'", " ");
            stmt.executeUpdate("INSERT INTO `receiptsFull`(`pid`,`receiptNum`,`receipt`) VALUES (NULL,'" + receiptNum + "','" + receipt + "')");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
    }

    public String getEmployeeNameByCode(int code) {

        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees where passcode = " + code);
            while (rs.next()) {
                //Statement stmt2 = con.createStatement();
                //stmt2.executeUpdate("UPDATE `inventory` set price=" + price + " where mutID = '" + mutID + "';");
                String temp = rs.getString(2);
                con.close();
                return temp;
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    void updateItemPrice(String mutID, double price) {//0 return means not found, otherwise returns mutID from database.
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = '" + mutID + "'");
            while (rs.next()) {
                Statement stmt2 = con.createStatement();
                stmt2.executeUpdate("UPDATE `inventory` set price=" + price + " where mutID = '" + mutID + "';");
                System.out.println("FOUND!");
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//end checkDatabaseForItem

    void checkDatabaseForItemByUPC(Item myItem) {//0 return means not found, otherwise returns mutID from database.
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where upc = '" + myItem.itemUPC + "'");
            while (rs.next()) {
//System.out.println(rs.getInt(1)+"  "+rs.getInt(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getDouble(5));  
///if(rs.getString(3).contentEquals(myItem.itemUPC)){ THIS DOES NOT WORK!
                myItem.mutID = rs.getString(2);
                myItem.itemPrice = rs.getDouble(5);
                myItem.itemCost = rs.getDouble(6);
                myItem.itemName = rs.getString(4);
                myItem.isTaxable = rs.getBoolean(7);
                myItem.category = rs.getInt(8);
                System.out.println(myItem.itemUPC);
//}//end if
            }//end while
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//end checkDatabaseForItem

    void checkDatabaseForItemByID(Item myItem) {//0 return means not found, otherwise returns mutID from database.
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = '" + myItem.mutID + "'");
            while (rs.next()) {

                myItem.itemUPC = rs.getString(3);
                if (myItem.itemUPC.length() < 11) {//LEADING ZEROS!
                    String leadingZeros = "";
                    for (int i = 0; i < 11 - myItem.itemUPC.length(); i++) {
                        leadingZeros += "0";
                    }
                    myItem.itemUPC = leadingZeros + myItem.itemUPC;
                }
                myItem.itemPrice = rs.getDouble(5);
                myItem.itemCost = rs.getDouble(6);
                myItem.itemName = rs.getString(4);
                myItem.isTaxable = rs.getBoolean(7);
                myItem.category = rs.getInt(8);
                //if(myItem.category==861){
                //    myItem.hasBeenRefunded=true;
                //    myItem.hasTaxBeenRefunded=true;
                // }
                System.out.println(myItem.itemUPC);
//}//end if
            }//end while
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//end checkDatabaseForItemByI

    public boolean checkDatabaseForTicket(String id) {//returns true if ticket exists.
        if (id.toUpperCase().contentEquals("WONDERLAND")) {
            return true;
        } else if (id.toUpperCase().contentEquals("STRANGER")) {
            return true;
        } else if (id.toUpperCase().contentEquals("HOW ABOUT A MAGIC TRICK?")) {
            return true;
        } else if (id.toUpperCase().contentEquals("WET BANDITS")) {
            return true;
        } else if (id.toUpperCase().contentEquals("WINGARDIUM LEVIOSA")) {
            return true;
        } else {
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from tickets where custId = '" + id + "'");

                while (rs.next()) {
                    // System.out.println(rs.getString(2));
                    if (rs.getString(2).contentEquals(id)) {
                        con.close();
                        return true;
                    }
//}//end if
                }//end while

                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            return false;
        }
    }//end checkDatabaseForTicket

    public void storeItem(Item item, String id) {

        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
//INSERT INTO
//ResultSet rs=stmt.executeQuery("select * from inventory where upc = "+myItem.itemUPC); 

            stmt.executeUpdate("INSERT INTO `tickets` (`pid`,`custId`,`mutID`,`upc`,`name`,`price`,`cost`,`taxable`,`category`,`rxNumber`,`insurance`,`filldate`,`quantity`,`isrx`,`percentagedisc`,`isprecharged`) VALUES (NULL,'" + id + "','" + item.getID() + "','" + item.getUPC() + "','" + item.getName() + "'," + item.getPrice() + "," + item.getCost() + "," + item.isTaxable() + "," + item.getCategory() + "," + item.getRxNumber() + ",'" + item.getInsurance() + "','" + item.getFillDate() + "'," + item.getQuantity() + "," + item.isRX() + "," + item.getDiscountPercentage() + "," + item.isPreCharged() + ")");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch

    }//end storeTicketInDatabase()

    public String[] getAllTicketsNames() {
        ArrayList<String> ticketNames = new ArrayList<String>();
        String[] ticketNamesActual = null;
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from tickets order by custId");
            int i = 0;
            while (rs.next()) {
                if (!ticketNames.contains(rs.getString(2))) {
                    ticketNames.add(rs.getString(2));
                    i++;
                }
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        ticketNamesActual = new String[ticketNames.size()];
        for (int i = 0; i < ticketNames.size(); i++) {
            ticketNamesActual[i] = ticketNames.get(i);
        }
        return ticketNamesActual;
    }

    public ArrayList<String> getAllTicketsNamesWithRxNumber(int rxNumber) {
        ArrayList<String> ticketNames = new ArrayList<>();
        if (rxNumber == 0) {
            return ticketNames;
        } else {
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from tickets  where rxnumber = " + rxNumber + " order by custId;");
                int i = 0;
                while (rs.next()) {
                    if (!ticketNames.contains(rs.getString(2))) {
                        ticketNames.add(rs.getString(2));
                        i++;
                    }
                }//end while

                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return ticketNames;
        }
    }

    public ArrayList<Item> getTicketItemsFromDatabase(String id) {
        ArrayList<Item> loadedItems = new ArrayList<>();
        if (id.toUpperCase().contentEquals("WONDERLAND")) {
            loadedItems.add(new Item(this, "MATRED", "MATRED", "Red Pill", 19.99, 19.99, false, 852, 0, "", "", 1, false, 0, false));
            loadedItems.add(new Item(this, "MATBLU", "MATBLU", "Blue Pill", 19.99, 19.99, false, 852, 0, "", "", 1, false, 0, false));
            return loadedItems;
        } else if (id.toUpperCase().contentEquals("STRANGER")) {
            for (int i = 1; i < 41; i++) {
                loadedItems.add(new Item(this, "BATDIS" + i, "BATDIS" + i, "District", 0.07, 0.07, false, 852, 0, "", "", 1, false, 0, false));
            }

            return loadedItems;
        } else if (id.toUpperCase().contentEquals("HOW ABOUT A MAGIC TRICK?")) {
            loadedItems.add(new Item(this, "BATMON", "BATMON", "Mob Money", 0.36, 0.36, false, 852, 0, "", "", 1, false, 0, false));
            loadedItems.add(new Item(this, "BATPEN", "BATPEN", "Pencil", 8.47, 8.47, false, 852, 0, "", "", 1, false, 0, false));

            return loadedItems;
        } else if (id.toUpperCase().contentEquals("WET BANDITS")) {
            loadedItems.add(new Item(this, "HAPIZZA", "HAPIZZA", "Pizza Box", 11.1363636364, 11.1363636364, false, 852, 0, "", "", 1, false, 0, false));
            EasterEgg ee = new EasterEgg("C:/POS/SOFTWARE/ha1.gif", "C:/POS/SOFTWARE/ha1.wav", "What're you scared, Marv? Are you afraid? C'mon, get out here.", "");
            return loadedItems;
        } else if (id.toUpperCase().contentEquals("WINGARDIUM LEVIOSA")) {
            loadedItems.add(new Item(this, "HPSS2", "HPSS2", "Erised stra ehru oyt ube cafru oyt on wohsi", 1041.11, 1041.11, false, 852, 0, "", "", 1, false, 0, false));
            EasterEgg ee = new EasterEgg("C:/POS/SOFTWARE/hpss2.gif", "C:/POS/SOFTWARE/hpss2.wav", "It does not do to dwell on dreams and forget to live.", "");
            return loadedItems;
        } else {
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from tickets where custId = '" + id + "'");

                while (rs.next()) {
                    // System.out.println(rs.getString(2));
                    if (rs.getString(2).contentEquals(id)) {
                        //System.out.println("HERE!");
                        loadedItems.add(new Item(this, rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getBoolean(8), rs.getInt(9), rs.getInt(10), rs.getString(11), rs.getString(12), rs.getInt(13), rs.getBoolean(14), rs.getDouble(15), rs.getBoolean(16)));
                    }//end if

                }//end while

                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            //REMEBER AFTER LOADED, REMOVE ALL THINGS WITH THAT ID TAG!
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DELETE from tickets where custId = '" + id + "'");

                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return loadedItems;
        }//end else EE

    }//end getTicketFromDatabase

    public void updateChargeAccountBalance(String accountName, double amtToUpdate) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
//here sonoo is database name, root is username and password  
            Statement stmt = con.createStatement();
            accountName = accountName.substring(0, accountName.indexOf(" "));
            System.out.println(accountName);
            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where accntname = '" + accountName + "';");
            while (rs.next()) {
                Statement stmt2 = con.createStatement();
                amtToUpdate += rs.getDouble(6);
                stmt2.executeUpdate("UPDATE `chargeaccounts` set balance='" + amtToUpdate + "' where accntname = '" + accountName + "';");
                System.out.println("FOUND ACCOUNT!");

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateDMEAccountBalance(String accountName, double amtToUpdate) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
//here sonoo is database name, root is username and password  
            Statement stmt = con.createStatement();
            accountName = accountName.substring(0, accountName.indexOf(" "));
            System.out.println(accountName);
            ResultSet rs = stmt.executeQuery("select * from dmeaccounts where pan = '" + accountName + "';");
            while (rs.next()) {
                Statement stmt2 = con.createStatement();
                amtToUpdate += rs.getDouble(6);
                stmt2.executeUpdate("UPDATE `dmeaccounts` set balance='" + amtToUpdate + "' where pan = '" + accountName + "';");
                System.out.println("FOUND ACCOUNT!");

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean checkFrozenAccount(String accountName) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where accntname = '" + accountName + "'");

            while (rs.next()) {
                // System.out.println(rs.getString(2));
                if (rs.getString(2).contentEquals(accountName) && rs.getBoolean(8)) {
                    con.close();
                    return true;
                }
//}//end if
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public void loadDMEData(String path) {
        ArrayList<String> accounts = new ArrayList<>();
        ArrayList<Double> balances = new ArrayList<>();
        ArrayList<String> firstNames = new ArrayList<>();
        ArrayList<String> lastNames = new ArrayList<>();
        ArrayList<String> unfoundAccounts = new ArrayList<>();
        String unfound = "";

        //CSV file header
        final String[] FILE_HEADER_MAPPING = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43"};

        //File attributes
        final String ACCOUNT_NAME = "35";
        final String PATIENT_NAME = "36";
        final String PATIENT_BALANCE1 = "40";
        final String PATIENT_BALANCE2 = "41";
        final String PATIENT_BALANCE3 = "42";
        final String PATIENT_BALANCE4 = "43";

        final String INSURANCE = "17";
        FileReader fileReader = null;

        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

        try {
            //initialize FileReader object
            fileReader = new FileReader(path);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 0; i < csvRecords.size(); i++) {
                CSVRecord record = (CSVRecord) csvRecords.get(i);
                //Create a new student object and fill his data

                if (record.get(INSURANCE).trim().contentEquals("Patient Pay Claims")) {//Its a claim we care about
                    double balance1 = Double.parseDouble(record.get(PATIENT_BALANCE1).substring(1));
                    double balance2 = Double.parseDouble(record.get(PATIENT_BALANCE2).substring(1));
                    double balance3 = Double.parseDouble(record.get(PATIENT_BALANCE3).substring(1));
                    double balance4 = Double.parseDouble(record.get(PATIENT_BALANCE4).substring(1));
                    double balance = balance1 + balance2 + balance3 + balance4;
                    if (accounts.contains(record.get(ACCOUNT_NAME).trim())) {//if we have already added this account to be updated
                        int index = accounts.indexOf(record.get(ACCOUNT_NAME).trim());
                        balances.set(index, round(balance + balances.get(index)));
                    } else {//add it to update
                        accounts.add(record.get(ACCOUNT_NAME).trim());
                        balances.add(round(balance));
                        lastNames.add(record.get(PATIENT_NAME).substring(0, record.get(PATIENT_NAME).indexOf(',')).trim());
                        firstNames.add(record.get(PATIENT_NAME).substring(record.get(PATIENT_NAME).indexOf(',') + 1).trim());
                        // System.out.println(record.get(PATIENT_NAME).substring(record.get(PATIENT_NAME).indexOf(',')+1).trim());
                    }
                    // System.out.println(record.get(PATIENT_NAME));	
                }//end if claims we care about
            }//end for all claims

            for (String s : accounts) {
                System.out.println("FIRST: " + firstNames.get(accounts.indexOf(s)) + " LAST " + lastNames.get(accounts.indexOf(s)) + " ACCOUNT: " + s + " BALANCE DUE: " + balances.get(accounts.indexOf(s)));
                boolean itemFound = false;
                try {
                    Class.forName(driverPath);
                    Connection con = DriverManager.getConnection(
                            host, userName, password);
//here sonoo is database name, root is username and password  
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from dmeaccounts where pan = '" + s + "';");
                    while (rs.next()) {
                        itemFound = true;
                        Statement stmt2 = con.createStatement();
                        stmt2.executeUpdate("UPDATE `dmeaccounts` set lastname = '" + lastNames.get(accounts.indexOf(s)) + "',firstname='" + firstNames.get(accounts.indexOf(s)) + "',balance=" + balances.get(accounts.indexOf(s)) + " where pan = '" + s + "';");
                        //System.out.println("FOUND ACCOUNT!");

                    }//end while
                    if (!itemFound) {
                        unfoundAccounts.add(s);
                        unfound += "\n " + s + " ";
                        // System.out.println("COULD NOT FIND: "+s);
                    }
                    con.close();
                } catch (ClassNotFoundException | SQLException e) {
                    System.out.println(e);
                }
            }//end for all accounts
            if (!unfound.contentEquals("")) {//if not empty show popup
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Couldn't Find: " + unfound + "\n Maybe entered wrong or unadded?");
            } else {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Successfully Loaded! No Errors!");
            }

        } catch (HeadlessException | IOException | NumberFormatException e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
    }

    public void loadARData(String path) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String line;
            while ((line = in.readLine()) != null) {
                String[] tokens = line.split(":");

                if (line != null && !line.isEmpty() && !line.contains("*")) {
                    double realBal = 0;
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String uuid = line.substring(0, 12).replaceAll(" ", "");
                        String accntname = line.substring(12, 24).replaceAll(" ", "");
                        String lastname = line.substring(24, 43).replaceAll(" ", "");
                        String firstname = line.substring(43, 57).replaceAll(" ", "");
                        String dob = line.substring(57, 67).replaceAll(" ", "");
                        String balance = line.substring(68, 87).replaceAll(" ", "");
                        String fro = line.substring(87).replaceAll(" ", "");
                        boolean frozen;
                        if (fro.contentEquals("YES")) {
                            frozen = true;
                        } else {
                            frozen = false;
                        }
                        // uuid=uuid.replaceAll(" ","");
                        if (firstname.isEmpty()) {
                            firstname = "_";
                        }
                        if (lastname.isEmpty()) {
                            lastname = "_";
                        }
                        accntname = accntname.replaceAll(" ", "");
                        dob = dob.replaceAll("/", "");
                        dob = dob.substring(0, 4) + dob.substring(6, 8);
                        firstname = firstname.replaceAll("'", " ");
                        lastname = lastname.replaceAll("'", " ");
                        accntname = accntname.replaceAll("'", " ");

                        balance = balance.replaceAll(",", "");
                        if (balance.charAt(balance.length() - 1) == '-') {
                            realBal = Double.parseDouble(balance.substring(0, balance.indexOf('-')));
                            realBal = realBal * -1;
                        } else {
                            realBal = Double.parseDouble(balance.substring(0, balance.length()));
                        }
                        System.out.println(uuid + accntname + lastname + firstname + dob + balance + "      " + realBal);

                        boolean itemFound = false;
                        if (accntname.isEmpty()) {
                            accntname = "DELETED";
                        }
                        try {
                            Class.forName(driverPath);
                            Connection con = DriverManager.getConnection(
                                    host, userName, password);
//here sonoo is database name, root is username and password  
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where uuid = '" + uuid + "';");
                            while (rs.next()) {
                                itemFound = true;
                                Statement stmt2 = con.createStatement();
                                stmt2.executeUpdate("UPDATE `chargeaccounts` set lastname = '" + lastname + "',firstname='" + firstname + "',balance=" + realBal + ",dob='" + dob + "',accntname='" + accntname + "',frozen =  " + frozen + " where uuid = '" + uuid + "';");
                                System.out.println("FOUND ACCOUNT!");

                            }//end while
                            if (!itemFound) {
                                stmt.executeUpdate("INSERT INTO `chargeaccounts` (`pid`,`accntname`,`lastname`,`firstname`,`dob`,`balance`,`uuid`,`frozen`) VALUES (NULL, '" + accntname + "','" + lastname + "','" + firstname + "','" + dob + "'," + realBal + ",'" + uuid + "'," + frozen + ");");
                            }
                            con.close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                    }//end if lines not empty

                }//end if line is garbage
                if (line.contains("*")) {
                    break;
                }
            }//end while next line
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found or opened");
        } catch (IOException e) {
            System.out.println("Error reading the file");
        }
    }

    public String[] getARList(String accntName, String lastName, String firstName, String dob) {
        boolean oneBefore = false;
        String[] accounts = new String[270];
        String statement = "select * from chargeaccounts where ";
        if (!accntName.isEmpty()) {
            statement += "accntname = '" + accntName + "'";
            oneBefore = true;
        }
        if (!lastName.isEmpty()) {
            if (oneBefore) {
                statement += "and lastname = '" + lastName + "'";
            } else {
                statement += "lastname = '" + lastName + "'";
                oneBefore = true;
            }
        }
        if (!firstName.isEmpty()) {
            if (oneBefore) {
                statement += "and firstname = '" + firstName + "'";
            } else {
                statement += "firstname = '" + firstName + "'";
                oneBefore = true;
            }
        }
        if (!dob.isEmpty()) {
            if (oneBefore) {
                statement += "and dob = '" + dob + "'";
            } else {
                statement += "dob = '" + dob + "'";
                oneBefore = true;
            }
        }
        int i = 0;
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(statement);

            while (rs.next()) {
                // System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5).substring(0, 2)+"-"+rs.getString(5).substring(2, 4)+"-"+rs.getString(5).substring(4, 6));
                accounts[i] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5).substring(0, 2) + "-" + rs.getString(5).substring(2, 4) + "-" + rs.getString(5).substring(4, 6) + " Current Balance $" + String.format("%.2f", rs.getDouble(6));
                i++;
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
        String[] accountsActual = new String[i];
        for (int z = 0; z < i; z++) {
            accountsActual[z] = accounts[z];
        }
        if (accountsActual.length == 0) {
            return null;
        }
        return accountsActual;
    }

    public String[] getDMEList(String accntName, String lastName, String firstName, String dob) {
        boolean oneBefore = false;
        String[] accounts = new String[2000];
        String statement = "select * from dmeaccounts where ";
        accntName = accntName.toUpperCase();
        lastName = lastName.toUpperCase();
        firstName = firstName.toUpperCase();
        if (!accntName.isEmpty()) {
            statement += "pan = '" + accntName + "'";
            oneBefore = true;
        }
        if (!lastName.isEmpty()) {
            if (oneBefore) {
                statement += "and lastname = '" + lastName + "'";
            } else {
                statement += "lastname = '" + lastName + "'";
                oneBefore = true;
            }
        }
        if (!firstName.isEmpty()) {
            if (oneBefore) {
                statement += "and firstname = '" + firstName + "'";
            } else {
                statement += "firstname = '" + firstName + "'";
                oneBefore = true;
            }
        }
        if (!dob.isEmpty()) {
            if (oneBefore) {
                statement += "and dob = '" + dob + "'";
            } else {
                statement += "dob = '" + dob + "'";
                oneBefore = true;
            }
        }
        int i = 0;
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(statement);

            while (rs.next()) {
                System.out.println(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5).substring(0, 2) + "-" + rs.getString(5).substring(2, 4) + "-" + rs.getString(5).substring(4, 6));
                accounts[i] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5).substring(0, 2) + "-" + rs.getString(5).substring(2, 4) + "-" + rs.getString(5).substring(4, 6) + " Current Balance $" + String.format("%.2f", rs.getDouble(6));
                i++;
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
        String[] accountsActual = new String[i];
        for (int z = 0; z < i; z++) {
            accountsActual[z] = accounts[z];
        }
        if (accountsActual.length == 0) {
            return null;
        }
        return accountsActual;
    }

    public String[] getEmployeesFromDatabase() {
        String[] employees = new String[20];
        int cntr = 0;
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees order by empname;");

            while (rs.next()) {

                employees[cntr] = rs.getString(2);
                cntr++;
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        String[] emp = new String[cntr];
        for (int i = 0; i < cntr; i++) {
            emp[i] = employees[i];
        }
        if (emp.length == 0) {
            return null;
        }
        return emp;
    }//end getTicketFromDatabase

    public void storeReceipt(Cart curCart, String receiptNum) {
        for (Item item : curCart.getItems()) {
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO `receipts`(`pid`,`receiptNum`,`mutID`,`upc`,`itemName`,`amtPaidBeforeTax`,`wasTaxed`,`category`,`rxNumber`,`insurance`,`filldate`,`quantity`,`isrx`,`percentagedisc`,`isprecharged`,`hasBeenRefunded`,`hasTaxBeenRefunded`) VALUES (NULL,'" + receiptNum + "','" + item.getID() + "','" + item.getUPC() + "','" + item.getName() + "'," + item.getPrice() + "," + item.isTaxable() + " ," + item.getCategory() + "," + item.getRxNumber() + ",'" + item.getInsurance() + "','" + item.getFillDate() + "'," + item.getQuantity() + "," + item.isRX() + "," + item.getDiscountPercentage() + "," + item.isPreCharged() + "," + item.hasBeenRefunded() + "," + item.hasTaxBeenRefunded() + ")");
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }//end catch
        }
    }

    public void storeReceiptByList(ArrayList<RefundItem> items, String receiptNum) {
        for (Item item : items) {
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO `receipts`(`pid`,`receiptNum`,`mutID`,`upc`,`itemName`,`amtPaidBeforeTax`,`wasTaxed`,`category`,`rxNumber`,`insurance`,`filldate`,`quantity`,`isrx`,`percentagedisc`,`isprecharged`,`hasBeenRefunded`,`hasTaxBeenRefunded`) VALUES (NULL,'" + receiptNum + "','" + item.getID() + "','" + item.getUPC() + "','" + item.getName() + "'," + item.getPrice() + "," + item.isTaxable() + " ," + item.getCategory() + "," + item.getRxNumber() + ",'" + item.getInsurance() + "','" + item.getFillDate() + "'," + item.getQuantity() + "," + item.isRX() + "," + item.getDiscountPercentage() + "," + item.isPreCharged() + "," + item.hasBeenRefunded() + "," + item.hasTaxBeenRefunded() + ")");
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }//end catch
        }
    }

    void removeReceiptByList(ArrayList<RefundItem> items2Del, String receiptNum) {
        for (Item item : items2Del) {
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DELETE from receipts where receiptNum = '" + receiptNum + "' AND mutID = '" + item.getID() + "';");
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }//end catch
        }
    }

    public ArrayList<RefundItem> loadReceipt(String receiptNum) {
        ArrayList<RefundItem> loadedItems = new ArrayList<RefundItem>();
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from receipts where receiptNum = '" + receiptNum + "'");

            while (rs.next()) {
                // System.out.println(rs.getString(2));
                if (rs.getString(2).contentEquals(receiptNum)) {
                    //System.out.println("HERE!,LOADING!!");
                    RefundItem temp = new RefundItem(this, receiptNum, rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getBoolean(7), rs.getInt(8), rs.getInt(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getBoolean(13), rs.getDouble(14), rs.getBoolean(15), rs.getBoolean(16), rs.getBoolean(17));
                    System.out.println("LOAD " + temp.getName() + " :" + temp.hasBeenRefunded());
                    System.out.println("LOAD " + temp.getName() + " :" + temp.hasTaxBeenRefunded());
                    //if(!temp.hasBeenRefunded&& temp.getCategory()==861){
                    //     temp.refundAllActive=true;
                    //  }

                    loadedItems.add(temp);
                }//end if
            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return loadedItems;
    }

    public String[] lookupReceiptByRX(int rxNumber) {//this returns array of receipt#'s
        ArrayList<String> loadedItems = new ArrayList<String>();
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from receipts where rxNumber = '" + rxNumber + "'");

            while (rs.next()) {
                // System.out.println(rs.getString(2));
                if (rs.getInt(9) == rxNumber) {
                    loadedItems.add(rs.getString(2));
                    //loadedItems.add(new RefundItem(this, rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getBoolean(8), rs.getInt(9), rs.getInt(10), rs.getString(11), rs.getString(12), rs.getInt(13), rs.getBoolean(14), rs.getDouble(15), rs.getBoolean(16),rs.getBoolean(17),rs.getBoolean(18)));
                }//end if

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        String[] rxs = new String[loadedItems.size()];
        int i = 0;
        for (String rx : loadedItems) {
            rxs[i] = rx;
            i++;
        }
        return rxs;
    }

    public void updateReceipt(RefundCart curCart, String receiptNum) {
        for (RefundItem item : curCart.getRefundItems()) {
            try {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                if (item.quantity == 0) {

                } else {
                    Statement stmt = con.createStatement();
                    System.out.println("UPDATE " + item.getName() + " :" + item.hasBeenRefunded());
                    System.out.println("UPDATE " + item.getName() + " :" + item.hasTaxBeenRefunded());
                    stmt.executeUpdate("UPDATE `receipts` set quantity = " + item.getQuantity() + ", hasBeenRefunded=" + item.hasBeenRefunded() + ", hasTaxBeenRefunded=" + item.hasTaxBeenRefunded() + " where receiptNum='" + item.receiptNum + "' AND upc = '" + item.getUPC() + "' AND mutID = '" + item.getID() + "'  ;");
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }//end catch
        }
    }

    boolean doesItemExistByUPC(String upc) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where upc = '" + upc + "'");
            while (rs.next()) {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    boolean doesItemExistByID(String mutID) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = '" + mutID + "'");
            while (rs.next()) {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    void addItem(String mutID, String upc, String name, double price, double cost, boolean taxed, int category) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `inventory` (`pid`,`mutID`,`upc`,`name`,`price`,`cost`,`taxable`,`category`) VALUES (NULL, '" + mutID + "','" + upc + "','" + name + "'," + price + "," + cost + "," + taxed + "," + category + ");");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
    }

    boolean doesChargeAccountExisit(String accountName) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where accntname = '" + accountName + "'");
            while (rs.next()) {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    boolean doesQS1UUIDExisit(String uuid) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where uuid = '" + uuid + "'");
            while (rs.next()) {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    void addChargeAccount(String accountName, String lastName, String firstName, String dob, String uuid) {

        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `chargeaccounts` (`pid`,`accntname`,`lastname`,`firstname`,`dob`,`uuid`) VALUES (NULL, '" + accountName + "','" + lastName + "','" + firstName + "','" + dob + "','" + uuid + "');");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
    }

    boolean doesDMEAccountExisit(String accountName) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from dmeaccounts where pan = '" + accountName + "'");
            while (rs.next()) {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    void addDMEAccount(String accountName, String lastName, String firstName, String dob) {

        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `dmeaccounts` (`pid`,`pan`,`firstname`,`lastname`,`dob`) VALUES (NULL, '" + accountName + "','" + firstName + "','" + lastName + "','" + dob + "');");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
    }

    String[] getInsurances() {
        ArrayList<String> loadedItems = new ArrayList<String>();
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from insurances order by insurance ASC;");

            while (rs.next()) {
                // System.out.println(rs.getString(2));
                loadedItems.add(rs.getString(2));

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        String[] insurances = new String[loadedItems.size()];
        int i = 0;
        for (String ins : loadedItems) {
            insurances[i] = ins;
            i++;
        }
        return insurances;
    }

    boolean doesInsuranceExisit(String insurance) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from insurances where insurance = '" + insurance + "'");
            while (rs.next()) {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    void addInsurance(String text) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `insurances` (`pid`,`insurance`) VALUES (NULL, '" + text + "');");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
    }

    void removeInsurance(String text) {
        try {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM `insurances` where insurance = '" + text + "';");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }//end catch
    }

    private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

}//end Database
