package hypersonic.graph;

import hypersonic.cell.Cell;
import hypersonic.cell.Floor;

import java.util.Set;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class GraphMaker {

    public static GraphFindAllPaths<Floor> constructGraph(Set<Floor> places, Cell[][] cells) {
        GraphFindAllPaths<Floor> graphFindAllPaths = new GraphFindAllPaths<Floor>();

        for (Floor currentCell : places) {
            graphFindAllPaths.addNode(currentCell);

            Cell right = currentCell.rightCell(cells);
            Cell left = currentCell.leftCell(cells);
            Cell up = currentCell.upCell(cells);
            Cell down = currentCell.downCell(cells);

            addEdgeToCurrentCell(graphFindAllPaths, currentCell, right, Direction.RIGHT);
            addEdgeToCurrentCell(graphFindAllPaths, currentCell, left, Direction.LEFT);
            addEdgeToCurrentCell(graphFindAllPaths, currentCell, up, Direction.UP);
            addEdgeToCurrentCell(graphFindAllPaths, currentCell, down, Direction.DOWN);
        }

        return graphFindAllPaths;
    }

    public static void addEdgeToCurrentCell(final GraphFindAllPaths<Floor> graphFindAllPaths, Floor currentCell, Cell destination, Direction direction) {
        if (destination != null && destination.isFreePlace()) {
            graphFindAllPaths.addNode((Floor) destination);
            graphFindAllPaths.addEdge(currentCell, (Floor) destination, direction);
        }
    }
}
