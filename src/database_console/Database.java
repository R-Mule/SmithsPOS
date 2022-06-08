package database_console;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**

 @author A.Smith
 */
public class Database {

    private static String host;
    private static String userName;
    private static String password;
    private static String driverPath = "com.mysql.cj.jdbc.Driver";
    //private ConfigFileReader reader;
    private static int addedCntr = 0;
    private static int updatedCntr = 0;
    private static int failedCntr = 0;
    private static int unchangedCntr = 0;
    private static int linesProcessed = 0;

    private Database() {
    }//end databaseCtor

    public static void loadDatabase() {
        host = ConfigFileReader.getHostName();
        userName = ConfigFileReader.getUserName();
        password = ConfigFileReader.getPassword();
    }

    public static void storeMasterRefundLog(String employeeName, String description, Double amount, LocalDateTime timestamp) {
        try
        {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO masterRefundLog (pid,employeeName,description,amount,date) VALUES (NULL,'" + employeeName + "', '" + description + "'," + amount + ",'" + sdf.format(timestamp) + "')");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static ArrayList<String> getMasterRefundListByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        ArrayList<String> refundData = new ArrayList<>();
        try
        {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from masterRefundLog where date >= '" + sdf.format(startDate) + "' and date <= '" + sdf.format(endDate) + "' order by date;");
            while (rs.next())
            {
                refundData.add("Date: " + rs.getTimestamp(5) + "  Employee:  " + rs.getString(2) + "  Description:  " + rs.getString(3) + "  Amount:  " + rs.getString(4));
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return refundData;
    }

        public static ArrayList<String> getRxPickupListByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        ArrayList<String> refundData = new ArrayList<>();
        DateTimeFormatter mmddyy = DateTimeFormatter.ofPattern("MMddyy");
        String query = "select * from receipts where isRx = 1 and (receiptNum like '" + startDate.format(mmddyy) + "%'";
        while(!startDate.isEqual(endDate))
        {
            startDate = startDate.plusDays(1);
            query+= " or receiptNum like '" + startDate.format(mmddyy) +"%'";
        }
        query +=") order by rxNumber;";
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
            {
                String tempString = rs.getString(2);
                String date = tempString.substring(0, 2) + "/" + tempString.substring(2,4) + "/" + tempString.substring(4,6);
                refundData.add("Date: " + date + "  Rx Number:  " + rs.getString(9));
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return refundData;
    }
        
    public static void createSmsSubscriber(String accountName, String phoneNumber) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO smssubscribers (pid,accountname,phoneNumber) VALUES (NULL,'" + accountName + "', '" + phoneNumber + "')");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void storeSmsMessage(String accountName, String message, String phoneNumber) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO smsmsgqueue (pid,phonenumber,message,accountname) VALUES (NULL,'" + phoneNumber + "', '" + message + "','" + accountName + "')");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static ArrayList<String> getPhoneNumbersForSmsAccount(String accountName) {
        ArrayList<String> phoneNumbers = new ArrayList<>();
        try
        {

            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smssubscribers where accountName = '" + accountName + "';");
            while (rs.next())
            {
                phoneNumbers.add(rs.getString(3));
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return phoneNumbers;
    }

    public static ArrayList<String> getAccountNamesForSmsAccountByPhoneNumber(String phoneNumber) {
        ArrayList<String> accountNames = new ArrayList<>();
        try
        {

            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smssubscribers where phoneNumber = '" + phoneNumber + "';");
            while (rs.next())
            {
                accountNames.add(rs.getString(2));
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return accountNames;
    }

    public static boolean isPhoneAndAccountNameSubscribed(String phoneNumber, String accountName) {
        boolean foundOne = false;
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smssubscribers where phonenumber = '" + phoneNumber + "' and accountName = '" + accountName + "';");
            while (rs.next())
            {
                System.out.println(rs.getString(2));
                foundOne = true;
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return foundOne;
    }

    //This one may not be needed on this side. 
    public static boolean isPhoneNumberSubscribed(String phoneNumber) {
        boolean foundOne = false;
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smssubscribers where phonenumber = '" + phoneNumber + "';");
            while (rs.next())
            {
                System.out.println(rs.getString(2));
                foundOne = true;
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return foundOne;
    }

    public static boolean isAccountNameSubscribedToSms(String accountName) {
        boolean foundOne = false;
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from smssubscribers where accountName = '" + accountName + "';");
            while (rs.next())
            {
                //System.out.println(rs.getString(2));
                foundOne = true;
            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return foundOne;
    }

    public static void deleteSmsSubscriberByPhoneNumber(String phoneNumber) {
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);

            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from smssubscribers where phonenumber = '" + phoneNumber + "';");

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end 

    public static void deleteSmsSubscriberByAccountName(String accountName) {
        try
        {
            Connection con = DriverManager.getConnection(
                    host, userName, password);

            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from smssubscribers where accountName = '" + accountName + "';");

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end 

    public static boolean updateMutualInventory(String mutID, String upc, String name, double price, double cost, boolean taxable, int category) {//0 return means not found, otherwise returns mutID from database.
        boolean itemFound = false;
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
//here sonoo is database name, root is username and password  
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = " + mutID);
            while (rs.next())
            {
                itemFound = true;
                Statement stmt2 = con.createStatement();
                stmt2.executeUpdate("UPDATE `inventory` set upc = '" + upc + "',name='" + name + "',price=" + price + ",category=" + category + ",cost=" + cost + ",taxable=" + taxable + " where mutID = '" + mutID + "';");
                //  System.out.println("FOUND!");
                // updatedCntr++;
                // totalUpdated.setText("Total Updated: " + updatedCntr);
            }//end while
            if (!itemFound)
            {
                stmt.executeUpdate("INSERT INTO `inventory` (`pid`,`mutID`,`upc`,`name`,`price`,`cost`,`taxable`,`category`) VALUES (NULL, '" + mutID + "','" + upc + "','" + name + "'," + price + "," + cost + ",false," + category + ");");
                // addedCntr++;
                // totalAdded.setText("Total Added: " + addedCntr);
            }
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return itemFound;
    }//end checkDatabaseForItem

    public static void removeItemFromInventory(String mutualID) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM `inventory` where mutID='" + mutualID + "';");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static String addEmployee(String firstName, String lastName, int passCode, String rfid, String patientCode) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `employees`(`pid`,`empname`,`passcode`,`wins`,`losses`,`emprfid`,`uuid`) VALUES (NULL,'" + lastName + ", " + firstName + "','" + passCode + "',0,0,'" + rfid + "','" + patientCode + "')");//zeros are for wins and losses. for March Madness
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
        return "Employee: " + lastName + ", " + firstName + " added successfully with Passcode: " + passCode;
    }

    public static void removeEmployee(int employeeID) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM `employees` where pid=" + employeeID + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void updateEmployeePermissionLevel(int employeeID, int permissionLevel) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("update `employees` set permissionLevel = " + permissionLevel + " where pid=" + employeeID + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void updateEmployeePatientCode(int employeeID, String patientCode) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("update `employees` set uuid = '" + patientCode + "' where pid=" + employeeID + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void updateEmployeeUpdateAckByPasscode(int passcode, String version) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("update `employees` set ackdUpdate = '" + version + "' where passcode= " + passcode + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void updateEmployeeName(int employeeID, String employeeName) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("update `employees` set empName = '" + employeeName + "' where pid=" + employeeID + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void updateEmployeeRFID(int employeeID, String employeeRFID) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("update `employees` set emprfid = '" + employeeRFID + "' where pid=" + employeeID + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void updateEmployeePasscode(int employeeID, int passcode) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("update `employees` set passcode = " + passcode + " where pid=" + employeeID + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void updateEmployeeMarchMadnessScores(int employeeID, int wins, int losses) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("update `employees` set wins = " + wins + " , losses = " + losses + " where pid=" + employeeID + ";");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static String getEmployeesSortByPID() {
        try
        {
            String bigList = "";
            // ArrayList<String> data = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees order by pid asc,empname;");
            while (rs.next())
            {
                bigList += rs.getInt(1) + " : " + rs.getString(2) + "\n";
            }//end while
            con.close();
            return bigList;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static ArrayList<Employee> getEmployeesListSortByPID() {
        ArrayList<Employee> employees = new ArrayList<>();
        try
        {
            // ArrayList<String> data = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees order by pid asc,empname;");
            while (rs.next())
            {
                employees.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(7), rs.getInt(3), rs.getInt(6), rs.getInt(4), rs.getInt(5), rs.getString(8), rs.getString(9)));
            }//end while
            con.close();
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;

    }

    public static ArrayList<String> getEmployeesSortByScore() {
        try
        {
            ArrayList<String> bigList = new ArrayList<>();

            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees order by currentscore desc;");
            while (rs.next())
            {
                bigList.add(rs.getInt(1) + "#" + rs.getString(2) + " : " + rs.getInt(8));
            }//end while
            con.close();
            return bigList;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static boolean checkIfPasscodeExisits(int passCode) {
        try
        {
            ArrayList<String> data = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select passcode from employees;");
            while (rs.next())
            {
                if (rs.getInt(1) == passCode)
                {
                    return true;
                }

            }//end while
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public static ArrayList<String> getEmployeesAndWinLossMM() {
        try
        {
            ArrayList<String> data = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees order by wins desc,losses,empname;");
            while (rs.next())
            {
                data.add(rs.getString(2) + " : " + rs.getInt(4) + " : " + rs.getInt(5));

            }//end while
            con.close();
            return data;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static String getQuote() {
        try
        {
            ArrayList<String> quotes = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from quotes;");
            while (rs.next())
            {
                quotes.add(rs.getString(2));
            }//end while
            int index = (int) (Math.random() * (quotes.size() - 1 - 0) + 0);
            con.close();
            return quotes.get(index);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static ArrayList<String> getReceiptString(String receiptNum) {
        try
        {
            ArrayList<String> receipts = new ArrayList<>();
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from receiptsFull where receiptNum = '" + receiptNum + "';");
            while (rs.next())
            {
                receipts.add(rs.getString(3));
            }//end while 

            ResultSet rs2 = stmt.executeQuery("select * from refundreceiptsFull where receiptNum = '" + receiptNum + "' or refundreceiptnum = '" + receiptNum + "';");
            while (rs2.next())
            {
                receipts.add(rs2.getString(4));
            }//end while 

            con.close();
            return receipts;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static void storeReceiptString(String receiptNum, String receipt) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            receipt = receipt.replaceAll("'", " ");
            stmt.executeUpdate("INSERT INTO `receiptsFull`(`pid`,`receiptNum`,`receipt`) VALUES (NULL,'" + receiptNum + "','" + receipt + "')");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void storeRefundReceiptString(String receiptNum, String refundReceiptNum, String receipt) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            receipt = receipt.replaceAll("'", " ");
            stmt.executeUpdate("INSERT INTO `refundreceiptsfull`(`pid`,`receiptNum`,`refundreceiptnum`,`receipt`) VALUES (NULL,'" + receiptNum + "','" + refundReceiptNum + "','" + receipt + "')");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static String getEmployeeNameByCode(int code) {

        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees where passcode = " + code);
            while (rs.next())
            {
                //Statement stmt2 = con.createStatement();
                //stmt2.executeUpdate("UPDATE `inventory` set price=" + price + " where mutID = '" + mutID + "';");
                String temp = rs.getString(2);
                con.close();
                return temp;
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static int getEmployeePermissionByCode(int code) {

        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select permissionLevel from employees where passcode = " + code);
            while (rs.next())
            {
                //Statement stmt2 = con.createStatement();
                //stmt2.executeUpdate("UPDATE `inventory` set price=" + price + " where mutID = '" + mutID + "';");
                int temp = rs.getInt(1);
                con.close();
                return temp;
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return -1;
    }

    public static void updateItemPrice(String mutID, double price) {//0 return means not found, otherwise returns mutID from database.
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = '" + mutID + "'");
            while (rs.next())
            {
                Statement stmt2 = con.createStatement();
                stmt2.executeUpdate("UPDATE `inventory` set price=" + price + " where mutID = '" + mutID + "';");
                System.out.println("FOUND!");
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end checkDatabaseForItem

    public static void checkDatabaseForItemByUPC(Item myItem) {//0 return means not found, otherwise returns mutID from database.
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where upc = '" + myItem.itemUPC + "'");
            while (rs.next())
            {
//System.out.println(rs.getInt(1)+"  "+rs.getInt(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getDouble(5));  
///if(rs.getString(3).contentEquals(myItem.itemUPC)){ THIS DOES NOT WORK!
                myItem.uniqueId = rs.getInt(1);
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
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end checkDatabaseForItem

    public static void checkDatabaseForItemByID(Item myItem) {//0 return means not found, otherwise returns mutID from database.
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = '" + myItem.mutID + "'");
            while (rs.next())
            {

                myItem.itemUPC = rs.getString(3);
                if (myItem.itemUPC.length() < 11)
                {//LEADING ZEROS!
                    String leadingZeros = "";
                    for (int i = 0; i < 11 - myItem.itemUPC.length(); i++)
                    {
                        leadingZeros += "0";
                    }
                    myItem.itemUPC = leadingZeros + myItem.itemUPC;
                }
                myItem.uniqueId = rs.getInt(1);
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
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }//end checkDatabaseForItemByI

    public static boolean checkDatabaseForTicket(String id) {//returns true if ticket exists.
        if (id.toUpperCase().contentEquals("WONDERLAND"))
        {
            return true;
        }
        else if (id.toUpperCase().contentEquals("STRANGER"))
        {
            return true;
        }
        else if (id.toUpperCase().contentEquals("HOW ABOUT A MAGIC TRICK?"))
        {
            return true;
        }
        else if (id.toUpperCase().contentEquals("WET BANDITS"))
        {
            return true;
        }
        else if (id.toUpperCase().contentEquals("WINGARDIUM LEVIOSA"))
        {
            return true;
        }
        else if (id.toUpperCase().contentEquals("MICHAEL MYERS"))
        {
            return true;
        }
        else
        {
            try
            {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from tickets where custId = '" + id + "'");

                while (rs.next())
                {
                    // System.out.println(rs.getString(2));
                    if (rs.getString(2).contentEquals(id))
                    {
                        con.close();
                        return true;
                    }
//}//end if
                }//end while

                con.close();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

            return false;
        }
    }//end checkDatabaseForTicket

    public static void storeItem(Item item, String id) {

        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
//INSERT INTO
//ResultSet rs=stmt.executeQuery("select * from inventory where upc = "+myItem.itemUPC); 

            stmt.executeUpdate("INSERT INTO `tickets` (`pid`,`custId`,`mutID`,`upc`,`name`,`price`,`cost`,`taxable`,`category`,`rxNumber`,`insurance`,`filldate`,`quantity`,`isrx`,`percentagedisc`,`isprecharged`) VALUES (NULL,'" + id + "','" + item.getID() + "','" + item.getUPC() + "','" + item.getName() + "'," + item.getPrice() + "," + item.getCost() + "," + item.isTaxable() + "," + item.getCategory() + "," + item.getRxNumber() + ",'" + item.getInsurance() + "','" + item.getFillDate() + "'," + item.getQuantity() + "," + item.isRX() + "," + item.getDiscountPercentage() + "," + item.isPreCharged() + ")");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch

    }//end storeTicketInDatabase()

    public static String[] getAllTicketsNames() {
        ArrayList<String> ticketNames = new ArrayList<String>();
        String[] ticketNamesActual = null;
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from tickets order by custId");
            int i = 0;
            while (rs.next())
            {
                if (!ticketNames.contains(rs.getString(2)))
                {
                    ticketNames.add(rs.getString(2));
                    i++;
                }
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        ticketNamesActual = new String[ticketNames.size()];
        for (int i = 0; i < ticketNames.size(); i++)
        {
            ticketNamesActual[i] = ticketNames.get(i);
        }
        return ticketNamesActual;
    }

    public static ArrayList<String> getAllTicketsNamesWithRxNumber(int rxNumber) {
        ArrayList<String> ticketNames = new ArrayList<>();
        if (rxNumber == 0)
        {
            return ticketNames;
        }
        else
        {
            try
            {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from tickets  where rxnumber = " + rxNumber + " order by custId;");
                int i = 0;
                while (rs.next())
                {
                    if (!ticketNames.contains(rs.getString(2)))
                    {
                        ticketNames.add(rs.getString(2));
                        i++;
                    }
                }//end while

                con.close();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            return ticketNames;
        }
    }

    public static ArrayList<Item> getTicketItemsFromDatabase(String id) {
        ArrayList<Item> loadedItems = new ArrayList<>();
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from tickets where custId = '" + id + "'");

            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                if (rs.getString(2).contentEquals(id))
                {
                    //System.out.println("HERE!");
                    loadedItems.add(new Item(rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getBoolean(8), rs.getInt(9), rs.getInt(10), rs.getString(11), rs.getString(12), rs.getInt(13), rs.getBoolean(14), rs.getDouble(15), rs.getBoolean(16), -1));
                }//end if

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        //REMEBER AFTER LOADED, REMOVE ALL THINGS WITH THAT ID TAG!
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE from tickets where custId = '" + id + "'");

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return loadedItems;

    }//end getTicketFromDatabase

    public static void updateChargeAccountBalance(String accountName, double amtToUpdate) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
//here sonoo is database name, root is username and password  
            Statement stmt = con.createStatement();
            accountName = accountName.substring(0, accountName.indexOf(" "));
            System.out.println(accountName);
            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where accntname = '" + accountName + "';");
            while (rs.next())
            {
                Statement stmt2 = con.createStatement();
                amtToUpdate += rs.getDouble(6);
                stmt2.executeUpdate("UPDATE `chargeaccounts` set balance='" + amtToUpdate + "' where accntname = '" + accountName + "';");
                System.out.println("FOUND ACCOUNT!");

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void updateDMEAccountBalance(String accountName, double amtToUpdate) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
//here sonoo is database name, root is username and password  
            Statement stmt = con.createStatement();
            accountName = accountName.substring(0, accountName.indexOf(" "));
            System.out.println(accountName);
            ResultSet rs = stmt.executeQuery("select * from dmeaccounts where pan = '" + accountName + "';");
            while (rs.next())
            {
                Statement stmt2 = con.createStatement();
                amtToUpdate += rs.getDouble(6);
                stmt2.executeUpdate("UPDATE `dmeaccounts` set balance='" + amtToUpdate + "' where pan = '" + accountName + "';");
                System.out.println("FOUND ACCOUNT!");

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static boolean checkFrozenAccount(String accountName) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where accntname = '" + accountName + "'");

            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                if (rs.getString(2).contentEquals(accountName) && rs.getBoolean(8))
                {
                    con.close();
                    return true;
                }
//}//end if
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return false;
    }

    public static void loadDMEData(String path) {
        ArrayList<String> accounts = new ArrayList<>();
        ArrayList<Double> balances = new ArrayList<>();
        ArrayList<String> firstNames = new ArrayList<>();
        ArrayList<String> lastNames = new ArrayList<>();
        ArrayList<String> unfoundAccounts = new ArrayList<>();
        String unfound = "";

        //CSV file header
        final String[] FILE_HEADER_MAPPING =
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43"
        };

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

        try
        {
            //initialize FileReader object
            fileReader = new FileReader(path);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 0; i < csvRecords.size(); i++)
            {
                CSVRecord record = (CSVRecord) csvRecords.get(i);
                //Create a new student object and fill his data

                if (record.get(INSURANCE).trim().contentEquals("Patient Pay Claims"))
                {//Its a claim we care about
                    double balance1 = Double.parseDouble(record.get(PATIENT_BALANCE1).substring(1));
                    double balance2 = Double.parseDouble(record.get(PATIENT_BALANCE2).substring(1));
                    double balance3 = Double.parseDouble(record.get(PATIENT_BALANCE3).substring(1));
                    double balance4 = Double.parseDouble(record.get(PATIENT_BALANCE4).substring(1));
                    double balance = balance1 + balance2 + balance3 + balance4;
                    if (accounts.contains(record.get(ACCOUNT_NAME).trim()))
                    {//if we have already added this account to be updated
                        int index = accounts.indexOf(record.get(ACCOUNT_NAME).trim());
                        balances.set(index, round(balance + balances.get(index)));
                    }
                    else
                    {//add it to update
                        accounts.add(record.get(ACCOUNT_NAME).trim());
                        balances.add(round(balance));
                        firstNames.add(record.get(PATIENT_NAME).substring(0, record.get(PATIENT_NAME).indexOf(',')).trim());//These are inverted....
                        lastNames.add(record.get(PATIENT_NAME).substring(record.get(PATIENT_NAME).indexOf(',') + 1).trim());
                        // System.out.println(record.get(PATIENT_NAME).substring(record.get(PATIENT_NAME).indexOf(',')+1).trim());
                    }
                    // System.out.println(record.get(PATIENT_NAME));	
                }//end if claims we care about
            }//end for all claims

            for (String s : accounts)
            {
                System.out.println("LAST: " + firstNames.get(accounts.indexOf(s)) + " FIRST " + lastNames.get(accounts.indexOf(s)) + " ACCOUNT: " + s + " BALANCE DUE: " + balances.get(accounts.indexOf(s)));
                boolean itemFound = false;
                try
                {
                    Class.forName(driverPath);
                    Connection con = DriverManager.getConnection(
                            host, userName, password);
//here sonoo is database name, root is username and password  
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from dmeaccounts where pan = '" + s + "';");
                    while (rs.next())
                    {
                        itemFound = true;
                        Statement stmt2 = con.createStatement();
                        stmt2.executeUpdate("UPDATE `dmeaccounts` set lastname = '" + lastNames.get(accounts.indexOf(s)) + "',firstname='" + firstNames.get(accounts.indexOf(s)) + "',balance=" + balances.get(accounts.indexOf(s)) + " where pan = '" + s + "';");
                        //System.out.println("FOUND ACCOUNT!");

                    }//end while
                    if (!itemFound)
                    {
                        unfoundAccounts.add(s);
                        unfound += "\n " + s + " ";
                        // System.out.println("COULD NOT FIND: "+s);
                    }
                    con.close();
                }
                catch (ClassNotFoundException | SQLException e)
                {
                    System.out.println(e);
                }
            }//end for all accounts
            if (!unfound.contentEquals(""))
            {//if not empty show popup
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Couldn't Find: " + unfound + "\n Maybe entered wrong or unadded?");
            }
            else
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Successfully Loaded! No Errors!");
            }

        }
        catch (HeadlessException | IOException | NumberFormatException e)
        {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileReader.close();
                csvFileParser.close();
            }
            catch (IOException e)
            {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
    }

    public static void loadARData(String path) {
        addedCntr = 0;
        updatedCntr = 0;
        failedCntr = 0;
        JFrame frame = new JFrame("AR Data Upload");
        //frame.setLayout();
        JLabel addedCustomersLabel = new JLabel("AR Accounts Added: 0");
        JLabel updatedCustomersLabel = new JLabel("AR Accounts Updated: 0");
        JLabel failuresLabel = new JLabel("Failures: (Notify Drew if Any): 0");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 0));
        JButton acknowledgeButton = new JButton("Ok");
        frame.setSize(350, 200);
        addedCustomersLabel.setSize(50, 50);
        updatedCustomersLabel.setSize(50, 50);
        failuresLabel.setSize(50, 50);
        panel.setSize(350, 200);
        frame.add(panel);
        frame.setLocation(800, 300);
        panel.add(addedCustomersLabel);
        panel.add(updatedCustomersLabel);
        panel.add(failuresLabel);
        panel.add(acknowledgeButton);
        panel.setVisible(true);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        acknowledgeButton.setVisible(false);
        acknowledgeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                frame.setVisible(false);
            }
        });
        SwingWorker worker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Background work
                try
                {
                    BufferedReader in = new BufferedReader(new FileReader(path));
                    String line;
                    while ((line = in.readLine()) != null)
                    {
                        String[] tokens = line.split(":");

                        if (line != null && !line.isEmpty() && !line.contains("*"))
                        {
                            double realBal = 0;
                            line = line.trim();
                            if (!line.isEmpty())
                            {
                                String uuid = line.substring(0, 12).replaceAll(" ", "");
                                String accntname = line.substring(12, 24).replaceAll(" ", "");
                                String lastname = line.substring(24, 43).replaceAll(" ", "");
                                String firstname = line.substring(43, 57).replaceAll(" ", "");
                                String dob = line.substring(57, 67).replaceAll(" ", "");
                                String balance = line.substring(68, 87).replaceAll(" ", "");
                                String fro = line.substring(87).replaceAll(" ", "");
                                boolean frozen;
                                if (fro.contentEquals("YES"))
                                {
                                    frozen = true;
                                }
                                else
                                {
                                    frozen = false;
                                }
                                // uuid=uuid.replaceAll(" ","");
                                if (firstname.isEmpty())
                                {
                                    firstname = "_";
                                }
                                if (lastname.isEmpty())
                                {
                                    lastname = "_";
                                }
                                accntname = accntname.replaceAll(" ", "");
                                dob = dob.replaceAll("/", "");
                                dob = dob.substring(0, 4) + dob.substring(6, 8);
                                firstname = firstname.replaceAll("'", "''");
                                lastname = lastname.replaceAll("'", "''");
                                accntname = accntname.replaceAll("'", "''");

                                balance = balance.replaceAll(",", "");
                                if (balance.charAt(balance.length() - 1) == '-')
                                {
                                    realBal = Double.parseDouble(balance.substring(0, balance.indexOf('-')));
                                    realBal = realBal * -1;
                                }
                                else
                                {
                                    realBal = Double.parseDouble(balance.substring(0, balance.length()));
                                }
                                //  System.out.println(uuid + accntname + lastname + firstname + dob + balance + "      " + realBal);

                                boolean itemFound = false;
                                if (accntname.isEmpty())
                                {
                                    accntname = "DELETED";
                                }
                                try
                                {
                                    Class.forName(driverPath);
                                    Connection con = DriverManager.getConnection(
                                            host, userName, password);
//here sonoo is database name, root is username and password  
                                    Statement stmt = con.createStatement();
                                    ResultSet rs = stmt.executeQuery("select * from chargeaccounts where uuid = '" + uuid + "';");
                                    while (rs.next())
                                    {
                                        itemFound = true;
                                        Statement stmt2 = con.createStatement();
                                        stmt2.executeUpdate("UPDATE `chargeaccounts` set lastname = '" + lastname + "',firstname='" + firstname + "',balance=" + realBal + ",dob='" + dob + "',accntname='" + accntname + "',frozen =  " + frozen + " where uuid = '" + uuid + "';");
                                        //System.out.println("FOUND ACCOUNT!");
                                        updatedCntr++;
                                        updatedCustomersLabel.setText("AR Accounts Updated: " + Integer.toString(updatedCntr) + "\n");

                                    }//end while
                                    if (!itemFound)
                                    {
                                        stmt.executeUpdate("INSERT INTO `chargeaccounts` (`pid`,`accntname`,`lastname`,`firstname`,`dob`,`balance`,`uuid`,`frozen`) VALUES (NULL, '" + accntname + "','" + lastname + "','" + firstname + "','" + dob + "'," + realBal + ",'" + uuid + "'," + frozen + ");");
                                        addedCntr++;
                                        addedCustomersLabel.setText("AR Accounts Added: " + Integer.toString(addedCntr) + "\n");
                                    }
                                    con.close();
                                }
                                catch (Exception e)
                                {
                                    System.out.println(e);
                                    failedCntr++;
                                    failuresLabel.setText("Failures: (Notify Drew if Any): " + Integer.toString(failedCntr) + "\n");
                                }

                            }//end if lines not empty

                        }//end if line is garbage
                        if (line.contains("*"))
                        {
                            break;
                        }
                    }//end while next line
                    in.close();
                }
                catch (FileNotFoundException e)
                {
                    System.out.println("The file could not be found or opened");
                }
                catch (IOException e)
                {
                    System.out.println("Error reading the file");
                }
                // Value transmitted to done()
                return true;
            }

            @Override
            protected void process(List<Integer> chunks) {
                // Process results
                //progress++;

            }

            @Override
            protected void done() {
                System.out.println("DONE!");
                acknowledgeButton.setVisible(true);
            }
        };

        // executes the swingworker on worker thread 
        worker.execute();
    }

    public static void loadCustomerData(String path) {

        addedCntr = 0;
        updatedCntr = 0;
        failedCntr = 0;
        unchangedCntr = 0;
        linesProcessed = 0;
        JFrame frame = new JFrame("Customer Data Upload");
        //frame.setLayout();
        JLabel addedCustomersLabel = new JLabel("Customers Added: 0");
        JLabel updatedCustomersLabel = new JLabel("Customers Updated: 0");
        JLabel unchangedCustomersLabel = new JLabel("Customers Unchanged: 0");
        JLabel failuresLabel = new JLabel("Failures: (Notify Drew if Any): 0");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 0));
        JButton acknowledgeButton = new JButton("Ok");
        frame.setSize(350, 250);
        addedCustomersLabel.setSize(50, 50);
        unchangedCustomersLabel.setSize(50, 50);
        updatedCustomersLabel.setSize(50, 50);
        failuresLabel.setSize(50, 50);
        panel.setSize(350, 200);
        frame.add(panel);
        frame.setLocation(800, 300);
        panel.add(addedCustomersLabel);
        panel.add(updatedCustomersLabel);
        panel.add(unchangedCustomersLabel);
        panel.add(failuresLabel);
        panel.add(acknowledgeButton);
        panel.setVisible(true);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        acknowledgeButton.setVisible(false);
        acknowledgeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                frame.setVisible(false);
            }
        });

        //loadTimer();
        SwingWorker worker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Background work
                HashMap<String, Customer> customerMap = new HashMap<>();
                try
                {
                    Connection con = DriverManager.getConnection(
                            host, userName, password);
                    Statement stmt = con.createStatement();

                    try
                    {

                        Class.forName(driverPath);
                        ResultSet rs = stmt.executeQuery("select * from customers;");

                        while (rs.next())
                        {
                            customerMap.put(rs.getString(2), new Customer(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(2), rs.getString(10), rs.getString(9), rs.getString(6), rs.getString(7), rs.getString(8)));

                        }//end while

                    }
                    catch (Exception e)
                    {

                    }

                    System.out.println("Map SIZE: " + customerMap.size());
                    BufferedReader in = new BufferedReader(new FileReader(path));
                    String line;
                    while ((line = in.readLine()) != null)
                    {
                        //line = line.replaceAll("'", "''");
                        String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 9);
                        if (tokens.length < 8)
                        {
                            continue;
                        }
                        linesProcessed++;
                        String uuid = tokens[0].replaceAll("\"", "");
                        String lastName = tokens[1].replaceAll("\"", "");
                        String firstName = tokens[2].replaceAll("\"", "");
                        String dob = tokens[3].replaceAll("\"", "");
                        String address = tokens[4].replaceAll("\"", "");
                        String state = tokens[5].replaceAll("\"", "");
                        String zip = tokens[6].replaceAll("\"", "");
                        String chargeAccount = tokens[7].replaceAll("\"", "");
                        String city = tokens[8].replaceAll("\"", "");

                        boolean itemFound = false;
                        Customer loadedCustomer = new Customer(firstName, lastName, dob, uuid, chargeAccount, zip, address, city, state);

                        if (customerMap.containsKey(loadedCustomer.cid) && loadedCustomer.doesCustomerMatchIdentically(customerMap.get(loadedCustomer.cid)))
                        {
                            itemFound = true;
                            unchangedCntr++;
                            unchangedCustomersLabel.setText("Customers Unchanged: " + Integer.toString(unchangedCntr) + "\n");
                        }
                        else if (customerMap.containsKey(loadedCustomer.cid))
                        {
                            itemFound = true;
                            Statement stmt2 = con.createStatement();
                            stmt2.executeUpdate("UPDATE `customers` set state = '" + state.replaceAll("'", "''") + "',firstname='" + firstName.replaceAll("'", "''") + "',lastname='" + lastName.replaceAll("'", "''") + "',dob='" + dob.replaceAll("'", "''") + "',address='" + address.replaceAll("'", "''") + "',city =  '" + city.replaceAll("'", "''") + "', zip = '" + zip.replaceAll("'", "''") + "', chargeaccount = '" + chargeAccount.replaceAll("'", "''") + "' where uuid = '" + uuid.replaceAll("'", "''") + "';");
                            updatedCntr++;
                            updatedCustomersLabel.setText("Customers Updated: " + Integer.toString(updatedCntr) + "\n");
                        }

                        if (!itemFound)
                        {
                            stmt.executeUpdate("INSERT INTO `customers` (pid, uuid, firstname, lastname, dob, address, city, state, zip, chargeaccount) VALUES (NULL, '" + uuid.replaceAll("'", "''") + "','" + firstName.replaceAll("'", "''") + "','" + lastName.replaceAll("'", "''") + "','" + dob.replaceAll("'", "''") + "','" + address.replaceAll("'", "''") + "','" + city.replaceAll("'", "''") + "','" + state.replaceAll("'", "''") + "','" + zip.replaceAll("'", "''") + "','" + chargeAccount.replaceAll("'", "''") + "');");
                            addedCntr++;
                            addedCustomersLabel.setText("Customers Added: " + Integer.toString(addedCntr) + "\n");
                            // System.out.println("ADDED: " + loadedCustomer.lastName+"," + loadedCustomer.firstName+": "+ loadedCustomer.cid);
                        }

                    }//end while next line

                    con.close();
                    in.close();
                }
                catch (FileNotFoundException e)
                {
                    System.out.println("The file could not be found or opened");
                    failedCntr++;
                    failuresLabel.setText("Failures: (Notify Drew if Any): " + Integer.toString(failedCntr) + "\n");
                    System.out.println(e);
                }
                catch (IOException e)
                {
                    System.out.println("Error reading the file");
                    failedCntr++;
                    failuresLabel.setText("Failures: (Notify Drew if Any): " + Integer.toString(failedCntr) + "\n");
                    System.out.println(e);
                }
                // Value transmitted to done()
                
                return true;
            }

            @Override
            protected void process(List<Integer> chunks) {
                // Process results
                //progress++;

            }

            @Override
            protected void done() {
                // System.out.println("Lines Updated: " + updatedCntr);
                // System.out.println("Lines Added: " + addedCntr);
                // System.out.println("Lines Unchanged: " + unchangedCntr);
                //  System.out.println("DONE!" + linesProcessed);
                acknowledgeButton.setVisible(true);
            }
        };

        // executes the swingworker on worker thread 
        worker.execute();

    }

    public static ArrayList<Customer> getCustomers(String accntName, String lastName, String firstName, String dob) {
        boolean oneBefore = false;
        ArrayList<Customer> customers = new ArrayList<>();
        String statement = "select * from customers where ";
        if (!accntName.isEmpty())
        {
            statement += "uuid = '" + accntName + "'";
            oneBefore = true;
        }
        if (!lastName.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and lastname = '" + lastName + "'";
            }
            else
            {
                statement += "lastname = '" + lastName + "'";
                oneBefore = true;
            }
        }
        if (!firstName.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and firstname = '" + firstName + "'";
            }
            else
            {
                statement += "firstname = '" + firstName + "'";
                oneBefore = true;
            }
        }
        if (!dob.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and dob = '" + dob + "'";
            }
            else
            {
                statement += "dob = '" + dob + "'";
                oneBefore = true;
            }
        }
        int i = 0;
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(statement);

            while (rs.next())
            {
                if (!rs.getString(2).contentEquals("DELETED"))
                {
                    customers.add(new Customer(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(2), rs.getString(10), rs.getString(9), rs.getString(6), rs.getString(7), rs.getString(8)));
                }
                // System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5).substring(0, 2)+"-"+rs.getString(5).substring(2, 4)+"-"+rs.getString(5).substring(4, 6));

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
        if (customers.size() == 0)
        {
            return null;
        }
        return customers;
    }

    public static String[] getARList(String accntName, String lastName, String firstName, String dob) {
        boolean oneBefore = false;
        String[] accounts = new String[270];
        String statement = "select * from chargeaccounts where ";
        if (!accntName.isEmpty())
        {
            statement += "accntname = '" + accntName + "'";
            oneBefore = true;
        }
        if (!lastName.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and lastname = '" + lastName + "'";
            }
            else
            {
                statement += "lastname = '" + lastName + "'";
                oneBefore = true;
            }
        }
        if (!firstName.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and firstname = '" + firstName + "'";
            }
            else
            {
                statement += "firstname = '" + firstName + "'";
                oneBefore = true;
            }
        }
        if (!dob.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and dob = '" + dob + "'";
            }
            else
            {
                statement += "dob = '" + dob + "'";
                oneBefore = true;
            }
        }
        int i = 0;
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(statement);

            while (rs.next())
            {
                if (!rs.getString(2).contentEquals("DELETED"))
                {
                    accounts[i] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5).substring(0, 2) + "-" + rs.getString(5).substring(2, 4) + "-" + rs.getString(5).substring(4, 6) + " Current Balance $" + String.format("%.2f", rs.getDouble(6));
                    i++;
                }
                // System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5).substring(0, 2)+"-"+rs.getString(5).substring(2, 4)+"-"+rs.getString(5).substring(4, 6));

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
        String[] accountsActual = new String[i];
        for (int z = 0; z < i; z++)
        {
            accountsActual[z] = accounts[z];
        }
        if (accountsActual.length == 0)
        {
            return null;
        }
        return accountsActual;
    }

    public static void removeDMEAccount(String accountName) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM `dmeaccounts` where pan = '" + accountName + "';");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void removeCustomer(String cid) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM `customers` where uuid = '" + cid + "';");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void removeChargeAccount(String accountName) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM `chargeaccounts` where accntName = '" + accountName + "';");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static String[] getDMEList(String accntName, String lastName, String firstName, String dob) {
        boolean oneBefore = false;
        String[] accounts = new String[2000];
        String statement = "select * from dmeaccounts where ";
        accntName = accntName.toUpperCase();
        lastName = lastName.toUpperCase();
        firstName = firstName.toUpperCase();
        if (!accntName.isEmpty())
        {
            statement += "pan = '" + accntName + "'";
            oneBefore = true;
        }
        if (!lastName.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and lastname = '" + lastName + "'";
            }
            else
            {
                statement += "lastname = '" + lastName + "'";
                oneBefore = true;
            }
        }
        if (!firstName.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and firstname = '" + firstName + "'";
            }
            else
            {
                statement += "firstname = '" + firstName + "'";
                oneBefore = true;
            }
        }
        if (!dob.isEmpty())
        {
            if (oneBefore)
            {
                statement += "and dob = '" + dob + "'";
            }
            else
            {
                statement += "dob = '" + dob + "'";
                oneBefore = true;
            }
        }
        int i = 0;
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(statement);

            while (rs.next())
            {
                System.out.println(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5).substring(0, 2) + "-" + rs.getString(5).substring(2, 4) + "-" + rs.getString(5).substring(4, 6));
                accounts[i] = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5).substring(0, 2) + "-" + rs.getString(5).substring(2, 4) + "-" + rs.getString(5).substring(4, 6) + " Current Balance $" + String.format("%.2f", rs.getDouble(6));
                i++;
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
        String[] accountsActual = new String[i];
        for (int z = 0; z < i; z++)
        {
            accountsActual[z] = accounts[z];
        }
        if (accountsActual.length == 0)
        {
            return null;
        }
        return accountsActual;
    }

    public static String[] getEmployeesFromDatabase() {
        String[] employees = new String[80];
        int cntr = 0;
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from employees order by empname;");

            while (rs.next())
            {
                employees[cntr] = rs.getString(2);
                cntr++;
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        String[] emp = new String[cntr];
        for (int i = 0; i < cntr; i++)
        {
            emp[i] = employees[i];
        }
        if (emp.length == 0)
        {
            return null;
        }
        return emp;
    }//end getTicketFromDatabase

    public static Customer getCustomerByCID(String customerCID) {
        Customer customer = null;
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customers where uuid = '" + customerCID + "'");

            while (rs.next())
            {
                customer = new Customer(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(2), rs.getString(10), rs.getString(9), rs.getString(6), rs.getString(7), rs.getString(8));
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return customer;
    }

    public static void storeReceipt(Cart curCart, String receiptNum) {
        for (Item item : curCart.getItems())
        {
            try
            {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO `receipts`(`pid`,`receiptNum`,`mutID`,`upc`,`itemName`,`amtPaidBeforeTax`,`wasTaxed`,`category`,`rxNumber`,`insurance`,`filldate`,`quantity`,`isrx`,`percentagedisc`,`isprecharged`,`hasBeenRefunded`,`hasTaxBeenRefunded`) VALUES (NULL,'" + receiptNum + "','" + item.getID() + "','" + item.getUPC() + "','" + item.getName() + "'," + item.getPrice() + "," + item.isTaxable() + " ," + item.getCategory() + "," + item.getRxNumber() + ",'" + item.getInsurance() + "','" + item.getFillDate() + "'," + item.getQuantity() + "," + item.isRX() + "," + item.getDiscountPercentage() + "," + item.isPreCharged() + "," + item.hasBeenRefunded() + "," + item.hasTaxBeenRefunded() + ")");
                con.close();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }//end catch
        }
    }

    public static void storeReceiptByList(ArrayList<RefundItem> items, String receiptNum) {
        for (Item item : items)
        {
            try
            {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO `receipts`(`pid`,`receiptNum`,`mutID`,`upc`,`itemName`,`amtPaidBeforeTax`,`wasTaxed`,`category`,`rxNumber`,`insurance`,`filldate`,`quantity`,`isrx`,`percentagedisc`,`isprecharged`,`hasBeenRefunded`,`hasTaxBeenRefunded`) VALUES (NULL,'" + receiptNum + "','" + item.getID() + "','" + item.getUPC() + "','" + item.getName() + "'," + item.getPrice() + "," + item.isTaxable() + " ," + item.getCategory() + "," + item.getRxNumber() + ",'" + item.getInsurance() + "','" + item.getFillDate() + "'," + item.getQuantity() + "," + item.isRX() + "," + item.getDiscountPercentage() + "," + item.isPreCharged() + "," + item.hasBeenRefunded() + "," + item.hasTaxBeenRefunded() + ")");
                con.close();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }//end catch
        }
    }

    public static void removeReceiptByList(ArrayList<RefundItem> items2Del, String receiptNum) {
        for (Item item : items2Del)
        {
            try
            {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DELETE from receipts where receiptNum = '" + receiptNum + "' AND mutID = '" + item.getID() + "';");
                con.close();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }//end catch
        }
    }

    public static ArrayList<RefundItem> loadReceipt(String receiptNum) {
        ArrayList<RefundItem> loadedItems = new ArrayList<RefundItem>();
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from receipts where receiptNum = '" + receiptNum + "'");

            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                if (rs.getString(2).contentEquals(receiptNum))
                {
                    //System.out.println("HERE!,LOADING!!");
                    RefundItem temp = new RefundItem(receiptNum, rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getBoolean(7), rs.getInt(8), rs.getInt(9), rs.getString(10), rs.getString(11), rs.getInt(12), rs.getBoolean(13), rs.getDouble(14), rs.getBoolean(15), rs.getBoolean(16), rs.getBoolean(17));
                    System.out.println("LOAD " + temp.getName() + " :" + temp.hasBeenRefunded());
                    System.out.println("LOAD " + temp.getName() + " :" + temp.hasTaxBeenRefunded());
                    // if(temp.hasBeenRefunded == true && temp.getPriceOfItemBeforeTax() > 0)
                    // {
                    //     temp.hasBeenRefunded = false;
                    // }
                    //if(!temp.hasBeenRefunded&& temp.getCategory()==861){
                    //     temp.refundAllActive=true;
                    //  }

                    loadedItems.add(temp);
                }//end if
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return loadedItems;
    }

    public static String[] lookupReceiptByRX(int rxNumber) {//this returns array of receipt#'s
        ArrayList<String> loadedItems = new ArrayList<String>();
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from receipts where rxNumber = '" + rxNumber + "'");

            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                if (rs.getInt(9) == rxNumber)
                {
                    loadedItems.add(rs.getString(2));
                    //loadedItems.add(new RefundItem(this, rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getBoolean(8), rs.getInt(9), rs.getInt(10), rs.getString(11), rs.getString(12), rs.getInt(13), rs.getBoolean(14), rs.getDouble(15), rs.getBoolean(16),rs.getBoolean(17),rs.getBoolean(18)));
                }//end if

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        String[] rxs = new String[loadedItems.size()];
        int i = 0;
        for (String rx : loadedItems)
        {
            rxs[i] = rx;
            i++;
        }
        return rxs;
    }

    public static void updateReceipt(RefundCart curCart, String receiptNum) {
        for (RefundItem item : curCart.getRefundItems())
        {
            try
            {
                Class.forName(driverPath);
                Connection con = DriverManager.getConnection(
                        host, userName, password);
                if (item.quantity == 0)
                {

                }
                else
                {
                    Statement stmt = con.createStatement();
                    System.out.println("UPDATE " + item.getName() + " :" + item.hasBeenRefunded());
                    System.out.println("UPDATE " + item.getName() + " :" + item.hasTaxBeenRefunded());
                    stmt.executeUpdate("UPDATE `receipts` set quantity = " + item.getQuantity() + ", hasBeenRefunded=" + item.hasBeenRefunded() + ", hasTaxBeenRefunded=" + item.hasTaxBeenRefunded() + " where receiptNum='" + item.receiptNum + "' AND upc = '" + item.getUPC() + "' AND mutID = '" + item.getID() + "'  ;");
                }
                con.close();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }//end catch
        }
    }

    public static boolean doesItemExistByUPC(String upc) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where upc = '" + upc + "'");
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

        public static boolean doesItemExistByUPCButNotPID(String upc, int pid) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where upc = '" + upc + "' and pid != " + pid);
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }
        
    public static boolean doesItemExistByID(String mutID) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = '" + mutID + "'");
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

        public static boolean doesItemExistByIDButNotPID(String mutID, int pid) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where mutID = '" + mutID + "' and pid != " + pid);
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }
        
    public static void addItem(String mutID, String upc, String name, double price, double cost, boolean taxed, int category) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `inventory` (`pid`,`mutID`,`upc`,`name`,`price`,`cost`,`taxable`,`category`) VALUES (NULL, '" + mutID + "','" + upc + "','" + name + "'," + price + "," + cost + "," + taxed + "," + category + ");");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void addCustomer(Customer customer) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `customers` (pid, uuid, firstname, lastname, dob, address, city, state, zip, chargeaccount) VALUES (NULL, '" + customer.cid.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.firstName.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.lastName.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.dob.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.address.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.city.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.state.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.zipCode.replaceAll("'", "''").replaceAll("\"", "") + "','" + customer.chargeAccountName.replaceAll("'", "''").replaceAll("\"", "") + "');");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static boolean doesChargeAccountExisit(String accountName) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from chargeaccounts where accntname = '" + accountName + "'");
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public static boolean doesQS1patientCodeExisit(String uuid) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customers where uuid = '" + uuid + "'");
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public static void addChargeAccount(String accountName, String lastName, String firstName, String dob, String uuid) {

        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `chargeaccounts` (`pid`,`accntname`,`lastname`,`firstname`,`dob`,`uuid`) VALUES (NULL, '" + accountName + "','" + lastName + "','" + firstName + "','" + dob + "','" + uuid + "');");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static ArrayList<TicketLog> getTicketLogList(LocalDateTime startDate, LocalDateTime endDate, String ticketOwnerAccountName) {
        ArrayList<TicketLog> ticketLogs = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ticketlog where ticketOwnersAccount = '" + ticketOwnerAccountName + "' and modificationTime >= '" + sdf.format(startDate) + "' and modificationTime <= '" + sdf.format(endDate) + "';");

            while (rs.next())
            {
                ticketLogs.add(new TicketLog(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getTimestamp(8).toLocalDateTime(), rs.getString(9)));
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return ticketLogs;
    }

    public static void insertTicketLog(TicketLog ticketLog) {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `ticketLog` (`pid`,`ticketOwnersName`,`ticketOwnersAccount`,`modifiedByName`,`modifiedBysAccount`,`itemModified`,`modificationType`,`modificationTime`,`registerUsed`) VALUES (NULL, '" + ticketLog.ticketOwnersName + "','" + ticketLog.ticketOwnersAccount + "','" + ticketLog.modifiedByName.toUpperCase() + "','" + ticketLog.modifiedByAccount + "','" + ticketLog.itemModified + "','" + ticketLog.modificationType + "','" + sdf.format(ticketLog.modificationTime) + "','" + ticketLog.registerUsed + "');");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static boolean doesDMEAccountExisit(String accountName) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from dmeaccounts where pan = '" + accountName + "'");
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public static void addDMEAccount(String accountName, String lastName, String firstName, String dob) {

        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `dmeaccounts` (`pid`,`pan`,`firstname`,`lastname`,`dob`) VALUES (NULL, '" + accountName + "','" + firstName + "','" + lastName + "','" + dob + "');");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static String[] getInsurances() {
        ArrayList<String> loadedItems = new ArrayList<String>();
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from insurances order by insurance ASC;");

            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                loadedItems.add(rs.getString(2));

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        String[] insurances = new String[loadedItems.size()];
        int i = 0;
        for (String ins : loadedItems)
        {
            insurances[i] = ins;
            i++;
        }
        return insurances;
    }

    public static boolean doesInsuranceExisit(String insurance) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from insurances where insurance = '" + insurance + "'");
            while (rs.next())
            {
                // System.out.println(rs.getString(2));
                return true;//there was atleast one item with this UPC

            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public static void addInsurance(String text) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `insurances` (`pid`,`insurance`) VALUES (NULL, '" + text + "');");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static void removeInsurance(String text) {
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM `insurances` where insurance = '" + text + "';");
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }//end catch
    }

    public static ArrayList<DMERentalItem> getAllDMERentalItems() {
        ArrayList<DMERentalItem> dmeRentalItems = new ArrayList<>();
        try
        {
            Class.forName(driverPath);
            Connection con = DriverManager.getConnection(
                    host, userName, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from dmeRentalItems order by equipmentName");
            int i = 0;
            while (rs.next())
            {
                dmeRentalItems.add(new DMERentalItem(rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getString(5), rs.getString(6)));
            }//end while

            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return dmeRentalItems;
    }

    private static double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

}//end Database
