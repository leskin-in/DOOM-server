package D592Client.UserInterface;

import D592Client.GameObjects.Cell;
import D592Client.GameObjects.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A panel with the game field
 */
public class PanelField extends JPanel implements IndicatorField {
    public PanelField(int widthInCells, int heightInCells) {
        this.displaySize = new Dimension(
                widthInCells * Cell.displaySize.width,
                heightInCells * Cell.displaySize.height
        );
        this.field = new GameState(widthInCells, heightInCells).fieldCells;
        this.fieldSize = new Dimension(widthInCells, heightInCells);
    }

    @Override
    public void paintComponent(Graphics g) {
        Iterator<Cell> c = field.iterator();
        for (int y = 0; y < fieldSize.height; y++) {
            for (int x = 0; x < fieldSize.width; x++) {
                if (!c.hasNext()) {
                     // This could not have happened
                    System.err.println("ERR: Game field size is inconsistent with display size");
                }
                c.next().r.draw(g, new Point(x * Cell.displaySize.width, y * Cell.displaySize.height));
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return displaySize;
    }

    public void updateField(GameState state) {
        this.field = state.fieldCells;
        this.repaint();
    }

    private final Dimension displaySize;

    private ArrayList<Cell> field;
    private Dimension fieldSize;
}
