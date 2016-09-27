package hypersonic.strategy;

import hypersonic.Action;

import java.util.List;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public abstract class Strategy {

    public abstract List<Action> makeActions(List<Action> actions);

}
