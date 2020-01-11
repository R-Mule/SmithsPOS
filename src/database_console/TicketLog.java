
package database_console;

import java.time.LocalDateTime;

/**

 @author R-Mule
 */
public class TicketLog {
    protected String ticketOwnersName;
    protected String ticketOwnersAccount;
    protected String modifiedByName;
    protected String modifiedByAccount;
    protected String itemModified;
    protected String modificationType;
    protected LocalDateTime modificationTime;
    protected String registerUsed;
    
    TicketLog(String ticketOwnersName, String ticketOwnersAccount, String modifiedByName, String modifiedByAccount, String itemModified, String modificationType, LocalDateTime modificationTime, String registerUsed){
        this.ticketOwnersName = ticketOwnersName;
        this.ticketOwnersAccount = ticketOwnersAccount;
        this.modifiedByName = modifiedByName;
        this.modifiedByAccount = modifiedByAccount;
        this.itemModified = itemModified;
        this.modificationType = modificationType;
        this.modificationTime = modificationTime;
        this.registerUsed = registerUsed;
    }
}
