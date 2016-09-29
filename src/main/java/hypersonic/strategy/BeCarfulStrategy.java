package hypersonic.strategy;

import hypersonic.Action;
import hypersonic.Grid;
import hypersonic.cell.Cell;
import hypersonic.entity.Bomb;

import java.util.List;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class BeCarfulStrategy extends Strategy {
    @Override
    public List<Action> makeActions(List<Action> actions) {
        return null;
    }


    public boolean isSafetyPlace(Cell cell, List<Bomb> bombs) {
        boolean isSafety = false;

        for (Bomb bomb : bombs) {
            if (bomb.coordinates.x == cell.coordinates.x) {
                int startY = bomb.rangeStartY(bomb.coordinates.y);
                int endY = bomb.rangeEndY(bomb.coordinates.y, Grid.DEFAULT_HEIGHT);

                if (cell.coordinates.y <= endY || cell.coordinates.y >= startY) {
                    return true;
                }

            }else if (bomb.coordinates.y == cell.coordinates.y) {
                int startX = bomb.rangeStartX(bomb.coordinates.x);
                int endX = bomb.rangeEndX(bomb.coordinates.x, Grid.DEFAULT_HEIGHT);

                if (cell.coordinates.x <= endX || cell.coordinates.x >= startX) {
                    return true;
                }
            }
        }

        return isSafety;
    }


}
