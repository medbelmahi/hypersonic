package hypersonic.strategy;

import hypersonic.Action;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.entity.BomberMan;
import hypersonic.graph.FindOptimalPath;
import hypersonic.graph.GraphFindAllPaths;

import java.util.List;
import java.util.Set;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class DestroyStrategy extends Strategy {
    @Override public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
    
    public static Floor getBestReachablePlace(FindOptimalPath<Floor> findOptimalPath, GraphFindAllPaths<Floor> graph, final Set<Floor> places, List<Bomb> bombs, BomberMan bomberMan) {
        System.err.println("getBestReachablePlace -> size :" + places.size());
        for (Floor place : places) {
            if (BeCarfulStrategy.isSafetyPlace(place, bombs)) {

                Bomb bomb = new Bomb(bomberMan, place.coordinates.x, place.coordinates.y, 8, bomberMan.explosionRange);
                bombs.add(bomb);
                Floor escapeTo = EscapeStrategy.escapeTo(graph, place, bombs);
                bombs.remove(bomb);
                if (escapeTo != null) {
                    boolean safetyPath = BeCarfulStrategy.isSafetyPath(findOptimalPath, bombs, bomberMan.getCurrentPlace(), place);
                    if (safetyPath) {
                        return place;
                    }
                }
                initial(places);
            }
        }

        return null;
    }

    private static void initial(Set<Floor> places) {
        for (Floor place : places) {
            place.setNotSafetyPlace(false);
        }
    }
}
