package pacman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PacmanGame extends JFrame {

    private static Object lock = new Object();
    private int widthHeight = 50;//default 50
    private int numCol = 6;//min 4 default 6
    private int numRow = 14;//min 4 default 14
    private int difficulty = 0;
    public boolean running = true;
    private JLabel pacmanLogo;
    private JLabel controls;
    public Maze myMaze;

    public PacmanGame() {
        initUI();

    }

    public boolean gameWon()
    {
        return myMaze.gameWon;
    }
    
    private void initUI() {
        //setVisible(true);

        this.setBackground(Color.DARK_GRAY);
        this.setResizable(false);

        controls = new JLabel();

        controls.setLocation(850, 200);
        controls.setVisible(true);
        controls.setSize(481, 491);
        try
        {
            Image img = ImageIO.read(getClass().getResource("images/pacman_directions.png"));
            controls.setIcon(new ImageIcon(img));
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        
        pacmanLogo = new JLabel();
        pacmanLogo.setLocation(400, 0);
        pacmanLogo.setVisible(true);
        pacmanLogo.setSize(200, 100);
        try
        {
            Image img = ImageIO.read(getClass().getResource("images/PacmanLogo.png"));
            pacmanLogo.setIcon(new ImageIcon(img));
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

        JPanel background = new JPanel(new BorderLayout());
        background.setSize(1400, 1000);
        background.setLayout(null);
        background.setBackground(Color.DARK_GRAY);
        myMaze = new Maze(numCol, numRow, difficulty, widthHeight, this);
        background.add(myMaze);
        this.setLayout(null);
        myMaze.setSize(600, 1000);
        background.add(pacmanLogo);
        background.add(controls);
        add(background);
        
        setSize(1400, 1000);

        setTitle("Pac-Man");

        this.setLocationRelativeTo(null);

    }
}
