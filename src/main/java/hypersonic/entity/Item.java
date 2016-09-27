package hypersonic.entity;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class Item extends Entity {
    private int id;
    public Item(int owner, int x, int y, int id, int explosionRange) {
        super(x, y, explosionRange);
        this.id = id;
    }
}