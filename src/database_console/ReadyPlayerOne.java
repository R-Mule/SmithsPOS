/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_console;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import pacman.PacmanGame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import javafx.scene.layout.Border;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import tetris.TetrisGame;

/**

 @author R-Mule
 */
public class ReadyPlayerOne implements KeyListener {

    private static Object lock = new Object();
    private LeaderBoard leaderboard;
    private Konami code;
    private MainFrame mf;
    private JButton button;
    private JButton tetrisButton;
    private JButton pacmanButton;
    private JButton galagaButton;
    private JButton selectThemeButton;
    private boolean galagaIsRunning = false;
    private boolean activeUser = false;
    private boolean pacmanIsRunning = false;
    private boolean tetrisIsRunning = false;

    public ReadyPlayerOne(MainFrame mf) {
        this.mf = mf;
        leaderboard = new LeaderBoard(mf);
        code = new Konami();
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String font : fonts)
        {
            if (font.contentEquals("Ready Player One"))
            {
                button = new JButton("Ready Player One");
                button.setFont(new Font("Ready Player One", Font.PLAIN, 30));
                button.setForeground(Color.BLUE);
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setOpaque(false);
                button.setVisible(false);
                button.setLocation(400, 800);
                button.setSize(400, 200);

                tetrisButton = new JButton();
                try
                {
                    Image img = ImageIO.read(getClass().getResource("images/TetrisLogo.png"));
                    tetrisButton.setIcon(new ImageIcon(img));
                }
                catch (Exception ex)
                {
                    System.out.println(ex);
                }
                tetrisButton.setBorderPainted(false);
                tetrisButton.setContentAreaFilled(false);
                tetrisButton.setFocusPainted(false);
                tetrisButton.setOpaque(false);
                tetrisButton.setVisible(false);
                tetrisButton.setLocation(500, 900);
                tetrisButton.setSize(200, 100);

                tetrisButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        if (mf.activeClerksPasscode == 8 || Database.isLockoutLifted(mf.activeClerksPasscode))
                        {
                            if (!pacmanIsRunning && !tetrisIsRunning && !galagaIsRunning)
                            {
                                tetrisIsRunning = true;
                                TetrisGame tetris = new TetrisGame();
                                tetris.setVisible(true);
                                mf.setEnabled(false);

                                tetris.addWindowListener(new WindowAdapter() {

                                    @Override
                                    public void windowClosing(WindowEvent arg0) {

                                        tetris.stopAll();
                                        //Also make sure to lock them out regardless.
                                        Database.insertEggLockout(LocalDate.now(), mf.activeClerksPasscode);

                                        tetris.setVisible(false);
                                        tetrisIsRunning = false;
                                        mf.setEnabled(true);
                                        mf.requestFocus();
                                        mf.textField.requestFocusInWindow();
                                    }
                                });
                            }
                        }
                        else
                        {
                            JFrame message1 = new JFrame("See ya tomorrow!");
                            JOptionPane.showMessageDialog(message1, "Please come back and try again tomorrow.");
                        }
                    }
                });

                galagaButton = new JButton();
                try
                {
                    Image img = ImageIO.read(getClass().getResource("images/GalagaLogo.png"));
                    galagaButton.setIcon(new ImageIcon(img));
                }
                catch (Exception ex)
                {
                    System.out.println(ex);
                }
                galagaButton.setBorderPainted(false);
                galagaButton.setContentAreaFilled(false);
                galagaButton.setFocusPainted(false);
                galagaButton.setOpaque(false);
                galagaButton.setVisible(false);
                galagaButton.setLocation(600, 825);
                galagaButton.setSize(200, 100);
                //galagaButton.setBorder(null);

                //TODO: Remove Tetris from Galaga settings....
                galagaButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        if (mf.activeClerksPasscode == 8 || Database.isLockoutLifted(mf.activeClerksPasscode))
                        {
                            TetrisGame tetris = new TetrisGame();
                            tetris.setVisible(true);
                            mf.setEnabled(false);
                            tetrisIsRunning = true;

                            tetris.addWindowListener(new WindowAdapter() {

                                @Override
                                public void windowClosing(WindowEvent arg0) {

                                    tetris.stopAll();
                                    //Also make sure to lock them out regardless.
                                    Database.insertEggLockout(LocalDate.now(), mf.activeClerksPasscode);

                                    tetris.setVisible(false);
                                    tetrisIsRunning = false;
                                    mf.setEnabled(true);
                                    mf.requestFocus();
                                    mf.textField.requestFocusInWindow();
                                }
                            });
                        }
                        else
                        {
                            JFrame message1 = new JFrame("See ya tomorrow!");
                            JOptionPane.showMessageDialog(message1, "Please come back and try again tomorrow.");
                        }
                    }
                });

                pacmanButton = new JButton();
                try
                {
                    Image img = ImageIO.read(getClass().getResource("images/PacmanLogo.png"));
                    pacmanButton.setIcon(new ImageIcon(img));
                }
                catch (Exception ex)
                {
                    System.out.println(ex);
                }
                pacmanButton.setBorderPainted(false);
                pacmanButton.setContentAreaFilled(false);
                pacmanButton.setFocusPainted(false);
                pacmanButton.setOpaque(false);
                pacmanButton.setVisible(false);
                pacmanButton.setLocation(400, 825);
                pacmanButton.setSize(200, 100);

                pacmanButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        if (mf.activeClerksPasscode == 8 || Database.isLockoutLifted(mf.activeClerksPasscode))
                        {
                            if (!pacmanIsRunning && !tetrisIsRunning && !galagaIsRunning)
                            {
                                pacmanIsRunning = true;
                                mf.setEnabled(false);
                                PacmanGame mw = new PacmanGame();
                                mw.setVisible(true);

                                mw.addWindowListener(new WindowAdapter() {

                                    @Override
                                    public void windowClosing(WindowEvent arg0) {

                                        //TODO: Need to get the data from the game here, window closed so must be time to update stuff...
                                        //Also make sure to lock them out regardless.
                                        Database.insertEggLockout(LocalDate.now(), mf.activeClerksPasscode);
                                        mw.myMaze.windowClosed();
                                        mw.setVisible(false);
                                        pacmanIsRunning = false;
                                        mf.setEnabled(true);
                                        mf.requestFocus();
                                        mf.textField.requestFocusInWindow();

                                    }

                                });
                            }
                        }
                        else
                        {
                            JFrame message1 = new JFrame("See ya tomorrow!");
                            JOptionPane.showMessageDialog(message1, "Please come back and try again tomorrow.");
                        }
                    }
                });

                button.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        System.out.println("They are ready to proceed to play!");

                        if (mf.curCart.isEmpty() && mf.refundCart.isEmpty())//if there is nothing on the GUI lets play a game!
                        {
                            if (Database.isLockoutLifted(mf.activeClerksPasscode))
                            {
                                initiateGame();
                            }
                            else
                            {
                                JFrame message1 = new JFrame("Terminated!");
                                JOptionPane.showMessageDialog(message1, "Please come back and try again tomorrow.");
                            }

                        }
                    }
                });

                selectThemeButton = new JButton("Theme");
                selectThemeButton.setBackground(new Color(0, 255, 255));
                selectThemeButton.setVisible(false);
                selectThemeButton.setLocation(400, 925);
                selectThemeButton.setSize(90, 50);

                selectThemeButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        JFrame textInputFrame = new JFrame("");
                        JTextField field1 = new JTextField();
                        Object[] message =
                        {
                            "Enter number for desired theme:\n1. Halloween\n2. Christmas\n3. Valentines Day\n4. Saint Patrick's Day\n5. Easter\n6. Wedding Month\n7. 4th of July\n8. Summer Time\n9. Thanksgiving\n10. Victory Theme\n11. None ", field1,

                        };
                        field1.addAncestorListener(new RequestFocusListener());
                        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Theme Selector", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION)
                        {
                            if (validateInteger(field1.getText()))
                            {
                                int selection = Integer.parseInt(field1.getText());

                                switch (selection)
                                {
                                    case 1:
                                        mf.holidayLoader.makeHalloweenActiveHoliday();
                                        break;
                                    case 2:
                                        mf.holidayLoader.makeChristmasActiveHoliday();
                                        break;
                                    case 3:
                                        mf.holidayLoader.makeValentinesDayActiveHoliday();
                                        break;
                                    case 4:
                                        mf.holidayLoader.makeSaintPatricksDayActiveHoliday();
                                        break;
                                    case 5:
                                        mf.holidayLoader.makeEasterActiveHoliday();
                                        break;
                                    case 6:
                                        mf.holidayLoader.makeWeddingMonthActiveHoliday();
                                        break;
                                    case 7:
                                        mf.holidayLoader.make4thOfJulyActiveHoliday();
                                        break;
                                    case 8:
                                        mf.holidayLoader.makeSummerTimeActiveHoliday();
                                        break;
                                    case 9:
                                        mf.holidayLoader.makeThanksgivingActiveHoliday();
                                        break;
                                    case 10:
                                        mf.holidayLoader.makeEventWinnerActiveHoliday();
                                        break;
                                    default:
                                        mf.holidayLoader.removeActiveHoliday();
                                        break;
                                }
                            }
                            else
                            {
                                JFrame message1 = new JFrame("Try again.");
                                JOptionPane.showMessageDialog(message1, "Incorrect value.");
                            }

                        }
                        mf.textField.requestFocusInWindow();
                    }
                });

                mf.add(button);
                mf.add(tetrisButton);
                mf.add(galagaButton);
                mf.add(pacmanButton);
                mf.add(selectThemeButton);

            }
        }
    }

    public void reload() {
        button.setVisible(false);//Maybe don't do this if they just logged in as themselves again?
        if (isBackdoorUser())
        {
            tetrisButton.setVisible(false);
            galagaButton.setVisible(false);
            pacmanButton.setVisible(false);
            selectThemeButton.setVisible(false);
        }
        activeUser = true;//but might have changed! Need to recheck database.
    }

    public void exit() {
        button.setVisible(false);
        tetrisButton.setVisible(false);
        galagaButton.setVisible(false);
        pacmanButton.setVisible(false);
        selectThemeButton.setVisible(false);

        activeUser = false;
    }

    public void initiateGame() {
        int currentTier = Database.getEggLevelByPasscode(mf.activeClerksPasscode);
        switch (currentTier)
        {
            //Copper Gate
            case 1:
                if (!tetrisIsRunning)
                {
                    tetrisIsRunning = true;
                    mf.setEnabled(false);
                    TetrisGame mw = new TetrisGame();
                    mw.setVisible(true);

                    mw.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent arg0) {

                            if (mw.stopAll())//This stops everything that needs to be stopped and returns TRUE if they won the game!
                            {

                                Database.setCurrentLevelByPasscode(mf.activeClerksPasscode, 2);
                                updateScore(2, 500);//Part 2 cleared, Max possible is 500
                            }
                            else
                            {
                                //Also make sure to lock them out if they lost..
                                Database.insertEggLockout(LocalDate.now(), mf.activeClerksPasscode);
                            }
                            mw.setVisible(false);
                            tetrisIsRunning = false;
                            mf.setEnabled(true);
                            mf.requestFocus();
                            mf.textField.requestFocusInWindow();

                        }

                    });
                }
                break;
            //Jade Key
            case 2:
            {
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();
                Object[] message =
                {//The Terrible Troll Raises His Sword.
                    "The number galactica\nbefore the number magic\nunder the number ultimate\nbeside the number fantastic\nreveals the Jade Key.", field1,

                };
                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "The Jade Key Riddle", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION)
                {
                    if (field1.getText().toUpperCase().contentEquals("75144"))
                    {
                        //TODO: Grant the Jade Key boss.
                        Database.setCurrentLevelByPasscode(mf.activeClerksPasscode, 3);
                        updateScore(3, 1000);//Part 3 cleared, Max possible is 1000
                        System.out.println("Jade Key Revealed!!");
                    }
                    else
                    {
                        Database.insertEggLockout(LocalDate.now(), mf.activeClerksPasscode);
                        //TODO: LOCKOUT!
                    }
                }
                break;
            }
            //Jade Gate
            case 3:
                break;
            //Crystal Key
            case 4:
            {
                JFrame message1 = new JFrame("");
                JFrame textInputFrame = new JFrame("");
                JTextField field1 = new JTextField();
                Object[] message =
                {//The Terrible Troll Raises His Sword.
                    "Login: ", field1,

                };
                field1.addAncestorListener(new RequestFocusListener());
                int option = JOptionPane.showConfirmDialog(textInputFrame, message, "The Crystal Key - Shall we play a game?", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION)
                {
                    if (field1.getText().toUpperCase().contentEquals("STARBUCK"))
                    {
                        //TODO: Grant the Crystal Key boss.
                        Database.setCurrentLevelByPasscode(mf.activeClerksPasscode, 5);
                        System.out.println("Crystal Key Revealed!!");
                    }
                    else
                    {
                        Database.insertEggLockout(LocalDate.now(), mf.activeClerksPasscode);
                    }
                }
                break;
            }
            //Crystal Gate
            case 5:
                if (!pacmanIsRunning)
                {
                    pacmanIsRunning = true;
                    mf.setEnabled(false);
                    PacmanGame mw = new PacmanGame();
                    mw.setVisible(true);

                    mw.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent arg0) {

                            //TODO: Need to get the data from the game here, window closed so must be time to update stuff...
                            //Also make sure to lock them out if they lost..
                            mw.myMaze.windowClosed();
                            mw.setVisible(false);
                            pacmanIsRunning = false;
                            mf.setEnabled(true);
                            mf.requestFocus();
                            mf.textField.requestFocusInWindow();

                        }

                    });
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //if they already have found this part maybe just load it at login and set visible?? so ignore this part for them?
        // System.out.println(e.getKeyChar());

        if (activeUser && mf.displayActive)
        {
            if (code.checkKonami(e.getKeyCode()))
            {

                System.out.println("Welcome Player One!!");
                if (isBackdoorUser())//This is the backdoor for Hollie and winners.
                {
                    beginBackdoor();
                }
                else if (Database.getEggLevelByPasscode(mf.activeClerksPasscode) == 0)
                {
                    //Copper Key Cleared! If they don't have it, give it to them!
                    Database.setCurrentLevelByPasscode(mf.activeClerksPasscode, 1);
                    updateScore(1, 100);//Key 1 cleared, Max possible is 100
                }
                if (!isBackdoorUser())
                {//if this isn't a backdoor access show the normal Ready Player One button.
                    button.setVisible(true);
                }

            }
        }

    }

    public void beginBackdoor() {
        tetrisButton.setVisible(true);
        galagaButton.setVisible(true);
        pacmanButton.setVisible(true);
        selectThemeButton.setVisible(true);

    }

    @Override
    public void keyReleased(KeyEvent e) {//System.out.println(e.getKeyChar());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println(e.getKeyChar());

    }

    private boolean isBackdoorUser() {
        int currentTier = Database.getEggLevelByPasscode(mf.activeClerksPasscode);
        return (mf.activeClerksPasscode == 8 || currentTier == 6);
    }

    private void updateScore(int gateNumber, int maxPoints) {
        int numCleared = Database.getNumberOfEmployeesWhoClearedGateSelfIncluded(gateNumber);
        int pointsToAdd;

        switch (numCleared)
        {
            case 1:
                pointsToAdd = maxPoints;
                break;
            case 2:
                pointsToAdd = maxPoints - maxPoints / 10;
                break;
            case 3:
                pointsToAdd = maxPoints - maxPoints / 10 * 2;
                break;
            case 4:
                pointsToAdd = maxPoints - maxPoints / 10 * 3;
                break;
            case 5:
                pointsToAdd = maxPoints - maxPoints / 10 * 4;
                break;
            default:
                pointsToAdd = maxPoints / 2;
                break;
        }

        Database.updateScore(mf.activeClerksPasscode, pointsToAdd);//first to clear gets 100 points. -10 down to 5 ppl then stays 50.
    }

    private boolean validateInteger(String integer) {
        try
        {
            int integ = Integer.parseInt(integer);
            if (integ < 0)
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            return false;
        }//end catch
        return true;
    }
}
