package database_console;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**

 @author A.Smith
 */
public class DrawerReport implements Serializable {

    private static final long serialVersionUID = 7526472295622776149L;
    private static final String HOLLIEFILENAME = "C:\\pos\\REPORTS\\HollieAccnt";
    private static final String DEBBIEFILENAME = "C:\\pos\\REPORTS\\DebbieAccnt";
    private static final String REPORTFILENAME = "C:\\pos\\REPORTS\\DailyReport";
    BufferedWriter bw = null;
    FileWriter fw = null;
    final private double TAXRATE = 0.053;
    CategoryFinder catFind = new CategoryFinder();

    private ArrayList<String> employeeNames = new ArrayList<String>();//UPDATED
    private ArrayList<Integer> employeeTransactions = new ArrayList<Integer>();//UPDATED

    //TOTAL AMOUNTS OF $ IN DRAWERS
    protected double totalCoinsAmt = 0;//UPDATED
    protected double totalCashAmt = 0;//UPDATED
    protected double totalChecksAmt = 0;//UPDATED
    protected double totalCreditAmt = 0;//UPDATED
    protected double totalDebitAmt = 0;

    protected double totalPayCheckPayments = 0;//This is used to record money that was deducted from employee checks for tabs
    protected double lunchChargedAmt = 0;

    //Total Refunded Today
    private double totalRefundedCash = 0;//UPDATED
    private double totalRefundedCredit = 0;//UPDATED
    public ArrayList<String> masterRefundDescriptionAndAmt = new ArrayList<>();

    //lunch counter
    private ArrayList<String> lunchErrorLog = new ArrayList<>();
    protected double lunchTotalAmt = 0;
    private double lunchTotalCash = 0;
    private double lunchTotalCredit = 0;
    private double lunchTotalDebit = 0;
    private double lunchTotalCheck = 0;
    private ArrayList<String> employeesWhoPaidForItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> itemsEmployeesBought = new ArrayList<>();
    private ArrayList<Double> amtEmployeePaidForAllItems = new ArrayList<>();

    //Total American Greetings Cards Sold
    private double totalAmericanGreetings = 0;

    //Total Tax Charged Today
    private double totalTaxCharged = 0;//UPDATED

    //MANUAL CHARGES
    private double totalChargesRXAmt = 0;//UPDATED

    protected ArrayList<String> accountNameCharged = new ArrayList<String>();//UPDATED
    protected ArrayList<Double> amountChargedToAccount = new ArrayList<Double>();//UPDATED
    protected ArrayList<ArrayList<String>> itemsChargedToAccount = new ArrayList<ArrayList<String>>();//YES THIS LOOKS THAT CRAZY. //UPDATED
    protected ArrayList<ArrayList<String>> receiptNumbersChargedToAccount = new ArrayList<ArrayList<String>>();

    //PAID OUTS
    private ArrayList<String> descriptionsPO = new ArrayList<String>();//UPDATED
    private ArrayList<Double> amountsPO = new ArrayList<Double>();//UPDATED

    //ACCOUNT PAYMENT TOTALS
    private double totalARPayments = 0;//UPDATED
    protected ArrayList<String> ARAccountName = new ArrayList<String>();//UPDATED
    protected ArrayList<Double> amountPaidToARAccount = new ArrayList<Double>();//UPDATED
    protected ArrayList<ArrayList<String>> receiptNumbersPaidToAccount = new ArrayList<ArrayList<String>>();

    private double totalDMEPayments = 0;//UPDATED
    protected ArrayList<String> DMEAccountName = new ArrayList<String>();//UPDATED
    protected ArrayList<Double> amountPaidToDMEAccount = new ArrayList<Double>();//UPDATED
    protected ArrayList<ArrayList<String>> receiptNumbersPaidToDMEAccount = new ArrayList<ArrayList<String>>();

    //ITEM CATEGORIES
    private double otcTaxedTotal = 0;//updated
    private double otcNonTaxedTotal = 0;//UPDATED
    private double dmeWithTax = 0;//UPDATED
    private double dmeWithoutTax = 0;//UPDATED
    private double totalRXCoppay = 0;//UPDATED
    private double totalPaperSales = 0;//UPDATED
    private double totalUPSSales = 0;//UPDATED

    DrawerReport(Cart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String employeeCheckoutName, boolean isPayCheckReceipt, String receiptNumber) {
        //FIRST TIME REPORT IS MADE. DEFAULT VALUE TIME!
        update(curCart, clerkName, paymentType, paymentAmt, employeeCheckoutName, isPayCheckReceipt, receiptNumber);
    }//end DrawerReportCtor

    DrawerReport(RefundCart curCart, String clerkName, String[] paymentType, double[] paymentAmt, boolean isRefund) {
        //FIRST TIME REPORT IS MADE. DEFAULT VALUE TIME!
        refundUpdate(curCart, clerkName, paymentType, paymentAmt);
    }//end DrawerReportCtor

    DrawerReport(String description, double amount) {
        paidOut(description, amount);
    }

    DrawerReport(double amount, String description) {//master Refund
        masterRefundDescriptionAndAmt.add(description + "@" + amount);
        totalRefundedCash = amount;
        totalCashAmt -= amount;

        String cash = Double.toString(totalCashAmt);
        boolean isNegative = false;
        if (totalCashAmt < 0)
        {
            isNegative = true;
        }
        totalCashAmt = Double.parseDouble(cash.substring(0, cash.indexOf('.')));
        totalCoinsAmt = Double.parseDouble(cash.substring(cash.indexOf('.')));
        totalCoinsAmt = round(totalCoinsAmt);
        totalCashAmt = round(totalCashAmt);
        if (isNegative)
        {
            totalCoinsAmt = round(totalCoinsAmt * -1);
        }
    }

    public void masterRefund(double amount, String description) {
        masterRefundDescriptionAndAmt.add(description + "@" + amount);
        totalRefundedCash += amount;
        totalCashAmt += totalCoinsAmt;
        totalCashAmt -= amount;

        String cash = Double.toString(totalCashAmt);
        boolean isNegative = false;
        if (totalCashAmt < 0)
        {
            isNegative = true;
        }
        totalCashAmt = Double.parseDouble(cash.substring(0, cash.indexOf('.')));
        totalCoinsAmt = Double.parseDouble(cash.substring(cash.indexOf('.')));
        totalCoinsAmt = round(totalCoinsAmt);
        totalCashAmt = round(totalCashAmt);
        if (isNegative)
        {
            totalCoinsAmt = round(totalCoinsAmt * -1);
        }

    }

    public void refundUpdate(RefundCart curCart, String clerkName, String[] paymentType, double paymentAmt[]) {
        if (paymentType[0].contains("CARD"))
        {
            totalCreditAmt -= curCart.getTotalPrice();
            totalRefundedCredit += curCart.getTotalPrice();
        }
        else
        {//Cash\
            totalRefundedCash = curCart.getTotalPrice();
            totalCashAmt += totalCoinsAmt;
            totalCashAmt -= curCart.getTotalPrice();

            String cash = Double.toString(totalCashAmt);
            boolean isNegative = false;
            if (totalCashAmt < 0)
            {
                isNegative = true;
            }
            totalCashAmt = Double.parseDouble(cash.substring(0, cash.indexOf('.')));
            totalCoinsAmt = Double.parseDouble(cash.substring(cash.indexOf('.')));
            totalCoinsAmt = round(totalCoinsAmt);
            totalCashAmt = round(totalCashAmt);
            if (isNegative)
            {
                totalCoinsAmt = round(totalCoinsAmt * -1);
            }
        }

        totalTaxCharged -= curCart.getTax();
    }

    public void update(Cart curCart, String clerkName, String[] paymentType, double[] paymentAmt, String employeeCheckoutName, boolean isPayCheckReceipt, String receiptNumber) {

        //UPDATE CLERK TRANSACTION COUNT!
        if (employeeNames != null && !employeeNames.isEmpty() && employeeNames.contains(clerkName))
        {
            int index = employeeNames.indexOf(clerkName);
            int value = employeeTransactions.get(index) + 1;
            employeeTransactions.set(index, value);
        }
        else
        {
            employeeNames.add(clerkName);
            employeeTransactions.add(1);
        }
        if (isPayCheckReceipt)
        {
            totalPayCheckPayments += paymentAmt[0];
        }
        else
        {

            double totalPaid = 0;
            for (int i = 0; i < paymentType.length; i++)
            {
                totalPaid += paymentAmt[i];
            }
            double changeDue = totalPaid - curCart.getTotalPrice();
            //UPDATE AMOUNT RECEIVED BY TENDER TYPE
            for (int i = 0; i < paymentType.length; i++)
            {
                if (paymentType[i].contains("CASH"))
                {
                    totalCashAmt += paymentAmt[i];
                    totalCashAmt += totalCoinsAmt;
                    String cash = Double.toString(totalCashAmt);
                    boolean isNegative = false;
                    if (totalCashAmt < 0)
                    {
                        isNegative = true;
                    }
                    totalCashAmt = Double.parseDouble(cash.substring(0, cash.indexOf('.')));
                    totalCoinsAmt = Double.parseDouble(cash.substring(cash.indexOf('.')));
                    totalCoinsAmt = round(totalCoinsAmt);
                    totalCashAmt = round(totalCashAmt);
                    if (isNegative)
                    {
                        totalCoinsAmt = round(totalCoinsAmt * -1);
                    }

                }
                else if (paymentType[i].contains("CREDIT"))
                {
                    totalCreditAmt += paymentAmt[i];
                    totalCreditAmt = round(totalCreditAmt);
                }
                else if (paymentType[i].contains("DEBIT"))
                {
                    totalDebitAmt += paymentAmt[i];
                    totalDebitAmt = round(totalDebitAmt);
                }
                else if (paymentType[i].contains("CHECK"))
                {
                    totalChecksAmt += paymentAmt[i];
                    totalChecksAmt = round(totalChecksAmt);
                }
                else if (paymentType[i].contains("CHARGED"))
                {
                    totalChargesRXAmt += paymentAmt[i];
                    totalChargesRXAmt = round(totalChargesRXAmt);
                }

            }
            if (changeDue > 0)
            {
                totalCashAmt += totalCoinsAmt;
                totalCashAmt -= changeDue;
                String cash = Double.toString(totalCashAmt);
                boolean isNegative = false;
                if (totalCashAmt < 0)
                {
                    isNegative = true;
                }
                totalCashAmt = Double.parseDouble(cash.substring(0, cash.indexOf('.')));
                totalCoinsAmt = Double.parseDouble(cash.substring(cash.indexOf('.')));
                totalCoinsAmt = round(totalCoinsAmt);
                totalCashAmt = round(totalCashAmt);
                if (isNegative)
                {
                    totalCoinsAmt = round(totalCoinsAmt * -1);
                }
            }

            boolean charged = false;//This is used to add items to charge account arraylist
            if (paymentType[0].contains("CHARGED"))
            {
                charged = true;
            }
            for (Item item : curCart.getItems())
            {
                totalTaxCharged = round(totalTaxCharged + item.getTaxTotal());
                if (item.getCategory() == 853)
                {//ACCOUNT PAYEMENT!
                    if (ARAccountName != null && !ARAccountName.isEmpty() && ARAccountName.contains(item.getName()))
                    {//if account name isnt null, isnt empty, and contains the account already
                        int index = ARAccountName.indexOf(item.getName());
                        double amount = round(amountPaidToARAccount.get(index) + item.getTotal());
                        amountPaidToARAccount.set(index, amount);
                        boolean found = false;
                        for (String receipt : receiptNumbersPaidToAccount.get(index))
                        {
                            if (receipt.contentEquals(receiptNumber))
                            {
                                found = true;
                                break;
                            }
                        }
                        if (!found)
                        {
                            receiptNumbersPaidToAccount.get(index).add(receiptNumber);
                        }
                    }
                    else
                    {
                        ArrayList<String> receipts = new ArrayList<>();
                        receipts.add(receiptNumber);
                        receiptNumbersPaidToAccount.add(receipts);
                        ARAccountName.add(item.getName());
                        amountPaidToARAccount.add(item.getTotal());
                    }
                    totalARPayments = round(totalARPayments + item.getTotal());
                }
                else if (item.getCategory() == 854)
                {//DME ACCOUNT PAYMENT!
                    if (DMEAccountName != null && !DMEAccountName.isEmpty() && DMEAccountName.contains(item.getName()))
                    {//if account name isnt null, isnt empty, and contains the account already
                        int index = DMEAccountName.indexOf(item.getName());
                        double amount = round(amountPaidToDMEAccount.get(index) + item.getTotal());
                        amountPaidToDMEAccount.set(index, amount);
                        boolean found = false;
                        for (String receipt : receiptNumbersPaidToDMEAccount.get(index))
                        {
                            if (receipt.contentEquals(receiptNumber))
                            {
                                found = true;
                                break;
                            }
                        }
                        if (!found)
                        {
                            receiptNumbersPaidToDMEAccount.get(index).add(receiptNumber);
                        }
                    }
                    else
                    {
                        DMEAccountName.add(item.getName());
                        amountPaidToDMEAccount.add(item.getTotal());
                        ArrayList<String> receiptsForAccount = new ArrayList<>();
                        receiptsForAccount.add(receiptNumber);
                        receiptNumbersPaidToDMEAccount.add(receiptsForAccount);
                    }
                    totalDMEPayments = round(totalDMEPayments + item.getTotal());
                }
                else if (item.getCategory() == 855)
                {//NEWSPAPER
                    totalPaperSales += item.getPriceOfItemsBeforeTax();
                }
                else if (item.isRX && !item.isPreCharged())
                {//RX
                    System.out.println("HERE2!");
                    totalRXCoppay += item.getPriceOfItemsBeforeTax();
                }
                else if (item.isRX() && item.isPreCharged())
                {
                    //DO NOTHING!
                }
                else if (item.getCategory() == 856)
                {
                    totalAmericanGreetings += item.getPriceOfItemsBeforeTax();
                }
                else if (item.getCategory() == 860)
                {
                    totalUPSSales += item.getPriceOfItemsBeforeTax();

                }
                else if (item.getCategory() == 621 || item.getCategory() == 622 || item.getCategory() == 623 || item.getCategory() == 624 || item.getCategory() == 628 || item.getCategory() == 631 || item.getCategory() == 632 || item.getCategory() == 633 || item.getCategory() == 634 || item.getCategory() == 635 || item.getCategory() == 636 || item.getCategory() == 637 || item.getCategory() == 639 || item.getCategory() == 640 || item.getCategory() == 641 || item.getCategory() == 642 || item.getCategory() == 643 || item.getCategory() == 862)
                {//DME
                    if (item.isTaxable())
                    {
                        dmeWithTax += item.getPriceOfItemsBeforeTax();
                    }
                    else
                    {
                        dmeWithoutTax += item.getPriceOfItemsBeforeTax();
                    }
                }
                else if (item.itemName.toUpperCase().contentEquals("LUNCH"))
                {
                    if (employeeCheckoutName.contentEquals("NO"))
                    {
                        lunchErrorLog.add(clerkName + " Failed to enter Employee for Lunch in amount of: $" + item.getTotal());
                    }
                }
                else
                {//MUST BE AN OTC CATEGORY!
                    if (item.isTaxable())
                    {
                        otcTaxedTotal += item.getPriceOfItemsBeforeTax();
                    }
                    else
                    {
                        otcNonTaxedTotal += item.getPriceOfItemsBeforeTax();
                    }
                }

                if (charged)
                {
                    if (accountNameCharged != null && accountNameCharged.contains(paymentType[0].substring(11)) && !item.isPreCharged)
                    {
                        int index = accountNameCharged.indexOf(paymentType[0].substring(11));
                        double amount = round(amountChargedToAccount.get(index) + item.getTotal());
                        amountChargedToAccount.set(index, amount);
                        ArrayList<String> items = itemsChargedToAccount.get(index);
                        items.add(item.getQuantity() + "  " + item.getName() + "  " + round(item.getTotal()));
                        System.out.println(index + "SIZE OF " + itemsChargedToAccount.size());

                    }
                    else if (!item.isPreCharged)
                    {
                        accountNameCharged.add(paymentType[0].substring(11));
                        amountChargedToAccount.add(round((item.getTotal())));
                        ArrayList<String> items = new ArrayList<String>();
                        ArrayList<String> receiptNums = new ArrayList<String>();
                        receiptNumbersChargedToAccount.add(receiptNums);//used later.
                        items.add(item.getQuantity() + "  " + item.getName() + "  " + round(item.getTotal()));

                        itemsChargedToAccount.add(items);
                        System.out.println("SIZE OF " + itemsChargedToAccount.size());
                    }//end else not added yet
                }//end if Charged

                if (!employeeCheckoutName.contentEquals("NO"))
                {
                    //lunch counter
                    if (!employeesWhoPaidForItems.contains(employeeCheckoutName))
                    {
                        employeesWhoPaidForItems.add(employeeCheckoutName);
                        ArrayList<String> tempItem = new ArrayList<>();
                        tempItem.add(item.getQuantity() + "x " + item.getName() + " " + item.getTotal());
                        itemsEmployeesBought.add(tempItem);
                        amtEmployeePaidForAllItems.add(item.getTotal());
                    }
                    else
                    {
                        int indexOfEmp = employeesWhoPaidForItems.indexOf(employeeCheckoutName);
                        itemsEmployeesBought.get(indexOfEmp).add(item.getQuantity() + "x " + item.getName() + " " + item.getTotal());
                        Double temp = item.getTotal() + amtEmployeePaidForAllItems.get(indexOfEmp);
                        amtEmployeePaidForAllItems.set(indexOfEmp, temp);
                    }
                    if (item.getName().toUpperCase().contentEquals("LUNCH"))
                    {
                        lunchTotalAmt += item.getTotal();
                        if (paymentType[0].contains("CASH"))
                        {
                            lunchTotalCash += item.getTotal();
                        }
                        else if (paymentType[0].contains("CREDIT"))
                        {
                            lunchTotalCredit += item.getTotal();
                        }
                        else if (paymentType[0].contains("DEBIT"))
                        {
                            lunchTotalDebit += item.getTotal();
                        }
                        else if (paymentType[0].contains("CHECK"))
                        {
                            lunchTotalCheck += item.getTotal();
                        }
                        else if (paymentType[0].contains("CHARGED TO"))
                        {
                            lunchChargedAmt += item.getTotal();

                        }
                    }
                }
            }//end for all items
            if (charged)
            {
                int index = accountNameCharged.indexOf(paymentType[0].substring(11));
                ArrayList<String> receipts = receiptNumbersChargedToAccount.get(index);
                boolean found = false;
                for (String rec : receipts)
                {
                    if (rec.contentEquals(receiptNumber))
                    {
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    receipts.add(receiptNumber);
                    //receiptNumbersChargedToAccount.add(receipts);
                }
            }
        }//end else not paycheckReceipt
    }//end update()

    public void paidOut(String description, double amount) {
        descriptionsPO.add(description);
        amountsPO.add(amount);
        totalCashAmt += totalCoinsAmt;
        totalCashAmt -= amount;

        String cash = Double.toString(totalCashAmt);
        boolean isNegative = false;
        if (totalCashAmt < 0)
        {
            isNegative = true;
        }
        totalCashAmt = Double.parseDouble(cash.substring(0, cash.indexOf('.')));
        totalCoinsAmt = Double.parseDouble(cash.substring(cash.indexOf('.')));
        totalCoinsAmt = round(totalCoinsAmt);
        totalCashAmt = round(totalCashAmt);
        if (isNegative)
        {
            totalCoinsAmt = round(totalCoinsAmt * -1);
        }
    }

    private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

    public double getTotalCashAmt() {
        return totalCashAmt;
    }

    public void printReport() {
        System.out.println("EMPLOYEE TRANSACTIONS:");
        int index = 0;
        for (String name : employeeNames)
        {
            System.out.println(name + " " + employeeTransactions.get(index));
            index++;
        }

        System.out.println("\nTotal Cash: " + totalCashAmt);
        System.out.println("Total Coins: " + totalCoinsAmt);
        System.out.println("Total Credit: " + totalCreditAmt);
        System.out.println("Total Debit: " + totalDebitAmt);
        System.out.println("Total Checks: " + totalChecksAmt + "\n");

        System.out.println("Total Tax Charged: " + totalTaxCharged + "\n");

        System.out.println("AR PAYMENT INFO");
        System.out.println("Total in AR Payments: " + totalARPayments);
        System.out.println("Total in DME Payments: " + totalDMEPayments);

        System.out.println("\nAR ACCOUNTS AND PAYMENTS");
        index = 0;
        for (String name : ARAccountName)
        {
            System.out.println(name + " " + amountPaidToARAccount.get(index));
            System.out.println("Receipts for Payments: ");
            for (String receiptNumber : receiptNumbersPaidToAccount.get(index))
            {
                System.out.println(receiptNumber);
            }

            index++;
        }

        System.out.println("\nDME ACCOUNTS AND PAYMENTS");
        index = 0;
        for (String name : DMEAccountName)
        {
            System.out.println(name + " " + amountPaidToDMEAccount.get(index));
            System.out.println("Receipts for " + name + " Payments: ");
            for (String receipt : receiptNumbersPaidToDMEAccount.get(index))
            {
                System.out.println(receipt);
            }
            index++;
        }

        System.out.println("Charges");

        index = 0;
        for (String s : accountNameCharged)
        {
            System.out.println("Account Name: " + s);
            System.out.println("Total Charged: " + amountChargedToAccount.get(index));
            ArrayList<String> temp = itemsChargedToAccount.get(index);
            for (String item : temp)
            {
                System.out.println(item);
            }

            System.out.println("\nReceipts for " + s + " Charges");
            for (String receipt : receiptNumbersChargedToAccount.get(index))
            {
                System.out.println(receipt);
            }
            System.out.println();
            index++;
        }

        System.out.println("\nPAID OUTS:");
        index = 0;
        for (String s : descriptionsPO)
        {
            System.out.println(s + "  " + amountsPO.get(index));
            index++;
        }

        System.out.println("\nTotal RX Copays: $" + totalRXCoppay);
        System.out.println("Total Paper Sales: $" + totalPaperSales);
        System.out.println("Total OTC Taxed: $" + otcTaxedTotal);
        System.out.println("Total OTC Non Taxed: $" + otcNonTaxedTotal);
        System.out.println("Total DME Taxed: $" + dmeWithTax);
        System.out.println("Total DME Non Taxed: $" + dmeWithoutTax);
        System.out.println("Total Charged to RX Accounts: $" + totalChargesRXAmt);

        System.out.print("\n EMPLOYEE TRANSACTION DATA:\n");
        System.out.print("Lunch Total Collected: $" + lunchTotalAmt + "\n");
        System.out.print("Lunch Check Total Collected: $" + lunchTotalCheck + "\n");
        System.out.print("Lunch Cash Total Collected: $" + lunchTotalCash + "\n");
        System.out.print("Lunch Credit Total Collected: $" + lunchTotalCredit + "\n");
        System.out.print("Total UPS Collected: " + totalUPSSales + "\n");
        System.out.print("Total Employee Checks Paid With: " + totalPayCheckPayments + "\n");
        System.out.print("Total Employee Lunches Charged: " + lunchChargedAmt + "\n\n");
        if (!masterRefundDescriptionAndAmt.isEmpty())
        {
            System.out.print("\nMaster Refunds: \n");
            for (String s : masterRefundDescriptionAndAmt)
            {
                System.out.print(s + "\n");
            }
        }
        index = 0;
        for (String s : employeesWhoPaidForItems)
        {
            System.out.print("Employee Name: " + s + "\n");
            System.out.print("Total Paid: " + amtEmployeePaidForAllItems.get(index) + "\n");
            ArrayList<String> temp = itemsEmployeesBought.get(index);
            for (String item : temp)
            {
                System.out.print(item + "\n");
            }
            System.out.print("\n");
            index++;
        }

        if (!lunchErrorLog.isEmpty())
        {
            for (String error : lunchErrorLog)
            {
                System.out.println(error);
            }
            System.out.println();
        }
    }

    public void generateReport(String date) {
        try
        {
            if (!accountNameCharged.isEmpty() || !ARAccountName.isEmpty())
            {
                fw = new FileWriter(HOLLIEFILENAME + date + ".txt");//Hollie's Accounts (RX)
                bw = new BufferedWriter(fw);
                bw.write("Charges: \n");

                int index = 0;
                for (String s : accountNameCharged)
                {
                    bw.write("******************************\n");
                    bw.write("Account Name: " + s + "\n");
                    bw.write("Total Charged: " + amountChargedToAccount.get(index) + "\n");
                    ArrayList<String> temp = itemsChargedToAccount.get(index);
                    for (String item : temp)
                    {
                        bw.write(item + "\n");
                    }

                    bw.write("\nReceipts for " + s + " Charges\n");
                    for (String receipt : receiptNumbersChargedToAccount.get(index))
                    {
                        bw.write(receipt + "\n");
                    }
                    bw.write("******************************\n\n");
                    index++;
                }

                if (!ARAccountName.isEmpty())
                {
                    bw.write("\n Account Payments: \n");
                    index = 0;
                    for (String name : ARAccountName)
                    {
                        bw.write("******************************\n");
                        bw.write(name + " " + amountPaidToARAccount.get(index) + "\n\n");
                        bw.write("Receipts for " + name + " Payments:\n");
                        for (String receiptNumber : receiptNumbersPaidToAccount.get(index))
                        {
                            bw.write(receiptNumber + "\n");
                        }
                        bw.write("******************************\n\n");
                        index++;
                    }
                }
            }
        }
        catch (IOException e)
        {

            e.printStackTrace();

        }
        finally
        {

            try
            {

                if (bw != null)
                {
                    bw.close();
                }

                if (fw != null)
                {
                    fw.close();
                }

            }
            catch (IOException ex)
            {

                ex.printStackTrace();

            }

        }

        try
        {
            if (!DMEAccountName.isEmpty())
            {
                fw = new FileWriter(DEBBIEFILENAME + date + ".txt");//Debbie's Accounts (RX)
                bw = new BufferedWriter(fw);
                int index = 0;
                bw.write("Account Payments: \n");
                
                for (String name : DMEAccountName)
                {
                    bw.write("******************************\n");
                    bw.write(name + " " + amountPaidToDMEAccount.get(index) + "\n");
                    bw.write("Receipts for " + name + " Payments: \n");
                    for (String receipt : receiptNumbersPaidToDMEAccount.get(index))
                    {
                        bw.write(receipt +"\n");
                    }
                    bw.write("******************************\n\n");
                    index++;
                }
            }

        }
        catch (IOException e)
        {

            e.printStackTrace();

        }
        finally
        {

            try
            {

                if (bw != null)
                {
                    bw.close();
                }

                if (fw != null)
                {
                    fw.close();
                }

            }
            catch (IOException ex)
            {

                ex.printStackTrace();

            }

        }

        try
        {
            fw = new FileWriter(REPORTFILENAME + date + ".txt");//Report for Hollie
            bw = new BufferedWriter(fw);
            bw.write(totalCoinsAmt + "\n");
            bw.write(totalCashAmt + "\n");
            bw.write(totalChecksAmt + "\n");
            bw.write(totalCreditAmt + "\n");
            bw.write(totalDebitAmt + "\n");

            bw.write(totalRefundedCash + "\n");
            bw.write(totalRefundedCredit + "\n");

            bw.write(totalTaxCharged + "\n");

            bw.write(totalChargesRXAmt + "\n");

            double total = 0;
            for (double amt : amountsPO)
            {
                total += amt;
            }
            total = round(total);
            bw.write(total + "\n");//TOTAL PAID OUTS!

            bw.write(totalARPayments + "\n");
            bw.write(totalDMEPayments + "\n");

            bw.write(otcTaxedTotal + "\n");
            bw.write(otcNonTaxedTotal + "\n");
            bw.write(dmeWithTax + "\n");
            bw.write(dmeWithoutTax + "\n");
            bw.write(totalRXCoppay + "\n");
            bw.write(totalPaperSales + "\n");
            bw.write(totalAmericanGreetings + "\n");
            bw.write(totalUPSSales + "\n");
            bw.write(totalPayCheckPayments + "\n");
            bw.write(lunchChargedAmt + "\n");

            if (!masterRefundDescriptionAndAmt.isEmpty())
            {
                bw.write("\nMaster Refunds: \n");
                for (String s : masterRefundDescriptionAndAmt)
                {
                    bw.write(s + "\n");
                }
            }

            bw.write("\nEMPLOYEE TRANSACTIONS:\n");
            int index = 0;
            for (String name : employeeNames)
            {
                bw.write(name + "  " + employeeTransactions.get(index) + "\n");
                index++;
            }

            bw.write("\nPAID OUTS:\n");
            index = 0;
            for (String s : descriptionsPO)
            {
                bw.write(s + "  " + amountsPO.get(index) + "\n");
                index++;
            }

            bw.write("\n EMPLOYEE TRANSACTION DATA:\n");
            bw.write("Lunch Total Collected: $" + lunchTotalAmt + "\n");
            bw.write("Lunch Check Total Collected: $" + lunchTotalCheck + "\n");
            bw.write("Lunch Cash Total Collected: $" + lunchTotalCash + "\n");
            bw.write("Lunch Credit Total Collected: $" + lunchTotalCredit + "\n\n");

            index = 0;
            for (String s : employeesWhoPaidForItems)
            {
                bw.write("Employee Name: " + s + "\n");
                bw.write("Total Paid: " + amtEmployeePaidForAllItems.get(index) + "\n");
                ArrayList<String> temp = itemsEmployeesBought.get(index);
                for (String item : temp)
                {
                    bw.write(item + "\n");
                }
                bw.write("\n");
                index++;
            }

            if (!lunchErrorLog.isEmpty())
            {
                for (String error : lunchErrorLog)
                {
                    bw.write(error + "\n");
                }
                bw.write("\n");
            }

        }
        catch (IOException e)
        {

            e.printStackTrace();

        }
        finally
        {

            try
            {

                if (bw != null)
                {
                    bw.close();
                }

                if (fw != null)
                {
                    fw.close();
                }

            }
            catch (IOException ex)
            {

                ex.printStackTrace();

            }

        }

    }
}//end DrawerReportClass
