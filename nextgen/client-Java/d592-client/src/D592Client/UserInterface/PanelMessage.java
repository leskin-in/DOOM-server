package D592Client.UserInterface;

import javax.swing.*;
import java.awt.*;

public class PanelMessage extends JPanel implements IndicatorMessage {
    public PanelMessage(Dimension displaySizeField) {
        this.displaySize = new Dimension(
                displaySizeField.width,
                (int)(displaySizeField.height * 0.10)
        );

        textLabel = new JLabel("");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(textLabel);
    }

    @Override
    public Dimension getPreferredSize() {
        return displaySize;
    }

    @Override
    public void updateMessage(String message) {
        this.textLabel.setText(message);
    }

    private Dimension displaySize;

    private JLabel textLabel;
}
