package database_console;

import java.time.LocalDate;

/**

 @author R-Mule
 */
public class DispensationItem implements Comparable {

    protected int rxNumber;
    protected LocalDate fillDate;
    protected LocalDate pickupDate;
    protected String drugName;
    protected double qtyDispensed;
    protected String patientName;
    protected boolean wasPickedUp = false;

    public DispensationItem() {

    }

    public int compareTo(Object o) {
        DispensationItem other = (DispensationItem) o;

        if (fillDate.isBefore(other.fillDate))
        {
            return -1;
        }
        else if (fillDate.isAfter(other.fillDate))
        {
            return 1;
        }

        //Fill dates match, compare patient names
        int compare = patientName.compareTo(other.patientName);
        if (compare < 0)
        {
            return -1; //our patient name is smaller
        }
        else if (compare > 0)
        {
            return 1; //our patient name is larger
        }
        
        //Patient Names match, compare pickup dates.
        
        if(pickupDate != null && other.pickupDate == null)
        {
            return -1;
        }
        else if(pickupDate == null && other.pickupDate != null)
        {
            return 1;
        }
        else if( pickupDate == null && other.pickupDate == null)
        {
            return 0;
        }
        else if (pickupDate.isBefore(other.pickupDate))
        {
            return -1;
        }
        else if (pickupDate.isAfter(other.pickupDate))
        {
            return 1;
        }
        
        return 0;//The two objects are equal
    }

}
