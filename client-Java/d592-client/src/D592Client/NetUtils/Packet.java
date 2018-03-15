package D592Client.NetUtils;

/**
 * A packet of data to send through the network
 */
public class Packet {
    public int type;
    public int meta;
    public long tick;
    public byte[] data;

    /**
     * Construct a fully customizable packet of data
     *
     * @param type packet type. Must be one of the values from {@link PacketType}
     * @param meta packet metadata. May be one of the values from {@link StatusCode}
     * @param tick in-game time associated with this packet
     * @param data packet contents
     *
     * Note that at the moment 'tick' field may actually be used as metadata
     */
    Packet(PacketType type, int meta, long tick, byte[] data) {
        this.type = type.getCode();
        this.meta = meta;
        this.tick = tick;
        this.data = data;
    }
}
