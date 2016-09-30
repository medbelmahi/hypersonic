import java.util.*;



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Action {
    public static final String MOVE = "MOVE";
    public static final String BOMB = "BOMB";
    
    private final String actionType;
    private final Coordinates coordinates;
    
    public Action(final String actionType, final Coordinates coordinates) {
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
    
    Box(final int x, final int y) {
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
    
    Cell(final int x, final int y) {
        coordinates = new Coordinates(x, y);
    }

    public abstract boolean isFreePlace();

    public abstract int value();
    
    public Cell rightCell(final Cell[][] cells) {
        return coordinates.x + 1 < Grid.DEFAULT_WIDTH ? cells[coordinates.y][coordinates.x + 1] : null;
    }
    
    public Cell leftCell(final Cell[][] cells) {
        return coordinates.x - 1 >= 0 ? cells[coordinates.y][coordinates.x - 1] : null;
    }
    
    public Cell upCell(final Cell[][] cells) {
        return coordinates.y - 1 >= 0 ? cells[coordinates.y - 1][coordinates.x] : null;
    }
    
    public Cell downCell(final Cell[][] cells) {
        return coordinates.y + 1 < Grid.DEFAULT_HEIGHT ? cells[coordinates.y + 1][coordinates.x] : null;
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class CellFactory {
    public static Cell[] constructRow(final int y, final String inputRow) {
        final int length = inputRow.length();
        
        final Cell[] row = new Cell[length];

        for (int x = 0; x < length; x++) {
            row[x] = constructCell(x, y, inputRow.charAt(x));
        }

        return row;
    }
    
    private static Cell constructCell(final int x, final int y, final char type) {
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
    
    ExtraBombBox(final int x, final int y) {
        super(x, y);
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class ExtraRangeBox extends Box {
    ExtraRangeBox(final int x, final int y) {
        super(x, y);
    }
}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Floor extends Cell implements Comparable<Floor>{

    private int numberOfReachableBox;
    private boolean isReachable;
    
    public Floor(final int x, final int y) {
        super(x, y);
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
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Wall extends Cell {
    
    Wall(final int x, final int y) {
        super(x, y);
    }

    public boolean isFreePlace() {
        return false;
    }

    public int value() {
        return 0;
    }
}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Coordinates {
    public int x;
    public int y;
    
    public Coordinates(final int x, final int y) {
        this.y = y;
        this.x = x;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
    
    @Override public boolean equals(final Object obj) {
        final Coordinates other = (Coordinates) obj;
        return this.x == other.x && this.y == other.y;
    }
    
    public LinkedList<Direction> getSortedDirection(final Coordinates destination) {
        
        final LinkedList<Direction> directions = new LinkedList<>();

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
            final int diffX = destination.x - this.x;
            final int diffY = destination.y - this.y;

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
            final int diffX = destination.x - this.x;
            final int diffY = destination.y - this.y;

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
    private final Timer timer;
    private final BomberMan owner;
    
    public Bomb(final BomberMan owner, final int x, final int y, final int countDown, final int range) {
        super(x, y, range);
        this.owner = owner;
        this.timer = new Timer(Timer.DEFAULT_DECREASED_TIME, countDown);
    }
    
    public boolean isHisOwner(final BomberMan bomberMan) {
        return bomberMan.equals(owner);
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class BomberMan extends Entity {
    private final int id;
    private final int bombsCount;
    
    public BomberMan(final int id, final int x, final int y, final int bombsCount, final int range) {
        super(x, y, range);
        this.id = id;
        this.bombsCount = bombsCount;
    }

    public boolean cantPlaceBomb() {
        return this.bombsCount < 1;
    }


    public Action makeAction() {

        return new Action(Action.MOVE, new Coordinates(0, 0));
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
abstract class Entity {

    public static final int BOMBER_MAN_TYPE = 0;
    public static final int BOMB_TYPE = 1;
    public static final int ITEM_TYPE = 2;

    protected int explosionRange;

    public Coordinates coordinates;
    
    public Entity(final int x, final int y, final int explosionRange) {
        this.coordinates = new Coordinates(x, y);
        this.explosionRange = explosionRange;
    }
    
    public int rangeStartY(final int y) {
        final int startY = y - (explosionRange - 1);
        return startY < 0 ? 0 : startY;
    }
    
    public int rangeStartX(final int x) {
        final int startX = x - (explosionRange - 1);
        return startX < 0 ? 0 : startX;
    }
    
    public int rangeEndY(final int y, final int height) {
        final int endY = y + ((explosionRange - 1));
        return endY < height ? endY : (height - 1);
    }
    
    public int rangeEndX(final int x, final int width) {
        final int endX = x + ((explosionRange - 1));
        return endX < width ? endX : (width - 1);
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Item extends Entity {
    private final int id;
    
    public Item(final int owner, final int x, final int y, final int id, final int explosionRange) {
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
    
    public Timer(final int decreasedTime, final int countDown) {
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
    public FindAllPaths(final GraphFindAllPaths<T> graph) {
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

    /**
     * Returns the list of paths, where path itself is a list of nodes.
     *
     * @param source            the source node
     * @param destination       the destination node
     * @return                  List of all paths
     */
    public List<List<T>> getAllPaths(final T source, final T destination) {
        validate(source, destination);
    
        final List<List<T>> paths = new ArrayList<List<T>>();
        recursive(source, destination, paths, new LinkedHashSet<T>());
        return paths;
    }

    // so far this dude ignore's cycles.
    private void recursive(final T current, final T destination, final List<List<T>> paths,
            final LinkedHashSet<T> path) {
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
    
        for (final T t : edges) {
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
            for (final Map.Entry<T, Direction> entry : edges.entrySet()) {
                if (direction.equals(entry.getValue())) {
                    if (entry.getKey() != destination) {
                        path.add(entry.getKey());
                    }
                    final List<T> recursivePath = recursive(entry.getKey(), destination);
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
    
    public Map<T, Integer> getPlacesWithDistance(final T currentPlace) {
        final Map<T, Integer> places = new HashMap<T, Integer>();

        final Map<T, Direction> edges  = graph.edgesFrom(currentPlace);
        
        for (final Map.Entry<T, Direction> entry : edges.entrySet()) {
            recursivePlacesWithDistance(places, currentPlace, entry.getKey());
        }

        return places;
    }
    
    private void recursivePlacesWithDistance(final Map<T, Integer> places, final T currentPlace, final T destination) {
        places.put(destination, getOptimalPath(currentPlace, destination).size());

        final Map<T, Direction> edges  = graph.edgesFrom(destination);
        
        for (final Map.Entry<T, Direction> entry : edges.entrySet()) {
            recursivePlacesWithDistance(places, currentPlace, entry.getKey());
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
    private final Map<T, Map<T, Direction>> graph = new HashMap<T, Map<T, Direction>>();

    /**
     *  Adds a new node to the graph. If the node already exists then its a
     *  no-op.
     *
     * @param node  Adds to a graph. If node is null then this is a no-op.
     * @return      true if node is added, false otherwise.
     */
    public boolean addNode(final T node) {
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
    public void addEdge(final T source, final T destination, final Direction direction) {
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
    public void removeEdge(final T source, final T destination) {
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
    public Map<T, Direction> edgesFrom(final T node) {
        if (node == null) {
            throw new NullPointerException("The node should not be null.");
        }
        final Map<T, Direction> edges = graph.get(node);
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
    
    public static GraphFindAllPaths<Floor> constructGraph(final Set<Floor> places, final Cell[][] cells) {
        final GraphFindAllPaths<Floor> graphFindAllPaths = new GraphFindAllPaths<Floor>();
        
        for (final Floor currentCell : places) {
            graphFindAllPaths.addNode(currentCell);
            
            final Cell right = currentCell.rightCell(cells);
            final Cell left = currentCell.leftCell(cells);
            final Cell up = currentCell.upCell(cells);
            final Cell down = currentCell.downCell(cells);

            addEdgeToCurrentCell(graphFindAllPaths, currentCell, right, Direction.RIGHT);
            addEdgeToCurrentCell(graphFindAllPaths, currentCell, left, Direction.LEFT);
            addEdgeToCurrentCell(graphFindAllPaths, currentCell, up, Direction.UP);
            addEdgeToCurrentCell(graphFindAllPaths, currentCell, down, Direction.DOWN);
        }

        return graphFindAllPaths;
    }
    
    public static void addEdgeToCurrentCell(final GraphFindAllPaths<Floor> graphFindAllPaths, final Floor currentCell,
            final Cell destination, final Direction direction) {
        if (destination != null && destination.isFreePlace()) {
            graphFindAllPaths.addNode((Floor) destination);
            graphFindAllPaths.addEdge(currentCell, (Floor) destination, direction);
        }
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class ReachabilityCalculator<T extends Floor> {
    private final GraphFindAllPaths<T> graph;
    private final Cell[][] cells;
    private final BomberMan myPlayer;
    
    public ReachabilityCalculator(final GraphFindAllPaths<T> graph, final Cell[][] cells, final BomberMan myPlayer) {
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





/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Grid{

    public static final int DEFAULT_WIDTH = 13;
    public static final int DEFAULT_HEIGHT = 11;
    
    private final int width;
    private final int height;
    private final int myPlayerId;
    private final Cell[][] cells;
    private final List<Bomb> bombs;
    private final List<Item> items;
    private final Map<Integer, BomberMan> players;
    private BomberMan myPlayer;
    private GraphFindAllPaths<Floor> pathGraph;
    
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

        switch (entityType) {
            case Entity.BOMBER_MAN_TYPE:
                this.players.put(owner, new BomberMan(owner, x, y, param1, param2));
                break;
            case Entity.BOMB_TYPE:
                this.bombs.add(new Bomb(this.players.get(owner), x, y, param1, param2));
                break;
            case Entity.ITEM_TYPE:
                this.items.add(new Item(owner, x, y, param1, param2));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void nextRound() {
        this.bombs.clear();
        this.items.clear();
    }

    public String doAction() {
    
        final Action action = this.myPlayer.makeAction();


       /* if (!myPlayer.cantPlaceBomb()) {
            Coordinates bestRichPlace = bestRichPlace();
            if (!bestRichPlace.equals(myPlayer.coordinates)) {
                action = new Action(Action.MOVE, bestRichPlace);
            }else{
                action = new Action(Action.BOMB, myPlayer.coordinates);
            }

        }else{
            Coordinates bestRichPlace = bestRichPlace();
            action = new Action(Action.MOVE, bestRichPlace);
        }*/

        return action.toString() + " " + action.toString();
    }

    private Coordinates bestRichPlace() {

        /*System.err.println("first place " + places.iterator().next().coordinates.toString());
        this.pathGraph = constructGraph(places);
        System.err.println("traitement 1");
        //FindAllPaths<Cell> findAllPaths = new FindAllPaths<Cell>(this.pathGraph);
        System.err.println("traitement 2");
        Cell currentPlace = cells[myPlayer.coordinates.y][myPlayer.coordinates.x];
        for (Floor place : places) {
            System.err.println("place (number="+place.getNumberOfReachableBox()+") " + place.coordinates.toString());

            boolean hasBomb = false;
            for (Bomb bomb : this.bombs) {
                if (bomb.coordinates.equals(place.coordinates)) {
                    hasBomb = true;
                    break;
                }
            }

            if (hasBomb) {
                continue;
            }

            if (currentPlace.coordinates.equals(place.coordinates) || places.size() > 100) {
                return place.coordinates;
            }

            try {
                List<List<Cell>> paths = findAllPaths.getAllPaths(currentPlace, place);
                if (paths != null && !paths.isEmpty()) {
                    return place.coordinates;
                }
            } catch (NoSuchElementException e) {
                System.err.println("(number="+place.getNumberOfReachableBox()+")no path for " + place.coordinates.toString());
            }
        }*/
        return new Coordinates(0, 0);
    }



    public void init() {
        myPlayer = players.get(myPlayerId);
    
        final Set<Floor> places = new TreeSet<Floor>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] instanceof Floor) {
                    //setNumberOfReachableBox(y, x);
                    places.add((Floor) cells[y][x]);
                }
            }
        }
        this.pathGraph = GraphMaker.constructGraph(places, this.cells);
    
        final ReachabilityCalculator<Floor> reachabilityCalculator = new ReachabilityCalculator<Floor>(this.pathGraph,
                cells, myPlayer);
        reachabilityCalculator.setReachableCases((Floor) this.cells[myPlayer.coordinates.y][myPlayer.coordinates.x]);
    
        final Iterator<Floor> iterator = places.iterator();
        while (iterator.hasNext()) {
            final Floor floor = iterator.next();
            if (!floor.isReachable()) places.remove(floor);
        }
    }
}



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Player {
    
    public static void main(final String[] args) {
        final Scanner in = new Scanner(System.in);
        final int width = in.nextInt();
        final int height = in.nextInt();
        final int myId = in.nextInt();
        in.nextLine();
        
        final Grid grid = new Grid(width, height, myId);

        // game loop
        while (true) {
            for (int i = 0; i < height; i++) {
                final String row = in.nextLine();
                grid.addRow(i, row);
            }
            final int entities = in.nextInt();
            for (int i = 0; i < entities; i++) {
                final int entityType = in.nextInt();
                final int owner = in.nextInt();
                final int x = in.nextInt();
                final int y = in.nextInt();
                final int param1 = in.nextInt();
                final int param2 = in.nextInt();
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
    @Override public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class BeCarfulStrategy extends Strategy {
    @Override public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
    
    public static boolean isSafetyPlace(final Cell cell, final List<Bomb> bombs) {
        final boolean isSafety = false;
        
        for (final Bomb bomb : bombs) {
            if (bomb.coordinates.x == cell.coordinates.x) {
                final int startY = bomb.rangeStartY(bomb.coordinates.y);
                final int endY = bomb.rangeEndY(bomb.coordinates.y, Grid.DEFAULT_HEIGHT);

                if (cell.coordinates.y <= endY || cell.coordinates.y >= startY) {
                    return true;
                }

            }else if (bomb.coordinates.y == cell.coordinates.y) {
                final int startX = bomb.rangeStartX(bomb.coordinates.x);
                final int endX = bomb.rangeEndX(bomb.coordinates.x, Grid.DEFAULT_HEIGHT);

                if (cell.coordinates.x <= endX || cell.coordinates.x >= startX) {
                    return true;
                }
            }
        }

        return isSafety;
    }


}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class DestroyStrategy extends Strategy {
    @Override public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
    
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class EatingStrategy extends Strategy {

    List<Item> items;
    
    public EatingStrategy(final List<Item> items) {
        this.items = items;
    }
    
    @Override public List<Action> makeActions(final List<Action> actions) {
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
    
    public Floor escapeTo(final FindOptimalPath<Floor> findOptimalPath, final Floor currentPlace,
            final List<Bomb> bombs) {
        
        final Map<Floor, Integer> placesWithDistance = findOptimalPath.getPlacesWithDistance(currentPlace);
        
        for (final Map.Entry<Floor, Integer> entry : placesWithDistance.entrySet()) {
            if (BeCarfulStrategy.isSafetyPlace(entry.getKey(), bombs)) {
                return entry.getKey();
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