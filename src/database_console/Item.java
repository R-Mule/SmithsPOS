package database_console;

/**
 *
 * @author A.Smith
 */
public class Item {

    protected String itemName = "";
    protected double itemPrice = -1;
    protected String mutID = "";
    protected double itemCost = 0;
    protected String itemUPC = "";
    protected int category = 0;
    protected boolean isTaxable;
    protected int rxNumber = 0;
    protected String insurance = "";
    protected String fillDate = "0";
    protected double taxAmt;
    final protected double taxRate = 0.053;
    final protected Database myDB;
    protected int quantity = 0;
    protected boolean isRX = false;
    protected double percentageDisc = 0.0;
    protected boolean isPreCharged = false;
    protected boolean hasBeenRefunded=false;
    protected boolean hasTaxBeenRefunded=false;

    Item(Database myDB, String UPCorID) {
        this.myDB = myDB;
        if (UPCorID.length() == 6) {
            mutID = UPCorID;
        } else {
            itemUPC = UPCorID;
        }
        setup();
    }

//THIS CONSTRUCTOR IS TO BE USED ONLY BY RX's
    Item(Database myDB, int rxNumber, String fillDate, String insurance, double copay, boolean isPreCharged) {
        this.myDB = myDB;
        this.rxNumber = rxNumber;
        this.fillDate = fillDate;
        this.insurance = insurance;
        this.itemPrice = copay;
        isTaxable = false;
        this.itemCost = copay;
        this.itemName = rxNumber + " " + insurance + " " + fillDate;
        isRX = true;
        this.mutID = "X"+Integer.toString(rxNumber).substring(2, 7);
        this.quantity = 1;
        this.category = 851;//USED FOR RX's
        this.itemUPC = "X"+Integer.toString(rxNumber).substring(0, 5)+fillDate;
        this.isPreCharged = isPreCharged;
        hasBeenRefunded=false;
        hasTaxBeenRefunded=false;

    }

    Item(Database myDB, String mutID, String upc, String name, double price, double cost, boolean taxable, int category, int rxNumber, String insurance, String filldate, int quantity, boolean isRX, double percentageDisc, boolean isPreCharged) {
        this.myDB = myDB;
        this.quantity = quantity;
        this.itemName = name;
        this.itemPrice = price;
        this.mutID = mutID;
        this.itemCost = cost;
        this.itemUPC = upc;
        this.category = category;
        this.isTaxable = taxable;
        this.rxNumber = rxNumber;
        this.insurance = insurance;
        this.fillDate = filldate;
        this.isRX = isRX;
        this.percentageDisc = percentageDisc;
        this.isPreCharged = isPreCharged;
        hasBeenRefunded=false;
        hasTaxBeenRefunded=false;

    }

    public boolean hasBeenRefunded(){
        return hasBeenRefunded;
    }
    public boolean hasTaxBeenRefunded(){
        return hasTaxBeenRefunded;
    }
    public void setHasBeenRefunded(boolean hasBeenRefunded){
        this.hasBeenRefunded=hasBeenRefunded;
    }
    public void setHasTaxBeenRefunded(boolean hasTaxBeenRefunded){
        this.hasTaxBeenRefunded=hasTaxBeenRefunded;
    }
    public boolean isPreCharged() {
        return isPreCharged;
    }

    public void setIsPreCharged(boolean isPreCharged) {
        this.isPreCharged = isPreCharged;
    }

    private void setup() {
        if(!mutID.isEmpty()){
        myDB.checkDatabaseForItemByID(this);
        }else{
            myDB.checkDatabaseForItemByUPC(this);
        }
    }

    public boolean isRX() {
        return isRX;
    }

    public int getCategory() {
        return category;
    }

    public String getFillDate() {
        return fillDate;
    }

    public int getRxNumber() {
        return rxNumber;
    }

    public String getInsurance() {
        return insurance;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        double totalOnItem = 0;
        double itemP;
        itemP = itemPrice * quantity - itemPrice * quantity * percentageDisc;
        if (isTaxable) {
            totalOnItem = round(itemP + itemP * taxRate);
        } else {
            totalOnItem = round(itemP);
        }
        return totalOnItem;
        //return round(itemPrice);
    }//end getTotal()

    double getPriceOfItemsBeforeTax() {
        double total = round(itemPrice * quantity-itemPrice * quantity * percentageDisc);
        return total;
    }//end priceOfItemsBeforeTax

       double getPriceOfItemBeforeTax() {
        double total = round(itemPrice -itemPrice  * percentageDisc);
        return total;
    }//end priceOfItemsBeforeTax
       
    double getTaxTotal() {
        double itemP = itemPrice * quantity - itemPrice * quantity * percentageDisc;
        if (isTaxable) {
            double taxTotal = round(itemP * taxRate);
            return taxTotal;
            
        } else {
            return 0.00;
        }
    }//end getTaxTotal

    double getTaxAmtPerItem() {
        if (isTaxable) {
            return round(taxRate * itemPrice);
        }
        return 0;
    }

    double getPrice() {//returns price
        return round(itemPrice);
    }
    public void setPrice(double price){
        itemPrice = round(price);
    }

    String getName() {//returns name
        return itemName;
    }

    double getCost() {//returns cost
        return round(itemCost);
    }

    String getUPC() {//returns cost
        return itemUPC;
    }

    String getID() {//returns cost
        return mutID;
    }

    boolean isTaxable() {//is the item taxable? true is yes.
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }

 

    public void setDiscountPercentage(double percentDisc) {
        this.percentageDisc = percentDisc;
    }//end setDiscountPercentage

    public double getDiscountPercentage() {
        return percentageDisc;
    }

    public Double getDiscountAmount() {
        double discountAmt = round(quantity * itemPrice * percentageDisc);
        return discountAmt;
    }

    protected double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

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

}
