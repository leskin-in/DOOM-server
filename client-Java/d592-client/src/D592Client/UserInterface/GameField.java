package D592Client.UserInterface;

import java.awt.*;
import javax.swing.*;

/**
 * A panel with the game field
 */
public class GameField extends Panel {
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(0, 0, 15, 15);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(16, 16);
    }
}
