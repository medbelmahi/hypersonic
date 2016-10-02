package hypersonic.strategy;

import hypersonic.Action;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.graph.Direction;
import hypersonic.graph.GraphFindAllPaths;

import java.util.List;
import java.util.Map;

/**
 * Created by Mohamed BELMAHI on 26/09/2016.
 */
public class EscapeStrategy extends Strategy {


    public List<Action> makeActions(final List<Action> actions) {
        return null;
    }
    
    public static Floor escapeTo(GraphFindAllPaths<Floor> graph, Floor currentPlace, List<Bomb> bombs) {

        Floor escapeTo = recursive(currentPlace, graph, bombs);
        initSafetyGraph(currentPlace, graph);
        return escapeTo;
    }

    private static void initSafetyGraph(Floor currentPlace, GraphFindAllPaths<Floor> graph) {
        if (!currentPlace.isNotSafetyPlace()) {
            return;
        }

        currentPlace.setNotSafetyPlace(false);

        Map<Floor, Direction> edgesFrom = graph.edgesFrom(currentPlace);

        if (edgesFrom != null) {
            for (Floor sousElement : edgesFrom.keySet()) {
                initSafetyGraph(sousElement, graph);
            }
        }
    }

    private static Floor recursive(Floor floor, GraphFindAllPaths<Floor> graph, List<Bomb> bombs) {
        if (BeCarfulStrategy.isSafetyPlace(floor, bombs)) {
            System.err.println("safety place : " + floor.toString());
            return floor;
        }else{
            floor.setNotSafetyPlace(true);
        }

        Map<Floor, Direction> edgesFrom = graph.edgesFrom(floor);


        if (edgesFrom != null) {
            for (Floor sousElement : edgesFrom.keySet()) {
                if (!sousElement.isNotSafetyPlace()) {
                    Floor recursive = recursive(sousElement, graph, bombs);
                    if (recursive != null) {
                        return recursive;
                    }
                }
            }
        }

        return null;
    }


}
