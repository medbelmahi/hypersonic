package hypersonic.graph;

import hypersonic.cell.Floor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mohamed BELMAHI on 27/09/2016.
 */
public class FindOptimalPath<T extends Floor> {

    private final GraphFindAllPaths<T> graph;

    public FindOptimalPath(GraphFindAllPaths<T> graph) {
        if (graph == null) {
            throw new NullPointerException("The input graph cannot be null.");
        }
        this.graph = graph;
    }

    private void validate (T source, T destination) {

        if (source == null) {
            throw new NullPointerException("The source: " + source + " cannot be  null.");
        }
        if (destination == null) {
            throw new NullPointerException("The destination: " + destination + " cannot be  null.");
        }
        if (source.equals(destination)) {
            throw new IllegalArgumentException("The source and destination: " + source + " cannot be the same.");
        }
    }

    public List<T> getOptimalPath(T source, T destination) {
        validate(source, destination);

        List<T> path = recursive(source, destination);
        return path;
    }


    private List<T> recursive(T current, T destination) {
        List<T> path = new ArrayList<>();

        if (current == destination) {
            return path;
        }

        final Map<T, Direction> edges  = graph.edgesFrom(current);


        LinkedList<Direction> directions = current.getDirections(destination);

        for (Direction direction : directions) {
            for (Map.Entry<T, Direction> entry : edges.entrySet()) {
                if (direction.equals(entry.getValue())) {
                    path.add(entry.getKey());
                    path.addAll(recursive(entry.getKey(), destination));
                    if (path.get(path.size()-1) != destination) {
                        path.clear();
                    }
                }
            }
        }

        return path;
    }
}
