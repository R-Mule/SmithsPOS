package database_console;

import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author hfull This menu bar spans the top of the screen and should have menu
 * items that drop down.
 */
public class TopMenuBar extends JMenuBar {

    //Class Variables
    JMenu addMenu, remMenu, dataMenu;
    JMenuItem addDmeAccount, remDmeAccount, addRxAccount, remRxAccount, addInsurance, remInsurance, addEmployee, remEmployee, addInventoryItem, remInventoryItem, dmeDataUpload, rxDataUpload;
    Database myDB;
    MainFrame mf;

    //ctor
    public TopMenuBar(Database myDB, MainFrame mf) {
        this.mf = mf;
        this.myDB = myDB;
        addMenu = new JMenu("Add");
        addMenu.setMnemonic(KeyEvent.VK_A);
        remMenu = new JMenu("Remove");
        remMenu.setMnemonic(KeyEvent.VK_R);
        //This is where Hollie added a new menu to accommodate data uploads in a separate way that seems clean...
        //If it's screwy, delete from the above comment line down to happy code
        dataMenu = new JMenu("Data");
        dataMenu.setMnemonic(KeyEvent.VK_D);

//All addition items grouped here
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
//All removal items grouped here
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
//Both data upload items grouped here
        dmeDataUpload = new JMenuItem();
        dmeDataUpload.setText("DME Data Upload");
        dataMenu.add(dmeDataUpload);//This adds DME Data Upload to Data Menu Choices

        rxDataUpload = new JMenuItem();
        rxDataUpload.setText("Rx Data Upload");
        dataMenu.add(rxDataUpload);//This adds Rx Data Upload to Data Menu Choices

        this.add(addMenu);
        this.add(remMenu);
        this.add(dataMenu);
        
//Action listeners for addition menu
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
//Action listeners for removal menu
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
        System.out.println("DME Add Account Menu Item Pressed!");
    }//end dmeAccountActionPerformed

    private void addRxAccountActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Add Rx Account Menu Item Pressed!");
    }//end addRxAccountActionPerformed

    private void addInsuranceActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Add Insurance Menu Item Pressed!");
    }//end addInsuranceActionPerformed

    private void addEmployeeActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Add Employee Menu Item Pressed!");
    }//end addEmployeeActionPerformed

    private void addInventoryItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Add Inventory Item Menu Item Pressed!");
    }//end addInventoryItemActionPerformed

//Removal menu item functions
    private void remDmeAccountActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("DME Remove Account Menu Item Pressed!");
    }//end dmeAccountActionPerformed

    private void remRxAccountActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Remove Rx Account Menu Item Pressed!");
    }//end rxAccountActionPerformed

    private void remInsuranceActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Remove Insurance Menu Item Pressed!");
    }//end InsuranceActionPerformed

    private void remEmployeeActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Remove Employee Menu Item Pressed!");
    }//end EmployeeActionPerformed

    private void remInventoryItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Remove Inventory Item Menu Item Pressed!");
    }//end rxAccountActionPerformed

    private void dmeDataUploadActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("DME Data Upload Menu Item Pressed!");
    }//end rxAccountActionPerformed

    private void rxDataUploadActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Rx Data Upload Menu Item Pressed!");
    }//end rxAccountActionPerformed

    //Other action events go here.
    public void updateVisible() {//this will eventually handle responsible menu items to show or not show.

    }//end updateVisible()

    public void setAllVisible(){
        
    }//end setAllVisible()
    
    public void setAllNotVisible(){
        
    }//end setAllNotVisible()
}//end TopMenuBar Class
