package hypersonic.cell;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class ExtraRangeBox extends Box {
    ExtraRangeBox(int x, int y) {
        super(x, y);
    }

    @Override
    public int value() {
        return 2;
    }
}
