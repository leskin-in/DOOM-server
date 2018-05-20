package D592Client.GameObjects;

/**
 * A class that unites all game logic components together
 */
public class Game {
    public Field field;
    public Player player;
    public String last_command;

    public Game(int field_width, int field_height) {
        field = new Field(field_width, field_height);
        player = new Player();
        last_command = null;
    }
}
