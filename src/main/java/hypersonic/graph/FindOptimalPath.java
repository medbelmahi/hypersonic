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
    
    public FindOptimalPath(final GraphFindAllPaths<T> graph) {
        if (graph == null) {
            throw new NullPointerException("The input graph cannot be null.");
        }
        this.graph = graph;
    }
    
    private void validate(final T source, final T destination) {

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
    
    public List<T> getOptimalPath(final T source, final T destination) {
        validate(source, destination);
        
        final List<T> path = recursive(source, destination);
        return path;
    }
    
    private List<T> recursive(final T current, final T destination) {
        final List<T> path = new ArrayList<>();
        
        if (current == destination) {
            path.add(current);
            return path;
        }
        
        final Map<T, Direction> edges  = graph.edgesFrom(current);
        
        final LinkedList<Direction> directions = current.getDirections(destination);
        
        for (final Direction direction : directions) {
            
            System.out.println(direction.toString());
        }
        System.out.println("-------------------------------------");
        
        for (final Direction direction : directions) {
            for (final Map.Entry<T, Direction> entry : edges.entrySet()) {
                if (direction.equals(entry.getValue())) {
                    path.add(entry.getKey());
                    final List<T> recursivePath = recursive(entry.getKey(), destination);
                    if (!recursivePath.isEmpty() && recursivePath.get(recursivePath.size() - 1) == destination) {
                        path.addAll(recursivePath);
                        return path;
                    }
                }
            }
        }
        
        return path;
    }
    
    public static void main(final String[] args) {
        final GraphFindAllPaths<Floor> graph = new GraphFindAllPaths<>();
        
        final Floor here = new Floor(0, 0);
        final Floor next1 = new Floor(0, 1);
        final Floor next2 = new Floor(0, 2);
        final Floor next3 = new Floor(0, 3);
        final Floor next4 = new Floor(0, 4);
        final Floor next5 = new Floor(0, 5);
        final Floor next6 = new Floor(0, 6);
        
        graph.addNode(here);
        
        graph.addNode(next1);
        graph.addNode(next2);
        graph.addNode(next3);
        graph.addNode(next4);
        graph.addNode(next5);
        graph.addNode(next6);
        
        graph.addEdge(here, next1, Direction.DOWN);
        graph.addEdge(next1, next2, Direction.DOWN);
        graph.addEdge(next2, next3, Direction.DOWN);
        graph.addEdge(next3, next4, Direction.DOWN);
        graph.addEdge(next4, next5, Direction.DOWN);
        graph.addEdge(next5, next6, Direction.DOWN);
        
        //
        final Floor next51 = new Floor(1, 5);
        graph.addNode(next51);
        graph.addEdge(next5, next51, Direction.RIGHT);
        
        final FindOptimalPath<Floor> findOptimalPath = new FindOptimalPath<>(graph);
        
        final List<Floor> optimalPath = findOptimalPath.getOptimalPath(here, next51);
        
        for (final Floor floor : optimalPath) {
            System.out.println(floor.coordinates.toString());
        }
        
    }
}
