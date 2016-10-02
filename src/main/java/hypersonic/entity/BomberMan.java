package hypersonic.entity;

import hypersonic.Action;
import hypersonic.Grid;
import hypersonic.cell.Box;
import hypersonic.cell.Cell;
import hypersonic.cell.Floor;
import hypersonic.graph.FindOptimalPath;
import hypersonic.graph.GraphFindAllPaths;
import hypersonic.strategy.BeCarfulStrategy;
import hypersonic.strategy.DestroyStrategy;
import hypersonic.strategy.EatingStrategy;
import hypersonic.strategy.EscapeStrategy;

import java.util.List;
import java.util.Set;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class BomberMan extends Entity {
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