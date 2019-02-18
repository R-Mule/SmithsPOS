package database_console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
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

 @author A.Smith
 */
public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jPanel1 = new JPanel(new BorderLayout());
        jPanel1.setBounds(100, 100, 1120, 1200);//jPanel1.setBounds(100, 100, 1120, 1200);
        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setAutoscrolls(true);
        JScrollPane helpSP = new JScrollPane(jPanel1,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jPanel1.setVisible(true);
        helpSP.setAutoscrolls(true);
        helpSP.setBounds(100, 100, 1120, 700);
        //OLD DREW
        // this.add(menuBar);
        helpSP.setPreferredSize(new Dimension(1120, 700));
        jPanel1.setPreferredSize(new Dimension(1120, 500));//jPanel1.setPreferredSize(new Dimension(1120, 2000));
        helpSP.setVisible(true);

        this.add(helpSP);

        pharmacyName = ConfigFileReader.getPharmacyName();
        if (pharmacyName.contentEquals(superaid))
        {//This only allows Holiday events for Super-Aid Pharmacy
            DateFormat dateFormat1 = new SimpleDateFormat("MMdd");//ddyyhhmmss");
            Date date1 = new Date();
            String month;
            month = dateFormat1.format(date1);
            String dayTemp = month.substring(2);
            month = month.substring(0, 2);
            int day = Integer.parseInt(dayTemp);
            if (month.contentEquals("03"))
            {//its march
                isMarchMadness = true;
                if (day >= 1 && day < 18)
                {//18th is day after St Patricks Day 2018
                    isSaintPatricksDay = true;
                    if (day == 9)
                    {
                        isHolliesBirthday = true;
                    }
                }
                else
                {
                    isEaster = true;
                }
            }
            else if (month.contentEquals("04"))
            {
                if (day == 1)
                {
                    isMarchMadness = true;
                    isEaster = true;
                }
                else if (day < 3)
                {
                    isMarchMadness = true;
                }
                else if (day == 28)
                {
                    isWeddingMonth = true;
                    quotesActive = false;//disable quotes, due to graphics
                }
            }
            else if (month.contentEquals("11"))
            {
                isThanksgiving = true;
            }
            else if (month.contentEquals("10"))
            {
                isHalloween = true;
                quotesActive = false;
            }
            else if (month.contentEquals("12"))
            {
                if (day <= 25)
                {
                    isChristmas = true;
                    quotesActive = false;
                }
            }
            else if (month.contentEquals("01"))
            {
                if (day > 15)
                {
                    isValentinesDay = true;
                    quotesActive = false;
                }
            }
            else if (month.contentEquals("02"))
            {
                if (day < 15)
                {
                    isValentinesDay = true;
                }
                if (day == 14)
                {
                    isReallyValentinesDay = true;
                }
            }
            else if (month.contentEquals("06"))
            {
                if (day > 15)
                {
                    isFourthOfJuly = true;
                }
            }
            else if (month.contentEquals("07"))
            {
                if (day <= 4)
                {
                    isFourthOfJuly = true;
                }
                else
                {
                    isSummerTime = true;

                    quotesActive = false;
                }
            }
            else if (month.contentEquals("08"))
            {
                isSummerTime = true;
                quotesActive = false;
            }
            else if (month.contentEquals("09"))
            {
                isHalloween = true;
                quotesActive = false;
                //nothing right now, give them a month off :)
            }
        }
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        Date date = new Date();
        previousDate = dateFormat.format(date);
        textField.setBounds(100, 800, 200, 20);
        textField.addKeyListener(rp1);
        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!isSplitSavingActive)
                    {
                        if (refundCart.isEmpty())
                        {
                            String myText = textField.getText();
                            try
                            {
                                if (myText.length() > 11)
                                {
                                    myText = myText.substring(0, 11);
                                }

                                Item myItem = new Item(myText);

                                if (!myItem.getID().isEmpty() && !myItem.getUPC().isEmpty())
                                {//then we have a real item!
                                    curCart.addItem(myItem);
                                    boolean exisits = false;
                                    int index = 0;
                                    int loc = 0;
                                    for (GuiCartItem item : guiItems)
                                    {

                                        if (item.getUPC().contentEquals(myItem.getUPC()))
                                        {
                                            exisits = true;
                                            loc = index;
                                        }
                                        index++;
                                    }
                                    if (exisits)
                                    {
                                        guiItems.get(loc).updateQuantityLabelAmount();
                                    }
                                    else
                                    {
                                        guiItems.add(new GuiCartItem(myItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                    }

                                    displayChangeDue = false;
                                    updateCartScreen();
                                }
                                textField.setText("");
                            }
                            catch (StringIndexOutOfBoundsException e)
                            {
                                textField.setText("");
                            }//end catch
                        }
                        else
                        {
                            textField.setText("");
                        }
                    }
                    else
                    {
                        textField.setText("");
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Cannot add item while Splitting Tickets!");
                    }
                }
                else
                {
                    textField.setText("");
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
            }//end actionPerformed
        });
        this.add(textField);
        this.setVisible(true);

        //TotalNumRXinCart
        totalNumRXinCart.setLocation(250, 780);
        totalNumRXinCart.setSize(250, 50);
        totalNumRXinCart.setFont(new Font(employeeCheckoutHeader.getName(), Font.BOLD, 12));
        totalNumRXinCart.setVisible(true);
        //Employee Checkout:
        employeeCheckoutHeader.setLocation(15, 910);
        employeeCheckoutHeader.setSize(250, 50);
        employeeCheckoutHeader.setFont(new Font(employeeCheckoutHeader.getName(), Font.BOLD, 12));
        employeeCheckoutHeader.setVisible(true);

        //versionHeader
        versionHeader.setLocation(1800, 950);
        versionHeader.setSize(250, 50);
        versionHeader.setFont(new Font(versionHeader.getName(), Font.BOLD, 12));
        versionHeader.setVisible(true);
        this.add(versionHeader);

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

        estimatedLunchTotalLabel.setLocation(1790, 870);
        estimatedLunchTotalLabel.setSize(250, 50);
        estimatedLunchTotalLabel.setFont(new Font(estimatedLunchTotalLabel.getName(), Font.BOLD, 12));
        estimatedLunchTotalLabel.setVisible(true);
        this.add(estimatedLunchTotalLabel);

        estimatedCheckTotalLabel.setLocation(1790, 885);
        estimatedCheckTotalLabel.setSize(250, 50);
        estimatedCheckTotalLabel.setFont(new Font(estimatedCheckTotalLabel.getName(), Font.BOLD, 12));
        estimatedCheckTotalLabel.setVisible(true);
        this.add(estimatedCheckTotalLabel);

        estimatedCashTotalLabel.setLocation(1790, 900);
        estimatedCashTotalLabel.setSize(250, 50);
        estimatedCashTotalLabel.setFont(new Font(estimatedCashTotalLabel.getName(), Font.BOLD, 12));
        estimatedCashTotalLabel.setVisible(true);
        this.add(estimatedCashTotalLabel);

        estimatedCoinTotalLabel.setLocation(1790, 915);
        estimatedCoinTotalLabel.setSize(250, 50);
        estimatedCoinTotalLabel.setFont(new Font(estimatedCoinTotalLabel.getName(), Font.BOLD, 12));
        estimatedCoinTotalLabel.setVisible(true);
        this.add(estimatedCoinTotalLabel);

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
        createTicket.setLocation(1800, 400);
        createTicket.setSize(100, 100);
        createTicket.setBackground(new Color(0, 255, 0));

        //resave
        resaveTicket.setLocation(1800, 500);
        resaveTicket.setSize(100, 100);
        resaveTicket.setBackground(new Color(95, 255, 188));
        resaveTicket.setVisible(false);
        this.add(resaveTicket);

        loadTicket.setLocation(1800, 300);
        loadTicket.setSize(100, 100);
        loadTicket.setBackground(new Color(255, 255, 0));

        beginSplitTicketButton.setLocation(1700, 300);
        beginSplitTicketButton.setSize(100, 100);
        beginSplitTicketButton.setBackground(new Color(236, 132, 255));
        beginSplitTicketButton.setVisible(true);
        this.add(beginSplitTicketButton);

        cancelSplitTicketButton.setLocation(1800, 300);
        cancelSplitTicketButton.setSize(100, 100);
        cancelSplitTicketButton.setBackground(new Color(255, 0, 0));
        cancelSplitTicketButton.setVisible(false);
        this.add(cancelSplitTicketButton);

        //this creates the lookupReceiptByRXButton
        lookupReceiptByRXButton.setLocation(1700, 400);
        lookupReceiptByRXButton.setSize(100, 100);
        lookupReceiptByRXButton.setBackground(new Color(255, 200, 100));

        resaveTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!curCart.isEmpty())
                    {
                        saveTicket(loadedTicketID);
                    }
                    else
                    {//CARTS EMPTY!
                        //notify nothing to save
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "There is nothing in the cart to save!");
                    }//end else its empty

                    updateCartScreen();

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//END createTicket ActionListener!

        createTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!curCart.isEmpty())
                    {
                        JFrame textInputFrame = new JFrame("");
                        String id = JOptionPane.showInputDialog(
                                textInputFrame,
                                "Enter the customer account name to continue:",
                                "Customer Account Name Input",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        saveTicket(id);
                    }
                    else
                    {//CARTS EMPTY!
                        //notify nothing to save
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "There is nothing in the cart to save!");
                    }//end else its empty

                    updateCartScreen();

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//END createTicket ActionListener!

        loadTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    //System.out.println("HELLO");
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();

                    Object[] message =
                    {
                        "Enter the customer account name to continue:", field1
                    };
                    field1.setText("");
                    field1.addAncestorListener(new RequestFocusListener());

                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Ticket Name", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        String id = field1.getText();
                        id = id.toUpperCase();
                        if (id != null && !id.isEmpty() && Database.checkDatabaseForTicket(id))
                        {
                            boolean allowed = true;
                            if (id.contentEquals("HOW ABOUT A MAGIC TRICK?"))
                            {
                                if (curCart.getItems().size() == 1 && curCart.getItems().get(0).mutID.contentEquals("BATDIS22"))
                                {
                                    allowed = true;
                                }
                                else
                                {
                                    allowed = false;
                                }
                            }
                            if (id.contentEquals("WET BANDITS"))
                            {
                                if (!isChristmas)
                                {
                                    allowed = false;
                                }
                            }
                            if (id.contentEquals("MICHAEL MYERS"))
                            {
                                if (!isHalloween)
                                {
                                    allowed = false;
                                }
                            }
                            if (id.contentEquals("WINGARDIUM LEVIOSA"))
                            {
                                for (Item item : curCart.getItems())
                                {
                                    if (item.itemName.contentEquals("Platform") && item.itemPrice == 9.75)
                                    {

                                    }
                                    else
                                    {
                                        allowed = false;
                                    }
                                }
                            }
                            if (allowed)
                            {
                                loadTicketWithId(id);
                                loadedTicketID = id;
                                resaveTicketText = "Resave\nTicket As\n" + loadedTicketID;
                                resaveTicket.setText("<html>" + resaveTicketText.replaceAll("\\n", "<br>") + "</html>");
                                resaveTicket.setVisible(true);
                                updateCartScreen();
                            }
                        }
                        else
                        {//Load All Tickets into selectable GUI
                            //Sorry no such ticket found
                            String[] choices = Database.getAllTicketsNames();
                            if (choices != null && choices.length > 0)
                            {

                                id = (String) JOptionPane.showInputDialog(null, "Couldn't find that ticket? Check these?...",
                                        "Choose Ticket", JOptionPane.QUESTION_MESSAGE, null, // Use
                                        // default
                                        // icon
                                        choices, // Array of choices
                                        choices[0]); // Initial choice
                                if (id != null)
                                {
                                    loadTicketWithId(id);
                                    loadedTicketID = id;
                                    resaveTicketText = "Resave\nTicket As\n" + loadedTicketID;
                                    resaveTicket.setText("<html>" + resaveTicketText.replaceAll("\\n", "<br>") + "</html>");
                                    resaveTicket.setVisible(true);
                                    updateCartScreen();
                                } //end if
                            }//end if
                        }//end else

                    }

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed loadTicket
        });//end loadTicket action

        lookupReceiptByRXButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //System.out.println("HELLO");
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();

                Object[] message =
                {
                    "Enter RX Number to find filename:", field1
                };
                field1.setText("");
                field1.addAncestorListener(new RequestFocusListener());

                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter RX Number", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION)
                {
                    String id = field1.getText();

                    if (id != null && !id.isEmpty() && validateInteger(id))
                    {
                        int rxNumber = Integer.parseInt(id);
                        String[] choices = Database.lookupReceiptByRX(rxNumber);
                        if (choices != null && choices.length > 0)
                        {
                            Object[] message2 =
                            {
                                "Here is what I found:", choices, choices[0]
                            };

                            id = (String) JOptionPane.showInputDialog(null, "Results",
                                    "Here is what I found:", JOptionPane.QUESTION_MESSAGE, null, // Use
                                    // default
                                    // icon
                                    choices, // Array of choices
                                    choices[0]); // Initial choice
                            if (Desktop.isDesktopSupported())
                            {
                                try
                                {
                                    if (id != null && !id.isEmpty())
                                    {
                                        String path = ConfigFileReader.getRemoteDrivePath();
                                        File myFile = new File(path + id.substring(0, 2) + id.substring(4, 6) + "\\" + id + ".pdf");
                                        Desktop.getDesktop().open(myFile);
                                    }
                                }
                                catch (IOException ex)
                                {
                                    // no application registered for PDFs
                                }
                            }
                        }
                        else
                        {
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
        //This creates the UPS Payment Button
        upsButton.setLocation(1800, 100);
        upsButton.setSize(100, 100);

        holidayLoader = new HolidayLoader(this);

        if (isMarchMadness)
        {
            holidayLoader.showMarchMaddness();
        }
        if (isEaster)
        {
            holidayLoader.makeEasterActiveHoliday();
        }
        else if (isFourthOfJuly)
        {
            holidayLoader.make4thOfJulyActiveHoliday();
        }
        else if (isValentinesDay)
        {
            holidayLoader.makeValentinesDayActiveHoliday();
        }

        else if (isHalloween)
        {
            holidayLoader.makeHalloweenActiveHoliday();
        }

        else if (isChristmas)
        {
            holidayLoader.makeChristmasActiveHoliday();
        }

        else if (isSummerTime)
        {
            holidayLoader.makeSummerTimeActiveHoliday();
        }

        else if (isThanksgiving)
        {
            holidayLoader.makeThanskgivingActiveHoliday();
        }

        else if (isSaintPatricksDay)
        {
            holidayLoader.makeSaintPatricksDayActiveHoliday();
        }

        if (isWeddingMonth)
        {
            holidayLoader.makeWeddingMonthActiveHoliday();
        }

        //upsButton.setBackground(new Color(100, 65, 23));
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
        //This creates the debitButton 
        debitButton.setLocation(1600, 700);
        debitButton.setSize(100, 100);
        debitButton.setBackground(new Color(150, 150, 150));
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
        splitTenderButton.setLocation(1700, 700);
        splitTenderButton.setSize(100, 100);
        splitTenderButton.setBackground(new Color(24, 240, 255));
        //this creates the Charge Button
        chargeButton.setLocation(1800, 700);
        chargeButton.setSize(100, 100);
        chargeButton.setBackground(new Color(255, 126, 21));
        //This creates the Mass Discount Button
        massDiscountButton.setLocation(875, 65);
        massDiscountButton.setSize(20, 20);
        massDiscountButton.setBackground(new Color(255, 255, 100));
        //This creates the massPrechargeButton
        massPrechargeButton.setLocation(155, 65);
        massPrechargeButton.setSize(20, 20);
        massPrechargeButton.setBackground(new Color(255, 0, 0));
        massPrechargeButton.setVisible(true);
        this.add(massPrechargeButton);

        //This creates the massSplitTicketButton
        massSplitTicketButton.setLocation(1080, 65);
        massSplitTicketButton.setSize(110, 20);
        massSplitTicketButton.setBackground(new Color(255, 0, 0));
        massSplitTicketButton.setVisible(false);
        this.add(massSplitTicketButton);

        //This creates the activateDisplayButton 
        activateDisplayButton.setLocation(500, 890);
        activateDisplayButton.setSize(150, 40);
        activateDisplayButton.setBackground(new Color(50, 255, 255));

        //This creates the clerkLoginButton
        clerkLoginButton.setLocation(10, 820);
        clerkLoginButton.setSize(100, 40);
        clerkLoginButton.setBackground(new Color(0, 255, 0));

        //This creates the clerkLogoutButton
        clerkLogoutButton.setLocation(120, 850);
        clerkLogoutButton.setSize(150, 40);
        clerkLogoutButton.setBackground(new Color(255, 0, 0));

        clerkLogoutButton.setVisible(false);
        clerkLoginButton.setVisible(true);
        this.add(clerkLogoutButton);
        this.add(clerkLoginButton);



        clerkLoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame textInputFrame = new JFrame("");

                JTextField field1 = new JTextField();
                field1.addAncestorListener(new RequestFocusListener());
                Object[] message =
                {
                    "Enter Passcode:", field1
                };
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Employee Login Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION)
                {

                    if (field1.getText().contentEquals("Prince Ali Ababwa"))
                    {
                        boolean found = false;
                        for (Item item : curCart.getItems())
                        {
                            if (item.itemName.contentEquals("Bread") && item.itemPrice == 112519.92 && item.getDiscountPercentage() == 1)
                            {
                                found = true;
                            }
                        }
                        if (found)
                        {
                            EasterEgg ee = new EasterEgg("images/al2.gif", "sounds/al2.wav", "", "You ain't never had a friend like me!");
                        }
                    }//end if EE Protocol
                    if (field1.getText().contentEquals("Please"))
                    {//EE Protocol

                        ImageIcon icon = new ImageIcon(getClass().getResource("images/ssj.gif"));
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "“I am the hope of the universe.\nI am the answer to all living things that cry out for peace.\nI am protector of the innocent.\nI am the light in the darkness. \nI am truth.\nAlly to good!\nNightmare to you!”", "", 0, icon);

                    }
                    else if (field1.getText().contentEquals("Kevin McCallister"))
                    {
                        for (Item item : curCart.getItems())
                        {
                            if (item.mutID.contentEquals("HAPIZZA") && item.quantity == 11 && isChristmas)
                            {
                                EasterEgg ee = new EasterEgg("images/ha3.gif", "sounds/ha3.wav", "", "Did anyone order me a plain cheese?");
                            }
                        }

                    }
                    else if (field1.getText().contentEquals("The One"))
                    {//end EE Protocol
                        boolean found1 = false;
                        boolean found2 = false;
                        boolean found3 = false;
                        int cntr = 0;
                        for (Item item : curCart.getItems())
                        {
                            cntr++;
                            if (item.mutID.contentEquals("MATRED") && item.percentageDisc == 0.01)
                            {
                                found1 = true;
                            }
                            if (item.itemName.contentEquals("Man") && item.itemPrice == 20.03 && item.percentageDisc == 0.01)
                            {
                                found2 = true;
                            }
                            if (item.itemName.contentEquals("Because I choose to.") && item.itemPrice == 20.03 && item.percentageDisc == 0.01)
                            {
                                found3 = true;
                            }
                        }
                        if (found1 && found2 && found3 && cntr == 3)
                        {
                            EasterEgg ee = new EasterEgg("images/mx4.gif", "sounds/mx4.wav", "", "You're not human, are you?");
                        }
                    }
                    if (!field1.getText().isEmpty() && validateInteger(field1.getText()))
                    {
                        String clerkName = Database.getEmployeeNameByCode(Integer.parseInt(field1.getText()));
                        if (clerkName != null)
                        {

                            menuBar.setAllVisible();
                            if (isHalloween)
                            {
                                EasterEgg ee = new EasterEgg("sounds/mario1.wav");
                            }
                            employeeSelectionHeader.setText("Active Clerk: " + clerkName);
                            activeClerksPasscode = Integer.parseInt(field1.getText());
                            checkForAdminButtonVisible(Integer.parseInt(field1.getText()));
                            clerkLogoutButton.setVisible(true);
                            if (clerkName.contentEquals("Smith, Hollie") && isHolliesBirthday)//It is Hollie's Birthday!
                            {
                                EasterEgg ee = new EasterEgg("images/birthday.gif", "sounds/birthday.wav", "", "Happy Birthday Love!");
                            }
                            if (clerkName.contentEquals("Smith, Hollie") && isWeddingMonth)//wedding month is now our anniversary only
                            {
                                EasterEgg ee = new EasterEgg("images/weddingphoto.jpg", "sounds/weddingthankyou.wav", "", "Happy Anniversay Babe!");
                            }

                            rp1.reload();
                        }
                    }

                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        });
        clerkLogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                employeeSelectionHeader.setText("Active Clerk: NONE");
                menuBar.setAllNotVisible();
                clerkLogoutButton.setVisible(false);
                activeClerksPasscode = -1;
                checkForAdminButtonVisible(-1);//We send -1 because no clerk is logged in now.
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
                rp1.exit();
            }
        });

        activateDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                display = new PoleDisplay(/*reader*/);
                if (!display.successfulStart())
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "The Pole Display could not be successfully started! Error: Smith_2 \nContact Hollie.");
                }
                curCart.setDisplay(display);
                refundCart.setDisplay(display);
                curCart.updateTotal();
                updateCartScreen();
                displayActive = true;
                activateDisplayButton.setVisible(false);
            }
        });

        paperButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    Item myItem = new Item("NEWSPAPER");

                    if (!myItem.getID().isEmpty() && !myItem.getUPC().isEmpty())
                    {//then we have a real item!
                        curCart.addItem(myItem);
                        boolean exisits = false;
                        int index = 0;
                        int loc = 0;
                        for (GuiCartItem item : guiItems)
                        {

                            if (item.getUPC().contentEquals(myItem.getUPC()))
                            {
                                exisits = true;
                                loc = index;
                            }
                            index++;
                        }
                        if (exisits)
                        {
                            guiItems.get(loc).updateQuantityLabelAmount();
                        }
                        else
                        {
                            guiItems.add(new GuiCartItem(myItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                        }

                        displayChangeDue = false;
                        updateCartScreen();
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });

        refundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    //System.out.println("HELLO");
                    if (curCart.isEmpty())
                    {
                        JFrame textInputFrame = new JFrame("");
                        JTextField field1 = new JTextField();

                        Object[] message =
                        {
                            "Enter receipt number:", field1
                        };
                        field1.setText("");
                        field1.addAncestorListener(new RequestFocusListener());

                        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Receipt Number", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION)
                        {
                            String receiptNum = field1.getText();
                            receiptNum = receiptNum.toUpperCase();
                            if (receiptNum != null && !receiptNum.isEmpty())
                            {
                                Database.loadReceipt(receiptNum);
                                loadReceipt(receiptNum);
                                if (!guiRefundItems.isEmpty())
                                {
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
                                    activateDisplayButton.setVisible(false);
                                    massPrechargeButton.setVisible(false);
                                    //creditButton.setVisible(false);
                                    debitButton.setVisible(false);
                                    upsButton.setVisible(false);
                                    if (isMarchMadness)
                                    {
                                        mmButton.setVisible(false);
                                    }
                                    menuBar.setAllNotVisible();//THIS HIDES MENU BAR DURING REFUND!!
                                    cancelRefundButton.setVisible(true);
                                    updateCartScreen();
                                }
                            }
                            else
                            {//Load All Tickets into selectable GUI
                                //Sorry no such receipt found
                            }//end else

                        }
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

            }//end actionPerformed refundButton
        });//end refundButton action

        massDiscountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty())
                {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field1.setSelectionStart(0);
                            field1.setSelectionEnd(12);
                            if (!validateInteger(field1.getText()))
                            {
                                field1.setText("0");
                            }//end if
                        }//end focusGained
                    });
                    Object[] message =
                    {
                        "Discount Percentage: %", field1
                    };
                    field1.setText("0");
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(2);

                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Mass Discount Menu", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        if (!validateInteger(field1.getText()))
                        {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Improper discount percentage.");
                        }
                        else
                        {
                            double discPer = Double.parseDouble(field1.getText());
                            discPer /= 100;//move decimal 2 places to get percentage
                            //discPer = discPer;
                            if (discPer > 1 || discPer < 0)
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Not a valid discount amount.");
                            }
                            else
                            {
                                System.out.println(discPer);
                                curCart.setMassDiscount(discPer);
                                for (GuiCartItem tempItem : guiItems)
                                {
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

        massPrechargeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty())
                {
                    if (isMassPreCharged)
                    {
                        massPrechargeButton.setBackground(new Color(255, 0, 0));
                    }
                    else
                    {
                        massPrechargeButton.setBackground(new Color(0, 255, 0));
                    }
                    isMassPreCharged = !isMassPreCharged;
                    for (Item item : curCart.getItems())
                    {
                        if (item.isRX)
                        {
                            item.setIsPreCharged(isMassPreCharged);
                        }
                    }
                    for (GuiCartItem tempItem : guiItems)
                    {
                        if (tempItem.item.isRX())
                        {
                            if (isMassPreCharged)
                            {
                                tempItem.setPrechargedFalsePressed(event);
                            }
                            else
                            {
                                tempItem.setPrechargedTruePressed(event);
                            }
                        }
                    }
                    updateCartScreen();
                }//end cartIsNotEmpty
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end massPrechargeAction

        massSplitTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!curCart.isEmpty())
                {
                    if (isMassSplitting)
                    {
                        massSplitTicketButton.setBackground(new Color(255, 0, 0));
                        massSplitTicketButton.setText("Mass Off");
                    }
                    else
                    {
                        massSplitTicketButton.setBackground(new Color(0, 255, 0));
                        massSplitTicketButton.setText("Mass On");
                    }
                    isMassSplitting = !isMassSplitting;
                    for (Item item : curCart.getItems())
                    {

                        item.isSetToSplitSave = isMassSplitting;

                    }
                    for (GuiCartItem tempItem : guiItems)
                    {
                        if (isMassSplitting)
                        {
                            tempItem.notSplitSavingButtonPressed(event);
                        }
                        else
                        {
                            tempItem.isSplitSavingButtonPressed(event);
                        }

                    }
                    updateCartScreen();
                }//end cartIsNotEmpty
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end massSplitTicketAction

        AbstractAction rxButtonAA = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();

                    field2.setText(previousDate);
                    String[] possibilities = Database.getInsurances();
                    JList<String> list = new JList<>(possibilities); //data has type Object[]
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    list.setLayoutOrientation(JList.VERTICAL_WRAP);
                    list.setBounds(100, 50, 50, 300);//100 to 300 on Y because so many insurances added.
                    list.setVisibleRowCount(-1);

                    for (int i = 0; i < possibilities.length; i++)
                    {
                        if (previousInsurance.contentEquals(possibilities[i]))
                        {
                            list.setSelectedIndex(i);
                        }//end if
                    }//end for

                    JScrollPane listScroller = new JScrollPane(list);
                    listScroller.setPreferredSize(new Dimension(250, 80));
                    Object[] message =
                    {
                        "RX Number:", field1,
                        "Copay:", field3,
                        "Fill Date:", field2,
                        "Insurance:", list
                    };
                    field3.setText("0.00");
                    field2.setSelectionStart(0);
                    field2.setSelectionEnd(6);
                    field3.setSelectionStart(0);
                    field3.setSelectionEnd(4);
                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "RX Information", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION)
                    {
                        int rxNumber;
                        String fillDate;
                        fillDate = field2.getText();
                        try
                        {
                            String insurance = (String) list.getSelectedValue();
                            rxNumber = Integer.parseInt(field1.getText());
                            int length = (int) (Math.log10(rxNumber) + 1);
                            if (!validateRX(length, rxNumber, insurance, fillDate))
                            {//invalid RXNumber
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Invalid RX Number");
                            }
                            else
                            {

                                if (!validateDate(fillDate))
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Invalid Fill Date");
                                }
                                else
                                {
                                    String temp = field3.getText();
                                    if (!validateDouble(temp))
                                    {//check for copay
                                        JFrame message1 = new JFrame("");
                                        JOptionPane.showMessageDialog(message1, "Invalid Copay");
                                    }
                                    else
                                    {//else everything checks out! WE HAVE ALL GOOD DATA!!!
                                        double copay = Double.parseDouble(temp);
                                        Item tempItem = new Item(rxNumber, fillDate, insurance, copay, false);
                                        if (!curCart.containsRX(tempItem.rxNumber, insurance, fillDate))
                                        {
                                            curCart.addItem(tempItem);
                                            guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                            totalNumRXinCart.setText("# of Rx's in Cart: " + curCart.getTotalNumRX());
                                            displayChangeDue = false;
                                            previousInsurance = insurance;
                                            previousDate = fillDate;
                                            ArrayList<String> ticketIDs = Database.getAllTicketsNamesWithRxNumber(rxNumber);
                                            if (!ticketIDs.isEmpty())
                                            {
                                                if (JOptionPane.showConfirmDialog(null, "There are already  ticket(s) with that RX Number. Would you like me to load those?", "WARNING",
                                                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                                                {
                                                    for (String id : ticketIDs)
                                                    {
                                                        loadedTicketID = id;
                                                        resaveTicketText = "Resave\nTicket As\n" + loadedTicketID;
                                                        resaveTicket.setText("<html>" + resaveTicketText.replaceAll("\\n", "<br>") + "</html>");
                                                        resaveTicket.setVisible(true);
                                                        loadTicketWithId(id);
                                                    }
                                                    updateCartScreen();
                                                }
                                                else
                                                {
                                                    // no option
                                                }
                                            }
                                        }

                                    }
                                }//end else valid fillDate
                            }//end else valid RXNumber
                        }
                        catch (NumberFormatException e)
                        {
                            //If number is not number for RX, print error msg.
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Invalid RX Number");
                        }//end catch

                    }//end if
                    updateCartScreen();

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        };
        AbstractAction otcButtonAA = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();
                    field2.setText(previousDate);
                    Object[] message =
                    {
                        "Item Name:", field1,
                        "Cost:", field2,
                        "Price:", field3
                    };
                    field1.setText("");
                    field2.setText("0.00");
                    field3.setText("");
                    field2.setSelectionStart(0);
                    field2.setSelectionEnd(4);
                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "OTC Item Information", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        if (field1.getText().isEmpty())
                        {
                            //must have some name
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Item must have a name.");
                        }
                        else
                        {//item has a name
                            if (!validateDouble(field2.getText()))
                            {//check cost
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cost is invalid.");
                            }
                            else
                            {//cost is good
                                if (!validateDouble(field3.getText()))
                                {//check price
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Price is invalid.");
                                }
                                else
                                {//price is good
                                    // randomItemCntr++;
                                    DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
                                    Date date = new Date();
                                    String tempID;
                                    tempID = dateFormat.format(date);
                                    System.out.println(tempID);
                                    String upc = 'T' + tempID;
                                    if (Double.parseDouble(field3.getText()) > 9000 && field1.getText().contentEquals("Son Goku"))
                                    {//EE Protocol
                                        JFrame message1 = new JFrame("");
                                        ImageIcon icon = new ImageIcon(getClass().getResource("images/over9000.jpg"));
                                        JOptionPane.showMessageDialog(message1, "", "", 0, icon);
                                    }
                                    else if (field1.getText().contentEquals("I show not your face but your hearts desire") && Double.parseDouble(field3.getText()) == 1114.01)
                                    {
                                        boolean part1 = false;
                                        boolean part2 = false;
                                        for (Item item : curCart.getItems())
                                        {
                                            if (item.mutID.contentEquals("HPSS2"))
                                            {
                                                part1 = true;
                                            }
                                            else if (item.itemName.contentEquals("Platform") && item.itemPrice == 9.75)
                                            {
                                                part2 = true;
                                            }

                                        }
                                        if (part1 && part2)
                                        {
                                            EasterEgg ee = new EasterEgg("images/hpss3.gif", "sounds/hpss3.wav", "", "One can never have enough socks.");
                                        }

                                    }
                                    else if (field1.getText().contentEquals("Platform") && Double.parseDouble(field3.getText()) == 9.75)
                                    {
                                        EasterEgg ee = new EasterEgg("images/hpss1.gif", "sounds/HPSS1.wav", "", "Alas! Earwax!");

                                    }
                                    else if (field1.getText().contentEquals("Guest") && Double.parseDouble(field3.getText()) == 20.17)
                                    {
                                        boolean part1 = false;
                                        boolean part2 = false;
                                        for (Item item : curCart.getItems())
                                        {
                                            if (item.itemName.contentEquals("Be") && item.getPriceOfItemsBeforeTax() == 0.03)
                                            {
                                                part1 = true;
                                            }
                                            else if (item.itemName.contentEquals("Our") && item.getPriceOfItemBeforeTax() == 0.17)
                                            {
                                                part2 = true;
                                            }
                                        }
                                        if (part1 && part2)
                                        {
                                            EasterEgg ee = new EasterEgg("images/bnb.gif", "sounds/BOG.wav", "", "Try the gray stuff, it's delicious!");
                                        }//end if BNB Protocol
                                    }//end EE protocol
                                    else if (field1.getText().contentEquals("Man") && Double.parseDouble(field3.getText()) == 20.03)
                                    {

                                        boolean found1 = false;
                                        int cntr = 0;
                                        for (Item item : curCart.getItems())
                                        {
                                            cntr++;
                                            if (item.mutID.contentEquals("MATRED"))
                                            {
                                                found1 = true;
                                            }
                                        }
                                        if (found1 && cntr == 1)
                                        {
                                            EasterEgg ee = new EasterEgg("images/mx2.gif", "sounds/mx2.wav", "Why do you persist?", "");

                                        }

                                    }
                                    else if (field1.getText().contentEquals("A Dark Knight") && Double.parseDouble(field3.getText()) == 20.08)
                                    {
                                        if (curCart.getItems().size() == 2 && curCart.getItems().get(0).mutID.contentEquals("BATDIS22") && curCart.getItems().get(1).mutID.contentEquals("BATMON") && curCart.getItems().get(1).getDiscountPercentage() == .5)
                                        {
                                            EasterEgg ee = new EasterEgg("images/dk3.gif", "sounds/dk3.wav", "Accomplice? I'm going to tell them the whole thing was your idea.", "");
                                        }
                                    }
                                    else if (field1.getText().contentEquals("Because I choose to.") && Double.parseDouble(field3.getText()) == 20.03)
                                    {

                                        boolean found1 = false;
                                        boolean found2 = false;
                                        int cntr = 0;
                                        for (Item item : curCart.getItems())
                                        {
                                            cntr++;
                                            if (item.mutID.contentEquals("MATRED"))
                                            {
                                                found1 = true;
                                            }
                                            if (item.itemName.contentEquals("Man") && item.itemPrice == 20.03)
                                            {
                                                found2 = true;
                                            }
                                        }
                                        if (found1 && found2 && cntr == 2)
                                        {
                                            EasterEgg ee = new EasterEgg("images/mx3.gif", "sounds/mx3.wav", "", "Who are you?");
                                        }

                                    }
                                    else if (isHalloween && Double.parseDouble(field3.getText()) == 3.00 && field1.getText().contentEquals("Come Little Children"))
                                    {
                                        EasterEgg ee = new EasterEgg("images/hp1.gif", "sounds/hp1.wav", "", "Just one item to go!");

                                    }
                                    else if (Double.parseDouble(field3.getText()) == 16.93 && field1.getText().contentEquals("I Put A Spell On You"))
                                    {
                                        boolean found = false;
                                        for (Item item : curCart.getItems())
                                        {
                                            if (item.itemName.contentEquals("Come Little Children") && item.itemPrice == 3.00)
                                            {
                                                found = true;
                                            }
                                        }
                                        if (found)
                                        {
                                            EasterEgg ee = new EasterEgg("images/hp2.gif", "sounds/hp2.wav", "", "Max likes your yabbos!");
                                        }
                                    }//end if EE Protocol
                                    else if (Double.parseDouble(field3.getText()) == 112519.92 && field1.getText().contentEquals("Bread"))
                                    {
                                        EasterEgg ee = new EasterEgg("images/al1.gif", "sounds/al1.wav", "", "Ring bells! Bang the drums!!");

                                    }
                                    else if (Double.parseDouble(field3.getText()) == 19.75 && field1.getText().contentEquals("Witch"))
                                    {
                                        boolean found = false;
                                        for (Item item : curCart.getItems())
                                        {
                                            if (item.itemName.contentEquals("Duck") && item.itemPrice == 19.75)
                                            {
                                                found = true;
                                            }
                                        }
                                        if (found)
                                        {
                                            EasterEgg ee = new EasterEgg("images/mp1.gif", "sounds/mp1.wav", "", "She turned me into a newt!!");
                                        }
                                    }
                                    else if (Double.parseDouble(field3.getText()) == 19.75 && field1.getText().contentEquals("Duck"))
                                    {
                                        boolean found = false;
                                        for (Item item : curCart.getItems())
                                        {
                                            if (item.itemName.contentEquals("Witch") && item.itemPrice == 19.75)
                                            {
                                                found = true;
                                            }
                                        }
                                        if (found)
                                        {
                                            EasterEgg ee = new EasterEgg("images/mp1.gif", "sounds/mp1.wav", "", "She turned me into a newt!!");
                                        }

                                    }
                                    else if (Double.parseDouble(field3.getText()) == 0.02 && field1.getText().contentEquals("Jango Fett"))
                                    {
                                        EasterEgg ee = new EasterEgg("images/sw1.gif", "sounds/sw1.wav", "I'm just a simple man, trying to make my way in the universe.", "");
                                    }//end if EE Protocol

                                    Item tempItem = new Item(tempID, upc, field1.getText().replaceAll("'", " "), Double.parseDouble(field3.getText()), Double.parseDouble(field2.getText()), true, 852, 0, "", "", 1, false, 0, false);
                                    curCart.addItem(tempItem);
                                    guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                    displayChangeDue = false;
                                }//end else
                            }//end else
                        }//end else
                    }//end if
                    updateCartScreen();

                }
                else
                {//No employee Selected!
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        };//end otcButtonAction

        upsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field3 = new JTextField();
                    Object[] message =
                    {
                        "Amount:", field3
                    };
                    field3.setText("");
                    field3.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "UPS Package Information", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        if (!validateDouble(field3.getText()))
                        {//check price
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Amount is invalid.");
                        }
                        else
                        {//price is good
                            // randomItemCntr++;
                            DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
                            Date date = new Date();
                            String tempID;
                            tempID = dateFormat.format(date);
                            System.out.println(tempID);
                            String upc = 'U' + tempID;
                            Item tempItem = new Item(tempID, upc, "UPS Package", Double.parseDouble(field3.getText()), Double.parseDouble(field3.getText()), false, 860, 0, "", "", 1, false, 0, false);
                            curCart.addItem(tempItem);
                            guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                            displayChangeDue = false;
                        }//end else

                    }//end else

                    updateCartScreen();

                }
                else
                {//No employee Selected!
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        }
        );//end upsButtonAction

        noSaleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    checkout.beginNoSaleCheckout((String) employeeSelectionHeader.getText().substring(14));

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end NoSaleAction
        splitTenderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!employeeSelectionHeader.getText().substring(14).contentEquals(empList2.getSelectedItem().toString()))
                    {
                        if (!curCart.isEmpty())
                        {
                            if (!checkLunchSplitTender())
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Split Tender with \"LUNCH\" not allowed.");
                            }
                            else
                            {
                                if (curCart.getTotalPrice() < 0)
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                                }
                                else
                                {
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
                                            if (validateDouble(field1.getText()))
                                            {
                                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field7.getText()) - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText());
                                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                                            }
                                            else
                                            {
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
                                            if (validateDouble(field2.getText()))
                                            {
                                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field7.getText()) - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText());
                                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                                            }
                                            else
                                            {
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
                                            if (validateInteger(field3.getText()))
                                            {
                                                //do nothing? its a valid check number
                                            }
                                            else
                                            {
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
                                            if (validateDouble(field4.getText()))
                                            {
                                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field7.getText()) - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText());
                                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                                            }
                                            else
                                            {
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
                                            if (validateInteger(field5.getText()))
                                            {
                                                //do nothing? its a valid check number
                                            }
                                            else
                                            {
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
                                            if (validateDouble(field6.getText()))
                                            {
                                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field7.getText()) - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText());
                                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                                            }
                                            else
                                            {
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
                                            if (validateDouble(field7.getText()))
                                            {
                                                double remaining = curCart.getTotalPrice() - Double.parseDouble(field7.getText()) - Double.parseDouble(field1.getText()) - Double.parseDouble(field2.getText()) - Double.parseDouble(field4.getText()) - Double.parseDouble(field6.getText());
                                                splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", remaining));
                                            }
                                            else
                                            {
                                                field7.setText("0.00");
                                            }//end else
                                        }//end focusLost
                                    });//end field7Listener

                                    splitTenderTotal.setText("Total: $ " + String.format("%.2f", curCart.getTotalPrice()));
                                    splitTenderRemaining.setText("Remaining: $" + String.format("%.2f", curCart.getTotalPrice()));
                                    field2.setText(previousDate);
                                    Object[] message =
                                    {
                                        "Cash:", field1,
                                        "Check 1:", field2,
                                        "Check 1#:", field3,
                                        "Check 2:", field4,
                                        "Check 2#:", field5,
                                        "Credit 1:", field6,
                                        "Debit 1:", field7,
                                        splitTenderRemaining, splitTenderTotal
                                    };
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
                                    if (option == JOptionPane.OK_OPTION)
                                    {
                                        if (!validateDouble(field4.getText()) || !validateDouble(field7.getText()) || !validateDouble(field2.getText()) || !validateDouble(field1.getText()) || !validateDouble(field6.getText()) || !validateDouble(field6.getText()) || !validateInteger(field3.getText()) || !validateInteger(field5.getText()))
                                        {
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "Improper text in fields.");
                                        }
                                        else
                                        {
                                            double remaining = Double.parseDouble(field7.getText()) + Double.parseDouble(field6.getText()) + Double.parseDouble(field4.getText()) + Double.parseDouble(field2.getText()) + Double.parseDouble(field1.getText());
                                            remaining = round(remaining);
                                            // System.out.println(remaining);
                                            // System.out.println(curCart.getTotalPrice());
                                            if (remaining < curCart.getTotalPrice())
                                            {
                                                //must have some name
                                                JFrame message1 = new JFrame("");
                                                JOptionPane.showMessageDialog(message1, "Totals do not match.");

                                            }
                                            else
                                            {
                                                if (Double.parseDouble(field7.getText()) > 0 && Double.parseDouble(field6.getText()) > 0)
                                                {
                                                    JFrame message2 = new JFrame("");
                                                    JOptionPane.showMessageDialog(message2, "You can only use either credit or debit, not both!");
                                                }
                                                else if (curCart.getTotalPrice() < Double.parseDouble(field7.getText()))
                                                {
                                                    JFrame message2 = new JFrame("");
                                                    JOptionPane.showMessageDialog(message2, "Debit Card amount MUST be LESS THAN the TOTAL amount of the Cart!");
                                                }
                                                else if (curCart.getTotalPrice() < Double.parseDouble(field6.getText()))
                                                {
                                                    JFrame message2 = new JFrame("");
                                                    JOptionPane.showMessageDialog(message2, "Credit Card amount MUST be LESS THAN the TOTAL amount of the Cart!");
                                                }
                                                else
                                                {
                                                    double amtReceived = Double.parseDouble(field7.getText()) + Double.parseDouble(field1.getText()) + Double.parseDouble(field2.getText()) + Double.parseDouble(field4.getText()) + Double.parseDouble(field6.getText());
                                                    amtReceived = round(amtReceived);
                                                    double change = amtReceived - curCart.getTotalPrice();
                                                    change = round(change);
                                                    changeDue.setText("Change Due: $" + String.format("%.2f", change));
                                                    String goodCheckout = checkout.beginSplitTenderCheckout(curCart, Double.parseDouble(field1.getText()), Double.parseDouble(field7.getText()), Double.parseDouble(field6.getText()), Double.parseDouble(field2.getText()), Double.parseDouble(field4.getText()), field3.getText(), field5.getText(), (String) employeeSelectionHeader.getText().substring(14), guiItems, myself, (String) empList2.getSelectedItem());
                                                    if (goodCheckout.contentEquals("SMITHSAPPROVEDCODE"))
                                                    {
                                                        changeDue.setText("Change Due: $" + String.format("%.2f", change));
                                                        displayChangeDue = true;
                                                    }
                                                    else
                                                    {
                                                        JFrame message1 = new JFrame("");
                                                        JOptionPane.showMessageDialog(message1, "Card Error:\n" + goodCheckout);
                                                    }
                                                    updateCartScreen();
                                                }//end else
                                            }
                                        }//end else
                                    }//end if
                                }//end check for negative amount
                            }//end checkSplitTenderLunch()
                        }//end if isNotEmpty
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "You cannot check out yourself!");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
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
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!employeeSelectionHeader.getText().substring(14).contentEquals(empList2.getSelectedItem().toString()))
                    {
                        if (!curCart.isEmpty())
                        {
                            if (!checkLunch())
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cannot checkout \"LUNCH\" without employee selected.");
                            }
                            else
                            {

                                JFrame textInputFrame = new JFrame("");
                                JLabel cashTotal = new JLabel("Total: $", SwingConstants.RIGHT);
                                JTextField field1 = new JTextField();
                                field1.addFocusListener(new java.awt.event.FocusAdapter() {
                                    public void focusLost(java.awt.event.FocusEvent evt) {
                                        field1.setSelectionStart(0);
                                        field1.setSelectionEnd(12);
                                        if (!validateDouble(field1.getText()))
                                        {
                                            field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                                        }//end if
                                    }//end focusGained
                                });
                                cashTotal.setText("Total: $ " + String.format("%.2f", curCart.getTotalPrice()));
                                Object[] message =
                                {
                                    "Cash Amount:", field1, cashTotal
                                };
                                field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                                field1.setSelectionStart(0);
                                field1.setSelectionEnd(8);

                                if (curCart.getTotalPrice() < 0)
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                                }
                                else
                                {
                                    field1.addAncestorListener(new RequestFocusListener());
                                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Cash Amount", JOptionPane.OK_CANCEL_OPTION);
                                    if (option == JOptionPane.OK_OPTION)
                                    {
                                        if (!validateDouble(field1.getText()))
                                        {
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "Improper cash value.");
                                        }
                                        else
                                        {
                                            double amtReceived = Double.parseDouble(field1.getText());
                                            amtReceived = round(amtReceived);
                                            if (amtReceived < curCart.getTotalPrice())
                                            {
                                                JFrame message1 = new JFrame("");
                                                JOptionPane.showMessageDialog(message1, "Not enough cash.");
                                            }
                                            else
                                            {

                                                double change = amtReceived - curCart.getTotalPrice();
                                                change = round(change);
                                                changeDue.setText("Change Due: $" + String.format("%.2f", change));
                                                displayChangeDue = true;
                                                checkout.beginCashCheckout(curCart, amtReceived, employeeSelectionHeader.getText().substring(14), guiItems, myself, (String) empList2.getSelectedItem());
                                                updateCartScreen();

                                            }//end else
                                        }//end else
                                    }//end if
                                }
                            }
                        }
                        else if (!refundCart.isEmpty())
                        {
                            if (refundCart.getTotalPrice() < 0)
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                            }
                            else
                            {
                                boolean isItemToRefund = false;
                                boolean isCartConditionsMet = true;
                                for (RefundItem item : refundCart.getRefundItems())
                                {
                                    if (item.isRefundAllActive() || item.isRefundTaxOnlyActive())
                                    {
                                        isItemToRefund = true;
                                    }
                                    if (item.quantityBeingRefunded > 0 && !item.isRefundAllActive() && !item.isRefundTaxOnlyActive())
                                    {
                                        //Houston we have a problem.
                                        isCartConditionsMet = false;
                                    }

                                }
                                if (isItemToRefund && isCartConditionsMet)
                                {
                                    checkout.beginRefundCashCheckout(refundCart, employeeSelectionHeader.getText().substring(14), guiRefundItems, myself);

                                }
                                else if (isCartConditionsMet)
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "An item in the cart is set to have quantity refunded, but no type of refund is selected.. Please set Quantity to refund to zero for that item or add a refund case.");
                                }
                            }
                        }
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "You cannot check out yourself!");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }

                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER

            }//end actionPerformed

        });//end cashButtonAction

        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!employeeSelectionHeader.getText().substring(14).contentEquals(empList2.getSelectedItem().toString()))
                    {
                        if (!curCart.isEmpty())
                        {
                            if (!checkLunch())
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cannot checkout \"LUNCH\" without employee selected.");
                            }
                            else
                            {
                                if (curCart.getTotalPrice() < 0)
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                                }
                                else
                                {
                                    JFrame textInputFrame = new JFrame("");
                                    JLabel checkTotal = new JLabel("Total: $", SwingConstants.RIGHT);
                                    JTextField field1 = new JTextField();
                                    JTextField field2 = new JTextField();
                                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                                        public void focusLost(java.awt.event.FocusEvent evt) {
                                            field1.setSelectionStart(0);
                                            field1.setSelectionEnd(12);
                                            if (!validateDouble(field1.getText()))
                                            {
                                                field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                                            }//end if
                                        }//end focusGained
                                    });
                                    field2.addFocusListener(new java.awt.event.FocusAdapter() {
                                        public void focusLost(java.awt.event.FocusEvent evt) {
                                            field2.setSelectionStart(0);
                                            field2.setSelectionEnd(12);
                                            if (field2.getText().isEmpty())
                                            {
                                                field2.setText(String.format("0"));
                                            }//end if
                                        }//end focusGained
                                    });
                                    checkTotal.setText("Total: $ " + String.format("%.2f", curCart.getTotalPrice()));
                                    Object[] message =
                                    {
                                        "Check Amount:", field1, "Check #", field2, checkTotal
                                    };
                                    field1.setText(String.format("%.2f", curCart.getTotalPrice()));
                                    field1.setSelectionStart(0);
                                    field1.setSelectionEnd(8);
                                    field2.setText("0");
                                    field2.setSelectionStart(0);
                                    field2.setSelectionEnd(2);

                                    field1.addAncestorListener(new RequestFocusListener());
                                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Check Value", JOptionPane.OK_CANCEL_OPTION);
                                    if (option == JOptionPane.OK_OPTION)
                                    {
                                        if (!validateDouble(field1.getText()))
                                        {
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "Improper check value.");
                                        }
                                        else
                                        {
                                            double amtReceived = Double.parseDouble(field1.getText());
                                            amtReceived = round(amtReceived);
                                            if (amtReceived < curCart.getTotalPrice())
                                            {
                                                JFrame message1 = new JFrame("");
                                                JOptionPane.showMessageDialog(message1, "Check value to small.");
                                            }
                                            else
                                            {
                                                double change = amtReceived - curCart.getTotalPrice();
                                                change = round(change);
                                                changeDue.setText("Change Due: $" + String.format("%.2f", change));

                                                if (field2.getText().toUpperCase().contains("PAYCHECK"))
                                                {
                                                    if (employeeSelectionHeader.getText().contains("Smith, Hollie"))
                                                    {
                                                        if (curCart.getTotalPrice() == amtReceived)
                                                        {
                                                            checkout.beginStoreCheckCheckout(curCart, amtReceived, employeeSelectionHeader.getText().substring(14), field2.getText(), myself, guiItems, (String) empList2.getSelectedItem());
                                                            displayChangeDue = true;
                                                        }
                                                        else
                                                        {
                                                            JFrame message1 = new JFrame("");
                                                            JOptionPane.showMessageDialog(message1, "When using PAYCHECK option you MUST match the amount paid to the total due.");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        JFrame message1 = new JFrame("");
                                                        JOptionPane.showMessageDialog(message1, "Only the Store Manager can use PAYCHECK inside Check # Field.");
                                                    }
                                                }
                                                else
                                                {
                                                    checkout.beginCheckCheckout(curCart, amtReceived, employeeSelectionHeader.getText().substring(14), field2.getText(), myself, guiItems, (String) empList2.getSelectedItem());
                                                    displayChangeDue = true;
                                                }
                                                updateCartScreen();
                                            }//end else
                                        }//end else
                                    }//end if  
                                }//end check for negative balance
                            }//end check for LUNCH
                        }//end if isNotEmpty
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "You cannot check out yourself!");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end checkButtonAction

        creditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!employeeSelectionHeader.getText().substring(14).contentEquals(empList2.getSelectedItem().toString()))
                    {
                        if (!curCart.isEmpty())
                        {
                            if (!checkLunch())
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cannot checkout \"LUNCH\" without employee selected.");
                            }
                            else
                            {
                                if (curCart.getTotalPrice() < 0)
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                                }
                                else
                                {
                                    changeDue.setText("Change Due: $" + String.format("%.2f", 0.00));
                                    displayChangeDue = true;
                                    String goodCheckout = checkout.beginCreditCheckout(curCart, curCart.getTotalPrice(), employeeSelectionHeader.getText().substring(14), myself, guiItems, (String) empList2.getSelectedItem());
                                    if (goodCheckout.contentEquals("SMITHSAPPROVEDCODE"))
                                    {
                                        displayChangeDue = true;
                                    }
                                    else
                                    {
                                        displayChangeDue = false;
                                        JFrame message1 = new JFrame("");
                                        JOptionPane.showMessageDialog(message1, "Card Error:\n" + goodCheckout);
                                    }
                                    updateCartScreen();
                                }
                            }
                        }
                        else if (!refundCart.isEmpty())
                        {
                            if (refundCart.getTotalPrice() < 0)
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                            }
                            else
                            {
                                boolean isItemToRefund = false;
                                for (RefundItem item : refundCart.getRefundItems())
                                {
                                    if (item.isRefundAllActive() || item.isRefundTaxOnlyActive())
                                    {
                                        isItemToRefund = true;
                                    }
                                }
                                if (isItemToRefund)
                                {
                                    String goodCheckout = checkout.beginRefundCardCheckout(refundCart, employeeSelectionHeader.getText().substring(14), guiRefundItems, myself);
                                    if (goodCheckout.contentEquals("SMITHSAPPROVEDCODE"))
                                    {
                                        displayChangeDue = true;
                                    }
                                    else
                                    {
                                        displayChangeDue = false;
                                        JFrame message1 = new JFrame("");
                                        JOptionPane.showMessageDialog(message1, "Card Error:\n" + goodCheckout);
                                    }
                                }
                            }
                        }//end cartIsNotEmpty
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "You cannot check out yourself!");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end creditButtonAction

        debitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!employeeSelectionHeader.getText().substring(14).contentEquals(empList2.getSelectedItem().toString()))
                    {
                        if (!curCart.isEmpty())
                        {
                            if (!checkLunch())
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cannot checkout \"LUNCH\" without employee selected.");
                            }
                            else
                            {
                                if (curCart.getTotalPrice() < 0)
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                                }
                                else
                                {
                                    changeDue.setText("Change Due: $" + String.format("%.2f", 0.00));
                                    displayChangeDue = true;
                                    String goodCheckout = checkout.beginDebitCheckout(curCart, curCart.getTotalPrice(), employeeSelectionHeader.getText().substring(14), myself, guiItems, (String) empList2.getSelectedItem());
                                    if (goodCheckout.contentEquals("SMITHSAPPROVEDCODE"))
                                    {
                                        displayChangeDue = true;
                                    }
                                    else
                                    {
                                        displayChangeDue = false;
                                        JFrame message1 = new JFrame("");
                                        JOptionPane.showMessageDialog(message1, "Card Error:\n" + goodCheckout);
                                    }
                                    updateCartScreen();
                                }
                            }
                        }
                        else if (!refundCart.isEmpty())
                        {
                            boolean isItemToRefund = false;
                            for (RefundItem item : refundCart.getRefundItems())
                            {
                                if (item.isRefundAllActive() || item.isRefundTaxOnlyActive())
                                {
                                    isItemToRefund = true;
                                }
                            }
                            if (isItemToRefund)
                            {
                                checkout.beginRefundCardCheckout(refundCart, employeeSelectionHeader.getText().substring(14), guiRefundItems, myself);

                            }

                        }//end cartIsNotEmpty
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "You cannot check out yourself!");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end debitButtonAction

        reprintReceiptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                displayChangeDue = false;
                if (!previousReceipt.contentEquals("EMPTY"))
                {
                    checkout.reprintReceipt(previousReceipt);
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end reprintReceiptButtonAction

        //ARPayment Button Listener
        arPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {

                    JFrame textInputFrame = new JFrame("");
                    JFrame textInputFrame2 = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();
                    JTextField field4 = new JTextField();
                    JTextField field5 = new JTextField();
                    Object[] message =
                    {
                        "Account Name:", field1,
                        "Last Name:", field2,
                        "First Name:", field3,
                        "DOB: ex: 022411", field4
                    };
                    Object[] message2 =
                    {
                        "Amount To Pay:", field5
                    };
                    field1.setText("");
                    field2.setText("");
                    field3.setText("");
                    field4.setText("");
                    field5.setText("");
                    String accountName = "";
                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Account Information", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        if (field1.getText().isEmpty() && field2.getText().isEmpty() && field3.getText().isEmpty() && field4.getText().isEmpty())
                        {
                            //do nothing, they clicked OK with everything blank
                        }
                        else
                        {
                            if (field1.getText().contentEquals("BTITUDE"))
                            {
                                EasterEgg ee = new EasterEgg("images/bat.gif", "sounds/batman.wav", "", "You think muscles are big, you haven't seen my brain!");
                            }
                            else if (field1.getText().contentEquals("Prince") && field2.getText().contentEquals("Smith") && field3.getText().contentEquals("Anduin") && field4.getText().contentEquals("091318"))
                            {
                                EasterEgg ee = new EasterEgg("images/as1.gif", "sounds/as1.wav", "", "Sometimes we must fight for what we believe in.");
                            }
                            else if (field1.getText().contentEquals("Princess") && field2.getText().contentEquals("Smith") && field3.getText().contentEquals("Kieryn") && field4.getText().contentEquals("022411"))
                            {
                                EasterEgg ee = new EasterEgg("images/ks1.gif", "sounds/ks1.wav", "", "Sometimes our strengths lie beneath the surface.");
                            }
                            else if (field1.getText().contentEquals("Apollo") && field2.getText().contentEquals("Smith") && field3.getText().contentEquals("Andrew") && field4.getText().contentEquals("111789"))
                            {
                                EasterEgg ee = new EasterEgg("images/fly1.gif", "sounds/fly1.wav", "When you run the marathon, you run against the distance, not against the other runners and not against the time.\n You are all the loves of my life and I do this all for you.", "");
                            }
                            else if (field1.getText().contentEquals("Starbuck") && field2.getText().contentEquals("Smith") && field3.getText().contentEquals("Hollie") && field4.getText().contentEquals("030986"))
                            {
                                EasterEgg ee = new EasterEgg("images/wwa.gif", "sounds/wwa.wav", "I would grant all your wishes \n"
                                        + "If you promised me a thousand kisses \n"
                                        + "I will never, love another, like you\n"
                                        + "So give me all your secrets \n"
                                        + "Your fear and doubts, honey you don’t need them\n"
                                        + "I will never, find another, like you\n"
                                        + "And the airs getting thin \n"
                                        + "Where the wings meet the wind\n"
                                        + "We see it, we can feel it and we know this\n"
                                        + "I believe in something more\n"
                                        + "All the days that came before\n"
                                        + "Led us right to where we are \n"
                                        + "Right to where we are\n"
                                        + "It’s all written in the stars\n"
                                        + "We’ve already come so far \n"
                                        + "And we can’t change who we are \n"
                                        + "Ah ah who we are\n"
                                        + "Oh who we are\n"
                                        + "Oh who we are \n"
                                        + "I don’t have all the answers\n"
                                        + "But right now is all the matters\n"
                                        + "I could never, love another, like I loved you \n"
                                        + "And we don’t have to understand\n"
                                        + "Fate always has the upper hand \n"
                                        + "And fate choose me and you.", "");
                            }
                            String[] choices = Database.getARList(field1.getText(), field2.getText(), field3.getText(), field4.getText());
                            if (choices != null)
                            {
                                accountName = (String) JOptionPane.showInputDialog(null, "Choose now...",
                                        "Choose AR Account", JOptionPane.QUESTION_MESSAGE, null, // Use
                                        // default
                                        // icon
                                        choices, // Array of choices
                                        choices[0]); // Initial choice
                                if (accountName != null)
                                {
                                    accountName = accountName.substring(0, accountName.indexOf("Current"));
                                    field5.addAncestorListener(new RequestFocusListener());
                                    int option2 = JOptionPane.showConfirmDialog(textInputFrame2, message2, "Enter Amount To Pay", JOptionPane.OK_CANCEL_OPTION);
                                    if (option2 == JOptionPane.OK_OPTION)
                                    {
                                        String temp = field5.getText();
                                        if (!validateDouble(temp))
                                        {
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "Not a valid amount.");
                                        }
                                        else
                                        {
                                            double price = Double.parseDouble(temp);
                                            DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
                                            Date date = new Date();
                                            String tempID;
                                            tempID = dateFormat.format(date);
                                            // System.out.println(tempID);
                                            String upc = "A" + tempID;
                                            if (!curCart.containsAP(accountName))
                                            {
                                                Item tempItem = new Item(tempID, upc, accountName, price, price, false, 853, 0, "", "", 1, false, 0, false);
                                                curCart.addItem(tempItem);
                                                guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                                displayChangeDue = false;
                                            }
                                            else
                                            {
                                                JFrame message1 = new JFrame("");
                                                JOptionPane.showMessageDialog(message1, "Already an account payment for that account in cart.");
                                            }
                                        }
                                    }//end if
                                    updateCartScreen();
                                }//end if accountname not null
                            }
                            else
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "No such account found.");
                            }//end else

                        }//end else
                    }//end if OK_OPTION

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }

                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//end arPaymentButtonAction

        //DME Payment Button Listener
        dmePaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    JFrame textInputFrame = new JFrame("");
                    JFrame textInputFrame2 = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();
                    JTextField field4 = new JTextField();
                    JTextField field5 = new JTextField();
                    Object[] message =
                    {
                        "Account Name:", field1,
                        "Last Name:", field3,
                        "First Name:", field2,
                        "DOB: ex: 102718", field4
                    };
                    Object[] message2 =
                    {
                        "Amount To Pay:", field5
                    };
                    field1.setText("");
                    field2.setText("");
                    field3.setText("");
                    field4.setText("");
                    field5.setText("");
                    String accountName = "";
                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Account Information", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        if (field1.getText().isEmpty() && field2.getText().isEmpty() && field3.getText().isEmpty() && field4.getText().isEmpty())
                        {
                            //do nothing, they clicked OK with everything blank
                        }
                        else
                        {
                            String[] choices = Database.getDMEList(field1.getText(), field2.getText(), field3.getText(), field4.getText());
                            if (choices != null)
                            {
                                accountName = (String) JOptionPane.showInputDialog(null, "Choose now...",
                                        "Choose DME Account", JOptionPane.QUESTION_MESSAGE, null, // Use
                                        // default
                                        // icon
                                        choices, // Array of choices
                                        choices[0]); // Initial choice
                                if (accountName != null)
                                {
                                    field5.addAncestorListener(new RequestFocusListener());
                                    int option2 = JOptionPane.showConfirmDialog(textInputFrame2, message2, "Enter Amount To Pay", JOptionPane.OK_CANCEL_OPTION);
                                    if (option2 == JOptionPane.OK_OPTION)
                                    {
                                        String temp = field5.getText();
                                        if (!validateDouble(temp))
                                        {
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "Not a valid amount.");
                                        }
                                        else
                                        {
                                            double price = Double.parseDouble(temp);
                                            DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
                                            Date date = new Date();
                                            String tempID;
                                            tempID = dateFormat.format(date);
                                            // System.out.println(tempID);
                                            String upc = "D" + tempID;
                                            //boolean taxable, int category, int rxNumber, String insurance, String filldate, int quantity,boolean isRX)
                                            if (!curCart.containsAP(accountName))
                                            {
                                                Item tempItem = new Item(tempID, upc, accountName.substring(0, accountName.indexOf("Current Bal") - 1), price, price, false, 854, 0, "", "", 1, false, 0, false);
                                                curCart.addItem(tempItem);
                                                guiItems.add(new GuiCartItem(tempItem, curCart.getItems().size() * 15, jPanel1, curCart, myself));
                                                displayChangeDue = false;
                                            }
                                            else
                                            {
                                                JFrame message1 = new JFrame("");
                                                JOptionPane.showMessageDialog(message1, "Already an account payment for that account in cart.");
                                            }
                                        }
                                    }//end if
                                    updateCartScreen();
                                }//end if accountname not null
                            }
                            else
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "No such account found.");
                            }//end else

                        }//end else
                    }//end if OK_OPTION

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//end dmePaymentButtonAction
        beginSplitTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (curCart.getItems().size() > 1)
                    {
                        for (GuiCartItem item : guiItems)
                        {//this hides all the items GUI options that shouldn't be active during this time, and SHOWS what should.
                            item.splitTicketActivated();
                        }
                        splitTicketBeginHideContent();
                        beginSplitTicketButton.setVisible(false);//Hide me, my cancel button will take my place.
                        cancelSplitTicketButton.setVisible(true);
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "You need at least two items to them into different tickets.");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
            }
        });
        cancelSplitTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    for (GuiCartItem item : guiItems)
                    {
                        item.splitTicketDeactivated();
                    }
                    for (Item item : curCart.getItems())
                    {
                        item.isSetToSplitSave = false;
                    }
                    splitTicketEndShowContent();
                    beginSplitTicketButton.setVisible(true);
                    cancelSplitTicketButton.setVisible(false);//Hide me, my begin button will take my place.

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
            }
        });

        paidOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    field1.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field1.setSelectionStart(0);
                            field1.setSelectionEnd(15);
                            if (field1.getText().isEmpty())
                            {
                                field1.setText("Description");
                            }//end if
                        }//end focusGained
                    });
                    field2.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            field2.setSelectionStart(0);
                            field2.setSelectionEnd(12);
                            if (!validateDouble(field2.getText()))
                            {
                                field2.setText(String.format("0"));
                            }//end if
                        }//end focusGained
                    });
                    Object[] message =
                    {
                        "Description:", field1, "Amount: $", field2
                    };
                    field1.setText(String.format("Description"));
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(15);
                    field2.setText("0.00");
                    field2.setSelectionStart(0);
                    field2.setSelectionEnd(4);

                    field1.addAncestorListener(new RequestFocusListener());
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Paid Out Menu", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        if (!validateDouble(field2.getText()))
                        {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Improper paid out value.");
                        }
                        else
                        {
                            double amtReceived = Double.parseDouble(field2.getText());
                            amtReceived = round(amtReceived);
                            if (amtReceived <= 0)
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Paid out must be greater than 0.");
                            }
                            else
                            {
                                if (field1.getText().isEmpty())
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Paid out must have description.");
                                }
                                else
                                {
                                    Object[] message2 =
                                    {
                                        "Are you sure?\nDescription: " + field1.getText(), "Amount: $ " + field2.getText()
                                    };

                                    int option2 = JOptionPane.showConfirmDialog(textInputFrame, message2, "Paid Out Menu", JOptionPane.OK_CANCEL_OPTION);
                                    if (option2 == JOptionPane.OK_OPTION)
                                    {
                                        checkout.beginPaidOut(field1.getText().replaceAll("'", " "), Double.parseDouble(field2.getText()));
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

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed

        });//end paidOutButtonAction

        //ARPayment Button Listener
        chargeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!employeeSelectionHeader.getText().contains("NONE"))
                {
                    if (!employeeSelectionHeader.getText().substring(14).contentEquals(empList2.getSelectedItem().toString()))
                    {
                        if (!curCart.isEmpty() && !curCart.containsChargedItem())
                        {
                            if (!checkLunch())
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Cannot checkout \"LUNCH\" without employee selected.");
                            }
                            else
                            {
                                if (curCart.getTotalPrice() < 0)
                                {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Cannot have negative checkout amount.");
                                }
                                else
                                {
                                    JFrame textInputFrame = new JFrame("");
                                    JTextField field1 = new JTextField();
                                    JTextField field2 = new JTextField();
                                    JTextField field3 = new JTextField();
                                    JTextField field4 = new JTextField();
                                    Object[] message =
                                    {
                                        "Account Name:", field1,
                                        "Last Name:", field2,
                                        "First Name:", field3,
                                        "DOB: ex. 091318", field4
                                    };
                                    field1.setText("");
                                    field2.setText("");
                                    field3.setText("");
                                    field4.setText("");
                                    String accountName = "";
                                    field1.addAncestorListener(new RequestFocusListener());
                                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Enter Account Information", JOptionPane.OK_CANCEL_OPTION);
                                    if (option == JOptionPane.OK_OPTION)
                                    {
                                        if (field1.getText().isEmpty() && field2.getText().isEmpty() && field3.getText().isEmpty() && field4.getText().isEmpty())
                                        {
                                            //do nothing, they clicked OK with everything blank
                                        }
                                        else
                                        {
                                            String[] choices = Database.getARList(field1.getText(), field2.getText(), field3.getText(), field4.getText());
                                            for (int i = 0; i < choices.length; i++)
                                            {//this removes current from the display when they are charging TO account
                                                choices[i] = choices[i].substring(0, choices[i].indexOf("Current"));
                                            }
                                            if (choices != null)
                                            {
                                                accountName = (String) JOptionPane.showInputDialog(null, "Choose now...",
                                                        "Choose AR Account", JOptionPane.QUESTION_MESSAGE, null, // Use
                                                        // default
                                                        // icon
                                                        choices, // Array of choices
                                                        choices[0]); // Initial choice
                                                if (accountName != null && Database.checkFrozenAccount(accountName.substring(0, accountName.indexOf(" "))))
                                                {
                                                    JFrame message1 = new JFrame("");
                                                    JOptionPane.showMessageDialog(message1, "This account has been FROZEN. Please speak to Hollie. Customer CANNOT charge!");
                                                }
                                                else if (accountName != null)
                                                {
                                                    changeDue.setText("Change Due: $0.00");
                                                    displayChangeDue = true;
                                                    checkout.beginChargeCheckout(curCart, accountName, employeeSelectionHeader.getText().substring(14), myself, guiItems, (String) empList2.getSelectedItem());
                                                    updateCartScreen();
                                                }//end if accountname not null
                                            }
                                            else
                                            {
                                                JFrame message1 = new JFrame("");
                                                JOptionPane.showMessageDialog(message1, "No such account found.");
                                            }//end else

                                        }//end else
                                    }//end if OK_OPTION
                                }//end if negative amount in cart
                            }//end else checkLunch()
                        }
                        else
                        {
                            if (curCart.containsChargedItem())
                            {
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Please remove account payments before charging.");
                            }
                        }
                    }
                    else
                    {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "You cannot check out yourself!");
                    }
                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Select an employee first!");
                }
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end chargeButtonAction

        cancelRefundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                refundCart.voidCart();
                for (GuiRefundCartItem item : guiRefundItems)
                {
                    item.removeAllGUIData();
                }
                checkForAdminButtonVisible(activeClerksPasscode);
                guiRefundItems.clear();
                displayChangeDue = false;
                refundOver();
                resizeRefundCartWindow();
                updateCartScreen();
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end cancelRefundAction

        activateDisplayButton.setVisible(true);
        this.add(activateDisplayButton);
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
        debitButton.setVisible(true);
        this.add(debitButton);
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
        upsButton.setVisible(true);
        this.add(upsButton);

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

        if (refundCart.isEmpty())
        {
            discountHeader.setText("Discount: ");
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

            if (curCart.isEmpty())
            {
                resaveTicket.setVisible(false);
                isMassPreCharged = false;
                massPrechargeButton.setBackground(new Color(255, 0, 0));
                isMassSplitting = false;
                massSplitTicketButton.setBackground(new Color(255, 0, 0));
                massSplitTicketButton.setText("Mass Off");
            }
        }
        else
        {
            discountHeader.setText("Qty to Refund: ");
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

        if (displayChangeDue)
        {
            changeDue.setVisible(true);
            if (displayActive)
            {
                display.printLines("****THANK YOU!****", String.format("Change: $%.2f", Double.parseDouble(changeDue.getText().substring(changeDue.getText().indexOf('$') + 1))));
            }
        }
        else
        {
            changeDue.setVisible(false);
        }

        this.add(changeDue);
        estimatedLunchTotalLabel.setText(String.format("Lunch: $%.2f", estimatedLunchTotal));
        estimatedCheckTotalLabel.setText(String.format("Checks: $%.2f", estimatedCheckTotal));
        estimatedCashTotalLabel.setText(String.format("Cash: $%d", estimatedCashTotal));
        estimatedCoinTotalLabel.setText(String.format("Coin: $%.2f", estimatedCoinTotal));

        if (curCart.getRequiresRepaint())
        {
            int y = 15;
            for (GuiCartItem item : guiItems)
            {
                item.reposition(y);
                y += 15;
            }
            curCart.setRequiresRepaint(false);
        }

        jPanel1.setVisible(true);
        this.setVisible(true);

        textField.requestFocusInWindow();
    }//end updateCartScreen

    public void checkForAdminButtonVisible(int passCode) {
        menuBar.updateVisible(Database.getEmployeePermissionByCode(passCode));
    }

    public void setData() {
        this.setTitle("Smith's Super-Aid POS - Developed by: Andrew & Hollie Smith");
        menuBar = new TopMenuBar(this);//Hollie's Menu Bar!
        this.setJMenuBar(menuBar);

        checkout = new CheckoutHandler();

        curCart = new Cart();//new cart because program just launched!
        String[] employeeStrings = Database.getEmployeesFromDatabase();
        employeeSelectionHeader.setBounds(120, 825, 400, 30);
        employeeSelectionHeader.setVisible(true);
        this.add(employeeSelectionHeader);
        String[] empStrings2 = new String[employeeStrings.length + 1];
        empStrings2[0] = "NO";
        for (int i = 1; i < employeeStrings.length + 1; i++)
        {
            empStrings2[i] = employeeStrings[i - 1];
        }
        empList2 = new JComboBox<String>(empStrings2);
        empList2.setSelectedIndex(0);
        empList2.setVisible(true);
        empList2.setBounds(100, 950, 200, 30);
        empList2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String employeeToCheckout = (String) empList2.getSelectedItem();
                System.out.println(employeeToCheckout);
                if (!employeeToCheckout.contentEquals("NO"))
                {
                    curCart.isEmpDiscountActive(true);
                    /*
                    for(Item item : curCart.getItems()){
                        if (!item.isRX() && item.getCategory() != 853 && item.getCategory() != 854) {
                        item.setEmployeeDiscount(true);
                        }
                    }*/
                    //curCart.updateTotal();
                    updateCartScreen();
                    for (GuiCartItem item : guiItems)
                    {
                        item.employeeSaleTriggered();
                    }

                }
                else
                {
                    curCart.isEmpDiscountActive(false);
                    /* for(Item item : curCart.getItems()){
                        if (!item.isRX() && item.getCategory() != 853 && item.getCategory() != 854) {
                        item.setEmployeeDiscount(false);
                        }
                    }*/
                    //curCart.updateTotal();
                    for (GuiCartItem item : guiItems)
                    {

                        item.employeeSaleCancelled();
                    }

                    // voidCarts();
                }
                //updateCartScreen();
                textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }//end actionPerformed
        });//end empListActionListener
        this.add(empList2);

        updateCartScreen();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (displayActive)
        {
            display.close();
        }
    }//GEN-LAST:event_formWindowClosing

    protected boolean validateRX(int length, int rxNumber, String insurance, String fillDate) {
        if (length == 7 && !curCart.containsRX(rxNumber, insurance, fillDate))
        {
            return true;
        }
        return false;
    }

    protected boolean validateDouble(String copay) {
        try
        {
            double cpay = Double.parseDouble(copay);
            if (cpay < 0)
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            return false;
        }//end catch
        return true;
    }

    protected boolean validateInteger(String integer) {
        try
        {
            int integ = Integer.parseInt(integer);
            if (integ < 0)
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            return false;
        }//end catch
        return true;
    }

    protected boolean validateDate(String fillDate) {
        String subString1, subString2, subString3;
        if (fillDate.length() != 6)
        {
            return false;
        }//end if not right length
        subString1 = fillDate.substring(0, 2);
        subString2 = fillDate.substring(2, 4);
        subString3 = fillDate.substring(4, 6);
        try
        {
            int month = Integer.parseInt(subString1);
            int day = Integer.parseInt(subString2);
            int year = Integer.parseInt(subString3);

            if (month < 1 || month > 12)
            {
                return false;
            }
            if (day < 1 || day > 31)
            {
                return false;
            }
            if (year < 0 || year > 99)
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
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

    public void loadTicketWithId(String id) {
        id = id.toUpperCase();
        //if it does, lets load it!
        curCart.loadCart(id);
        int i = 15;
        for (GuiCartItem item : guiItems)
        {
            item.removeAllGUIData();
        }
        guiItems.clear();
        for (Item item : curCart.getItems())
        {
            guiItems.add(new GuiCartItem(item, i, jPanel1, curCart, myself));
            i += 15;
        }

        displayChangeDue = false;
    }

    private void loadReceipt(String receiptNum) {
        //if it does, lets load it!
        refundCart.loadRefundCart(receiptNum);
        int i = 15;

        for (RefundItem item : refundCart.getRefundItems())
        {
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
        creditButton.setVisible(true);
        debitButton.setVisible(true);
        upsButton.setVisible(true);
        massPrechargeButton.setVisible(true);

        if (isMarchMadness)
        {
            mmButton.setVisible(true);
        }
        if (!displayActive)
        {
            activateDisplayButton.setVisible(true);
        }
        cancelRefundButton.setVisible(false);
        checkForAdminButtonVisible(activeClerksPasscode);
    }

    public void splitTicketBeginHideContent() {
        //Hide everything. It matters.   (easiest way to stop people from clicking stuff they shouldn't when trying to split)
        isSplitSavingActive = true;
        rxButton.setVisible(false);
        otcButton.setVisible(false);
        arPaymentButton.setVisible(false);
        splitTenderButton.setVisible(false);
        chargeButton.setVisible(false);
        voidButton.setVisible(false);
        noSaleButton.setVisible(false);
        loadTicket.setVisible(false);
        //  createTicket.setVisible(false);//Show this guy. It should still be seen. We need to allow a save.
        checkButton.setVisible(false);
        paidOutButton.setVisible(false);
        refundButton.setVisible(false);
        lookupReceiptByRXButton.setVisible(false);
        massDiscountButton.setVisible(false);
        reprintReceiptButton.setVisible(false);
        dmePaymentButton.setVisible(false);
        paperButton.setVisible(false);
        creditButton.setVisible(false);
        debitButton.setVisible(false);
        upsButton.setVisible(false);
        massPrechargeButton.setVisible(false);
        cashButton.setVisible(false);
        creditButton.setVisible(false);
        massSplitTicketButton.setVisible(true);

        if (isMarchMadness)
        {
            mmButton.setVisible(false);
        }
        //  if (!displayActive) {
        //      activateDisplayButton.setVisible(true);
        //  }
        //cancelRefundButton.setVisible(false);//Not needed
        // checkForAdminButtonVisible(activeClerksPasscode);
    }

    public void splitTicketEndShowContent() {
        //Hide everything. It matters.   (easiest way to stop people from clicking stuff they shouldn't when trying to split)
        isSplitSavingActive = false;
        rxButton.setVisible(true);
        otcButton.setVisible(true);
        arPaymentButton.setVisible(true);
        splitTenderButton.setVisible(true);
        chargeButton.setVisible(true);
        voidButton.setVisible(true);
        noSaleButton.setVisible(true);
        loadTicket.setVisible(true);
        //  createTicket.setVisible(false);//Show this guy. It should still be seen. We need to allow a save.
        checkButton.setVisible(true);
        paidOutButton.setVisible(true);
        refundButton.setVisible(true);
        lookupReceiptByRXButton.setVisible(true);
        massDiscountButton.setVisible(true);
        reprintReceiptButton.setVisible(true);
        dmePaymentButton.setVisible(true);
        paperButton.setVisible(true);
        creditButton.setVisible(true);
        debitButton.setVisible(true);
        upsButton.setVisible(true);
        massPrechargeButton.setVisible(true);
        cashButton.setVisible(true);
        creditButton.setVisible(true);
        massSplitTicketButton.setVisible(false);
        if (isMarchMadness)
        {
            mmButton.setVisible(true);
        }
        //if (!displayActive) {
        //     activateDisplayButton.setVisible(true);
        // }
        //cancelRefundButton.setVisible(false);//Not needed
        // checkForAdminButtonVisible(activeClerksPasscode);
    }

    public void voidCarts() {

        if (!receiptNum.isEmpty())
        {
            updateCartScreen();
            rxSignout();
            receiptNum = "";
        }
        curCart.voidCart();
        refundCart.voidCart();
        resaveTicket.setVisible(false);
        previousInsurance = "AARP";
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        Date date = new Date();
        previousDate = dateFormat.format(date);
        for (GuiCartItem item : guiItems)
        {
            item.removeAllGUIData();
        }
        for (GuiRefundCartItem item : guiRefundItems)
        {
            item.removeAllGUIData();
        }
        guiItems.clear();
        guiRefundItems.clear();
        empList2.setSelectedIndex(0);
        //displayChangeDue = false;//cant do this!
        updateCartScreen();
    }

    public void saveTicket(String id) {
        if (id != null && !id.isEmpty())
        {
            id = id.toUpperCase();
            if (!Database.checkDatabaseForTicket(id))
            {//check ID to see if it exists in database
                //if it doesnt, lets create it!
                boolean eefound = false;
                if (id.toUpperCase().contentEquals("DENNIS NEDRY"))
                {//EE Protocol
                    boolean itemFound = false;
                    for (Item item : curCart.getItems())
                    {
                        if (item.mutID.contentEquals("012849"))
                        {
                            itemFound = true;
                            eefound = true;
                            EasterEgg ee = new EasterEgg("images/jp2.gif", "sounds/jp2.wav", "", "Life finds a way!");
                        }//end if
                    }//end for all items
                    if (!itemFound)
                    {
                        eefound = true;
                        EasterEgg ee = new EasterEgg("images/jp1.gif", "sounds/jp1.wav", " \nIt would seem the Park, I mean CART is missing something...\n", "Ah Ah Ah, you did't say the magic word!");
                    }//not found!

                }
                else if (id.toUpperCase().contentEquals("ANTHONY EDWARD STARK"))
                {//EE Protocol
                    boolean itemFound = false;
                    for (Item item : curCart.getItems())
                    {
                        if (item.itemName.contentEquals("Mark") && item.itemPrice == 0.42)
                        {
                            itemFound = true;
                            eefound = true;
                            EasterEgg ee = new EasterEgg("images/im2.gif", "sounds/im2.wav", "", "I AM IRONMAN");
                        }
                    }
                    if (!itemFound)
                    {
                        eefound = true;
                        EasterEgg ee = new EasterEgg("images/im1.gif", "sounds/im1.wav", "", "Jarvis: Greetings sir!");
                    }//not found!

                }
                else if (id.toUpperCase().contentEquals("SIFO-DYAS"))
                {
                    for (Item item : curCart.getItems())
                    {
                        if (item.getPrice() == 0.02 && item.itemName.contentEquals("Jango Fett") && item.getQuantity() == 2)
                        {
                            eefound = true;
                        }
                    }
                    if (eefound)
                    {
                        EasterEgg ee = new EasterEgg("images/sw3.gif", "sounds/sw3.wav", "", "Negotiations with a lightsaber.");
                    }
                }
                else if (isHalloween && id.toUpperCase().contentEquals("THACKERY BINX"))
                {
                    boolean found1 = false;
                    boolean found2 = false;
                    for (Item item : curCart.getItems())
                    {
                        if (item.itemName.contentEquals("Come Little Children") && item.itemPrice == 3.00)
                        {
                            found1 = true;
                        }
                        if (item.itemName.contentEquals("I Put A Spell On You") && item.itemPrice == 16.93)
                        {
                            found2 = true;
                        }
                    }
                    if (found1 && found2)
                    {
                        EasterEgg ee = new EasterEgg("images/hp3.gif", "sounds/hp3.wav", "", "Okay then, let's go!");
                    }
                }
                else//end if EE Protocol
                if (!eefound)
                {
                    if (!isSplitSavingActive)
                    {
                        for (GuiCartItem item : guiItems)
                        {
                            item.removeAllGUIData();
                        }
                        guiItems.clear();
                        if (curCart.isEmpDiscountActive)
                        {
                            curCart.isEmpDiscountActive(false);
                            empList2.setSelectedIndex(0);
                        }

                        curCart.storeCart(id, false);
                        resizeCartWindow();
                        resaveTicket.setVisible(false);
                        isMassPreCharged = false;
                        massPrechargeButton.setBackground(new Color(255, 0, 0));
                        isMassSplitting = false;
                        massSplitTicketButton.setBackground(new Color(255, 0, 0));
                        massSplitTicketButton.setText("Mass Off");

                        resetVars();
                    }
                    else
                    {//Begin split ticket save logic

                        if (curCart.containsItemToBeSplit())
                        {
                            ArrayList<GuiCartItem> itemsToRemove = new ArrayList<>();
                            for (GuiCartItem item : guiItems)
                            {
                                if (item.item.isSetToSplitSave)
                                {
                                    itemsToRemove.add(item);
                                    resizeCartWindow();
                                }
                                else
                                {
                                    item.splitTicketDeactivated();//we need to bring it back to normal view.
                                }
                            }
                            for (GuiCartItem toRemove : itemsToRemove)
                            {
                                guiItems.remove(toRemove);
                                toRemove.removeAllGUIData();
                            }
                            curCart.storeCart(id, true);

                            curCart.setRequiresRepaint(true);
                            curCart.updateTotal();

                            updateCartScreen();
                            resizeCartWindow();
                            resaveTicket.setVisible(false);
                            if (curCart.isEmpty())
                            {
                                isMassPreCharged = false;
                                massPrechargeButton.setBackground(new Color(255, 0, 0));
                                isMassSplitting = false;
                                massSplitTicketButton.setBackground(new Color(255, 0, 0));
                                massSplitTicketButton.setText("Mass Off");
                                resetVars();
                                if (curCart.isEmpDiscountActive)
                                {
                                    curCart.isEmpDiscountActive(false);
                                    empList2.setSelectedIndex(0);
                                }
                            }
                            splitTicketEndShowContent();
                            cancelSplitTicketButton.setVisible(false);
                            beginSplitTicketButton.setVisible(true);
                        }
                        else
                        {
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Cannot save a ticket with nothing set to save.");
                        }
                    }
                }

            }
            else if (!isSplitSavingActive)
            {
                //if it does, send error message!
                JFrame message2 = new JFrame("");
                //JOptionPane.showMessageDialog(message2, "There are already items in ticket for customer. Would you like me to load those?");
                if (!id.toUpperCase().contentEquals("WONDERLAND") && !id.toUpperCase().contentEquals("STRANGER") && !id.toUpperCase().contentEquals("HOW ABOUT A MAGIC TRICK?") && !id.toUpperCase().contentEquals("WET BANDITS") && !id.toUpperCase().contentEquals("WINGARDIUM LEVIOSA") && !id.toUpperCase().contentEquals("MICHAEL MYERS"))
                {
                    if (JOptionPane.showConfirmDialog(null, "There are already items in ticket for customer. Would you like me to load those?", "WARNING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        loadTicketWithId(id);
                        loadedTicketID = id;
                        resaveTicketText = "Resave\nTicket As\n" + loadedTicketID;
                        resaveTicket.setText("<html>" + resaveTicketText.replaceAll("\\n", "<br>") + "</html>");
                        resaveTicket.setVisible(true);
                        updateCartScreen();
                    }
                    else
                    {
                        // no option
                    }
                }

            }
            else
            {//end else already items in tickets
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "There is already a ticket under that ID. Please cancel split ticket and open that ticket to continue.");
            }
        }//end string null or empty part, didnt do an else, we just do nothing.
    }

    public void resizeCartWindow() {
        int extra = 15 + (curCart.getItems().size() * 15);
        jPanel1.setPreferredSize(new Dimension(1120, extra));
    }

    public void resizeRefundCartWindow() {
        int extra = 15 + (refundCart.getRefundItems().size() * 15);
        jPanel1.setPreferredSize(new Dimension(1120, extra));
    }

    private boolean checkLunch() {
        boolean valid = true;
        for (Item item : curCart.getItems())
        {
            if (item.getName().toUpperCase().contains("LUNCH") && empList2.getSelectedItem().toString().contains("NO"))
            {
                valid = false;
            }
        }

        return valid;
    }

    private boolean checkLunchSplitTender() {
        boolean valid = true;
        for (Item item : curCart.getItems())
        {
            if (item.getName().toUpperCase().contains("LUNCH"))
            {
                valid = false;
            }
        }

        return valid;
    }

    public void rxSignout() {
        int reply = JOptionPane.showConfirmDialog(null, "Does patient have questions about medications?", "Medication Questions", JOptionPane.YES_NO_OPTION);
        boolean questions = false;
        if (reply == JOptionPane.YES_OPTION)
        {
            questions = true;
        }

        //We need sig, and to save RX File.TEST
        //CapSignature frame = new CapSignature(this, curCart, checkout.reader.getRemoteDrivePath(), receiptNum);
        CapSignature frame = new CapSignature(this, curCart, ConfigFileReader.getRemoteDrivePath(), receiptNum);

        frame.begin(questions);
        frame.setVisible(true);

        while (!frame.hasBeenSaved)
        {
            //frame = new CapSignature(this, curCart, checkout.reader.getRemoteDrivePath(), receiptNum);
            frame = new CapSignature(this, curCart, ConfigFileReader.getRemoteDrivePath(), receiptNum);
            frame.begin(questions);
            frame.setVisible(true);
        }
    }

    /*
    private void setGUITextColor(Color color) {
        itemNameHeader.setForeground(color);
        employeeCheckoutHeader.setForeground(color);
        totalNumRXinCart.setForeground(color);
        itemNameHeader.setForeground(color);
        itemQuantityHeader.setForeground(color);
        itemPriceHeader.setForeground(color);
        versionHeader.setForeground(color);
        discountHeader.setForeground(color);
        itemSubTotalHeader.setForeground(color);
        estimatedCheckTotalLabel.setForeground(color);
        estimatedLunchTotalLabel.setForeground(color);
        estimatedCashTotalLabel.setForeground(color);
        estimatedCoinTotalLabel.setForeground(color);
        employeeSelectionHeader.setForeground(color);
        quote.setForeground(color);
        subTotal.setForeground(color);
        totalTax.setForeground(color);
        totalPrice.setForeground(color);
        changeDue.setForeground(color);
        subTotalHeader.setForeground(color);
        itemTaxTotalHeader.setForeground(color);
    }
     */

    public static void main(String args[]) {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
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

    public javax.swing.JPanel jPanel1;
    protected Cart curCart;
    protected CheckoutHandler checkout;
    private int[] integerArray = new int[12];
    private int arrayLoc = 0;
    JLabel totalPrice = new JLabel("Total Price: ", SwingConstants.RIGHT);
    JLabel totalTax = new JLabel("Total Tax: ", SwingConstants.RIGHT);
    JLabel subTotal = new JLabel("Subtotal: ", SwingConstants.RIGHT);
    JLabel changeDue = new JLabel("Change Due: ", SwingConstants.RIGHT);
    boolean displayChangeDue = false;
    JLabel quote = new JLabel("", SwingConstants.LEFT);
    JComboBox empList2;
    JLabel itemPriceHeader = new JLabel("Price Per Item: ", SwingConstants.RIGHT);
    JLabel employeeCheckoutHeader = new JLabel("Employee To Checkout:", SwingConstants.RIGHT);
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
    String beginSplitTicketText = "Split\nTicket";
    JButton beginSplitTicketButton = new JButton("<html>" + beginSplitTicketText.replaceAll("\\n", "<br>") + "</html>");
    boolean isSplitSavingActive = false;
    String cancelSplitTicketText = "Cancel\nSplit\nTicket";
    JButton cancelSplitTicketButton = new JButton("<html>" + cancelSplitTicketText.replaceAll("\\n", "<br>") + "</html>");
    String loadedTicketID = "";
    String resaveTicketText = "Resave\nTicket As\n" + loadedTicketID;
    JButton resaveTicket = new JButton("<html>" + resaveTicketText.replaceAll("\\n", "<br>") + "</html>");
    String loadTicketText = "Open\nTicket";
    JButton loadTicket = new JButton("<html>" + loadTicketText.replaceAll("\\n", "<br>") + "</html>");
    String chargeButtonText = "Charge to\nAccount";
    JButton chargeButton = new JButton("<html>" + chargeButtonText.replaceAll("\\n", "<br>") + "</html>");
    JButton refundButton = new JButton("Refund");
    String switchEmployeeString = "Switch\nEmployee";
    JButton switchEmployeeButton = new JButton("<html>" + switchEmployeeString.replaceAll("\\n", "<br>") + "</html>");
    String logoutEmployeeString = "Logout\nEmployee";
    JButton logoutEmployeeButton = new JButton("<html>" + logoutEmployeeString.replaceAll("\\n", "<br>") + "</html>");
    JButton noSaleButton = new JButton("No Sale");
    JButton rxButton = new JButton("RX");
    String receiptLookup = "Lookup\nReceipt";
    JButton lookupReceiptByRXButton = new JButton("<html>" + receiptLookup.replaceAll("\\n", "<br>") + "</html>");
    String previousInsurance = "AARP";
    String previousDate = "";
    JButton otcButton = new JButton("OTC");
    ImageIcon upsimg = new ImageIcon(getClass().getResource("images/ups.png"));
    int activeClerksPasscode = 0;
    JButton upsButton = new JButton(upsimg);
    JButton paperButton = new JButton("Paper");
    JButton voidButton = new JButton("Void");
    JButton cashButton = new JButton("Cash");
    JButton checkButton = new JButton("Check");
    JButton creditButton = new JButton("Credit Card");
    JButton debitButton = new JButton("Debit Card");
    JButton paidOutButton = new JButton("Paid Out");
    JButton clerkLoginButton = new JButton("Clerk Login");
    JButton clerkLogoutButton = new JButton("Clerk Logout");
    JButton activateDisplayButton = new JButton("Activate Display");
    JButton quoteButton = new JButton("New Quote");
    String cancelRefund = "Cancel\nRefund";
    JButton cancelRefundButton = new JButton("<html>" + cancelRefund.replaceAll("\\n", "<br>") + "</html>");
    JButton massDiscountButton = new JButton("");
    JButton massPrechargeButton = new JButton("");
    boolean isMassPreCharged = false;//always start out with mass precharged off
    JButton massSplitTicketButton = new JButton("Mass Off");
    boolean isMassSplitting = false;
    JButton employeeDiscountFalseButton = new JButton("");
    String ar = "Accounts\nReceivable\nPayment";
    String dme = "DME\nAccount\nPayment";
    JLabel employeeSelectionHeader = new JLabel("Active Clerk: NONE", SwingConstants.LEFT);
    JLabel versionHeader = new JLabel("Version 1.2.10", SwingConstants.LEFT);
    JButton dmePaymentButton = new JButton("<html>" + dme.replaceAll("\\n", "<br>") + "</html>");
    protected String previousReceipt = "EMPTY";
    String st = "Split\nTender";
    JButton splitTenderButton = new JButton("<html>" + st.replaceAll("\\n", "<br>") + "</html>");
    JButton arPaymentButton = new JButton("<html>" + ar.replaceAll("\\n", "<br>") + "</html>");
    String receipt = "Reprint\nReceipt";
    protected double estimatedLunchTotal = 0;
    protected double estimatedCheckTotal = 0;
    protected double estimatedCoinTotal = 0;
    protected long estimatedCashTotal = 0;
    JLabel estimatedLunchTotalLabel = new JLabel(String.format("Lunch: $%.2f", estimatedCheckTotal), SwingConstants.LEFT);
    JLabel estimatedCheckTotalLabel = new JLabel(String.format("Checks: $%.2f", estimatedCheckTotal), SwingConstants.LEFT);
    JLabel estimatedCashTotalLabel = new JLabel(String.format("Cash: $%d", estimatedCashTotal), SwingConstants.LEFT);
    JLabel estimatedCoinTotalLabel = new JLabel(String.format("Coin: $%.2f", estimatedCoinTotal), SwingConstants.LEFT);
    JButton reprintReceiptButton = new JButton("<html>" + receipt.replaceAll("\\n", "<br>") + "</html>");
    private boolean itemIsTaxable = true;//assume every item is taxable at first.
    JTextField textField = new JTextField(10);
    ArrayList<GuiCartItem> guiItems = new ArrayList<GuiCartItem>();
    ArrayList<GuiRefundCartItem> guiRefundItems = new ArrayList<GuiRefundCartItem>();
    MainFrame myself = this;
    PoleDisplay display;
    RefundCart refundCart = new RefundCart();
    boolean displayActive = false;
    String receiptNum = "";
    //Holiday Flags!
    boolean isEaster = false;
    boolean isMarchMadness = false;
    boolean isFourthOfJuly = false;
    boolean isHalloween = false;
    boolean isThanksgiving = false;
    boolean isChristmas = false;
    boolean isValentinesDay = false;
    boolean isReallyValentinesDay = false;//used for Hollie <3
    boolean isHolliesBirthday = false;
    boolean isSaintPatricksDay = false;
    boolean isSummerTime = false;
    boolean isWeddingMonth = false;
    boolean quotesActive = true;

    ReadyPlayerOne rp1 = new ReadyPlayerOne(this);
    HolidayLoader holidayLoader;
    String pharmacyName = "";
    final String superaid = "Smiths Super Aid";
    ImageIcon mmimg = new ImageIcon(getClass().getResource("images/MARCHMADNESS.png"));
    JButton mmButton = new JButton(mmimg);
    TopMenuBar menuBar;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
