package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**

 @author holliefuller
 */
public class PacmanChar implements Subject, ActionListener {

    Timer powerUpT = new Timer(8000, this);//repeat every 8 seconds
    private ArrayList<Observer> observers = new ArrayList<Observer>();
    private int pacX;//pacmans current x coordinate on maze
    private int pacY;//pacmans current y coordinate on maze
    private int currentImageIndex;//pacmans current png index
    private int widthHeight;
    private int xBlocks;//number of total X cells
    private int yBlocks;//number of total Y cells
    private int totalCakeSlices;//number of cake slices remaining
    private int totalPowerUps;//number of power ups remaining
    private int totalPoints;//total Number of Points
    private char currentDirection;//pacmans current heading, North, South, East, West
    private char nextDirection = 'X';
    private ArrayList<Cell> maze;//maze reference for location knowledge
    private boolean isPoweredUp = false;//is Pacman currently Powered Up?
    private Image[] pacmanGifsEAST = new Image[10];
    private Image[] pacmanGifsWEST = new Image[10];
    private Image[] pacmanGifsNORTH = new Image[10];
    private Image[] pacmanGifsSOUTH = new Image[10];
    private Image[] pacmanPoweredGifsEAST = new Image[10];
    private Image[] pacmanPoweredGifsWEST = new Image[10];
    private Image[] pacmanPoweredGifsNORTH = new Image[10];
    private Image[] pacmanPoweredGifsSOUTH = new Image[10];
    private Content contConsumed;//What did you eat?!?!?!
    private int numLives;
    private int noContentCntr = 0;

    Timer deathDelay = new Timer(4000, this);//start delay timer
    private boolean isDead = false;
    private GameSounds sound;

    PacmanChar(int xCoord, int yCoord, ArrayList<Cell> maze, int widthHeight, int xBlocks, int yBlocks, GameSounds sound, int numLives, int totalPoints) {
        this.pacX = xCoord;
        this.pacY = yCoord;
        this.currentImageIndex = 0;
        this.widthHeight = widthHeight;
        this.maze = maze;
        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        this.sound = sound;
        loadImageArray();
        totalCakeSlices = xBlocks * 2 * yBlocks - 12;
        totalPowerUps = 4;
        this.numLives = numLives;
        this.totalPoints = totalPoints;
        powerUpT.setInitialDelay(8000);//8 seconds total powerup time
    }//end PacmanChar ctor

    private void loadImageArray() {
        ImageIcon east1 = createImageIcon("images/Pacman_East_CLOSED.png", "PmEastFrame1");
        pacmanGifsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/Pacman_East_HALFO.png", "PmEastFrame2");
        pacmanGifsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/Pacman_East_FULLO.png", "PmEastFrame3");
        pacmanGifsEAST[2] = east3.getImage();
        pacmanGifsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/PacMan_West_CLOSED.png", "PmWestFrame1");
        pacmanGifsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/Pacman_West_HALFO.png", "PmWestFrame2");
        pacmanGifsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/Pacman_West_FULLO.png", "PmWestFrame3");
        pacmanGifsWEST[2] = west3.getImage();
        pacmanGifsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/Pacman_South_CLOSED.png", "PmSouthFrame1");
        pacmanGifsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/Pacman_South_HALFO.png", "PmSouthFrame2");
        pacmanGifsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/Pacman_South_FULLO.png", "PmSouthFrame3");
        pacmanGifsSOUTH[2] = south3.getImage();
        pacmanGifsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/Pacman_North_CLOSED.png", "PmNorthFrame1");
        pacmanGifsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/Pacman_North_HALFO.png", "PmNorthFrame2");
        pacmanGifsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/Pacman_North_FULLO.png", "PmNorthFrame3");
        pacmanGifsNORTH[2] = north3.getImage();
        pacmanGifsNORTH[3] = north2.getImage();

        //POWERED UP LOADS
        ImageIcon east1p = createImageIcon("images/MawManStartEAST.png", "PmEastFrame1");
        pacmanPoweredGifsEAST[0] = (east1p.getImage());
        ImageIcon east2p = createImageIcon("images/MawManMidEAST.png", "PmEastFrame2");
        pacmanPoweredGifsEAST[1] = east2p.getImage();
        ImageIcon east3p = createImageIcon("images/MawManEAST.png", "PmEastFrame3");
        pacmanPoweredGifsEAST[2] = east3p.getImage();
        pacmanPoweredGifsEAST[3] = east2p.getImage();

        ImageIcon west1p = createImageIcon("images/MawManStartWEST.png", "PmWestFrame1");
        pacmanPoweredGifsWEST[0] = (west1p.getImage());
        ImageIcon west2p = createImageIcon("images/MawManMidWEST.png", "PmWestFrame2");
        pacmanPoweredGifsWEST[1] = west2p.getImage();
        ImageIcon west3p = createImageIcon("images/MawManWEST.png", "PmWestFrame3");
        pacmanPoweredGifsWEST[2] = west3p.getImage();
        pacmanPoweredGifsWEST[3] = west2p.getImage();

        ImageIcon south1p = createImageIcon("images/MawManStartSOUTH.png", "PmSouthFrame1");
        pacmanPoweredGifsSOUTH[0] = (south1p.getImage());
        ImageIcon south2p = createImageIcon("images/MawManMidSOUTH.png", "PmSouthFrame2");
        pacmanPoweredGifsSOUTH[1] = south2p.getImage();
        ImageIcon south3p = createImageIcon("images/MawManSOUTH.png", "PmSouthFrame3");
        pacmanPoweredGifsSOUTH[2] = south3p.getImage();
        pacmanPoweredGifsSOUTH[3] = south2p.getImage();

        ImageIcon north1p = createImageIcon("images/MawManStartNORTH.png", "PmNorthFrame1");
        pacmanPoweredGifsNORTH[0] = (north1p.getImage());
        ImageIcon north2p = createImageIcon("images/MawManMidNORTH.png", "PmNorthFrame2");
        pacmanPoweredGifsNORTH[1] = north2p.getImage();
        ImageIcon north3p = createImageIcon("images/MawManNORTH.png", "PmNorthFrame3");
        pacmanPoweredGifsNORTH[2] = north3p.getImage();
        pacmanPoweredGifsNORTH[3] = north2p.getImage();
    }//end loadImageArray

    public void draw(Graphics g, Graphics2D g2d) {
        g2d = (Graphics2D) g;

        if (getCurImg() != null)
        {
            /*
            switch (currentDirection)
            {
                
                case 'S':
                    //g2d.drawImage(getCurImg(), pacX * widthHeight + 4, pacY * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), pacX , pacY, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'W':
                    g2d.drawImage(getCurImg(), pacX * widthHeight + 4, pacY * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'E':
                    g2d.drawImage(getCurImg(), pacX * widthHeight + 4, pacY * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'N':
                    g2d.drawImage(getCurImg(), pacX * widthHeight + 4, pacY * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                default:
                    g2d.drawImage(getCurImg(), pacX * widthHeight + 4, pacY * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
            }
             */
            g2d.drawImage(getCurImg(), pacX + 4, pacY + 4, widthHeight - 8, widthHeight - 8, null);
        }
    }//end draw

    public void timerTriggered() {
        if (!isDead)
        {
            if (nextDirection != 'X')
            {

                if (!checkWall(nextDirection, pacX, pacY, true))
                {
                    currentDirection = nextDirection;
                    nextDirection = 'X';//on success
                }

            }
            int pointsGained = 0;

            //if (maze.get(xBlocks * 2 * pacY + pacX).isEastSolid() && currentDirection == 'E')
            if (currentDirection == 'E' && checkWall('E', pacX, pacY, false))
            {

                return;
            }
            //else if (maze.get(xBlocks * 2 * pacY + pacX).isWestSolid() && currentDirection == 'W')
            else if (currentDirection == 'W' && checkWall('W', pacX, pacY, false))
            {

                return;
            }
            //else if (maze.get(xBlocks * 2 * pacY + pacX).isNorthSolid() && currentDirection == 'N')
            else if (currentDirection == 'N' && checkWall('N', pacX, pacY, false))
            {

                return;
            }
            //else if (maze.get(xBlocks * 2 * pacY + pacX).isSouthSolid() && currentDirection == 'S')
            else if (currentDirection == 'S' && checkWall('S', pacX, pacY, false))
            {
                return;
            }

            switch (currentDirection)
            {
                //else if (currentDirection == 'S' && pacY * xBlocks * 2 + pacX != xBlocks * yBlocks - xBlocks * 3 && pacY * xBlocks * 2 + pacX != xBlocks * yBlocks - xBlocks * 3 - 1)
                case 'N':
                    pacY -= 10;
                    // contConsumed = maze.get(xBlocks * 2 * pacY + pacX).getContent();
                    contConsumed = tryConsumeContent();
                    incImg();
                    notifyObservers();
                    //Keeps the yellow guy from entering jail
                    break;
                case 'S':
                    //animate south with next img frame

                    pacY += 10;
                    contConsumed = tryConsumeContent();
                    //contConsumed = maze.get(xBlocks * 2 * pacY + pacX).getContent();//he just ate something
                    incImg();
                    notifyObservers();
                    break;
                case 'W':
                    if (pacX < 0)
                    {
                        pacX = (xBlocks * 2 - 1) * widthHeight;
                    }
                    else
                    {
                        pacX -= 10;
                    }
                    contConsumed = tryConsumeContent();
                    //contConsumed = maze.get(xBlocks * 2 * pacY + pacX).getContent();
                    incImg();
                    notifyObservers();
                    break;
                case 'E':
                    //if (pacX == xBlocks * 2 - 1)
                    //{
                    //    pacX = 0;
                    //}
                    if (pacX > (xBlocks * 2 - 1) * widthHeight)
                    {
                        pacX = 0;
                    }
                    else
                    {
                        pacX += 10;
                    }
                    contConsumed = tryConsumeContent();
                    // contConsumed = maze.get(xBlocks * 2 * pacY + pacX).getContent();
                    incImg();
                    notifyObservers();
                    break;
                default:
                    break;
            }

            if (contConsumed != null && contConsumed.getContentType().equals(ContentType.Cake) && contConsumed.isVisible())
            {
                isPoweredUp = true;//Ooo someone just ate a whole cake...it must be all the sugar...
                contConsumed.consumeContent();
                powerUpT.restart();
                totalPowerUps--;
                pointsGained = contConsumed.getPointValue();
                sound.eatFruitMusic();
            }
            else if (contConsumed != null && contConsumed.getContentType().equals(ContentType.CakeSlice) && contConsumed.isVisible())
            {
                contConsumed.consumeContent();
                totalCakeSlices--;
                pointsGained = contConsumed.getPointValue();

                sound.eatCakeSlice();
                noContentCntr = 0;
            }
            else if (contConsumed == null)
            {

            }
            contConsumed = null;

            totalPoints += pointsGained;
        }
    }

    public int remainingContent() {
        return totalCakeSlices + totalPowerUps;
    }

    public Image getCurImg() {
        if (isPoweredUp)
        {   //loads powered up pacman pngs
            switch (currentDirection)
            {
                case 'N':
                    return pacmanPoweredGifsNORTH[currentImageIndex];
                case 'S':
                    return pacmanPoweredGifsSOUTH[currentImageIndex];
                case 'W':
                    return pacmanPoweredGifsWEST[currentImageIndex];
                default:
                    return pacmanPoweredGifsEAST[currentImageIndex];
            }
        }
        else
        {   //loads normal pacman pngs
            switch (currentDirection)
            {
                case 'N':
                    return pacmanGifsNORTH[currentImageIndex];
                case 'S':
                    return pacmanGifsSOUTH[currentImageIndex];
                case 'W':
                    return pacmanGifsWEST[currentImageIndex];
                default:
                    return pacmanGifsEAST[currentImageIndex];
            }
        }
    }

    public void incImg() {
        if (currentImageIndex == 3)
        {
            currentImageIndex = 0;
        }
        else
        {
            currentImageIndex++;
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
    }//end createImageIcon

    public void setXcoord(int xCoord) {
        pacX = xCoord;
    }//end setXcoord

    public void setYcoord(int yCoord) {
        pacY = yCoord;
    }//end setYcoord

    public int getXcoord() {
        return pacX;
    }//end getXcoord

    public int getYcoord() {
        return pacY;
    }//end getYCoord

    public boolean isPoweredUp() {
        return isPoweredUp;
    }//end isPoweredUp

    public void setPoweredUp(boolean powerStatus) {
        isPoweredUp = powerStatus;
    }//end setPoweredUp

    public char getCurrentDirection() {
        return currentDirection;
    }//end getCurrentDirection

    public void setCurrentDirection(char currentDirection) {
        if (Character.toUpperCase(currentDirection) == 'N' || Character.toUpperCase(currentDirection) == 'S' || Character.toUpperCase(currentDirection) == 'E' || Character.toUpperCase(currentDirection) == 'W')
        {
            this.currentDirection = currentDirection;
        }
    }//end setCurrentDirection

    public int getTotalScore() {
        return totalPoints;
    }

    public int getRemainingLives() {
        return numLives;
    }

    @Override
    public void register(Observer obj) {
        if (!observers.contains(obj))//make sure observer is not already added
        {
            observers.add(obj);
        }
    }

    @Override
    public void unregister(Observer obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
        for (Observer obj : observers)
        {
            obj.update();
        }
    }

    @Override
    public Object getUpdate(Observer obj) {
        return this;
    }

    public boolean makeContact() {
        if (isPoweredUp)
        {
            totalPoints += 200;//you get points for eating ghosts
            return true;//ghost dies 
        }
        else
        {
            pacX = 4 * widthHeight;//restore pacman to start position
            pacY = 8 * widthHeight;
            currentDirection = ' ';

            numLives--;//remove a life
            isDead = true;
            deathDelay.start();
            sound.pacmanDeathMusic();
            notifyObservers();

            return false;
        }
    }//end makeContact

    public boolean isDead() {
        return isDead;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(powerUpT))
        {
            isPoweredUp = false;
            powerUpT.stop();
        }
        if (e.getSource().equals(deathDelay) && numLives > 0)
        {
            isDead = false; //I AM BACK!
            
            deathDelay.stop();
            notifyObservers();
            sound.loopMainGhostMusic();

        }
    }//end actionPerformed

    public Content tryConsumeContent() {
        for (Cell cell : maze)
        {
            if (cell.getXLoc() * widthHeight == pacX && cell.getYLoc() * widthHeight == pacY)
            {
                return cell.getContent();
            }
        }
        return null;
    }

    public boolean checkWall(char wallToCheck, int x, int y, boolean userKeyEvent) {
        for (Cell cell : maze)
        {
            if (cell.getXLoc() * widthHeight == x && cell.getYLoc() * widthHeight == y)
            {

                if (wallToCheck == 'N' && cell.isNorthSolid())
                {

                    return true;

                }
                else if (wallToCheck == 'S' && cell.isSouthSolid() || (wallToCheck == 'S' && (cell.getXLoc() == 5 || cell.getXLoc() == 6) && cell.getYLoc() == 5))
                {

                    return true;
                }
                else if (wallToCheck == 'E' && cell.isEastSolid())
                {
                    return true;
                }
                else if (wallToCheck == 'W' && cell.isWestSolid())
                {

                    return true;

                }
                else
                {
                    return false;
                }
            }
        }

        //This point can be reached IF the cell didn't match. Keyevent should trigger true, otherwise false.
        if (userKeyEvent)
        {
            return true;//couldn't even find the cell.
        }
        return false;
    }

    public void setNextDirection(char nextDirection) {
        this.nextDirection = nextDirection;
    }

}//end PacmanChar Class
