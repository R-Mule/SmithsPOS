package database_console;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author A.Smith
 */
public class GuiRefundCartItem extends GuiCartItem {

    JLabel nonRefundableLabel;
    JButton refundActiveButton;
    JLabel alreadyRefundedLabel;
    JButton refundInactiveButton;
    JLabel taxAlreadyRefundedOnItemLabel;
    JButton taxRefundedTrueButton;
    JButton taxRefundedFalseButton;
    JButton partialRefundTrueButton;
    JButton partialRefundFalseButton;
    
    RefundItem item;
    RefundCart refundCart;

    GuiRefundCartItem(RefundItem item, int baseY, JPanel frameGUI, RefundCart curCart, MainFrame mainFrame) {
        super(item, baseY, frameGUI, curCart, mainFrame);
        this.taxableButton.setVisible(false);
        this.notTaxableButton.setVisible(false);
        this.addQuantityButton.setVisible(false);
        this.subQuantityButton.setVisible(false);
        this.discountButton.setVisible(false);
        this.item = item;
        this.refundCart = curCart;
        discountLabel.setVisible(false);
        alreadyRefundedLabel = new JLabel("ALREADY REFUNDED");
        nonRefundableLabel = new JLabel("NON REFUNDABLE");
        refundInactiveButton = new JButton("Click to Refund");
        refundActiveButton = new JButton("Refunding");
        taxRefundedFalseButton = new JButton("NO");
        taxRefundedTrueButton = new JButton("YES");
        taxAlreadyRefundedOnItemLabel = new JLabel("PR");
        
        
        if (item.hasBeenRefunded()) {

            frameGUI.add(alreadyRefundedLabel);
            alreadyRefundedLabel.setLocation(980, -7 + baseY);
            alreadyRefundedLabel.setSize(110, 15);
            alreadyRefundedLabel.setFont(new Font(alreadyRefundedLabel.getName(), Font.BOLD, 10));
            alreadyRefundedLabel.setBackground(new Color(255, 0, 0));
            alreadyRefundedLabel.setOpaque(true);
            alreadyRefundedLabel.setVisible(true);
        } else if (item.getCategory() == 853||item.getCategory()==854) {

            frameGUI.add(nonRefundableLabel);
            nonRefundableLabel.setLocation(980, -7 + baseY);
            nonRefundableLabel.setSize(110, 15);
            nonRefundableLabel.setFont(new Font(nonRefundableLabel.getName(), Font.BOLD, 10));
            nonRefundableLabel.setBackground(new Color(255, 0, 0));
            nonRefundableLabel.setOpaque(true);
            nonRefundableLabel.setVisible(true);
        }else {//its refundable!

            frameGUI.add(refundInactiveButton);
            refundInactiveButton.setLocation(980, -7 + baseY);
            refundInactiveButton.setSize(110, 15);
            refundInactiveButton.setFont(new Font(refundInactiveButton.getName(), Font.BOLD, 10));
            refundInactiveButton.setBackground(new Color(255, 0, 0));
            refundInactiveButton.setVisible(true);
            refundInactiveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    refundInactiveButtonPressed();
                    taxRefundedFalsePressed();
                }
            });

            frameGUI.add(refundActiveButton);
            refundActiveButton.setLocation(980, -7 + baseY);
            refundActiveButton.setSize(110, 15);
            refundActiveButton.setFont(new Font(refundActiveButton.getName(), Font.BOLD, 10));
            refundActiveButton.setBackground(new Color(0, 255, 0));
            refundActiveButton.setVisible(false);
            refundActiveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    refundActiveButtonPressed();
                    taxRefundedTruePressed();
                }
            });

            //its an OTC item, that may or may not have refundable tax
            if (item.isTaxable && !item.hasTaxBeenRefunded()) {//tax not refunded yet, but its taxable

                frameGUI.add(taxRefundedFalseButton);
                taxRefundedFalseButton.setLocation(875, -7 + baseY);
                taxRefundedFalseButton.setSize(60, 15);
                taxRefundedFalseButton.setFont(new Font(taxRefundedFalseButton.getName(), Font.BOLD, 10));
                taxRefundedFalseButton.setBackground(new Color(255, 0, 0));
                // taxRefundedFalseButton.setOpaque(true);
                taxRefundedFalseButton.setVisible(item.isTaxable);
                taxRefundedFalseButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        taxRefundedFalsePressed();
                    }
                });

                frameGUI.add(taxRefundedTrueButton);
                taxRefundedTrueButton.setLocation(875, -7 + baseY);
                taxRefundedTrueButton.setSize(60, 15);
                taxRefundedTrueButton.setFont(new Font(taxRefundedTrueButton.getName(), Font.BOLD, 10));
                taxRefundedTrueButton.setBackground(new Color(0, 255, 0));
                // taxRefundedFalseButton.setOpaque(true);
                taxRefundedTrueButton.setVisible(false);
                taxRefundedTrueButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        if (!refundActiveButton.isVisible()) {//if we are returning the item, WE HAVE, to return the tax.
                            taxRefundedTruePressed();
                        }
                    }
                });

            } else if (!item.isTaxable) {//not taxable, no active tax buttons to show
                //do nothing?
            } else if (item.isTaxable && item.hasTaxBeenRefunded()) {//item is taxable, and it has ALREADY BEEN REFUNDED!
                frameGUI.add(taxAlreadyRefundedOnItemLabel);
                taxAlreadyRefundedOnItemLabel.setLocation(875, -7 + baseY);
                taxAlreadyRefundedOnItemLabel.setSize(50, 15);
                taxAlreadyRefundedOnItemLabel.setFont(new Font(taxAlreadyRefundedOnItemLabel.getName(), Font.BOLD, 10));
                taxAlreadyRefundedOnItemLabel.setBackground(new Color(255, 0, 0));
                taxAlreadyRefundedOnItemLabel.setOpaque(true);
                taxAlreadyRefundedOnItemLabel.setVisible(true);
            }
        }

    }

    public void taxRefundedFalsePressed() {

        taxRefundedFalseButton.setVisible(false);
        taxRefundedTrueButton.setVisible(true);
        item.setRefundTaxOnly(true);
        refundCart.updateTotal();
        mainFrame.updateCartScreen();
    }

    public void taxRefundedTruePressed() {

        taxRefundedTrueButton.setVisible(false);
        taxRefundedFalseButton.setVisible(true);
        item.setRefundTaxOnly(false);
        refundCart.updateTotal();
        mainFrame.updateCartScreen();
    }

    public void refundInactiveButtonPressed() {

        
        if(item.isRX){//BEGIN PARTIAL REFUND!
             JFrame textInputFrame = new JFrame("");
                JTextField field2 = new JTextField();

                field2.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        field2.setSelectionStart(0);
                        field2.setSelectionEnd(12);
                        if (!validateDouble(field2.getText())) {
                            field2.setText(String.format("0"));
                        }//end if
                    }//end focusGained
                });
                Object[] message = { "Amount: $", field2};
                field2.setText("0.00");
                field2.setSelectionStart(0);
                field2.setSelectionEnd(4);
                field2.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "RX Refund Menu", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (!validateDouble(field2.getText())) {
                        JFrame message1 = new JFrame("");
                        JOptionPane.showMessageDialog(message1, "Improper Refund Amount.");
                    } else {
                        double amtReceived = Double.parseDouble(field2.getText());
                        amtReceived = round(amtReceived);
                        if (amtReceived>0&&amtReceived<=item.getTotal()) {
                            item.setPrice(amtReceived);
                            
                            refundInactiveButton.setVisible(false);
                            refundActiveButton.setVisible(true);
                            
                            totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
                            item.setRefundAllActive(true);
                        } else {
                           JFrame message1 = new JFrame("");
                           JOptionPane.showMessageDialog(message1, "Refund must be greater than 0 and less than or equal to RX Total Price.");
                        }//end else not 0 or less
                    }//end else
                }//end if  
        }else{
        refundInactiveButton.setVisible(false);
        refundActiveButton.setVisible(true);
        item.setRefundAllActive(true);
        }
        refundCart.updateTotal();
        mainFrame.updateCartScreen();
    }

    public void refundActiveButtonPressed() {

        // taxTotalLabel.setText(String.format("%.2f", item.getTaxTotal()));
        // priceOfItemsLabel.setText(String.format("%.2f", item.getPriceOfItemsBeforeTax()));
        // pricePerItemLabel.setText(String.format("%.2f", item.getPrice()));
        // totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        if(item.isRX){
           item.setPrice(Double.parseDouble(priceOfItemsLabel.getText())); 
           totalItemPriceLabel.setText(String.format("%.2f", item.getTotal()));
        }
        refundInactiveButton.setVisible(true);
        refundActiveButton.setVisible(false);
        item.setRefundAllActive(false);
        refundCart.updateTotal();
        mainFrame.updateCartScreen();
        
    }

    @Override
    public void removeAllGUIData() {
        taxableButton.setVisible(false);
        taxableButton.setVisible(false);
        notTaxableButton.setVisible(false);
        prechargedTrueButton.setVisible(false);
        prechargedFalseButton.setVisible(false);
        addQuantityButton.setVisible(false);
        subQuantityButton.setVisible(false);
        discountButton.setVisible(false);

        quantityLabel.setVisible(false);
        nameLabel.setVisible(false);
        pricePerItemLabel.setVisible(false);
        priceOfItemsLabel.setVisible(false);
        discountLabel.setVisible(false);
        taxTotalLabel.setVisible(false);
        percentOffItemLabel.setVisible(false);
        totalItemPriceLabel.setVisible(false);
        nonRefundableLabel.setVisible(false);
        refundActiveButton.setVisible(false);
        alreadyRefundedLabel.setVisible(false);
        refundInactiveButton.setVisible(false);
        taxAlreadyRefundedOnItemLabel.setVisible(false);
        taxRefundedTrueButton.setVisible(false);
        taxRefundedFalseButton.setVisible(false);

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
    
       private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round
}
