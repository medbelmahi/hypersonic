package hypersonic.cell;

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
