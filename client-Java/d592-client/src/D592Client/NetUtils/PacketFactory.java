package D592Client.NetUtils;

import D592Client.Ticker;

/**
 * A factory of {@link Packet}
 */
public class PacketFactory {
    /**
     * Create a new factory
     * @param ticker {@link Ticker} to use to produce packet timestamps
     */
    PacketFactory(Ticker ticker) {
        this.ticker = ticker;
    }

    /**
     * @return an empty {@link Packet}
     */
    public Packet make() {
        return new Packet(this.type, this.meta, this.ticker.getNow(), null);
    }

    /**
     * @param data data to store in {@link Packet}
     * @return a {@link Packet} with the data set appropriately
     */
    public Packet make(byte[] data) {
        return new Packet(this.type, this.meta, this.ticker.getNow(), data);
    }

    /**
     * @param meta metadata to store in {@link Packet}
     * @param data data to store in {@link Packet}
     * @return a {@link Packet} with the data and 'meta' set appropriately
     */
    public Packet make(int meta, byte[] data) {
        return new Packet(this.type, meta, this.ticker.getNow(), data);
    }

    /**
     * @param meta {@link StatusCode} to store in {@link Packet}
     * @param data data to store in {@link Packet}
     * @return a {@link Packet} with the data and 'meta' set appropriately
     */
    public Packet make(StatusCode meta, byte[] data) {
        return new Packet(this.type, meta.getCode(), this.ticker.getNow(), data);
    }


    /**
     * @return current {@link PacketType} of packets this factory creates
     */
    public PacketType getType() {
        return type;
    }

    /**
     * Set new {@link PacketType} for packets this factory creates
     * @param newType new packet type
     */
    public void setType(PacketType newType) {
        this.type = newType;
    }

    /**
     * @return current 'meta' of packets this factory creates
     */
    public int getMeta() {
        return meta;
    }

    /**
     * Set new 'meta' for packets this factory creates
     * @param newMeta new 'meta'
     */
    public void setMeta(int newMeta) {
        this.meta = newMeta;
    }

    /**
     * Set new {@link StatusCode} (as meta) for packets this factory creates
     * @param newMeta new 'meta'
     */
    public void setMeta(StatusCode newMeta) {
        this.meta = newMeta.getCode();
    }


    // The type of packets this factory produces
    private PacketType type;
    // The 'meta' of packets this factory produces
    private int meta;
    // The ticker this factory uses to make timestamps
    private Ticker ticker;
}
