package D592Client.UserInterface;

import D592Client.NetUtils.NetWorker;
import D592Client.NetUtils.Packet;
import D592Client.NetUtils.PacketType;

import java.io.IOException;


public interface UICommand {
    /**
     * Test if this command can be executed using the 'execute()' method
     *
     * If the command cannot be executed, use 'toString()' to identify it
     */
    public boolean isExecutable();

    /**
     * Execute a UI command
     * @throws IOException if an error happens when executing the command
     * @throws UnsupportedOperationException if execute() is not supported
     */
    public void execute(NetWorker nwk, long tick) throws IOException, UnsupportedOperationException;

    /**
     * @return a string representation of this command
     * @apiNote the output may be used to identify the command if it cannot be executed
     */
    public String toString();
}


abstract class UICommandExecutable implements UICommand {
    public UICommandExecutable(byte commandCode) {
        this.packet = new Packet(
                PacketType.CLIENT_ACTION,
                0,
                0,
                new byte[] {commandCode}
        );
    }

    public boolean isExecutable() {
        return true;
    }

    public void execute(NetWorker nwk, long tick) {
        packet.setTick(tick);
        try {
            nwk.send(packet);
        }
        catch (IOException ex) {
            // Do nothing; maybe the next command will be executed properly
        }
    }

    private Packet packet;
}


/**
 * 'CMD_W'
 */
class UICommandUp extends UICommandExecutable {
    public UICommandUp() {
        super((byte)'w');
    }

    public String toString() {
        return "CMD_W";
    }
}


/**
 * 'CMD_A'
 */
class UICommandLeft extends UICommandExecutable {
    public UICommandLeft() {
        super((byte)'a');
    }

    public String toString() {
        return "CMD_A";
    }
}


/**
 * 'CMD_S'
 */
class UICommandDown extends UICommandExecutable {
    public UICommandDown() {
        super((byte)'s');
    }

    public String toString() {
        return "CMD_S";
    }
}


/**
 * 'CMD_D'
 */
class UICommandRight extends UICommandExecutable {
    public UICommandRight() {
        super((byte)'d');
    }

    public String toString() {
        return "CMD_D";
    }
}


/**
 * 'CMD_WEAPON'
 */
class UICommandWeapon extends UICommandExecutable {
    public UICommandWeapon() {
        super((byte)'b');
    }

    public String toString() {
        return "CMD_WEAPON";
    }
}


/**
 * 'CMD_QUIT'
 */
class UICommandQuit extends UICommandExecutable {
    public UICommandQuit() {
        super((byte)'q');
    }

    public String toString() {
        return "CMD_QUIT";
    }
}


/**
 * 'CMD_NONE'
 */
class UICommandNone extends UICommandExecutable {
    public UICommandNone() {
        super((byte)' ');
    }

    public String toString() {
        return "CMD_NONE";
    }
}
