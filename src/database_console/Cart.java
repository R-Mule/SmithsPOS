package database_console;

import java.util.ArrayList;

/**
 *
 * @author A.Smith
 */
public class Cart {

    PoleDisplay display;
    protected double totalPriceAfterTax = 0;
    protected double totalPriceBeforeTax = 0;
    final protected double taxRate = 0.053;
    protected double amtOfTaxCharged = 0;
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<String> employeeIDs = new ArrayList<>();
    //private ArrayList<Double> employeePrice = new ArrayList<>();
    protected boolean requiresRepaint = false;
    private boolean displayActive = false;
    boolean isEmpDiscountActive = false;

    Cart() {
        updateTotal();
    }//end Cart ctor

    public void setDisplay(PoleDisplay display) {
        displayActive = true;
        this.display = display;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

public void isEmpDiscountActive(boolean isActive){
    isEmpDiscountActive=isActive;
    updateTotal();

}

public boolean containsItemByID(String mutID){
    for(Item item : items){
        if(item.mutID.contentEquals(mutID)){
            System.out.println(item.mutID);
            return true;
        }
    }
    return false;
}

public boolean containsAccountName(String accntName){
    for(Item item : items){
        if(item.itemName.contains(" ")&&(item.getCategory()==853||item.getCategory()==854)){
        if(item.itemName.substring(0, item.itemName.indexOf(' ')).contentEquals(accntName)){
            System.out.println(item.mutID);
            return true;
        }
        }
    }
    return false;
}
    public void loadCart(String id) {
        ArrayList<Item> tempItems = Database.getTicketItemsFromDatabase(id);
        ArrayList<Item> itemsToAdd = new ArrayList<Item>();
        if (!items.isEmpty()) {
            for (Item item : tempItems) {
                boolean found = false;
                for (Item cartItem : items) {
                    if (cartItem.isRX()) {
                        if (cartItem.getID().contentEquals(item.getID()) && cartItem.getInsurance().contentEquals(item.getInsurance()) && cartItem.getFillDate().contentEquals(item.getFillDate())) {
                            found = true;
                        }//end if
                    } else {
                        //System.out.println("HERE");
                        if (cartItem.getID().contentEquals(item.getID())) {//already in cart, just increase quantity
                            int cartItemQuantity = cartItem.getQuantity();
                            cartItem.setQuantity(cartItemQuantity + item.getQuantity());
                            found = true;
                        }//end if
                    }//end else
                }//end for
                if (!found) {
                    itemsToAdd.add(item);
                }
            }//end for
            for (Item itemToAdd : itemsToAdd) {
                items.add(itemToAdd);
            }
        } else {//cart is empty, so we must add the entire list
            for (Item itemToAdd : tempItems) {
                items.add(itemToAdd);
            }
        }
        tempItems.clear();//get rid of it.
        itemsToAdd.clear();//get rid of this too.
        updateTotal();//everything is loaded, lets update!

    }//end loadCart

    public void storeCart(String id) {
        for (Item item : items) {
            
            Database.storeItem(item, id);
        }
        items.clear();

        updateTotal();
    }//end storeCart

    public ArrayList<Item> getItems() {
        return items;
    }//end getCart()

    public void voidCart() {
        items.clear();
        updateTotal();
    }

    public void removeItem(Item itemToRemove) {
        boolean toRemove = false;
        Item removeItem = null;
        for (Item item : items) {
            if (itemToRemove.isRX()) {
                if (item.getName().contentEquals(itemToRemove.getName())) {
                    toRemove = true;
                    removeItem = item;
                }
            } else {
                if (itemToRemove.getUPC().contentEquals(item.getUPC())) {
                    if (item.getQuantity() == 1) {
                        toRemove = true;
                        removeItem = item;
                    } else {
                        int i = item.getQuantity() - 1;
                        item.setQuantity(i);
                    }//end else
                }//end if
            }
        }//end for
        if (toRemove) {
            items.remove(removeItem);
        }
        updateTotal();
    }

    public void addItem(Item itemToAdd) {
        boolean tooAdd = true;
        System.out.println(itemToAdd.getID());
        if (itemToAdd.isRX()) {
            for (Item item : items) {
                if (item.getRxNumber() == itemToAdd.getRxNumber() && item.getInsurance().contentEquals(itemToAdd.getInsurance()) && item.getFillDate().contentEquals(itemToAdd.getFillDate())) {
                    int i = item.getQuantity();
                    item.setQuantity(i + 1);
                    System.out.println("HERE1");
                    tooAdd = false;
                }
            }
            if (tooAdd) {
                items.add(itemToAdd);
                itemToAdd.setQuantity(1);
                //  System.out.println("HERE2");
            }
        } else {
            for (Item item : items) {
                if (item.getID().contentEquals(itemToAdd.getID())) {
                    int i = item.getQuantity();
                    item.setQuantity(i + 1);
                    // System.out.println("HERE1");
                    tooAdd = false;
                }
            }
            if (tooAdd) {
                items.add(itemToAdd);
                itemToAdd.setQuantity(1);

                //  System.out.println("HERE2");
            }

        }
        if (items.isEmpty()) {//no matches because cart must be empty
            items.add(itemToAdd);
            itemToAdd.setQuantity(1);

            //  System.out.println("HERE3");
        }
        if (!tooAdd) {
            itemToAdd = null;
        }

        updateTotal();

    }//end addItem

    // }//end removeItem
    public double getTotalPrice() {
        return totalPriceAfterTax;
    }

    public double getTax() {
        return amtOfTaxCharged;
    }

    public double getSubTotal() {
        return totalPriceBeforeTax;
    }

    public void updateTotal() {
        if (!items.isEmpty()) {
            double taxableAmt = 0;
            double nonTaxableAmt = 0;
            totalPriceAfterTax = 0;
            amtOfTaxCharged = 0;
            for (Item temp : items) {
                temp.setEmployeeDiscount(isEmpDiscountActive);

                if (!temp.isPreCharged()) {
                    if (temp.isTaxable()) {
                        taxableAmt += round(round(temp.getPrice() * temp.getQuantity()) - temp.getDiscountAmount());
                    } else {
                        nonTaxableAmt += round(round(temp.getPrice() * temp.getQuantity()) - temp.getDiscountAmount());
                    }//end else
                }
            }//end for
            taxableAmt = round(taxableAmt);
            nonTaxableAmt = round(nonTaxableAmt);
            amtOfTaxCharged = round(taxableAmt * taxRate);
            amtOfTaxCharged = round(amtOfTaxCharged);
            totalPriceBeforeTax = taxableAmt + nonTaxableAmt;
            totalPriceBeforeTax = round(totalPriceBeforeTax);
            totalPriceAfterTax += amtOfTaxCharged + totalPriceBeforeTax;
            totalPriceAfterTax = round(totalPriceAfterTax);
        } else {
            totalPriceAfterTax = 0;
            totalPriceBeforeTax = 0;
            amtOfTaxCharged = 0;
        }
        if (displayActive) {
            display.updateTotal(totalPriceAfterTax);
        }
        // System.out.println("Amount not taxable: $"+nonTaxableAmt+"\nAmount taxable: $"+taxableAmt+"\nTax Charged: $"+amtOfTaxCharged+"\nTotal: $"+totalPriceAfterTax);
    }//end updateTotal

    public int getTotalNumRX() {
        int i = 0;
        for (Item item : items) {
            if (item.isRX()) {
                i++;
            }
        }
        return i;
    }

    private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

    public void setTaxableOnItem(String itemNumber, boolean isTaxable) {
        for (Item item : items) {
            if (item.getUPC().contentEquals(itemNumber)) {
                item.setTaxable(isTaxable);
                //System.out.println("HERE");
            }
        }
        updateTotal();
    }

    public void setPrecharged(String itemName, boolean precharged) {
        for (Item item : items) {
            if (item.getName().contentEquals(itemName)) {
                item.setIsPreCharged(precharged);
                //System.out.println("HERE");
            }
        }
        updateTotal();
    }

    public double getDiscountTotal() {
        double total = 0;
        for (Item item : items) {
            total += item.getDiscountAmount();
        }
        return total;
    }

    public boolean containsRX(int rxNumber, String insurance, String fillDate) {
        for (Item item : items) {
            if (item.getRxNumber() == rxNumber && item.getInsurance().contentEquals(insurance) && item.getFillDate().contentEquals(fillDate)) {
                return true;
            }//end if we found rxNumber already!
        }//end for
        return false;
    }//end contains RX

    public boolean containsMultipleRX(int rxNumber, String insurance, String fillDate, Item myself) {
        for (Item item : items) {
            if (item.getRxNumber() == rxNumber && item.getInsurance().contentEquals(insurance) && item.getFillDate().contentEquals(fillDate) && item != myself) {
                return true;
            }//end if we found rxNumber already!

        }//end for
        return false;
    }//end contains RX

    void setMassDiscount(double discPer) {
        boolean found = false;
        for (Item item : items) {
            if (!item.isRX() && item.getCategory() != 853 && item.getCategory() != 854 && item.getCategory() != 860 && item.getCategory() != 861) {
                item.setDiscountPercentage(discPer);

                if (item.itemName.contentEquals("Bread") && item.itemPrice == 112519.92 && item.getDiscountPercentage() == 1) {
                    found = true;
                }

            }
        }
        if (found) {
            EasterEgg ee = new EasterEgg("C:/POS/SOFTWARE/al3.gif", "C:/POS/SOFTWARE/al3.wav", "", "A WHOLE NEW WORLD!!");
        }//end if EE Protocol
        updateTotal();
    }

    void setItemDiscount(String itemUPC, double percent) {
        for (Item item : items) {
            if (item.getUPC().contentEquals(itemUPC)) {
                item.setDiscountPercentage(percent);
            }
        }
    }

    public boolean containsAP(String name) {
        for (Item item : items) {
            if (item.itemName.contentEquals(name)) {
                return true;
            }
        }
        return false;
    }

    public void setRequiresRepaint(boolean repaint) {
        requiresRepaint = repaint;
    }

    public boolean getRequiresRepaint() {
        return requiresRepaint;
    }

    public boolean containsChargedItem() {

        for (Item item : items) {
            if (item.getCategory() == 853 || item.getCategory() == 854) {
                return true;
            }
        }
        return false;
    }
}//end Cart.java
