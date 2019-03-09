package pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**

 @author holliefuller
 */
public class Maze extends JPanel implements KeyListener, ActionListener {

    JLabel scoreLabel = new JLabel("Score: 0");
    private boolean runOnce;
    Font f = new Font("Dialog", Font.BOLD, 20);
    Font f2 = new Font("GameOver", Font.BOLD, 40);
    Font f3 = new Font("System", Font.BOLD, 40);
    private int xBlocks, yBlocks;//number of X and Y Cells
    private int widthHeight;//width and height of a block/cell
    private int difficulty;//set difficulty of current maze, used in draw maze
    private int midwallRandom;//random number for middle walls
    private int levelNum;//level number
    GameSounds gameSounds;//game sounds, which by the way, kill me.  I really don't like them...
    private Random rand = new Random();//random num gen
    Timer startDELAY = new Timer(500, this);//start delay timer
    Timer pacmant1 = new Timer(65, this);//recur every .2 seconds after the initial pacmans main clock
    Timer blinkyt1 = new Timer(70, this);//recur every .19 seconds after the inital blinky delay
    Timer blinkyt2 = new Timer(63, this);//animator
    Timer inkyt1 = new Timer(70, this);//recur every .19 seconds after the inital blinky delay
    Timer inkyt2 = new Timer(63, this);//animator
    Timer pinkyt1 = new Timer(70, this);//recur every .19 seconds after the inital blinky delay
    Timer pinkyt2 = new Timer(63, this);//animator
    Timer clydet1 = new Timer(70, this);//recur every .19 seconds after the inital blinky delay
    Timer clydet2 = new Timer(63, this);//animator
    private JLabel readyLabel = new JLabel("READY!");
    public boolean forceStop = false;
    public boolean gameWon = false;
    private boolean hasEarnedExtraLife = false;//only get one at 10,000 then never again
    
    Graphics2D g2d;
    ArrayList<Cell> mazeCells = new ArrayList<Cell>();//array list of cells used
    ArrayList<Cell> actualMaze = new ArrayList<Cell>();//actual maze
    JFrame jframe;
    PacmanChar pacman;//RELEASE THE YELLOW MAN
    Inky inky;//Blue Ghost
    Pinky pinky;//Pink Ghost
    Blinky blinky;//Red Ghost
    Clyde clyde;//Orange Ghost

    Maze(int xBlocks, int yBlocks, int difficulty, int widthHeight, PacmanGame jframe) {
        //this.setLayout(null);
        this.setLocation(200, 100);

        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        this.widthHeight = widthHeight;
        this.difficulty = difficulty;
        levelNum = 1;//Start at level 1 - because where else would you start?
        for (int k = 0; k < yBlocks; k++)
        {
            for (int j = 0; j < xBlocks; j++)
            {
                if ((k == 0 && j == 0) || (k == yBlocks - 1 && j == 0))
                {
                    mazeCells.add(new Cell(j, k, widthHeight, xBlocks, yBlocks, ContentType.Cake));
                }
                else
                {
                    mazeCells.add(new Cell(j, k, widthHeight, xBlocks, yBlocks, ContentType.CakeSlice));
                }
            }
        }
        this.jframe = jframe;
        this.jframe.addKeyListener(this);

        midwallRandom = rand.nextInt((2) + 1);//generate midwallRandom number between 0 & max node -1
        pacmant1.setInitialDelay(200);//wait for .2 seconds before starting
        blinkyt1.setInitialDelay(190);//wait for .19 seconds before starting
        blinkyt2.setInitialDelay(190);//same wait...
        pinkyt1.setInitialDelay(190);//wait for .19 seconds before starting
        pinkyt2.setInitialDelay(190);//same wait...
        clydet1.setInitialDelay(190);//wait for .19 seconds before starting
        clydet2.setInitialDelay(190);//same wait...
        inkyt1.setInitialDelay(190);//wait for .19 seconds before starting
        inkyt2.setInitialDelay(190);//same wait...
        gameSounds = new GameSounds();//game sounds, which by the way, kill me.  I really don't like them...
        gameSounds.introMusic();
        startDELAY.setInitialDelay(4000);//wait for 4 seconds before starting

        startDELAY.start();//begin startDelay Timer
        createMaze();
        int chaseTime = 10000;
        int scatterTime = 5000;
        //I took the intro music out of here - let the maze be built in silence.  Seriously.
        pacman = new PacmanChar(4 * widthHeight, 8 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, gameSounds, 4, 0,hasEarnedExtraLife);//GENERATE THE YELLOW MAN
        inky = new Inky((xBlocks - 2) * widthHeight, yBlocks / 2 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);//the next four lines generate ghosts
        blinky = new Blinky(5 * widthHeight, 7 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);
        pinky = new Pinky(xBlocks * widthHeight, yBlocks / 2 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);
        clyde = new Clyde((xBlocks + 1) * widthHeight, yBlocks / 2 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);
        pacman.register(clyde);//HEY...HEY OBSERVERS...COME HERE TO GET REGISTERED
        pacman.register(inky);
        pacman.register(pinky);
        pacman.register(blinky);

        runOnce = true;

    }//end Maze ctor

    private void createMaze() {
        dfsMazeCreate mazeCreation = new dfsMazeCreate(mazeCells, xBlocks, yBlocks);

        int midwallRandomNum = rand.nextInt((xBlocks * yBlocks - 1));//generate midwallRandom number between 0 & max node -1
        mazeCreation.createMaze(midwallRandomNum);//create perfect Depth First Search Maze, then ruin that perfection (we have to play after all)

        mirrorMaze();//gen the other maze half
        ghostJail();//<--this thing is a coding nightmare but anyway, there's the ghost jail
        openPortal();

    }

    public void openPortal() {
        //choose portal location randomly...
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((yBlocks - 1 - 1) + 1) + 1;
        for (Cell cell : actualMaze)
        {
            if (cell.getYLoc() == randomNum && cell.getXLoc() == 0)
            {
                cell.setWest(false);

            }
            else if (cell.getYLoc() == randomNum && cell.getXLoc() == xBlocks * 2 - 1)
            {
                cell.setEast(false);
            }
        }
    }

    public Cell getCell(int x, int y) {
        for (Cell cell : actualMaze)
        {
            if (cell.getXLoc() == x && cell.getYLoc() == y)
            {
                return cell;
            }
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setBackground(Color.DARK_GRAY);
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        drawLines(g);
        pinky.draw(g, g2d);
        inky.draw(g, g2d);
        blinky.draw(g, g2d);
        clyde.draw(g, g2d);
        pacman.draw(g, g2d);
        drawScoreBoard(g);
    }

    public void drawScoreBoard(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.setFont(f);
        g2d.drawString("POINTS: " + pacman.getTotalScore(), xBlocks * widthHeight - widthHeight * 2, yBlocks * widthHeight + widthHeight);
        g2d.drawString("LIVES LEFT: " + pacman.getRemainingLives(), xBlocks * widthHeight - widthHeight * 2, yBlocks * widthHeight + widthHeight / 2);
        g2d.drawString("DIFFICULTY: " + levelNum, xBlocks * widthHeight - widthHeight * 2, yBlocks * widthHeight + widthHeight * 2 - widthHeight / 2);
        if (!pacmant1.isRunning() || pacman.isDead())
        {
            readyLabel.setFont(f3);
            readyLabel.setLocation(225, 300);
            readyLabel.setForeground(Color.red);
            readyLabel.setVisible(true);
            this.add(readyLabel);
        }
        else if (readyLabel.isVisible())
        {
            readyLabel.setVisible(false);
        }

        if (pacman.getRemainingLives() <= 0)
        {//oh no, its game over....
            //g2d.setFont(f2);//set larger font
            //g2d.drawString("GAME OVER!", xBlocks * widthHeight - widthHeight * 4, yBlocks * widthHeight / 2);
            readyLabel.setText("GAME OVER!");
            readyLabel.setLocation(175, 250);
            pacmant1.stop();
            blinkyt1.stop();
            blinkyt2.stop();
            pinkyt1.stop();
            pinkyt2.stop();
            inkyt1.stop();
            inkyt2.stop();
            clydet1.stop();
            clydet2.stop();
            gameSounds.stop();
        }
    }

    private void mirrorMaze() {
        int invc = xBlocks - 1; //this will invert columns
        for (int row = 0; row < yBlocks; row++)
        {
            for (int col = 0; col < xBlocks * 2; col++)
            {
                if (col >= xBlocks)
                {
                    Cell tempCell = new Cell(col, row, widthHeight, xBlocks, yBlocks, mazeCells.get(row * xBlocks + invc).getContentType());
                    tempCell.setEast(mazeCells.get(row * xBlocks + invc).isWestSolid());
                    tempCell.setWest(mazeCells.get(row * xBlocks + invc).isEastSolid());
                    tempCell.setNorth(mazeCells.get(row * xBlocks + invc).isNorthSolid());
                    tempCell.setSouth(mazeCells.get(row * xBlocks + invc).isSouthSolid());
                    actualMaze.add(tempCell);
                    invc--;
                }
                else
                {

                    actualMaze.add(mazeCells.get(row * xBlocks + col));
                }//end else
            }//end for
            invc = xBlocks - 1;//reset inverted column counter
        }//end outer for
    }//end mirrorMaze

    private void ghostJail() {

        if (midwallRandom == 1)
        {//remove first wall top center middle, then every other
            for (int j = 0; j < yBlocks; j = j + 2)
            {
                actualMaze.get(xBlocks * j * 2 + xBlocks).setWest(false);
                actualMaze.get(xBlocks * j * 2 + xBlocks - 1).setEast(false);
            }

        }
        else
        {//leave top center middle wall, then remove every other
            for (int j = 1; j < yBlocks; j = j + 2)
            {
                actualMaze.get(xBlocks * j * 2 + xBlocks).setWest(false);
                actualMaze.get(xBlocks * j * 2 + xBlocks - 1).setEast(false);
            }
        }

        actualMaze.get(xBlocks * yBlocks + xBlocks).setNorth(false);//bottom row right center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks).setSouth(true);//bottom row right center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks).setEast(false);//bottom row right center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks).setWest(false);//bottom row right center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks + 1).setNorth(false);//bottom row right corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks + 1).setSouth(true);//bottom row right corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks + 1).setEast(true);//bottom row right corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks + 1).setWest(false);//bottom row right corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks + 2).setNorth(false);//bottom row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks + 2).setSouth(false);//bottom row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks + 2).setWest(true);//bottom row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks - 1).setNorth(false);//bottom row left center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 1).setSouth(true);//bottom row left center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 1).setEast(false);//bottom row left center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 1).setWest(false);//bottom row left center cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 2).setNorth(false);//bottom row left corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 2).setSouth(true);//bottom row left corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 2).setEast(false);//bottom row left corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 2).setWest(true);//bottom row left corner cell
        actualMaze.get(xBlocks * yBlocks + xBlocks - 3).setSouth(false);//bottom row left corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks - 3).setEast(true);//bottom row left corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks - 3).setNorth(false);//bottom row left corner cell OUTSIDE
        //Row Below Jail
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3).setNorth(true);//bottom row right center cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3).setWest(false);//bottom row right center cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3).setEast(false);//bottom row right center cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 + 1).setNorth(true);//bottom row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 + 1).setWest(false);//bottom row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 + 1).setEast(false);//bottom row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 + 2).setNorth(false);//bottom row right corner cell OUTSIDE edge
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 + 2).setWest(false);//bottom row right corner cell OUTSIDE edge
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 1).setNorth(true);//bottom row left center cell below
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 1).setWest(false);//bottom row left center cell below
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 1).setEast(false);//bottom row left center cell below
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 2).setNorth(true);//bottom row left corner cell below
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 2).setWest(false);//bottom row left corner cell below
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 2).setEast(false);//bottom row left corner cell below
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 3).setNorth(false);//bottom row left corner cell below edge
        actualMaze.get(xBlocks * yBlocks + xBlocks * 3 - 3).setEast(false);//bottom row left corner cell below edge

        //Upper Half of Jail
        //RIGHT DOOR ABOVE
        actualMaze.get(xBlocks * yBlocks - xBlocks).setNorth(false);//top row right center cell
        actualMaze.get(xBlocks * yBlocks - xBlocks).setSouth(false);//top row right center cell
        actualMaze.get(xBlocks * yBlocks - xBlocks).setEast(false);//top row right center cell
        actualMaze.get(xBlocks * yBlocks - xBlocks).setWest(false);//top row right center cell

        actualMaze.get(xBlocks * yBlocks - xBlocks + 1).setNorth(true);//top row right corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks + 1).setSouth(false);//top row right corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks + 1).setEast(true);//top row right corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks + 1).setWest(false);//top row right corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks + 2).setNorth(false);//top row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks - xBlocks + 2).setSouth(false);//top row right corner cell OUTSIDE
        actualMaze.get(xBlocks * yBlocks - xBlocks + 2).setWest(true);//top row right corner cell OUTSIDE

        //LEFT DOOR BELOW
        actualMaze.get(xBlocks * yBlocks - xBlocks - 1).setNorth(false);//top row left center cell
        actualMaze.get(xBlocks * yBlocks - xBlocks - 1).setSouth(false);//top row left center cell
        actualMaze.get(xBlocks * yBlocks - xBlocks - 1).setEast(false);//top row left center cell
        actualMaze.get(xBlocks * yBlocks - xBlocks - 1).setWest(false);//top row left center cell

        actualMaze.get(xBlocks * yBlocks - xBlocks - 2).setNorth(true);//top row left corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks - 2).setSouth(false);//top row left corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks - 2).setEast(false);//top row left corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks - 2).setWest(true);//top row left corner cell
        actualMaze.get(xBlocks * yBlocks - xBlocks - 3).setEast(true);//top row left corner cell Outside
        actualMaze.get(xBlocks * yBlocks - xBlocks - 3).setNorth(false);//top row left corner cell Outside
        actualMaze.get(xBlocks * yBlocks - xBlocks - 3).setSouth(false);//top row left corner cell Outside
        //set all cells as Jail Cells
        actualMaze.get(xBlocks * yBlocks - xBlocks - 2).setJailCell(true);
        actualMaze.get(xBlocks * yBlocks - xBlocks - 1).setJailCell(true);
        actualMaze.get(xBlocks * yBlocks - xBlocks).setJailCell(true);
        actualMaze.get(xBlocks * yBlocks - xBlocks + 1).setJailCell(true);
        actualMaze.get(xBlocks * yBlocks + xBlocks).setJailCell(true);
        actualMaze.get(xBlocks * yBlocks + xBlocks + 1).setJailCell(true);
        actualMaze.get(xBlocks * yBlocks + xBlocks - 1).setJailCell(true);
        actualMaze.get(xBlocks * yBlocks + xBlocks - 2).setJailCell(true);

        //RIGHT DOOR above Jail
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3).setSouth(false);//Jail Top of Right Door Opening
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3).setEast(false);//Jail Top of Right Door Opening
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3).setWest(false);//Jail Top of Right Door Opening

        //LEFT DOOR ABOVE
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 1).setSouth(false);//Jail Top of Left Door Opening
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 1).setEast(false);//Jail Top of Left Door Opening
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 1).setWest(false);//Jail Top of Left Door Opening

        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 2).setEast(false);//Jail Top of Left Corner
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 2).setWest(false);//Jail Top of Left Corner
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 2).setSouth(true);//Jail Top of Left Corner
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 3).setEast(false);//Jail Top of Left Corner OUTSIDE
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 - 3).setSouth(false);//Jail Top of Left Corner OUTSIDE
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 + 1).setEast(false);//Jail Top of Right Corner
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 + 1).setWest(false);//Jail Top of Right Corner
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 + 1).setSouth(true);//Jail Top of Right Corner
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 + 2).setWest(false);//Jail Top of Right Corner Outside
        actualMaze.get(xBlocks * yBlocks - xBlocks * 3 + 2).setSouth(false);//Jail Top of Right Corner Outside
    }//end ghostJail

    public void drawLines(Graphics g) {

        g2d = (Graphics2D) g;
        //Draw the lines:
        g2d.setStroke(new BasicStroke(3));
        for (Cell curCell : actualMaze)
        {//Cell generation
            curCell.draw(g, g2d);
        }//end draw Cells

        //draw jail door
        g.setColor(Color.WHITE);

        //paint jail doors...
        g2d.drawLine(xBlocks * widthHeight, (yBlocks / 2 - 2) * widthHeight + widthHeight, xBlocks * widthHeight + widthHeight, (yBlocks / 2 - 2) * widthHeight + widthHeight);//BOTTOM WALLS
        g2d.drawLine((xBlocks - 1) * widthHeight, (yBlocks / 2 - 2) * widthHeight + widthHeight, (xBlocks - 1) * widthHeight + widthHeight, (yBlocks / 2 - 2) * widthHeight + widthHeight);//BOTTOM WALLS

    }//end drawMaze

    @Override
    public void keyReleased(KeyEvent e) {
        //Don't do anything - lets the yellow man keep going
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //if needed, update the yellow man's current direction
    }//end keyTyped

    @Override
    public void keyPressed(KeyEvent e) {
        //update the yellow man's current direction
        int location = e.getExtendedKeyCode();
        int index = pacman.getYcoord() / widthHeight * xBlocks * 2 + pacman.getXcoord() / widthHeight;//find the index of the yellow guy's current cell
        boolean turn = false;
        switch (location)
        {
            case KeyEvent.VK_LEFT:
                //each of these if statements check to see if PacMan can move in the direction indicated by the key press
                //if (!actualMaze.get(index).isWestSolid())
                //{
                if (pacman.getCurrentDirection() != 'W')
                {
                    turn = pacman.getCurrentDirection() != 'E';
                    if (!pacman.checkWall('W', pacman.getXcoord(), pacman.getYcoord(), turn))
                    {
                        pacman.setCurrentDirection('W');

                    }//end inner if
                    else
                    {
                        pacman.setNextDirection('W');
                    }
                }
                break;
            case KeyEvent.VK_RIGHT:
                //if (!actualMaze.get(index).isEastSolid())
                // {
                if (pacman.getCurrentDirection() != 'E')
                {
                    turn = pacman.getCurrentDirection() != 'W';
                    if (!pacman.checkWall('E', pacman.getXcoord(), pacman.getYcoord(), turn))
                    {
                        pacman.setCurrentDirection('E');

                    }//end inner if
                    else
                    {
                        pacman.setNextDirection('E');
                    }
                }
                break;
            case KeyEvent.VK_UP:
                if (pacman.getCurrentDirection() != 'N')
                {
                    //if (!actualMaze.get(index).isNorthSolid())
                    turn = pacman.getCurrentDirection() != 'S';
                    if (!pacman.checkWall('N', pacman.getXcoord(), pacman.getYcoord(), turn))
                    {
                        pacman.setCurrentDirection('N');

                    }//end inner if
                    else
                    {
                        pacman.setNextDirection('N');
                    }
                }
                break;
            case KeyEvent.VK_DOWN:
                //if (!actualMaze.get(index).isSouthSolid())
                if (pacman.getCurrentDirection() != 'S')
                {
                    turn = pacman.getCurrentDirection() != 'N';
                    if (!pacman.checkWall('S', pacman.getXcoord(), pacman.getYcoord(), turn))
                    {
                        pacman.setCurrentDirection('S');

                    }//end inner if
                    else
                    {
                        pacman.setNextDirection('S');
                    }
                }
                break;
            default:
                break;
        }
    }//end keyPressed

    @Override
    public void actionPerformed(ActionEvent e) {
        //       if(blinky.death || clyde.death || inky.death || pinky.death)
        //   {

        //   }
        checkContact();
        if (e.getSource().equals(pacmant1))
        {
            pacman.timerTriggered();
            if (pacman.remainingContent() <= 0 && runOnce)
            {//increase the level since you've completed this one
                gameSounds.intermissionMusic();
                runOnce = false;
                pacmant1.stop();
                blinkyt1.stop();
                blinkyt2.stop();
                inkyt1.stop();
                inkyt2.stop();
                clydet1.stop();
                clydet2.stop();
                pinkyt1.stop();
                pinkyt2.stop();
                makeNewLevel();
            }

        }
        else if (e.getSource().equals(startDELAY))
        {
            startDelay();
        }
        else if (e.getSource().equals(inkyt1))
        {
            // System.out.println("Inky X: " +inky.xCoord + " Y: " +inky.yCoord);
            inky.timerTriggered();
        }
        else if (e.getSource().equals(inkyt2))
        {
            inky.incImg();
            inky.draw(g2d, g2d);

        }
        else if (e.getSource().equals(pinkyt1))
        {
            //System.out.println("pInky X: " +pinky.xCoord + " Y: " +pinky.yCoord);
            pinky.timerTriggered();
        }
        else if (e.getSource().equals(pinkyt2))
        {
            pinky.incImg();
            pinky.draw(g2d, g2d);

        }
        else if (e.getSource().equals(blinkyt1))
        {
            //System.out.println("blInky X: " +blinky.xCoord + " Y: " +blinky.yCoord);
            blinky.timerTriggered();
        }
        else if (e.getSource().equals(blinkyt2))
        {
            blinky.incImg();
            blinky.draw(g2d, g2d);

        }

        else if (e.getSource().equals(clydet1))
        {
            // System.out.println("clyde X: " +clyde.xCoord + " Y: " +clyde.yCoord);
            clyde.timerTriggered();
        }
        else if (e.getSource().equals(clydet2))
        {
            clyde.incImg();
            clyde.draw(g2d, g2d);

        }
        checkContact();
        jframe.repaint();
    }//end actionPerformed

    private void checkContact() {
        // if(pacman.getXcoord() == blinky.xCoord && pacman.getYcoord() == blinky.yCoord)
    }

    private void startDelay() {
        pacmant1.start();
        pinkyt1.start();
        pinkyt2.start();
        inkyt1.start();
        inkyt2.start();
        clydet1.start();
        clydet2.start();
        blinkyt1.start();
        blinkyt2.start();
        startDELAY.stop();
        gameSounds.loopMainGhostMusic();
    }//end startDelay

    void makeNewLevel() {
        if (levelNum == 4)
        {
            System.out.println("You beat the game!");
            gameWon = true;
            pacmant1.stop();
            blinkyt1.stop();
            blinkyt2.stop();
            pinkyt1.stop();
            pinkyt2.stop();
            inkyt1.stop();
            inkyt2.stop();
            clydet1.stop();
            clydet2.stop();
            readyLabel.setText("CLEARED!");
            readyLabel.setVisible(true);
        }
        else
        {
            levelNum++;//add to our level number counter!
            mazeCells.clear();//clears last maze
            actualMaze.clear();//...
            for (int k = 0; k < yBlocks; k++)
            {//create new maze cells
                for (int j = 0; j < xBlocks; j++)
                {
                    if ((k == 0 && j == 0) || (k == yBlocks - 1 && j == 0))
                    {
                        mazeCells.add(new Cell(j, k, widthHeight, xBlocks, yBlocks, ContentType.Cake));
                    }
                    else
                    {
                        mazeCells.add(new Cell(j, k, widthHeight, xBlocks, yBlocks, ContentType.CakeSlice));
                    }
                }

            }
            midwallRandom = rand.nextInt((2) + 1);//generate midwallRandom number between 0 & max node -1
            startDELAY.start();
            int chaseTime = 0;
            int scatterTime = 0;
            if (levelNum == 2)//one was chase 10sec, scatter 5sec.
            {
                chaseTime = 11000;//These are in MS.
                scatterTime = 4000;
            }
            else if (levelNum == 3)
            {
                chaseTime = 12000;
                scatterTime = 3000;
            }
            if (levelNum == 4)
            {
                chaseTime = 12000;
                scatterTime = 2000;
                gameSounds.finalLevel(true);
            }
            int totalPoints = pacman.getTotalScore();//retains player score - important only to some of us
            this.hasEarnedExtraLife = pacman.hasEarnedExtraLife;
            int numLives = pacman.getRemainingLives();//retains pacman's lives - kind of important
            createMaze();//create the actual maze
            pacman = new PacmanChar(4 * widthHeight, 8 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, gameSounds, numLives, totalPoints,hasEarnedExtraLife);//GENERATE THE YELLOW MAN
            inky = new Inky((xBlocks - 2) * widthHeight, yBlocks / 2 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);//the next four lines generate ghosts
            blinky = new Blinky(5 * widthHeight, 7 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);
            pinky = new Pinky(xBlocks * widthHeight, yBlocks / 2 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);
            clyde = new Clyde((xBlocks + 1) * widthHeight, yBlocks / 2 * widthHeight, actualMaze, widthHeight, xBlocks, yBlocks, pacman, gameSounds, chaseTime, scatterTime);
            pacman.register(clyde);//register observers
            pacman.register(inky);
            pacman.register(pinky);
            pacman.register(blinky);
            runOnce = true;
        }

    }//end makeNewLevel

    public void windowClosed() {
        startDELAY.stop();
        pacmant1.stop();
        blinkyt1.stop();
        blinkyt2.stop();
        inkyt1.stop();
        inkyt2.stop();
        clydet1.stop();
        clydet2.stop();
        pinkyt1.stop();
        pinkyt2.stop();
        gameSounds.forceStop();
    }
}//end Maze
