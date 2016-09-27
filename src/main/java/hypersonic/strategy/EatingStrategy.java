package hypersonic.strategy;

import hypersonic.Action;
import hypersonic.entity.Item;

import java.util.List;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class EatingStrategy extends Strategy {

    List<Item> items;

    public EatingStrategy(List<Item> items) {
        this.items = items;
    }

    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }
}
