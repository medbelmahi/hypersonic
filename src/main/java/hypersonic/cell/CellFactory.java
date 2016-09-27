package hypersonic.cell;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class CellFactory {
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