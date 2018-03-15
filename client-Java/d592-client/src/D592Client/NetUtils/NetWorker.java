package D592Client.NetUtils;

import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * A processor for all network operations and "low-level" server interaction
 */
public class NetWorker {
    public static synchronized NetWorker getInstance() {
        if (instance == null) {
            instance = new NetWorker();
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

    }

    /**
     * Disconnect from the server
     */
    public void disconnect() {

    }

    /**
     * Send data to the server
     *
     * @param data data to send
     */
    public void send(int type, int meta, long tick, byte[] data) {

    }

    /**
     * Receive data from the server
     *
     * @return data received
     */
    public byte[] receive() {
        return null;
    }


    private static NetWorker instance = null;

    private NetWorker() {}
}
