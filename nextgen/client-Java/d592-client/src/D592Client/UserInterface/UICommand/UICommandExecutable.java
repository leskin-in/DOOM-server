package D592Client.UserInterface.UICommand;

import D592Client.NetUtils.NetWorker;
import D592Client.NetUtils.Packet;
import D592Client.NetUtils.PacketType;

import java.io.IOException;

public abstract class UICommandExecutable implements UICommand {
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

    public void execute(NetWorker nwk, long tick, int senderId) {
        packet.setMeta(senderId);
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
