package database_console;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**

 @author hollie smith This menu bar spans the top of the screen and should have
 menu items that drop down.
 */
public class TopMenuBar extends JMenuBar {

    //Class Variables
    JMenu addMenu, remMenu, mgmtMenu, feedMenu, themeMenu;
    JMenuItem addDmeAccount, remDmeAccount, addRxAccount, remRxAccount, addInsurance, remInsurance,
            addEmployee, remEmployee, addInventoryItem, remInventoryItem, dmeDataUpload, rxDataUpload,
            masterRefund, masterRptRecpt, drawerReports, updatePrice, bugReport, featureRequest, mutualFileUpload, ncaaReportUpload,
            editEmployee, arAuditReport, customerDataUpload, ticketReport,
            christmasTheme, thanksgivingTheme, fourthTheme, saintPatsTheme, easterTheme, summerTimeTheme, halloweenTheme, valentinesTheme; //bugReport and featureRequest - Hollie's suggestions
    MainFrame mf;
    int totalCntr = 0;
    int totalFound = 0;
    int totalAdded = 0;

    //ctor
    public TopMenuBar(MainFrame mf) {
        this.mf = mf;
        addMenu = new JMenu("Add");
        addMenu.setMnemonic(KeyEvent.VK_A);
        addMenu.setVisible(false);
        remMenu = new JMenu("Remove");
        remMenu.setMnemonic(KeyEvent.VK_R);
        remMenu.setVisible(false);
        //This is where Hollie added a new menu to accommodate data uploads in a separate way that seems clean...
        //If it's screwy, delete from the above comment line down to happy code
        mgmtMenu = new JMenu("Management");
        mgmtMenu.setMnemonic(KeyEvent.VK_M);
        mgmtMenu.setVisible(false);
        themeMenu = new JMenu("Theme");
        themeMenu.setMnemonic(KeyEvent.VK_T);
        themeMenu.setVisible(false);
        feedMenu = new JMenu("Feedback");
        feedMenu.setMnemonic(KeyEvent.VK_F);
        feedMenu.setVisible(false);

//Add  menu items
        addDmeAccount = new JMenuItem();
        addDmeAccount.setText("DME Account");
        addMenu.add(addDmeAccount);//This adds DME Account Selection to Add Menu Choices

        addRxAccount = new JMenuItem();
        addRxAccount.setText("Rx Account");
        addMenu.add(addRxAccount);//This adds Rx Account Selection to Add Menu Choices

        addInsurance = new JMenuItem();
        addInsurance.setText("Insurance");
        addMenu.add(addInsurance);//This adds Insurance Selection to Add Menu Choices

        addEmployee = new JMenuItem();
        addEmployee.setText("Employee");
        addMenu.add(addEmployee);//This adds Employee Selection to Add Menu Choices

        addInventoryItem = new JMenuItem();
        addInventoryItem.setText("Inventory Item");
        addMenu.add(addInventoryItem);//This adds Inventory Item Selection to Add Menu Choices
//Remove menu items
        remDmeAccount = new JMenuItem();
        remDmeAccount.setText("DME Account");
        remMenu.add(remDmeAccount);//This adds DME Account Selection to Rem Menu Choices

        remRxAccount = new JMenuItem();
        remRxAccount.setText("Rx Account");
        remMenu.add(remRxAccount);//This adds Rx Account Selection to Rem Menu Choices

        remInsurance = new JMenuItem();
        remInsurance.setText("Insurance");
        remMenu.add(remInsurance);//This adds Insurance Selection to Rem Menu Choices

        remEmployee = new JMenuItem();
        remEmployee.setText("Employee");
        remMenu.add(remEmployee);//This adds Employee Selection to Rem Menu Choices

        remInventoryItem = new JMenuItem();
        remInventoryItem.setText("Inventory Item");
        remMenu.add(remInventoryItem);//This adds Inventory Item Selection to Rem Menu Choices
//Management menu items
        dmeDataUpload = new JMenuItem();
        dmeDataUpload.setText("DME Data Upload");
        mgmtMenu.add(dmeDataUpload);//This adds DME Data Upload to Management Menu Choices

        rxDataUpload = new JMenuItem();
        rxDataUpload.setText("AR Data Upload");
        mgmtMenu.add(rxDataUpload);//This adds Rx Data Upload to Management Menu Choices

        customerDataUpload = new JMenuItem();
        customerDataUpload.setText("Customer Data Upload");
        mgmtMenu.add(customerDataUpload);//This adds Customer Data Upload to Management Menu Choices

        masterRefund = new JMenuItem();
        masterRefund.setText("Master Refund");
        mgmtMenu.add(masterRefund);//This adds Master Refund to Management Menu Choices

        masterRptRecpt = new JMenuItem();
        masterRptRecpt.setText("Master Reprint Receipt");
        mgmtMenu.add(masterRptRecpt);//This adds Master Reprint Receipt to Management Menu Choices

        drawerReports = new JMenuItem();
        drawerReports.setText("Drawer Reports");
        mgmtMenu.add(drawerReports);//This adds Drawer Reports to Management Menu Choices

        arAuditReport = new JMenuItem();
        arAuditReport.setText("AR Audit Reports");
        mgmtMenu.add(arAuditReport);

        ticketReport = new JMenuItem();
        ticketReport.setText("Ticket Report");
        mgmtMenu.add(ticketReport);

        updatePrice = new JMenuItem();
        updatePrice.setText("Update Price");
        mgmtMenu.add(updatePrice);//This adds Update Price to Management Menu Choices

        mutualFileUpload = new JMenuItem();
        mutualFileUpload.setText("Mutual File Upload");
        mgmtMenu.add(mutualFileUpload);//This adds Mutual File Upload to Management Menu Choices

        ncaaReportUpload = new JMenuItem();
        ncaaReportUpload.setText("March Madness File Upload");
        mgmtMenu.add(ncaaReportUpload);//This adds March Maddness File Upload to Management Menu Choices

        editEmployee = new JMenuItem();
        editEmployee.setText("Edit Employee");
        mgmtMenu.add(editEmployee);//This adds Modify Employee to Management Menu Choices
//Feedback menu items
        bugReport = new JMenuItem();
        bugReport.setText("Bug Report");
        feedMenu.add(bugReport);//This adds Update Price to Management Menu Choices

        featureRequest = new JMenuItem();
        featureRequest.setText("Feature Request");
        feedMenu.add(featureRequest);//This adds Update Price to Management Menu Choices

        //Theme menu items
        christmasTheme = new JMenuItem();
        christmasTheme.setText("Christmas");
        themeMenu.add(christmasTheme);

        thanksgivingTheme = new JMenuItem();
        thanksgivingTheme.setText("Thanksgiving");
        themeMenu.add(thanksgivingTheme);

        fourthTheme = new JMenuItem();
        fourthTheme.setText("4th of July");
        themeMenu.add(fourthTheme);

        saintPatsTheme = new JMenuItem();
        saintPatsTheme.setText("Saint Patricks Day");
        themeMenu.add(saintPatsTheme);

        easterTheme = new JMenuItem();
        easterTheme.setText("Easter");
        themeMenu.add(easterTheme);

        summerTimeTheme = new JMenuItem();
        summerTimeTheme.setText("Summer");
        themeMenu.add(summerTimeTheme);

        halloweenTheme = new JMenuItem();
        halloweenTheme.setText("Halloween");
        themeMenu.add(halloweenTheme);

        valentinesTheme = new JMenuItem();
        valentinesTheme.setText("Valentines");
        themeMenu.add(valentinesTheme);

        this.add(addMenu);
        this.add(remMenu);
        this.add(mgmtMenu);
        this.add(themeMenu);
        this.add(feedMenu);

//Add menu action listeners
        addDmeAccount.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDmeAccountActionPerformed(evt);
            }
        });

        addRxAccount.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRxAccountActionPerformed(evt);
            }
        });

        addInsurance.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInsuranceActionPerformed(evt);
            }
        });

        addEmployee.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeActionPerformed(evt);
            }
        });

        addInventoryItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInventoryItemActionPerformed(evt);
            }
        });
//Remove menu action listeners
        remDmeAccount.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remDmeAccountActionPerformed(evt);
            }
        });

        remRxAccount.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remRxAccountActionPerformed(evt);
            }
        });

        remInsurance.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remInsuranceActionPerformed(evt);
            }
        });

        remEmployee.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remEmployeeActionPerformed(evt);
            }
        });

        remInventoryItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remInventoryItemActionPerformed(evt);
            }
        });
//Management menu action listeners
        dmeDataUpload.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dmeDataUploadActionPerformed(evt);
            }
        });

        rxDataUpload.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxDataUploadActionPerformed(evt);
            }
        });

        customerDataUpload.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerDataUploadActionPerformed(evt);
            }
        });

        masterRefund.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masterRefundActionPerformed(evt);
            }
        });
        masterRptRecpt.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masterRptRecptActionPerformed(evt);
            }
        });
        drawerReports.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawerReportsActionPerformed(evt);
            }
        });

        arAuditReport.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arAuditReportsActionPerformed(evt);
            }
        });

        ticketReport.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticketReportActionPerformed(evt);
            }
        });

        updatePrice.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePriceActionPerformed(evt);
            }
        });

        mutualFileUpload.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mutualFileUploadActionPerformed(evt);
            }
        });

        ncaaReportUpload.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ncaaReportUploadActionPerformed(evt);
            }
        });

        editEmployee.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeActionPerformed(evt);
            }
        });

//Feedback menu action listeners
        bugReport.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bugReportActionPerformed(evt);
            }
        });

        featureRequest.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                featureRequestActionPerformed(evt);
            }
        });

        christmasTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                christmasThemeActionPerformed(evt);
            }
        });

        fourthTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fourthThemeActionPerformed(evt);
            }
        });

        saintPatsTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saintPatsThemeActionPerformed(evt);
            }
        });

        thanksgivingTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thanksgivingThemeActionPerformed(evt);
            }
        });

        halloweenTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                halloweenThemeActionPerformed(evt);
            }
        });

        summerTimeTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summerTimeThemeActionPerformed(evt);
            }
        });

        valentinesTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valentinesThemeActionPerformed(evt);
            }
        });

        easterTheme.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                easterThemeActionPerformed(evt);
            }
        });

    }//end ctor

    //members/functions
//Addition menu item functions
    private void addDmeAccountActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextField field3 = new JTextField();
        JTextField field4 = new JTextField();
        Object[] message =
        {
            "Account Name: ", field1, "First Name: ", field2, "Last Name: ", field3, "DOB: example: 111789", field4
        };

        field1.addAncestorListener(new RequestFocusListener());
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add DME Account Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!mf.validateInteger(field4.getText()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Not a valid DOB");
            }
            else if (Database.doesDMEAccountExisit(field1.getText().toUpperCase()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: DME Account Name already exisits!");
            }
            else
            {

                Object[] message2 =
                {
                    "Are you sure?\nAccount Name: " + field1.getText().toUpperCase(), "First Name: " + field2.getText().toUpperCase(), "Last Name: " + field3.getText().toUpperCase(), "DOB: example: 010520: " + field4.getText()
                };

                int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add DME Account Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option2 == JOptionPane.OK_OPTION)
                {
                    Database.addDMEAccount(field1.getText().toUpperCase(), field2.getText().toUpperCase(), field3.getText().toUpperCase(), field4.getText());
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Success!");
                }
                //FIELD1 CONTAINS DESCRIPTION
                //FIELD2 AMOUNT
                mf.displayChangeDue = false;
                mf.updateCartScreen();
            }//end else
        }//end if  
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end dmeAccountActionPerformed

    private void addRxAccountActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field5 = new JTextField();
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextField field3 = new JTextField();
        JTextField field4 = new JTextField();
        Object[] message =
        {
            "QS1 UUID: ", field5, "Account Name: ", field1, "First Name: ", field2, "Last Name: ", field3, "DOB: example: 030986", field4
        };

        field5.addAncestorListener(new RequestFocusListener());
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add RX Account Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!mf.validateInteger(field4.getText()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Not a valid DOB");
            }
            else if (Database.doesChargeAccountExisit(field1.getText().toUpperCase()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: Charge Account Name already exisits!");
            }
            else if (Database.doesQS1UUIDExisit(field5.getText().toUpperCase()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: QS1 UUID already exisits!");
            }
            else if (field5.getText().isEmpty())
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Must enter a UUID!");
            }
            else
            {

                Object[] message2 =
                {
                    "Are you sure?\nUUID: " + field5.getText().toUpperCase() + "\nAccount Name: " + field1.getText().toUpperCase(), "First Name: " + field2.getText().toUpperCase(), "Last Name: " + field3.getText().toUpperCase(), "DOB: example: 010520: " + field4.getText()
                };

                int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add RX Account Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option2 == JOptionPane.OK_OPTION)
                {
                    Database.addChargeAccount(field1.getText().toUpperCase(), field3.getText().toUpperCase(), field2.getText().toUpperCase(), field4.getText(), field5.getText().toUpperCase());
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Success!");
                }
                //FIELD1 CONTAINS DESCRIPTION
                //FIELD2 AMOUNT
                mf.displayChangeDue = false;
                mf.updateCartScreen();
            }//end else
        }//end if  
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end addRxAccountActionPerformed

    private void addInsuranceActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");

        JTextField field1 = new JTextField();
        field1.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Insurance to Add:", field1
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Insurance Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field1.getText().isEmpty())
            {
                if (Database.doesInsuranceExisit(field1.getText().replaceAll("'", " ")))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Insurance already exisits!");
                }
                else
                {
                    Database.addInsurance(field1.getText().replaceAll("'", " "));
                }
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end addInsuranceActionPerformed
    //Fn to be built (below)

    private void addEmployeeActionPerformed(java.awt.event.ActionEvent evt) {
        boolean tryAgain;
        JFrame textInputFrame = new JFrame("");
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextField field3 = new JTextField();
        JTextField field4 = new JTextField();
        JTextField field5 = new JTextField();
        Object[] message =
        {
            "First Name: ex. Anduin", field1, "Last Name: ex. Smith", field2, "Passcode: (1-99)", field3, "Employee RFID: ###,#####", field4, "Employee Patient Code: 10 Char Max", field5
        };
        do
        {
            tryAgain = false;
            field1.addAncestorListener(new RequestFocusListener());
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add New Employee Menu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
                if (!mf.validateInteger(field3.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Not a valid Passcode must be a whole number.");//prompt try again?
                    tryAgain = true;
                }
                else if (Integer.parseInt(field3.getText()) < 1 || Integer.parseInt(field3.getText()) > 99)
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Passcode must be less than 100 and greater than 0.");//prompt try again?
                    tryAgain = true;
                }
                else if (Database.checkIfPasscodeExisits(Integer.parseInt(field3.getText())))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Passcode is already in use.");//prompt try again?
                    tryAgain = true;
                }
                else if (!field1.getText().matches("[A-Z]{1}[a-z]+?"))
                {//Requires Capital first letter of name, and then all lower case at least 1 lower case unknown more.
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "First Name must have capital first letter and rest all lowercase. Ex: Abcde");//prompt try again?
                    tryAgain = true;
                }
                else if (!field2.getText().matches("[A-Z]{1}[a-z]+?"))
                {//Same as above, name Validation.
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Last Name must have capital first letter and rest all lowercase. Ex: Abcde");//prompt try again?
                    tryAgain = true;
                }
                else if (!field4.getText().matches("[0-9][0-9][0-9],[0-9][0-9][0-9][0-9][0-9]"))
                {//Same as above, name Validation. ###,#####
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Employee RFID Must be in format: ###,#####");//prompt try again?
                    tryAgain = true;
                }
                else if (field5.getText().length() > 10)
                {//Same as above, name Validation. ###,#####
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Employee Charge Account must be no more than 10 characters.");//prompt try again?
                    tryAgain = true;
                }
                if (tryAgain)
                {
                    int option2 = JOptionPane.showConfirmDialog(textInputFrame, "Would you like to try again?", "Try Again Menu", JOptionPane.YES_NO_OPTION);
                    if (option2 != JOptionPane.YES_OPTION)
                    {
                        tryAgain = false;
                    }
                }
                else
                {

                    Object[] message2 =
                    {
                        "Are you sure?\nFirst Name: " + field1.getText(), "Last Name: " + field2.getText(), "Passcode: " + field3.getText(), "RFID #: " + field4.getText(), "Patient Code: " + field5.getText(),
                    };

                    int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add New Employee Menu", JOptionPane.OK_CANCEL_OPTION);
                    if (option2 == JOptionPane.OK_OPTION)
                    {
                        String result = Database.addEmployee(field1.getText(), field2.getText(), Integer.parseInt(field3.getText()), field4.getText(), field5.getText());
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, result);
                    }
                    //FIELD1 CONTAINS DESCRIPTION
                    //FIELD2 AMOUNT
                    mf.displayChangeDue = false;
                    mf.updateCartScreen();
                }//end else
            }
            else
            {
                tryAgain = false;//Cancel pressed on first menu. Game over man.
            }//end if  
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        }
        while (tryAgain);
    }//end addEmployeeActionPerformed

    private void addInventoryItemActionPerformed(java.awt.event.ActionEvent evt) {

        JFrame textInputFrame = new JFrame("");
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextField field3 = new JTextField();
        JTextField field4 = new JTextField();
        JTextField field5 = new JTextField();
        JTextField field6 = new JTextField();
        JTextField field7 = new JTextField();
        Object[] message =
        {
            "Name: ", field1, "ID: ", field2, "UPC: ", field3, "Cost: $", field4, "Price: $", field5, "Category: ", field6, "Is Taxed: ", field7
        };
        field7.setText("Yes");
        field7.setSelectionStart(0);
        field7.setSelectionEnd(4);

        field1.addAncestorListener(new RequestFocusListener());
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add Item Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!mf.validateDouble(field4.getText()) || !mf.validateDouble(field5.getText()) || !mf.validateInteger(field6.getText()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Invalid price, cost, or category.");
            }
            else if (!field7.getText().toUpperCase().contentEquals("YES") && !field7.getText().toUpperCase().contentEquals("NO"))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Must enter YES or NO for Is Taxed");
            }
            else if (Database.doesItemExistByUPC(field3.getText()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: Same UPC exists for item already.");
            }
            else if (Database.doesItemExistByID(field2.getText()))
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: Same mutual ID exists for item already.");
            }
            else
            {

                Object[] message2 =
                {
                    "Are you sure?\nName: " + field1.getText().replaceAll("'", " "), "ID: " + field2.getText().replaceAll("'", " "), "UPC: " + field3.getText().replaceAll("'", " "), "Cost: $ " + field4.getText(), "Price: $ " + field5.getText(), "Category: " + field6.getText(), "Is Taxed:  " + field7.getText()
                };

                int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add Item Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option2 == JOptionPane.OK_OPTION)
                {
                    boolean taxed = false;
                    if (field7.getText().toUpperCase().contentEquals("YES"))
                    {
                        taxed = true;
                    }
                    String upc = field3.getText();
                    if (upc.length() > 11)
                    {
                        upc = upc.replaceAll("'", "");
                        upc = upc.substring(0, 11);
                    }
                    Database.addItem(field2.getText().replaceAll("'", ""), upc, field1.getText().replaceAll("'", " "), Double.parseDouble(field5.getText()), Double.parseDouble(field4.getText()), taxed, Integer.parseInt(field6.getText()));
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Success!");
                }
                //FIELD1 CONTAINS DESCRIPTION
                //FIELD2 AMOUNT
                mf.displayChangeDue = false;
                mf.updateCartScreen();
            }//end else
        }//end if  
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end addInventoryItemActionPerformed

//Removal menu item functions
    //Fn to be built (below)
    private void remDmeAccountActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field2 = new JTextField();
        field2.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "DME Account Name to Remove", field2
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "DME Account Removal Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field2.getText().isEmpty())
            {
                if (!Database.doesDMEAccountExisit(field2.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: DME Account Name:" + field2.getText() + " does not exisit.");
                }
                else if (mf.curCart.containsAccountName(field2.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Cannot delete DME account while it is in the cart!");
                }
                else
                {
                    String[] temp = Database.getDMEList(field2.getText(), "", "", "");
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete: " + temp[0] + "?", "WARNING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        Database.removeDMEAccount(field2.getText());
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "DME Account Name:" + field2.getText() + " has been deleted successfully.");
                    }//end if yes option
                }//end else
            }//end if
        }//end if ok option
    }//end dmeAccountActionPerformed
    //Fn to be built (below)

    private void remRxAccountActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field2 = new JTextField();
        field2.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "RX Account Name to Remove", field2
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "RX Account Removal Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field2.getText().isEmpty())
            {
                if (!Database.doesChargeAccountExisit(field2.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: RX Account Name:" + field2.getText() + " does not exisit.");
                }
                else if (mf.curCart.containsAccountName(field2.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Cannot delete RX account while it is in the cart!");
                }
                else
                {
                    String[] temp = Database.getARList(field2.getText(), "", "", "");
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete: " + temp[0] + "?", "WARNING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        Database.removeChargeAccount(field2.getText());
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "RX Account Name:" + field2.getText() + " has been deleted successfully.");
                    }//end if yes option
                }//end else
            }//end if
        }//end if ok option
    }//end rxAccountActionPerformed

    private void remInsuranceActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field2 = new JTextField();
        field2.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Insurance to Remove", field2
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Insurance Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field2.getText().isEmpty())
            {
                if (!Database.doesInsuranceExisit(field2.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: No such insurance to remove!");
                }
                else
                {
                    Database.removeInsurance(field2.getText());
                }
            }
        }
    }//end InsuranceActionPerformed
    //Fn to be built (below)

    private void remEmployeeActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field1 = new JTextField();
        field1.addAncestorListener(new RequestFocusListener());
        String masterList = Database.getEmployeesSortByPID();//Format PID : NAME \n for all employees in this one String.
        Object[] message =
        {
            "Please select an employee # from this list to remove: \n" + masterList + "Enter Employee #", field1
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Remove Employee Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field1.getText().isEmpty())
            {
                if (masterList.contains(field1.getText()) && mf.validateInteger(field1.getText()))
                {
                    int begin = masterList.indexOf(field1.getText());//This finds the string of employee being removed.
                    String temp = masterList.substring(begin);
                    temp = temp.substring(0, temp.indexOf("\n"));
                    int clerkIndex = mf.employeeSelectionHeader.getText().indexOf("Active Clerk: ") + 14;
                    String activeClerk = mf.employeeSelectionHeader.getText().substring(clerkIndex);
                    System.out.println(activeClerk);
                    System.out.println(temp.substring(temp.indexOf(": ") + 2));
                    if (activeClerk.contentEquals(temp.substring(temp.indexOf(": ") + 2)))
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Error: You cannot delete yourself.");
                    }
                    else if (Integer.parseInt(field1.getText()) == 5 || Integer.parseInt(field1.getText()) == 10 || Integer.parseInt(field1.getText()) == 11 || Integer.parseInt(field1.getText()) == 12 || Integer.parseInt(field1.getText()) == 14 || Integer.parseInt(field1.getText()) == 15)
                    {//Hard protect these employees.
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Error: This employee cannot be deleted.");
                    }
                    else if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to remove: " + temp, "WARNING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        Database.removeEmployee(Integer.parseInt(field1.getText()));//Remove, its final.
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Employee: " + temp + " removed successfully.");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Invalid Employee ID.");
                }
            }
        }
    }//end EmployeeActionPerformed
    //Fn to be built (below)

    private void remInventoryItemActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field2 = new JTextField();
        field2.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "NOTE: PLEASE Only delete items made by employees. There is NO need to delete Mutual Items. Thanks!\nID of Item to Remove ex.POP001:", field2
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Delete User Made Item Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field2.getText().isEmpty())
            {
                if (!Database.doesItemExistByID(field2.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: No such item with ID: " + field2.getText() + " to remove!");
                }
                else if (mf.curCart.containsItemByID(field2.getText()))
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Cannot remove item while it is in cart!");
                }
                else
                {
                    Database.removeItemFromInventory(field2.getText());
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Successfully removed item: " + field2.getText());

                }
            }
        }
        Database.doesItemExistByID(TOOL_TIP_TEXT_KEY);
    }//end rxAccountActionPerformed

//Management menu items
    //Path selection to be implemented (below)
    private void dmeDataUploadActionPerformed(java.awt.event.ActionEvent evt) {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
        int result = fileChooser.showOpenDialog(mf);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to load file data?", "WARNING",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                Database.loadDMEData(selectedFile.getAbsolutePath());
                mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        }

    }//end dmeDataUploadActionPerformed    
    //Path selection to be implemented (below)

    private void rxDataUploadActionPerformed(java.awt.event.ActionEvent evt) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
        int result = fileChooser.showOpenDialog(mf);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to load file data?", "WARNING",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                //Hollie or Drew, do AR.
                // String path = "C://QS1/AR.txt";
                Database.loadARData(selectedFile.getAbsolutePath());
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end rxDataUploadActionPerformed

    private void customerDataUploadActionPerformed(java.awt.event.ActionEvent evt) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
        int result = fileChooser.showOpenDialog(mf);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to load file data?", "WARNING",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                //Hollie or Drew, do AR.
                // String path = "C://QS1/AR.txt";
                // create a frame 
                Database.loadCustomerData(selectedFile.getAbsolutePath());
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end rxDataUploadActionPerformed

    private void masterRefundActionPerformed(java.awt.event.ActionEvent evt) {

        JFrame textInputFrame = new JFrame("");
        JTextField field2 = new JTextField();
        JTextField field1 = new JTextField();
        field2.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Description: ", field2,
            "Refund Amount: $", field1
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Refund Amount Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (mf.validateDouble(field1.getText()) && field2.getText() != null && !field2.getText().isEmpty())
            {

                mf.checkout.beginMasterRefund(Double.parseDouble(field1.getText()), field2.getText());
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Success! Please give them: $" + field1.getText());
            }
            else
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Refund failed. Enter a description and a number please.");
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

    }//end masterRefundActionPerformed

    private void masterRptRecptActionPerformed(java.awt.event.ActionEvent evt) {

        JFrame textInputFrame = new JFrame("");

        JTextField field1 = new JTextField();
        field1.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Receipt #:", field1
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Master Reprint Receipt Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field1.getText().isEmpty())
            {
                ArrayList<String> receipts = Database.getReceiptString(field1.getText());
                if (receipts != null && !receipts.isEmpty())
                {
                    for (String receipt : receipts)
                    {
                        mf.checkout.reprintReceipt(receipt);
                    }

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Could not find receipt.");
                }
            }

        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end masterRptRecptActionPerformed

    private void arAuditReportsActionPerformed(java.awt.event.ActionEvent evt) {
        DateRangeSelector drs = new DateRangeSelector();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMddyy");

        if (drs.validDates())
        {
            LocalDateTime startDate = drs.getStartDate();
            LocalDateTime endDate = drs.getEndDate();
            FileWriter rxFW = null;
            BufferedWriter rxBW = null;
            FileWriter dmeFW = null;
            BufferedWriter dmeBW = null;
            try
            {
                //All reports now loaded. Time to do the work and generate the file.
                rxFW = new FileWriter("C:\\pos\\REPORTS\\RX_AR_AUIDT-" + startDate.format(dateFormat) + "_" + endDate.format(dateFormat) + ".txt");//Hollie's Accounts (RX)
                rxBW = new BufferedWriter(rxFW);
                dmeFW = new FileWriter("C:\\pos\\REPORTS\\DME_AR_AUIDT-" + startDate.format(dateFormat) + "_" + endDate.format(dateFormat) + ".txt");
                dmeBW = new BufferedWriter(dmeFW);
            }
            catch (IOException ex)
            {
                Logger.getLogger(TopMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (startDate.isBefore(endDate) || startDate.isEqual(endDate))
            {
                String currentDate = startDate.format(dateFormat);
                try
                {
                    File rxFile, dmeFile;
                    String path = "";

                    //rxFile = new File("Z:\\" + currentDate + "R.posrf");
                    //path = "Z:\\";
                    path = ConfigFileReader.getRxReportPath();
                    rxFile = new File(path + currentDate + "R.posrf");
                    // path = "Z:\\";

                    // System.out.println("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                    //path = "Y:\\";
                    path = ConfigFileReader.getDmeReportPath();
                    dmeFile = new File(path + currentDate + "D.posrf");

                    //System.out.println("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                    DrawerReport rxReport = null;
                    if (rxFile.exists() && !rxFile.isDirectory())
                    {
                        // read object from file path + currentDate + "R.posrf"
                        FileInputStream fis = new FileInputStream(rxFile);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        rxReport = (DrawerReport) ois.readObject();
                        //System.out.println("RX Exisits " + currentDate);
                        //reports.add(dr);
                        ois.close();
                    }
                    DrawerReport dmeReport = null;
                    if (dmeFile.exists() && !dmeFile.isDirectory())
                    {
                        // read object from file
                        FileInputStream fis = new FileInputStream(dmeFile);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        dmeReport = (DrawerReport) ois.readObject();
                        //System.out.println("DME Exisits " + currentDate);
                        //reports.add(dr);
                        ois.close();
                    }
                    //Merge the two reports, then log the data to file.

                    ArrayList<String> mergedARAccountNames = new ArrayList<>();
                    ArrayList<Double> mergedARAccountPayments = new ArrayList<>();
                    ArrayList<String> mergedARAccountChargesNames = new ArrayList<>();
                    ArrayList<Double> mergedARAccountCharges = new ArrayList<>();
                    ArrayList<String> mergedDMEAccountNames = new ArrayList<>();
                    ArrayList<Double> mergedDMEAccountPayments = new ArrayList<>();
                    if (rxReport != null)
                    {
                        int loadIndex = 0;
                        for (String accountName : rxReport.ARAccountName)
                        {
                            mergedARAccountNames.add(accountName);
                            mergedARAccountPayments.add(rxReport.amountPaidToARAccount.get(loadIndex));
                            loadIndex++;
                        }
                        loadIndex = 0;
                        for (String accountName : rxReport.accountNameCharged)
                        {
                            mergedARAccountChargesNames.add(accountName);
                            mergedARAccountCharges.add(rxReport.amountChargedToAccount.get(loadIndex));
                            loadIndex++;
                        }
                        loadIndex = 0;
                        for (String accountName : rxReport.DMEAccountName)
                        {
                            mergedDMEAccountNames.add(accountName);
                            mergedDMEAccountPayments.add(rxReport.amountPaidToDMEAccount.get(loadIndex));
                            loadIndex++;
                        }
                    }

                    if (dmeReport != null)
                    {
                        int index = 0;
                        for (String dmeName : dmeReport.ARAccountName)
                        {
                            boolean found = false;
                            int innerIndex = 0;
                            for (String existingName : mergedARAccountNames)
                            {
                                if (dmeName.contentEquals(existingName))//FOUND!
                                {
                                    mergedARAccountPayments.set(innerIndex, mergedARAccountPayments.get(innerIndex) + dmeReport.amountPaidToARAccount.get(index));
                                    //    mergedARAccountCharges.set(index, mergedARAccountCharges.get(index) + dmeReport.amountChargedToAccount.get(innerIndex));
                                    found = true;
                                    break;
                                }
                                innerIndex++;
                            }
                            if (!found)
                            {
                                mergedARAccountNames.add(dmeName);
                                mergedARAccountPayments.add(dmeReport.amountPaidToARAccount.get(index));
                            }
                            index++;
                        }

                        index = 0;
                        for (String dmeName : dmeReport.accountNameCharged)
                        {
                            boolean found = false;
                            int innerIndex = 0;
                            for (String existingName : mergedARAccountChargesNames)
                            {
                                if (dmeName.contentEquals(existingName))//FOUND!
                                {
                                    mergedARAccountCharges.set(innerIndex, mergedARAccountCharges.get(innerIndex) + dmeReport.amountChargedToAccount.get(index));
                                    found = true;
                                    break;
                                }
                                innerIndex++;
                            }
                            if (!found)
                            {
                                mergedARAccountChargesNames.add(dmeName);
                                mergedARAccountCharges.add(dmeReport.amountChargedToAccount.get(index));
                                //    mergedARAccountCharges.add(dmeReport.amountChargedToAccount.get(index));
                            }
                            index++;
                        }

                        index = 0;
                        for (String dmeName : dmeReport.DMEAccountName)
                        {
                            boolean found = false;
                            int innerIndex = 0;
                            for (String existingName : mergedDMEAccountNames)
                            {
                                if (dmeName.contentEquals(existingName))//FOUND!
                                {
                                    mergedDMEAccountPayments.set(innerIndex, mergedDMEAccountPayments.get(innerIndex) + dmeReport.amountPaidToDMEAccount.get(index));
                                    found = true;
                                    break;
                                }
                                innerIndex++;
                            }
                            if (!found)
                            {
                                mergedDMEAccountNames.add(dmeName);
                                mergedDMEAccountPayments.add(dmeReport.amountPaidToDMEAccount.get(index));
                            }
                            index++;
                        }

                    }
                    int index = 0;
                    for (String accountName : mergedARAccountNames)
                    {
                        if (index == 0)
                        {
                            rxBW.write("Account Payments\n");
                        }
                        rxBW.write(currentDate + "  :  " + accountName + "  :  " + mergedARAccountPayments.get(index) + "\n");
                        index++;
                    }

                    index = 0;
                    for (String accountName : mergedARAccountChargesNames)
                    {
                        if (index == 0)
                        {
                            rxBW.write("Account Charges\n");
                        }
                        rxBW.write(currentDate + "  :  " + accountName + "  :  " + mergedARAccountCharges.get(index) + "\n");
                        index++;
                    }

                    index = 0;
                    for (String accountName : mergedDMEAccountNames)
                    {
                        dmeBW.write(currentDate + "  :  " + accountName + "  :  " + mergedDMEAccountPayments.get(index) + "\n");
                        index++;
                    }

                }
                catch (FileNotFoundException e)
                {
                    System.out.println("JERE");
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    System.out.println("EERE");
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                    System.out.println("JERsE");
                }

                startDate = startDate.plusDays(1);
            }//end while loading reports

            try
            {

                if (rxBW != null)
                {
                    rxBW.close();
                }

                if (rxFW != null)
                {
                    rxFW.close();
                }
                if (dmeBW != null)
                {
                    dmeBW.close();
                }

                if (dmeFW != null)
                {
                    dmeFW.close();
                }
            }
            catch (IOException ex)
            {

                ex.printStackTrace();

            }

        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

    }//end arReportsActionPerformed

    private void drawerReportsActionPerformed(java.awt.event.ActionEvent evt) {

        JFrame textInputFrame = new JFrame("");
        DrawerReport dr = null;
        JTextField field1 = new JTextField();
        field1.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Report Date: EX. 012017D", field1
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Report Name", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {

            try
            {
                File f;
                String path = "";
                if (field1.getText().toUpperCase().contains("R"))
                {
                    //f = new File("Z:\\" + field1.getText().toUpperCase() + ".posrf");
                    //path = "Z:\\";
                    path = ConfigFileReader.getRxReportPath();
                    f = new File(path + field1.getText().toUpperCase() + ".posrf");

                    // System.out.println("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                }
                else
                {
                    //f = new File("Y:\\" + field1.getText().toUpperCase() + ".posrf");
                    //path = "Y:\\";
                    path = ConfigFileReader.getDmeReportPath();
                    f = new File(path + field1.getText().toUpperCase() + ".posrf");
                    //System.out.println("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                }
                if (f.exists() && !f.isDirectory())
                {
                    // read object from file
                    FileInputStream fis = new FileInputStream(path + field1.getText().toUpperCase() + ".posrf");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    dr = (DrawerReport) ois.readObject();
                    dr.generateReport(field1.getText().toUpperCase());
                    ois.close();
                }
                else
                {
                    //WRONG DOESNT EXISIT!
                }

                //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());
            }
            catch (FileNotFoundException e)
            {
                System.out.println("NO FILE FOUND!");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                System.out.println("READ WRITE EX");
                mf.showErrorMessage("Could not open drawer data, incompatible file?!");
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                System.out.println("JERsE");
            }

        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

    }//end DrawerReportsActionPerformed

    private void updatePriceActionPerformed(java.awt.event.ActionEvent evt) {

        JFrame textInputFrame = new JFrame("");

        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        field1.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Mutual ID:", field1, "New Price: $", field2
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Item Info", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (mf.validateDouble(field2.getText()))
            {
                Database.updateItemPrice(field1.getText(), Double.parseDouble(field2.getText()));
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

    }//end updatePriceActionPerformed
//Feedback menu items

    private void mutualFileUploadActionPerformed(java.awt.event.ActionEvent evt) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("POS File", "pos");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
        int result = fileChooser.showOpenDialog(mf);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to load file data?", "WARNING",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                JFrame frame = new JFrame("Mutual POS File Upload");
                //frame.setLayout();
                JLabel addedCustomersLabel = new JLabel("Mutual Items Added: 0");
                JLabel updatedCustomersLabel = new JLabel("Mutual Items Updated: 0");
                JLabel processedLabel = new JLabel("Total Muutal Items Processed: 0");
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(4, 0));
                JButton acknowledgeButton = new JButton("Ok");
                frame.setSize(350, 200);
                addedCustomersLabel.setSize(50, 50);
                updatedCustomersLabel.setSize(50, 50);
                processedLabel.setSize(50, 50);
                panel.setSize(350, 200);
                frame.add(panel);
                frame.setLocation(800, 300);
                panel.add(addedCustomersLabel);
                panel.add(updatedCustomersLabel);
                panel.add(processedLabel);
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
                totalCntr = 0;
                totalFound = 0;
                totalAdded = 0;
                SwingWorker worker = new SwingWorker<Boolean, Integer>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        // Background work
                        try
                        {

                            BufferedReader in = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));

                            String line;

                            while ((line = in.readLine()) != null && !line.isEmpty())
                            {
                                totalCntr++;
                                //WORKING UPC
                                //  System.out.println("");
                                String upc = line.substring(0, 11);

                                // System.out.println("UPC: " + upc);
                                //WORKING NAME
                                String name = line.substring(26, 71);
                                name = name.replace("'", " ");
                                // System.out.println("NAME: " + name);

                                //WORKING PRICE
                                String price = line.substring(107, 113);
                                Double d = Double.parseDouble(price);
                                d = d / 100;
                                //  System.out.println("PRICE: " + d);

                                //WORKING MUTUAL ID
                                String mutID = line.substring(114, 117);
                                mutID += line.substring(118, 121);
                                //  System.out.println("mutual ID: " + mutID);

                                //COST
                                String costTemp = line.substring(90, 99);
                                Double cost = Double.parseDouble(costTemp);
                                cost = cost / 1000;
                                //System.out.println(line.substring(124, 127));
                                int quantity = Integer.parseInt(line.substring(124, 127));
                                if (quantity != 0)
                                {
                                    cost = round(cost / quantity);
                                }
                                // System.out.println("COST: " + cost);

                                //ITEM CATEGORY CODE
                                String code = line.substring(76, 79);
                                int actualCode = Integer.parseInt(code);

                                //System.out.println("CODE: " + code);
                                boolean found;

                                if (actualCode == 11 || actualCode == 12 || actualCode == 31 || actualCode == 32 || actualCode == 151 || actualCode == 152 || actualCode == 153 || actualCode == 154 || actualCode == 252 || actualCode == 371 || actualCode == 372 || actualCode == 471 || actualCode == 651 || actualCode == 851 || actualCode == 801)
                                {
                                    found = Database.updateMutualInventory(mutID, upc, name, d, cost, false, actualCode);
                                    // System.out.println("INSERT INTO `inventory` (`pid`,`mutID`,`upc`,`name`,`price`,`cost`,`taxable`,`category`) VALUES (NULL, '"+mutID+"','"+upc+"','"+name+"',"+d+","+cost+",false,"+actualCode+");");
                                }
                                else
                                {
                                    //System.out.println("INSERT INTO `inventory` (`pid`,`mutID`,`upc`,`name`,`price`,`cost`,`taxable`,`category`) VALUES (NULL, '"+mutID+"','"+upc+"','"+name+"',"+d+","+cost+",true,"+actualCode+");");
                                    found = Database.updateMutualInventory(mutID, upc, name, d, cost, true, actualCode);
                                }
                                if (found)
                                {
                                    totalFound++;
                                    updatedCustomersLabel.setText("Mutual Items Updated: " + Integer.toString(totalFound) + "\n");
                                }
                                else
                                {
                                    totalAdded++;
                                    addedCustomersLabel.setText("Mutual Items Added: " + Integer.toString(totalAdded) + "\n");
                                }
                                processedLabel.setText("Total Mututal Items Processed: " + Integer.toString(totalCntr) + "\n");

                            }//end while
                            //System.out.println("Total Items Updated: " + totalFound);
                            //System.out.println("Total Items Added: " + totalAdded);
                            //System.out.println("Total Items Processed: " + totalCntr);
                            //JFrame message1 = new JFrame("");
                            //JOptionPane.showMessageDialog(message1, "WHOOP THERE IT IS!\nTotal Items Updated: " + totalFound + "\nTotal Items Added: " + totalAdded + "\nTotal Items Processed: " + totalCntr);
                            //progressFrame.setVisible(false);
                        }
                        catch (FileNotFoundException e)
                        {
                            System.out.println("The file could not be found or opened");
                        }
                        catch (IOException e)
                        {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Error reading the file.");
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
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

    }//end mutualFileUploadActionPerformed()

    private void ncaaReportUploadActionPerformed(java.awt.event.ActionEvent evt) {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("March Madness File", "csv");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
        int result = fileChooser.showOpenDialog(mf);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to load file data?", "WARNING",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                try
                {
                    BufferedReader in = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));

                    String line;
                    try
                    {
                        while ((line = in.readLine()) != null)
                        {
                            String[] tokens = line.split(",", 3);   //limits the split to 3 array elements ie only the first occurance so it will keep any colons in the value portion

                            if (tokens.length < 3) //not all data is there just move along
                            {
                                continue;
                            }
                            //System.out.println(Integer.parseInt(tokens[0].trim()) + " " +Integer.parseInt(tokens[1].trim()) + " " + Integer.parseInt(tokens[2].trim()) );
                            Database.updateEmployeeMarchMadnessScores(Integer.parseInt(tokens[0].trim()), Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()));

                        }
                    }
                    catch (IOException ex)
                    {
                        System.out.println("Failed to load the NCAA March Madness File 2");
                    }
                }
                catch (FileNotFoundException ex)
                {
                    System.out.println("Failed to load the NCAA March Madness File");
                }
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }

    private void editEmployeeActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField selectedEmployeePID = new JTextField();
        JTextField columnToModify = new JTextField();
        JTextField updatedValue = new JTextField();
        selectedEmployeePID.addAncestorListener(new RequestFocusListener());
        ArrayList<Employee> employees = Database.getEmployeesListSortByPID();//Format PID : NAME \n for all employees in this one String.

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        // table.setBounds(0, 0, 700, 700);
        model.addColumn("# (PID)");
        model.addColumn("A (Name)");
        model.addColumn("B (Passcode)");
        model.addColumn("C (Permission Level 1-4)");
        model.addColumn("D (RFID)");
        model.addColumn("E (C-Account)");
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setWidth(50);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setMinWidth(150);
        table.getColumnModel().getColumn(1).setWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(2).setWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setMinWidth(150);
        table.getColumnModel().getColumn(3).setWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setMinWidth(80);
        table.getColumnModel().getColumn(4).setWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setMinWidth(100);
        table.getColumnModel().getColumn(5).setWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        model.addRow(new Object[]
        {
            "# (PID)", "A (Name)", "B (Passcode)", "C (Permission Level 1-4)", "D (RFID)", "E (Patient Code)",
        });

        for (Employee employee : employees)
        {
            model.addRow(new Object[]
            {
                employee.pid, employee.name, employee.passcode, employee.permissionLevel, employee.rfid, employee.patientCode
            });
        }

        table.setEnabled(false);

        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        Object[] message =
        {
            table, "Enter Employee #", selectedEmployeePID, "\nColumn letter to change: (ex. A)\n", columnToModify, "\nNew Value: (ex. Smith,Andrew)\n", updatedValue
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Modify Employee Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            String columnValue = columnToModify.getText().toUpperCase();
            String dataValue = updatedValue.getText();
            String empNum = selectedEmployeePID.getText();

            if (!empNum.isEmpty() && mf.validateInteger(empNum))
            {
                int employeeID = Integer.parseInt(empNum);
                boolean validID = false;
                for (Employee checkEmpID : employees)
                {
                    if (checkEmpID.pid == employeeID)
                    {
                        validID = true;
                    }
                }
                if (validID)
                {
                    if (!columnValue.isEmpty() && (columnValue.contentEquals("A") || columnValue.contentEquals("B") || columnValue.contentEquals("C") || columnValue.contentEquals("D") || columnValue.contentEquals("E")))
                    {
                        if (columnValue.contentEquals("A"))//Update of Employee Name
                        {
                            if (!dataValue.isEmpty() && dataValue.matches("[A-Z]{1}[a-z]+?,[A-Z]{1}[a-z]+?"))
                            {
                                boolean exisits = false;
                                for (Employee tempEmp : employees)
                                {
                                    if (tempEmp.name.contentEquals(dataValue))
                                    {
                                        exisits = true;
                                        break;
                                    }
                                }
                                if (!exisits)
                                {
                                    //Everything checks out, store the new employee name.
                                    Database.updateEmployeeName(employeeID, dataValue);
                                }
                                else
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Error: Employee already exisits with that name.");
                                }

                            }
                            else
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Error: Employee name must be in Last,First format.");
                            }
                        }
                        else if (columnValue.contentEquals("B"))//Update of Passcode
                        {
                            if (!dataValue.isEmpty() && mf.validateInteger(dataValue) && Integer.parseInt(dataValue) > 0 && Integer.parseInt(dataValue) < 100)
                            {
                                int passcode = Integer.parseInt(dataValue);
                                boolean exisits = false;
                                for (Employee tempEmployee : employees)
                                {
                                    if (tempEmployee.passcode == passcode)
                                    {
                                        exisits = true;
                                        break;
                                    }
                                }
                                if (!exisits)
                                {//cleared, go ahead and save it.
                                    Database.updateEmployeePasscode(employeeID, passcode);
                                }
                                else
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Error: An employee already has that passcode.");
                                }

                            }
                            else
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Error: Invalid Passcode must be between 1-99.");
                            }
                        }
                        else if (columnValue.contains("C"))//update of permission level
                        {
                            if (!dataValue.isEmpty() && mf.validateInteger(dataValue) && Integer.parseInt(dataValue) >= 1 && Integer.parseInt(dataValue) <= 4)
                            {//go ahead and add it.
                                Database.updateEmployeePermissionLevel(employeeID, Integer.parseInt(dataValue));
                            }
                            else
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Error: Invalid Permission Level.");
                            }
                        }
                        else if (columnValue.contains("D"))//update of RFID
                        {
                            if (!dataValue.isEmpty() && dataValue.matches("[0-9][0-9][0-9],[0-9][0-9][0-9][0-9][0-9]"))
                            {
                                boolean exisits = false;
                                for (Employee tempEmp : employees)
                                {
                                    if (tempEmp.rfid.contentEquals(dataValue))
                                    {
                                        exisits = true;
                                    }
                                }
                                if (!exisits)
                                {
                                    Database.updateEmployeeRFID(employeeID, dataValue);
                                }
                                else
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Error: Employee RFID already exisits.");
                                }

                            }
                            else
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Error: Invalid RFID, must be in the format of ###,#####.");
                            }
                        }
                        else if (columnValue.contains("E"))//update of Patient Code
                        {
                            if (dataValue.length() <= 10)
                            {
                                Database.updateEmployeePatientCode(employeeID, dataValue.toUpperCase());
                            }
                            else
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Error: Invalid Patient Code Name, must be 10 characters or less.");
                            }
                        }
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Error: Column Value must be A,B,C,D or E.");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Employee ID not a valid ID.");
                }
            }
            else
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: Invalid Employee ID.");
            }
        }
    }//end EmployeeActionPerformed

    private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

    private void bugReportActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextArea field1 = new JTextArea(15, 40);
        JTextArea field2 = new JTextArea(15, 40);
        field1.setLineWrap(true);
        field2.setLineWrap(true);

        field1.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Description: ", field1, "Steps to Reproduce: ", field2
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Bug Report - Enter BOTH places", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field1.getText().isEmpty() && !field2.getText().isEmpty())
            {
                MailSender ms = new MailSender();
                int clerkIndex = mf.employeeSelectionHeader.getText().indexOf("Active Clerk: ") + 14;
                String activeClerk = mf.employeeSelectionHeader.getText().substring(clerkIndex);
                try
                {
                    ms.sendMail("Bug Report - " + activeClerk, "Description:\n" + field1.getText() + "\nSteps to Reproduce:\n" + field2.getText());
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Bug report submitted.");
                }
                catch (MessagingException e)
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Mail server connection failed. Report not sent.");

                }
                catch (Exception ex)
                {
                    Logger.getLogger(TopMenuBar.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }
            else
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Both boxes must have content.");
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

    }//end masterRefundActionPerformed
    //Fn to be built (below)

    private void featureRequestActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextArea field1 = new JTextArea(15, 40);
        JTextArea field2 = new JTextArea(15, 40);
        field1.setLineWrap(true);
        field2.setLineWrap(true);

        field1.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "What is the feature? ", field1, "Why is it better or needed? ", field2
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Feature Request - Enter BOTH places", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            if (!field1.getText().isEmpty() && !field2.getText().isEmpty())
            {
                MailSender ms = new MailSender();
                int clerkIndex = mf.employeeSelectionHeader.getText().indexOf("Active Clerk: ") + 14;
                String activeClerk = mf.employeeSelectionHeader.getText().substring(clerkIndex);
                try
                {
                    ms.sendMail("Feature Request - " + activeClerk, "What is the feature?\n" + field1.getText() + "\nWhy is it better or needed?\n" + field2.getText());
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Feature request submitted.");
                }
                catch (MessagingException e)
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Mail server connection failed. Report not sent.");

                }
                catch (Exception ex)
                {
                    Logger.getLogger(TopMenuBar.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }
            else
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Both boxes must have content.");
            }
        }
        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
    }//end masterRefundActionPerformed

    private void ticketReportActionPerformed(java.awt.event.ActionEvent evt) {
        DateRangeSelector drs = new DateRangeSelector();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMddyy");
        if (drs.validDates())
        {
            LocalDateTime startDate = drs.getStartDate();
            LocalDateTime endDate = drs.getEndDate();
            endDate = endDate.plusHours(23);
            endDate = endDate.plusMinutes(59);
            JFrame textInputFrame = new JFrame("");
            String id = JOptionPane.showInputDialog(
                    textInputFrame,
                    "Enter the Patient Code to continue:",
                    "Customer Patient Code Input",
                    JOptionPane.INFORMATION_MESSAGE
            );
            if (id == null)
            {
               return; //Cancel was clicked, we are done here.
            }
            else if (id.isEmpty())
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Patient Code cannot be empty.");
            }
            else
            {
                id = id.toUpperCase();
            }
            FileWriter ticketFW = null;
            BufferedWriter ticketBW = null;
            try
            {
                //All reports now loaded. Time to do the work and generate the file.
                ticketFW = new FileWriter("C:\\pos\\REPORTS\\TicketReport-" + startDate.format(dateFormat) + "_" + endDate.format(dateFormat) + ".txt");//TicketReportFile
                ticketBW = new BufferedWriter(ticketFW);
            }
            catch (IOException ex)
            {
                Logger.getLogger(TopMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArrayList<TicketLog> ticketLogs = Database.getTicketLogList(startDate, endDate, id);

            try
            {
                if(ticketLogs.isEmpty())
                {
                    ticketBW.write("No Data Found, Ticket must not have been modified or patient code entered wrong.");
                }
                else
                {
                    ticketBW.write("Ticket-Owners-Name@Ticket-Owners-Account@Modified-By-Name@Modifed-Bys-Account@Modification-Time@Register-Modified-On@Modification-Type@Item-Modified\n");
                }
                for (TicketLog ticketLog : ticketLogs)
                {
                    ticketBW.write(ticketLog.ticketOwnersName +"@"+ticketLog.ticketOwnersAccount+"@"+ticketLog.modifiedByName+"@"+ticketLog.modifiedByAccount+"@"+ticketLog.modificationTime+"@"+ticketLog.registerUsed+"@"+ticketLog.modificationType+"@"+ticketLog.itemModified + "\n");
                }
                if (ticketBW != null)
                {
                    ticketBW.close();
                }

                if (ticketFW != null)
                {
                    ticketFW.close();
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("JERE");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                System.out.println("EERE");
                e.printStackTrace();
            }

        }//end while loading reports

        mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

    }//end ticketReportActionPerformed

    private void christmasThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.makeChristmasActiveHoliday();
    }

    private void fourthThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.make4thOfJulyActiveHoliday();
    }

    private void saintPatsThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.makeSaintPatricksDayActiveHoliday();
    }

    private void thanksgivingThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.makeThanksgivingActiveHoliday();
    }

    private void easterThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.makeEasterActiveHoliday();
    }

    private void halloweenThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.makeHalloweenActiveHoliday();
    }

    private void valentinesThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.makeValentinesDayActiveHoliday();
    }

    private void summerTimeThemeActionPerformed(java.awt.event.ActionEvent evt) {
        mf.holidayLoader.makeSummerTimeActiveHoliday();
    }

    //Other action events go here.
    public void updateVisible(int permission) { //this will eventually handle responsible menu items to show or not show.

        switch (permission)
        {
            case 4:
                feedMenu.setVisible(true);//Menus visible
                addMenu.setVisible(true);
                remMenu.setVisible(true);
                mgmtMenu.setVisible(true);
                addEmployee.setVisible(true);//Only Hollie and I may add or remove employees.
                remEmployee.setVisible(true);
                drawerReports.setVisible(true);
                masterRptRecpt.setVisible(true);
                rxDataUpload.setVisible(true);
                customerDataUpload.setVisible(true);
                dmeDataUpload.setVisible(true);
                editEmployee.setVisible(true);
                arAuditReport.setVisible(true);
                ticketReport.setVisible(true);
                break;
            case 3:
                feedMenu.setVisible(true);//Menus visible
                addMenu.setVisible(true);
                addEmployee.setVisible(false);//Removed since RFID added.
                remEmployee.setVisible(false);
                remMenu.setVisible(true);
                mgmtMenu.setVisible(true);
                drawerReports.setVisible(false);
                arAuditReport.setVisible(false);
                masterRptRecpt.setVisible(true);
                rxDataUpload.setVisible(false);
                customerDataUpload.setVisible(false);
                dmeDataUpload.setVisible(true);
                editEmployee.setVisible(false);
                ticketReport.setVisible(false);
                break;
            case 2:
                feedMenu.setVisible(true);//Menus visible
                addMenu.setVisible(true);
                remMenu.setVisible(true);
                addEmployee.setVisible(false);//Removed since RFID added.
                remEmployee.setVisible(false);
                mgmtMenu.setVisible(true);
                rxDataUpload.setVisible(false);
                customerDataUpload.setVisible(false);
                dmeDataUpload.setVisible(false);
                drawerReports.setVisible(false);
                arAuditReport.setVisible(false);
                editEmployee.setVisible(false);
                ticketReport.setVisible(false);
                break;
            case 1:
                addMenu.setVisible(false);
                remMenu.setVisible(false);
                mgmtMenu.setVisible(false);
                feedMenu.setVisible(true);

                // masterRptRecpt.setVisible(false);
                break;
            case 0:
                addMenu.setVisible(false);
                remMenu.setVisible(false);
                mgmtMenu.setVisible(false);
                feedMenu.setVisible(false);
                // masterRptRecpt.setVisible(false);
                break;
            default:
                break;
        }
    }//end updateVisible()

    public void setAllVisible() {
        addMenu.setVisible(true);
        remMenu.setVisible(true);
        mgmtMenu.setVisible(true);
        feedMenu.setVisible(true);
        themeMenu.setVisible(true);
    }//end setAllVisible()

    public void setAllNotVisible() {
        addMenu.setVisible(false);
        remMenu.setVisible(false);
        mgmtMenu.setVisible(false);
        feedMenu.setVisible(false);
        themeMenu.setVisible(false);
    }//end setAllNotVisible()
}//end TopMenuBar Class
