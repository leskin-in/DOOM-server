package D592Client;

import D592Client.NetUtils.NetWorker;
import D592Client.UserInterface.PanelMessage;
import D592Client.UserInterface.PanelField;
import D592Client.UserInterface.PanelControl;
import D592Client.UserInterface.UICommand.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.swing.*;


/**
 * A GUI invoker
 */
public class Client {
    public static void main(String[] args) {
        // Parse arguments
        if ((args.length < 1) || (args.length > 1)) {
            printHelp();
            return;
        }
        String serverUrl = args[0];

        // Launch GUI
        EventQueue.invokeLater(() -> {
            JFrame frame = new ClientFrame(serverUrl);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void printHelp() {
        System.out.println("Usage: java d592client <server_url>");
        System.out.println("A DOOM-592 Java client");
        System.out.println("https://github.com/leskin-in/DOOM-server");
        System.out.println();
    }

    public static synchronized void doExit(int code) {
        System.out.flush();
        if (code != 0) {
            System.err.println("Exit code " + Integer.toString(code));
            System.err.flush();
        }
        System.exit(code);
    }

    public static void doExit(int code, String reason) {
        System.out.flush();
        System.err.flush();
        System.out.println("Exiting (" + reason + ")");
        doExit(code);
    }
}


/**
 * The main frame
 */
class ClientFrame extends JFrame {
    ClientFrame(String serverUrl) {
        //
        /* Basic settings */
        //
        this.setTitle("D592 Client");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setLayout(new GridBagLayout());
        nwk = NetWorker.getInstance();

        //
        /* Create components */
        //
        panelField = new PanelField(11, 11);
        panelControl = new PanelControl(panelField.getPreferredSize());
        panelMessage = new PanelMessage(panelField.getPreferredSize());
        this.setSize(
                (int)(panelField.getPreferredSize().width * 1.2),
                (int)(1.2 * (panelField.getPreferredSize().height
                        + panelControl.getPreferredSize().height
                        + panelMessage.getPreferredSize().height))
        );
        gameThread = GameThread.getInstance();
        try {
            gameThread.setup(
                    new Dimension(11, 11),
                    nwk
            );
            gameThread.subscribeIndicator(panelField);
            gameThread.subscribeIndicator(panelControl);
            gameThread.subscribeIndicator(panelMessage);
        }
        catch (IllegalAccessException ex) {
            Client.doExit(1, "Cannot setup GameThread");
        }

        //
        /* Add components */
        //
        Insets insets = this.getInsets();
        this.add(
                panelMessage,
                new GridBagConstraints(
                        0, 0,
                        1, 1,
                        0.0, 100.0,
                        GridBagConstraints.SOUTH,
                        GridBagConstraints.VERTICAL,
                        new Insets(insets.top, insets.left, 0, insets.right),
                        0, 0
                )
        );
        this.getContentPane().add(
                panelField,
                new GridBagConstraints(
                        0, 1,
                        1, 1,
                        0.0, 0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.NONE,
                        new Insets(0, insets.left, 0, insets.right),
                        0, 0
                )
        );
        this.getContentPane().add(
                panelControl,
                new GridBagConstraints(
                        0, 2,
                        1, 1,
                        0.0, 100.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.VERTICAL,
                        new Insets(0, insets.left, insets.bottom, insets.right),
                        0, 0
                )
        );

        //
        /* Connect to server */
        //
        try {
            System.out.println("Connecting to " + serverUrl + ":59200 ...");
            nwk.connect(new InetSocketAddress(serverUrl, 59200));
        }
        catch (IOException ex) {
            System.out.println("Connection failure");
            Client.doExit(2, ex.getMessage());
            return;
        }
        System.out.println("Connection successful!");

        //
        /* Create and add actions */
        //
        InputMap gameFieldInputMap = this.panelField.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap gameFieldActionMap = this.panelField.getActionMap();

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0),         "move.up");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),        "move.up");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, 0),     "move.up");
        gameFieldActionMap.put("move.up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameThread.command(new UICommandUp());
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),         "move.left");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),      "move.left");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, 0),   "move.left");
        gameFieldActionMap.put("move.left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameThread.command(new UICommandLeft());
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),         "move.down");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),      "move.down");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, 0),   "move.down");
        gameFieldActionMap.put("move.down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameThread.command(new UICommandDown());
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),         "move.right");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),     "move.right");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, 0),  "move.right");
        gameFieldActionMap.put("move.right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameThread.command(new UICommandRight());
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0),         "action.bomb");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0),         "action.bomb");
        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0),         "action.bomb");
        gameFieldActionMap.put("action.bomb", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameThread.command(new UICommandWeapon());
            }
        });

        gameFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),    "action.exit");
        gameFieldActionMap.put("action.exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameThread.command(new UICommandQuit());
            }
        });

        //
        /* Launch the game */
        //
        threadKeeper = new Thread(null, gameThread, "GameThread");
        threadKeeper.start();
    }

    private PanelField panelField;
    private PanelControl panelControl;
    private PanelMessage panelMessage;

    private NetWorker nwk;
    private Thread threadKeeper;
    private GameThread gameThread;
}
