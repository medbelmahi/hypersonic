package hypersonic.graph;

import hypersonic.Grid;
import hypersonic.cell.Cell;
import hypersonic.cell.Floor;
import hypersonic.entity.BomberMan;

import java.util.Set;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class ReachabilityCalculator<T extends Floor> {
    private final GraphFindAllPaths<T> graph;
    private Cell[][] cells;
    private BomberMan myPlayer;

    public ReachabilityCalculator(GraphFindAllPaths<T> graph, Cell[][] cells, BomberMan myPlayer) {
        this.cells = cells;
        this.myPlayer = myPlayer;
        if (graph == null) {
            throw new NullPointerException("The input graph cannot be null.");
        }
        this.graph = graph;
    }

    public void setReachableCases(T currentNode) {
        currentNode.setReachable(true);
        setNumberOfReachableBox(currentNode);

        final Set<T> edges  = graph.edgesFrom(currentNode).keySet();

        for (T t : edges) {
            if (!t.isReachable()) {
                setReachableCases(t);
            }
        }
    }


    private void setNumberOfReachableBox(T place) {

        int yy = place.coordinates.y;
        int xx = place.coordinates.x;

        int yStart = myPlayer.rangeStartY(yy);
        int yEnd = myPlayer.rangeEndY(yy, Grid.DEFAULT_HEIGHT);
        int xStart = myPlayer.rangeStartX(xx);
        int xEnd = myPlayer.rangeEndX(xx, Grid.DEFAULT_WIDTH);

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
