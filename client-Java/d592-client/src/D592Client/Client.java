package D592Client;

import D592Client.UserInterface.Panel;
import D592Client.UserInterface.GameField;
import D592Client.UserInterface.InformationPanel;

import java.awt.*;
import javax.swing.*;

/**
 * A GUI invoker
 */
public class Client {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new ClientFrame();
            frame.setVisible(true);
        });
    }
}


/**
 * The main frame
 */
class ClientFrame extends JFrame {
    ClientFrame() {
        // Basic operations
        this.setTitle("DOOM-592 client");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setLayout(new GridBagLayout());

        // Calculate size of the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int actualHeight = Math.min(screenSize.width, screenSize.height);
        int actualWidth;
        if (screenSize.getWidth() - (double)(actualHeight * 5 / 4) < (Float.MIN_VALUE * 512)) {
            //noinspection SuspiciousNameCombination
            actualWidth = actualHeight;
        }
        else {
            actualWidth = actualHeight * 5 / 4;
        }
        actualHeight -= (int)((double)actualHeight * 0.125);
        actualWidth -= (int)((double)actualHeight * 0.125);
        this.setSize(actualWidth, actualHeight);

        // Create components
        Insets insets = this.getInsets();
        this.getContentPane().add(
                new GameField(),
                new GridBagConstraints(
                        0, 0,
                        3, 3,
                        0.0, 0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(insets.top, insets.left, 0, 0),
                        0, 0
                )
        );
        this.getContentPane().add(
                new InformationPanel(),
                new GridBagConstraints(
                        0, 3,
                        3, 1,
                        0.0, 100.0,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.VERTICAL,
                        new Insets(0, insets.left, insets.bottom, 0),
                        0, 0
                )
        );
        this.getContentPane().add(
                new Panel(),
                new GridBagConstraints(
                        3, 0,
                        1, 4,
                        100.0, 100.0,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH,
                        new Insets(insets.top, 0, insets.bottom, insets.right),
                        0, 0
                )
        );
    }
}
