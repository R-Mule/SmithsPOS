package database_console;

/**
 *
 * @author A.Smith
 */
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import com.topaz.sigplus.*;
import java.io.*;
import java.awt.image.BufferedImage;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class CapSignature extends JDialog {//implements Runnable {

    boolean hasBeenSaved = false;
    SigPlus sigObj = null;
    MainFrame mf = null;
    Cart curCart;
    String remoteDrivePath;
    String emergencyDrivePath = "C:\\POS\\Emergency_RX_Saves\\";
    String receiptNumber;
    boolean hasBeenClicked = false;
    // Thread eventThread;

    public CapSignature(MainFrame mf, Cart curCart, String remoteDrivePath, String receiptNumber) {
        super((Window) null);
        this.mf = mf;
        this.curCart = curCart;
        this.remoteDrivePath = remoteDrivePath;
        this.receiptNumber = receiptNumber;
    }

    public void begin(boolean questions) {

        setVisible(false);

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(800, 300);
        this.setBackground(Color.lightGray);
        this.setLocation(500, 500);
        this.setAlwaysOnTop(true);
        mf.setAlwaysOnTop(false);
        this.requestFocusInWindow();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(gbl);
        Panel controlPanel = new Panel();

        setConstraints(controlPanel, gbl, gc, 0, 0,
                GridBagConstraints.REMAINDER, 1, 0, 0,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE, 0, 0, 0, 0);
        add(controlPanel, gc);

        controlPanel.add(connectionChoice);
        controlPanel.add(connectionTablet);

        Button startButton = new Button("START");
        controlPanel.add(startButton);

        Button stopButton = new Button("STOP");
        controlPanel.add(stopButton);

        Button clearButton = new Button("CLEAR");
        controlPanel.add(clearButton);

        Button savePdfButton = new Button("SAVE TO PDF");
        controlPanel.add(savePdfButton);

        initConnection();

        /* String drivername = "com.sun.comm.Win32Driver";
        try {
            CommDriver driver = (CommDriver) Class.forName(drivername).newInstance();
            driver.initialize();
        } catch (Throwable th) {
            System.out.println("HERE");
            printError(new Exception());

        }*/
        try {

            ClassLoader cl = (com.topaz.sigplus.SigPlus.class).getClassLoader();
            sigObj = (SigPlus) Beans.instantiate(cl, "com.topaz.sigplus.SigPlus");

            setConstraints(sigObj, gbl, gc, 0, 1,
                    GridBagConstraints.REMAINDER, 1, 1, 1,
                    GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, 5, 0, 5, 0);
            add(sigObj, gc);
            sigObj.setSize(100, 100);
            sigObj.clearTablet();
            //setTitle( "Demo SigPlus iText PDF Demo" );

            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sigObj.setTabletState(0);
                    sigObj.setTabletState(1);
                }
            });

            stopButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sigObj.setTabletState(0);
                }
            });

            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   // System.out.println(sigObj.getKeyReceipt());
                    sigObj.clearTablet();
                }
            });

            savePdfButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!hasBeenClicked) {
                        hasBeenClicked = true;
                        Document document = new Document();
                        if (!new File(remoteDrivePath).exists()) {
                            remoteDrivePath = emergencyDrivePath;
                            System.out.println("EMERGENCY RX SAVE");
                        }
                        try {
                            System.out.println(remoteDrivePath);
                            File theFile = new File(remoteDrivePath + receiptNumber.substring(0, 2) + receiptNumber.substring(4, 6));
                            theFile.mkdirs();
                            theFile.setReadable(true);
                            theFile.setWritable(true);
                            PdfWriter.getInstance(document, new FileOutputStream(remoteDrivePath + receiptNumber.substring(0, 2) + receiptNumber.substring(4, 6) + "//" + receiptNumber + ".pdf"));

                            document.open();

                            sigObj.setTabletState(0);
                            sigObj.setImageJustifyMode(5);
                            sigObj.setImagePenWidth(6);
                            sigObj.setImageXSize(1600);
                            sigObj.setImageYSize(400);
                            BufferedImage sigImage = sigObj.sigImage();
                            int w = sigImage.getWidth(null);
                            int h = sigImage.getHeight(null);
                            int[] pixels = new int[(w * h) * 2];
                            sigImage.setRGB(0, 0, 0, 0, pixels, 0, 0);
                            com.lowagie.text.Image img1 = com.lowagie.text.Image.getInstance(sigImage, null, true);
                            img1.scalePercent(15);
                            Paragraph paragraph = new Paragraph();
                            paragraph.add("Fill Date          RX Number          Pickup Date         Clerk                  Insurance\n");
                            for (Item item : curCart.getItems()) {
                                if (item.isRX()) {
                                    String fillDate = item.getFillDate().substring(0, 2) + "/" + item.getFillDate().substring(2, 4) + "/" + item.getFillDate().substring(4, 6);
                                    String insurance = item.getInsurance().trim();
                                    DateFormat dateFormat = new SimpleDateFormat("MMddyy");
                                    Date date = new Date();
                                    String pickupDate = dateFormat.format(date);
                                    String clerk = mf.employeeSelectionHeader.getText().substring(14);
                                    String line = "";
                                    if (fillDate == null || pickupDate == null || clerk == null || insurance == null) {
                                        line = "NULL ERROR, REPORT TO DREW";
                                    } else {
                                        line = String.format("%-20s%-20s%-15s%-20s%-25s", fillDate, item.getRxNumber(), pickupDate, clerk, insurance);
                                    }
                                    paragraph.add(line);
                                    paragraph.add("\n");
                                }
                            }
                            paragraph.setAlignment(Element.ALIGN_LEFT);
                            if (questions) {
                                paragraph.add("Does Patient have medication questions? Yes\n");
                            } else {
                                paragraph.add("Does Patient have medication questions? No\n");
                            }
                            document.add(paragraph);
                            document.add(img1);
                            hasBeenSaved = true;
                            setVisible(false);
                        } catch (DocumentException de) {
                            printError(de);

                        } catch (IOException ioe) {
                            printError(ioe);
                        }

                        document.close();
                    }
                }
            });

            connectionTablet.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {

                    if (connectionTablet.getSelectedItem() != "SignatureGemLCD4X3") {
                        sigObj.setTabletModel(connectionTablet.getSelectedItem());
                    } else {
                        sigObj.setTabletModel("SignatureGemLCD4X3New"); //properly set up LCD4X3
                    }

                }
            });

            connectionChoice.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {

                    if (connectionChoice.getSelectedItem() != "HSB") {
                        sigObj.setTabletComPort(connectionChoice.getSelectedItem());
                    } else {
                        sigObj.setTabletComPort("HID1"); //properly set up HSB tablet
                    }

                }
            });

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    sigObj.setTabletState(0);
                }

                public void windowClosed(WindowEvent we) {
                }
            });

            sigObj.addSigPlusListener(new SigPlusListener() {
                public void handleTabletTimerEvent(SigPlusEvent0 evt) {
                }

                public void handleNewTabletData(SigPlusEvent0 evt) {
                }

                public void handleKeyPadData(SigPlusEvent0 evt) {
                }
            });

            this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        } catch (Exception e) {
            printError(e);
            return;
        }

        sigObj.setTabletComPort("HID1");
        sigObj.setTabletModel(connectionTablet.getSelectedItem());
        sigObj.setTabletState(0);
        sigObj.setTabletState(1);

    }

    TextField txtPath = new TextField("C:\\test.sig", 30);

    Choice connectionChoice = new Choice();
    protected String[] connections
            = {
                "HSB"};

    Choice connectionTablet = new Choice();
    protected String[] tablets
            = {
                "SignatureGem1X5"};

    private void initConnection() {
        for (int i = 0; i < connections.length; i++) {
            connectionChoice.add(connections[i]);
        }

        for (int i = 0; i < tablets.length; i++) {
            connectionTablet.add(tablets[i]);
        }

    }

    //Convenience method for GridBagLayout
    private void setConstraints(
            Component comp,
            GridBagLayout gbl,
            GridBagConstraints gc,
            int gridx,
            int gridy,
            int gridwidth,
            int gridheight,
            int weightx,
            int weighty,
            int anchor,
            int fill,
            int top,
            int left,
            int bottom,
            int right) {
        gc.gridx = gridx;
        gc.gridy = gridy;
        gc.gridwidth = gridwidth;
        gc.gridheight = gridheight;
        gc.weightx = weightx;
        gc.weighty = weighty;
        gc.anchor = anchor;
        gc.fill = fill;
        gc.insets = new Insets(top, left, bottom, right);
        gbl.setConstraints(comp, gc);

    }

    void printError(Exception e) {
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream("ERROR.txt"));
            System.setOut(out);
            System.err.println(e.getMessage());
            System.setErr(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CapSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
