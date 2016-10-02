package hypersonic;


import hypersonic.cell.Box;
import hypersonic.cell.Cell;
import hypersonic.cell.CellFactory;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.entity.BomberMan;
import hypersonic.entity.Entity;
import hypersonic.entity.Item;
import hypersonic.graph.FindOptimalPath;
import hypersonic.graph.GraphFindAllPaths;
import hypersonic.graph.GraphMaker;
import hypersonic.graph.ReachableCalculator;
import hypersonic.strategy.EatingStrategy;
import hypersonic.util.DangerCalculator;

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
