package hypersonic.entity;

import hypersonic.Coordinates;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public abstract class Entity {

    public static final int BOMBER_MAN_TYPE = 0;
    public static final int BOMB_TYPE = 1;
    public static final int ITEM_TYPE = 2;

    public int explosionRange;

    public Coordinates coordinates;


    public Entity(int x, int y, int explosionRange) {
        this.coordinates = new Coordinates(x, y);
        this.explosionRange = explosionRange;
    }

    public int rangeStartY(int y) {
        int startY = y - (explosionRange - 1);
        return startY < 0 ? 0 : startY;
    }

    public int rangeStartX(int x) {
        int startX = x - (explosionRange - 1);
        return startX < 0 ? 0 : startX;
    }

    public int rangeEndY(int y, int height) {
        int endY = y + ((explosionRange - 1));
        return endY < height ? endY : (height - 1);
    }

    public int rangeEndX(int x, int width) {
        int endX = x + ((explosionRange - 1));
        return endX < width ? endX : (width - 1);
    }

    public void update(int x, int y, int explosionRange) {
        this.coordinates = new Coordinates(x, y);
        this.explosionRange = explosionRange;
    }
}