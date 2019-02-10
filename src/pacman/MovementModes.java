package pacman;

import java.util.ArrayList;

/**

 @author R-Mule
 */
//There is chase mode, and scatter mode.
//reference http://gameinternals.com/post/2072558330/understanding-pac-man-ghost-behavior
public class MovementModes {

    private int startX;
    private int startY;
    private Cell startCell;
    private Cell endCell;
    private int endX;
    private int endY;
    ArrayList<Cell> path;
    ArrayList<Cell> visitedCells;
    ArrayList<Cell> maze;

    public MovementModes(ArrayList<Cell> maze) {
        this.maze = maze;
    }

    public Cell getNextLocation(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        for (Cell cell : maze)
        {
            if (cell.getXLoc() == startX && cell.getYLoc() == startY)
            {
                startCell = cell;
            }
            else if (cell.getXLoc() == endX && cell.getYLoc() == endY)
            {
                endCell = cell;
            }
        }
        findPath();
        return null;

    }

    public void findPath() {
        path = new ArrayList<>();
        visitedCells = new ArrayList<>();

    }

}
