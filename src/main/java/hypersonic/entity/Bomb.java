package hypersonic.entity;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class Bomb extends Entity {
    private Timer timer;
    private BomberMan owner;
    private int countDown;


    public Bomb(BomberMan owner, int x, int y, int countDown, int range) {
        super(x, y, range);
        this.owner = owner;
        this.timer = new Timer(Timer.DEFAULT_DECREASED_TIME, countDown);
        this.countDown = countDown;
    }

    public boolean isHisOwner(BomberMan bomberMan) {
        return bomberMan.equals(owner);
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }
}