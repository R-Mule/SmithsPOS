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

/**

 @author A.Smith
 */
public class CheckoutHandler {

    private String printerName; //= "EPSON TM-T20II Receipt";
    private String registerID;// = "D";
    private String remoteDrivePath;

    //ConfigFileReader reader;
    public CheckoutHandler() {
        //reader = new ConfigFileReader();
        printerName = ConfigFileReader.getPrinterName();
        registerID = ConfigFileReader.getRegisterID();
        remoteDrivePath = ConfigFileReader.getRemoteDrivePath();
        //LOAD REGISTER ID and PRINTER NAME from file.

    }

    public String beginSplitTenderCheckout(Cart curCart, double cashAmt, double debitAmount, double credit1Amt, double check1Amt, double check2Amt, String check1Num, String check2Num, String clerkName, ArrayList<GuiCartItem> guiItems, MainFrame mainFrame, String employeeCheckoutName) {
        Date date = new Date();
        ArrayList<String> creditInfo = new ArrayList<>();
        CardDataRequester cdr = new CardDataRequester();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;
        int amtCntr = 0;
        if (cashAmt > 0)
        {
            amtCntr++;
        }
        if (credit1Amt > 0)
        {
            mainFrame.setEnabled(false);

            cdr.postRequest(ConfigFileReader.getCardReaderURL(), Double.toString(credit1Amt), "CCR1");
            mainFrame.setEnabled(true);

            if (cdr.transTerminated())
            {
                return cdr.responseText;
            }
            creditInfo.add("3130031394051");//Merchant ID
            creditInfo.add(cdr.approvalCode);
            creditInfo.add(cdr.transID);
            creditInfo.add(cdr.AID);
            creditInfo.add(cdr.TVR);
            creditInfo.add(cdr.TSI);
            creditInfo.add("CREDIT");
            amtCntr++;

        }
        else if (debitAmount > 0)
        {
            mainFrame.setEnabled(false);

            cdr.postRequest(ConfigFileReader.getCardReaderURL(), Double.toString(debitAmount), "DB00");
            mainFrame.setEnabled(true);

            if (cdr.transTerminated())
            {
                return cdr.responseText;
            }
            creditInfo.add("3130031394051");//Merchant ID
            creditInfo.add(cdr.approvalCode);
            creditInfo.add(cdr.transID);
            creditInfo.add(cdr.AID);
            creditInfo.add(cdr.TVR);
            creditInfo.add(cdr.TSI);
            creditInfo.add("DEBIT");
            amtCntr++;

        }

        if (check1Amt > 0)
        {
            amtCntr++;
        }
        if (check2Amt > 0)
        {
            amtCntr++;
        }

        double[] paymentAmt = new double[amtCntr];
        String[] paymentType = new String[amtCntr];
        int cntr = 0;
        if (cashAmt > 0)
        {
            paymentAmt[cntr] = cashAmt;
            paymentType[cntr] = "CASH: ";
            cntr++;
        }
        if (credit1Amt > 0)
        {
            paymentAmt[cntr] = credit1Amt;
            paymentType[cntr] = String.format(cdr.cardType + " CREDIT:" + cdr.last4ofCard + ": ");
            cntr++;
        }
        if (debitAmount > 0)
        {
            paymentAmt[cntr] = debitAmount;
            paymentType[cntr] = String.format(cdr.cardType + " DEBIT:" + cdr.last4ofCard + ": ");
            cntr++;
        }
        if (check1Amt > 0)
        {
            paymentAmt[cntr] = check1Amt;
            paymentType[cntr] = "CHECK#" + check1Num + ": ";
            cntr++;
        }
        if (check2Amt > 0)
        {
            paymentAmt[cntr] = check2Amt;
            paymentType[cntr] = "CHECK#" + check2Num + ": ";
            cntr++;
        }

        if (curCart.getTotalNumRX() > 0)
        {
            //rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
            mainFrame.receiptNum = receiptNum;
        }

        //RX's are saved!
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame, employeeCheckoutName, creditInfo);
        Database.storeReceipt(curCart, receiptNum);

        mainFrame.voidCarts();
        //save Receipt to Database
        return "SMITHSAPPROVEDCODE";

    }

    public void beginCashCheckout(Cart curCart, double amtPaid, String clerkName, ArrayList<GuiCartItem> guiItems, MainFrame mainFrame, String employeeCheckoutName) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];

        paymentAmt[0] = amtPaid;
        paymentType[0] = "CASH: ";
        //STORE CART BEFORE SIGNOUT CHECK

        if (curCart.getTotalNumRX() > 0)
        {
            //rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
            mainFrame.receiptNum = receiptNum;
        }

        //RX's are saved!
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame, employeeCheckoutName, null);
        Database.storeReceipt(curCart, receiptNum);

        mainFrame.voidCarts();
        //save Receipt to Database
        //IMPLEMENT

    }

    public void beginCheckCheckout(Cart curCart, double amtPaid, String clerkName, String checkNum, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems, String employeeCheckoutName) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];

        paymentAmt[0] = amtPaid;
        paymentType[0] = "CHECK#" + checkNum + ": ";
        if (curCart.getTotalNumRX() > 0)
        {
            //rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
            mainFrame.receiptNum = receiptNum;
        }

        Database.storeReceipt(curCart, receiptNum);
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame, employeeCheckoutName, null);
        mainFrame.voidCarts();
    }

    public void beginStoreCheckCheckout(Cart curCart, double amtPaid, String clerkName, String checkNum, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems, String employeeCheckoutName) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];

        paymentAmt[0] = amtPaid;
        paymentType[0] = "CHECK#" + checkNum + ": ";
        if (curCart.getTotalNumRX() > 0)
        {
            //rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
            mainFrame.receiptNum = receiptNum;
        }

        Database.storeReceipt(curCart, receiptNum);
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame, employeeCheckoutName, null);
        mainFrame.voidCarts();
    }

    public String beginCreditCheckout(Cart curCart, double amtPaid, String clerkName, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems, String employeeCheckoutName) {
        mainFrame.setEnabled(false);
        ArrayList<String> creditInfo = new ArrayList<>();

        CardDataRequester cdr = new CardDataRequester();
        cdr.postRequest(ConfigFileReader.getCardReaderURL(), Double.toString(amtPaid), "CCR1");
        mainFrame.setEnabled(true);

        if (cdr.transTerminated())
        {
            return cdr.responseText;
        }

        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;

        paymentAmt[0] = amtPaid;
        paymentType[0] = String.format(cdr.cardType + " CREDIT:" + cdr.last4ofCard + ": ");// + cardType + "%04d", cardNumber);

        if (curCart.getTotalNumRX() > 0)
        {
            //rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
            mainFrame.receiptNum = receiptNum;
        }
        Database.storeReceipt(curCart, receiptNum);
        creditInfo.add("3130031394051");//Merchant ID
        creditInfo.add(cdr.approvalCode);
        creditInfo.add(cdr.transID);
        creditInfo.add(cdr.AID);
        creditInfo.add(cdr.TVR);
        creditInfo.add(cdr.TSI);
        creditInfo.add("CREDIT");

        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame, employeeCheckoutName, creditInfo);
        mainFrame.voidCarts();
        return "SMITHSAPPROVEDCODE";

    }

    public String beginDebitCheckout(Cart curCart, double amtPaid, String clerkName, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems, String employeeCheckoutName) {
        mainFrame.setEnabled(false);
        ArrayList<String> creditInfo = new ArrayList<>();

        CardDataRequester cdr = new CardDataRequester();
        cdr.postRequest(ConfigFileReader.getCardReaderURL(), Double.toString(amtPaid), "DB00");
        mainFrame.setEnabled(true);

        if (cdr.transTerminated())
        {
            return cdr.responseText;
        }

        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");
        String receiptNum = dateFormat.format(date) + registerID;

        paymentAmt[0] = amtPaid;
        paymentType[0] = String.format(cdr.cardType + " DEBIT:" + cdr.last4ofCard + ": ");// + cardType + "%04d", cardNumber);

        if (curCart.getTotalNumRX() > 0)
        {
            //rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
            mainFrame.receiptNum = receiptNum;
        }
        Database.storeReceipt(curCart, receiptNum);
        creditInfo.add("3130031394051");//Merchant ID
        creditInfo.add(cdr.approvalCode);
        creditInfo.add(cdr.transID);
        creditInfo.add(cdr.AID);
        creditInfo.add(cdr.TVR);
        creditInfo.add(cdr.TSI);
        creditInfo.add("DEBIT");

        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame, employeeCheckoutName, creditInfo);
        mainFrame.voidCarts();
        return "SMITHSAPPROVEDCODE";

    }

    public void beginChargeCheckout(Cart curCart, String accountName, String clerkName, MainFrame mainFrame, ArrayList<GuiCartItem> guiItems, String employeeCheckoutName) {
        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyhhmmss");

        String receiptNum = dateFormat.format(date) + registerID;
        paymentAmt[0] = curCart.getTotalPrice();
        int spaceLoc = accountName.indexOf(" ");
        paymentType[0] = "CHARGED TO " + accountName.substring(0, spaceLoc) + ": ";

        if (curCart.getTotalNumRX() > 0)
        {
            //rxSignout(curCart, mainFrame, receiptNum, clerkName, paymentAmt, paymentType, guiItems);
            mainFrame.receiptNum = receiptNum;
        }
        Database.updateChargeAccountBalance(accountName, curCart.getTotalPrice());
        Database.storeReceipt(curCart, receiptNum);
        printReceipt(curCart, clerkName, paymentType, paymentAmt, receiptNum, mainFrame, employeeCheckoutName, null);
        mainFrame.voidCarts();

    }

    public void printReceipt(Cart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String receiptNum, MainFrame myself, String employeeCheckoutName, ArrayList<String> creditInfo) {

        PrinterService printerService = new PrinterService();
        boolean itemDiscounted = false;
        boolean isCreditSale = false;
        boolean isDebitSale = false;
        boolean requires2Receipts = false;
        boolean isPayCheckReceipt = false;

        double prechargedTotal = 0;

        boolean isCashSale = false;
        for (int i = 0; i < paymentType.length; i++)
        {
            if (paymentType[i].contentEquals("CASH: "))
            {
                isCashSale = true;
            }
            if (paymentType[i].contains("CREDIT"))
            {
                isCreditSale = true;
                requires2Receipts = true;
            }
            if (paymentType[i].contains("DEBIT"))
            {
                isDebitSale = true;
                requires2Receipts = true;
            }
            if (paymentType[i].contains("PAYCHECK"))
            {
                isPayCheckReceipt = true;
                requires2Receipts = true;
            }
        }
        int rxCntr = 0;
        //System.out.println(printerService.getPrinters());
        String receipt = "";

        DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy KK:mmaa");
        Date date = new Date();
        String displayDate;
        displayDate = dateFormat2.format(date);
        receipt += "           Smith's Super-Aid Pharmacy\n              247 Old Virginia Ave\n              Rich Creek, VA 24147\n            Main Line:(540) 726-2993\n           Office/DME: (540) 726-7331\nStore Hours: M-F 9:00AM-7:00PM Office closes @5\n             Sat: 9:00AM-2:00PM Office Closed\n             Sun: Closed\n\n";
        String tempClerkName = "Clerk: " + clerkName.substring(clerkName.indexOf(" ") + 1) + " " + clerkName.substring(0, 1) + ".";
        receipt += String.format("%-20s %24s\n", "Receipt Number", tempClerkName);
        receipt += String.format("%-20s %25s\n\n", receiptNum, displayDate);

        String s = String.format("%-10s %15s %9s\n", "Qty@Price", "Item Name", "               Price");
        String s1 = String.format("%-10s %-27s %9s\n", "----------", "---------------------------", "--------");
        receipt += s + s1;
        //print some stuff
        ArrayList<Item> items = curCart.getItems();
        for (Item item : items)
        {

            if (item.isRX() && item.isPreCharged())
            {
                prechargedTotal += item.getPriceOfItemBeforeTax();
            }
            if (item.getCategory() == 853 || item.getCategory() == 854 || item.getCategory() == 860)
            {
                requires2Receipts = true;
                if (item.getCategory() == 853)
                {
                    Database.updateChargeAccountBalance(item.getName(), item.getTotal() * -1);
                }
                else if (item.getCategory() == 854)
                {
                    Database.updateDMEAccountBalance(item.getName(), item.getTotal() * -1);
                }
            }
            String itemName = "";
            String quantity = Integer.toString(item.getQuantity()) + "@" + String.format("%.2f", round(item.getPrice()));
            Double price = round(item.getPrice() * item.getQuantity());
            Boolean isPreCharged = item.isPreCharged();
            if (item.isRX())
            {//IS RX SO WE MUST NOT USE QUANTITY
                rxCntr++;
                if (item.getName().length() > 35)
                {
                    itemName = item.getName().substring(0, 35);
                }
                else
                {
                    itemName = item.getName();
                }
                if (!isPreCharged)
                {
                    receipt += String.format("RX %-36s $%7.2f\n", itemName, price);
                }
                else
                {
                    // itemName = itemName+"$"+item.getPriceOfItemBeforeTax(); This puts the price before precharged, not using it right now
                    receipt += String.format("RX %-36s $%7s\n", itemName, "PRECHG");
                }
            }
            else if (item.getCategory() == 853)
            {//item is an RA NO QUANTITY
                if (item.getName().length() > 35)
                {
                    itemName = item.getName().substring(0, 35);
                }
                else
                {
                    itemName = item.getName();
                }
                receipt += String.format("AP %-36s $%7.2f\n", itemName, price);
            }
            else if (item.getCategory() == 854)
            {//item is an DME Account Payment NO QUANTITY
                if (item.getName().length() > 35)
                {
                    itemName = item.getName().substring(0, 35);
                }
                else
                {
                    itemName = item.getName();
                }
                receipt += String.format("DP %-36s $%7.2f\n", itemName, price);
            }
            else
            {//NORMAL ITEM
                if (item.getName().length() > 27)
                {
                    itemName = item.getName().substring(0, 27);
                }
                else
                {
                    itemName = item.getName();
                }
                if (!isPreCharged)
                {
                    receipt += String.format("%10s %-28s $%7.2f\n", quantity, itemName, price);
                }
                else
                {
                    receipt += String.format("%10s %-28s $%7s\n", quantity, itemName, "PRECHG");
                }
            }
            if (item.getDiscountPercentage() != 0)
            {//Then there is an active discount
                itemDiscounted = true;
                String percentAmt = Double.toString(item.getDiscountPercentage() * 100);
                percentAmt = percentAmt.substring(0, percentAmt.indexOf('.'));
                String discount = "               DISCOUNT: " + percentAmt + "%";
                double discount2 = round(item.getDiscountAmount());
                receipt += String.format("%-39s-$%7.2f\n", discount, discount2);
            }
        }//end for all items

        Double subTotal = curCart.getSubTotal();
        Double taxTotal = curCart.getTax();
        Double total = curCart.getTotalPrice();
        Double totalDisc = curCart.getDiscountTotal();
        receipt += "\n";//this puts a line between last time and the first total column
        if (itemDiscounted)
        {
            receipt += String.format("%35s-$%8.2f\n", "TOTAL DISCOUNT AMOUNT: ", totalDisc);
        }

        receipt += String.format("%36s$%8.2f\n%36s$%8.2f\n%36s$%8.2f\n", "SUBTOTAL: ", subTotal, "TAX: ", taxTotal, "TOTAL: ", total);
        double changeDue = 0;
        double totalPaid = 0;
        for (int i = 0; i < paymentType.length; i++)
        {
            receipt += String.format("%36s$%8.2f\n", paymentType[i], paymentAmt[i]);
            totalPaid += paymentAmt[i];
        }
        changeDue = totalPaid - total;
        receipt += String.format("%37s%8.2f\n", "CHANGE DUE: $", changeDue);
        if (prechargedTotal > 0)
        {
            receipt += String.format("%37s%8.2f\n", "TOTAL PRECHARGED: $", prechargedTotal);
        }
        if (rxCntr > 0)
        {
            receipt += "\nTotal RX(s): " + rxCntr + "\n";
        }
        if (!employeeCheckoutName.contentEquals("NO"))
        {
            receipt += "\nPurchasing Employee: " + employeeCheckoutName + "\n";

        }
        String storeCopy = "";
        if (isCreditSale || isDebitSale)
        {
            storeCopy = receipt;
            if (isCreditSale)
            {
                storeCopy += "\n\n\n\n      X___________________________________\n";
                storeCopy += "               Customer Signature\n";
            }

            storeCopy += "\nMerchant ID: " + creditInfo.get(0) + "\n";
            receipt += "\nMerchant ID: " + creditInfo.get(0) + "\n";
            storeCopy += "Approval Code: " + creditInfo.get(1) + "\n";
            receipt += "Approval Code: " + creditInfo.get(1) + "\n";
            storeCopy += "Transaction ID: " + creditInfo.get(2) + "\n";
            receipt += "Transaction ID: " + creditInfo.get(2) + "\n";
            if (!creditInfo.get(3).contentEquals(""))
            {
                EasterEgg ee = new EasterEgg("images/apple.wav");// This is on CHIP READER SOUND!
                storeCopy += "AID: " + creditInfo.get(3) + "\n";
                receipt += "AID: " + creditInfo.get(3) + "\n";
                storeCopy += "TVR: " + creditInfo.get(4) + "\n";
                receipt += "TVR: " + creditInfo.get(4) + "\n";
                storeCopy += "TSI: " + creditInfo.get(5) + "\n";
                receipt += "TSI: " + creditInfo.get(5) + "\n";
            }
            storeCopy += "\n       I AGREE TO PAY ABOVE TOTAL AMOUNT IN\n      ACCORDANCE WITH CARD ISSUER'S AGREEMENT\n";
            receipt += "\n       I AGREE TO PAY ABOVE TOTAL AMOUNT IN\n      ACCORDANCE WITH CARD ISSUER'S AGREEMENT\n";
        }
        storeCopy += "\n            STORE RETURN POLICY\nAny RX that leaves the building cannot be\nreturned. Any consumable item must be unopened. All other items are subject to inspection upon\nreturn and must be in good, unused condition.\nYou must have a copy of your receipt. Item must be returned within 30 days of purchase. All\nclearance item sales are final.\n\n";
        receipt += "\n            STORE RETURN POLICY\nAny RX that leaves the building cannot be\nreturned. Any consumable item must be unopened. All other items are subject to inspection upon\nreturn and must be in good, unused condition.\nYou must have a copy of your receipt. Item must be returned within 30 days of purchase. All\nclearance item sales are final.\n\n";
        storeCopy += "            Thanks for shopping at\n          Smith's Super-Aid Pharmacy\n       \"The professional pharmacy with\n           that hometown feeling!\"\n\n                  ";
        receipt += "            Thanks for shopping at\n          Smith's Super-Aid Pharmacy\n       \"The professional pharmacy with\n           that hometown feeling!\"\n\n                 ";

        if (isCreditSale || isDebitSale)
        {
            storeCopy += "STORE COPY\n\n\n\n\n\n\n";
            receipt += "CUSTOMER COPY\n\n\n\n\n\n\n";
            printerService.printString(printerName, storeCopy);

        }
        else
        {
            storeCopy += "\n\n\n\n\n\n\n";
            receipt += "\n\n\n\n\n\n\n";
            printerService.printString(printerName, receipt);
        }

        // cut that paper!
        byte[] cutP = new byte[]
        {
            0x1d, 'V', 1
        };
        byte[] kickDrawer = new byte[]
        {
            27, 112, 48, 55, 121
        };
        boolean drawerHasBeenKicked = false;
        if (changeDue > 0 || (isCashSale && curCart.getTotalPrice() != 0))
        {
            printerService.printBytes(printerName, kickDrawer);
            drawerHasBeenKicked = true;
        }

        printerService.printBytes(printerName, cutP);
        if (requires2Receipts)
        {
            printerService.printString(printerName, receipt);
            printerService.printBytes(printerName, cutP);
            /* if (!drawerHasBeenKicked) {
                printerService.printBytes(printerName, kickDrawer);
                drawerHasBeenKicked = true;
            }*/

        }

        myself.previousReceipt = receipt;
        Database.storeReceiptString(receiptNum, receipt);
        storeReceiptData(curCart, clerkName, paymentType, paymentAmt, receiptNum, false, employeeCheckoutName, myself, isPayCheckReceipt);

    }

    public void beginNoSaleCheckout(String employeeName) {
        PrinterService printerService = new PrinterService();
        byte[] kickDrawer = new byte[]
        {
            27, 112, 48, 55, 121
        };
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
        paymentType[0] = "CASH REFUND: ";

        printRefundReceipt(refundCart, clerkName, paymentType, paymentAmt, refundCart.receiptNum, myself, null);
        handleRefundItems(refundCart, clerkName, guiRefundItems, myself);
    }

    String beginRefundCardCheckout(RefundCart refundCart, String clerkName, ArrayList<GuiRefundCartItem> guiRefundItems, MainFrame myself) {
        ArrayList<String> creditInfo = new ArrayList<>();

        CardDataRequester cdr = new CardDataRequester();
        cdr.postRequest(ConfigFileReader.getCardReaderURL(), Double.toString(refundCart.getTotalPrice()), "CCR9");
        myself.setEnabled(true);

        if (cdr.transTerminated())
        {
            return cdr.responseText;
        }

        double[] paymentAmt = new double[1];
        String[] paymentType = new String[1];
        paymentAmt[0] = refundCart.getTotalPrice();
        paymentType[0] = String.format(cdr.cardType + " CARD REFUND:" + cdr.last4ofCard + ": ");

        Database.updateReceipt(refundCart, refundCart.receiptNum);

        creditInfo.add("3130031394051");//Merchant ID
        creditInfo.add(cdr.approvalCode);
        creditInfo.add(cdr.transID);
        creditInfo.add(cdr.AID);
        creditInfo.add(cdr.TVR);
        creditInfo.add(cdr.TSI);
        creditInfo.add("CREDIT");

        printRefundReceipt(refundCart, clerkName, paymentType, paymentAmt, refundCart.receiptNum, myself, creditInfo);
        handleRefundItems(refundCart, clerkName, guiRefundItems, myself);

        return "SMITHSAPPROVEDCODE";
    }

    public void handleRefundItems(RefundCart refundCart, String clerkName, ArrayList<GuiRefundCartItem> guiRefundItems, MainFrame myself) {
        ArrayList<RefundItem> items2Add = new ArrayList<>();
        ArrayList<RefundItem> items2Del = new ArrayList<>();
        for (RefundItem item : refundCart.getRefundItems())
        {
            if (item.refundAllActive())
            {
                if (refundCart.containsItemByID(item.getID() + "F") || refundCart.containsItemByID(item.getID() + "TF"))
                {
                    if (item.getID().length() > 6)
                    {
                        refundCart.increaseQtyByID(item.getID() + "F", item.quantityBeingRefunded);
                    }
                    else
                    {
                        refundCart.increaseQtyByID(item.getID() + "TF", item.quantityBeingRefunded);
                    }
                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0)
                    {
                        items2Del.add(item);
                    }
                }
                else
                {
                    boolean hasBeenAddedAlready = false;
                    for (RefundItem item2 : items2Add)
                    {
                        if (item2.getID().contentEquals(item.getID() + "F") || item2.getID().contentEquals(item.getID() + "TF"))
                        {
                            hasBeenAddedAlready = true;
                            item2.quantity += item.quantityBeingRefunded;
                        }
                    }
                    if (!hasBeenAddedAlready)
                    {
                        RefundItem itemTemp = new RefundItem(item);
                        if (item.getID().length() > 6)
                        {
                            itemTemp.setID(item.getID() + "F");//FINISHED!
                        }
                        else
                        {
                            itemTemp.setID(item.getID() + "TF");//FINISHED!
                        }
                        itemTemp.quantity = item.quantityBeingRefunded;
                        itemTemp.hasBeenRefunded = true;
                        itemTemp.hasTaxBeenRefunded = true;
                        itemTemp.refundAllActive = false;
                        itemTemp.refundTaxOnly = false;
                        itemTemp.quantityBeingRefunded = 0;
                        items2Add.add(itemTemp);

                    }
                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0)
                    {
                        items2Del.add(item);
                    }
                }
            }
            else if (item.refundTaxOnly() && !item.refundAllActive())
            {
                if (refundCart.containsItemByID(item.getID() + "T"))
                {
                    refundCart.increaseQtyByID(item.getID() + "T", item.quantityBeingRefunded);

                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0)
                    {
                        items2Del.add(item);
                    }
                }
                else
                {
                    boolean hasBeenAddedAlready = false;
                    for (RefundItem item2 : items2Add)
                    {
                        if (item2.getID().contentEquals(item.getID() + "T"))
                        {
                            hasBeenAddedAlready = true;
                            item2.quantity += item.quantityBeingRefunded;
                        }
                    }
                    if (!hasBeenAddedAlready)
                    {
                        RefundItem itemTemp = new RefundItem(item);
                        itemTemp.mutID = item.getID() + "T";//TAX REFUNDED!
                        itemTemp.quantity = item.quantityBeingRefunded;

                        itemTemp.hasTaxBeenRefunded = true;
                        itemTemp.refundTaxOnly = false;
                        itemTemp.quantityBeingRefunded = 0;
                        System.out.println(item.quantity + " NOW" + itemTemp.quantity + "AND " + item.quantityBeingRefunded);
                        items2Add.add(itemTemp);
                    }
                    item.quantity -= item.quantityBeingRefunded;
                    if (item.quantity == 0)
                    {
                        items2Del.add(item);
                    }
                }
            }
        }
        if (!items2Del.isEmpty())
        {
            System.out.println("Beginning Removal...");
            Database.removeReceiptByList(items2Del, refundCart.receiptNum);
            for (RefundItem item : items2Del)
            {
                System.out.println("Removing: " + item.getName());
                refundCart.removeItem(item);
            }
        }
        if (!items2Add.isEmpty())
        {
            System.out.println("Beginning Store New Items...");
            Database.storeReceiptByList(items2Add, refundCart.receiptNum);
        }
        System.out.println("Updating Existing Items...");
        Database.updateReceipt(refundCart, refundCart.receiptNum);
        myself.voidCarts();
        myself.refundOver();
    }

    public void printRefundReceipt(RefundCart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String receiptNum, MainFrame myself, ArrayList<String> creditInfo) {
        PrinterService printerService = new PrinterService();
        boolean itemDiscounted = false;
        String receipt = "";
        boolean isCreditSale = false;
        boolean requires2Receipts = false;
        for (int i = 0; i < paymentType.length; i++)
        {
            if (paymentType[i].contains("CARD"))
            {
                isCreditSale = true;
                requires2Receipts = true;
            }
        }
        DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy KK:mmaa");
        Date date = new Date();
        String displayDate;
        displayDate = dateFormat2.format(date);
        receipt += "           Smith's Super-Aid Pharmacy\n              247 Old Virginia Ave\n              Rich Creek, VA 24147\n            Main Line:(540) 726-2993\n           Office/DME: (540) 726-7331\nStore Hours: M-F 9:00AM-7:00PM Office closes @5\n             Sat: 9:00AM-2:00PM Office Closed\n             Sun: Closed\n\n";
        String tempClerkName = "Clerk: " + clerkName.substring(clerkName.indexOf(" ") + 1) + " " + clerkName.substring(0, 1) + ".";
        receipt += String.format("%-20s %24s\n", "Receipt Number", tempClerkName);
        receipt += String.format("%-20s %25s\n\n", receiptNum, displayDate);

        String s = String.format("%-10s %15s %9s\n", "Qty@Price", "Item Name", "               Price");
        String s1 = String.format("%-10s %-27s %9s\n", "----------", "---------------------------", "--------");
        receipt += s + s1;
        //print some stuff
        ArrayList<RefundItem> items = curCart.getRefundItems();
        for (RefundItem item : items)
        {
            if (item.refundAllActive || item.refundTaxOnly)
            {
                String itemName = "";
                String quantity = Integer.toString(item.quantityBeingRefunded) + "@" + String.format("%.2f", round(item.getPrice()));
                Double price = round(item.getPrice() * item.quantityBeingRefunded);
                if (item.refundAllActive)
                {
                    if (item.getName().length() > 27)
                    {
                        itemName = item.getName().substring(0, 27);
                    }
                    else
                    {
                        itemName = item.getName();
                    }
                }
                else if (item.refundTaxOnly)
                {
                    price = item.getTaxTotal();
                    if (item.getName().length() > 24)
                    {
                        itemName = item.getName().substring(0, 24) + " TR";

                    }
                    else
                    {
                        itemName = item.getName() + " TR";
                    }
                }
                receipt += String.format("%10s %-28s $%7.2f\n", quantity, itemName, price);

            }
            if (item.getDiscountPercentage() != 0)
            {//Then there is an active discount
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
        if (itemDiscounted)
        {
            receipt += String.format("%35s-$%8.2f\n", "TOTAL DISCOUNT AMOUNT: ", totalDisc);
        }

        receipt += String.format("%36s$%8.2f\n%36s$%8.2f\n%36s$%8.2f\n", "SUBTOTAL REFUNDED: ", subTotal, "TAX REFUNDED: ", taxTotal, "TOTAL REFUNDED: ", total);
        for (int i = 0; i < paymentType.length; i++)
        {
            receipt += String.format("%36s$%8.2f\n", paymentType[i], paymentAmt[i]);
        }

        String storeCopy = "";
        if (isCreditSale)
        {
            storeCopy = receipt;
            if (isCreditSale)
            {
                storeCopy += "\n\n\n\n      X___________________________________\n";
                storeCopy += "               Customer Signature\n";
            }

            storeCopy += "\nMerchant ID: " + creditInfo.get(0) + "\n";
            receipt += "\nMerchant ID: " + creditInfo.get(0) + "\n";
            storeCopy += "Approval Code: " + creditInfo.get(1) + "\n";
            receipt += "Approval Code: " + creditInfo.get(1) + "\n";
            storeCopy += "Transaction ID: " + creditInfo.get(2) + "\n";
            receipt += "Transaction ID: " + creditInfo.get(2) + "\n";
            if (!creditInfo.get(3).contentEquals(""))
            {
                storeCopy += "AID: " + creditInfo.get(3) + "\n";
                receipt += "AID: " + creditInfo.get(3) + "\n";
                storeCopy += "TVR: " + creditInfo.get(4) + "\n";
                receipt += "TVR: " + creditInfo.get(4) + "\n";
                storeCopy += "TSI: " + creditInfo.get(5) + "\n";
                receipt += "TSI: " + creditInfo.get(5) + "\n";
            }
            storeCopy += "\n       I AGREE TO PAY ABOVE TOTAL AMOUNT IN\n      ACCORDANCE WITH CARD ISSUER'S AGREEMENT\n";
            receipt += "\n       I AGREE TO PAY ABOVE TOTAL AMOUNT IN\n      ACCORDANCE WITH CARD ISSUER'S AGREEMENT\n";
        }
        storeCopy += "\n            STORE RETURN POLICY\nAny RX that leaves the building cannot be\nreturned. Any consumable item must be unopened. All other items are subject to inspection upon\nreturn and must be in good, unused condition.\nYou must have a copy of your receipt. Item must be returned within 30 days of purchase. All\nclearance item sales are final.\n\n";
        receipt += "\n            STORE RETURN POLICY\nAny RX that leaves the building cannot be\nreturned. Any consumable item must be unopened. All other items are subject to inspection upon\nreturn and must be in good, unused condition.\nYou must have a copy of your receipt. Item must be returned within 30 days of purchase. All\nclearance item sales are final.\n\n";
        storeCopy += "            Thanks for shopping at\n          Smith's Super-Aid Pharmacy\n       \"The professional pharmacy with\n           that hometown feeling!\"\n\n                  ";
        receipt += "            Thanks for shopping at\n          Smith's Super-Aid Pharmacy\n       \"The professional pharmacy with\n           that hometown feeling!\"\n\n                 ";

        byte[] cutP = new byte[]
        {
            0x1d, 'V', 1
        };
        if (isCreditSale)
        {
            storeCopy += "STORE COPY\n\n\n\n\n\n\n";
            receipt += "CUSTOMER COPY\n\n\n\n\n\n\n";
            printerService.printString(printerName, storeCopy);
            printerService.printBytes(printerName, cutP);

        }
        else
        {
            storeCopy += "\n\n\n\n\n\n\n";
            receipt += "\n\n\n\n\n\n\n";
            printerService.printString(printerName, receipt);
            printerService.printBytes(printerName, cutP);
        }

        // cut that paper!
        byte[] kickDrawer = new byte[]
        {
            27, 112, 48, 55, 121
        };
        if (requires2Receipts)
        {
            printerService.printString(printerName, receipt);
            printerService.printBytes(printerName, cutP);
        }
        printerService.printBytes(printerName, kickDrawer);
        myself.previousReceipt = receipt;
        myself.changeDue.setText("Change Due: $" + String.format("%.2f", total));
        myself.displayChangeDue = true;
        storeReceiptData(curCart, clerkName, paymentType, paymentAmt, receiptNum, true, "NO", myself, false);
    }

    public void reprintReceipt(String receipt) {
        PrinterService printerService = new PrinterService();
        printerService.printString(printerName, receipt);

        // cut that paper!
        byte[] cutP = new byte[]
        {
            0x1d, 'V', 1
        };
        printerService.printBytes(printerName, cutP);
    }

    public void beginPaidOut(String description, double amount) {
        DrawerReport dr = null;
        PrinterService printerService = new PrinterService();
        byte[] kickDrawer = new byte[]
        {
            27, 112, 48, 55, 121
        };
        printerService.printBytes(printerName, kickDrawer);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        String todaysDate = dateFormat.format(date);
        try
        {

            File f = new File(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
            if (f.exists() && !f.isDirectory())
            {
                // read object from file
                FileInputStream fis = new FileInputStream(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
                ObjectInputStream ois = new ObjectInputStream(fis);
                dr = (DrawerReport) ois.readObject();
                ois.close();

                dr.paidOut(description, amount);

            }
            else
            {
                dr = new DrawerReport(description, amount);

            }

            // write object to file
            FileOutputStream fos = new FileOutputStream(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            dr.printReport();
            oos.writeObject(dr);
            oos.close();

            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void beginMasterRefund(double amount, String description) {
        DrawerReport dr = null;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        String todaysDate = dateFormat.format(date);
        try
        {

            File f = new File(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
            if (f.exists() && !f.isDirectory())
            {
                // read object from file
                FileInputStream fis = new FileInputStream(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
                ObjectInputStream ois = new ObjectInputStream(fis);
                dr = (DrawerReport) ois.readObject();
                ois.close();
                dr.masterRefund(amount, description);
            }
            else
            {
                dr = new DrawerReport(amount, description);
            }

            // write object to file
            FileOutputStream fos = new FileOutputStream(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            dr.printReport();
            oos.writeObject(dr);
            oos.close();
            PrinterService printerService = new PrinterService();
            byte[] kickDrawer = new byte[]
            {
                27, 112, 48, 55, 121
            };
            printerService.printBytes(printerName, kickDrawer);
            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());
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
    }

    public void storeReceiptData(Cart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String receiptNum, boolean isRefund, String employeeCheckoutName, MainFrame mainFrame, boolean isPayCheckReceipt) {
        DrawerReport dr = null;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyy");
        String todaysDate = dateFormat.format(date);
        try
        {

            File f = new File(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
            if (f.exists() && !f.isDirectory())
            {
                // read object from file
                FileInputStream fis = new FileInputStream(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
                ObjectInputStream ois = new ObjectInputStream(fis);
                dr = (DrawerReport) ois.readObject();
                ois.close();
                if (!isRefund)
                {

                    dr.update(curCart, clerkName, paymentType, paymentAmt, employeeCheckoutName, isPayCheckReceipt);
                }
                else
                {
                    dr.refundUpdate((RefundCart) curCart, clerkName, paymentType, paymentAmt);
                }
            }
            else
            {
                if (!isRefund)
                {
                    dr = new DrawerReport(curCart, clerkName, paymentType, paymentAmt, employeeCheckoutName, isPayCheckReceipt);
                }
                else
                {
                    dr = new DrawerReport((RefundCart) curCart, clerkName, paymentType, paymentAmt, isRefund);
                }
            }

            mainFrame.estimatedCoinTotal = dr.totalCoinsAmt;
            mainFrame.estimatedCashTotal = Math.round(dr.totalCashAmt);
            mainFrame.estimatedCheckTotal = dr.totalChecksAmt;
            mainFrame.estimatedLunchTotal = dr.lunchTotalAmt;
            // write object to file
            FileOutputStream fos = new FileOutputStream(ConfigFileReader.getRegisterReportPath() + todaysDate + ConfigFileReader.getRegisterID() + ".posrf");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            dr.printReport();
            oos.writeObject(dr);
            oos.close();

            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());
        }
        catch (FileNotFoundException e)
        {
            try
            {
                String emergencyDrivePath = "C:\\POS\\Emergency_Report_Saves\\";
                File f = new File(emergencyDrivePath + ConfigFileReader.getRegisterID() + ".posrf");
                if (f.exists() && !f.isDirectory())
                {
                    // read object from file
                    FileInputStream fis = new FileInputStream(emergencyDrivePath + ConfigFileReader.getRegisterID() + ".posrf");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    dr = (DrawerReport) ois.readObject();
                    ois.close();
                    if (!isRefund)
                    {
                        dr.update(curCart, clerkName, paymentType, paymentAmt, employeeCheckoutName, isPayCheckReceipt);
                    }
                    else
                    {
                        dr.refundUpdate((RefundCart) curCart, clerkName, paymentType, paymentAmt);
                    }
                }
                else
                {
                    if (!isRefund)
                    {
                        dr = new DrawerReport(curCart, clerkName, paymentType, paymentAmt, employeeCheckoutName, isPayCheckReceipt);
                    }
                    else
                    {
                        dr = new DrawerReport((RefundCart) curCart, clerkName, paymentType, paymentAmt, isRefund);
                    }
                }

                mainFrame.estimatedCoinTotal = dr.totalCoinsAmt;
                mainFrame.estimatedCashTotal = Math.round(dr.totalCashAmt);
                mainFrame.estimatedCheckTotal = dr.totalChecksAmt;
                mainFrame.estimatedLunchTotal = dr.lunchTotalAmt;
                // write object to file
                FileOutputStream fos = new FileOutputStream(emergencyDrivePath + ConfigFileReader.getRegisterID() + ".posrf");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                dr.printReport();
                oos.writeObject(dr);
                oos.close();
                System.out.println("JERE");
                e.printStackTrace();
            }
            catch (FileNotFoundException ex)
            {

            }
            catch (IOException exx)
            {
                System.out.println("EERE");
                e.printStackTrace();
            }
            catch (ClassNotFoundException exxx)
            {
                e.printStackTrace();
                System.out.println("JERsE");
            }
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

        // System.out.println("CREATED: "+dr.getTotalCashAmt());
    }//end store data

}
