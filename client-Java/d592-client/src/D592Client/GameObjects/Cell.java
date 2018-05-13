package D592Client.GameObjects;

import java.util.*;

/**
 * A cell of a {@link Field}
 */
public class Cell {
    public int x;
    public int y;
    public Representation r;

    public Cell() {
        units = new TreeSet<>();
        entities = new ArrayList<>();
    }
    public Cell(int x, int y, Representation r) {
        this();
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Iterator<Unit> getUnits() {
        return units.iterator();
    }
    public void addUnit(Unit unit) {
        units.add(unit);
    }
    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public Iterator<Entity> getEntities() {
        return entities.iterator();
    }
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
    public void removeEntities(Entity entity) {
        //noinspection StatementWithEmptyBody
        while (entities.remove(entity)) {}
    }

    private Set<Unit> units;
    private List<Entity> entities;
}
