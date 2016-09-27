package hypersonic;

/**
 * Created by Mohamed BELMAHI on 25/09/2016.
 */
public class Action {
    public static final String MOVE = "MOVE";
    public static final String BOMB = "BOMB";

    private String actionType;
    private Coordinates coordinates;


    Action(String actionType, Coordinates coordinates) {
        this.actionType = actionType;
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return actionType + " " + coordinates.toString();
    }
}
