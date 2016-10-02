package hypersonic.strategy;

import hypersonic.Action;
import hypersonic.Grid;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.entity.Item;
import hypersonic.graph.FindOptimalPath;

import java.util.List;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class EatingStrategy extends Strategy {

    List<Item> items;
    private FindOptimalPath<Floor> findOptimalPath;

    public EatingStrategy(List<Item> items, FindOptimalPath<Floor> findOptimalPath) {
        this.items = items;
        this.findOptimalPath = findOptimalPath;
    }

    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }

    public Action eatIt(List<Bomb> bombs, Floor currentPlace) {
        for (Item item : this.items) {
            Floor itemCell = (Floor) Grid.cells[item.coordinates.y][item.coordinates.x];
            if (itemCell.isReachable()) {
                List<Floor> optimalPath = findOptimalPath.getOptimalPath(currentPlace, itemCell);
                if (optimalPath.size() < 3) {
                    if (BeCarfulStrategy.isSafetyPath(optimalPath, bombs)) {
                        return new Action(Action.MOVE, itemCell.coordinates);
                    }
                }
            }
        }

        return null;
    }
}
