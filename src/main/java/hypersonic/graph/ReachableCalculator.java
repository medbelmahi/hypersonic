package hypersonic.graph;

import hypersonic.Grid;
import hypersonic.cell.Cell;
import hypersonic.cell.Floor;
import hypersonic.entity.BomberMan;

import java.util.*;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class ReachableCalculator<T extends Floor> {
    private final GraphFindAllPaths<T> graph;
    private final Cell[][] cells;
    private final BomberMan myPlayer;
    private int heuristicNumber;
    
    private final Set<T> sortedFloor = new TreeSet<>();

    public Set<T> getSortedFloor() {
        return sortedFloor;
    }
    
    public ReachableCalculator(final GraphFindAllPaths<T> graph, final Cell[][] cells, final BomberMan myPlayer) {
        this.cells = cells;
        this.myPlayer = myPlayer;
        this.heuristicNumber = 3;
        if (graph == null) {
            throw new NullPointerException("The input graph cannot be null.");
        }
        this.graph = graph;
    }

    public int getHeuristicNumber() {
        if (heuristicNumber > 0) heuristicNumber--;
        return heuristicNumber;
    }

    public void setReachableCases(final T currentNode) {
        currentNode.setReachable(true);
        setNumberOfReachableBox(currentNode);
        sortedFloor.add(currentNode);
        
        final Set<T> edges  = graph.edgesFrom(currentNode).keySet();
        
        for (final T t : edges) {
            if (!t.isReachable()) {
                setReachableCases(t);
            }
        }
    }
    
    private void setNumberOfReachableBox(final T place) {
        
        final int yy = place.coordinates.y;
        final int xx = place.coordinates.x;
        
        final int yStart = myPlayer.rangeStartY(yy);
        final int yEnd = myPlayer.rangeEndY(yy, Grid.DEFAULT_HEIGHT);
        final int xStart = myPlayer.rangeStartX(xx);
        final int xEnd = myPlayer.rangeEndX(xx, Grid.DEFAULT_WIDTH);

        int numberOfBoxes = 0;

        for (int y = yy-1; y >= yStart; y--) {
            int value = cells[y][xx].value();
            if (value > 0) {
                numberOfBoxes += value;
                break;
            } else if (value == -1) {
                break;
            }
        }

        for (int y = yy+1; y <= yEnd; y++) {
            int value = cells[y][xx].value();
            if (value > 0) {
                numberOfBoxes += value;
                break;
            } else if (value == -1) {
                break;
            }
        }

        for (int x = xx-1; x >= xStart; x--) {
            int value = cells[yy][x].value();
            if (value > 0) {
                numberOfBoxes += value;
                break;
            } else if (value == -1) {
                break;
            }
        }


        for (int x = xx+1; x <= xEnd; x++) {
            int value = cells[yy][x].value();
            if (value > 0) {
                numberOfBoxes += value;
                break;
            } else if (value == -1) {
                break;
            }
        }

        if (numberOfBoxes > 0) {
            place.setNumberOfReachableBox(numberOfBoxes + getHeuristicNumber());
        } else {
            place.setNumberOfReachableBox(numberOfBoxes);
        }
        //System.err.println("set number : "+place.toString());
    }
}
