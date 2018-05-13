package D592Client.GameObjects;


import java.util.ArrayList;
import java.util.List;

/**
 * A game field
 */
public class Field {
    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new ArrayList<>(width * height);
    }

    public List<Cell> cells;
    public int width;
    public int height;
}
