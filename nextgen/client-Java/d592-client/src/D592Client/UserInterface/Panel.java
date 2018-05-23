package D592Client.UserInterface;

import java.awt.*;
import javax.swing.*;

/**
 * An empty panel
 */
public class Panel extends JPanel {
    private Dimension displaySize;

    public Panel(Dimension panelSize) {
        this.displaySize = new Dimension(panelSize);
    }

    @Override
    public void paintComponent(Graphics g) {
        Color previous = g.getColor();
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, displaySize.width, displaySize.height);
        g.setColor(previous);
    }

    @Override
    public Dimension getPreferredSize() {
        return displaySize;
    }
}
