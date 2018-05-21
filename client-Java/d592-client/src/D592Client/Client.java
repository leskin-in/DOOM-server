package D592Client;

import D592Client.NetUtils.NetWorker;
import D592Client.UserInterface.Panel;
import D592Client.UserInterface.GameField;
import D592Client.UserInterface.ControlPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
        //
        /* Basic settings */
        //
        this.setTitle("DOOM-592 client");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setLayout(new GridBagLayout());
        nwk = NetWorker.getInstance();

        //
        /* Create components */
        //
        gameField = new GameField(11, 11);
        controlPanel = new ControlPanel(gameField.getPreferredSize());
        spacer = new Panel(new Dimension(
                (int)(gameField.getPreferredSize().width * 0.25),
                gameField.getPreferredSize().height + controlPanel.getPreferredSize().height
        ));
        this.setSize(
                gameField.getPreferredSize().width + spacer.getPreferredSize().width,
                gameField.getPreferredSize().height + controlPanel.getPreferredSize().height
        );

        gameThread = GameThread.getInstance();
        gameThread.setup(
                
        )

        //
        /* Add components */
        //
        Insets insets = this.getInsets();
        this.getContentPane().add(
                gameField,
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
                controlPanel,
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
                spacer,
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

        //
        /* Create and add actions */
        //
        InputMap gameFieldInputMap = this.gameField.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap gameFieldActionMap = this.gameField.getActionMap();

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0),         "move.up");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),        "move.up");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, 0),     "move.up");
        gameFieldActionMap.put("move.up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.err.println("UP");
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),         "move.left");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),      "move.left");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, 0),   "move.left");
        gameFieldActionMap.put("move.left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.err.println("LEFT");
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),         "move.down");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),      "move.down");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, 0),   "move.down");
        gameFieldActionMap.put("move.down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.err.println("DOWN");
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),         "move.right");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),     "move.right");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, 0),  "move.right");
        gameFieldActionMap.put("move.right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.err.println("RIGHT");
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0),         "action.bomb");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0),         "action.bomb");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0),         "action.bomb");
        gameFieldActionMap.put("action.bomb", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.err.println("BOMB");
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),    "action.exit");
        gameFieldActionMap.put("action.exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.err.println("EXIT");
            }
        });


    }

    private GameField gameField;
    private ControlPanel controlPanel;
    private Panel spacer;

    private NetWorker nwk;
    private Thread threadKeeper;
    private GameThread gameThread;
}
