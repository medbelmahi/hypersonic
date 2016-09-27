package hypersonic;

import hypersonic.cell.Floor;
import hypersonic.graph.GraphFindAllPaths;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class FindAllPaths<T extends Floor> {

    private final GraphFindAllPaths<T> graph;

    /**
     * Takes in a graph. This graph should not be changed by the client
     */
    public FindAllPaths(GraphFindAllPaths<T> graph) {
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

    /**
     * Returns the list of paths, where path itself is a list of nodes.
     *
     * @param source            the source node
     * @param destination       the destination node
     * @return                  List of all paths
     */
    public List<List<T>> getAllPaths(T source, T destination) {
        validate(source, destination);

        List<List<T>> paths = new ArrayList<List<T>>();
        recursive(source, destination, paths, new LinkedHashSet<T>());
        return paths;
    }

    // so far this dude ignore's cycles.
    private void recursive (T current, T destination, List<List<T>> paths, LinkedHashSet<T> path) {
        if (!paths.isEmpty()) {
            return;
        }
        path.add(current);

        if (current == destination) {
            paths.add(new ArrayList<T>(path));
            path.remove(current);
            return;
        }

        final Set<T> edges  = graph.edgesFrom(current).keySet();

        for (T t : edges) {
            if (!path.contains(t)) {
                recursive (t, destination, paths, path);
            }
        }

        path.remove(current);
    }
}
