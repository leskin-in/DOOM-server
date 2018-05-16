package D592Client.GameObjects;


/**
 * A player (a real human player, not NPC)
 */
public class Player extends Unit {
    public Weapon weapon;
    public int healthPercent;

    public Player(int x, int y, int health, Weapon weapon) {
        super(x, y, health);
        this.weapon = weapon;
        healthPercent = 11;
    }
}
