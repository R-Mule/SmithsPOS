package database_console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author A.Smith
 */
public class CheckoutHandler {

    private Database myDB;
    private String printerName; //= "EPSON TM-T20II Receipt";
    private String registerID;// = "D";
    private String remoteDrivePath;
    private PoleDisplay display;

    ConfigFileReader reader;

    public CheckoutHandler(Database myDB) {
        reader = new ConfigFileReader();
        printerName = reader.getPrinterName();
        registerID = reader.getRegisterID();
        remoteDrivePath = reader.getRemoteDrivePath();
        this.myDB = myDB;
        //LOAD REGISTER ID and PRINTER NAME from file.

    }

    public void beginSplitTenderCheckout(Cart curCart, double cashAmt, double credit1Amt, double credit2Amt, double check1Amt, double check2Amt, int check1Num, int check2Num, String clerkName, ArrayList<GuiCartItem> guiItems, MainFrame mainFrame) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;
        int amtCntr = 0;
        if (cashAmt > 0) {
            amtCntr++;
        }
        if (credit1Amt > 0) {
            amtCntr++;
        }
        if (credit2Amt > 0) {
            amtCntr++;
        }
        if (check1Amt > 0) {
            amtCntr++;
        }
        if (check2Amt > 0) {
            amtCntr++;
        }

        double[] paymentAmt = new double[amtCntr];
        String[] paymentType = new String[amtCntr];
        int cntr = 0;
        if (cashAmt > 0) {
            paymentAmt[cntr] = cashAmt;
            paymentType[cntr] = "Cash: ";
            cntr++;
        }
        if (credit1Amt > 0) {
            paymentAmt[cntr] = credit1Amt;
            paymentType[cntr] = "Credit 1: ";
            cntr++;
        }
        if (credit2Amt > 0) {
            paymentAmt[cntr] = credit2Amt;
            paymentType[cntr] = "Credit 2: ";
            cntr++;
        }
        if (check1Amt > 0) {
            paymentAmt[cntr] = check1Amt;
            paymentType[cntr] = "Check " + check1Num + ": ";
            cntr++;
        }
        if (check2Amt > 0) {
            paymentAmt[cntr] = check2Amt;
            paymentType[cntr] = "Check " + check2Num + ": ";
            cntr++;
        }

        if (curCart.getTotalNumRX() > 0) {
            rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
        }

        //RX's are saved!
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame);
        myDB.storeReceipt(curCart, receiptNum);

        mainFrame.voidCarts();
        //save Receipt to Database

    }

    public void beginCashCheckout(Cart curCart, double amtPaid, String clerkName, ArrayList<GuiCartItem> guiItems, MainFrame mainFrame) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];

        paymentAmt[0] = amtPaid;
        paymentType[0] = "Cash: ";
        //STORE CART BEFORE SIGNOUT CHECK

        if (curCart.getTotalNumRX() > 0) {
            rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
        }

        //RX's are saved!
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame);
        myDB.storeReceipt(curCart, receiptNum);

        mainFrame.voidCarts();
        //save Receipt to Database
        //IMPLEMENT

    }

    public void rxSignout(Cart curCart, MainFrame mainFrame, String receiptNum, String clerkName, double[] paymentAmt, String[] paymentType, ArrayList<GuiCartItem> guiItems) {
        int reply = JOptionPane.showConfirmDialog(null, "Does patient have questions about medications?", "Medication Questions", JOptionPane.YES_NO_OPTION);
        boolean questions = false;
        if (reply == JOptionPane.YES_OPTION) {
            questions = true;
        }

        //We need sig, and to save RX File.
        CapSignature frame = new CapSignature(mainFrame, curCart, remoteDrivePath, receiptNum);

        frame.begin(questions);
        frame.setVisible(true);

        while (!frame.hasBeenSaved) {
            frame = new CapSignature(mainFrame, curCart, remoteDrivePath, receiptNum);
            frame.begin(questions);
            frame.setVisible(true);
        }
    }

    public void beginCheckCheckout(Cart curCart, double amtPaid, String clerkName, int checkNum, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];

        paymentAmt[0] = amtPaid;
        paymentType[0] = "Check#" + checkNum + ": ";
        if (curCart.getTotalNumRX() > 0) {
            rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
        }

        myDB.storeReceipt(curCart, receiptNum);
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame);
        mainFrame.voidCarts();
    }

    public void beginCreditCheckout(Cart curCart, double amtPaid, String clerkName, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems) {
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;

        paymentAmt[0] = amtPaid;
        paymentType[0] = String.format("Credit: ");// + cardType + "%04d", cardNumber);

        if (curCart.getTotalNumRX() > 0) {
            rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
        }
        myDB.storeReceipt(curCart, receiptNum);
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame);
        mainFrame.voidCarts();
    }

    public void beginChargeCheckout(Cart curCart, String accountName, String clerkName, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems) {
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");

        String receiptNum = dateFormat.format(date) + registerID;
        paymentAmt[0] = curCart.getTotalPrice();
        int spaceLoc = accountName.indexOf(" ");
        paymentType[0] = "Charged To " + accountName.substring(0, spaceLoc) + ": ";

        if (curCart.getTotalNumRX() > 0) {
            rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
        }
        myDB.storeReceipt(curCart, receiptNum);
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame);
        mainFrame.voidCarts();

    }

    public void printReceipt(Cart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String receiptNum, MainFrame myself) {

        PrinterService printerService = new PrinterService();
        boolean itemDiscounted = false;
        boolean isCreditSale = false;
        boolean requires2Receipts = false;
        int rxCntr = 0;
        //System.out.println(printerService.getPrinters());
        String receipt = "";

        DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy KK:mmaa");
        Date date = new Date();
        String displayDate;
        displayDate = dateFormat2.format(date);
        receipt += "           Smith's Super-Aid Pharmacy\n              247 Old Virginia Ave\n              Rich Creek, VA 24147\n            Main Line:(540) 726-2993\n           Office/DME: (540) 726-7331\nStore Hours: M-F 9:00AM-7:00PM Office closes @5\n             Sat: 9:00AM-2:00PM Office Closed\n             Sun: Closed\n\n";
        receipt += String.format("%-20s %24s\n", "Receipt Number", clerkName);
        receipt += String.format("%-20s %25s\n\n", receiptNum, displayDate);

        String s = String.format("%-10s %15s %9s\n", "Qty@Price", "Item Name", "               Price");
        String s1 = String.format("%-10s %-27s %9s\n", "----------", "---------------------------", "--------");
        receipt += s + s1;
        //print some stuff
        ArrayList<Item> items = curCart.getItems();
        for (Item item : items) {
            if (item.getCategory() == 853 || item.getCategory() == 854) {
                requires2Receipts = true;
            }
            String itemName = "";
            String quantity = Integer.toString(item.getQuantity()) + "@" + String.format("%.2f", round(item.getPrice()));
            Double price = round(item.getPrice() * item.getQuantity());
            Boolean isPreCharged = item.isPreCharged();
            if (item.isRX()) {//IS RX SO WE MUST NOT USE QUANTITY
                rxCntr++;
                if (item.getName().length() > 35) {
                    itemName = item.getName().substring(0, 35);
                } else {
                    itemName = item.getName();
                }
                if (!isPreCharged) {
                    receipt += String.format("RX %-36s $%7.2f\n", itemName, price);
                } else {
                    receipt += String.format("RX %-36s $%7s\n", itemName, "PRECHG");
                }
            } else if (item.getCategory() == 853) {//item is an RA NO QUANTITY
                if (item.getName().length() > 35) {
                    itemName = item.getName().substring(0, 35);
                } else {
                    itemName = item.getName();
                }
                receipt += String.format("AP %-36s $%7.2f\n", itemName, price);
            } else if (item.getCategory() == 854) {//item is an DME Account Payment NO QUANTITY
                if (item.getName().length() > 35) {
                    itemName = item.getName().substring(0, 35);
                } else {
                    itemName = item.getName();
                }
                receipt += String.format("DP %-36s $%7.2f\n", itemName, price);
            } else {//NORMAL ITEM
                if (item.getName().length() > 27) {
                    itemName = item.getName().substring(0, 27);
                } else {
                    itemName = item.getName();
                }
                if (!isPreCharged) {
                    receipt += String.format("%10s %-28s $%7.2f\n", quantity, itemName, price);
                } else {
                    receipt += String.format("%10s %-28s $%7s\n", quantity, itemName, "PRECHG");
                }
            }
            if (item.getDiscountPercentage() != 0) {//Then there is an active discount
                itemDiscounted = true;
                String percentAmt = Double.toString(item.getDiscountPercentage() * 100);
                percentAmt = percentAmt.substring(0, percentAmt.indexOf('.'));
                String discount = "               Discount: " + percentAmt + "%";
                double discount2 = round(item.getDiscountAmount());
                receipt += String.format("%-39s-$%7.2f\n", discount, discount2);
            }
        }//end for all items

        Double subTotal = curCart.getSubTotal();
        Double taxTotal = curCart.getTax();
        Double total = curCart.getTotalPrice();
        Double totalDisc = curCart.getDiscountTotal();
        receipt += "\n";//this puts a line between last time and the first total column
        if (itemDiscounted) {
            receipt += String.format("%35s-$%8.2f\n", "TOTAL DISCOUNT AMOUNT: ", totalDisc);
        }

        receipt += String.format("%36s$%8.2f\n%36s$%8.2f\n%36s$%8.2f\n", "SUBTOTAL: ", subTotal, "TAX: ", taxTotal, "TOTAL: ", total);
        double changeDue = 0;
        double totalPaid = 0;
        for (int i = 0; i < paymentType.length; i++) {
            if (paymentType[i].contains("Credit")) {
                requires2Receipts = true;
            }
            receipt += String.format("%36s$%8.2f\n", paymentType[i], paymentAmt[i]);
            totalPaid += paymentAmt[i];
        }
        changeDue = totalPaid - total;
        receipt += String.format("%37s%8.2f\n\n", "Change Due: $", changeDue);
        if (rxCntr > 0) {
            receipt += "Total RX(s): " + rxCntr + "\n\n";
        }
        receipt += "              STORE RETURN POLICY\nAny RX that leaves the building cannot be\nreturned. Any consumable item must be unopened. All other items are subject to inspection upon\nreturn and must be in good, unused condition.\nYou must have a copy of your receipt. Item must be returned within 30 days of purchase. All\nclearance item sales are final.\n\n";
        receipt += "            Thanks for shopping at\n          Smith's Super-Aid Pharmacy\n       \"The professional pharmacy with\n           that hometown feeling!\"\n\n\n\n\n\n\n";
        printerService.printString(printerName, receipt);

        // cut that paper!
        byte[] cutP = new byte[]{0x1d, 'V', 1};
        byte[] kickDrawer = new byte[]{27, 112, 48, 55, 121};
        boolean isCashSale = false;
        for (int i = 0; i < paymentType.length; i++) {
            if (paymentType[i].contentEquals("Cash: ")) {
                isCashSale = true;
            }
            if (paymentType[i].contains("Credit")) {
                isCreditSale = true;
            }
        }
        if (changeDue > 0 || (isCashSale && curCart.getTotalPrice() != 0) || isCreditSale) {
            printerService.printBytes(printerName, kickDrawer);
        }

        printerService.printBytes(printerName, cutP);
        if (requires2Receipts) {
            printerService.printString(printerName, receipt);
            printerService.printBytes(printerName, cutP);
        }

        myself.previousReceipt = receipt;

        storeReceiptData(curCart, clerkName, paymentType, paymentAmt, receiptNum, false);

    }

    public void beginNoSaleCheckout(String employeeName) {
        PrinterService printerService = new PrinterService();
        byte[] kickDrawer = new byte[]{27, 112, 48, 55, 121};
        printerService.printBytes(printerName, kickDrawer);
    }

    private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

    void beginRefundCashCheckout(RefundCart refundCart, String clerkName, ArrayList<GuiRefundCartItem> guiRefundItems, MainFrame myself) {
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        paymentAmt[0] = refundCart.getTotalPrice();
        paymentType[0] = "Cash Refund: ";

        printRefundReceipt(refundCart, clerkName, paymentType, paymentAmt, refundCart.receiptNum, myself);
        ArrayList<RefundItem> items2Add = new ArrayList<>();
        ArrayList<RefundItem> items2Del = new ArrayList<>();
        for (RefundItem item : refundCart.getRefundItems()) {
            if (item.refundAllActive()) {
                if (refundCart.containsItemByID(item.getID().substring(0, item.getID().length() - 1) + "F")) {
                    refundCart.increaseQtyByID(item.getID().substring(0, item.getID().length() - 1) + "F", item.quantityBeingRefunded);
                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                } else {
                    //NEW ITEM TO ADD! F IT UP PAPA BEAR!
                    RefundItem itemTemp = new RefundItem(myDB,item);
                    itemTemp.setID(item.getID().substring(0, item.getID().length() - 1) + "F");//FINISHED!
                    itemTemp.quantity = item.quantityBeingRefunded;
                    item.quantity -= item.quantityBeingRefunded;
                    itemTemp.hasBeenRefunded = true;
                    itemTemp.hasTaxBeenRefunded = true;
                    itemTemp.refundAllActive = false;
                    itemTemp.refundTaxOnly = false;
                    itemTemp.quantityBeingRefunded=0;
                    items2Add.add(itemTemp);
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                }
            } else if (item.refundTaxOnly() && !item.refundAllActive()) {
                if (refundCart.containsItemByID(item.getID().substring(0, item.getID().length() - 1) + "T")) {
                    refundCart.increaseQtyByID(item.getID().substring(0, item.getID().length() - 1) + "T", item.quantityBeingRefunded);
                    
                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                } else {
                    //NEW ITEM TO ADD! F IT UP PAPA BEAR!
                    RefundItem itemTemp = new RefundItem(myDB,item);
                    itemTemp.mutID =item.getID().substring(0, item.getID().length()- 1) + "T";//TAX REFUNDED!
                    itemTemp.quantity = item.quantityBeingRefunded;
                    item.quantity -= item.quantityBeingRefunded;
                    itemTemp.hasTaxBeenRefunded = true;
                    itemTemp.refundTaxOnly = false;
                    itemTemp.quantityBeingRefunded=0;
                    System.out.println(item.quantity + " NOW" + itemTemp.quantity+"AND "+item.quantityBeingRefunded);
                    items2Add.add(itemTemp);
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                }
            }
        }
        if (!items2Del.isEmpty()) {
            System.out.println("Beginning Removal...");
            myDB.removeReceiptByList(items2Del, refundCart.receiptNum);
            for (RefundItem item : items2Del) {
                System.out.println("Removing: " + item.getName());
                refundCart.removeItem(item);
            }
        }
        if (!items2Add.isEmpty()) {
            System.out.println("Beginning Store New Items...");
            myDB.storeReceiptByList(items2Add, refundCart.receiptNum);
        }
        System.out.println("Updating Existing Items...");
        myDB.updateReceipt(refundCart, refundCart.receiptNum);
        myself.voidCarts();
        myself.refundOver();

    }

    void beginRefundCardCheckout(RefundCart refundCart, String clerkName, ArrayList<GuiRefundCartItem> guiRefundItems, MainFrame myself) {
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        paymentAmt[0] = refundCart.getTotalPrice();
        paymentType[0] = "Card Refund: ";

        myDB.updateReceipt(refundCart, refundCart.receiptNum);
        
        printRefundReceipt(refundCart, clerkName, paymentType, paymentAmt, refundCart.receiptNum, myself);
           ArrayList<RefundItem> items2Add = new ArrayList<>();
        ArrayList<RefundItem> items2Del = new ArrayList<>();
        for (RefundItem item : refundCart.getRefundItems()) {
            if (item.refundAllActive()) {
                if (refundCart.containsItemByID(item.getID().substring(0, item.getID().length() - 1) + "F")) {
                    refundCart.increaseQtyByID(item.getID().substring(0, item.getID().length() - 1) + "F", item.quantityBeingRefunded);
                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                } else {
                    //NEW ITEM TO ADD! F IT UP PAPA BEAR!
                    RefundItem itemTemp = new RefundItem(myDB,item);
                    itemTemp.setID(item.getID().substring(0, item.getID().length() - 1) + "F");//FINISHED!
                    itemTemp.quantity = item.quantityBeingRefunded;
                    item.quantity -= item.quantityBeingRefunded;
                    itemTemp.hasBeenRefunded = true;
                    itemTemp.hasTaxBeenRefunded = true;
                    itemTemp.refundAllActive = false;
                    itemTemp.refundTaxOnly = false;
                    itemTemp.quantityBeingRefunded=0;
                    items2Add.add(itemTemp);
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                }
            } else if (item.refundTaxOnly() && !item.refundAllActive()) {
                if (refundCart.containsItemByID(item.getID().substring(0, item.getID().length() - 1) + "T")) {
                    refundCart.increaseQtyByID(item.getID().substring(0, item.getID().length() - 1) + "T", item.quantityBeingRefunded);
                    
                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                } else {
                    //NEW ITEM TO ADD! F IT UP PAPA BEAR!
                    RefundItem itemTemp = new RefundItem(myDB,item);
                    itemTemp.mutID =item.getID().substring(0, item.getID().length()- 1) + "T";//TAX REFUNDED!
                    itemTemp.quantity = item.quantityBeingRefunded;
                    item.quantity -= item.quantityBeingRefunded;
                    itemTemp.hasTaxBeenRefunded = true;
                    itemTemp.refundTaxOnly = false;
                    itemTemp.quantityBeingRefunded=0;
                    System.out.println(item.quantity + " NOW" + itemTemp.quantity+"AND "+item.quantityBeingRefunded);
                    items2Add.add(itemTemp);
                    if (item.quantity == 0) {
                        items2Del.add(item);
                    }
                }
            }
        }
        if (!items2Del.isEmpty()) {
            System.out.println("Beginning Removal...");
            myDB.removeReceiptByList(items2Del, refundCart.receiptNum);
            for (RefundItem item : items2Del) {
                System.out.println("Removing: " + item.getName());
                refundCart.removeItem(item);
            }
        }
        if (!items2Add.isEmpty()) {
            System.out.println("Beginning Store New Items...");
            myDB.storeReceiptByList(items2Add, refundCart.receiptNum);
        }
        System.out.println("Updating Existing Items...");
        myDB.updateReceipt(refundCart, refundCart.receiptNum);
        myself.voidCarts();
        myself.refundOver();

    }

    public void printRefundReceipt(RefundCart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String receiptNum, MainFrame myself) {
        PrinterService printerService = new PrinterService();
        boolean itemDiscounted = false;
        String receipt = "";

        DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy KK:mmaa");
        Date date = new Date();
        String displayDate;
        displayDate = dateFormat2.format(date);
        receipt += "           Smith's Super-Aid Pharmacy\n              247 Old Virginia Ave\n              Rich Creek, VA 24147\n            Main Line:(540) 726-2993\n           Office/DME: (540) 726-7331\nStore Hours: M-F 9:00AM-7:00PM Office closes @5\n             Sat: 9:00AM-2:00PM Office Closed\n             Sun: Closed\n\n";
        receipt += String.format("%-20s %24s\n", "Receipt Number", clerkName);
        receipt += String.format("%-20s %25s\n\n", receiptNum, displayDate);

        String s = String.format("%-10s %15s %9s\n", "Qty@Price", "Item Name", "               Price");
        String s1 = String.format("%-10s %-27s %9s\n", "----------", "---------------------------", "--------");
        receipt += s + s1;
        //print some stuff
        ArrayList<RefundItem> items = curCart.getRefundItems();
        for (RefundItem item : items) {
            if (item.refundAllActive || item.refundTaxOnly) {
                String itemName = "";
                String quantity = Integer.toString(item.quantityBeingRefunded) + "@" + String.format("%.2f", round(item.getPrice()));
                Double price = round(item.getPrice() * item.quantityBeingRefunded);
                if (item.refundAllActive) {
                    if (item.getName().length() > 27) {
                        itemName = item.getName().substring(0, 27);
                    } else {
                        itemName = item.getName();
                    }
                } else if (item.refundTaxOnly) {
                    price = item.getTaxTotal();
                    if (item.getName().length() > 24) {
                        itemName = item.getName().substring(0, 24) + " TR";

                    } else {
                        itemName = item.getName() + " TR";
                    }
                }
                receipt += String.format("%10s %-28s $%7.2f\n", quantity, itemName, price);

            }
            if (item.getDiscountPercentage() != 0) {//Then there is an active discount
                itemDiscounted = true;
                String percentAmt = Double.toString(item.getDiscountPercentage() * 100);
                percentAmt = percentAmt.substring(0, percentAmt.indexOf('.'));
                String discount = "               Discount: " + percentAmt + "%";
                double discount2 = round(item.getDiscountAmount());
                receipt += String.format("%-39s-$%7.2f\n", discount, discount2);
            }
        }//end for all items

        Double subTotal = curCart.getSubTotal();
        Double taxTotal = curCart.getTax();
        Double total = curCart.getTotalPrice();
        Double totalDisc = curCart.getDiscountTotal();
        receipt += "\n";//this puts a line between last time and the first total column
        if (itemDiscounted) {
            receipt += String.format("%35s-$%8.2f\n", "TOTAL DISCOUNT AMOUNT: ", totalDisc);
        }

        receipt += String.format("%36s$%8.2f\n%36s$%8.2f\n%36s$%8.2f\n", "SUBTOTAL REFUNDED: ", subTotal, "TAX REFUNDED: ", taxTotal, "TOTAL REFUNDED: ", total);
        for (int i = 0; i < paymentType.length; i++) {
            receipt += String.format("%36s$%8.2f\n", paymentType[i], paymentAmt[i]);
        }

        receipt += "\n              STORE RETURN POLICY\nAny RX that leaves the building cannot be\nreturned. Any consumable item must be unopened. All other items are subject to inspection upon\nreturn and must be in good, unused condition.\nYou must have a copy of your receipt. Item must be returned within 30 days of purchase. All\nclearance item sales are final.\n\n";
        receipt += "            Thanks for shopping at\n          Smith's Super-Aid Pharmacy\n       \"The professional pharmacy with\n           that hometown feeling!\"\n\n\n\n\n\n\n";
        printerService.printString(printerName, receipt);

        // cut that paper!
        byte[] cutP = new byte[]{0x1d, 'V', 1};
        byte[] kickDrawer = new byte[]{27, 112, 48, 55, 121};

        printerService.printBytes(printerName, kickDrawer);
        printerService.printBytes(printerName, cutP);
        myself.previousReceipt = receipt;

        storeReceiptData(curCart, clerkName, paymentType, paymentAmt, receiptNum, true);
    }

    public void reprintReceipt(String receipt) {
        PrinterService printerService = new PrinterService();
        printerService.printString(printerName, receipt);

        // cut that paper!
        byte[] cutP = new byte[]{0x1d, 'V', 1};
        printerService.printBytes(printerName, cutP);
    }

    public void beginPaidOut(String description, double amount) {
        DrawerReport dr = null;
        PrinterService printerService = new PrinterService();
        byte[] kickDrawer = new byte[]{27, 112, 48, 55, 121};
        printerService.printBytes(printerName, kickDrawer);
        try {

            File f = new File(reader.getRegisterReportPath() + reader.getRegisterID() + ".posrf");
            if (f.exists() && !f.isDirectory()) {
                // read object from file
                FileInputStream fis = new FileInputStream(reader.getRegisterReportPath() + reader.getRegisterID() + ".posrf");
                ObjectInputStream ois = new ObjectInputStream(fis);
                dr = (DrawerReport) ois.readObject();
                ois.close();

                dr.paidOut(description, amount);

            } else {
                dr = new DrawerReport(description, amount);

            }

            // write object to file
            FileOutputStream fos = new FileOutputStream(reader.getRegisterReportPath() + reader.getRegisterID() + ".posrf");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            dr.printReport();
            oos.writeObject(dr);
            oos.close();

            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void beginMasterRefund(double amount) {
        DrawerReport dr = null;

        try {

            File f = new File(reader.getRegisterReportPath() + reader.getRegisterID() + ".posrf");
            if (f.exists() && !f.isDirectory()) {
                // read object from file
                FileInputStream fis = new FileInputStream(reader.getRegisterReportPath() + reader.getRegisterID() + ".posrf");
                ObjectInputStream ois = new ObjectInputStream(fis);
                dr = (DrawerReport) ois.readObject();
                ois.close();
                dr.masterRefund(amount);
            } else {
                dr = new DrawerReport(amount);
            }

            // write object to file
            FileOutputStream fos = new FileOutputStream(reader.getRegisterReportPath() + reader.getRegisterID() + ".posrf");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            dr.printReport();
            oos.writeObject(dr);
            oos.close();
            PrinterService printerService = new PrinterService();
            byte[] kickDrawer = new byte[]{27, 112, 48, 55, 121};
            printerService.printBytes(printerName, kickDrawer);
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

    public void storeReceiptData(Cart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String receiptNum, boolean isRefund) {
        DrawerReport dr = null;

        try {

            File f = new File(reader.getRegisterReportPath() + receiptNum.substring(12) + ".posrf");
            if (f.exists() && !f.isDirectory()) {
                // read object from file
                FileInputStream fis = new FileInputStream(reader.getRegisterReportPath() + receiptNum.substring(12) + ".posrf");
                ObjectInputStream ois = new ObjectInputStream(fis);
                dr = (DrawerReport) ois.readObject();
                ois.close();
                if (!isRefund) {
                    dr.update(curCart, clerkName, paymentType, paymentAmt);
                } else {
                    dr.refundUpdate((RefundCart) curCart, clerkName, paymentType, paymentAmt);
                }
            } else {
                if (!isRefund) {
                    dr = new DrawerReport(curCart, clerkName, paymentType, paymentAmt);
                } else {
                    dr = new DrawerReport((RefundCart) curCart, clerkName, paymentType, paymentAmt, isRefund);
                }
            }

            // write object to file
            FileOutputStream fos = new FileOutputStream(reader.getRegisterReportPath() + receiptNum.substring(12) + ".posrf");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            dr.printReport();
            oos.writeObject(dr);
            oos.close();

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

        // System.out.println("CREATED: "+dr.getTotalCashAmt());
    }//end store data

}
