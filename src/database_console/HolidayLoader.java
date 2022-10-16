package database_console;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**

 @author R-Mule
 */
public class HolidayLoader {

    private Holidays chosenHoliday;
    private Holidays actualHoliday;
    private MainFrame mf;
    private ArrayList<Component> activeHolidayComponents = new ArrayList<>();

    public HolidayLoader(MainFrame mf) {
        this.mf = mf;
        mf.quote.setText(Database.getQuote());
        mf.add(mf.quote);
        mf.add(mf.quoteButton);
        mf.quote.setBounds(10, 10, 1900, 50);
        mf.quote.setVisible(true);
        mf.getContentPane().setBackground(new Color(200, 200, 200));
        mf.quoteButton.setVisible(true);
        //this creates the quoteButton
        actualHoliday = Holidays.NONE;
        chosenHoliday = Holidays.NONE;
        mf.quoteButton.setLocation(10, 10);
        mf.quoteButton.setSize(100, 15);
        mf.quoteButton.setBackground(new Color(10, 255, 10));
        mf.quoteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String temp = Database.getQuote();
                while (!temp.contentEquals("") && temp.contentEquals(mf.quote.getText()))
                {
                    temp = Database.getQuote();
                }
                mf.quote.setText(temp);
                mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        });

        mf.mmButton.setSize(350, 100);
        mf.mmButton.setLocation(1400, 900);
        mf.mmButton.setVisible(false);
        mf.add(mf.mmButton);

        mf.mmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ArrayList<String> data = Database.getEmployeesAndWinLossMM();
                System.out.println(data.get(0));
                JFrame frame = new JFrame("");
                String dataString = "Name : Wins : Losses\n";
                for (String temp : data)
                {
                    dataString += temp + "\n";
                }
//custom title, no icon
                JOptionPane.showMessageDialog(frame,
                        dataString,
                        "Yes...I spent time on this.",
                        JOptionPane.PLAIN_MESSAGE);
                mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        });

        if (mf.isMarchMadness)
        {
            mf.mmButton.setVisible(true);

        }

    }

    public void assignActualHoliday(Holidays holiday) {
        actualHoliday = holiday;
    }

    public void switchToActualHoliday() {
        if (chosenHoliday == actualHoliday)
        {
            return;
        }

        chosenHoliday = actualHoliday;
        switch (actualHoliday)
        {
            case CHRISTMAS:
                makeChristmasActiveHoliday();
                break;
            case THANKSGIVING:
                makeThanksgivingActiveHoliday();
                break;
            case VALENTINES:
                makeThanksgivingActiveHoliday();
                break;
            case EASTER:
                makeEasterActiveHoliday();
                break;
            case FOURTHOFJULY:
                make4thOfJulyActiveHoliday();
                break;
            case SAINTPTSDAY:
                makeSaintPatricksDayActiveHoliday();
                break;
            case WEDDINGMONTH:
                makeWeddingMonthActiveHoliday();
                break;
            case HALLOWEEN:
                makeHalloweenActiveHoliday();
                break;
            case SUMMERTIME:
                makeSummerTimeActiveHoliday();
                break;
            default:
                removeActiveHoliday();
                break;
        }
    }

    public void makeHolliesLastDay() {
        /*HOLLIES
        JButton lastDay = new JButton("One Last Time.");
        lastDay.setLocation(800, 25);
        lastDay.setSize(300, 25);
        lastDay.setBackground(new Color(134, 248, 255));
        lastDay.setVisible(true);

        JLabel goodbyeMessage = new JLabel("You have worked incredibly hard. We are so very proud of you Hollie! Love,  Andrew, Kieryn, Anduin, Giles, Mayne, Jawa, and Muffin <3!!!!");
        goodbyeMessage.setSize(800, 300);
        goodbyeMessage.setLocation(10, -115);
        goodbyeMessage.setVisible(true);
        mf.add(goodbyeMessage);
        mf.add(lastDay);
        deactivateQuotes();
        mf.getContentPane().setBackground(new Color(203, 203, 203));
        
        lastDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                EasterEgg ee = new EasterEgg("images/lastDay.gif", "sounds/weddingthankyou.wav", "", "Chapter 2 Here We Come!");
                mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        });*/
        
        //Bevs
        JButton lastDay = new JButton("30 Years <3");
        lastDay.setLocation(800, 25);
        lastDay.setSize(300, 25);
        lastDay.setBackground(new Color(134, 248, 255));
        lastDay.setVisible(true);

        JLabel goodbyeMessage = new JLabel("Congratulations Bev on 30 great years! Enjoy a well earned retirement! Love, Your Work Family!");
        goodbyeMessage.setSize(800, 300);
        goodbyeMessage.setLocation(10, -115);
        goodbyeMessage.setVisible(true);
        mf.add(goodbyeMessage);
        mf.add(lastDay);
        deactivateQuotes();
        mf.getContentPane().setBackground(new Color(203, 203, 203));
        
        lastDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                EasterEgg ee = new EasterEgg("images/lastDay3.gif", "sounds/moonlight.wav", "", "Thank you for the memories!");
                mf.textField.requestFocusInWindow();//this keeps focus on the UPC BAR READER
            }
        });
    }

    public void makeChristmasActiveHoliday() {
        removeActiveHoliday();
        hideMarchMaddness();
        chosenHoliday = Holidays.CHRISTMAS;
        //Christmas
        ImageIcon christmas1img = new ImageIcon(getClass().getResource("images/Christmas1.png"));
        JLabel christmas1imageLabel = new JLabel(christmas1img);
        ImageIcon christmas2img = new ImageIcon(getClass().getResource("images/Christmas2.png"));
        JLabel christmas2imageLabel = new JLabel(christmas2img);
        ImageIcon christmas3img = new ImageIcon(getClass().getResource("images/Christmas3.png"));
        JLabel christmas3imageLabel = new JLabel(christmas3img);
        ImageIcon christmas4img = new ImageIcon(getClass().getResource("images/Christmas4.png"));
        JLabel christmas4imageLabel = new JLabel(christmas4img);
        ImageIcon christmas5img = new ImageIcon(getClass().getResource("images/Christmas5.png"));
        JLabel christmas5imageLabel = new JLabel(christmas5img);
        ImageIcon christmas6img = new ImageIcon(getClass().getResource("images/Christmas6.png"));
        JLabel christmas6imageLabel = new JLabel(christmas6img);

        christmas1imageLabel.setSize(300, 300);
        christmas1imageLabel.setLocation(1250, 450);
        christmas1imageLabel.setVisible(true);
        mf.add(christmas1imageLabel);
        christmas2imageLabel.setSize(300, 200);
        christmas2imageLabel.setLocation(1525, 500);
        christmas2imageLabel.setVisible(true);
        mf.add(christmas2imageLabel);
        christmas3imageLabel.setSize(300, 200);
        christmas3imageLabel.setLocation(250, -40);
        christmas3imageLabel.setVisible(true);
        mf.add(christmas3imageLabel);
        christmas4imageLabel.setSize(500, 200);
        christmas4imageLabel.setLocation(1325, 820);
        christmas4imageLabel.setVisible(true);
        mf.add(christmas4imageLabel);
        christmas5imageLabel.setSize(200, 500);
        christmas5imageLabel.setLocation(1175, 650);
        christmas5imageLabel.setVisible(true);
        mf.add(christmas5imageLabel);
        christmas6imageLabel.setSize(200, 200);
        christmas6imageLabel.setLocation(1450, 200);
        christmas6imageLabel.setVisible(true);
        mf.add(christmas6imageLabel);
        mf.getContentPane().setBackground(new Color(203, 203, 203));

        deactivateQuotes();

        activeHolidayComponents.add(christmas1imageLabel);
        activeHolidayComponents.add(christmas2imageLabel);
        activeHolidayComponents.add(christmas3imageLabel);
        activeHolidayComponents.add(christmas4imageLabel);
        activeHolidayComponents.add(christmas5imageLabel);
        activeHolidayComponents.add(christmas6imageLabel);
    }

    public void makeHalloweenActiveHoliday() {
        removeActiveHoliday();
        hideMarchMaddness();
        chosenHoliday = Holidays.HALLOWEEN;
        ImageIcon halloween1img = new ImageIcon(getClass().getResource("images/Halloween1.png"));
        JLabel halloween1imageLabel = new JLabel(halloween1img);
        ImageIcon halloween2img = new ImageIcon(getClass().getResource("images/Halloween2.png"));
        JLabel halloween2imageLabel = new JLabel(halloween2img);
        ImageIcon halloween3img = new ImageIcon(getClass().getResource("images/Halloween3.png"));
        JLabel halloween3imageLabel = new JLabel(halloween3img);
        ImageIcon halloween4img = new ImageIcon(getClass().getResource("images/Halloween4.png"));
        JLabel halloween4imageLabel = new JLabel(halloween4img);
        ImageIcon halloween5img = new ImageIcon(getClass().getResource("images/Halloween5.png"));
        JLabel halloween5imageLabel = new JLabel(halloween5img);
        ImageIcon halloween6img = new ImageIcon(getClass().getResource("images/Halloween6.png"));
        JLabel halloween6imageLabel = new JLabel(halloween6img);
        ImageIcon halloween7img = new ImageIcon(getClass().getResource("images/Halloween7.png"));
        JLabel halloween7imageLabel = new JLabel(halloween7img);
        ImageIcon halloween8img = new ImageIcon(getClass().getResource("images/Halloween8.png"));
        JLabel halloween8imageLabel = new JLabel(halloween8img);
        //Halloween
        // e1imageLabel.setBounds(100, 100, 100, 100);
        halloween1imageLabel.setSize(300, 200);
        halloween1imageLabel.setLocation(1475, 200);
        halloween1imageLabel.setVisible(true);
        mf.add(halloween1imageLabel);
        halloween2imageLabel.setSize(300, 200);
        halloween2imageLabel.setLocation(1325, 200);
        halloween2imageLabel.setVisible(true);
        mf.add(halloween2imageLabel);
        halloween3imageLabel.setSize(300, 200);
        halloween3imageLabel.setLocation(1200, 800);
        halloween3imageLabel.setVisible(true);
        mf.add(halloween3imageLabel);
        halloween4imageLabel.setSize(300, 200);
        halloween4imageLabel.setLocation(1425, 800);
        halloween4imageLabel.setVisible(true);
        mf.add(halloween4imageLabel);
        halloween5imageLabel.setSize(200, 200);
        halloween5imageLabel.setLocation(1625, 550);
        halloween5imageLabel.setVisible(true);
        mf.add(halloween5imageLabel);
        halloween6imageLabel.setSize(200, 200);
        halloween6imageLabel.setLocation(1425, 500);
        halloween6imageLabel.setVisible(true);
        mf.add(halloween6imageLabel);
        halloween7imageLabel.setSize(200, 200);
        halloween7imageLabel.setLocation(1230, 535);
        halloween7imageLabel.setVisible(true);
        mf.add(halloween7imageLabel);
        halloween8imageLabel.setSize(200, 200);
        halloween8imageLabel.setLocation(350, -50);
        halloween8imageLabel.setVisible(true);
        mf.add(halloween8imageLabel);

        deactivateQuotes();

        activeHolidayComponents.add(halloween1imageLabel);
        activeHolidayComponents.add(halloween2imageLabel);
        activeHolidayComponents.add(halloween3imageLabel);
        activeHolidayComponents.add(halloween4imageLabel);
        activeHolidayComponents.add(halloween5imageLabel);
        activeHolidayComponents.add(halloween6imageLabel);
        activeHolidayComponents.add(halloween7imageLabel);
        activeHolidayComponents.add(halloween8imageLabel);

        setGUITextColor(Color.RED);
        mf.getContentPane().setBackground(Color.BLACK);

    }

    public void make4thOfJulyActiveHoliday() {
        removeActiveHoliday();
        hideMarchMaddness();
        chosenHoliday = Holidays.FOURTHOFJULY;
        ImageIcon fourth1img = new ImageIcon(getClass().getResource("images/4th1.png"));
        JLabel fourth1imageLabel = new JLabel(fourth1img);
        ImageIcon fourth2img = new ImageIcon(getClass().getResource("images/4th2.png"));
        JLabel fourth2imageLabel = new JLabel(fourth2img);
        ImageIcon fourth3img = new ImageIcon(getClass().getResource("images/4th3.png"));
        JLabel fourth3imageLabel = new JLabel(fourth3img);
        fourth1imageLabel.setSize(400, 200);
        fourth1imageLabel.setLocation(1400, 815);
        fourth1imageLabel.setVisible(true);
        mf.add(fourth1imageLabel);
        fourth2imageLabel.setSize(200, 400);
        fourth2imageLabel.setLocation(1600, 390);
        fourth2imageLabel.setVisible(true);
        mf.add(fourth2imageLabel);
        fourth3imageLabel.setSize(600, 400);
        fourth3imageLabel.setLocation(1300, 100);
        fourth3imageLabel.setVisible(true);
        mf.add(fourth3imageLabel);

        activeHolidayComponents.add(fourth1imageLabel);
        activeHolidayComponents.add(fourth2imageLabel);
        activeHolidayComponents.add(fourth3imageLabel);

        mf.getContentPane().setBackground(new Color(30, 45, 96));
        setGUITextColor(Color.WHITE);
    }

    public void makeSummerTimeActiveHoliday() {
        removeActiveHoliday();
        hideMarchMaddness();
        chosenHoliday = Holidays.SUMMERTIME;
        ImageIcon christmas1img = new ImageIcon(getClass().getResource("images/beach1.png"));
        JLabel christmas1imageLabel = new JLabel(christmas1img);
        ImageIcon christmas2img = new ImageIcon(getClass().getResource("images/beach2.png"));
        JLabel christmas2imageLabel = new JLabel(christmas2img);
        ImageIcon christmas3img = new ImageIcon(getClass().getResource("images/beach3.png"));
        JLabel christmas3imageLabel = new JLabel(christmas3img);
        ImageIcon christmas4img = new ImageIcon(getClass().getResource("images/beach4.png"));
        JLabel christmas4imageLabel = new JLabel(christmas4img);
        ImageIcon christmas5img = new ImageIcon(getClass().getResource("images/beach5.png"));
        JLabel christmas5imageLabel = new JLabel(christmas5img);
        ImageIcon christmas6img = new ImageIcon(getClass().getResource("images/beach6.png"));
        JLabel christmas6imageLabel = new JLabel(christmas6img);

        christmas1imageLabel.setSize(300, 300);
        christmas1imageLabel.setLocation(1400, 175);
        christmas1imageLabel.setVisible(true);
        mf.add(christmas1imageLabel);
        christmas2imageLabel.setSize(300, 200);
        christmas2imageLabel.setLocation(1450, 850);
        christmas2imageLabel.setVisible(true);
        mf.add(christmas2imageLabel);
        christmas3imageLabel.setSize(350, 400);
        christmas3imageLabel.setLocation(1275, 710);
        christmas3imageLabel.setVisible(true);
        mf.add(christmas3imageLabel);
        christmas4imageLabel.setSize(600, 200);
        christmas4imageLabel.setLocation(1375, 500);
        christmas4imageLabel.setVisible(true);
        mf.add(christmas4imageLabel);
        christmas5imageLabel.setSize(200, 200);
        christmas5imageLabel.setLocation(300, -30);
        christmas5imageLabel.setVisible(true);
        mf.add(christmas5imageLabel);
        christmas6imageLabel.setSize(200, 200);
        christmas6imageLabel.setLocation(1300, 500);
        christmas6imageLabel.setVisible(true);
        mf.add(christmas6imageLabel);

        mf.getContentPane().setBackground(new Color(219, 209, 180));

        activeHolidayComponents.add(christmas1imageLabel);
        activeHolidayComponents.add(christmas2imageLabel);
        activeHolidayComponents.add(christmas3imageLabel);
        activeHolidayComponents.add(christmas4imageLabel);
        activeHolidayComponents.add(christmas5imageLabel);
        activeHolidayComponents.add(christmas6imageLabel);

    }

    public void makeEasterActiveHoliday() {
        removeActiveHoliday();
        chosenHoliday = Holidays.EASTER;
        ImageIcon easter1img = new ImageIcon(getClass().getResource("images/Easter1.png"));
        JLabel e1imageLabel = new JLabel(easter1img);
        ImageIcon easter2img = new ImageIcon(getClass().getResource("images/Easter2.png"));
        JLabel e2imageLabel = new JLabel(easter2img);
        // e1imageLabel.setBounds(100, 100, 100, 100);
        e1imageLabel.setSize(800, 200);
        e1imageLabel.setLocation(1100, 540);
        e1imageLabel.setVisible(true);
        mf.add(e1imageLabel);
        e2imageLabel.setSize(800, 400);
        e2imageLabel.setLocation(1200, 100);
        e2imageLabel.setVisible(true);
        mf.add(e2imageLabel);
        activeHolidayComponents.add(e1imageLabel);
        activeHolidayComponents.add(e2imageLabel);
        mf.getContentPane().setBackground(new Color(224, 205, 255));

    }

    public void makeWeddingMonthActiveHoliday() {
        removeActiveHoliday();
        deactivateQuotes();
        hideMarchMaddness();
        chosenHoliday = Holidays.WEDDINGMONTH;
        ImageIcon christmas1img = new ImageIcon(getClass().getResource("images/Wedding1.png"));
        JLabel christmas1imageLabel = new JLabel(christmas1img);
        ImageIcon christmas2img = new ImageIcon(getClass().getResource("images/Wedding2.png"));
        JLabel christmas2imageLabel = new JLabel(christmas2img);
        ImageIcon christmas3img = new ImageIcon(getClass().getResource("images/Wedding3.png"));
        JLabel christmas3imageLabel = new JLabel(christmas3img);
        ImageIcon christmas4img = new ImageIcon(getClass().getResource("images/Wedding4.png"));
        JLabel christmas4imageLabel = new JLabel(christmas4img);
        ImageIcon christmas5img = new ImageIcon(getClass().getResource("images/Wedding5.png"));
        JLabel christmas5imageLabel = new JLabel(christmas5img);

        christmas1imageLabel.setSize(500, 500);
        christmas1imageLabel.setLocation(1250, 675);
        christmas1imageLabel.setVisible(true);
        mf.add(christmas1imageLabel);
        christmas2imageLabel.setSize(300, 200);
        christmas2imageLabel.setLocation(1525, 500);
        christmas2imageLabel.setVisible(true);
        mf.add(christmas2imageLabel);
        christmas3imageLabel.setSize(300, 200);
        christmas3imageLabel.setLocation(250, -40);
        christmas3imageLabel.setVisible(true);
        mf.add(christmas3imageLabel);
        christmas4imageLabel.setSize(500, 300);
        christmas4imageLabel.setLocation(1175, 450);
        christmas4imageLabel.setVisible(true);
        mf.add(christmas4imageLabel);
        christmas5imageLabel.setSize(400, 200);
        christmas5imageLabel.setLocation(1375, 200);
        christmas5imageLabel.setVisible(true);
        mf.add(christmas5imageLabel);

        mf.getContentPane().setBackground(new Color(192, 192, 192));

        activeHolidayComponents.add(christmas1imageLabel);
        activeHolidayComponents.add(christmas2imageLabel);
        activeHolidayComponents.add(christmas3imageLabel);
        activeHolidayComponents.add(christmas4imageLabel);
        activeHolidayComponents.add(christmas5imageLabel);

    }

    public void makeValentinesDayActiveHoliday() {
        removeActiveHoliday();
        hideMarchMaddness();
        chosenHoliday = Holidays.VALENTINES;
        ImageIcon fourth1img = new ImageIcon(getClass().getResource("images/Valentines1.png"));
        JLabel fourth1imageLabel = new JLabel(fourth1img);
        ImageIcon fourth2img = new ImageIcon(getClass().getResource("images/Valentines2.png"));
        JLabel fourth2imageLabel = new JLabel(fourth2img);
        ImageIcon fourth3img = new ImageIcon(getClass().getResource("images/Valentines3.png"));
        JLabel fourth3imageLabel = new JLabel(fourth3img);
        ImageIcon valentines4img = new ImageIcon(getClass().getResource("images/Valentines4.png"));
        JLabel valentines4imageLabel = new JLabel(valentines4img);

        fourth1imageLabel.setSize(400, 200);
        fourth1imageLabel.setLocation(1300, 815);
        fourth1imageLabel.setVisible(true);
        mf.add(fourth1imageLabel);
        fourth2imageLabel.setSize(200, 200);
        fourth2imageLabel.setLocation(300, -50);
        fourth2imageLabel.setVisible(true);
        mf.add(fourth2imageLabel);
        fourth3imageLabel.setSize(600, 400);
        fourth3imageLabel.setLocation(1200, 420);
        fourth3imageLabel.setVisible(true);
        mf.add(fourth3imageLabel);
        valentines4imageLabel.setSize(600, 400);
        valentines4imageLabel.setLocation(1300, 100);
        valentines4imageLabel.setVisible(true);
        mf.add(valentines4imageLabel);

        activeHolidayComponents.add(fourth1imageLabel);
        activeHolidayComponents.add(fourth2imageLabel);
        activeHolidayComponents.add(fourth3imageLabel);
        activeHolidayComponents.add(valentines4imageLabel);

        mf.getContentPane().setBackground(new Color(228, 131, 151));

    }

    public void makeThanksgivingActiveHoliday() {
        removeActiveHoliday();//This makes quotes active by default.
        hideMarchMaddness();
        chosenHoliday = Holidays.THANKSGIVING;
        ImageIcon thanksgiving1img = new ImageIcon(getClass().getResource("images/Thanksgiving1.png"));
        JLabel thanksgiving1imageLabel = new JLabel(thanksgiving1img);
        ImageIcon thanksgiving2img = new ImageIcon(getClass().getResource("images/Thanksgiving2.png"));
        JLabel thanksgiving2imageLabel = new JLabel(thanksgiving2img);
        ImageIcon thanksgiving3img = new ImageIcon(getClass().getResource("images/Thanksgiving3.png"));
        JLabel thanksgiving3imgLabel = new JLabel(thanksgiving3img);

        thanksgiving1imageLabel.setSize(400, 200);
        thanksgiving1imageLabel.setLocation(1400, 200);
        thanksgiving1imageLabel.setVisible(true);
        mf.add(thanksgiving1imageLabel);
        thanksgiving2imageLabel.setSize(200, 400);
        thanksgiving2imageLabel.setLocation(1400, 400);
        thanksgiving2imageLabel.setVisible(true);
        mf.add(thanksgiving2imageLabel);
        thanksgiving3imgLabel.setSize(600, 400);
        thanksgiving3imgLabel.setLocation(1200, 700);
        thanksgiving3imgLabel.setVisible(true);
        mf.add(thanksgiving3imgLabel);
        mf.getContentPane().setBackground(new Color(158, 104, 42));
        //activateQuotes();
        activeHolidayComponents.add(thanksgiving1imageLabel);
        activeHolidayComponents.add(thanksgiving2imageLabel);
        activeHolidayComponents.add(thanksgiving3imgLabel);

    }

    public void makeSaintPatricksDayActiveHoliday() {
        removeActiveHoliday();
        chosenHoliday = Holidays.SAINTPTSDAY;
        ImageIcon thanksgiving1img = new ImageIcon(getClass().getResource("images/saintpt1.png"));
        JLabel thanksgiving1imageLabel = new JLabel(thanksgiving1img);
        ImageIcon thanksgiving2img = new ImageIcon(getClass().getResource("images/saintpt2.png"));
        JLabel thanksgiving2imageLabel = new JLabel(thanksgiving2img);
        ImageIcon thanksgiving3img = new ImageIcon(getClass().getResource("images/saintpt3.png"));
        JLabel thanksgiving3imgLabel = new JLabel(thanksgiving3img);

        thanksgiving1imageLabel.setSize(400, 200);
        thanksgiving1imageLabel.setLocation(1400, 200);
        thanksgiving1imageLabel.setVisible(true);
        mf.add(thanksgiving1imageLabel);
        thanksgiving2imageLabel.setSize(200, 400);
        thanksgiving2imageLabel.setLocation(1400, 400);
        thanksgiving2imageLabel.setVisible(true);
        mf.add(thanksgiving2imageLabel);
        thanksgiving3imgLabel.setSize(600, 400);
        thanksgiving3imgLabel.setLocation(1100, -150);
        thanksgiving3imgLabel.setVisible(true);
        mf.add(thanksgiving3imgLabel);

        mf.getContentPane().setBackground(new Color(96, 168, 48));

        activeHolidayComponents.add(thanksgiving1imageLabel);
        activeHolidayComponents.add(thanksgiving2imageLabel);
        activeHolidayComponents.add(thanksgiving3imgLabel);

    }

    public void removeActiveHoliday() {
        for (Component comp : activeHolidayComponents)
        {
            comp.setVisible(false);
            mf.remove(comp);
        }
        mf.getContentPane().setBackground(new Color(200, 200, 200));
        activeHolidayComponents.clear();
        activateQuotes();

        Color color = Color.BLACK;
        mf.itemNameHeader.setForeground(color);
        mf.employeeCheckoutHeader.setForeground(color);
        mf.totalNumRXinCart.setForeground(color);
        mf.itemNameHeader.setForeground(color);
        mf.itemQuantityHeader.setForeground(color);
        mf.itemPriceHeader.setForeground(color);
        mf.versionHeader.setForeground(color);
        mf.discountHeader.setForeground(color);
        mf.itemSubTotalHeader.setForeground(color);
        mf.estimatedCheckTotalLabel.setForeground(color);
        mf.estimatedLunchTotalLabel.setForeground(color);
        mf.estimatedCashTotalLabel.setForeground(color);
        mf.estimatedCoinTotalLabel.setForeground(color);
        mf.employeeSelectionHeader.setForeground(color);
        mf.quote.setForeground(color);
        mf.subTotal.setForeground(color);
        mf.totalTax.setForeground(color);
        mf.totalPrice.setForeground(color);
        mf.changeDue.setForeground(color);
        mf.subTotalHeader.setForeground(color);
        mf.itemTaxTotalHeader.setForeground(color);

        if (mf.isMarchMadness)
        {
            mf.mmButton.setVisible(true);
        }
        else
        {
            hideMarchMaddness();
        }

    }

    public void showMarchMaddness() {
        if (mf.isMarchMadness)
        {
            mf.mmButton.setVisible(true);
        }
    }

    private void hideMarchMaddness() {
        mf.mmButton.setVisible(false);
    }

    private void setGUITextColor(Color color) {
        mf.itemNameHeader.setForeground(color);
        mf.employeeCheckoutHeader.setForeground(color);
        mf.totalNumRXinCart.setForeground(color);
        mf.itemNameHeader.setForeground(color);
        mf.itemQuantityHeader.setForeground(color);
        mf.itemPriceHeader.setForeground(color);
        mf.versionHeader.setForeground(color);
        mf.discountHeader.setForeground(color);
        mf.itemSubTotalHeader.setForeground(color);
        mf.estimatedCheckTotalLabel.setForeground(color);
        mf.estimatedLunchTotalLabel.setForeground(color);
        mf.estimatedCashTotalLabel.setForeground(color);
        mf.estimatedCoinTotalLabel.setForeground(color);
        mf.employeeSelectionHeader.setForeground(color);
        mf.quote.setForeground(color);
        mf.subTotal.setForeground(color);
        mf.totalTax.setForeground(color);
        mf.totalPrice.setForeground(color);
        mf.changeDue.setForeground(color);
        mf.subTotalHeader.setForeground(color);
        mf.itemTaxTotalHeader.setForeground(color);
    }

    private void activateQuotes() {
        mf.quote.setVisible(true);
        mf.quoteButton.setVisible(true);

    }

    private void deactivateQuotes() {
        mf.quote.setVisible(false);
        mf.quoteButton.setVisible(false);
    }

}
