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
    
    private final TreeSet<Floor> sortedFloor = new TreeSet<>();
    
    public ReachableCalculator(final GraphFindAllPaths<T> graph, final Cell[][] cells, final BomberMan myPlayer) {
        this.cells = cells;
        this.myPlayer = myPlayer;
        if (graph == null) {
            throw new NullPointerException("The input graph cannot be null.");
        }
        this.graph = graph;
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

        for (int y = yStart; y < yEnd; y++) {
            numberOfBoxes += cells[y][xx].value();
        }
        for (int x = xStart; x < xEnd; x++) {
            numberOfBoxes += cells[yy][x].value();
        }

        place.setNumberOfReachableBox(numberOfBoxes);
    }
}
