package database_console;

/**

 @author R-Mule
 */
public class Customer {

    protected String firstName;
    protected String lastName;
    protected String dob;
    protected String cid;
    protected String chargeAccountName;
    protected String zipCode;
    protected String address;
    protected String city;
    protected String state;

    public Customer(String firstName, String lastName, String dob, String cid, String chargeAccountName, String zipCode, String address, String city, String state) 
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.cid = cid;
        this.chargeAccountName = chargeAccountName;
        this.zipCode = zipCode;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    
}
