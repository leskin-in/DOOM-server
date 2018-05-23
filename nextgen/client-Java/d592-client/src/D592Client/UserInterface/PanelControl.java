package D592Client.UserInterface;

import javax.swing.*;
import java.awt.*;


/**
 * A panel with game information
 */
public class PanelControl extends JPanel implements IndicatorPlayer {
    public PanelControl(Dimension displaySizeField) {
        this.displaySize = new Dimension(
                displaySizeField.width,
                (int)(displaySizeField.height * 0.15)
        );

        this.playerHealthInd = new JButton(
                String.format("♥ %04d", 0)
        );
        this.playerHealthInd.setEnabled(false);
        this.weaponInd = new JButton(
                "NONE"
        );
        this.weaponInd.setEnabled(false);
        this.weaponChargeInd = new JButton(
                String.format("%03d", 0)
        );
        this.weaponChargeInd.setEnabled(false);

        this.add(
                this.playerHealthInd,
                new GridBagConstraints(
                        0, 0,
                        1, 1,
                        0.0, 100.0,
                        GridBagConstraints.EAST,
                        GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0),
                        0, 0
                )
        );
        this.add(
                this.weaponInd,
                new GridBagConstraints(
                        1, 0,
                        1, 1,
                        100.0, 100.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0),
                        0, 0
                )
        );
        this.add(
                this.weaponChargeInd,
                new GridBagConstraints(
                        2, 0,
                        1, 1,
                        0.0, 100.0,
                        GridBagConstraints.WEST,
                        GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0),
                        0, 0
                )
        );
    }

    @Override
    public Dimension getPreferredSize() {
        return displaySize;
    }

    public void updateHealth(int health, int healthPercent) {
        this.playerHealthInd.setText(
                String.format("♥ %04d", health)
        );
        this.repaint();
    }

    public void updateWeapon(String weapon, int charge) {
        this.weaponInd.setText(
                weapon
        );
        this.weaponChargeInd.setText(
                String.format("%03d", charge)
        );
        this.repaint();
    }


    private final Dimension displaySize;

    private JButton playerHealthInd;
    private JButton weaponInd;
    private JButton weaponChargeInd;
}
