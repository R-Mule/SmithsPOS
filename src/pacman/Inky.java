package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**

 @author holliefuller
 */
public class Inky extends Ghost implements ActionListener {

    private Image[] inkyPngsEAST = new Image[10];
    private Image[] inkyPngsWEST = new Image[10];
    private Image[] inkyPngsNORTH = new Image[10];
    private Image[] inkyPngsSOUTH = new Image[10];
    private ArrayList<Cell> currentPath;
    private ArrayList<Cell> tempPath;
    // private ArrayList<Cell> visitedCells;
    private Cell targetCell;
    private int maxVisited = 0;

    public Inky(int xCoord, int yCoord, ArrayList<Cell> myMaze, int widthHeight, int xBlocks, int yBlocks, PacmanChar pacman, GameSounds sounds, int chaseTimeMS, int scatterTimeMS) {
        super(xCoord, yCoord, myMaze, widthHeight, xBlocks, yBlocks, pacman, sounds, chaseTimeMS, scatterTimeMS);
        loadImageArray();
        currentPath = new ArrayList<>();
        tempPath = new ArrayList<>();
        queue = new ArrayList<>();
        currentMode = Mode.CHASE;
        requestedMode = Mode.CHASE;
        modeTimer = new Timer(2000, this);
        modeTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(modeTimer))
        {
            if (currentMode.equals(Mode.CHASE))
            {
                modeTimer.setInitialDelay(scatterTimeMS);
                requestedMode = Mode.SCATTER;
            }
            else
            {
                modeTimer.setInitialDelay(chaseTimeMS);
                requestedMode = Mode.CHASE;
            }
        }
        modeTimer.stop();
    }

    @Override
    public void draw(Graphics g, Graphics2D g2d) {
        g2d = (Graphics2D) g;

        if (getCurImg() != null)
        {   //draw the image if it's not null
            switch (currentDirection)
            {
                case 'S':
                    // g2d.drawImage(getCurImg(), xCoord * widthHeight + 4 - xOffset, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'W':
                    //g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4 - yOffset, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'E':
                    // g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4 + yOffset, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                case 'N':
                    //g2d.drawImage(getCurImg(), xCoord * widthHeight + 4 + xOffset, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
                default:
                    // g2d.drawImage(getCurImg(), xCoord * widthHeight + 4, yCoord * widthHeight + 4, widthHeight - 8, widthHeight - 8, null);
                    g2d.drawImage(getCurImg(), xCoord + 4, yCoord + 4, widthHeight - 8, widthHeight - 8, null);
                    break;
            }
        }
    }//end draw

    public Image getCurImg() {
        if (death)
        {
            return this.getCurrentDeadImage();
        }
        else if (pacman.isPoweredUp())
        {
            return this.getCurrentZedImage();
        }
        else
        {
            switch (this.currentDirection)
            {
                case 'N':
                    return inkyPngsNORTH[curImgIndex];
                case 'S':
                    return inkyPngsSOUTH[curImgIndex];
                case 'W':
                    return inkyPngsWEST[curImgIndex];
                default:
                    return inkyPngsEAST[curImgIndex];
            }
        }
    }

    public void incImg() {
        if (curImgIndex == 3)
        {
            curImgIndex = 0;
        }
        else
        {
            curImgIndex++;
        }
    }

    private void loadImageArray() {
        ImageIcon east1 = createImageIcon("images/inkyeast.png", "InkyEast");
        inkyPngsEAST[0] = (east1.getImage());
        ImageIcon east2 = createImageIcon("images/inkyeastalt.png", "InkyEastAlt");
        inkyPngsEAST[1] = east2.getImage();
        ImageIcon east3 = createImageIcon("images/inkyeastalt2.png", "InkyEastAlt2");
        inkyPngsEAST[2] = east3.getImage();
        inkyPngsEAST[3] = east2.getImage();

        ImageIcon west1 = createImageIcon("images/inkywest.png", "InkyWest");
        inkyPngsWEST[0] = (west1.getImage());
        ImageIcon west2 = createImageIcon("images/inkywestalt.png", "InkyWestAlt");
        inkyPngsWEST[1] = west2.getImage();
        ImageIcon west3 = createImageIcon("images/inkywestalt2.png", "InkyWestAlt2");
        inkyPngsWEST[2] = west3.getImage();
        inkyPngsWEST[3] = west2.getImage();

        ImageIcon south1 = createImageIcon("images/inkysouth.png", "InkySouth");
        inkyPngsSOUTH[0] = (south1.getImage());
        ImageIcon south2 = createImageIcon("images/inkysouthalt.png", "InkySouthAlt");
        inkyPngsSOUTH[1] = south2.getImage();
        ImageIcon south3 = createImageIcon("images/inkysouthalt2.png", "InkySouthAlt2");
        inkyPngsSOUTH[2] = south3.getImage();
        inkyPngsSOUTH[3] = south2.getImage();

        ImageIcon north1 = createImageIcon("images/inkynorth.png", "InkyNorth");
        inkyPngsNORTH[0] = (north1.getImage());
        ImageIcon north2 = createImageIcon("images/inkynorthalt.png", "InkyNorthAlt");
        inkyPngsNORTH[1] = north2.getImage();
        ImageIcon north3 = createImageIcon("images/inkynorthalt2.png", "InkyNorthAlt2");
        inkyPngsNORTH[2] = north3.getImage();
        inkyPngsNORTH[3] = north2.getImage();
    }//end loadImageArray

    public void timerTriggered() {
        if (!pacman.isDead())
        {
            checkContact();
            if ((currentTargetX == xCoord && currentTargetY == yCoord))//|| (findNextPath && xCoord % widthHeight == 0 && yCoord % widthHeight == 0))
            {
                if (death && !currentPath.isEmpty() && currentPath.get(0) == startCell)
                {
                    death = false;
                    sounds.stopGhostDeadMusic(true);
                    if (pacman.isPoweredUp())
                    {
                        sounds.startGhostTurnBlueMusic();
                    }
                    else
                    {
                        sounds.loopMainGhostMusic();
                    }
                    currentPath.clear();
                }

                if (!currentPath.isEmpty())
                {
                    currentPath.remove(0);
                }

                if (currentPath.isEmpty() || (!death && !currentMode.equals(requestedMode)))
                {
                    if (!death && !currentMode.equals(requestedMode))
                    {
                        modeTimer.start();
                        currentMode = requestedMode;
                    }

                    findNextPath();//This loads currentPath
                    findNextPath = false;
                }

                if (!currentPath.isEmpty())
                {
                    if (currentPath.get(0).getXLoc() * widthHeight > xCoord && !checkWall('E', xCoord, yCoord))
                    {
                        this.currentTargetX = xCoord + widthHeight;
                        this.currentTargetY = yCoord;
                        this.currentDirection = 'E';

                        if (xCoord > (xBlocks * 2 - 1) * widthHeight)
                        {
                            xCoord = 0;
                        }
                        else
                        {
                            xCoord += 10;
                        }
                        checkContact();
                        return;
                    }
                    else if (currentPath.get(0).getXLoc() * widthHeight < xCoord && !checkWall('W', xCoord, yCoord))
                    {
                        this.currentTargetX = xCoord - widthHeight;
                        this.currentTargetY = yCoord;
                        this.currentDirection = 'W';

                        if (xCoord < 0)
                        {
                            xCoord = (xBlocks * 2 - 1) * widthHeight;
                        }
                        else
                        {
                            xCoord -= 10;
                        }
                        // }
                        checkContact();
                        return;
                    }
                    else if (currentPath.get(0).getYLoc() * widthHeight > yCoord && !checkWall('S', xCoord, yCoord))
                    {
                        this.currentTargetX = xCoord;
                        this.currentTargetY = yCoord + widthHeight;
                        this.currentDirection = 'S';
                        //incImg();
                        yCoord += 10;
                        checkContact();
                        return;
                    }
                    else if (currentPath.get(0).getYLoc() * widthHeight < yCoord && !checkWall('N', xCoord, yCoord))
                    {
                        this.currentTargetX = xCoord;
                        this.currentTargetY = yCoord - widthHeight;
                        this.currentDirection = 'N';
                        // incImg();
                        yCoord -= 10;
                        checkContact();
                        return;
                    }
                }
            }
            else
            {
                switch (this.currentDirection)
                {
                    case 'N':
                        yCoord -= 10;
                        break;
                    case 'S':
                        yCoord += 10;
                        break;
                    case 'E':
                        if (xCoord > (xBlocks * 2 - 1) * widthHeight)
                        {
                            xCoord = 0;
                            this.currentTargetX = xCoord;
                            this.currentTargetY = yCoord;
                        }
                        else
                        {
                            xCoord += 10;
                        }
                        break;
                    case 'W':
                        if (xCoord < 0)
                        {
                            xCoord = (xBlocks * 2 - 1) * widthHeight;
                            this.currentTargetX = xCoord;
                            this.currentTargetY = yCoord;
                        }
                        else
                        {
                            xCoord -= 10;
                        }
                        break;
                    default:
                        break;
                }
                checkContact();
            }
        }
    }

    public void findNextPath() {
        if (!death)
        {
            if (currentMode.equals(Mode.CHASE))
            {
                //Here is where we either chase, or reassign...
                findNextPath = false;
                currentPath.clear();
                if (pacman.getXcoord() % widthHeight != 0)
                {
                    subXcoord = pacman.getXcoord() - pacman.getXcoord() % widthHeight;
                }
                else
                {
                    subXcoord = pacman.getXcoord();
                }
                if (pacman.getYcoord() % widthHeight != 0)
                {
                    subYcoord = pacman.getYcoord() - pacman.getYcoord() % widthHeight;
                }
                else
                {
                    subYcoord = pacman.getYcoord();
                }

                for (Cell cell : maze)
                {

                    if (cell.getXLoc() * widthHeight == subXcoord && cell.getYLoc() * widthHeight == subYcoord)
                    {
                        targetCell = cell;
                    }
                }
            }
            else if (currentMode.equals(Mode.SCATTER))//Blinky owns the Bottom Left. So anything greater than yBlocks+1 and less than or equal xBlocks for random choice.
            {
                int minX = 0;
                int maxX = xBlocks;
                int minY = yBlocks / 2 + 2;//+2 keeps it below the jail.
                int maxY = yBlocks - 1;
                int xRandomNum = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
                int yRandomNum = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
                targetCell = maze.get(xRandomNum + yRandomNum * xBlocks * 2);
            }
        }
        else
        {
            targetCell = startCell;
        }
        generateWaterfall(targetCell, 0);
        loadNextPath();

        /*
        int index = 0;
        for (Cell cell : maze)
        {

            System.out.print(cell.inkyWeight + "      ");
            index++;
            if (index == 12)
            {
                System.out.println();
                index = 0;
            }
        }
        System.out.println("DONE");
         */
    }

    public void loadNextPath() {
        Cell currentCell = maze.get(xCoord / widthHeight + yCoord / widthHeight * xBlocks * 2);
        Cell nextCell = null;
        int xActual = xCoord / widthHeight;
        int yActual = yCoord / widthHeight;
        int weight = currentCell.inkyWeight;
        int tempWeight;
        while (weight != 0)
        {
            if (!currentCell.isSouthSolid())
            {
                tempWeight = maze.get(xActual + (yActual + 1) * xBlocks * 2).inkyWeight;
                if (tempWeight < weight)
                {
                    nextCell = maze.get(xActual + (yActual + 1) * xBlocks * 2);
                    weight = tempWeight;
                }
            }

            if (!currentCell.isNorthSolid())
            {
                tempWeight = maze.get(xActual + (yActual - 1) * xBlocks * 2).inkyWeight;
                if (tempWeight < weight)
                {
                    nextCell = maze.get(xActual + (yActual - 1) * xBlocks * 2);
                    weight = tempWeight;
                }
            }

            if (!currentCell.isEastSolid() && xActual != xBlocks * 2 - 1)
            {
                tempWeight = maze.get(xActual + 1 + yActual * xBlocks * 2).inkyWeight;
                if (tempWeight < weight)
                {

                    nextCell = maze.get(xActual + 1 + yActual * xBlocks * 2);
                    weight = tempWeight;
                }
            }

            if (!currentCell.isWestSolid() && xActual != 0)
            {
                tempWeight = maze.get(xActual - 1 + yActual * xBlocks * 2).inkyWeight;
                if (tempWeight < weight)
                {
                    nextCell = maze.get(xActual - 1 + yActual * xBlocks * 2);
                    weight = tempWeight;
                }
            }
            // currentCell.inkyWeight = 10000;//so we don't return to this cell. Hopefully.
            xActual = nextCell.getXLoc();
            yActual = nextCell.getYLoc();
            currentPath.add(nextCell);
            currentCell = nextCell;

        }

    }

    public void generateWaterfall(Cell currentCell, int currentWeight) {

        ArrayList<Cell> tempList = new ArrayList<>();

        for (Cell tempCell : maze)
        {
            tempCell.inkyWeight = 100000;
        }
        currentCell.inkyVisited = true;
        currentCell.inkyWeight = 0;
        queue.add(currentCell);

        while (!queue.isEmpty())
        {
            currentWeight++;
            for (Cell cell : queue)
            {

                if (!cell.isSouthSolid())

                {
                    Cell southCell = maze.get(cell.getXLoc() + (cell.getYLoc() + 1) * xBlocks * 2);
                    if (southCell.inkyWeight > currentWeight || !southCell.inkyVisited)
                    {
                        southCell.inkyVisited = true;
                        southCell.inkyWeight = currentWeight;
                        tempList.add(southCell);
                    }
                }

                if (!cell.isEastSolid() && cell.getXLoc() != xBlocks * 2 - 1)

                {
                    Cell eastCell = maze.get(cell.getXLoc() + 1 + cell.getYLoc() * xBlocks * 2);
                    if (eastCell.inkyWeight > currentWeight || !eastCell.inkyVisited)
                    {
                        eastCell.inkyVisited = true;
                        eastCell.inkyWeight = currentWeight;
                        tempList.add(eastCell);

                    }
                }

                if (!cell.isWestSolid() && cell.getXLoc() != 0)

                {
                    Cell westCell = maze.get(cell.getXLoc() - 1 + cell.getYLoc() * xBlocks * 2);
                    if (westCell.inkyWeight > currentWeight || !westCell.inkyVisited)
                    {
                        westCell.inkyVisited = true;
                        westCell.inkyWeight = currentWeight;
                        tempList.add(westCell);

                    }
                }

                if (!cell.isNorthSolid())

                {
                    Cell northCell = maze.get(cell.getXLoc() + (cell.getYLoc() - 1) * xBlocks * 2);
                    if (northCell.inkyWeight > currentWeight || !northCell.inkyVisited)
                    {
                        northCell.inkyVisited = true;
                        northCell.inkyWeight = currentWeight;
                        tempList.add(northCell);

                    }
                }
            }
            queue.remove(0);
            for (Cell cell : tempList)
            {
                queue.add(cell);
            }
            tempList.clear();
        }
    }

    public boolean checkWall(char wallToCheck, int x, int y) {
        for (Cell cell : maze)
        {
            if (cell.getXLoc() * widthHeight == x && cell.getYLoc() * widthHeight == y)
            {

                if (wallToCheck == 'N' && cell.isNorthSolid())
                {

                    return true;

                }
                else if (wallToCheck == 'S' && cell.isSouthSolid() || (!death && (wallToCheck == 'S' && (cell.getXLoc() == 5 || cell.getXLoc() == 6) && cell.getYLoc() == 5)))
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
        return true;
    }

    @Override
    public void update() {
        if (!death)//if I am alive I would like these updates...
        {
            subXcoord = pacman.getXcoord();//update current coords

            subYcoord = pacman.getYcoord();

            if (subXcoord == xCoord && subYcoord == yCoord)
            {
                if (!pacman.isDead())
                {
                    death = pacman.makeContact();
                    if (death)
                    {
                        currentPath.clear();
                        sounds.stopGhostTurnBlueMusic();
                        sounds.startGhostDeadMusic(true);
                    }
                }
            }

        }
//even if I am dead, I need to know if pacman is dead so I can reset.
        if (pacman.isDead())
        {
            currentPath.clear();
            xCoord = (xBlocks - 2) * widthHeight;
            yCoord = (yBlocks / 2) * widthHeight;
            this.currentTargetX = xCoord;
            this.currentTargetY = yCoord;
            this.currentDirection = 'N';
            if (death)
            {
                death = false;
                sounds.stopGhostDeadMusic(true);
            }

        }

    }

}
