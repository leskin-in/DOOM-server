package D592Client.GameObjects;

import java.awt.*;

/**
 * A cell of a {@link Field}
 */
public class Cell {
    public static final Dimension displaySize = new Dimension(24, 24);

    public int x;
    public int y;
    public Representation r;

    public Cell(int x, int y, Representation r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
}
