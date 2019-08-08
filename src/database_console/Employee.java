
package database_console;

/**

 @author R-Mule
 */
public class Employee {
    protected String rfid;
    protected String name;
    protected int pid;
    protected int passcode;
    protected int permissionLevel;
    protected int wins;//March Maddness
    protected int losses;//March Maddness
    
    public Employee(int pid,String name, String rfid, int passcode, int permissionLevel, int wins, int losses){
        this.rfid = rfid;
        this.pid = pid;
        this.passcode = passcode;
        this.name = name;
        this.permissionLevel = permissionLevel;
        this.wins = wins;
        this.losses = losses;
    }
    
}
