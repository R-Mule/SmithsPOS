package database_console;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author hfull This menu bar spans the top of the screen and should have menu
 * items that drop down.
 */
public class TopMenuBar extends JMenuBar {

    //Class Variables
    JMenu addMenu, remMenu, mgmtMenu, feedMenu;
    JMenuItem addDmeAccount, remDmeAccount, addRxAccount, remRxAccount, addInsurance, remInsurance,
            addEmployee, remEmployee, addInventoryItem, remInventoryItem, dmeDataUpload, rxDataUpload,
            masterRefund, masterRptRecpt, drawerReports, updatePrice, bugReport, featureRequest; //bugReport and featureRequest - Hollie's suggestions
    Database myDB;
    MainFrame mf;

    //ctor
    public TopMenuBar(Database myDB, MainFrame mf) {
        this.mf = mf;
        this.myDB = myDB;
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
        rxDataUpload.setText("Rx Data Upload");
        mgmtMenu.add(rxDataUpload);//This adds Rx Data Upload to Management Menu Choices

        masterRefund = new JMenuItem();
        masterRefund.setText("Master Refund");
        mgmtMenu.add(masterRefund);//This adds Master Refund to Management Menu Choices

        masterRptRecpt = new JMenuItem();
        masterRptRecpt.setText("Master Reprint Receipt");
        mgmtMenu.add(masterRptRecpt);//This adds Master Reprint Receipt to Management Menu Choices

        drawerReports = new JMenuItem();
        drawerReports.setText("Drawer Reports");
        mgmtMenu.add(drawerReports);//This adds Drawer Reports to Management Menu Choices

        updatePrice = new JMenuItem();
        updatePrice.setText("Update Price");
        mgmtMenu.add(updatePrice);//This adds Update Price to Management Menu Choices
//Feedback menu items
        bugReport = new JMenuItem();
        bugReport.setText("Bug Report");
        feedMenu.add(bugReport);//This adds Update Price to Management Menu Choices

        featureRequest = new JMenuItem();
        featureRequest.setText("Feature Request");
        feedMenu.add(featureRequest);//This adds Update Price to Management Menu Choices

        this.add(addMenu);
        this.add(remMenu);
        this.add(mgmtMenu);
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
        updatePrice.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePriceActionPerformed(evt);
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
    }//end ctor

    //members/functions
//Addition menu item functions
    private void addDmeAccountActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextField field3 = new JTextField();
        JTextField field4 = new JTextField();
        Object[] message = {
            "Account Name: ", field1, "First Name: ", field2, "Last Name: ", field3, "DOB: example: 111789", field4};

        field1.addAncestorListener(new RequestFocusListener());
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add DME Account Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (!mf.validateInteger(field4.getText())) {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Not a valid DOB");
            } else if (myDB.doesDMEAccountExisit(field1.getText().toUpperCase())) {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: DME Account Name already exisits!");
            } else {

                Object[] message2 = {
                    "Are you sure?\nAccount Name: " + field1.getText().toUpperCase(), "First Name: " + field2.getText().toUpperCase(), "Last Name: " + field3.getText().toUpperCase(), "DOB: example: 010520: " + field4.getText()};

                int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add DME Account Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option2 == JOptionPane.OK_OPTION) {
                    myDB.addDMEAccount(field1.getText().toUpperCase(), field2.getText().toUpperCase(), field3.getText().toUpperCase(), field4.getText());
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
        Object[] message = {
            "QS1 UUID: ", field5, "Account Name: ", field1, "First Name: ", field2, "Last Name: ", field3, "DOB: example: 030986", field4};

        field5.addAncestorListener(new RequestFocusListener());
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add RX Account Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (!mf.validateInteger(field4.getText())) {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Not a valid DOB");
            } else if (myDB.doesChargeAccountExisit(field1.getText().toUpperCase())) {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: Charge Account Name already exisits!");
            } else if (myDB.doesQS1UUIDExisit(field5.getText().toUpperCase())) {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Error: QS1 UUID already exisits!");
            } else if (field5.getText().isEmpty()) {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Must enter a UUID!");
            } else {

                Object[] message2 = {
                    "Are you sure?\nUUID: " + field5.getText().toUpperCase() + "\nAccount Name: " + field1.getText().toUpperCase(), "First Name: " + field2.getText().toUpperCase(), "Last Name: " + field3.getText().toUpperCase(), "DOB: example: 010520: " + field4.getText()};

                int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add RX Account Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option2 == JOptionPane.OK_OPTION) {
                    myDB.addChargeAccount(field1.getText().toUpperCase(), field3.getText().toUpperCase(), field2.getText().toUpperCase(), field4.getText(), field5.getText().toUpperCase());
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
        {
            JFrame textInputFrame = new JFrame("");

            JTextField field1 = new JTextField();
            field1.addAncestorListener(new RequestFocusListener());
            Object[] message = {
                "Insurance to Add:", field1};
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Insurance Menu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (!field1.getText().isEmpty()) {
                    if (myDB.doesInsuranceExisit(field1.getText().replaceAll("'", " "))) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Error: Insurance already exisits!");
                    } else {
                        myDB.addInsurance(field1.getText().replaceAll("'", " "));
                    }
                }
                /*
                JFrame textInputFrame = new JFrame("");
                JTextField field2 = new JTextField();
                field2.addAncestorListener(new RequestFocusListener());
                Object[] message = {"Insurance to Remove", field2};
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Insurance Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION)
                if (!field2.getText().isEmpty()) {
                    if (!myDB.doesInsuranceExisit(field2.getText())) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Error: No such insurance to remove!");
                    } else {
                        myDB.removeInsurance(field2.getText());
                    }
                }*/

            }
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        }

        System.out.println("Add Insurance Menu Item Pressed!");
    }//end addInsuranceActionPerformed
    //Fn to be built (below)

    private void addEmployeeActionPerformed(java.awt.event.ActionEvent evt) {
        boolean tryAgain;
        JFrame textInputFrame = new JFrame("");
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextField field3 = new JTextField();
        Object[] message = {
            "First Name: ", field1, "Last Name: ", field2, "Passcode: ", field3};
        do {
            tryAgain = false;
            field1.addAncestorListener(new RequestFocusListener());
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add New Employee Menu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (!mf.validateInteger(field3.getText())) {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Not a valid Passcode must be a whole number.");//prompt try again?
                    tryAgain = true;
                } else if (myDB.checkIfPasscodeExisits(Integer.parseInt(field3.getText()))) {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Passcode is already in use.");//prompt try again?
                    tryAgain = true;
                } else if (!field1.getText().matches("[A-Z]{1}[a-z]+?")) {//Requires Capital first letter of name, and then all lower case at least 1 lower case unknown more.
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "First Name must have capital first letter and rest all lowercase.");//prompt try again?
                    tryAgain = true;
                } else if (!field2.getText().matches("[A-Z]{1}[a-z]+?")) {//Same as above, name Validation.
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Last Name must have capital first letter and rest all lowercase.");//prompt try again?
                    tryAgain = true;
                }  
                if (tryAgain) {
                    int option2 = JOptionPane.showConfirmDialog(textInputFrame,"Would you like to try again?", "Try Again Menu", JOptionPane.YES_NO_OPTION);
                    if (option2 != JOptionPane.YES_OPTION) {
                        tryAgain=false;
                    }
                } else {

                    Object[] message2 = {
                        "Are you sure?\nFirst Name: " + field1.getText(), "Last Name: " + field2.getText(), "Passcode: " + field3.getText()};

                    int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add New Employee Menu", JOptionPane.OK_CANCEL_OPTION);
                    if (option2 == JOptionPane.OK_OPTION) {
                        String result = myDB.addEmployee(field1.getText(), field2.getText(), Integer.parseInt(field3.getText()));
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, result);
                    }
                    //FIELD1 CONTAINS DESCRIPTION
                    //FIELD2 AMOUNT
                    mf.displayChangeDue = false;
                    mf.updateCartScreen();
                }//end else
            }else{
                tryAgain=false;//Cancel pressed on first menu. Game over man.
            }//end if  
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        } while (tryAgain);
    }//end addEmployeeActionPerformed

    private void addInventoryItemActionPerformed(java.awt.event.ActionEvent evt) {
        {
            JFrame textInputFrame = new JFrame("");
            JTextField field1 = new JTextField();
            JTextField field2 = new JTextField();
            JTextField field3 = new JTextField();
            JTextField field4 = new JTextField();
            JTextField field5 = new JTextField();
            JTextField field6 = new JTextField();
            JTextField field7 = new JTextField();
            Object[] message = {
                "Name: ", field1, "ID: ", field2, "UPC: ", field3, "Cost: $", field4, "Price: $", field5, "Category: ", field6, "Is Taxed: ", field7};
            field7.setText("Yes");
            field7.setSelectionStart(0);
            field7.setSelectionEnd(4);

            field1.addAncestorListener(new RequestFocusListener());
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add Item Menu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (!mf.validateDouble(field4.getText()) || !mf.validateDouble(field5.getText()) || !mf.validateInteger(field6.getText())) {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Invalid price, cost, or category.");
                } else if (!field7.getText().toUpperCase().contentEquals("YES") && !field7.getText().toUpperCase().contentEquals("NO")) {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Must enter YES or NO for Is Taxed");
                } else if (myDB.doesItemExistByUPC(field3.getText())) {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Same UPC exists for item already.");
                } else if (myDB.doesItemExistByID(field2.getText())) {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: Same mutual ID exists for item already.");
                } else {

                    Object[] message2 = {
                        "Are you sure?\nName: " + field1.getText().replaceAll("'", " "), "ID: " + field2.getText().replaceAll("'", " "), "UPC: " + field3.getText().replaceAll("'", " "), "Cost: $ " + field4.getText(), "Price: $ " + field5.getText(), "Category: " + field6.getText(), "Is Taxed:  " + field7.getText()};

                    int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add Item Menu", JOptionPane.OK_CANCEL_OPTION);
                    if (option2 == JOptionPane.OK_OPTION) {
                        boolean taxed = false;
                        if (field7.getText().toUpperCase().contentEquals("YES")) {
                            taxed = true;
                        }
                        String upc = field3.getText();
                        if (upc.length() > 11) {
                            upc = upc.replaceAll("'", "");
                            upc = upc.substring(0, 11);
                        }
                        myDB.addItem(field2.getText().replaceAll("'", ""), upc, field1.getText().replaceAll("'", " "), Double.parseDouble(field5.getText()), Double.parseDouble(field4.getText()), taxed, Integer.parseInt(field6.getText()));
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
        }//end actionPerformed
        System.out.println("Add Inventory Item Menu Item Pressed!");
    }//end addInventoryItemActionPerformed

//Removal menu item functions
    //Fn to be built (below)
    private void remDmeAccountActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("DME Remove Account Menu Item Pressed!");
    }//end dmeAccountActionPerformed
    //Fn to be built (below)

    private void remRxAccountActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Remove Rx Account Menu Item Pressed!");
    }//end rxAccountActionPerformed

    private void remInsuranceActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame textInputFrame = new JFrame("");
        JTextField field2 = new JTextField();
        field2.addAncestorListener(new RequestFocusListener());
        Object[] message = {"Insurance to Remove", field2};
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Insurance Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (!field2.getText().isEmpty()) {
                if (!myDB.doesInsuranceExisit(field2.getText())) {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Error: No such insurance to remove!");
                } else {
                    myDB.removeInsurance(field2.getText());
                }
            }
        }
        System.out.println("Remove Insurance Menu Item Pressed!");
    }//end InsuranceActionPerformed
    //Fn to be built (below)

    private void remEmployeeActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Remove Employee Menu Item Pressed!");
    }//end EmployeeActionPerformed
    //Fn to be built (below)

    private void remInventoryItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Remove Inventory Item Menu Item Pressed!");
    }//end rxAccountActionPerformed

//Management menu items
    //Path selection to be implemented (below)
    private void dmeDataUploadActionPerformed(java.awt.event.ActionEvent evt) {
        {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
            int result = fileChooser.showOpenDialog(mf);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to load file data?", "WARNING",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    myDB.loadDMEData(selectedFile.getAbsolutePath());
                    mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
                }
            }
        }
    }//end dmeDataUploadActionPerformed    
    //Path selection to be implemented (below)

    private void rxDataUploadActionPerformed(java.awt.event.ActionEvent evt) {
        {
            {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
                int result = fileChooser.showOpenDialog(mf);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to load file data?", "WARNING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        //Hollie or Drew, do AR.
                        // String path = "C://QS1/AR.txt";
                        myDB.loadARData(selectedFile.getAbsolutePath());
                    }
                }
            }
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        }
        System.out.println("Rx Data Upload Menu Item Pressed!");
    }//end rxDataUploadActionPerformed

    private void masterRefundActionPerformed(java.awt.event.ActionEvent evt) {
        {
            JFrame textInputFrame = new JFrame("");
            JTextField field2 = new JTextField();
            JTextField field1 = new JTextField();
            field2.addAncestorListener(new RequestFocusListener());
            Object[] message = {"Description: ", field2,
                "Refund Amount: $", field1};
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Refund Amount Menu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (mf.validateDouble(field1.getText()) && field2.getText() != null && !field2.getText().isEmpty()) {

                    mf.checkout.beginMasterRefund(Double.parseDouble(field1.getText()), field2.getText());
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Success! Please give them: $" + field1.getText());
                } else {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Refund failed. Enter a description and a number please.");
                }
            }
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        }
        System.out.println("Master Refund Menu Item Pressed!");
    }//end masterRefundActionPerformed

    private void masterRptRecptActionPerformed(java.awt.event.ActionEvent evt) {
        {
            JFrame textInputFrame = new JFrame("");

            JTextField field1 = new JTextField();
            field1.addAncestorListener(new RequestFocusListener());
            Object[] message = {
                "Receipt #:", field1};
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Master Reprint Receipt Menu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (!field1.getText().isEmpty()) {
                    String receipt = myDB.getReceiptString(field1.getText());
                    if (receipt != null && !receipt.isEmpty()) {
                        mf.checkout.reprintReceipt(receipt);
                    } else {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Could not find receipt.");
                    }
                }

            }
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        }
        System.out.println("Master Reprint Receipt Menu Item Pressed!");
    }//end masterRptRecptActionPerformed

    private void drawerReportsActionPerformed(java.awt.event.ActionEvent evt) {
        {
            JFrame textInputFrame = new JFrame("");
            DrawerReport dr = null;
            JTextField field1 = new JTextField();
            field1.addAncestorListener(new RequestFocusListener());
            Object[] message = {
                "Report Date: EX. 012017D", field1};
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Report Name", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {

                try {
                    File f;
                    String path = "";
                    if (field1.getText().toUpperCase().contains("R")) {
                        f = new File("Z:\\" + field1.getText().toUpperCase() + ".posrf");
                        path = "Z:\\";
                        // System.out.println("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                    } else {
                        f = new File("Y:\\" + field1.getText().toUpperCase() + ".posrf");
                        path = "Y:\\";
                        //System.out.println("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                    }
                    if (f.exists() && !f.isDirectory()) {
                        // read object from file
                        FileInputStream fis = new FileInputStream(path + field1.getText().toUpperCase() + ".posrf");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        dr = (DrawerReport) ois.readObject();
                        dr.generateReport(field1.getText().toUpperCase());
                        ois.close();
                    } else {
                        //WRONG DOESNT EXISIT!
                    }

                    //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());
                } catch (FileNotFoundException e) {
                    System.out.println("JERE");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("EERE");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("JERsE");
                }

            }
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        }
        System.out.println("Drawer Reports Menu Item Pressed!");
    }//end DrawerReportsActionPerformed

    private void updatePriceActionPerformed(java.awt.event.ActionEvent evt) {
        {
            JFrame textInputFrame = new JFrame("");

            JTextField field1 = new JTextField();
            JTextField field2 = new JTextField();
            field1.addAncestorListener(new RequestFocusListener());
            Object[] message = {
                "Mutual ID:", field1, "New Price: $", field2};
            int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Item Info", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (mf.validateDouble(field2.getText())) {
                    myDB.updateItemPrice(field1.getText(), Double.parseDouble(field2.getText()));
                }
            }
            mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
        }
        System.out.println("Update Price Menu Item Pressed!");
    }//end updatePriceActionPerformed
//Feedback menu items
    //Fn to be built (below)

    private void bugReportActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Bug Report Menu Item Pressed!");
    }//end masterRefundActionPerformed
    //Fn to be built (below)

    private void featureRequestActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Feature Request Menu Item Pressed!");
    }//end masterRefundActionPerformed

    //Other action events go here.
    public void updateVisible(int permission) { //this will eventually handle responsible menu items to show or not show.
        switch (permission) {
            case 3:
                feedMenu.setVisible(true);//Menus visible
                addMenu.setVisible(true);
                remMenu.setVisible(true);
                mgmtMenu.setVisible(true);

                masterRptRecpt.setVisible(true);
                break;
            case 2:
                feedMenu.setVisible(true);//Menus visible
                addMenu.setVisible(true);
                remMenu.setVisible(true);
                mgmtMenu.setVisible(true);
                rxDataUpload.setVisible(false);
                dmeDataUpload.setVisible(false);
                drawerReports.setVisible(false);
                break;
            case 1:
                addMenu.setVisible(false);
                remMenu.setVisible(false);
                mgmtMenu.setVisible(false);
                feedMenu.setVisible(true);
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
    }//end setAllVisible()

    public void setAllNotVisible() {
        addMenu.setVisible(false);
        remMenu.setVisible(false);
        mgmtMenu.setVisible(false);
        feedMenu.setVisible(false);
    }//end setAllNotVisible()
}//end TopMenuBar Class
