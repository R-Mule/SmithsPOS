package database_console;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 @author R-Mule
 */
public class ErrorLogger {

    public static void writeErrorMsg(Exception ex, String additionalInfo) {
        FileWriter fw = null;
        BufferedWriter bw = null;
    
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy_hh-mm-ss");
        String currentTime = dateFormat.format(date);
        String logPath = ConfigFileReader.getErrorLogPath();
        try
        {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            fw = new FileWriter(logPath + currentTime + ".txt");
            bw = new BufferedWriter(fw);
            bw.write("Additional Info: \n" + additionalInfo + "\n\n\nException:\n" + ex.toString() + "\n\nStack Trace:\n" + stackTrace);
        }
        catch (IOException ex1)
        {
            Logger.getLogger(ErrorLogger.class.getName()).log(Level.SEVERE, null, ex1);
        }
                finally
        {

            try
            {

                if (bw != null)
                {
                    bw.close();
                }

                if (fw != null)
                {
                    fw.close();
                }

            }
            catch (IOException e)
            {

                ex.printStackTrace();

            }
        }

    }
    
        public static void writeErrorMsg(String msg) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy_hh-mm-ss");
        String currentTime = dateFormat.format(date);
        String logPath = ConfigFileReader.getErrorLogPath();
        try
        {
            fw = new FileWriter(logPath + currentTime + ".txt");
            bw = new BufferedWriter(fw);
            bw.write("Info: \n" + msg);
        }
        catch (IOException ex1)
        {
            Logger.getLogger(ErrorLogger.class.getName()).log(Level.SEVERE, null, ex1);
        }
        finally
        {

            try
            {

                if (bw != null)
                {
                    bw.close();
                }

                if (fw != null)
                {
                    fw.close();
                }

            }
            catch (IOException ex)
            {

                ex.printStackTrace();

            }
        }
    }
}
