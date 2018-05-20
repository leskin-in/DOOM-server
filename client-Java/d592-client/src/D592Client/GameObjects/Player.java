package D592Client.GameObjects;


/**
 * A representation of the current game player
 */
public class Player extends Entity {
    public int health;
    public int healthPercent;
    public Weapon weapon;

    public Player() {
        this.x = 0;
        this.y = 0;
        this.r = null;
        this.health = 0;
        this.healthPercent = 0;  // This is a default health percent
        this.weapon = new Weapon();
    }
}
