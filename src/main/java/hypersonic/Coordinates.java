package hypersonic;

import hypersonic.graph.Direction;

import java.util.LinkedList;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class Coordinates {
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