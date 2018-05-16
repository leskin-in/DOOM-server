package D592Client.NetUtils;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * A customizable packet of data.
 * This is a builder of {@link DatagramPacket}
 */
public class Packet {
    /**
     * Maximum possible size of a single UDP packet (in bytes)
     */
    public static final int MAX_SIZE_UDP = 65536;

    /**
     * Deserialize a packet received from the network
     *
     * @param bytes raw data from the network
     * @return new instance of {@link Packet}
     */
    public static Packet deserialize(byte[] bytes) {
        ByteBuffer deserializer = ByteBuffer.allocate(bytes.length);
        deserializer.put(bytes);
        deserializer.rewind();
        Packet result = new Packet();
        result.type = Integer.reverseBytes(deserializer.getInt());
        result.meta = Integer.reverseBytes(deserializer.getInt());
        result.tick = Long.reverseBytes(deserializer.getLong());
        if (!deserializer.hasRemaining()) {
            result.data = null;
        }
        else {
            result.data = new byte[deserializer.remaining()];
            deserializer.get(result.data);
        }
        return result;
    }

    /**
     * Construct a {@link Packet} of {@link PacketType} 'NONE'.
     */
    public Packet() {
        this(PacketType.NONE, StatusCode.ASK.getCode(), 0, null);
    }

    /**
     * Construct a customized {@link Packet}.
     *
     * @param type a {@link PacketType}
     * @param meta packet metadata. May be a {@link StatusCode}
     * @param tick in-game time for signing the packet
     * @param data packet content
     *
     * Note that at the moment 'tick' field may actually be used as metadata
     */
    public Packet(PacketType type, int meta, long tick, byte[] data) {
        this.type = type.getCode();
        this.meta = meta;
        this.tick = tick;
        this.data = (data == null) ? null : Arrays.copyOf(data, data.length);
    }

    /**
     * Create a {@link DatagramPacket} from the {@link Packet}.
     *
     * @param address Address to send packet to
     * @return A complete {@link DatagramPacket}
     */
    public DatagramPacket serialize(SocketAddress address) {
        int data_size = 0;
        if (this.data != null) {
            data_size = this.data.length;
        }
        ByteBuffer serializer = ByteBuffer.allocate(4 + 4 + 8 + data_size);
        serializer.order(ByteOrder.LITTLE_ENDIAN);
        serializer.putInt(this.type);
        serializer.putInt(this.meta);
        serializer.putLong(this.tick);
        if (this.data != null) {
            for (byte c : this.data) {
                serializer.put(c);
            }
        }
        byte[] serialized_data = serializer.array();
        return new DatagramPacket(serialized_data, serialized_data.length, address);
    }

    public void setType(PacketType newType) {
        this.type = newType.getCode();
    }

    public void setMeta(int newMeta) {
        this.meta = newMeta;
    }

    public void setTick(long newTick) {
        this.tick = newTick;
    }

    /**
     * @param newData will not be copied
     */
    public void setData(byte[] newData) {
        this.data = newData;
    }

    public int getType() {
        return type;
    }

    public int getMeta() {
        return meta;
    }

    public long getTick() {
        return tick;
    }

    /**
     * @return data field of the packet (not copied)
     */
    public byte[] getData() {
        return data;
    }

    // Packet values
    private int type;
    private int meta;
    private long tick;
    private byte[] data;
}
