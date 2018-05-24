package D592Client;

import D592Client.GameObjects.*;
import D592Client.NetUtils.NetWorker;
import D592Client.NetUtils.Packet;
import D592Client.NetUtils.PacketType;
import D592Client.UserInterface.IndicatorField;
import D592Client.UserInterface.IndicatorMessage;
import D592Client.UserInterface.IndicatorPlayer;
import D592Client.UserInterface.UICommand.UICommand;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
     * @throws IllegalAccessException if the {@link GameThread} is already running
     * @throws IllegalArgumentException if the provided {@link NetWorker} is not connected
     */
    public void setup (
            Dimension fieldSize,
            NetWorker nwk
    ) throws IllegalAccessException {
        interactionLock.lock();
        try {
            if (this.isRunning) {
                throw new IllegalAccessException("The GameThread is running");
            }
            this.nwk = nwk;
            this.fieldUIs = new LinkedList<>();
            this.playerUIs = new LinkedList<>();
            this.messageUIs = new LinkedList<>();

            this.gameState = new GameState(fieldSize.width, fieldSize.height);
            this.commandBuffer = null;
            this.tick = 0;

            this.isSetupComplete = true;
        }
        finally {
            interactionLock.unlock();
        }
    }

    /**
     * Subscribe for updates for {@link IndicatorField}
     * @param ind subscriber
     * @throws IllegalAccessException if this GameThread was not setup
     */
    public void subscribeIndicator(IndicatorField ind) throws IllegalAccessException {
        if (!this.isSetupComplete) {
            throw new IllegalAccessException("Trying to subscribe for a GameThread that was not setup");
        }
        this.fieldUIs.add(ind);
    }

    /**
     * Subscribe for updates for {@link IndicatorPlayer}
     * @param ind subscriber
     * @throws IllegalAccessException if this GameThread was not setup
     */
    public void subscribeIndicator(IndicatorPlayer ind) throws IllegalAccessException {
        if (!this.isSetupComplete) {
            throw new IllegalAccessException("Trying to subscribe for a GameThread that was not setup");
        }
        this.playerUIs.add(ind);
    }

    /**
     * Subscribe for updates for {@link IndicatorMessage}
     * @param ind subscriber
     * @throws IllegalAccessException if this GameThread was not setup
     */
    public void subscribeIndicator(IndicatorMessage ind) throws IllegalAccessException {
        if (!this.isSetupComplete) {
            throw new IllegalAccessException("Trying to subscribe for a GameThread that was not setup");
        }
        this.messageUIs.add(ind);
    }

    /**
     * Rewrite (or add) a {@link UICommand} for this GameThread
     * @param uiCommand the command to execute
     */
    public void command(UICommand uiCommand) {
        interactionLock.lock();
        try {
            commandBuffer = uiCommand;
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
        // Launch checks
        interactionLock.lock();
        try {
            if (this.isRunning) {
                return;
            }
            if (!this.isSetupComplete) {
                Client.doExit(1, "Trying to run GameThread without proper setup; returning");
                return;
            }
            if (!nwk.isConnected()) {
                Client.doExit(2, "Trying to run GameThread without a connected NetWorker; returning");
                return;
            }
            this.isRunning = true;
        }
        finally {
            interactionLock.unlock();
        }

        Packet buff;  // A packet to receive and send data to server
        int clientId;  // A signature for packets from this client

        // Non-game actions
        this.updateMessageUIs("Please wait");
        while (true) {
            try {
                buff = nwk.receive();
            }
            catch (IOException ex) {
                continue;
            }

            if (buff.getType() == PacketType.MESSAGE.getCode()) {
                String message = new String(buff.getData());
                this.updateMessageUIs(message);
            }
            else if ((buff.getType() == PacketType.GAME_PREPARE.getCode())
                    || (buff.getType() == PacketType.GAME_BEGIN.getCode())) {
                clientId = buff.getMeta();
                break;
            }
        }

        // Game starts
        this.updateMessageUIs("PREPARE!");
        while (buff.getType() == PacketType.GAME_PREPARE.getCode()) {
            try {
                buff = nwk.receive();
            }
            catch (IOException ex) {
                // pass
            }
        }
        this.updateMessageUIs("START");
        interactionLock.lock();
        commandBuffer = null;
        interactionLock.unlock();
        try {
            TimeUnit.MILLISECONDS.sleep(750);
        }
        catch (InterruptedException ex) {
            // pass
        }

        // In-game actions
        this.updateMessageUIs("GAME IS GOING");
        while (true) {
            // Receive a packet from server
            try {
                do {
                    buff = nwk.receive();
                } while (buff.getTick() <= tick);
            }
            catch (IOException ex) {
                // pass
            }

            // Execute client commands
            interactionLock.lock();
            UICommand lastUICommand = commandBuffer;
            commandBuffer = null;
            interactionLock.unlock();
            if (lastUICommand != null) {
                if (lastUICommand.isExecutable()) {
                    try {
                        lastUICommand.execute(nwk, tick, clientId);
                    }
                    catch (IOException ex) {
                        // pass
                    }
                }
                else {
                    switch(lastUICommand.toString()) {
                        default: {
                            // pass
                        }
                    }
                }
            }

            // Try to get new gameState or do in-game actions if the packet is not a usual one
            tick = buff.getTick();
            try {
                PacketDecoder.deserializeToField(buff, gameState);
            }
            catch (IOException ex) {
                if (buff.getType() == PacketType.GAME_OVER.getCode()) {
                    break;
                }
                continue;
            }

            // Update field and player indicators
            this.updateFieldUIs(gameState);
            this.updatePlayerUIs(
                    gameState.playerHealth, gameState.playerHealthPercent,
                    gameState.weapon, gameState.weaponCharge
            );
        }

        // Game finished. Note that the last packet was of type GAME_OVER
        buff.getData();
        buff.setType(PacketType.GAME);
        try {
            PacketDecoder.deserializeToField(buff, gameState);
        }
        catch (IOException ex) {
            gameState.lastServerCommand = "win";
        }

        endGame(gameState.lastServerCommand.equals("win"));

        try {
            TimeUnit.SECONDS.sleep(10);
        }
        catch (InterruptedException ex) {
            // pass
        }

        Client.doExit(0, "Game finished");
    }


    private static GameThread instance = null;

    private GameThread() {
        interactionLock = new ReentrantLock(true);
    }

    private void updateFieldUIs(GameState state) {
        for (IndicatorField ind : this.fieldUIs) {
            ind.updateField(state);
        }
    }

    private void updatePlayerUIs(int health, int healthPercent, String weapon, int weaponCharge) {
        for (IndicatorPlayer ind : this.playerUIs) {
            ind.updateHealth(health, healthPercent);
            ind.updateWeapon(weapon, weaponCharge);
        }
    }

    private void updateMessageUIs(String message) {
        for (IndicatorMessage ind : this.messageUIs) {
            ind.updateMessage(message);
        }
    }

    /**
     * Finish the game and update the GUI accordingly
     * @param isWin true if the current player has won the game
     *
     * @implNote This function must be called ONLY by 'run()',
     * as it requires a few objects to be initialized in advance
     */
    private void endGame(boolean isWin) {
        // Field
        Representation display = new RepresentationFactory().get(
                RepresentationFactory.REPR_RECT,
                Cell.displaySize,
                isWin ? new Color(255, 215, 0) : new Color(36, 9, 53)
        );
        for (int i = 0; i < gameState.fieldCells.size(); i++) {
            gameState.fieldCells.get(i).r = display;
        }
        this.updateFieldUIs(this.gameState);
        // Message
        if (isWin) {
            this.updateMessageUIs("\uD83C\uDFC6 You've WON! \uD83C\uDFC6");
        }
        else {
            this.updateMessageUIs("☹ You've lost ☹");
        }
    }

    private boolean isSetupComplete = false;
    private boolean isRunning = false;
    private Lock interactionLock;
    private NetWorker nwk;

    private GameState gameState;
    private UICommand commandBuffer;
    private long tick;

    private List<IndicatorField> fieldUIs;
    private List<IndicatorPlayer> playerUIs;
    private List<IndicatorMessage> messageUIs;
}
