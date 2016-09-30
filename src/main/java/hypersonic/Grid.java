package hypersonic;


import hypersonic.cell.Cell;
import hypersonic.cell.CellFactory;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.entity.BomberMan;
import hypersonic.entity.Entity;
import hypersonic.entity.Item;
import hypersonic.graph.*;

import java.util.*;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class Grid {
    
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
    private FindOptimalPath<Floor> findOptimalPath;
    private ReachableCalculator<Floor> reachableCalculator;
    
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
        
        this.reachableCalculator = new ReachableCalculator<Floor>(this.pathGraph, cells, myPlayer);
        reachableCalculator.setReachableCases((Floor) this.cells[myPlayer.coordinates.y][myPlayer.coordinates.x]);
        
        final Iterator<Floor> iterator = places.iterator();
        while (iterator.hasNext()) {
            final Floor floor = iterator.next();
            if (!floor.isReachable())
                places.remove(floor);
        }
        
        this.findOptimalPath = new FindOptimalPath<>(this.pathGraph);
    }
}
