package D592Client.UserInterface;

import D592Client.GameObjects.Cell;
import D592Client.GameObjects.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;


/**
 * A panel with the game field
 */
public class GameField extends JPanel implements FieldIndicator {
    public GameField(int widthInCells, int heightInCells) {
        this.gameState = new GameState(widthInCells, heightInCells);
        this.displaySize = new Dimension(
                this.gameState.fieldWidth * Cell.displaySize.width,
                this.gameState.fieldHeight * Cell.displaySize.height
        );
    }

    @Override
    public void paintComponent(Graphics g) {
        Iterator<Cell> c = gameState.fieldCells.iterator();
        for (int y = 0; y < gameState.fieldHeight; y++) {
            for (int x = 0; x < gameState.fieldWidth; x++) {
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
        this.gameState = state;
        this.repaint();
    }


    private final Dimension displaySize;

    private GameState gameState;
}
