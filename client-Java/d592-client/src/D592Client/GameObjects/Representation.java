package D592Client.GameObjects;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * A representation of any in-game object that can be displayed on a screen.
 * This is a flyweight interface
 */
public interface Representation {
    void draw(Graphics g, Point2D location);
}
