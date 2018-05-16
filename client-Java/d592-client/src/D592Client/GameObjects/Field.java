package D592Client.GameObjects;


import java.awt.*;
import java.util.ArrayList;

/**
 * A game field
 */
public class Field {
    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new ArrayList<>(width * height);
    }

    public ArrayList<Cell> cells;
    public int width;
    public int height;
}
