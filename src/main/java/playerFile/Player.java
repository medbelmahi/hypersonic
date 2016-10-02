import java.util.*;



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Action {
    public static final String MOVE = "MOVE";
    public static final String BOMB = "BOMB";

    private String actionType;
    private Coordinates coordinates;


    public Action(String actionType, Coordinates coordinates) {
        this.actionType = actionType;
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return actionType + " " + coordinates.toString();
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Box extends Cell {

    Box(int x, int y) {
        super(x, y);
    }

    public boolean isFreePlace() {
        return false;
    }

    public int value() {
        return 1;
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
abstract class Cell{
    public static final char FLOOR_TYPE = '.';
    public static final char WALL_TYPE = 'X';
    public static final int BOX_TYPE = '0';
    public static final char EXTRA_RANGE_BOX_TYPE = '1';
    public static final int EXTRA_BOMB_BOX_TYPE = '2';

    public Coordinates coordinates;

    Cell(int x, int y) {
        coordinates = new Coordinates(x, y);
    }

    public abstract boolean isFreePlace();

    public abstract int value();

    public String presenter() {
        return "O";
    }

    public Cell rightCell(Cell[][] cells) {
        return coordinates.x + 1 < Grid.DEFAULT_WIDTH ? cells[coordinates.y][coordinates.x + 1] : null;
    }

    public Cell leftCell(Cell[][] cells) {
        return coordinates.x - 1 >= 0 ? cells[coordinates.y][coordinates.x - 1] : null;
    }

    public Cell upCell(Cell[][] cells) {
        return coordinates.y - 1 >= 0 ? cells[coordinates.y - 1][coordinates.x] : null;
    }

    public Cell downCell(Cell[][] cells) {
        return coordinates.y + 1 < Grid.DEFAULT_HEIGHT ? cells[coordinates.y + 1][coordinates.x] : null;
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class CellFactory {
    public static Cell[] constructRow(int y, String inputRow){
        int length = inputRow.length();

        Cell[] row = new Cell[length];

        for (int x = 0; x < length; x++) {
            row[x] = constructCell(x, y, inputRow.charAt(x));
        }

        return row;
    }

    private static Cell constructCell(int x, int y, char type){
        switch (type){
            case Cell.FLOOR_TYPE :
                return new Floor(x, y);
            case Cell.WALL_TYPE :
                return new Wall(x, y);
            case Cell.BOX_TYPE :
                return new Box(x, y);
            case Cell.EXTRA_RANGE_BOX_TYPE:
                return new ExtraRangeBox(x, y);
            case Cell.EXTRA_BOMB_BOX_TYPE:
                return new ExtraBombBox(x, y);
            default:
                throw new IllegalArgumentException();
        }
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class ExtraBombBox extends Box {

    ExtraBombBox(int x, int y) {
        super(x, y);
    }

    @Override
    public int value() {
        return 2;
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class ExtraRangeBox extends Box {
    ExtraRangeBox(int x, int y) {
        super(x, y);
    }

    @Override
    public int value() {
        return 2;
    }
}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Floor extends Cell implements Comparable<Floor>{

    private int numberOfReachableBox;
    private boolean isReachable;
    private boolean isNotSafetyPlace;
    private Bomb bomb;
    
    public Floor(final int x, final int y) {
        super(x, y);
    }

    @Override
    public String presenter() {
        if (isNotSafetyPlace) {
            return String.valueOf(bomb.getCountDown());
        }
        return "O";
    }

    public boolean isFreePlace() {
        return true;
    }

    public int value() {
        return 0;
    }
    
    public void setNumberOfReachableBox(final int numberOfReachableBox) {
        this.numberOfReachableBox = numberOfReachableBox;
    }
    public int getNumberOfReachableBox() {
        return this.numberOfReachableBox;
    }
    
    public int compareTo(final Floor o) {
        return this.numberOfReachableBox < o.numberOfReachableBox ? 1 : -1;
    }

    public boolean isReachable() {
        return isReachable;
    }
    
    public void setReachable(final boolean reachable) {
        isReachable = reachable;
    }
    
    public <T extends Floor> LinkedList<Direction> getDirections(final T destination) {
        return this.coordinates.getSortedDirection(destination.coordinates);
    }

    public boolean isNotSafetyPlace() {
        return isNotSafetyPlace;
    }

    public void setNotSafetyPlace(boolean notSafetyPlace) {
        isNotSafetyPlace = notSafetyPlace;
    }

    @Override
    public String toString() {
        return "box=" + numberOfReachableBox + " xy : " + coordinates.toString();
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Wall extends Cell {

    Wall(int x, int y) {
        super(x, y);
    }

    public boolean isFreePlace() {
        return false;
    }

    public int value() {
        return -1;
    }
}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y) {
        this.y = y;
        this.x = x;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }

    @Override
    public boolean equals(Object obj) {
        Coordinates other = (Coordinates) obj;
        return this.x == other.x && this.y == other.y;
    }

    public LinkedList<Direction> getSortedDirection(Coordinates destination) {

        LinkedList<Direction> directions = new LinkedList<>();

        if (this.y == destination.y) {
            if (this.x < destination.x) {
                directions.add(Direction.RIGHT);
                directions.add(Direction.DOWN);
                directions.add(Direction.UP);
                directions.add(Direction.LEFT);
            }else{
                directions.add(Direction.LEFT);
                directions.add(Direction.DOWN);
                directions.add(Direction.UP);
                directions.add(Direction.RIGHT);
            }
        } else if (this.x == destination.x) {
            if (this.y < destination.y) {
                directions.add(Direction.DOWN);
                directions.add(Direction.RIGHT);
                directions.add(Direction.LEFT);
                directions.add(Direction.UP);
            }else{
                directions.add(Direction.UP);
                directions.add(Direction.RIGHT);
                directions.add(Direction.LEFT);
                directions.add(Direction.DOWN);
            }
        } else if (this.x < destination.x && this.y < destination.y) {
            int diffX = destination.x - this.x;
            int diffY = destination.y - this.y;

            if (diffX < diffY) {
                directions.add(Direction.DOWN);
                directions.add(Direction.RIGHT);
                directions.add(Direction.LEFT);
                directions.add(Direction.UP);
            }else{
                directions.add(Direction.RIGHT);
                directions.add(Direction.DOWN);
                directions.add(Direction.LEFT);
                directions.add(Direction.UP);
            }
        }else if (this.x > destination.x && this.y > destination.y) {
            int diffX = destination.x - this.x;
            int diffY = destination.y - this.y;

            if (diffX < diffY) {
                directions.add(Direction.LEFT);
                directions.add(Direction.UP);
                directions.add(Direction.DOWN);
                directions.add(Direction.RIGHT);
            }else{
                directions.add(Direction.UP);
                directions.add(Direction.RIGHT);
                directions.add(Direction.DOWN);
                directions.add(Direction.RIGHT);
            }
        }else if (this.x < destination.x && this.y > destination.y){
            directions.add(Direction.LEFT);
            directions.add(Direction.UP);
            directions.add(Direction.DOWN);
            directions.add(Direction.RIGHT);
        }if (this.x > destination.x && this.y < destination.y){
            directions.add(Direction.LEFT);
            directions.add(Direction.DOWN);
            directions.add(Direction.UP);
            directions.add(Direction.RIGHT);
        }


        return directions;
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Bomb extends Entity {
    private Timer timer;
    private BomberMan owner;
    private int countDown;


    public Bomb(BomberMan owner, int x, int y, int countDown, int range) {
        super(x, y, range);
        this.owner = owner;
        this.timer = new Timer(Timer.DEFAULT_DECREASED_TIME, countDown);
        this.countDown = countDown;
    }

    public boolean isHisOwner(BomberMan bomberMan) {
        return bomberMan.equals(owner);
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }
}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class BomberMan extends Entity {
    private int id;
    private int bombsCount;
    private Floor destination;
    private Floor currentPlace;
    private Floor escapeDestination;
    private EatingStrategy eatingStrategy;

    public BomberMan(int id, int x, int y, int bombsCount, int range) {
        super(x, y, range);
        this.id = id;
        this.bombsCount = bombsCount;
    }

    public void setCurrentPlace(Cell[][] cells) {
        this.currentPlace = (Floor) cells[coordinates.y][coordinates.x];
    }

    public Floor getCurrentPlace() {
        return this.currentPlace;
    }

    public boolean canPlaceBomb() {
        return this.bombsCount > 0;
    }


    public Action makeAction(FindOptimalPath<Floor> findOptimalPath, GraphFindAllPaths<Floor> graph, final Set<Floor> places, List<Bomb> bombs, List<Box> boxes) {

        if (this.escapeDestination != null && !this.escapeDestination.coordinates.equals(this.coordinates)) {
            System.err.println("Last escape");
            return new Action(Action.MOVE, this.escapeDestination.coordinates);

        } else {

            this.escapeDestination = null;

            if (canPlaceBomb() && boxes.size() > 1) {

                /*Action action = eatingStrategy.eatIt(bombs, this.currentPlace);
                if (action != null) {
                    return action;
                }*/

                if (this.destination != null && BeCarfulStrategy.isSafetyPlace(this.destination, bombs)) {
                    if (this.destination.coordinates.equals(this.coordinates)) {

                        return getMakeBombAction(findOptimalPath, graph, bombs);

                    } else {
                        System.err.println("Last destination");
                        return new Action(Action.MOVE, this.destination.coordinates);
                    }
                } else {

                    this.destination = DestroyStrategy.getBestReachablePlace(findOptimalPath, graph, places, bombs, this);

                    if (this.destination != null) {
                        if (this.destination.coordinates.equals(this.currentPlace.coordinates)) {

                            return getMakeBombAction(findOptimalPath, graph, bombs);

                        } else {
                            return new Action(Action.MOVE, this.destination.coordinates);
                        }
                    } else {
                        Floor floor = EscapeStrategy.escapeTo(graph, this.currentPlace, bombs);
                        this.escapeDestination = floor;
                        System.err.println("escape");
                        return new Action(Action.MOVE, this.escapeDestination.coordinates);
                    }
                }

            } else {
                System.err.println("escape from " + this.currentPlace.toString());

                Floor floor = EscapeStrategy.escapeTo(graph, this.currentPlace, bombs);
                this.escapeDestination = floor;
                if (this.escapeDestination != null) {
                    return new Action(Action.MOVE, this.escapeDestination.coordinates);
                } else {
                    System.err.println("rester dans sa place");
                    return new Action(Action.MOVE, this.currentPlace.coordinates);
                }
            }
        }


    }

    private Action getMakeBombAction(FindOptimalPath<Floor> findOptimalPath, GraphFindAllPaths<Floor> graph, List<Bomb> bombs) {
        Bomb bomb = new Bomb(this, this.destination.coordinates.x, this.destination.coordinates.y, 8, this.explosionRange);
        bombs.add(bomb);
        System.err.println("find escape place from : " + this.destination.toString());
        Floor escapeTo = EscapeStrategy.escapeTo(graph, (Floor) Grid.cells[this.destination.coordinates.y][this.destination.coordinates.x], bombs);
        bombs.remove(bomb);

        if (escapeTo == null ) {
            this.destination = null;
            System.err.println("go to current place");
            return new Action(Action.MOVE, this.currentPlace.coordinates);
        }

        System.err.println("escapeTo : " + escapeTo.toString());
        boolean safetyPath = BeCarfulStrategy.isSafetyPath(findOptimalPath, bombs, this.destination, escapeTo);
        if (safetyPath) {

            this.destination = null;
            return new Action(Action.BOMB, this.coordinates);
        } else {
            return new Action(Action.MOVE, this.destination.coordinates);
        }
    }

    public void update(int id, int x, int y, int bombsCount, int range) {
        super.update(x, y, range);
        this.id = id;
        this.bombsCount = bombsCount;
    }

    public void setEatingStrategy(EatingStrategy eatingStrategy) {
        this.eatingStrategy = eatingStrategy;
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
abstract class Entity {

    public static final int BOMBER_MAN_TYPE = 0;
    public static final int BOMB_TYPE = 1;
    public static final int ITEM_TYPE = 2;

    public int explosionRange;

    public Coordinates coordinates;


    public Entity(int x, int y, int explosionRange) {
        this.coordinates = new Coordinates(x, y);
        this.explosionRange = explosionRange;
    }

    public int rangeStartY(int y) {
        int startY = y - (explosionRange - 1);
        return startY < 0 ? 0 : startY;
    }

    public int rangeStartX(int x) {
        int startX = x - (explosionRange - 1);
        return startX < 0 ? 0 : startX;
    }

    public int rangeEndY(int y, int height) {
        int endY = y + ((explosionRange - 1));
        return endY < height ? endY : (height - 1);
    }

    public int rangeEndX(int x, int width) {
        int endX = x + ((explosionRange - 1));
        return endX < width ? endX : (width - 1);
    }

    public void update(int x, int y, int explosionRange) {
        this.coordinates = new Coordinates(x, y);
        this.explosionRange = explosionRange;
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Item extends Entity {
    private int id;
    public Item(int owner, int x, int y, int id, int explosionRange) {
        super(x, y, explosionRange);
        this.id = id;
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Timer{
    public static final int DEFAULT_STARTED_TIME = 8;
    public static final int DEFAULT_DECREASED_TIME = 1;
    int decreasedTime;
    int countDown;

    public Timer(int decreasedTime, int countDown) {
        this.decreasedTime = decreasedTime;
        this.countDown = countDown;
    }
}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class FindAllPaths<T extends Floor> {

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



/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
enum Direction {
    RIGHT, LEFT, UP, DOWN
}




/**
 * Created by Mohamed BELMAHI on 27/09/2016.
 */
class FindOptimalPath<T extends Floor> {

    private final GraphFindAllPaths<T> graph;
    public static final int PLACE_TO_ESCAPE = 10;
    
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
            //throw new IllegalArgumentException("The source and destination: " + source + " cannot be the same.");
            throw new IllegalArgumentException();
        }
    }
    
    public List<T> getOptimalPath(final T source, final T destination) {
        try {
            validate(source, destination);
        } catch (IllegalArgumentException e) {
            return new ArrayList<T>();
        }

        List<T> alreadyList = new ArrayList<>();

        final List<T> path = recursive(source, destination, alreadyList);
        return path;
    }
    
    private List<T> recursive(final T current, final T destination, List<T> alreadyList) {
        final List<T> path = new ArrayList<>();

        alreadyList.add(current);
        if (current == destination) {
            path.add(current);
            return path;
        }

        //System.err.println("current : " + current.coordinates.toString());
        final Map<T, Direction> edges  = graph.edgesFrom(current);
        
        final LinkedList<Direction> directions = current.getDirections(destination);
        
        
        for (final Direction direction : directions) {
            for (final Map.Entry<T, Direction> entry : edges.entrySet()) {
                T entryKey = entry.getKey();
                if (direction.equals(entry.getValue()) && !alreadyList.contains(entryKey)) {
                    if (entryKey != destination) {
                        path.add(entryKey);
                    }
                    final List<T> recursivePath = recursive(entryKey, destination, alreadyList);
                    if (!recursivePath.isEmpty() && recursivePath.get(recursivePath.size() - 1) == destination) {
                        path.addAll(recursivePath);
                        return path;
                    }
                }
            }
        }
    
        if (!path.isEmpty() && path.get(path.size() - 1) != destination) {
            return new ArrayList<>();
        }
        
        return path;
    }


    public Map<T, Integer> getPlacesWithDistance(T currentPlace){
        Map<T, Integer> places = new HashMap<T, Integer>();

        final Map<T, Direction> edges  = graph.edgesFrom(currentPlace);

        for (Map.Entry<T, Direction> entry : edges.entrySet()) {
            recursivePlacesWithDistance(places, currentPlace, entry.getKey());
        }

        return places;
    }

    private void recursivePlacesWithDistance(Map<T, Integer> places, T currentPlace, T destination) {
        if (places.size() > 10) {
            places.put(destination, getOptimalPath(currentPlace, destination).size());

            final Map<T, Direction> edges  = graph.edgesFrom(destination);

            for (Map.Entry<T, Direction> entry : edges.entrySet()) {
                recursivePlacesWithDistance(places, currentPlace, entry.getKey());
            }
        }
    }

    public static void main(final String[] args) {
        final GraphFindAllPaths<Floor> graph = new GraphFindAllPaths<>();
        
        final Floor here = new Floor(0, 0);
        final Floor next1 = new Floor(0, 1);
        final Floor next2 = new Floor(0, 2);
        final Floor next3 = new Floor(0, 3);
        final Floor next4 = new Floor(0, 4);
        final Floor next41 = new Floor(1, 4);
        final Floor next5 = new Floor(0, 5);
        final Floor next6 = new Floor(0, 6);
        final Floor next62 = new Floor(2, 6);
        
        graph.addNode(here);
        
        graph.addNode(next1);
        graph.addNode(next2);
        graph.addNode(next3);
        graph.addNode(next4);
        graph.addNode(next5);
        graph.addNode(next6);
        graph.addNode(next62);
        graph.addNode(next41);
        
        graph.addEdge(here, next1, Direction.DOWN);
        graph.addEdge(next1, next2, Direction.DOWN);
        graph.addEdge(next2, next3, Direction.DOWN);
        graph.addEdge(next3, next4, Direction.DOWN);
        graph.addEdge(next4, next5, Direction.DOWN);
        graph.addEdge(next5, next6, Direction.DOWN);
        graph.addEdge(next4, next41, Direction.RIGHT);
        
        //
        final Floor next51 = new Floor(1, 5);
        graph.addNode(next51);
        graph.addEdge(next5, next51, Direction.RIGHT);
        
        final FindOptimalPath<Floor> findOptimalPath = new FindOptimalPath<>(graph);
    
        final List<Floor> optimalPath = findOptimalPath.getOptimalPath(here, next41);
        
        for (final Floor floor : optimalPath) {
            System.out.println(floor.coordinates.toString());
        }
        
    }



}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class GraphFindAllPaths<T extends Floor> implements Iterable<T> {

    /* A map from nodes in the graph to sets of outgoing edges.  Each
     * set of edges is represented by a map from edges to doubles.
     */
    public final Map<T, Map<T, Direction>> graph = new HashMap<T, Map<T, Direction>>();

    /**
     *  Adds a new node to the graph. If the node already exists then its a
     *  no-op.
     *
     * @param node  Adds to a graph. If node is null then this is a no-op.
     * @return      true if node is added, false otherwise.
     */
    public boolean addNode(T node) {
        if (node == null) {
            throw new NullPointerException("The input node cannot be null.");
        }
        if (graph.containsKey(node)) return false;

        graph.put(node, new HashMap<T, Direction>());
        return true;
    }

    /**
     * Given the source and destination node it would add an arc from source
     * to destination node. If an arc already exists then the value would be
     * updated the new value.
     *
     * @param source                    the source node.
     * @param destination               the destination node.
     * @param direction                    if length if
     * @throws NullPointerException     if source or destination is null.
     * @throws NoSuchElementException   if either source of destination does not exists.
     */
    public void addEdge (T source, T destination, Direction direction) {
        if (source == null || destination == null) {
            throw new NullPointerException("Source and Destination, both should be non-null.");
        }
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            throw new NoSuchElementException("Source and Destination, both should be part of graph");
        }
        /* A node would always be added so no point returning true or false */
        graph.get(source).put(destination, direction);
    }

    /**
     * Removes an edge from the graph.
     *
     * @param source        If the source node.
     * @param destination   If the destination node.
     * @throws NullPointerException     if either source or destination specified is null
     * @throws NoSuchElementException   if graph does not contain either source or destination
     */
    public void removeEdge (T source, T destination) {
        if (source == null || destination == null) {
            throw new NullPointerException("Source and Destination, both should be non-null.");
        }
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            throw new NoSuchElementException("Source and Destination, both should be part of graph");
        }
        graph.get(source).remove(destination);
    }

    /**
     * Given a node, returns the edges going outward that node,
     * as an immutable map.
     *
     * @param node The node whose edges should be queried.
     * @return An immutable view of the edges leaving that node.
     * @throws NullPointerException   If input node is null.
     * @throws NoSuchElementException If node is not in graph.
     */
    public Map<T, Direction> edgesFrom(T node) {
        if (node == null) {
            throw new NullPointerException("The node should not be null.");
        }
        Map<T, Direction> edges = graph.get(node);
        if (edges == null) {
            throw new NoSuchElementException("Source node does not exist.");
        }
        return Collections.unmodifiableMap(edges);
    }

    /**
     * Returns the iterator that travels the nodes of a graph.
     *
     * @return an iterator that travels the nodes of a graph.
     */
    public Iterator<T> iterator() {
        return graph.keySet().iterator();
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class GraphMaker {

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




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class ReachableCalculator<T extends Floor> {
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





/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Grid {
    
    public static final int DEFAULT_WIDTH = 13;
    public static final int DEFAULT_HEIGHT = 11;
    
    private final int width;
    private final int height;
    private final int myPlayerId;
    public static Cell[][] cells;
    private final List<Bomb> bombs;
    private final List<Item> items;
    private final Map<Integer, BomberMan> players;
    private BomberMan myPlayer;
    private GraphFindAllPaths<Floor> pathGraph;
    private FindOptimalPath<Floor> findOptimalPath;
    private ReachableCalculator<Floor> reachableCalculator;
    private List<Box> boxes = new ArrayList<>();
    private EatingStrategy eatingStrategy;
    private Map<Floor, Entity> floorEntityMap = new HashMap<>();
    private DangerCalculator dangerCalculator;

    public Grid(final int width, final int height, final int myPlayerId) {
        this.width = width;
        this.height = height;
        this.myPlayerId = myPlayerId;
        this.cells = new Cell[height][width];
        this.bombs = new ArrayList<Bomb>();
        this.items = new ArrayList<Item>();
        this.players = new HashMap<Integer, BomberMan>();
    }
    
    public void addRow(final int y, final String row) {
        cells[y] = CellFactory.constructRow(y, row);
    }
    
    public void addEntity(final int entityType, final int owner, final int x, final int y, final int param1,
            final int param2) {
        
        BomberMan bomberMan = this.players.get(owner);
        switch (entityType) {
        case Entity.BOMBER_MAN_TYPE:
            if (bomberMan != null) {
                bomberMan.update(owner, x, y, param1, param2);
            } else {
                bomberMan = new BomberMan(owner, x, y, param1, param2);
            }
            this.players.put(owner, bomberMan);
            this.floorEntityMap.put((Floor) cells[y][x], bomberMan);
            break;
        case Entity.BOMB_TYPE:
            Bomb bomb = new Bomb(bomberMan, x, y, param1, param2);
            this.bombs.add(bomb);
            this.floorEntityMap.put((Floor) cells[y][x], bomb);
            break;
        case Entity.ITEM_TYPE:
            Item item = new Item(owner, x, y, param1, param2);
            this.items.add(item);
            this.floorEntityMap.put((Floor) cells[y][x], item);
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
    
    public void nextRound() {
        this.bombs.clear();
        this.items.clear();
        this.floorEntityMap.clear();
    }
    
    public String doAction() {
        
        final Action action = this.myPlayer.makeAction(this.findOptimalPath, this.pathGraph,
                reachableCalculator.getSortedFloor(), this.bombs, this.boxes);

        return action.toString() + " " + action.toString();
    }
    
    public void init() {
        myPlayer = players.get(myPlayerId);
        myPlayer.setCurrentPlace(cells);


        final Set<Floor> places = new TreeSet<Floor>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] instanceof Floor) {
                    places.add((Floor) cells[y][x]);
                } else if (cells[y][x] instanceof Box) {
                    boxes.add((Box) cells[y][x]);
                }
            }
        }
        this.pathGraph = GraphMaker.constructGraph(places, this.cells);

        this.reachableCalculator = new ReachableCalculator<Floor>(this.pathGraph, cells, myPlayer);
        reachableCalculator.setReachableCases((Floor) this.cells[myPlayer.coordinates.y][myPlayer.coordinates.x]);

        final Iterator<Floor> iterator = places.iterator();
        while (iterator.hasNext()) {
            final Floor floor = iterator.next();
            if (!floor.isReachable())
                places.remove(floor);
        }

        this.findOptimalPath = new FindOptimalPath<>(this.pathGraph);

        eatingStrategy = new EatingStrategy(this.items, this.findOptimalPath);
        this.myPlayer.setEatingStrategy(eatingStrategy);

        this.dangerCalculator = new DangerCalculator(floorEntityMap);
        this.dangerCalculator.setDangerToAllCells(this.bombs);

        gridPresenter();
    }


    private void gridPresenter() {
        for (int y = 0; y < Grid.DEFAULT_HEIGHT; y++) {
            String line = "";
            for (int x = 0; x < Grid.DEFAULT_WIDTH; x++) {
                line += cells[y][x].presenter();
            }
            System.err.println(line);
        }
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        int myId = in.nextInt();
        in.nextLine();

        Grid grid = new Grid(width, height, myId);

        // game loop
        while (true) {
            for (int i = 0; i < height; i++) {
                String row = in.nextLine();
                grid.addRow(i, row);
            }
            int entities = in.nextInt();
            for (int i = 0; i < entities; i++) {
                int entityType = in.nextInt();
                int owner = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int param1 = in.nextInt();
                int param2 = in.nextInt();
                grid.addEntity(entityType, owner, x, y, param1, param2);
            }
            in.nextLine();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            //System.out.println("BOMB 6 5 Amiral");

            grid.init();

            System.out.println(grid.doAction());
            grid.nextRound();
        }
    }
}





/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class AttackStrategy extends Strategy {
    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class BeCarfulStrategy extends Strategy {
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




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class DestroyStrategy extends Strategy {
    @Override public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
    
    public static Floor getBestReachablePlace(FindOptimalPath<Floor> findOptimalPath, GraphFindAllPaths<Floor> graph, final Set<Floor> places, List<Bomb> bombs, BomberMan bomberMan) {
        System.err.println("getBestReachablePlace -> size :" + places.size());
        for (Floor place : places) {
            if (BeCarfulStrategy.isSafetyPlace(place, bombs)) {

                Bomb bomb = new Bomb(bomberMan, place.coordinates.x, place.coordinates.y, 8, bomberMan.explosionRange);
                bombs.add(bomb);
                Floor escapeTo = EscapeStrategy.escapeTo(graph, place, bombs);
                bombs.remove(bomb);
                if (escapeTo != null) {
                    boolean safetyPath = BeCarfulStrategy.isSafetyPath(findOptimalPath, bombs, bomberMan.getCurrentPlace(), place);
                    if (safetyPath) {
                        return place;
                    }
                }
                initial(places);
            }
        }

        return null;
    }

    private static void initial(Set<Floor> places) {
        for (Floor place : places) {
            place.setNotSafetyPlace(false);
        }
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class EatingStrategy extends Strategy {

    List<Item> items;
    private FindOptimalPath<Floor> findOptimalPath;

    public EatingStrategy(List<Item> items, FindOptimalPath<Floor> findOptimalPath) {
        this.items = items;
        this.findOptimalPath = findOptimalPath;
    }

    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }

    public Action eatIt(List<Bomb> bombs, Floor currentPlace) {
        for (Item item : this.items) {
            Floor itemCell = (Floor) Grid.cells[item.coordinates.y][item.coordinates.x];
            if (itemCell.isReachable()) {
                List<Floor> optimalPath = findOptimalPath.getOptimalPath(currentPlace, itemCell);
                if (optimalPath.size() < 3) {
                    if (BeCarfulStrategy.isSafetyPath(optimalPath, bombs)) {
                        return new Action(Action.MOVE, itemCell.coordinates);
                    }
                }
            }
        }

        return null;
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class EscapeStrategy extends Strategy {


    public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
    
    public static Floor escapeTo(GraphFindAllPaths<Floor> graph, Floor currentPlace, List<Bomb> bombs) {

        Floor escapeTo = recursive(currentPlace, graph, bombs);
        initSafetyGraph(currentPlace, graph);
        return escapeTo;
    }

    private static void initSafetyGraph(Floor currentPlace, GraphFindAllPaths<Floor> graph) {
        if (!currentPlace.isNotSafetyPlace()) {
            return;
        }

        currentPlace.setNotSafetyPlace(false);

        Map<Floor, Direction> edgesFrom = graph.edgesFrom(currentPlace);

        if (edgesFrom != null) {
            for (Floor sousElement : edgesFrom.keySet()) {
                initSafetyGraph(sousElement, graph);
            }
        }
    }

    private static Floor recursive(Floor floor, GraphFindAllPaths<Floor> graph, List<Bomb> bombs) {
        if (BeCarfulStrategy.isSafetyPlace(floor, bombs)) {
            System.err.println("safety place : " + floor.toString());
            return floor;
        }else{
            floor.setNotSafetyPlace(true);
        }

        Map<Floor, Direction> edgesFrom = graph.edgesFrom(floor);


        if (edgesFrom != null) {
            for (Floor sousElement : edgesFrom.keySet()) {
                if (!sousElement.isNotSafetyPlace()) {
                    Floor recursive = recursive(sousElement, graph, bombs);
                    if (recursive != null) {
                        return recursive;
                    }
                }
            }
        }

        return null;
    }


}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
abstract class Strategy {

    public abstract List<Action> makeActions(List<Action> actions);

}





/**
 * Created by Mohamed BELMAHI on 02/10/2016.
 */
class DangerCalculator {

    private Map<Floor, Entity> floorEntityMap;

    public DangerCalculator(Map<Floor, Entity> floorEntityMap) {
        this.floorEntityMap = floorEntityMap;
    }

    public void setDangerToAllCells(List<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            scanXandYLineForBomb(bomb);
        }
    }

    public void scanXandYLineForBomb(Bomb bomb) {
        scanXLine(bomb);
        scanYLine(bomb);
    }

    private void scanXLine(Bomb bomb) {

        int y = bomb.coordinates.y;

        int endXRange = bomb.coordinates.x + (bomb.explosionRange - 1);
        for (int x = bomb.coordinates.x; x < Grid.DEFAULT_WIDTH; x++) {

            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedX = x - (neighbour.explosionRange - 1);
                    if (explodedX <= bomb.coordinates.x) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (x <= endXRange) {
                setNotSafety(bomb, (Floor) cell);
            }

        }

        int startX = bomb.coordinates.x - (bomb.explosionRange - 1);

        for (int x = bomb.coordinates.x-1; x >= 0; x--) {
            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedX = x + (neighbour.explosionRange - 1);
                    if (explodedX >= bomb.coordinates.x) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (x >= startX) {
                setNotSafety(bomb, (Floor) cell);
            }

        }
    }

    private void setNotSafety(Bomb bomb, Floor cell) {
        Floor floor = cell;
        floor.setNotSafetyPlace(true);
        floor.setBomb(bomb);
    }

    private void scanYLine(Bomb bomb) {

        int x = bomb.coordinates.x;

        int endYRange = bomb.coordinates.y + (bomb.explosionRange - 1);
        for (int y = bomb.coordinates.y; y < Grid.DEFAULT_HEIGHT; y++) {

            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedY = y - (neighbour.explosionRange - 1);
                    if (explodedY <= bomb.coordinates.y) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (y <= endYRange) {
                setNotSafety(bomb, (Floor) cell);
            }

        }

        int startYRange = bomb.coordinates.y - (bomb.explosionRange - 1);

        for (int y = bomb.coordinates.y-1; y >= 0; y--) {
            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedY = y + (neighbour.explosionRange - 1);
                    if (explodedY >= bomb.coordinates.y) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (y >= startYRange) {
                setNotSafety(bomb, (Floor) cell);
            }

        }
    }

    private boolean goBreak(Cell cell, Entity entity, boolean entityNotNull) {
        if (!cell.isFreePlace() || (entityNotNull && entity instanceof Item)) {
            return true;
        }
        return false;
    }
}