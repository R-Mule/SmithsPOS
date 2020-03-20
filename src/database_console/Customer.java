package database_console;

import java.time.LocalDateTime;
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
    protected Boolean dobValid;
    protected LocalDateTime dobDate;

    public Customer(String firstName, String lastName, String dob, String cid, String chargeAccountName, String zipCode, String address, String city, String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.cid = cid;
        this.chargeAccountName = chargeAccountName;
        this.zipCode = zipCode;
        this.address = address;
        this.city = city;
        this.state = state;
        validateDOB();
    }

    private void validateDOB()
    {
        if(dob.matches("^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$"))
        {
            int month = Integer.parseInt(dob.substring(0,2));
            int day = Integer.parseInt(dob.substring(3,5));
            int year = Integer.parseInt(dob.substring(6,10));
            dobDate = LocalDateTime.of(year, month, day, 1,1);
            dobValid = true;
        }
        else
        {
            dobValid = false;
        }
    }
    
    public boolean doesCustomerMatchIdentically(Customer customer) {
        if (!this.firstName.contentEquals(customer.firstName))
        {
            return false;
        }
        if (!this.lastName.contentEquals(customer.lastName))
        {
            return false;
        }
        if (!this.dob.contentEquals(customer.dob))
        {
            return false;
        }
        if (!this.cid.contentEquals(customer.cid))
        {
            return false;
        }
        if (!this.chargeAccountName.contentEquals(customer.chargeAccountName))
        {
            return false;
        }
        if (!this.zipCode.contentEquals(customer.zipCode))
        {
            return false;
        }
        if (!this.address.contentEquals(customer.address))
        {
            return false;
        }
        if (!this.city.contentEquals(customer.city))
        {
            return false;
        }
        if (!this.state.contentEquals(customer.state))
        {
            return false;
        }
        return true;
    }

}
