package hypersonic.cell;

import hypersonic.graph.Direction;

import java.util.LinkedList;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class Floor extends Cell implements Comparable<Floor>{

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
