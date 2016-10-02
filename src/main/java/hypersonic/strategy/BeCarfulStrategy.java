package hypersonic.strategy;

import hypersonic.Action;
import hypersonic.Grid;
import hypersonic.cell.Cell;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.graph.FindOptimalPath;

import java.util.List;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class BeCarfulStrategy extends Strategy {
    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }


    public static boolean isSafetyPlace(Cell cell, List<Bomb> bombs) {
        boolean isSafety = true;
        for (Bomb bomb : bombs) {
            if (bomb.coordinates.x == cell.coordinates.x) {
                int startY = bomb.rangeStartY(bomb.coordinates.y);
                int endY = bomb.rangeEndY(bomb.coordinates.y, Grid.DEFAULT_HEIGHT);
                if (cell.coordinates.y <= endY && cell.coordinates.y >= startY && noWallOrBoxInTheMiddle(cell, bomb, true)) {
                    return false;
                }

            }else if (bomb.coordinates.y == cell.coordinates.y) {
                int startX = bomb.rangeStartX(bomb.coordinates.x);
                int endX = bomb.rangeEndX(bomb.coordinates.x, Grid.DEFAULT_WIDTH);
                if (cell.coordinates.x <= endX && cell.coordinates.x >= startX && noWallOrBoxInTheMiddle(cell, bomb, false)) {
                    return false;
                }
            }
        }

        return isSafety;
    }

    private static boolean noWallOrBoxInTheMiddle(Cell cell, Bomb bomb, boolean aboutX) {

        if (aboutX) {
            if (bomb.coordinates.y < cell.coordinates.y) {
                for (int y = bomb.coordinates.y + 1; y < cell.coordinates.y; y++) {
                    Cell cellTest = Grid.cells[y][cell.coordinates.x];
                    if (!cellTest.isFreePlace()) {
                        return false;
                    }
                }
            } else if (bomb.coordinates.y > cell.coordinates.y) {
                for (int y = cell.coordinates.y + 1; y < bomb.coordinates.y; y++) {
                    Cell cellTest = Grid.cells[y][bomb.coordinates.x];
                    if (!cellTest.isFreePlace()) {
                        return false;
                    }
                }
            }
        } else {
            if (bomb.coordinates.x < cell.coordinates.x) {
                for (int x = bomb.coordinates.x + 1; x < cell.coordinates.x; x++) {
                    Cell cellTest = Grid.cells[cell.coordinates.y][x];
                    if (!cellTest.isFreePlace()) {
                        return false;
                    }
                }
            } else if (bomb.coordinates.x > cell.coordinates.x) {
                for (int x = cell.coordinates.x + 1; x < bomb.coordinates.x; x++) {
                    Cell cellTest = Grid.cells[bomb.coordinates.y][x];
                    if (!cellTest.isFreePlace()) {
                        return false;
                    }
                }
            }
        }


        return true;

    }

    public static boolean isSafetyPath(FindOptimalPath<Floor> findOptimalPath, List<Bomb> bombs, Floor source, Floor destination) {

        Floor cellSource = (Floor) Grid.cells[source.coordinates.y][source.coordinates.x];
        Floor cellDestination = (Floor) Grid.cells[destination.coordinates.y][destination.coordinates.x];

        System.err.println("cellSource : " + cellSource.toString());
        System.err.println("cellDestination : " + cellDestination.toString());
        List<Floor> path = findOptimalPath.getOptimalPath(cellSource, cellDestination);

        return isSafetyPath(path, bombs);

    }


    public static boolean isSafetyPath(List<Floor> optimalPath, List<Bomb> bombs) {
        for (Floor floor : optimalPath) {
            if (!isSafetyPlace(floor, bombs)) {
                System.err.println("isSafetyPath=false");
                return false;
            }
        }

        System.err.println("isSafetyPath=true");

        return true;
    }
}
