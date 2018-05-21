package D592Client;

import D592Client.GameObjects.GameState;
import D592Client.NetUtils.NetWorker;
import D592Client.UserInterface.FieldIndicator;
import D592Client.UserInterface.PlayerdataIndicator;
import D592Client.UserInterface.UICommand;

import java.awt.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// TODO: To Observer (this is an `Observable` class)
/**
 * A game controller. Keeps the game going, manages all game logic objects
 */
public class GameThread implements Runnable {
    public static GameThread getInstance() {
        if (instance == null) {
            instance = new GameThread();
        }
        return instance;
    }

    /**
     * Setup the {@link GameThread}
     * @param nwk the network requests processor to use
     * @param fieldUI the output field
     * @param playerdataUI the output player data
     * @throws IllegalAccessException if the {@link GameThread} is already running
     * @throws IllegalArgumentException if the provided {@link NetWorker} is not connected
     */
    public void setup (
            Dimension fieldSize,
            NetWorker nwk,
            FieldIndicator fieldUI,
            PlayerdataIndicator playerdataUI
    ) throws IllegalAccessException, IllegalArgumentException {
        interactionLock.lock();
        try {
            if (this.isRunning) {
                throw new IllegalAccessException("The GameThread is running");
            }
            this.nwk = nwk;
            if (!nwk.isConnected()) {
                throw new IllegalArgumentException("The NetWorker is not connected");
            }
            this.fieldUI = fieldUI;
            this.playerdataUI = playerdataUI;

            this.gameState = new GameState(fieldSize.width, fieldSize.height);
            this.lastCommand = null;

            this.isSetupComplete = true;
        }
        finally {
            interactionLock.unlock();
        }
    }

    /**
     * Run the thread
     * Launch 'setup()' before calling this!
     */
    public void run() {
        interactionLock.lock();
        try {
            if (this.isRunning) {
                return;
            }
            if (!this.isSetupComplete) {
                System.err.println("Trying to run GameThread without proper setup; returning");
                return;
            }
            this.isRunning = true;
        }
        finally {
            interactionLock.unlock();
        }

        while (true) {

        }
    }

    /**
     * Rewrite (or add) a {@link UICommand} for this GameThread
     * @param uiCommand the command to execute
     */
    public void command(UICommand uiCommand) {
        interactionLock.lock();
        try {
            lastCommand = uiCommand;
        }
        finally {
            interactionLock.unlock();
        }
    }


    private static GameThread instance = null;

    private GameThread() {
        interactionLock = new ReentrantLock(true);
    }

    private NetWorker nwk;
    private FieldIndicator fieldUI;
    private PlayerdataIndicator playerdataUI;
    private boolean isSetupComplete = false;
    private boolean isRunning = false;
    private Lock interactionLock;

    private GameState gameState;
    private UICommand lastCommand;
}
