package D592Client.GameObjects;

import java.util.ArrayList;

/**
 * A game field
 */
public class GameState {
    /**
     * Object constructor
     * @implNote There is a guarantee that after this call 'fieldCells' contains meaningful values
     */
    public GameState(int fieldWidth, int fieldHeight) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.fieldCells = new ArrayList<>(fieldWidth * fieldHeight);
        RepresentationFactory representationFactoryTemp = new RepresentationFactory();
        for (int y = 0; y < this.fieldHeight; y++) {
            for (int x = 0; x < this.fieldWidth; x++) {
                this.fieldCells.add(new Cell(
                        x,
                        y,
                        representationFactoryTemp.get(RepresentationFactory.REPR_RECT, Cell.displaySize)
                ));
            }
        }

        this.lastServerCommand = null;

        this.playerHealth = 0;
        this.playerHealthPercent = 0;

        this.weapon = "";
        this.weaponCharge = 0;
    }

    public int fieldWidth;
    public int fieldHeight;
    public ArrayList<Cell> fieldCells;

    public String lastServerCommand;

    public int playerHealth;
    public int playerHealthPercent;
    public Representation playerRepresentation;

    public String weapon;
    public int weaponCharge;
}
