package database_console;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**

 @author R-Mule
 */
public class LeaderBoard {

    private ArrayList<String> employees;
    private ArrayList<String> leaderNames;
    private ArrayList<Integer> leaderScores;
    private ArrayList<Integer> pids;
    private JFrame frame;
    private JButton button;
    private MainFrame mf;

    public LeaderBoard(MainFrame mf) {
        this.mf = mf;
        frame = new JFrame("Leaderboard");
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(1000, 600);
        frame.setLocation(350, 200);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(false);
        button = new JButton("");
        button.setSize(5, 5);
        button.setVisible(true);
        button.setLocation(0, 0);
        button.setBackground(Color.WHITE);

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //  try
                //  {
                frame = new JFrame("Leaderboard");
                frame.setLayout(null);
                frame.setResizable(false);
                frame.setSize(1000, 600);
                frame.setLocation(350, 200);
                frame.setAlwaysOnTop(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //BufferedImage myImage = ImageIO.read(new File("images\\anaorak_.jpg"));
                //JFrame myJFrame = new JFrame("Image pane");
                //myJFrame.setContentPane(new ImagePanel(myImage));
                //frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("C:\\Users\\asmit\\Downloads\\test1.gif")))));
                //frame.setBackground(Color.BLACK);
                frame.getContentPane().setBackground(Color.BLACK);
                // frame.setBackground(Color.BLACK);
                loadEverything();
                frame.setVisible(true);
                mf.textField.requestFocusInWindow();
                //  }
                //  catch (IOException ex)
                //  {
                //  Logger.getLogger(LeaderBoard.class.getName()).log(Level.SEVERE, null, ex);
                //  }
            }
        });

        this.mf.add(button);

    }

    private void loadEverything() {
        employees = Database.getEmployeesSortByScore();
        leaderNames = new ArrayList<>();
        leaderScores = new ArrayList<>();
        pids = new ArrayList<>();

        int cntr = 0;
        for (String employee : employees)
        {
            if (cntr == 10)//only ever display top 10.
            {
                break;
            }
            int pid = Integer.parseInt(employee.substring(0, employee.indexOf('#')));

            String empLastNameInital = employee.substring(employee.indexOf('#') + 1, employee.indexOf('#') + 2) + ".";
            String empName = employee.substring(employee.indexOf(',') + 2, employee.indexOf(':'));
            int currentScore = Integer.parseInt(employee.substring(employee.indexOf(':') + 2));
            // System.out.println("Name: "+  empName  +empLastNameInital + "Score: " + currentScore);

            if (currentScore > 0)
            {
                leaderNames.add(empName + empLastNameInital);
                leaderScores.add(currentScore);
                pids.add(pid);
                cntr++;
            }

        }
        int customNames = 0;
        for (int i = 0; i < 10; i++)
        {

            if (leaderNames.size() == i)
            {
                switch (customNames)
                {
                    case 0:
                        leaderNames.add("Apollo");
                        break;
                    case 1:
                        leaderNames.add("Plum");
                        break;
                    case 2:
                        leaderNames.add("Wrynn");
                        break;
                    case 3:
                        leaderNames.add("Mudkip");
                        break;
                    case 4:
                        leaderNames.add("Parzival");
                        break;
                    case 5:
                        leaderNames.add("Art3mis");
                        break;
                    case 6:
                        leaderNames.add("Aech");
                        break;
                    case 7:
                        leaderNames.add("Daito");
                        break;
                    case 8:
                        leaderNames.add("Sho");
                        break;
                    default:
                        leaderNames.add("JDH");
                        break;
                }
                customNames++;
                //leaderNames.add("ATS");
                int temp = 0;//for some dumb reason java sees 0 as int when used directly...
                leaderScores.add(temp);
                pids.add(-1);
            }
        }

        //at this point everything is loaded up!
        int posCntr = 0;
        for (String name : leaderNames)
        {
            JLabel field1 = new JLabel();
            field1.setSize(300, 50);
            field1.setLocation(0, 0 + posCntr);
            field1.setFont(new Font("", Font.PLAIN, 30));
            field1.setText(name + " : " + leaderScores.get(leaderNames.indexOf(name)));
            field1.setForeground(Color.WHITE);
            frame.add(field1);
            System.out.println(name + " : " + leaderScores.get(leaderNames.indexOf(name)));

            //load images for leaders...
            loadImages(pids.get(leaderNames.indexOf(name)), 0 + 300, posCntr);
            posCntr += 50;
        }

        JLabel considerText = new JLabel();
        considerText.setSize(900, 50);
        considerText.setLocation(100, 500);
        considerText.setFont(new Font("Monotype Corsiva", Font.BOLD, 28));
        considerText.setForeground(Color.WHITE);
        considerText.setText("To play this game, you must first consider yourself a hero...");
        considerText.setVisible(true);
        frame.add(considerText);
    }

    private void loadImages(int pid, int curX, int curY) {
        //this is where I will load default images for each employee based on keys cleared..
        //get results for employee PID
        //load proper images for said results
        int level = Database.getEggLevelByPID(pid);
        switch (level)
        {
            case 0:
            {
                //Load all dummy keys.
                JLabel copperKey = new JLabel(createImageIcon("images\\CopperKeyFade.png", "copperKeyFade"));
                copperKey.setVisible(true);
                copperKey.setLocation(curX, curY);
                copperKey.setSize(100, 50);
                frame.add(copperKey);
                JLabel copperGate = new JLabel(createImageIcon("images/CopperGateFade.png", "CopperGateFade"));
                copperGate.setVisible(true);
                copperGate.setLocation(curX + 100, curY);
                copperGate.setSize(100, 50);
                frame.add(copperGate);
                JLabel jadeKey = new JLabel(createImageIcon("images/JadeKeyFade.png", "JadeKeyFade"));
                jadeKey.setVisible(true);
                jadeKey.setLocation(curX + 200, curY);
                jadeKey.setSize(100, 50);
                frame.add(jadeKey);
                JLabel jadeGate = new JLabel(createImageIcon("images/JadeGateFade.png", "JadeGateFade"));
                jadeGate.setVisible(true);
                jadeGate.setLocation(curX + 300, curY);
                jadeGate.setSize(100, 50);
                frame.add(jadeGate);
                JLabel crystalKey = new JLabel(createImageIcon("images/CrystalKeyFade.png", "CrystalKeyFade"));
                crystalKey.setVisible(true);
                crystalKey.setLocation(curX + 400, curY);
                crystalKey.setSize(100, 50);
                frame.add(crystalKey);
                JLabel crystalGate = new JLabel(createImageIcon("images/CrystalGateFade.png", "CrystalGateFade"));
                crystalGate.setVisible(true);
                crystalGate.setLocation(curX + 500, curY);
                crystalGate.setSize(100, 50);
                frame.add(crystalGate);
                break;
            }
            case 1:
            {
                JLabel copperKey = new JLabel(createImageIcon("images/CopperKey.png", "CopperKey"));
                copperKey.setVisible(true);
                copperKey.setLocation(curX, curY);
                copperKey.setSize(100, 50);
                frame.add(copperKey);
                JLabel copperGate = new JLabel(createImageIcon("images/CopperGateFade.png", "CopperGateFade"));
                copperGate.setVisible(true);
                copperGate.setLocation(curX + 100, curY);
                copperGate.setSize(100, 50);
                frame.add(copperGate);
                JLabel jadeKey = new JLabel(createImageIcon("images/JadeKeyFade.png", "JadeKeyFade"));
                jadeKey.setVisible(true);
                jadeKey.setLocation(curX + 200, curY);
                jadeKey.setSize(100, 50);
                frame.add(jadeKey);
                JLabel jadeGate = new JLabel(createImageIcon("images/JadeGateFade.png", "JadeGateFade"));
                jadeGate.setVisible(true);
                jadeGate.setLocation(curX + 300, curY);
                jadeGate.setSize(100, 50);
                frame.add(jadeGate);
                JLabel crystalKey = new JLabel(createImageIcon("images/CrystalKeyFade.png", "CrystalKeyFade"));
                crystalKey.setVisible(true);
                crystalKey.setLocation(curX + 400, curY);
                crystalKey.setSize(100, 50);
                frame.add(crystalKey);
                JLabel crystalGate = new JLabel(createImageIcon("images/CrystalGateFade.png", "CrystalGateFade"));
                crystalGate.setVisible(true);
                crystalGate.setLocation(curX + 500, curY);
                crystalGate.setSize(100, 50);
                frame.add(crystalGate);
                break;
            }
            case 2:
            {
                JLabel copperKey = new JLabel(createImageIcon("images/CopperKey.png", "CopperKey"));
                copperKey.setVisible(true);
                copperKey.setLocation(curX, curY);
                copperKey.setSize(100, 50);
                frame.add(copperKey);
                JLabel copperGate = new JLabel(createImageIcon("images/CopperGate.png", "CopperGate"));
                copperGate.setVisible(true);
                copperGate.setLocation(curX + 100, curY);
                copperGate.setSize(100, 50);
                frame.add(copperGate);
                JLabel jadeKey = new JLabel(createImageIcon("images/JadeKeyFade.png", "JadeKeyFade"));
                jadeKey.setVisible(true);
                jadeKey.setLocation(curX + 200, curY);
                jadeKey.setSize(100, 50);
                frame.add(jadeKey);
                JLabel jadeGate = new JLabel(createImageIcon("images/JadeGateFade.png", "JadeGateFade"));
                jadeGate.setVisible(true);
                jadeGate.setLocation(curX + 300, curY);
                jadeGate.setSize(100, 50);
                frame.add(jadeGate);
                JLabel crystalKey = new JLabel(createImageIcon("images/CrystalKeyFade.png", "CrystalKeyFade"));
                crystalKey.setVisible(true);
                crystalKey.setLocation(curX + 400, curY);
                crystalKey.setSize(100, 50);
                frame.add(crystalKey);
                JLabel crystalGate = new JLabel(createImageIcon("images/CrystalGateFade.png", "CrystalGateFade"));
                crystalGate.setVisible(true);
                crystalGate.setLocation(curX + 500, curY);
                crystalGate.setSize(100, 50);
                frame.add(crystalGate);
                break;
            }
            case 3:
            {
                JLabel copperKey = new JLabel(createImageIcon("images/CopperKey.png", "CopperKey"));
                copperKey.setVisible(true);
                copperKey.setLocation(curX, curY);
                copperKey.setSize(100, 50);
                frame.add(copperKey);
                JLabel copperGate = new JLabel(createImageIcon("images/CopperGate.png", "CopperGate"));
                copperGate.setVisible(true);
                copperGate.setLocation(curX + 100, curY);
                copperGate.setSize(100, 50);
                frame.add(copperGate);
                JLabel jadeKey = new JLabel(createImageIcon("images/JadeKey.png", "JadeKey"));
                jadeKey.setVisible(true);
                jadeKey.setLocation(curX + 200, curY);
                jadeKey.setSize(100, 50);
                frame.add(jadeKey);
                JLabel jadeGate = new JLabel(createImageIcon("images/JadeGateFade.png", "JadeGateFade"));
                jadeGate.setVisible(true);
                jadeGate.setLocation(curX + 300, curY);
                jadeGate.setSize(100, 50);
                frame.add(jadeGate);
                JLabel crystalKey = new JLabel(createImageIcon("images/CrystalKeyFade.png", "CrystalKeyFade"));
                crystalKey.setVisible(true);
                crystalKey.setLocation(curX + 400, curY);
                crystalKey.setSize(100, 50);
                frame.add(crystalKey);
                JLabel crystalGate = new JLabel(createImageIcon("images/CrystalGateFade.png", "CrystalGateFade"));
                crystalGate.setVisible(true);
                crystalGate.setLocation(curX + 500, curY);
                crystalGate.setSize(100, 50);
                frame.add(crystalGate);
                break;
            }
            case 4:
            {
                JLabel copperKey = new JLabel(createImageIcon("images/CopperKey.png", "CopperKey"));
                copperKey.setVisible(true);
                copperKey.setLocation(curX, curY);
                copperKey.setSize(100, 50);
                frame.add(copperKey);
                JLabel copperGate = new JLabel(createImageIcon("images/CopperGate.png", "CopperGate"));
                copperGate.setVisible(true);
                copperGate.setLocation(curX + 100, curY);
                copperGate.setSize(100, 50);
                frame.add(copperGate);
                JLabel jadeKey = new JLabel(createImageIcon("images/JadeKey.png", "JadeKey"));
                jadeKey.setVisible(true);
                jadeKey.setLocation(curX + 200, curY);
                jadeKey.setSize(100, 50);
                frame.add(jadeKey);
                JLabel jadeGate = new JLabel(createImageIcon("images/JadeGate.png", "JadeGate"));
                jadeGate.setVisible(true);
                jadeGate.setLocation(curX + 300, curY);
                jadeGate.setSize(100, 50);
                frame.add(jadeGate);
                JLabel crystalKey = new JLabel(createImageIcon("images/CrystalKeyFade.png", "CrystalKeyFade"));
                crystalKey.setVisible(true);
                crystalKey.setLocation(curX + 400, curY);
                crystalKey.setSize(100, 50);
                frame.add(crystalKey);
                JLabel crystalGate = new JLabel(createImageIcon("images/CrystalGateFade.png", "CrystalGateFade"));
                crystalGate.setVisible(true);
                crystalGate.setLocation(curX + 500, curY);
                crystalGate.setSize(100, 50);
                frame.add(crystalGate);
                break;
            }
            case 5:
            {
                JLabel copperKey = new JLabel(createImageIcon("images/CopperKey.png", "CopperKey"));
                copperKey.setVisible(true);
                copperKey.setLocation(curX, curY);
                copperKey.setSize(100, 50);
                frame.add(copperKey);
                JLabel copperGate = new JLabel(createImageIcon("images/CopperGate.png", "CopperGate"));
                copperGate.setVisible(true);
                copperGate.setLocation(curX + 100, curY);
                copperGate.setSize(100, 50);
                frame.add(copperGate);
                JLabel jadeKey = new JLabel(createImageIcon("images/JadeKey.png", "JadeKey"));
                jadeKey.setVisible(true);
                jadeKey.setLocation(curX + 200, curY);
                jadeKey.setSize(100, 50);
                frame.add(jadeKey);
                JLabel jadeGate = new JLabel(createImageIcon("images/JadeGate.png", "JadeGate"));
                jadeGate.setVisible(true);
                jadeGate.setLocation(curX + 300, curY);
                jadeGate.setSize(100, 50);
                frame.add(jadeGate);
                JLabel crystalKey = new JLabel(createImageIcon("images/CrystalKey.png", "CrystalKey"));
                crystalKey.setVisible(true);
                crystalKey.setLocation(curX + 400, curY);
                crystalKey.setSize(100, 50);
                frame.add(crystalKey);
                JLabel crystalGate = new JLabel(createImageIcon("images/CrystalGateFade.png", "CrystalGateFade"));
                crystalGate.setVisible(true);
                crystalGate.setLocation(curX + 500, curY);
                crystalGate.setSize(100, 50);
                frame.add(crystalGate);
                break;
            }
            case 6:
            {
                JLabel copperKey = new JLabel(createImageIcon("images/CopperKey.png", "CopperKey"));
                copperKey.setVisible(true);
                copperKey.setLocation(curX, curY);
                copperKey.setSize(100, 50);
                frame.add(copperKey);
                JLabel copperGate = new JLabel(createImageIcon("images/CopperGate.png", "CopperGate"));
                copperGate.setVisible(true);
                copperGate.setLocation(curX + 100, curY);
                copperGate.setSize(100, 50);
                frame.add(copperGate);
                JLabel jadeKey = new JLabel(createImageIcon("images/JadeKey.png", "JadeKey"));
                jadeKey.setVisible(true);
                jadeKey.setLocation(curX + 200, curY);
                jadeKey.setSize(100, 50);
                frame.add(jadeKey);
                JLabel jadeGate = new JLabel(createImageIcon("images/JadeGate.png", "JadeGate"));
                jadeGate.setVisible(true);
                jadeGate.setLocation(curX + 300, curY);
                jadeGate.setSize(100, 50);
                frame.add(jadeGate);
                JLabel crystalKey = new JLabel(createImageIcon("images/CrystalKey.png", "CrystalKey"));
                crystalKey.setVisible(true);
                crystalKey.setLocation(curX + 400, curY);
                crystalKey.setSize(100, 50);
                frame.add(crystalKey);
                JLabel crystalGate = new JLabel(createImageIcon("images/CrystalGate.png", "CrystalGate"));
                crystalGate.setVisible(true);
                crystalGate.setLocation(curX + 500, curY);
                crystalGate.setSize(100, 50);
                frame.add(crystalGate);
                break;
            }
            default:
                break;
        }

    }

    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null)
        {
            return new ImageIcon(imgURL, description);
        }
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
