package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**

 @author holliefuller
 */
public abstract class Ghost implements Observer {

    protected Timer modeTimer;
    protected Mode currentMode;
    protected Mode requestedMode;
    protected int chaseTimeMS;
    protected int scatterTimeMS;
    protected Random rand = new Random();//used to generate random numbers
    protected int xCoord;//ghost current x Coordinate on Maze
    protected int yCoord;//ghosts current y Cordinate on Maze
    protected int widthHeight;//size of a cell 
    protected int xBlocks;//number of X cells in maze
    protected int yBlocks;//number of Y cells in maze
    protected int subXcoord;//subjects current X coord
    protected int subYcoord;//subjects current Y coord
    protected int xOffset, yOffset;//offset for the actual position(GUI related only)
    protected int offsetCntr;
    protected int currentTargetX;
    protected int currentTargetY;
    protected char currentDirection;//current direction ghost is looking
    protected boolean death = false;
    protected ArrayList<Cell> maze;//current Maze ghost is inside of
    protected int curImgIndex;//used to set the current index of the ghosts img array
    protected PacmanChar pacman;
    protected GameSounds sounds;
    protected Image[] zedPngs = new Image[10];//Ghost on the run images.
    protected Image[] deadPngs = new Image[10];//Ghost Dead images.
    protected ArrayList<Cell> currentPath;
    protected ArrayList<Cell> tempPath;
    protected ArrayList<Cell> queue;
    protected boolean findNextPath = false;
    protected Cell startCell;

    //protected int startxCoord;//These are the base coordinates. Where it begins...
    //protected int startyCoord;
    public Ghost(int xCoord, int yCoord, ArrayList<Cell> maze, int widthHeight, int xBlocks, int yBlocks, PacmanChar pacman, GameSounds sounds, int chaseTimeMS, int scatterTimeMS) {
        this.currentMode = Mode.SCATTER;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        //this.startxCoord = xCoord;
        //this.startyCoord = yCoord;
        this.maze = maze;
        this.widthHeight = widthHeight;
        this.currentDirection = 'N';//default
        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        this.pacman = pacman;
        this.sounds = sounds;
        subXcoord = 0;
        subYcoord = 0;
        curImgIndex = 0;//default
        this.currentTargetX = xCoord;
        this.currentTargetY = yCoord;
        this.chaseTimeMS = chaseTimeMS;
        this.scatterTimeMS = scatterTimeMS;

        for (Cell cell : maze)
        {
            if (xCoord / widthHeight == cell.getXLoc() && yCoord / widthHeight == cell.getYLoc())
            {
                startCell = cell;
                break;
            }
        }
        loadImageArray();
    }

    public void resetPosition() {

    }

    public Image getCurrentZedImage() {

        return zedPngs[curImgIndex];

    }

    public Image getCurrentDeadImage() {

        return deadPngs[curImgIndex];

    }

    abstract public void draw(Graphics g, Graphics2D g2d);

    public ImageIcon createImageIcon(String path, String description) {
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
    }//end createImageIcon

    private void loadImageArray() {
        ImageIcon east1 = createImageIcon("images/zed.png", "Zed");
        zedPngs[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/zedalt.png", "ZedAlt");
        zedPngs[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/zedalt2.png", "ZedAlt2");
        zedPngs[2] = east3.getImage();
        zedPngs[3] = east2.getImage();

        ImageIcon dead1 = createImageIcon("images/ghostdeath.png", "Dead");
        deadPngs[0] = (dead1.getImage());
        ImageIcon dead2 = createImageIcon("images/ghostdeathalt.png", "DeadAlt");
        deadPngs[1] = dead2.getImage();
        ImageIcon dead3 = createImageIcon("images/ghostdeathalt2.png", "DeadAlt2");
        deadPngs[2] = dead3.getImage();
        deadPngs[3] = dead2.getImage();

    }//end loadImageArray

    public void checkContact() {
        if (!death)
        {
            subYcoord = pacman.getYcoord();
            subXcoord = pacman.getXcoord();
            if (yCoord == subYcoord && xCoord == subXcoord)
            {
                pacman.makeContact();
            }
        }
    }
}
