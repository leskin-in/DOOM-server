package D592Client.NetUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * A customizable packet of data.
 * This is a factory of {@link DatagramPacket} with additional capabilities.
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
     * @return this
     * @throws IOException if a parsing error occurs
     */
    public static Packet deserialize(byte[] bytes) throws IOException {
        Packet result = new Packet();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            try (DataInputStream dis = new DataInputStream(bais)) {
                result.type = dis.readInt();
                result.meta = dis.readInt();
                result.tick = dis.readLong();
                while (dis.available() > 0) {
                    result.data.add((char)dis.readUnsignedByte());
                }
            }
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
    public Packet(PacketType type, int meta, long tick, List<Character> data) {
        this.type = type.getCode();
        this.meta = meta;
        this.tick = tick;
        this.data = (data == null) ? null : new ArrayList<Character>(data);
    }

    /**
     * Create a {@link DatagramPacket} from the {@link Packet}.
     *
     * @param address Address to send packet to
     * @return A complete {@link DatagramPacket}
     * @throws IOException if a serialization error occurs
     */
    public DatagramPacket serialize(SocketAddress address) throws IOException {
        byte[] serialized_data;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(MAX_SIZE_UDP)) {
            try (DataOutputStream dos = new DataOutputStream(baos)) {
                dos.writeInt(this.type);
                dos.writeInt(this.meta);
                dos.writeLong(this.tick);
                for (char c : this.data) {
                    dos.writeByte(c);
                }
                dos.flush();
            }
            serialized_data = baos.toByteArray();
        }
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
     * Set 'data' field for the packet.
     * @param newData will NOT be copied! The provided instance will be used directly
     */
    public void setData(List<Character> newData) {
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
     * @return data field that is NOT copied! The returned instance is the same as in this packet
     */
    public List<Character> getData() {
        return data;
    }

    // Packet values
    private int type;
    private int meta;
    private long tick;
    private List<Character> data;
}
