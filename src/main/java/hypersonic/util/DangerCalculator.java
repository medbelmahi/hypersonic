package hypersonic.util;

import hypersonic.Grid;
import hypersonic.cell.Cell;
import hypersonic.cell.Floor;
import hypersonic.entity.Bomb;
import hypersonic.entity.Entity;
import hypersonic.entity.Item;

import java.util.List;
import java.util.Map;

/**
 * Created by Mohamed BELMAHI on 02/10/2016.
 */
public class DangerCalculator {

    private Map<Floor, Entity> floorEntityMap;

    public DangerCalculator(Map<Floor, Entity> floorEntityMap) {
        this.floorEntityMap = floorEntityMap;
    }

    public void setDangerToAllCells(List<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            scanXandYLineForBomb(bomb);
        }
    }

    public void scanXandYLineForBomb(Bomb bomb) {
        scanXLine(bomb);
        scanYLine(bomb);
    }

    private void scanXLine(Bomb bomb) {

        int y = bomb.coordinates.y;

        int endXRange = bomb.coordinates.x + (bomb.explosionRange - 1);
        for (int x = bomb.coordinates.x; x < Grid.DEFAULT_WIDTH; x++) {

            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedX = x - (neighbour.explosionRange - 1);
                    if (explodedX <= bomb.coordinates.x) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (x <= endXRange) {
                setNotSafety(bomb, (Floor) cell);
            }

        }

        int startX = bomb.coordinates.x - (bomb.explosionRange - 1);

        for (int x = bomb.coordinates.x-1; x >= 0; x--) {
            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedX = x + (neighbour.explosionRange - 1);
                    if (explodedX >= bomb.coordinates.x) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (x >= startX) {
                setNotSafety(bomb, (Floor) cell);
            }

        }
    }

    private void setNotSafety(Bomb bomb, Floor cell) {
        Floor floor = cell;
        floor.setNotSafetyPlace(true);
        floor.setBomb(bomb);
    }

    private void scanYLine(Bomb bomb) {

        int x = bomb.coordinates.x;

        int endYRange = bomb.coordinates.y + (bomb.explosionRange - 1);
        for (int y = bomb.coordinates.y; y < Grid.DEFAULT_HEIGHT; y++) {

            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedY = y - (neighbour.explosionRange - 1);
                    if (explodedY <= bomb.coordinates.y) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (y <= endYRange) {
                setNotSafety(bomb, (Floor) cell);
            }

        }

        int startYRange = bomb.coordinates.y - (bomb.explosionRange - 1);

        for (int y = bomb.coordinates.y-1; y >= 0; y--) {
            Cell cell = Grid.cells[y][x];
            Entity entity = floorEntityMap.get(cell);
            boolean entityNotNull = entity != null;
            if (goBreak(cell, entity, entityNotNull)) break;

            if (entityNotNull && entity instanceof Bomb) {
                Bomb neighbour = (Bomb) entity;
                if (neighbour.getCountDown() < bomb.getCountDown()) {
                    int explodedY = y + (neighbour.explosionRange - 1);
                    if (explodedY >= bomb.coordinates.y) {
                        bomb.setCountDown(neighbour.getCountDown());
                    }
                }
            }

            if (y >= startYRange) {
                setNotSafety(bomb, (Floor) cell);
            }

        }
    }

    private boolean goBreak(Cell cell, Entity entity, boolean entityNotNull) {
        if (!cell.isFreePlace() || (entityNotNull && entity instanceof Item)) {
            return true;
        }
        return false;
    }
}
