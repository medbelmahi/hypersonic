package hypersonic.entity;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class BomberMan extends Entity {
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