
package database_console;

/**

 @author R-Mule
 */
public class DMERentalItem {
    protected double customerRate;
    protected double nonCustomerRate;
    protected String itemName;
    protected String minCustomerUnit;
    protected String minNonCustomerUnit;
    
    public DMERentalItem(String equipName, double customerRate, double nonCustomerRate, String minCustomerUnit, String minNonCustomerUnit){
        this.customerRate = customerRate;
        this.nonCustomerRate = nonCustomerRate;
        this.itemName = equipName;
        this.minCustomerUnit = minCustomerUnit;
        this.minNonCustomerUnit = minNonCustomerUnit;
    }
}
