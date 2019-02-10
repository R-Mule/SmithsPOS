package pacman;

import java.util.ArrayList;
import java.util.Random;

public class dfsMazeCreate implements createMazeAlgo {

    private int xBlocks, yBlocks;
    private ArrayList<Cell> cells;
    private Random rand = new Random();

    dfsMazeCreate(ArrayList<Cell> cells, int xBlocks, int yBlocks) {//run any inits our maze might need, consider dfs needs
        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        this.cells = cells;
    }

    private boolean checkNorth(int index) {
        if (cells.get(index).getYLoc() != 0)//checks for a cell above
        {
            return cells.get(index - xBlocks).wasVisited();//checks visited status  
        }        //this mtd actually tricks the program somewhat - showing us that the cell
        //isn't actually a valid cell, though the program just doesn't know it
        return true;//so that we skip over the invalid cell, we mark it as visited
    }

    private boolean checkSouth(int index) {
        if (cells.get(index).getYLoc() < yBlocks - 1)//checks for a cell below
        {
            return cells.get(index + xBlocks).wasVisited();//checks visited status  
        }
        return true;//same as the above mtd.  we're tricking the program, sort of
        //"cheating" in handling the fact that this cell isn't valid
    }

    private boolean checkEast(int index) {
        if (index < xBlocks * yBlocks - 1 && cells.get(index).getYLoc() == cells.get(index + 1).getYLoc())//checks for cell to the right
        {
            return cells.get(index + 1).wasVisited();//checks visited status
        }
        return true;//following the pattern of trickery MWAHAHAHAHAHA AHAHAHAHA *cough cough*
    }

    private boolean checkWest(int index) {
        if (index != 0 && cells.get(index).getYLoc() == cells.get(index - 1).getYLoc())//checks for a cell to the left
        {
            return cells.get(index - 1).wasVisited();//checks visited status
        }
        return true;//IT'S A TRAP
    }

    @Override
    public void createMaze(int index) {
        boolean rightCell = true, leftCell = true, topCell = true, bottomCell = true;//mark all neighbors as visited at start
        cells.get(index).setVisited("MAZE_BUILDER", true);//marks cell as visited

        //checks surrounding cells for visitation status 
        topCell = checkNorth(index);
        bottomCell = checkSouth(index);
        rightCell = checkEast(index);
        leftCell = checkWest(index);

        //Here, take this: It's dangerous to go alone...
        while (!rightCell || !leftCell || !topCell || !bottomCell)
        {
            int randomNum = rand.nextInt((4 - 1) + 1) + 1;//generate random number between 1 and 4

            if (randomNum == 1 && !topCell)
            {//1: top cell time
                topCell = true;//mark as visited
                cells.get(index).setNorth(false);//north wall: *poof*
                cells.get(index - xBlocks).setSouth(false);//above cell's south wall: *poof*
                createMaze(index - xBlocks);//chug into the cell above (recursion)
                //checking cell status - we've just made some changes.
                topCell = checkNorth(index);
                bottomCell = checkSouth(index);
                rightCell = checkEast(index);
                leftCell = checkWest(index);

            }
            else if (!rightCell && randomNum == 2)
            {//right cell time
                rightCell = true;//mark as visited
                cells.get(index).setEast(false);//east wall: *poof*
                cells.get(index + 1).setWest(false);//right cell's west wall: *poof*
                createMaze(index + 1);//chug into the right cell (recursion)
                //checking cell status: we've made some changes
                topCell = checkNorth(index);
                bottomCell = checkSouth(index);
                rightCell = checkEast(index);
                leftCell = checkWest(index);

            }
            else if (randomNum == 3 && !leftCell)
            {//TO THE LEFT, TO THE LEFT, TO THE LEFT, TO THE LEFT
                leftCell = true;//mark as visited
                cells.get(index).setWest(false);//west wall: *poof*
                cells.get(index - 1).setEast(false);//left cell's east wall: *poof*
                createMaze(index - 1);//chug into the left cell
                //checking cell status: we've made some changes
                topCell = checkNorth(index);
                bottomCell = checkSouth(index);
                rightCell = checkEast(index);
                leftCell = checkWest(index);

            }
            else if (randomNum == 4 && !bottomCell)
            {//bottom cell time.  if you call it the butt cell, it will be offended.
                bottomCell = true;//mark as visited
                cells.get(index).setSouth(false);//south wall: *Poof*
                cells.get(index + xBlocks).setNorth(false);//below cell north wall: *poof*
                createMaze(index + xBlocks);//chug into the cell below
                //checking cell status: we've made some changes
                topCell = checkNorth(index);
                bottomCell = checkSouth(index);
                rightCell = checkEast(index);
                leftCell = checkWest(index);

            }
        }
        if (topCell && bottomCell && rightCell && leftCell)
        {//if all cells are marked visited
            if (cells.get(index).isEastSolid() && cells.get(index).isNorthSolid() && cells.get(index).isSouthSolid())
            {//east wall is middle wall
                if (index < xBlocks * yBlocks - 1 && cells.get(index).getYLoc() == cells.get(index + 1).getYLoc())
                {//if we are NOT on right wall
                    cells.get(index).setEast(false);
                    cells.get(index + 1).setWest(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getYLoc() != cells.get(index + 1).getYLoc())
                {//if we are at top right corner cell
                    cells.get(index + xBlocks).setNorth(false);
                    cells.get(index).setSouth(false);
                }
                else if (index == xBlocks * yBlocks - 1)
                {//if we are at bottom right corner cell
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
                else
                {//east wall border not at bottom or top
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
            }
            if (cells.get(index).isWestSolid() && cells.get(index).isNorthSolid() && cells.get(index).isSouthSolid())
            {//west wall is middle wall
                if (index != 0 && cells.get(index).getYLoc() == cells.get(index - 1).getYLoc())
                {//if we are NOT on left wall
                    cells.get(index - 1).setEast(false);
                    cells.get(index).setWest(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getXLoc() == 0)
                {//if we are at top left corner cell
                    cells.get(index).setSouth(false);
                    cells.get(index + xBlocks).setNorth(false);
                }
                else if (cells.get(index).getYLoc() == yBlocks - 1 && cells.get(index).getYLoc() != cells.get(index - 1).getYLoc())
                {//if we are at bottom left corner cell
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
                else
                {//left wall on the left border not in corners
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
            }
            if (cells.get(index).isEastSolid() && cells.get(index).isWestSolid() && cells.get(index).isSouthSolid())
            {//south wall is middle wall
                if (cells.get(index).getYLoc() < yBlocks - 1)
                {
                    cells.get(index).setSouth(false);
                    cells.get(index + xBlocks).setNorth(false);
                }
                else if (cells.get(index).getYLoc() == yBlocks - 1 && cells.get(index).getYLoc() != cells.get(index - 1).getYLoc())
                {//if we are at bottom left cell
                    cells.get(index).setEast(false);
                    cells.get(index + 1).setWest(false);
                }
                else if (index == xBlocks * yBlocks - 1)
                {//if we are at bottom right cell
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }
                else if (cells.get(index).getYLoc() == yBlocks - 1)
                {//if we are on bottom row not in a corner
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }

            }//end south wall is middle wall
            if (cells.get(index).isEastSolid() && cells.get(index).isNorthSolid() && cells.get(index).isWestSolid())
            {//north wall is middle wall
                if (index - xBlocks >= 0)
                {//if we are not at a top cell
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getXLoc() == 0)
                {//if we are at top left corner cell
                    cells.get(index).setEast(false);
                    cells.get(index + 1).setWest(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getYLoc() != cells.get(index + 1).getYLoc())
                {//if we are at top right corner cell
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }
                else if (cells.get(index).getYLoc() == 0 && index != 0 && index != xBlocks)
                {//if we are at a top cell not in corners
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }
            }
        }

        if (topCell && bottomCell && rightCell && leftCell)
        {//if all cells are marked visited
            if (cells.get(index).isEastSolid() && cells.get(index).isNorthSolid() && cells.get(index).isSouthSolid())
            {//east wall is middle wall
                if (index < xBlocks * yBlocks - 1 && cells.get(index).getYLoc() == cells.get(index + 1).getYLoc())
                {//if we are NOT on right wall
                    cells.get(index).setEast(false);
                    cells.get(index + 1).setWest(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getYLoc() != cells.get(index + 1).getYLoc())
                {//if we are at top right corner cell
                    cells.get(index + xBlocks).setNorth(false);
                    cells.get(index).setSouth(false);
                }
                else if (index == xBlocks * yBlocks - 1)
                {//if we are at bottom right corner cell
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
                else
                {//east wall border not at bottom or top
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
            }
            if (cells.get(index).isWestSolid() && cells.get(index).isNorthSolid() && cells.get(index).isSouthSolid())
            {//west wall is middle wall
                if (index != 0 && cells.get(index).getYLoc() == cells.get(index - 1).getYLoc())
                {//if we are NOT on left wall
                    cells.get(index - 1).setEast(false);
                    cells.get(index).setWest(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getXLoc() == 0)
                {//if we are at top left corner cell
                    cells.get(index).setSouth(false);
                    cells.get(index + xBlocks).setNorth(false);
                }
                else if (cells.get(index).getYLoc() == yBlocks - 1 && cells.get(index).getYLoc() != cells.get(index - 1).getYLoc())
                {//if we are at bottom left corner cell
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
                else
                {//left wall on the left border not in corners
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
            }
            if (cells.get(index).isEastSolid() && cells.get(index).isWestSolid() && cells.get(index).isSouthSolid())
            {//south wall is middle wall
                if (cells.get(index).getYLoc() < yBlocks - 1)
                {
                    cells.get(index).setSouth(false);
                    cells.get(index + xBlocks).setNorth(false);
                }
                else if (cells.get(index).getYLoc() == yBlocks - 1 && cells.get(index).getYLoc() != cells.get(index - 1).getYLoc())
                {//if we are at bottom left cell
                    cells.get(index).setEast(false);
                    cells.get(index + 1).setWest(false);
                }
                else if (index == xBlocks * yBlocks - 1)
                {//if we are at bottom right cell
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }
                else if (cells.get(index).getYLoc() == yBlocks - 1)
                {//if we are on bottom row not in a corner
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }

            }//end south wall is middle wall
            if (cells.get(index).isEastSolid() && cells.get(index).isNorthSolid() && cells.get(index).isWestSolid())
            {//north wall is middle wall
                if (index - xBlocks >= 0)
                {//if we are not at a top cell
                    cells.get(index).setNorth(false);
                    cells.get(index - xBlocks).setSouth(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getXLoc() == 0)
                {//if we are at top left corner cell
                    cells.get(index).setEast(false);
                    cells.get(index + 1).setWest(false);
                }
                else if (cells.get(index).getYLoc() == 0 && cells.get(index).getYLoc() != cells.get(index + 1).getYLoc())
                {//if we are at top right corner cell
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }
                else if (cells.get(index).getYLoc() == 0 && index != 0 && index != xBlocks)
                {//if we are at a top cell not in corners
                    cells.get(index).setWest(false);
                    cells.get(index - 1).setEast(false);
                }//end elseif
            }//end master if stmt

        }//end visited cells marked if
    }//end createMaze mtd
}//end CLASS

