package hypersonic.cell;

import hypersonic.Coordinates;
import hypersonic.Grid;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public abstract class Cell{
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
