package database_console;
//test Hollie

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Database_console {

    public static void main(String args[]) {

        try {
            ConfigFileReader.loadConfiguration();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Database.loadDatabase();
        MainFrame jf = new MainFrame();
        jf.setData();
        jf.setVisible(true);
    }

}
