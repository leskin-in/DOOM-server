package D592Client.NetUtils;
import D592Client.Ticker;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * A processor for all network operations and "low-level" server interaction
 */
public class NetWorker {
    public static synchronized NetWorker getInstance(Ticker ticker) {
        if (instance == null) {
            instance = new NetWorker(ticker);
        }
        return instance;
    }

    /**
     * Connect to the server. If the connection exists, close it
     *
     * @param server the server to connect to
     *
     * @throws SocketException if the connect fails
     * @throws IllegalArgumentException if server address is incorrect
     * @throws SecurityException if the access to the given server is not allowed
     */
    public void connectTo(InetSocketAddress server) throws SocketException, IllegalArgumentException, SecurityException {
        Packet packet = new Packet(PacketType.CONNECT, 0, StatusCode.ASK.getCode(), null);
        socket = new DatagramSocket();

        for (int i = 0; i < CLIENT_RECVTRYES; i++) {

        }
    }

    /**
     * Disconnect from the server
     */
    public void disconnect() {

    }

    /**
     * Send data to the server
     *
     * @param packet a packet to send
     */
    public void send(Packet packet) {

    }

    /**
     * Receive data from the server
     *
     * @return a packet received
     */
    public Packet receive() {
        return null;
    }


    // Singleton instance
    private static NetWorker instance = null;

    // A ticker for this packet sender
    private Ticker ticker;

    // Constants to describe the number of repeats of certain operations
    private static final int CLIENT_RECVTRYES = 128;
    private static final int CLIENT_REPEAT = 1;

    // The key element of the class. Allows to send and receive UDP datagrams
    private DatagramSocket socket;

    private NetWorker(Ticker ticker) {
        this.ticker = ticker;
    }
}
