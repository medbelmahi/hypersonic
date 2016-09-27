package hypersonic.entity;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
class Timer{
    public static final int DEFAULT_STARTED_TIME = 8;
    public static final int DEFAULT_DECREASED_TIME = 1;
    int decreasedTime;
    int countDown;

    public Timer(int decreasedTime, int countDown) {
        this.decreasedTime = decreasedTime;
        this.countDown = countDown;
    }
}
