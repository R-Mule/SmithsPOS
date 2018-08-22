
package database_console;

import java.util.ArrayList;

/**
 *
 * @author A.Smith
 */
public class RefundCart extends Cart {

   protected String receiptNum;
    private ArrayList<RefundItem> refundItems = new ArrayList<RefundItem>();
   // private boolean requiresRepaint=false;

    RefundCart() {
    super();
        updateTotal();
    }//end Cart ctor

    public void setReceiptNum(String receiptNum){
        this.receiptNum=receiptNum;
    }
    public boolean isEmpty() {
        return refundItems.isEmpty();
    }

    public void loadRefundCart(String receiptNum, Database myDB) {
        ArrayList<RefundItem> tempItems = myDB.loadReceipt(receiptNum);
        ArrayList<RefundItem> itemsToAdd = new ArrayList<RefundItem>();
        if (!refundItems.isEmpty()) {
            for (RefundItem item : tempItems) {
                boolean found = false;
                for (RefundItem cartItem : refundItems) {
                    if (cartItem.isRX()) {
                        if(cartItem.getID().contentEquals(item.getID())&&cartItem.getInsurance().contentEquals(item.getInsurance())){
                            found=true;
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
            for (RefundItem itemToAdd : itemsToAdd) {
                refundItems.add(itemToAdd);
            }
        } else {//cart is empty, so we must add the entire list
            for (RefundItem itemToAdd : tempItems) {
                refundItems.add(itemToAdd);
            }
        }
        tempItems.clear();//get rid of it.
        itemsToAdd.clear();//get rid of this too.
        updateTotal();//everything is loaded, lets update!
    }//end loadCart



    public ArrayList<RefundItem> getRefundItems() {
        return refundItems;
    }//end getCart()

    public void voidCart() {
        refundItems.clear();
        updateTotal();
    }

    
    public void addItem(RefundItem itemToAdd) {
        boolean tooAdd = true;
        System.out.println(itemToAdd.getID());
        if (itemToAdd.isRX()) {
            for (RefundItem item : refundItems) {
                if (item.getRxNumber() == itemToAdd.getRxNumber() && item.getInsurance().contentEquals(itemToAdd.getInsurance())) {
                    int i = item.getQuantity();
                    item.setQuantity(i + 1);
                    // System.out.println("HERE1");
                    tooAdd = false;
                }
            }
            if (tooAdd) {
                refundItems.add(itemToAdd);
                itemToAdd.setQuantity(1);
                //  System.out.println("HERE2");
            }
        } else {
            for (Item item : refundItems) {
                if (item.getID().contentEquals(itemToAdd.getID())) {
                    int i = item.getQuantity();
                    item.setQuantity(i + 1);
                    // System.out.println("HERE1");
                    tooAdd = false;
                }
            }
            if (tooAdd) {
                refundItems.add(itemToAdd);
                itemToAdd.setQuantity(1);
                //  System.out.println("HERE2");
            }

        }
        if (refundItems.isEmpty()) {//no matches because cart must be empty
            refundItems.add(itemToAdd);
            itemToAdd.setQuantity(1);
            //  System.out.println("HERE3");
        }
        if(!tooAdd){
            itemToAdd=null;
        }
        
        updateTotal();

    }//end addItem

    //public void removeItem(Item itemToRemove){
    //    items.remove(itemToRemove);
    //    updateTotal();
    // }//end removeItem
        @Override
    public double getTotalPrice() {
        return totalPriceAfterTax;
    }

    @Override
    public double getTax() {
        return amtOfTaxCharged;
    }

    @Override
    public double getSubTotal() {
        return totalPriceBeforeTax;
    }

    @Override
    public void updateTotal() {
        if(refundItems!=null&&!refundItems.isEmpty()){
        double taxableAmt = 0;
        double nonTaxableAmt = 0;
        totalPriceAfterTax = 0;
        amtOfTaxCharged = 0;
        for (RefundItem temp : refundItems) {
            if(!temp.isPreCharged()&&!temp.hasBeenRefunded()&&temp.getCategory()!=853&&temp.getCategory()!=854&&temp.refundAllActive()){
            if (temp.isTaxable()&&!temp.hasTaxBeenRefunded){
                taxableAmt += temp.getPrice() * temp.quantityBeingRefunded-temp.getDiscountAmount();
            } else {
                nonTaxableAmt += temp.getPrice() * temp.quantityBeingRefunded-temp.getDiscountAmount();
            }//end else
            
            }else if(temp.refundTaxOnly()){
                amtOfTaxCharged+=temp.getTaxTotal();
            }
        }//end for
        taxableAmt = round(taxableAmt);
        nonTaxableAmt = round(nonTaxableAmt);
        amtOfTaxCharged+= taxableAmt * taxRate;
        amtOfTaxCharged = round(amtOfTaxCharged);
        totalPriceBeforeTax = taxableAmt + nonTaxableAmt;
        totalPriceBeforeTax = round(totalPriceBeforeTax);
        totalPriceAfterTax += amtOfTaxCharged + totalPriceBeforeTax;
        totalPriceAfterTax = round(totalPriceAfterTax);
        }else{
            totalPriceAfterTax=0;
            totalPriceBeforeTax=0;
            amtOfTaxCharged=0;
        }
        // System.out.println("Amount not taxable: $"+nonTaxableAmt+"\nAmount taxable: $"+taxableAmt+"\nTax Charged: $"+amtOfTaxCharged+"\nTotal: $"+totalPriceAfterTax);
    }//end updateTotal

    @Override
    public int getTotalNumRX(){
        int i=0;
        for(RefundItem item :refundItems){
            if(item.isRX()){
                i++;
            }
        }
        return i;
    }
    private double round(double num) {//rounds to 2 decimal places.
        num = Math.round(num * 100.0) / 100.0;
        return num;
    }//end round

    @Override
    public void setTaxableOnItem(String itemNumber, boolean isTaxable) {
        for (RefundItem item : refundItems) {
            if (item.getUPC().contentEquals(itemNumber)) {
                item.setTaxable(isTaxable);
                //System.out.println("HERE");
            }
        }
        updateTotal();
    }
   @Override
    public void setPrecharged(String itemName,boolean precharged){
                for (RefundItem item : refundItems) {
            if (item.getName().contentEquals(itemName)) {
                item.setIsPreCharged(precharged);
                //System.out.println("HERE");
            }
        }
        updateTotal();
    }
    
    public boolean containsItemByID(String id){
        for(RefundItem item :refundItems){
            if(item.getID().contentEquals(id)){
                return true;
            }
        }
        return false;
    }
    
    public void increaseQtyByID(String id, int qty2Add){
        for(RefundItem item: refundItems){
            if(item.getID().contentEquals(id)){
                System.out.println("INCREASING: "+item.getID()+"with "+item.quantity+" BY "+qty2Add);
                item.quantity+=qty2Add;
                
            }
        }
    }
    
    
   @Override
    public double getDiscountTotal(){
        double total=0;
        for(RefundItem item:refundItems){
            total+=item.getDiscountAmount();
        }
        return total;
    }

   @Override
    void setMassDiscount(double discPer) {
        for(RefundItem item: refundItems){
            if(!item.isRX()&&item.getCategory()!=853&&item.getCategory()!=854)
           item.setDiscountPercentage(discPer);
        }
        updateTotal();
    }
    
    void setItemDiscount(String itemUPC,double percent){
        for(RefundItem item:refundItems){
            if(item.getUPC().contentEquals(itemUPC)){
                item.setDiscountPercentage(percent);
            }
        }
    }
    
       public void setRequiresRepaint(boolean repaint){
        requiresRepaint = repaint;
    }
    
    @Override
    public boolean getRequiresRepaint(){
        return requiresRepaint;
    }
}
