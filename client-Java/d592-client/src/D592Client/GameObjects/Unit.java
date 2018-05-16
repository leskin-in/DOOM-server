package D592Client.GameObjects;


/**
 * A game unit
 */
public abstract class Unit {
    public int health;
    public int x;
    public int y;

    public Unit() {
        this(0, 0, 0);
    }

    public Unit(int x, int y, int health) {
        this.health = health;
        this.x = x;
        this.y = y;
    }
}
