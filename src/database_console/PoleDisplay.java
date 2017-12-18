
package database_console;

/**
 *
 * @author A.Smith
 */
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.OutputStream;

import gnu.io.*;                                      //import from RXTXcomm.jar
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PoleDisplay {

      Enumeration       portList;
     CommPortIdentifier portId;
     SerialPort	      serialPort;
     OutputStream       outputStream;
     boolean	      outputBufferEmptyFlag = false;
    ConfigFileReader reader;
    
    @SuppressWarnings("SleepWhileInLoop")
 public void StartDisplay() {
    boolean portFound = false;
    String  defaultPort = reader.getDisplayComPort();
    portList = CommPortIdentifier.getPortIdentifiers();
 
    while (portList.hasMoreElements()) {
	    portId = (CommPortIdentifier) portList.nextElement();
 
	    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
 
		 if (portId.getName().equals(defaultPort)) {
		   // System.out.println("Found port " + defaultPort);
                    portFound = true;
 
		    try {
                         
			serialPort = (SerialPort) portId.open("SimpleWrite", 2000);
                        
		    } catch (PortInUseException e) {
			//System.out.println("Port is offline now.");
                        printError(e);
                        continue;
		    } 
 
		    try {
			outputStream = serialPort.getOutputStream();
                     
		    } catch (IOException e) {
                        printError(e);
                    }
 
		    try {
			serialPort.setSerialPortParams(9600, 
						       SerialPort.DATABITS_8, 
						       SerialPort.STOPBITS_1, 
						       SerialPort.PARITY_NONE);
                      //  System.out.println("Display is online now");
		    } catch (UnsupportedCommOperationException e) {
                          printError(e);
                    }
	
 
		    try {
		    	serialPort.notifyOnOutputEmpty(true);
		    } catch (Exception e) {
			//System.out.println("Error setting event notification");
			//System.out.println(e.toString());
                        printError(e);
			System.exit(-1);
		    }
 
                    
		    try {
		       Thread.sleep(2000);                          // Be sure data is xferred before closing
		    } catch (Exception e) {printError(e);}
		    
		} 
	    } 
	} 
 
	if (!portFound) {
	   // System.out.println("port " + defaultPort + " not found.");
            //System.out.println(reader.getDisplayComPort());
	} 
}   
public void ClearDisplay(){
        try{
    //outputStream.write(ESCPOS.SELECT_DISPLAY);
        outputStream.write(ESCPOS.VISOR_CLEAR);
        outputStream.write(ESCPOS.HIDE_CURSOR);
        //outputStream.write(ESCPOS.VISOR_HOME);
        //outputStream.flush();
        }
        catch(IOException e){
            printError(e);
        //System.out.println("HERE");
        }
}

    



    public void init(){
    try{
    outputStream.write(ESCPOS.Anim);
    }
    catch(IOException i){printError(i);}
}

public void close(){
     serialPort.close();
     //System.exit(1);
   
}
    public void writeMode(){
         try{
    outputStream.write(ESCPOS.SEND40);
    }
    catch(IOException i){printError(i);}
    }

public PoleDisplay(ConfigFileReader reader) {
    this.reader =reader;
    StartDisplay();   //optimal choice is start when system start.
    writeMode();
    ClearDisplay();

}


    void updateTotal(double totalPriceAfterTax) {
    //StartDisplay();   //optimal choice is start when system start.
   // writeMode();
    ClearDisplay();
    
    
   // System.out.println("HERE");
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        Date date = new Date();
        String curDate = dateFormat.format(date);
   // PrintFirstLine("Java Open Soruce");
   //PrintSecondLine(Double.toString(totalPriceAfterTax));
   String s = String.format("Total: $%.2f", totalPriceAfterTax);
   printLines(curDate,s);
    }
    
    public void printLines(String line1,String line2){
         try{
    if(line1.length()>20)            //Display can hold only 20 characters per line.Most of displays have 2 lines.
        line1=line1.substring(0,20);
    outputStream.write(ESCPOS.Left_Line);
    outputStream.write(line1.getBytes());
   // outputStream.flush();
   
    }
    catch(IOException r){
        printError(r);
    }
         try{
    outputStream.write(ESCPOS.Down_Line);
    outputStream.write(ESCPOS.Left_Line);
    if(line2.length()>20)
        line2=line2.substring(0,20);
        outputStream.write(line2.getBytes());
        //outputStream.flush();
    }
    catch(IOException y){
        printError(y);
        //System.out.println("Failed to print second line because of :"+y);
    }
    }

        void printError(Exception e) {
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream("ERROR.txt"));
            System.setOut(out);
           // System.out.println("POLE DISPLAY\n"+e.getMessage());
            System.setErr(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CapSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
