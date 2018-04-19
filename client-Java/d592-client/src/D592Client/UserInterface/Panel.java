package D592Client.UserInterface;

import java.awt.*;
import javax.swing.*;

/**
 * A base class for all panels used.
 * Can also be used for testing purposes, as it defines the methods of {@link JPanel}
 */
public class Panel extends JPanel {
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, 15, 15);
        g.setColor(Color.GREEN);
        g.fillOval(0, 0, 15, 15);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(16, 16);
    }
}
