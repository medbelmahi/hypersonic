package hypersonic.strategy;

import hypersonic.Action;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.graph.FindOptimalPath;

import java.util.List;
import java.util.Map;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class EscapeStrategy extends Strategy {


    public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
    
    public Floor escapeTo(FindOptimalPath<Floor> findOptimalPath, Floor currentPlace, List<Bomb> bombs) {

        Map<Floor, Integer> placesWithDistance = findOptimalPath.getPlacesWithDistance(currentPlace);

        for (Map.Entry<Floor, Integer> entry : placesWithDistance.entrySet()) {
            if (BeCarfulStrategy.isSafetyPlace(entry.getKey(), bombs)) {
                return entry.getKey();
            }
        }

        return null;
    }


}
