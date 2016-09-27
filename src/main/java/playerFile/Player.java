import java.util.*;



/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Action {
    public static final String MOVE = "MOVE";
    public static final String BOMB = "BOMB";

    private String actionType;
    private Coordinates coordinates;


    Action(String actionType, Coordinates coordinates) {
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
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class ExtraRangeBox extends Box {
    ExtraRangeBox(int x, int y) {
        super(x, y);
    }
}




/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Floor extends Cell implements Comparable<Floor>{

    private int numberOfReachableBox;
    private boolean isReachable;

    Floor(int x, int y) {
        super(x, y);
    }

    public boolean isFreePlace() {
        return true;
    }

    public int value() {
        return 0;
    }

    public void setNumberOfReachableBox(int numberOfReachableBox) {
        this.numberOfReachableBox = numberOfReachableBox;
    }
    public int getNumberOfReachableBox() {
        return this.numberOfReachableBox;
    }

    public int compareTo(Floor o) {
        return this.numberOfReachableBox < o.numberOfReachableBox ? 1 : -1;
    }

    public boolean isReachable() {
        return isReachable;
    }

    public void setReachable(boolean reachable) {
        isReachable = reachable;
    }

    public <T extends Floor> LinkedList<Direction> getDirections(T destination) {
        return this.coordinates.getSortedDirection(destination.coordinates);
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
        return 0;
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


    public Bomb(BomberMan owner, int x, int y, int countDown, int range) {
        super(x, y, range);
        this.owner = owner;
        this.timer = new Timer(Timer.DEFAULT_DECREASED_TIME, countDown);
    }

    public boolean isHisOwner(BomberMan bomberMan) {
        return bomberMan.equals(owner);
    }
}


/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class BomberMan extends Entity {
    private int id;
    private int bombsCount;

    public BomberMan(int id, int x, int y, int bombsCount, int range) {
        super(x, y, range);
        this.id = id;
        this.bombsCount = bombsCount;
    }

    public boolean cantPlaceBomb() {
        return this.bombsCount < 1;
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
public enum Direction {
    RIGHT, LEFT, UP, DOWN
}




/**
 * Created by Mohamed BELMAHI on 27/09/2016.
 */
class FindOptimalPath<T extends Floor> {

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
class ReachabilityCalculator<T extends Floor> {
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





/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Grid{

    public static final int DEFAULT_WIDTH = 13;
    public static final int DEFAULT_HEIGHT = 11;

    private int width;
    private int height;
    private int myPlayerId;
    private Cell[][] cells;
    private List<Bomb> bombs;
    private List<Item> items;
    private Map<Integer, BomberMan> players;
    private BomberMan myPlayer;
    private GraphFindAllPaths<Floor> pathGraph;

    public Grid(int width, int height, int myPlayerId){
        this.width = width;
        this.height = height;
        this.myPlayerId = myPlayerId;
        this.cells = new Cell[height][width];
        this.bombs = new ArrayList<Bomb>();
        this.items = new ArrayList<Item>();
        this.players = new HashMap<Integer, BomberMan>();
    }

    public void addRow(int y, String row) {
        cells[y] = CellFactory.constructRow(y, row);
    }


    public void addEntity(int entityType, int owner, int x, int y, int param1, int param2) {

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
        this.myPlayer = players.get(myPlayerId);
        Action action = null;
        if (!myPlayer.cantPlaceBomb()) {
            Coordinates bestRichPlace = bestRichPlace();
            if (!bestRichPlace.equals(myPlayer.coordinates)) {
                action = new Action(Action.MOVE, bestRichPlace);
            }else{
                action = new Action(Action.BOMB, myPlayer.coordinates);
            }

        }else{
            Coordinates bestRichPlace = bestRichPlace();
            action = new Action(Action.MOVE, bestRichPlace);
        }

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

    private void setNumberOfReachableBox(int yy, int xx) {
        if (cells[yy][xx].isFreePlace()) {

            int yStart = myPlayer.rangeStartY(yy);
            int yEnd = myPlayer.rangeEndY(yy, height);
            int xStart = myPlayer.rangeStartX(xx);
            int xEnd = myPlayer.rangeEndX(xx, width);

            int numberOfBoxes = 0;

            for (int y = yStart; y < yEnd; y++) {
                numberOfBoxes += cells[y][xx].value();
            }
            for (int x = xStart; x < xEnd; x++) {
                numberOfBoxes += cells[yy][x].value();
            }

            ((Floor) cells[yy][xx]).setNumberOfReachableBox(numberOfBoxes);
        }
    }

    public void init() {
        myPlayer = players.get(myPlayerId);

        Set<Floor> places = new TreeSet<Floor>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] instanceof Floor) {
                    //setNumberOfReachableBox(y, x);
                    places.add((Floor) cells[y][x]);
                }
            }
        }
        this.pathGraph = GraphMaker.constructGraph(places, this.cells);

        ReachabilityCalculator<Floor> reachabilityCalculator = new ReachabilityCalculator<Floor>(this.pathGraph);
        reachabilityCalculator.setReachableCases((Floor) this.cells[myPlayer.coordinates.y][myPlayer.coordinates.x]);

        Iterator<Floor> iterator = places.iterator();
        while (iterator.hasNext()) {
            Floor floor = iterator.next();
            if (!floor.isReachable()) places.remove(floor);
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
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class DestroyStrategy extends Strategy {
    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class EatingStrategy extends Strategy {

    List<Item> items;

    public EatingStrategy(List<Item> items) {
        this.items = items;
    }

    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
class EscapeStrategy extends Strategy {
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }
}




/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
abstract class Strategy {

    public abstract List<Action> makeActions(List<Action> actions);

}