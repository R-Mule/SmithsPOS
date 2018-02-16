package database_console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 *
 * @author A.Smith
 */
public class GuiCartItem {

    MainFrame mainFrame;
    JButton taxableButton;
    JButton notTaxableButton;
    JButton prechargedTrueButton;
    JButton prechargedFalseButton;
    JButton addQuantityButton;
    JButton editRXButton;
    JButton subQuantityButton;
    JButton discountButton;

    JLabel quantityLabel;//PORTED, sorta.
    JLabel nameLabel;
    JLabel pricePerItemLabel;
    JLabel priceOfItemsLabel;
    JLabel discountLabel;
    JLabel taxTotalLabel;
    JLabel percentOffItemLabel;
    JLabel totalItemPriceLabel;

    JPanel frame;
    int baseY;

    boolean hasChanged = false;//This is used to determine if we need to update the GUI items on this item.

    Item item;//corresponding item in cart that I belong to.
    Cart curCart;//the cart in which I am placed.

    public GuiCartItem(Item item, int baseY, JPanel frameGUI, Cart curCart, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.frame = frameGUI;
        this.curCart = curCart;
        this.item = item;
        this.baseY = baseY;
        quantityLabel = new JLabel(item.getID(), SwingConstants.CENTER);
        quantityLabel.setSize(50, 50);
        quantityLabel.setText(item.getQuantity() + "x");
        quantityLabel.setLocation(5, -25 + baseY);
        quantityLabel.setVisible(true);
        frame.add(quantityLabel);

        taxTotalLabel = new JLabel(item.getID(), SwingConstants.RIGHT);
        taxTotalLabel.setSize(100, 50);
        taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        taxTotalLabel.setLocation(775, -25 + baseY);
        frame.setVisible(true);
        frame.add(taxTotalLabel);

        discountLabel = new JLabel(item.getID(), SwingConstants.RIGHT);
        discountLabel.setSize(100, 50);
        discountLabel.setText(String.format("%.2f", item.getDiscountAmount()));
        discountLabel.setLocation(660, -25 + baseY);
        discountLabel.setVisible(true);
        frame.add(discountLabel);
        
        priceOfItemsLabel = new JLabel(item.getID(), SwingConstants.RIGHT);
        priceOfItemsLabel.setSize(100, 50);
        priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
        priceOfItemsLabel.setLocation(560, -25 + baseY);
        priceOfItemsLabel.setVisible(true);
        frame.add(priceOfItemsLabel);

        pricePerItemLabel = new JLabel(item.getID(), SwingConstants.RIGHT);
        pricePerItemLabel.setSize(100, 50);
        pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
        pricePerItemLabel.setLocation(420, -25 + baseY);
        pricePerItemLabel.setVisible(true);
        frame.add(pricePerItemLabel);

        percentOffItemLabel = new JLabel(item.getID(), SwingConstants.LEFT);
        percentOffItemLabel.setSize(100, 50);
        int i = (int) item.getDiscountPercentage() * 100;
        percentOffItemLabel.setText(String.format("%4s", Integer.toString(i) + "%"));
        percentOffItemLabel.setLocation(782, -25 + baseY);
        percentOffItemLabel.setVisible(true);
        frame.add(percentOffItemLabel);

        nameLabel = new JLabel(item.getID(), SwingConstants.LEFT);
        nameLabel.setSize(400, 50);
        nameLabel.setText(item.getName());
        nameLabel.setLocation(100, -25 + baseY);
        nameLabel.setVisible(true);
        frame.add(nameLabel);

        if (item.isPreCharged()) {
            //TOTAL PRECHARGED
            totalItemPriceLabel = new JLabel(item.getID(), SwingConstants.RIGHT);
            totalItemPriceLabel.setText(String.format("%4s", "PCHG"));
        } else {
            //TOTAL NORMAL (Not Precharged RX)
            totalItemPriceLabel = new JLabel(item.getID(), SwingConstants.RIGHT);
            totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        }//end else
        totalItemPriceLabel.setLocation(875, -25 + baseY);
        totalItemPriceLabel.setSize(100, 50);
        totalItemPriceLabel.setVisible(true);
        frame.add(totalItemPriceLabel);

        //ADD BUTTON
        addQuantityButton = new JButton("ADD");
        editRXButton = new JButton("Edit");
        if (item.isRX() || item.getCategory() == 853 || item.getCategory()==854|| item.getCategory()==860) {
            addQuantityButton.setVisible(false);
            
        }else {
            addQuantityButton.setVisible(true);
            
        }
        
        if(item.isRX()){
            editRXButton.setVisible(true);
        }else{
            editRXButton.setVisible(false);
        }
        addQuantityButton.setSize(55, 15);
        addQuantityButton.setName(item.getUPC());
        addQuantityButton.setBackground(new Color(0, 255, 0));
        addQuantityButton.setFont(new Font(addQuantityButton.getName(), Font.BOLD, 10));
        addQuantityButton.setLocation(1035, -7 + baseY);
        
        editRXButton.setSize(55, 15);
        editRXButton.setName(item.getUPC());
        editRXButton.setBackground(new Color(255, 255, 0));
        editRXButton.setFont(new Font(addQuantityButton.getName(), Font.BOLD, 10));
        editRXButton.setLocation(1035, -7 + baseY);
        
        frame.add(addQuantityButton);
        frame.add(editRXButton);
        
        //SUB BUTTON
        subQuantityButton = new JButton("SUB");
        subQuantityButton.setVisible(true);
        subQuantityButton.setSize(55, 15);
        subQuantityButton.setName(item.getUPC());
        subQuantityButton.setBackground(new Color(255, 0, 0));
        subQuantityButton.setFont(new Font(subQuantityButton.getName(), Font.BOLD, 10));
        subQuantityButton.setLocation(980, -7 + baseY);
        frame.add(subQuantityButton);

        //ISTAXABLE BUTTON       
        taxableButton = new JButton("");
        if (item.isTaxable()) {
            taxableButton.setVisible(true);
        } else {
            taxableButton.setVisible(false);
        }
        taxableButton.setSize(15, 15);
        taxableButton.setName(item.getUPC());
        taxableButton.setBackground(new Color(0, 255, 0));
        taxableButton.setFont(new Font(taxableButton.getName(), Font.BOLD, 10));
        taxableButton.setLocation(875, -7 + baseY);
        frame.add(taxableButton);

        //NOT TAXABLE BUTTON
        notTaxableButton = new JButton("");

        if (item.isRX() || item.getCategory() == 853||item.getCategory()==854|| item.getCategory()==860) {//this HIDES tax buttons for RX because it is ALWAYS false
            notTaxableButton.setVisible(false);
            notTaxableButton.setVisible(false);
        } else {//this determines wether to show or hide them
            if (item.isTaxable()) {
                notTaxableButton.setVisible(false);
            } else {
                notTaxableButton.setVisible(true);
            }
        }
        notTaxableButton.setSize(15, 15);
        notTaxableButton.setName(item.getUPC());
        notTaxableButton.setBackground(new Color(255, 0, 0));
        notTaxableButton.setFont(new Font(notTaxableButton.getName(), Font.BOLD, 10));
        notTaxableButton.setLocation(875, -7 + baseY);
        frame.add(notTaxableButton);

        //PRECHARGED FALSE
        prechargedFalseButton = new JButton("");
        prechargedFalseButton.setSize(15, 15);
        prechargedFalseButton.setName(item.getName());
        prechargedFalseButton.setBackground(new Color(255, 0, 0));
        prechargedFalseButton.setFont(new Font(prechargedFalseButton.getName(), Font.BOLD, 10));
        prechargedFalseButton.setLocation(50, -7 + baseY);
        frame.add(prechargedFalseButton);

        //PRECHARGED TRUE
        prechargedTrueButton = new JButton("");
        prechargedTrueButton.setSize(15, 15);
        prechargedTrueButton.setName(item.getName());
        prechargedTrueButton.setBackground(new Color(0, 255, 0));
        prechargedTrueButton.setFont(new Font(prechargedTrueButton.getName(), Font.BOLD, 10));
        prechargedTrueButton.setLocation(50, -7 + baseY);
        frame.add(prechargedTrueButton);

        //DISCOUNT BUTTON
        discountButton = new JButton("");
        discountButton.setSize(15, 15);
        discountButton.setName(item.getUPC());
        discountButton.setBackground(new Color(255, 255, 100));
        discountButton.setFont(new Font(discountButton.getName(), Font.BOLD, 10));
        discountButton.setLocation(765, -7 + baseY);
        frame.add(discountButton);

        //determine which precharged buttons should show
        if (item.isRX() && item.isPreCharged()) {
            prechargedFalseButton.setVisible(false);
            prechargedTrueButton.setVisible(true);
        } else if (item.isRX() && !item.isPreCharged()) {
            prechargedFalseButton.setVisible(true);
            prechargedTrueButton.setVisible(false);
        } else {
            prechargedFalseButton.setVisible(false);
            prechargedTrueButton.setVisible(false);
        }

        if (item.isRX() || item.getCategory() == 853||item.getCategory()==854|| item.getCategory()==860) {//this HIDES discount buttons for RX &&RA because it is ALWAYS false
            discountButton.setVisible(false);
            discountButton.setVisible(false);
        } else {
            discountButton.setVisible(true);
        }
        //DISCOUNT BUTTON PRESSED
        discountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                individualDiscountButtonPressed(event);
            }
        });
        //ADD ITEM BUTTON PRESSED
        addQuantityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addItemButtonPressed(event);
            }
        });
                //ADD ITEM BUTTON PRESSED
        editRXButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                editRXButtonPressed(event);
            }
        });
        //REMOVE ITEM BUTTON PRESSED
        subQuantityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                removeItemButtonPressed(event);
            }
        });
        //SET TAXABLE FALSE BUTTON
        notTaxableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                notTaxableButtonPressed(event);
            }
        });

        //SET NOTTAXABLE FALSE BUTTON
        taxableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                taxableButtonPressed(event);
            }
        });
        //SET PRECHARGED FALSE BUTTON
        prechargedFalseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setPrechargedFalsePressed(event);
            }
        });
        //SET PRECHARGED TRUE BUTTON
        prechargedTrueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setPrechargedTruePressed(event);
            }
        });

    }//end ctor

    public void employeeSaleTriggered(){
        if(!item.isRX()&&item.getCategory()!=853&&item.getCategory()!=854){
        item.setPrice(item.getCost());
        curCart.updateTotal();
        taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
        quantityLabel.setText(item.getQuantity() + "x");
        totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
        mainFrame.updateCartScreen();
        }
    }

    public void updateQuantityLabelAmount() {
        quantityLabel.setText(item.getQuantity() + "x");
        totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        discountLabel.setText(String.format("%.2f", item.getDiscountAmount()));
        
    }//end updateQuantityLabelAmount

    public void updateYcord(int y) {
        quantityLabel.setLocation(5, -10 + y);
        taxTotalLabel.setLocation(775, -10 + y);
        discountLabel.setLocation(660, -10 + y);
        percentOffItemLabel.setLocation(782, -10 + y);
    }//end updateQuantityLabelPosition

    //DISCOUNT AMT
    public void individualDiscountButtonPressed(ActionEvent event) {
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
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Item Discount Menu", JOptionPane.OK_CANCEL_OPTION);
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
                    //System.out.println(discPer);
                    item.setDiscountPercentage(discPer);
                    curCart.updateTotal();
                    discountLabel.setText(String.format("%.2f", item.getDiscountAmount()));
                    taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
                    priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
                    pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
                    totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
                    int i = (int) (item.getDiscountPercentage() * 100);
                    percentOffItemLabel.setText(String.format("%4s", Integer.toString(i) + "%"));
                    mainFrame.updateCartScreen();
                }//end else
            }//end else
        }//end if

    }

    
    public String getUPC(){
        return item.getUPC();
    }
    public void addItemButtonPressed(ActionEvent event) {//Since I know, I exist, just increase my quantity 1;
        int quantity = item.getQuantity();
        item.setQuantity(quantity + 1);
        curCart.updateTotal();
        taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
        quantityLabel.setText(item.getQuantity() + "x");
        discountLabel.setText(String.format("%.2f", item.getDiscountAmount()));
        totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        mainFrame.updateCartScreen();

    }

        public void editRXButtonPressed(ActionEvent event) {//Since I know, I exist, just increase my quantity 1;
                           JFrame textInputFrame = new JFrame("");
                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();

                    field2.setText(item.fillDate);
                    String[] possibilities = mainFrame.myDB.getInsurances();
                    JList list = new JList(possibilities); //data has type Object[]
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    list.setLayoutOrientation(JList.VERTICAL_WRAP);
                    list.setBounds(100, 50, 50, 100);
                    list.setVisibleRowCount(-1);

                    for (int i = 0; i < possibilities.length; i++) {
                        if (item.insurance.contentEquals(possibilities[i])) {
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
                    
                    field1.setText(Integer.toString(item.rxNumber));
                    field3.setText(Double.toString(item.itemPrice));
                    field2.setSelectionStart(0);
                    field2.setSelectionEnd(6);
                    field3.setSelectionStart(0);
                    field3.setSelectionEnd(4);
                    field1.addAncestorListener(new RequestFocusListener());
                    field1.setSelectionStart(0);
                    field1.setSelectionEnd(7);
                    int option = JOptionPane.showConfirmDialog(textInputFrame, message, "RX Information", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        int rxNumber;
                        String fillDate;
                        fillDate = field2.getText();
                        try {
                            String insurance = (String) list.getSelectedValue();
                            rxNumber = Integer.parseInt(field1.getText());
                            int length = (int) (Math.log10(rxNumber) + 1);
                            if (length!=7) {//invalid RXNumber
                                JFrame message1 = new JFrame("");
                                JOptionPane.showMessageDialog(message1, "Invalid RX Number");
                            } else {

                                if (!mainFrame.validateDate(fillDate)) {
                                    JFrame message1 = new JFrame("");
                                    JOptionPane.showMessageDialog(message1, "Invalid Fill Date");
                                } else {
                                    String temp = field3.getText();
                                    if (!mainFrame.validateDouble(temp)) {//check for copay
                                        JFrame message1 = new JFrame("");
                                        JOptionPane.showMessageDialog(message1, "Invalid Copay");
                                    } else {//else everything checks out! WE HAVE ALL GOOD DATA!!!
                                        if (!curCart.containsMultipleRX(Integer.parseInt(field1.getText()), insurance,fillDate,item)) {
                                            item.rxNumber=rxNumber;
                                            item.fillDate=fillDate;
                                            item.insurance=insurance;
                                            item.itemPrice=Double.parseDouble(field3.getText());
                                            item.itemCost=Double.parseDouble(field3.getText());
                                            item.itemName = rxNumber + " " + insurance + " " + fillDate;
                                            item.mutID = "X"+Integer.toString(rxNumber).substring(2, 7);
                                            item.itemUPC = "X"+Integer.toString(rxNumber).substring(0, 5)+fillDate;
        
                                            nameLabel.setText(item.itemName);
                                            priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
                                            pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
                                            if(item.isPreCharged){
                                                totalItemPriceLabel.setText(String.format("%4s", "PCHG"));
                                            }else{
                                                totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
                                            }
                                            curCart.updateTotal();
                                            mainFrame.updateCartScreen();
                                            
                                        }else{
                                            JFrame message1 = new JFrame("");
                                            JOptionPane.showMessageDialog(message1, "RX Already in Cart");
                                        }
}
                                }//end else valid fillDate
                            }//end else valid RXNumber
                        } catch (NumberFormatException e) {
                            //If number is not number for RX, print error msg.
                            JFrame message1 = new JFrame("");
                            JOptionPane.showMessageDialog(message1, "Invalid RX Number");
                        }//end catch

                    }//end if
 




       // int quantity = item.getQuantity();
       // item.setQuantity(quantity + 1);
       // 
       // taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
       // priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
       // quantityLabel.setText(item.getQuantity() + "x");
       // discountLabel.setText(String.format("%.2f", item.getDiscountAmount()));
        //totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        
        

    }
        
    public void removeItemButtonPressed(ActionEvent event) {

        if (item.getQuantity() > 1) {
            int q = item.getQuantity();
            item.setQuantity(q - 1);
            taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
            priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
            quantityLabel.setText(item.getQuantity() + "x");
            totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
            discountLabel.setText(String.format("%.2f", item.getDiscountAmount()));
        } else {
            removeAllGUIData();
            curCart.removeItem(item);
            curCart.setRequiresRepaint(true);
            mainFrame.removeGuiCartItem(this);
        }
        
        curCart.updateTotal();
        mainFrame.updateCartScreen();

        //displayChangeDue = false;
        //updateCartScreen();
    }

    public void notTaxableButtonPressed(ActionEvent event) {

        item.setTaxable(true);//sets button to not taxable
        curCart.updateTotal();
        taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
        pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
        totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        notTaxableButton.setVisible(false);
        taxableButton.setVisible(true);
        mainFrame.updateCartScreen();
    }

    public void taxableButtonPressed(ActionEvent event) {

        item.setTaxable(false);//sets button to taxable
        curCart.updateTotal();
        taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
        pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
        totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        taxableButton.setVisible(false);
        notTaxableButton.setVisible(true);
        mainFrame.updateCartScreen();
    }

    public void setPrechargedFalsePressed(ActionEvent event) {

        item.setIsPreCharged(true);
        curCart.updateTotal();
        totalItemPriceLabel.setText(String.format("%4s", "PCHG"));
        prechargedFalseButton.setVisible(false);
        prechargedTrueButton.setVisible(true);
        mainFrame.updateCartScreen();

    }

    public void setPrechargedTruePressed(ActionEvent event) {
        item.setIsPreCharged(false);
        curCart.updateTotal();
        totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        prechargedFalseButton.setVisible(true);
        prechargedTrueButton.setVisible(false);
        mainFrame.updateCartScreen();
    }

    public void setMassDiscount() {
        discountLabel.setText(String.format("%.2f", item.getDiscountAmount()));
        taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
        pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
        totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        int i = (int) (item.getDiscountPercentage() * 100);
        percentOffItemLabel.setText(String.format("%4s", Integer.toString(i) + "%"));
        mainFrame.updateCartScreen();
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

    public void removeAllGUIData() {
        taxableButton.setVisible(false);
        taxableButton.setVisible(false);
        notTaxableButton.setVisible(false);
        prechargedTrueButton.setVisible(false);
        prechargedFalseButton.setVisible(false);
        addQuantityButton.setVisible(false);
        subQuantityButton.setVisible(false);
        discountButton.setVisible(false);

        quantityLabel.setVisible(false);//PORTED, sorta.
        nameLabel.setVisible(false);
        pricePerItemLabel.setVisible(false);
        priceOfItemsLabel.setVisible(false);
        discountLabel.setVisible(false);
        taxTotalLabel.setVisible(false);
        percentOffItemLabel.setVisible(false);
        totalItemPriceLabel.setVisible(false);
        
        editRXButton.setVisible(false);
    }

    public void reposition(int baseY) {
        this.baseY = baseY;

        quantityLabel.setLocation(5, -25 + baseY);
        taxTotalLabel.setLocation(775, -25 + baseY);
        discountLabel.setLocation(660, -25 + baseY);
        percentOffItemLabel.setLocation(782, -25 + baseY);
        priceOfItemsLabel.setLocation(560, -25 + baseY);
        pricePerItemLabel.setLocation(420, -25 + baseY);
        nameLabel.setLocation(100, -25 + baseY);
        totalItemPriceLabel.setLocation(875, -25 + baseY);
        addQuantityButton.setLocation(1035, -7 + baseY);
        editRXButton.setLocation(1035, -7 + baseY);
        subQuantityButton.setLocation(980, -7 + baseY);
        taxableButton.setLocation(875, -7 + baseY);
        notTaxableButton.setLocation(875, -7 + baseY);
        prechargedFalseButton.setLocation(50, -7 + baseY);
        prechargedTrueButton.setLocation(50, -7 + baseY);
        discountButton.setLocation(765, -7 + baseY);
        
    }
}//end GuiCartItem
