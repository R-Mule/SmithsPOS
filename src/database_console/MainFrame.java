package database_console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 *
 * @author A.Smith
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form JFrame
     */
    public MainFrame() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //this.setUndecorated(true);
        jPanel1 = new JPanel(new FlowLayout());
        jPanel1.setBounds(100, 100, 1120, 1200);
        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setAutoscrolls(true);
        this.add(quote);
        quote.setVisible(true);
        quote.setBounds(10, 10, 1900, 50);
        JScrollPane helpSP = new JScrollPane(jPanel1,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jPanel1.setVisible(true);
        helpSP.setAutoscrolls(true);
        helpSP.setBounds(100, 100, 1120, 700);

        helpSP.setPreferredSize(new Dimension(1120, 700));
        jPanel1.setPreferredSize(new Dimension(1120, 2000));
        helpSP.setVisible(true);
        this.add(helpSP);

        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        Date date = new Date();
        previousDate = dateFormat.format(date);
        textField.setBounds(100, 800, 200, 20);
        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String myText = textField.getText();
                try {
                    if (myText.length() > 11) {
                        myText = myText.substring(0, 11);
                    }

                    Item myItem = new Item(myDB, myText);

                    if (!myItem.getID().isEmpty() && !myItem.getUPC().isEmpty()) {//then we have a real item!
                        curCart.addItem(myItem);
                        boolean exisits = false;
                        int index = 0;
                        int loc = 0;
                        for (GuiCartItem item : guiItems) {

                            if (item.getUPC().contentEquals(myItem.getUPC())) {
                                exisits = true;
                                loc = index;
                            }
                            index++;
                        }
                        if (exisits) {
                            guiItems.get(loc).updateQuantityLabelAmount();
                        } else {
                            guiItems.add(new GuiCartItem(myItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                        }

                        displayChangeDue = false;
                        updateCartScreen();
                    }//end if
                    textField.setText("");
                } catch (StringIndexOutOfBoundsException e) {
                    textField.setText("");
                }//end catch

            }//end actionPerformed
        });
        this.add(textField);
        this.setVisible(true);

        //HEADERS FOR ITEMS
        //TotalNumRXinCart
        // JLabel totalNumRXinCart = new JLabel("# of Rx's in Cart: 0", SwingConstants.RIGHT);
        totalNumRXinCart.setLocation(250, 780);
        totalNumRXinCart.setSize(250, 50);
        totalNumRXinCart.setFont(new Font(employeeCheckoutHeader.getName(), Font.BOLD, 12));
        totalNumRXinCart.setVisible(true);
        //Employee Checkout:
        employeeCheckoutHeader.setLocation(15, 910);
        employeeCheckoutHeader.setSize(250, 50);
        employeeCheckoutHeader.setFont(new Font(employeeCheckoutHeader.getName(), Font.BOLD, 12));
        employeeCheckoutHeader.setVisible(true);
        //"Item Name: "
        itemNameHeader.setLocation(0, 50);
        itemNameHeader.setSize(250, 50);
        itemNameHeader.setFont(new Font(itemNameHeader.getName(), Font.BOLD, 12));
        itemNameHeader.setVisible(true);
        //"Item Quantity: "
        itemQuantityHeader.setLocation(-95, 50);
        itemQuantityHeader.setSize(250, 50);
        itemQuantityHeader.setFont(new Font(itemQuantityHeader.getName(), Font.BOLD, 12));
        itemQuantityHeader.setVisible(true);
        //"Price Per Item: "
        itemPriceHeader.setLocation(400, 50);
        itemPriceHeader.setSize(250, 50);
        itemPriceHeader.setFont(new Font(itemPriceHeader.getName(), Font.BOLD, 12));
        itemPriceHeader.setVisible(true);
        discountHeader.setLocation(625, 50);
        discountHeader.setSize(250, 50);
        discountHeader.setFont(new Font(discountHeader.getName(), Font.BOLD, 12));
        discountHeader.setVisible(true);
        //"Total: "
        itemSubTotalHeader.setLocation(830, 50);
        itemSubTotalHeader.setSize(250, 50);
        itemSubTotalHeader.setFont(new Font(itemSubTotalHeader.getName(), Font.BOLD, 12));
        itemSubTotalHeader.setVisible(true);
        //"Item(s) Tax: "
        itemTaxTotalHeader.setLocation(750, 50);
        itemTaxTotalHeader.setSize(250, 50);
        itemTaxTotalHeader.setFont(new Font(itemTaxTotalHeader.getName(), Font.BOLD, 12));
        itemTaxTotalHeader.setVisible(true);
        //"Price of Item(s):  "
        subTotalHeader.setLocation(540, 50);
        subTotalHeader.setSize(250, 50);
        subTotalHeader.setFont(new Font(subTotalHeader.getName(), Font.BOLD, 12));
        subTotalHeader.setVisible(true);

        this.add(employeeCheckoutHeader);
        employeeCheckoutHeader.setVisible(true);
        this.add(discountHeader);
        discountHeader.setVisible(true);
        this.add(itemPriceHeader);
        itemPriceHeader.setVisible(true);
        this.add(itemSubTotalHeader);
        itemSubTotalHeader.setVisible(true);
        this.add(itemTaxTotalHeader);
        itemTaxTotalHeader.setVisible(true);
        this.add(itemNameHeader);
        itemNameHeader.setVisible(true);
        this.add(itemQuantityHeader);
        itemQuantityHeader.setVisible(true);
        this.add(subTotalHeader);
        subTotalHeader.setVisible(true);
        this.add(totalNumRXinCart);
        totalNumRXinCart.setVisible(true);

        //This creates the two Load and Save Buttons
        createTicket.setLocation(1800, 350);
        createTicket.setSize(100, 100);
        createTicket.setBackground(new Color(0, 255, 0));
        loadTicket.setLocation(1800, 250);
        loadTicket.setSize(100, 100);
        loadTicket.setBackground(new Color(255, 255, 0));
        //this creates the lookupReceiptByRXButton
        lookupReceiptByRXButton.setLocation(1800, 450);
        lookupReceiptByRXButton.setSize(100, 100);
        lookupReceiptByRXButton.setBackground(new Color(255, 200, 100));

        createTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty()) {
                    JFrame textInputFrame = new JFrame("");
                    String id = JOptionPane.showInputDialog(
                            textInputFrame,
                            "Enter the customer account name to continue:",
                            "Customer Account Name Input",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    if (id != null && !id.isEmpty()) {
                        id = id.toUpperCase();
                        if (!myDB.checkDatabaseForTicket(id)) {//check ID to see if it exists in database
                            //if it doesnt, lets create it!
                            for (GuiCartItem item : guiItems) {
                                item.removeAllGUIData();
                            }
                            guiItems.clear();
                            curCart.storeCart(id, myDB);
                            resetVars();

                        } else {
                            //if it does, send error message!
                            JFrame message2 = new JFrame("");
                            JOptionPane.showMessageDialog(message2, "There are already items in ticket for customer. Maybe load those?");
                        }//end else already items in tickets
                    }//end string null or empty part, didnt do an else, we just do nothing.
                } else {//CARTS EMPTY!
                    //notify nothing to save
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "There is nothing in the cart to save!");
                }//end else its empty

                updateCartScreen();
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//END createTicket ActionListener!

        loadTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //System.out.println("HELLO");
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();

                Object[] message = {
                    "Enter the customer account name to continue:", field1};
                field1.setText("");
                field1.addAncestorListener(new RequestFocusListener());

                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Ticket Name", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String id = field1.getText();
                    id = id.toUpperCase();
                    if (id != null && !id.isEmpty() && myDB.checkDatabaseForTicket(id)) {
                        loadTicketWithId(id);
                        updateCartScreen();
                    } else {//Load All Tickets into selectable GUI
                        //Sorry no such ticket found
                        String[] choices = myDB.getAllTicketsNames();
                        //Object[] message2 = {
                        //    "Select Ticket To Load:", choices, choices[0]};
                        if (choices != null && choices.length > 0) {
                            
                            id = (String) JOptionPane.showInputDialog(null, "Couldn't find that ticket? Check these?...",
                                    "Choose Ticket", JOptionPane.QUESTION_MESSAGE, null, // Use
                                    // default
                                    // icon
                                    choices, // Array of choices
                                    choices[0]); // Initial choice
                            if (id != null) {
                                loadTicketWithId(id);
                                updateCartScreen();
                            } //end if
                        }//end if
                    }//end else

                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed loadTicket
        });//end loadTicket action

        lookupReceiptByRXButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //System.out.println("HELLO");
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();

                Object[] message = {
                    "Enter RX Number to find filename:", field1};
                field1.setText("");
                field1.addAncestorListener(new RequestFocusListener());

                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter RX Number", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String id = field1.getText();

                    if (id != null && !id.isEmpty() && validateInteger(id)) {
                        int rxNumber = Integer.parseInt(id);
                        String[] choices = myDB.lookupReceiptByRX(rxNumber);
                        if (choices != null && choices.length > 0) {
                            Object[] message2 = {
                                "Here is what I found:", choices, choices[0]};

                            id = (String) JOptionPane.showInputDialog(null, "Results",
                                    "Here is what I found:", JOptionPane.QUESTION_MESSAGE, null, // Use
                                    // default
                                    // icon
                                    choices, // Array of choices
                                    choices[0]); // Initial choice
                            if (Desktop.isDesktopSupported()) {
                                try {
                                    if (id != null && !id.isEmpty()) {
                                        String path = reader.getRemoteDrivePath();
                                        File myFile = new File(path + id.substring(0, 2) + id.substring(4, 6) + "\\" + id + ".pdf");
                                        Desktop.getDesktop().open(myFile);
                                    }
                                } catch (IOException ex) {
                                    // no application registered for PDFs
                                }
                            }
                        } else {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "No data for that RX Number.");
                        }
                    }

                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed loadTicket
        });//end lookupReceipt action

        createTicket.setVisible(true);
        loadTicket.setVisible(true);
        this.add(createTicket);
        this.add(loadTicket);
        lookupReceiptByRXButton.setVisible(true);
        this.add(lookupReceiptByRXButton);

        //This creates the RX Item Button
        rxButton.setLocation(1300, 100);
        rxButton.setSize(100, 100);
        rxButton.setBackground(new Color(0, 255, 255));
        //This creates the OTC Item Button
        otcButton.setLocation(1400, 100);
        otcButton.setSize(100, 100);
        otcButton.setBackground(new Color(238, 130, 238));
        //This creates the OTC Item Button
        paperButton.setLocation(1500, 100);
        paperButton.setSize(100, 100);
        paperButton.setBackground(new Color(179, 179, 179));
        //This creates the DME Payment Button
        dmePaymentButton.setLocation(1700, 100);
        dmePaymentButton.setSize(100, 100);
        dmePaymentButton.setBackground(new Color(255, 143, 137));
        //This creates the Void Item Button
        voidButton.setLocation(1300, 200);
        voidButton.setSize(100, 100);
        voidButton.setBackground(new Color(255, 0, 0));
        //This creates the cashCheckout Button
        cashButton.setLocation(1300, 700);
        cashButton.setSize(100, 100);
        cashButton.setBackground(new Color(104, 255, 4));
        //this creates the check Checkout Button
        checkButton.setLocation(1400, 700);
        checkButton.setSize(100, 100);
        checkButton.setBackground(new Color(195, 145, 255));
        //this creates the Credit Checkout Button
        creditButton.setLocation(1500, 700);
        creditButton.setSize(100, 100);
        creditButton.setBackground(new Color(255, 90, 170));
        //This creates the reprint recript button
        reprintReceiptButton.setLocation(1500, 400);
        reprintReceiptButton.setSize(100, 100);
        reprintReceiptButton.setLayout(new BorderLayout());
        reprintReceiptButton.setBackground(new Color(255, 74, 249));
        //This creates the noSale button
        noSaleButton.setLocation(1400, 400);
        noSaleButton.setSize(100, 100);
        noSaleButton.setLayout(new BorderLayout());
        noSaleButton.setBackground(new Color(255, 166, 118));
        //This creates the refund button
        refundButton.setLocation(1300, 400);
        refundButton.setSize(100, 100);
        refundButton.setLayout(new BorderLayout());
        refundButton.setBackground(new Color(255, 235, 82));
        //This creates the cancelRefundButton
        cancelRefundButton.setLocation(1300, 200);
        cancelRefundButton.setSize(100, 100);
        cancelRefundButton.setBackground(new Color(255, 0, 0));
        //this creates the AR Payment Button
        arPaymentButton.setLocation(1600, 100);
        arPaymentButton.setSize(100, 100);
        arPaymentButton.setBackground(new Color(95, 255, 188));
        //this creates the Paid Out Button
        paidOutButton.setLocation(1600, 400);
        paidOutButton.setSize(100, 100);
        paidOutButton.setBackground(new Color(88, 199, 255));
        //this creates the SPlit Tender Button
        splitTenderButton.setLocation(1600, 700);
        splitTenderButton.setSize(100, 100);
        splitTenderButton.setBackground(new Color(24, 240, 255));
        //this creates the Charge Button
        chargeButton.setLocation(1700, 700);
        chargeButton.setSize(100, 100);
        chargeButton.setBackground(new Color(255, 126, 21));
        //This creates the Mass Discount Button
        massDiscountButton.setLocation(875, 65);
        massDiscountButton.setSize(20, 20);
        massDiscountButton.setBackground(new Color(255, 255, 100));
        //This creates the UpdatePrice Button
        updatePriceButton.setLocation(350, 850);
        updatePriceButton.setSize(150, 40);
        updatePriceButton.setBackground(new Color(255, 0, 0));
        //This creates the masterRefundButton
        masterRefundButton.setLocation(650, 850);
        masterRefundButton.setSize(150, 40);
        masterRefundButton.setBackground(new Color(255, 255, 0));
        //This creates the GenerateReportButton
        generateReportButton.setLocation(500, 850);
        generateReportButton.setSize(150, 40);
        generateReportButton.setBackground(new Color(0, 255, 0));

        //This creates the addNewItemButton 
        addNewItemButton.setLocation(350, 890);
        addNewItemButton.setSize(150, 40);
        addNewItemButton.setBackground(new Color(0, 255, 0));
        //This creates the addRxAccountButton 
        addRxAccountButton.setLocation(350, 930);
        addRxAccountButton.setSize(150, 40);
        addRxAccountButton.setBackground(new Color(200, 200, 255));

        //This creates the addRxAccountButton 
        addDmeAccountButton.setLocation(500, 930);
        addDmeAccountButton.setSize(150, 40);
        addDmeAccountButton.setBackground(new Color(255, 200, 200));

        //This creates the activateDisplayButton 
        activateDisplayButton.setLocation(500, 890);
        activateDisplayButton.setSize(150, 40);
        activateDisplayButton.setBackground(new Color(50, 255, 255));
         
                
        activateDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                display = new PoleDisplay(reader);
                curCart.setDisplay(display);
                refundCart.setDisplay(display);
                curCart.updateTotal();
               updateCartScreen();
               displayActive=true; 
               activateDisplayButton.setVisible(false);
            }});
        
        addRxAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                JTextField field3 = new JTextField();
                JTextField field4 = new JTextField();
                Object[] message = {
                    "Account Name: ", field1, "First Name: ", field2, "Last Name: ", field3, "DOB: example: 010520", field4};

                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add RX Account Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (!validateInteger(field4.getText())) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Not a valid DOB");
                    } else {

                        Object[] message2 = {
                            "Are you sure?\nAccount Name: " + field1.getText().toUpperCase(), "First Name: " + field2.getText().toUpperCase(), "Last Name: " + field3.getText().toUpperCase(), "DOB: example: 010520: " + field4.getText()};

                        int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add RX Account Menu", JOptionPane.OK_CANCEL_OPTION);
                        if (option2 == JOptionPane.OK_OPTION) {
                            myDB.addChargeAccount(field1.getText().toUpperCase(), field3.getText().toUpperCase(), field2.getText().toUpperCase(), field4.getText());
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Success!");
                        }
                        //FIELD1 CONTAINS DESCRIPTION
                        //FIELD2 AMOUNT
                        displayChangeDue = false;
                        updateCartScreen();
                    }//end else
                }//end if  
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end addRxAccountAction

        addDmeAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                JTextField field3 = new JTextField();
                JTextField field4 = new JTextField();
                Object[] message = {
                    "Account Name: ", field1, "First Name: ", field2, "Last Name: ", field3, "DOB: example: 010520", field4};

                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Add DME Account Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (!validateInteger(field4.getText())) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Not a valid DOB");
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
                        displayChangeDue = false;
                        updateCartScreen();
                    }//end else
                }//end if  
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end addDMEAccountAction

        addNewItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
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
                    if (!validateDouble(field4.getText()) || !validateDouble(field5.getText()) || !validateInteger(field6.getText())) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Invalid price, cost, or category.");
                    } else if (!field7.getText().toUpperCase().contentEquals("YES") && !field7.getText().toUpperCase().contentEquals("NO")) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Must enter YES or NO for Is Taxed");
                    } else {

                        Object[] message2 = {
                            "Are you sure?\nName: " + field1.getText(), "ID: " + field2.getText(), "UPC: $ " + field3.getText(), "Cost: $ " + field4.getText(), "Price: $ " + field5.getText(), "Category: " + field6.getText(), "Is Taxed:  " + field7.getText()};

                        int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Add Item Menu", JOptionPane.OK_CANCEL_OPTION);
                        if (option2 == JOptionPane.OK_OPTION) {
                            boolean taxed = false;
                            if (field7.getText().toUpperCase().contentEquals("YES")) {
                                taxed = true;
                            }
                            String upc = field3.getText();
                            if (upc.length() > 11) {
                                upc = upc.substring(0, 11);
                            }
                            myDB.addItem(field2.getText(), upc, field1.getText(), Double.parseDouble(field5.getText()), Double.parseDouble(field4.getText()), taxed, Integer.parseInt(field6.getText()));
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Success!");
                        }
                        //FIELD1 CONTAINS DESCRIPTION
                        //FIELD2 AMOUNT
                        displayChangeDue = false;
                        updateCartScreen();
                    }//end else
                }//end if  
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end addNewItemButtonAction

        generateReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                DrawerReport dr = null;
                JTextField field1 = new JTextField();
                field1.addAncestorListener(new RequestFocusListener());
                Object[] message = {
                    "Report Date: EX. 012017D", field1};
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Report Name", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {

                    try {

                        File f = new File("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                        System.out.println("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
                        if (f.exists() && !f.isDirectory()) {
                            // read object from file
                            FileInputStream fis = new FileInputStream("\\\\Pos-server\\pos\\REPORTS\\" + field1.getText().toUpperCase() + ".posrf");
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
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        });
        masterRefundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");

                JTextField field1 = new JTextField();
                field1.addAncestorListener(new RequestFocusListener());
                Object[] message = {
                     "Refund Amount: $", field1};
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Refund Amount Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (validateDouble(field1.getText())) {
                                
                         checkout.beginMasterRefund(Double.parseDouble(field1.getText()));
                         JFrame message1 = new JFrame("");
                         JOptionPane.showMessageDialog(message1, "Success! Please give them: $"+field1.getText());
                    }
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }});
        
        updatePriceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");

                JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                field1.addAncestorListener(new RequestFocusListener());
                Object[] message = {
                    "Mutual ID:", field1, "New Price: $", field2};
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Item Info", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (validateDouble(field2.getText())) {
                        myDB.updateItemPrice(field1.getText(), Double.parseDouble(field2.getText()));
                    }
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        });

        paperButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Item myItem = new Item(myDB, "NEWSPAPER");

                if (!myItem.getID().isEmpty() && !myItem.getUPC().isEmpty()) {//then we have a real item!
                    curCart.addItem(myItem);
                    boolean exisits = false;
                    int index = 0;
                    int loc = 0;
                    for (GuiCartItem item : guiItems) {

                        if (item.getUPC().contentEquals(myItem.getUPC())) {
                            exisits = true;
                            loc = index;
                        }
                        index++;
                    }
                    if (exisits) {
                        guiItems.get(loc).updateQuantityLabelAmount();
                    } else {
                        guiItems.add(new GuiCartItem(myItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                    }

                    displayChangeDue = false;
                    updateCartScreen();
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });

        refundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //System.out.println("HELLO");
                if (curCart.isEmpty()) {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();

                    Object[] message = {
                        "Enter receipt number:", field1};
                    field1.setText("");
                    field1.addAncestorListener(new RequestFocusListener());

                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Receipt Number", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String receiptNum = field1.getText();
                        receiptNum = receiptNum.toUpperCase();
                        if (receiptNum != null && !receiptNum.isEmpty()) {
                            myDB.loadReceipt(receiptNum);
                            loadReceipt(receiptNum);
                            if (!guiRefundItems.isEmpty()) {
                                //Time to hide some buttons...
                                dmePaymentButton.setVisible(false);
                                rxButton.setVisible(false);
                                otcButton.setVisible(false);
                                arPaymentButton.setVisible(false);
                                splitTenderButton.setVisible(false);
                                chargeButton.setVisible(false);
                                voidButton.setVisible(false);
                                noSaleButton.setVisible(false);
                                loadTicket.setVisible(false);
                                createTicket.setVisible(false);
                                checkButton.setVisible(false);
                                paidOutButton.setVisible(false);
                                refundButton.setVisible(false);
                                lookupReceiptByRXButton.setVisible(false);
                                massDiscountButton.setVisible(false);
                                reprintReceiptButton.setVisible(false);
                                paperButton.setVisible(false);
                                generateReportButton.setVisible(false);
                                updatePriceButton.setVisible(false);
                                addNewItemButton.setVisible(false);
                                masterRefundButton.setVisible(false);
                                addRxAccountButton.setVisible(false);
                                addDmeAccountButton.setVisible(false);
                                activateDisplayButton.setVisible(false);
                                cancelRefundButton.setVisible(true);
                                updateCartScreen();
                            }
                        } else {//Load All Tickets into selectable GUI
                            //Sorry no such receipt found
                        }//end else

                    }
                    textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
                }
            }//end actionPerformed refundButton
        });//end refundButton action

        massDiscountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty()) {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field1.setSelectionStart(0);
                            field1.setSelectionEnd(12);
                            if (!validateInteger(field1.getText())) {
                                field1.setText("0");
                            }//end if
                        }//end focusGained
                    });
                    Object[] message = {
                        "Discount Percentage: %", field1};
                    field1.setText("0");
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(2);

                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Mass Discount Menu", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!validateInteger(field1.getText())) {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Improper discount percentage.");
                        } else {
                            double discPer = Double.parseDouble(field1.getText());
                            discPer /= 100;//move decimal 2 places to get percentage
                            //discPer = discPer;
                            if (discPer > 1 || discPer < 0) {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Not a valid discount amount.");
                            } else {
                                System.out.println(discPer);
                                curCart.setMassDiscount(discPer);
                                for (GuiCartItem tempItem : guiItems) {
                                    tempItem.setMassDiscount();
                                }
                                updateCartScreen();
                            }//end else
                        }//end else
                    }//end if
                }//end cartIsNotEmpty
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end massDiscountAction
        AbstractAction rxButtonAA = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();
                // JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                JTextField field3 = new JTextField();

                field2.setText(previousDate);
                String[] possibilities = {"AARP", "Aetna", "Amerigroup", "Anthem", "Caremark", "Cash", "Catalyst", "Champva", "Chips", "Cigna", "Envision", "Express Scripts","Healthgram", "Highmark BCBS", "Hospice", "Humana", "Medco","Medco Medicare","Megellan", "Optum RX", "Prime", "Savings Voucher", "Silverscript", "WV Medicaid", "VA Medicaid"};
                JList list = new JList(possibilities); //data has type Object[]
                list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                list.setLayoutOrientation(JList.VERTICAL_WRAP);
                list.setBounds(100, 50, 50, 100);
                list.setVisibleRowCount(-1);

                for (int i = 0; i < possibilities.length; i++) {
                    if (previousInsurance.contentEquals(possibilities[i])) {
                        list.setSelectedIndex(i);
                    }//end if
                }//end for

                JScrollPane listScroller = new JScrollPane(list);
                listScroller.setPreferredSize(new Dimension(250, 80));
                Object[] message = {
                    "RX Number:", field1,
                    "Copay:", field3,
                    "Fill Date:", field2,
                    "Insurance:", list};
                field3.setText("0.00");
                field2.setSelectionStart(0);
                field2.setSelectionEnd(6);
                field3.setSelectionStart(0);
                field3.setSelectionEnd(4);
                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "RX Information", JOptionPane.OK_CANCEL_OPTION);
                //field1.requestFocusInWindow();

                if (option == JOptionPane.OK_OPTION) {
                    int rxNumber;
                    String fillDate;
                    try {
                        String insurance = (String) list.getSelectedValue();
                        rxNumber = Integer.parseInt(field1.getText());
                        int length = (int) (Math.log10(rxNumber) + 1);
                        if (!validateRX(length, rxNumber, insurance)) {//invalid RXNumber
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Invalid RX Number");
                        } else {
                            fillDate = field2.getText();
                            if (!validateDate(fillDate)) {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Invalid Fill Date");
                            } else {
                                String temp = field3.getText();
                                if (!validateDouble(temp)) {//check for copay
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Invalid Copay");
                                } else {//else everything checks out! WE HAVE ALL GOOD DATA!!!
                                    double copay = Double.parseDouble(temp);
                                    Item tempItem = new Item(myDB, rxNumber, fillDate, insurance, copay, false);
                                    curCart.addItem(tempItem);
                                    guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                    totalNumRXinCart.setText("# of Rx's in Cart: " + curCart.getTotalNumRX());
                                    displayChangeDue = false;
                                    previousInsurance = insurance;
                                    previousDate = fillDate;
                                }
                            }//end else valid fillDate
                        }//end else valid RXNumber
                    } catch (NumberFormatException e) {
                        //If number is not number for RX, print error msg.
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Invalid RX Number");
                    }//end catch

                }//end if
                updateCartScreen();
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        };
        AbstractAction otcButtonAA = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                JTextField field3 = new JTextField();
                field2.setText(previousDate);
                Object[] message = {
                    "Item Name:", field1,
                    "Cost:", field2,
                    "Price:", field3};
                field1.setText("");
                field2.setText("0.00");
                field3.setText("");
                field2.setSelectionStart(0);
                field2.setSelectionEnd(4);
                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "OTC Item Information", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (field1.getText().isEmpty()) {
                        //must have some name
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Item must have a name.");
                    } else {//item has a name
                        if (!validateDouble(field2.getText())) {//check cost
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Cost is invalid.");
                        } else {//cost is good
                            if (!validateDouble(field3.getText())) {//check price
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Price is invalid.");
                            } else {//price is good
                                // randomItemCntr++;
                                DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
                                Date date = new Date();
                                String tempID;
                                tempID = dateFormat.format(date);
                                System.out.println(tempID);
                                String upc = 'T' + tempID;
                                Item tempItem = new Item(myDB, tempID, upc, field1.getText(), Double.parseDouble(field3.getText()), Double.parseDouble(field2.getText()), true, 852, 0, "", "", 1, false, 0, false);
                                curCart.addItem(tempItem);
                                guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                displayChangeDue = false;
                            }//end else
                        }//end else
                    }//end else
                }//end if
                updateCartScreen();
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        };//end otcButtonAction

        noSaleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                checkout.beginNoSaleCheckout((String) empList.getSelectedItem());
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end NoSaleAction
        splitTenderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty()) {
                    JLabel splitTenderTotal = new JLabel("Total: $", SwingConstants.RIGHT);
                    JLabel splitTenderRemaining = new JLabel("Remaining: $", SwingConstants.RIGHT);
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();
                    JTextField field4 = new JTextField();
                    JTextField field5 = new JTextField();
                    JTextField field6 = new JTextField();
                    JTextField field7 = new JTextField();

                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            field1.setSelectionStart(0);
                            field1.setSelectionEnd(12);
                        }//end focusGained

                        public void focusLost(java.awt.event.FocusEvent evt) {
                            //this will be called on tab i.e when the field looses focus
                            if (validateDouble(field1.getText())) {
                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText()) - Double.parseDouble(field7.getText());
                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                            } else {
                                field1.setText("0.00");
                            }//end else
                        }//end focusLost
                    });//end field1Listener

                    field2.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            field2.setSelectionStart(0);
                            field2.setSelectionEnd(12);
                        }//end focusGained

                        public void focusLost(java.awt.event.FocusEvent evt) {
                            //this will be called on tab i.e when the field looses focus
                            if (validateDouble(field2.getText())) {
                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText()) - Double.parseDouble(field7.getText());
                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                            } else {
                                field2.setText("0.00");
                            }//end else
                        }//end focusLost
                    });//end field2Listener

                    field3.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            field3.setSelectionStart(0);
                            field3.setSelectionEnd(12);
                        }//end focusGained

                        public void focusLost(java.awt.event.FocusEvent evt) {
                            //this will be called on tab i.e when the field looses focus
                            if (validateInteger(field3.getText())) {
                                //do nothing? its a valid check number
                            } else {
                                field3.setText("0");
                            }//end else
                        }//end focusLost
                    });//end field3Listener

                    field4.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            field4.setSelectionStart(0);
                            field4.setSelectionEnd(12);
                        }//end focusGained

                        public void focusLost(java.awt.event.FocusEvent evt) {
                            //this will be called on tab i.e when the field looses focus
                            if (validateDouble(field4.getText())) {
                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText()) - Double.parseDouble(field7.getText());
                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                            } else {
                                field4.setText("0.00");
                            }//end else
                        }//end focusLost
                    });//end field4Listener

                    field5.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            field5.setSelectionStart(0);
                            field5.setSelectionEnd(12);
                        }//end focusGained

                        public void focusLost(java.awt.event.FocusEvent evt) {
                            //this will be called on tab i.e when the field looses focus
                            if (validateInteger(field5.getText())) {
                                //do nothing? its a valid check number
                            } else {
                                field5.setText("0");
                            }//end else
                        }//end focusLost
                    });//end field5Listener

                    field6.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            field6.setSelectionStart(0);
                            field6.setSelectionEnd(12);
                        }//end focusGained

                        public void focusLost(java.awt.event.FocusEvent evt) {
                            //this will be called on tab i.e when the field looses focus
                            if (validateDouble(field6.getText())) {
                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText()) - Double.parseDouble(field7.getText());
                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                            } else {
                                field6.setText("0.00");
                            }//end else
                        }//end focusLost
                    });//end field6Listener

                    field7.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            field7.setSelectionStart(0);
                            field7.setSelectionEnd(12);
                        }//end focusGained

                        public void focusLost(java.awt.event.FocusEvent evt) {
                            //this will be called on tab i.e when the field looses focus
                            if (validateDouble(field7.getText())) {
                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText()) - Double.parseDouble(field7.getText());
                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                            } else {
                                field7.setText("0.00");
                            }//end else
                        }//end focusLost
                    });//end field7Listener

                    splitTenderTotal.setText("Total: $ " + String.format("%.2f", curCart.getTotalPrice()));
                    splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", curCart.getTotalPrice()));
                    field2.setText(previousDate);
                    Object[] message = {
                        "Cash:", field1,
                        "Check 1:", field2,
                        "Check 1#:", field3,
                        "Check 2:", field4,
                        "Check 2#:", field5,
                        "Credit 1:", field6,
                        "Credit 2:", field7,
                        splitTenderRemaining, splitTenderTotal};
                    field1.setText("0.00");
                    field2.setText("0.00");
                    field3.setText("0");
                    field4.setText("0.00");
                    field5.setText("0");
                    field6.setText("0.00");
                    field7.setText("0.00");
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(4);
                    field2.setSelectionStart(0);
                    field2.setSelectionEnd(4);
                    field3.setSelectionStart(0);
                    field3.setSelectionEnd(4);
                    field4.setSelectionStart(0);
                    field4.setSelectionEnd(4);
                    field5.setSelectionStart(0);
                    field5.setSelectionEnd(4);
                    field6.setSelectionStart(0);
                    field6.setSelectionEnd(4);
                    field7.setSelectionStart(0);
                    field7.setSelectionEnd(4);

                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Split Tender Menu", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!validateDouble(field4.getText()) || !validateDouble(field2.getText()) || !validateDouble(field1.getText()) || !validateDouble(field6.getText()) || !validateDouble(field6.getText()) || !validateInteger(field3.getText()) || !validateInteger(field5.getText())) {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Improper text in fields.");
                        } else {
                            double remaining = Double.parseDouble(field7.getText()) + Double.parseDouble(field6.getText()) + Double.parseDouble(field4.getText()) + Double.parseDouble(field2.getText()) + Double.parseDouble(field1.getText());
                            remaining = round(remaining);
                            // System.out.println(remaining);
                            // System.out.println(curCart.getTotalPrice());
                            if (remaining < curCart.getTotalPrice()) {
                                //must have some name
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Totals do not match.");
                            } else {
                                //JFrame message1 = new JFrame("");
                                double amtReceived = Double.parseDouble(field1.getText()) + Double.parseDouble(field2.getText()) + Double.parseDouble(field4.getText()) + Double.parseDouble(field6.getText()) + Double.parseDouble(field7.getText());
                                amtReceived = round(amtReceived);
                                double change = amtReceived - curCart.getTotalPrice();
                                change = round(change);
                                changeDue.setText("Change Due: $" + String.format("%.2f", change));
                                checkout.beginSplitTenderCheckout(curCart, Double.parseDouble(field1.getText()), Double.parseDouble(field6.getText()), Double.parseDouble(field7.getText()), Double.parseDouble(field2.getText()), Double.parseDouble(field4.getText()), Integer.parseInt(field3.getText()), Integer.parseInt(field5.getText()), (String) empList.getSelectedItem(), guiItems, myself);
                                displayChangeDue = true;
                                updateCartScreen();
                            }//end else
                        }//end else
                    }//end if
                }//end if isNotEmpty
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//end splitTenderButtonAction

        voidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                voidCarts();
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end voidButtonAction

        cashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty()) {
                    JFrame textInputFrame = new JFrame("");
                    JLabel cashTotal = new JLabel("Total: $", SwingConstants.RIGHT);
                    JTextField field1 = new JTextField();
                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field1.setSelectionStart(0);
                            field1.setSelectionEnd(12);
                            if (!validateDouble(field1.getText())) {
                                field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                            }//end if
                        }//end focusGained
                    });
                    cashTotal.setText("Total: $ " + String.format("%.2f", curCart.getTotalPrice()));
                    Object[] message = {
                        "Cash Amount:", field1, cashTotal};
                    field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(8);

                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Cash Amount", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!validateDouble(field1.getText())) {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Improper cash value.");
                        } else {
                            double amtReceived = Double.parseDouble(field1.getText());
                            amtReceived = round(amtReceived);

                            // System.out.println(remaining);
                            // System.out.println(curCart.getTotalPrice());
                            if (amtReceived < curCart.getTotalPrice()) {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Not enough cash.");
                            } else {

                                double change = amtReceived - curCart.getTotalPrice();
                                change = round(change);
                                changeDue.setText("Change Due: $" + String.format("%.2f", change));
                                displayChangeDue = true;
                                checkout.beginCashCheckout(curCart, amtReceived, (String) empList.getSelectedItem(), guiItems, myself);
                                updateCartScreen();

                            }//end else
                        }//end else
                    }//end if
                } else if (!refundCart.isEmpty()) {
                    boolean isItemToRefund = false;
                    for (RefundItem item : refundCart.getRefundItems()) {
                        if (item.isRefundAllActive() || item.isRefundTaxOnlyActive()) {
                            isItemToRefund = true;
                        }
                    }
                    if (isItemToRefund) {
                        checkout.beginRefundCashCheckout(refundCart, (String) empList.getSelectedItem(), guiRefundItems, myself);

                    }

                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//end cashButtonAction

        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty()) {
                    JFrame textInputFrame = new JFrame("");
                    JLabel checkTotal = new JLabel("Total: $", SwingConstants.RIGHT);
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field1.setSelectionStart(0);
                            field1.setSelectionEnd(12);
                            if (!validateDouble(field1.getText())) {
                                field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                            }//end if
                        }//end focusGained
                    });
                    field2.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field2.setSelectionStart(0);
                            field2.setSelectionEnd(12);
                            if (!validateInteger(field2.getText())) {
                                field2.setText(String.format("0"));
                            }//end if
                        }//end focusGained
                    });
                    checkTotal.setText("Total: $ " + String.format("%.2f", curCart.getTotalPrice()));
                    Object[] message = {
                        "Check Amount:", field1, "Check #", field2, checkTotal};
                    field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(8);
                    field2.setText("0");
                    field2.setSelectionStart(0);
                    field2.setSelectionEnd(2);

                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Check Value", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!validateDouble(field1.getText())) {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Improper check value.");
                        } else {
                            double amtReceived = Double.parseDouble(field1.getText());
                            amtReceived = round(amtReceived);
                            if (amtReceived < curCart.getTotalPrice()) {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Check value to small.");
                            } else {
                                double change = amtReceived - curCart.getTotalPrice();
                                change = round(change);
                                checkout.beginCheckCheckout(curCart, amtReceived, (String) empList.getSelectedItem(), Integer.parseInt(field2.getText()), myself, guiItems);
                                changeDue.setText("Change Due: $" + String.format("%.2f", change));
                                //FIELD1 CONTAINS CHECK AMT
                                //FIELD2 CONTAINS CHECK #
                                displayChangeDue = true;
                                updateCartScreen();
                            }//end else
                        }//end else
                    }//end if  
                }//end if isNotEmpty
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end checkButtonAction

        creditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty()) {
                    JFrame textInputFrame = new JFrame("");
                    JLabel creditTotal = new JLabel("Total: $", SwingConstants.RIGHT);
                    JTextField field1 = new JTextField();
                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field1.setSelectionStart(0);
                            field1.setSelectionEnd(12);
                            if (!validateDouble(field1.getText())) {
                                field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                            }//end if
                        }//end focusGained
                    });
                    creditTotal.setText("Total: $ " + String.format("%.2f", curCart.getTotalPrice()));
                    Object[] message = {
                        "Credit Amount:", field1, creditTotal};
                    field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(8);

                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Credit Amount", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!validateDouble(field1.getText())) {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Improper credit value.");
                        } else {
                            double amtReceived = Double.parseDouble(field1.getText());
                            amtReceived = round(amtReceived);

                            // System.out.println(remaining);
                            // System.out.println(curCart.getTotalPrice());
                            if (amtReceived < curCart.getTotalPrice()) {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Not enough credit.");
                            } else {
                                double change = amtReceived - curCart.getTotalPrice();
                                change = round(change);
                                checkout.beginCreditCheckout(curCart, amtReceived, (String) empList.getSelectedItem(), myself, guiItems);
                                changeDue.setText("Change Due: $" + String.format("%.2f", change));
                                displayChangeDue = true;
                                updateCartScreen();
                            }//end else
                        }//end else
                    }//end if
                } else if (!refundCart.isEmpty()) {
                    boolean isItemToRefund = false;
                    for (RefundItem item : refundCart.getRefundItems()) {
                        if (item.isRefundAllActive() || item.isRefundTaxOnlyActive()) {
                            isItemToRefund = true;
                        }
                    }
                    if (isItemToRefund) {
                        checkout.beginRefundCardCheckout(refundCart, (String) empList.getSelectedItem(), guiRefundItems, myself);

                    }

                }//end cartIsNotEmpty
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end creditButtonAction

        reprintReceiptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                displayChangeDue = false;
                if (!previousReceipt.contentEquals("EMPTY")) {
                    checkout.reprintReceipt(previousReceipt);
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end reprintReceiptButtonAction

        //ARPayment Button Listener
        arPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                JFrame textInputFrame2 = new JFrame("");
                JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                JTextField field3 = new JTextField();
                JTextField field4 = new JTextField();
                JTextField field5 = new JTextField();
                Object[] message = {
                    "Account Name:", field1,
                    "Last Name:", field2,
                    "First Name:", field3,
                    "DOB:", field4};
                Object[] message2 = {
                    "Amount To Pay:", field5};
                field1.setText("");
                field2.setText("");
                field3.setText("");
                field4.setText("");
                field5.setText("");
                String accountName = "";
                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Account Information", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (field1.getText().isEmpty() && field2.getText().isEmpty() && field3.getText().isEmpty() && field4.getText().isEmpty()) {
                        //do nothing, they clicked OK with everything blank
                    } else {
                        String[] choices = myDB.getARList(field1.getText(), field2.getText(), field3.getText(), field4.getText());
                        if (choices != null) {
                            accountName = (String) JOptionPane.showInputDialog(null, "Choose now...",
                                    "Choose AR Account", JOptionPane.QUESTION_MESSAGE, null, // Use
                                    // default
                                    // icon
                                    choices, // Array of choices
                                    choices[0]); // Initial choice
                            if (accountName != null) {
                                field5.addAncestorListener(new RequestFocusListener());
                                int option2 = JOptionPane.showConfirmDialog(textInputFrame2, message2, "Enter Amount To Pay", JOptionPane.OK_CANCEL_OPTION);
                                if (option2 == JOptionPane.OK_OPTION) {
                                    String temp = field5.getText();
                                    if (!validateDouble(temp)) {
                                        JFrame message1 = new JFrame("");
                                        JOptionPane.showMessageDialog(message1, "Not a valid amount.");
                                    } else {
                                        double price = Double.parseDouble(temp);
                                        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
                                        Date date = new Date();
                                        String tempID;
                                        tempID = dateFormat.format(date);
                                        // System.out.println(tempID);
                                        String upc = "A" + tempID;
                                        if (!curCart.containsAP(accountName)) {
                                            Item tempItem = new Item(myDB, tempID, upc, accountName, price, price, false, 853, 0, "", "", 1, false, 0, false);
                                            curCart.addItem(tempItem);
                                            guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                            displayChangeDue = false;
                                        } else {
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "Already an account payment for that account in cart.");
                                        }
                                    }
                                }//end if
                                updateCartScreen();
                            }//end if accountname not null
                        } else {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "No such account found.");
                        }//end else

                    }//end else
                }//end if OK_OPTION
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//end arPaymentButtonAction

        //DME Payment Button Listener
        dmePaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                JFrame textInputFrame2 = new JFrame("");
                JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                JTextField field3 = new JTextField();
                JTextField field4 = new JTextField();
                JTextField field5 = new JTextField();
                Object[] message = {
                    "Account Name:", field1,
                    "Last Name:", field3,
                    "First Name:", field2,
                    "DOB:", field4};
                Object[] message2 = {
                    "Amount To Pay:", field5};
                field1.setText("");
                field2.setText("");
                field3.setText("");
                field4.setText("");
                field5.setText("");
                String accountName = "";
                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Account Information", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (field1.getText().isEmpty() && field2.getText().isEmpty() && field3.getText().isEmpty() && field4.getText().isEmpty()) {
                        //do nothing, they clicked OK with everything blank
                    } else {
                        String[] choices = myDB.getDMEList(field1.getText(), field2.getText(), field3.getText(), field4.getText());
                        if (choices != null) {
                            accountName = (String) JOptionPane.showInputDialog(null, "Choose now...",
                                    "Choose DME Account", JOptionPane.QUESTION_MESSAGE, null, // Use
                                    // default
                                    // icon
                                    choices, // Array of choices
                                    choices[0]); // Initial choice
                            if (accountName != null) {
                                field5.addAncestorListener(new RequestFocusListener());
                                int option2 = JOptionPane.showConfirmDialog(textInputFrame2, message2, "Enter Amount To Pay", JOptionPane.OK_CANCEL_OPTION);
                                if (option2 == JOptionPane.OK_OPTION) {
                                    String temp = field5.getText();
                                    if (!validateDouble(temp)) {
                                        JFrame message1 = new JFrame("");
                                        JOptionPane.showMessageDialog(message1, "Not a valid amount.");
                                    } else {
                                        double price = Double.parseDouble(temp);
                                        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
                                        Date date = new Date();
                                        String tempID;
                                        tempID = dateFormat.format(date);
                                        // System.out.println(tempID);
                                        String upc = "D" + tempID;
                                        //boolean taxable, int category, int rxNumber, String insurance, String filldate, int quantity,boolean isRX)
                                        if (!curCart.containsAP(accountName)) {
                                            Item tempItem = new Item(myDB, tempID, upc, accountName, price, price, false, 854, 0, "", "", 1, false, 0, false);
                                            curCart.addItem(tempItem);
                                            guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                            displayChangeDue = false;
                                        } else {
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "Already an account payment for that account in cart.");
                                        }
                                    }
                                }//end if
                                updateCartScreen();
                            }//end if accountname not null
                        } else {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "No such account found.");
                        }//end else

                    }//end else
                }//end if OK_OPTION
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//end dmePaymentButtonAction
        paidOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();
                JTextField field2 = new JTextField();
                field1.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        field1.setSelectionStart(0);
                        field1.setSelectionEnd(15);
                        if (field1.getText().isEmpty()) {
                            field1.setText("Description");
                        }//end if
                    }//end focusGained
                });
                field2.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        field2.setSelectionStart(0);
                        field2.setSelectionEnd(12);
                        if (!validateDouble(field2.getText())) {
                            field2.setText(String.format("0"));
                        }//end if
                    }//end focusGained
                });
                Object[] message = {
                    "Description:", field1, "Amount: $", field2};
                field1.setText(String.format("Description"));
                field1.setSelectionStart(0);
                field1.setSelectionEnd(15);
                field2.setText("0.00");
                field2.setSelectionStart(0);
                field2.setSelectionEnd(4);

                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Paid Out Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (!validateDouble(field2.getText())) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Improper paid out value.");
                    } else {
                        double amtReceived = Double.parseDouble(field2.getText());
                        amtReceived = round(amtReceived);
                        if (amtReceived <= 0) {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Paid out must be greater than 0.");
                        } else {
                            if (field1.getText().isEmpty()) {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Paid out must have description.");
                            } else {
                                Object[] message2 = {
                                    "Are you sure?\nDescription: " + field1.getText(), "Amount: $ " + field2.getText()};

                                int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Paid Out Menu", JOptionPane.OK_CANCEL_OPTION);
                                if (option2 == JOptionPane.OK_OPTION) {
                                    checkout.beginPaidOut(field1.getText(), Double.parseDouble(field2.getText()));
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Success!");
                                }
                                //FIELD1 CONTAINS DESCRIPTION
                                //FIELD2 AMOUNT
                                displayChangeDue = false;
                                updateCartScreen();
                            }//end else
                        }//end else not 0 or less
                    }//end else
                }//end if  
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end paidOutButtonAction

        //ARPayment Button Listener
        chargeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty() && !curCart.containsChargedItem()) {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();
                    JTextField field4 = new JTextField();
                    Object[] message = {
                        "Account Name:", field1,
                        "Last Name:", field2,
                        "First Name:", field3,
                        "DOB:", field4};
                    field1.setText("");
                    field2.setText("");
                    field3.setText("");
                    field4.setText("");
                    String accountName = "";
                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Account Information", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (field1.getText().isEmpty() && field2.getText().isEmpty() && field3.getText().isEmpty() && field4.getText().isEmpty()) {
                            //do nothing, they clicked OK with everything blank
                        } else {
                            String[] choices = myDB.getARList(field1.getText(), field2.getText(), field3.getText(), field4.getText());
                            if (choices != null) {
                                accountName = (String) JOptionPane.showInputDialog(null, "Choose now...",
                                        "Choose AR Account", JOptionPane.QUESTION_MESSAGE, null, // Use
                                        // default
                                        // icon
                                        choices, // Array of choices
                                        choices[0]); // Initial choice
                                if (accountName != null) {
                                    checkout.beginChargeCheckout(curCart, accountName, (String) empList.getSelectedItem(), myself, guiItems);
                                    changeDue.setText("Change Due: $0.00");
                                    displayChangeDue = true;
                                    updateCartScreen();
                                }//end if accountname not null
                            } else {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "No such account found.");
                            }//end else

                        }//end else
                    }//end if OK_OPTION
                } else {
                    if (curCart.containsChargedItem()) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Please remove account payments before charging.");
                    }
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end chargeButtonAction

        cancelRefundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                refundCart.voidCart();
                for (GuiRefundCartItem item : guiRefundItems) {
                    item.removeAllGUIData();
                }
                guiRefundItems.clear();
                displayChangeDue = false;
                refundOver();
                updateCartScreen();
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end cancelRefundAction

        activateDisplayButton.setVisible(true);
        this.add(activateDisplayButton);
        addDmeAccountButton.setVisible(false);
        this.add(addDmeAccountButton);
        addRxAccountButton.setVisible(false);
        this.add(addRxAccountButton);
        addNewItemButton.setVisible(false);
        this.add(addNewItemButton);
        generateReportButton.setVisible(false);
        this.add(generateReportButton);
        updatePriceButton.setVisible(false);
        this.add(updatePriceButton);
        masterRefundButton.setVisible(false);
        this.add(masterRefundButton);
        paperButton.setVisible(true);
        this.add(paperButton);
        dmePaymentButton.setVisible(true);
        this.add(dmePaymentButton);
        cancelRefundButton.setVisible(false);
        this.add(cancelRefundButton);
        massDiscountButton.setVisible(true);
        this.add(massDiscountButton);
        reprintReceiptButton.setVisible(true);
        this.add(reprintReceiptButton);
        checkButton.setVisible(true);
        this.add(checkButton);
        creditButton.setVisible(true);
        this.add(creditButton);
        cashButton.setVisible(true);
        this.add(cashButton);
        otcButton.setVisible(true);
        this.add(otcButton);
        rxButton.setVisible(true);
        this.add(rxButton);
        voidButton.setVisible(true);
        this.add(voidButton);
        arPaymentButton.setVisible(true);
        this.add(arPaymentButton);
        paidOutButton.setVisible(true);
        this.add(paidOutButton);
        splitTenderButton.setVisible(true);
        this.add(splitTenderButton);
        chargeButton.setVisible(true);
        this.add(chargeButton);
        refundButton.setVisible(true);
        this.add(refundButton);
        noSaleButton.setVisible(true);
        this.add(noSaleButton);

        // //KEYBINDINGS
        rxButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "Page Up");
        rxButton.getActionMap().put("Page Up", rxButtonAA);
        rxButton.addActionListener(rxButtonAA);
        otcButton.addActionListener(otcButtonAA);

        KeyStroke down = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke ctrlTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK);
        Set<KeyStroke> keys = new HashSet<>();
        keys.add(down);
        keys.add(tab);
        keys.add(ctrlTab);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keys);
    }//end JFrame

    public void removeGuiCartItem(GuiCartItem item) {
        guiItems.remove(item);
    }

    public void updateCartScreen() {

        jPanel1.setLayout(null);

        this.repaint(10);
        this.setVisible(true);
        jPanel1.setVisible(true);

        if (refundCart.isEmpty()) {
            subTotal.setText(String.format("Subtotal: $%.2f", curCart.getSubTotal()));
            subTotal.setLocation(750, 800);
            subTotal.setSize(350, 50);
            subTotal.setFont(new Font(subTotal.getName(), Font.BOLD, 30));
            subTotal.setVisible(true);
            this.add(subTotal);
            totalTax.setText(String.format("Tax: $%.2f", curCart.getTax()));
            totalTax.setLocation(750, 850);
            totalTax.setSize(350, 50);
            totalTax.setFont(new Font(totalTax.getName(), Font.BOLD, 30));
            totalTax.setVisible(true);
            this.add(totalTax);
            totalPrice.setText(String.format("Total: $%.2f", curCart.getTotalPrice()));
            totalPrice.setLocation(750, 900);
            totalPrice.setSize(350, 50);
            totalPrice.setFont(new Font(totalPrice.getName(), Font.BOLD, 30));
            totalPrice.setVisible(true);
            this.add(totalPrice);
            changeDue.setLocation(750, 950);
            changeDue.setSize(350, 50);
            changeDue.setFont(new Font(subTotal.getName(), Font.BOLD, 30));
            totalNumRXinCart.setText("# of Rx's in Cart: " + curCart.getTotalNumRX());
        } else {
            subTotal.setText(String.format("Subtotal: $%.2f", refundCart.getSubTotal()));
            subTotal.setLocation(750, 800);
            subTotal.setSize(350, 50);
            subTotal.setFont(new Font(subTotal.getName(), Font.BOLD, 30));
            subTotal.setVisible(true);
            this.add(subTotal);
            totalTax.setText(String.format("Tax Refunded: $%.2f", refundCart.getTax()));
            totalTax.setLocation(750, 850);
            totalTax.setSize(350, 50);
            totalTax.setFont(new Font(totalTax.getName(), Font.BOLD, 30));
            totalTax.setVisible(true);
            this.add(totalTax);
            totalPrice.setText(String.format("Total Refunded: $%.2f", refundCart.getTotalPrice()));
            totalPrice.setLocation(750, 900);
            totalPrice.setSize(350, 50);
            totalPrice.setFont(new Font(totalPrice.getName(), Font.BOLD, 30));
            totalPrice.setVisible(true);
            this.add(totalPrice);
            changeDue.setLocation(750, 950);
            changeDue.setSize(350, 50);
            changeDue.setFont(new Font(subTotal.getName(), Font.BOLD, 30));
        }

        if (displayChangeDue) {
            changeDue.setVisible(true);
            if(displayActive){
            display.printLines("****THANK YOU!****",String.format("Change: $%.2f", Double.parseDouble(changeDue.getText().substring(changeDue.getText().indexOf('$')+1))) );
            }
        } else {
            changeDue.setVisible(false);
        }
        this.add(changeDue);

        if (curCart.getRequiresRepaint()) {
            int y = 15;
            for (GuiCartItem item : guiItems) {
                item.reposition(y);
                y += 15;
            }
            curCart.setRequiresRepaint(false);
        }

        jPanel1.setVisible(true);
        this.setVisible(true);

        textField.requestFocusInWindow();
    }//end updateCartScreen

    public void checkForAdminButtonVisible() {
        if (empList.getSelectedItem().toString().contentEquals("Smith, Andrew") || empList.getSelectedItem().toString().contentEquals("Fuller, Hollie")|| empList.getSelectedItem().toString().contentEquals("Sutphin, Debbie")) {
            updatePriceButton.setVisible(true);
            generateReportButton.setVisible(true);
            addNewItemButton.setVisible(true);
            addRxAccountButton.setVisible(true);
            addDmeAccountButton.setVisible(true);
            masterRefundButton.setVisible(true);
        } else if (empList.getSelectedItem().toString().contentEquals("Smith, Haley") ||empList.getSelectedItem().toString().contentEquals("Booth, Sam") || empList.getSelectedItem().toString().contentEquals("Broussard, Kayla")) {
            updatePriceButton.setVisible(true);
            addNewItemButton.setVisible(true);
            addRxAccountButton.setVisible(true);
            generateReportButton.setVisible(false);
            addDmeAccountButton.setVisible(false);
            masterRefundButton.setVisible(true);
        } else {
            updatePriceButton.setVisible(false);
            generateReportButton.setVisible(false);
            addNewItemButton.setVisible(false);
            addRxAccountButton.setVisible(false);
            addDmeAccountButton.setVisible(false);
            masterRefundButton.setVisible(false);
        }
    }

    public void setData(Database myDB) {
        this.setTitle("Smith's Super-Aid POS");
        //ImageIcon image = new ImageIcon("C:\\Users\\A.Smith\\Downloads\\Logo7.png");
       // this.setIconImage(image.getImage());
        
        this.myDB = myDB;
        checkout = new CheckoutHandler(myDB);
        curCart = new Cart();//new cart because program just launched!
        String[] employeeStrings = myDB.getEmployeesFromDatabase();
        //System.out.println(employeeStrings[0]);
        empList = new JComboBox(employeeStrings);
        JLabel employeeSelectionHeader = new JLabel("Register Clerk:", SwingConstants.CENTER);
        employeeSelectionHeader.setBounds(150, 825, 100, 30);
        employeeSelectionHeader.setVisible(true);
        empList.setSelectedIndex(0);
        empList.setVisible(true);
        empList.setBounds(100, 850, 200, 30);
        empList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                checkForAdminButtonVisible();
                //THIS WILL BE CALLED WHEN A NEW EMPLOYEE IS SELECTED TO RUN REGISTER
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end empListActionListener
        this.add(employeeSelectionHeader);
        this.add(empList);
        String[] empStrings2 = new String[employeeStrings.length + 1];
        empStrings2[0] = "NO";
        for (int i = 1; i < employeeStrings.length + 1; i++) {
            empStrings2[i] = employeeStrings[i - 1];
        }
        empList2 = new JComboBox(empStrings2);
        empList2.setSelectedIndex(0);
        empList2.setVisible(true);
        empList2.setBounds(100, 950, 200, 30);
        empList2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String employeeToCheckout = (String) empList2.getSelectedItem();
                if (!employeeToCheckout.contentEquals("NO")) {
                    for (GuiCartItem item : guiItems) {
                        item.employeeSaleTriggered();
                    }
                }else{
                    voidCarts();
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end empListActionListener
        this.add(empList2);

        updateCartScreen();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1980, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
        System.out.println(evt.getKeyCode());
    }//GEN-LAST:event_formKeyTyped

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(displayActive){
                display.close();
        }
    }//GEN-LAST:event_formWindowClosing

    private boolean validateRX(int length, int rxNumber, String insurance) {
        if (length == 7 && !curCart.containsRX(rxNumber, insurance)) {
            return true;
        }
        return false;
    }

    private boolean validateDouble(String copay) {
        try {
            double cpay = Double.parseDouble(copay);
            if (cpay < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }//end catch
        return true;
    }

    private boolean validateInteger(String integer) {
        try {
            int integ = Integer.parseInt(integer);
            if (integ < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }//end catch
        return true;
    }

    private boolean validateDate(String fillDate) {
        String subString1, subString2, subString3;
        if (fillDate.length() != 6) {
            return false;
        }//end if not right length
        subString1 = fillDate.substring(0, 2);
        subString2 = fillDate.substring(2, 4);
        subString3 = fillDate.substring(4, 6);
        try {
            int month = Integer.parseInt(subString1);
            int day = Integer.parseInt(subString2);
            int year = Integer.parseInt(subString3);

            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if (year < 0 || year > 99) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }//end catch
        return true;
    }//end validateDate

    private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }

    public void resetVars() {
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        Date date = new Date();
        previousDate = dateFormat.format(date);
        previousInsurance = "AARP";
        hasSigBeenCap = false;
    }

    private void loadTicketWithId(String id) {
        id = id.toUpperCase();
        //if it does, lets load it!
        curCart.loadCart(id, myDB);
        int i = 15;
        for (GuiCartItem item : guiItems) {
            item.removeAllGUIData();
        }
        guiItems.clear();
        for (Item item : curCart.getItems()) {
            guiItems.add(new GuiCartItem(item, i, jPanel1, curCart, myself));
            i += 15;
        }
        displayChangeDue = false;
    }

    private void loadReceipt(String receiptNum) {
        //if it does, lets load it!
        refundCart.loadRefundCart(receiptNum, myDB);
        int i = 15;
        /*
        for (GuiCartItem item : guiItems) {
            item.removeAllGUIData();
        }
        guiItems.clear();*/

        for (RefundItem item : refundCart.getRefundItems()) {
            guiRefundItems.add(new GuiRefundCartItem(item, i, jPanel1, refundCart, myself));
            i += 15;
            refundCart.setReceiptNum(item.getReceiptNum());
        }
        displayChangeDue = false;
    }

    public void refundOver() {
        rxButton.setVisible(true);
        otcButton.setVisible(true);
        arPaymentButton.setVisible(true);
        splitTenderButton.setVisible(true);
        chargeButton.setVisible(true);
        voidButton.setVisible(true);
        noSaleButton.setVisible(true);
        loadTicket.setVisible(true);
        createTicket.setVisible(true);
        checkButton.setVisible(true);
        paidOutButton.setVisible(true);
        refundButton.setVisible(true);
        lookupReceiptByRXButton.setVisible(true);
        massDiscountButton.setVisible(true);
        reprintReceiptButton.setVisible(true);
        dmePaymentButton.setVisible(true);
        paperButton.setVisible(true);
        if(!displayActive){
        activateDisplayButton.setVisible(true);
        }
        cancelRefundButton.setVisible(false);
        checkForAdminButtonVisible();
    }

    public void voidCarts() {
        curCart.voidCart();
        refundCart.voidCart();
        previousInsurance = "AARP";
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        Date date = new Date();
        previousDate = dateFormat.format(date);
        for (GuiCartItem item : guiItems) {
            item.removeAllGUIData();
        }
        for (GuiRefundCartItem item : guiRefundItems) {
            item.removeAllGUIData();
        }
        guiItems.clear();
        guiRefundItems.clear();
        empList2.setSelectedIndex(0);
        //displayChangeDue = false;//cant do this!
        updateCartScreen();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel jPanel1;
    private Database myDB;
    private Cart curCart;
    private CheckoutHandler checkout;
    private int[] integerArray = new int[12];
    private int arrayLoc = 0;
    JLabel totalPrice = new JLabel("Total Price: ", SwingConstants.RIGHT);
    JLabel totalTax = new JLabel("Total Tax: ", SwingConstants.RIGHT);
    JLabel subTotal = new JLabel("Subtotal: ", SwingConstants.RIGHT);
    JLabel changeDue = new JLabel("Change Due: ", SwingConstants.RIGHT);
    boolean displayChangeDue = false;
    JLabel quote = new JLabel("\"Pass on what you have learned, strength, mastery, but weakness, folly, failure also. Failure most of all. The greatest teacher, failure is.\" Jedi Master Yoda", SwingConstants.LEFT);
    JComboBox empList;//this has the select employee at its current selection
    JComboBox empList2;
    JLabel itemPriceHeader = new JLabel("Price Per Item: ", SwingConstants.RIGHT);
    JLabel employeeCheckoutHeader = new JLabel("Employee To Checkout:", SwingConstants.RIGHT);
    private boolean employeeCheckout = false;
    JLabel subTotalHeader = new JLabel("Price of Item(s): ", SwingConstants.RIGHT);
    JLabel totalNumRXinCart = new JLabel("# of Rx's in Cart: 0", SwingConstants.RIGHT);
    boolean hasSigBeenCap = false;
    JLabel discountHeader = new JLabel("Discount: ", SwingConstants.RIGHT);
    JLabel itemTaxTotalHeader = new JLabel("Item(s) Tax: ", SwingConstants.RIGHT);
    JLabel itemSubTotalHeader = new JLabel("Total: ", SwingConstants.RIGHT);
    JLabel itemNameHeader = new JLabel("Item: ", SwingConstants.RIGHT);
    JLabel itemQuantityHeader = new JLabel("Quantity: ", SwingConstants.RIGHT);
    String createTicketText = "Save\nTicket";
    JButton createTicket = new JButton("<html>" + createTicketText.replaceAll("\\n", "<br>") + "</html>");
    String loadTicketText = "Open\nTicket";
    JButton loadTicket = new JButton("<html>" + loadTicketText.replaceAll("\\n", "<br>") + "</html>");
    String chargeButtonText="Charge to\nAccount";
    JButton chargeButton = new JButton("<html>" + chargeButtonText.replaceAll("\\n", "<br>") + "</html>");
    JButton refundButton = new JButton("Refund");
    JButton masterRefundButton = new JButton("Master Refund");
    JButton noSaleButton = new JButton("No Sale");
    JButton rxButton = new JButton("RX");
    String receiptLookup = "Lookup\nReceipt";
    JButton lookupReceiptByRXButton = new JButton("<html>" + receiptLookup.replaceAll("\\n", "<br>") + "</html>");
    String previousInsurance = "AARP";
    String previousDate = "";
    JButton otcButton = new JButton("OTC");
    JButton paperButton = new JButton("Paper");
    JButton voidButton = new JButton("Void");
    JButton cashButton = new JButton("Cash");
    JButton checkButton = new JButton("Check");
    JButton creditButton = new JButton("Credit Card");
    JButton paidOutButton = new JButton("Paid Out");
    JButton updatePriceButton = new JButton("Update Price");
    JButton addNewItemButton = new JButton("Add Item");
    JButton generateReportButton = new JButton("Reports");
    JButton addRxAccountButton = new JButton("Add RX Account");
    JButton addDmeAccountButton = new JButton("Add DME Account");
    JButton activateDisplayButton = new JButton("Activate Display");
    String cancelRefund = "Cancel\nRefund";
    JButton cancelRefundButton = new JButton("<html>" + cancelRefund.replaceAll("\\n", "<br>") + "</html>");
    JButton massDiscountButton = new JButton("");
    ConfigFileReader reader = new ConfigFileReader();
    
    JButton employeeDiscountFalseButton = new JButton("");
    String ar = "Accounts\nReceivable\nPayment";
    String dme = "DME\nAccount\nPayment";
    JButton dmePaymentButton = new JButton("<html>" + dme.replaceAll("\\n", "<br>") + "</html>");
    protected String previousReceipt = "EMPTY";
    String st = "Split\nTender";
    JButton splitTenderButton = new JButton("<html>" + st.replaceAll("\\n", "<br>") + "</html>");
    JButton arPaymentButton = new JButton("<html>" + ar.replaceAll("\\n", "<br>") + "</html>");
    String receipt = "Reprint\nReceipt";
    JButton reprintReceiptButton = new JButton("<html>" + receipt.replaceAll("\\n", "<br>") + "</html>");
    private boolean itemIsTaxable = true;//assume every item is taxable at first.
    JTextField textField = new JTextField(10);
    ArrayList<GuiCartItem> guiItems = new ArrayList<GuiCartItem>();
    ArrayList<GuiRefundCartItem> guiRefundItems = new ArrayList<GuiRefundCartItem>();
    MainFrame myself = this;
    PoleDisplay display;
    RefundCart refundCart = new RefundCart();
    boolean displayActive=false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
