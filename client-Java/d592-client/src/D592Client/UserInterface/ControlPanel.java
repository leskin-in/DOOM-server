package D592Client.UserInterface;

import javax.swing.*;
import java.awt.*;


/**
 * A panel with game information
 */
public class ControlPanel extends JPanel implements PlayerdataIndicator {
    public ControlPanel(Dimension displaySizeField) {
        this.health = 0;
        this.healthPercent = 0;
        this.weapon = "";
        this.weaponCharge = 0;

        this.displaySize = new Dimension(
                displaySizeField.width,
                (int)(displaySizeField.height * 0.33)
        );
    }

    @Override
    public Dimension getPreferredSize() {
        return displaySize;
    }

    public void updateHealth(int health, int healthPercent) {
        this.health = health;
        this.healthPercent = healthPercent;
        this.repaint();
    }

    public void updateWeapon(String weapon, int charge) {
        this.weapon = weapon;
        this.weaponCharge = charge;
        this.repaint();
    }


    private final Dimension displaySize;

    private int health;
    private int healthPercent;
    private String weapon;
    private int weaponCharge;
}
