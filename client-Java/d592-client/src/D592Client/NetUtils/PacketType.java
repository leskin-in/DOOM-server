package D592Client.NetUtils;

/**
 * Packet types. 'type' of {@link Packet} must be one of these values
 *
 * Note that these values are synchronized with the same ones on server side
 */
public enum PacketType {
    // Service packets
    NONE(0), SERVICE(1), CONNECT(2), RECONNECT(3),
    // Game packets
    GAME(10), GAME_PREPARE(11), GAME_BEGIN(12), GAME_OVER(13),
    // Special packets
    CLIENT_ACTION(20), MESSAGE(31);

    public int getCode() {
        return code;
    }

    private int code;

    PacketType(int code) {
        this.code = code;
    }
}
