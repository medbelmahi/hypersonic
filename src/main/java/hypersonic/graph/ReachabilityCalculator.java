package hypersonic.graph;

import hypersonic.cell.Floor;

import java.util.Set;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class ReachabilityCalculator<T extends Floor> {
    private final GraphFindAllPaths<T> graph;


    public ReachabilityCalculator(GraphFindAllPaths<T> graph) {
        if (graph == null) {
            throw new NullPointerException("The input graph cannot be null.");
        }
        this.graph = graph;
    }

    public void setReachableCases(T currentNode) {
        currentNode.setReachable(true);

        final Set<T> edges  = graph.edgesFrom(currentNode).keySet();

        for (T t : edges) {
            if (!t.isReachable()) {
                setReachableCases(t);
            }
        }
    }
}
