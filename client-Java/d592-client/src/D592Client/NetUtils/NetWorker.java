package D592Client.NetUtils;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * A processor for all network operations and "low-level" server interaction.
 * This class is a singleton.
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
     * @throws IOException if there are some problems with the connection
     */
    public synchronized void connect(InetSocketAddress server) throws IOException {
        if (this.socket != null) {
            return;
        }
        this.server = server;

        try {
            // Bind socket
            socket = new DatagramSocket();
            socket.setSendBufferSize(Packet.MAX_SIZE_UDP);
            socket.setReceiveBufferSize(Packet.MAX_SIZE_UDP);
            socket.setSoTimeout(25);
            socket.bind(null);
            socket.connect(this.server);

            // Get response from the server
            Packet toSend = new Packet(PacketType.CONNECT, 0, StatusCode.ASK.getCode(), null);
            Packet toReceive;
            for (int i = 0; i < CLIENT_RECVTRYES; i++) {
                this.send(toSend);
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                }
                catch (InterruptedException ex) {
                    // pass
                }

                try {
                    toReceive = this.receive();
                }
                catch (SocketTimeoutException ex) {
                    continue;
                }

                if ((toReceive != null) && (toReceive.getTick() == StatusCode.SUCCESS.getCode())) {
                    return;
                }
            }

            throw new IOException("Cannot connect: the server is unreachable.");
        }
        catch (Exception ex) {
            this.disconnect();
            throw ex;
        }
    }

    /**
     * Disconnect from the server
     */
    public synchronized void disconnect() {
        if (socket == null) {
            return;
        }
        socket.disconnect();
        socket.close();
        socket = null;
    }

    /**
     * Send {@link Packet} to the server
     *
     * @param packet a packet to send
     *
     * @throws IOException if the network send failed
     */
    public void send(Packet packet) throws IOException {
        if (socket == null) {
            throw new IOException("Not connected to the server");
        }
        socket.send(packet.serialize(server));
    }

    /**
     * Receive data from the server
     *
     * @return a packet received
     *
     * @throws IOException if the network receive failed
     */
    public Packet receive() throws IOException {
        if (socket == null) {
            throw new IOException("Not connected to the server");
        }
        DatagramPacket dgp = new DatagramPacket(new byte[Packet.MAX_SIZE_UDP], Packet.MAX_SIZE_UDP);
        socket.receive(dgp);
        return Packet.deserialize(dgp.getData());
    }

    /**
     * Instance of a singleton
     */
    private static NetWorker instance = null;

    /**
     * Number of attempts made to receive a packet from the server
     */
    private static final int CLIENT_RECVTRYES = 128;

    /**
     * A DatagramSocket to communicate with the server
     */
    private DatagramSocket socket = null;

    /**
     * An address of the server
     */
    private InetSocketAddress server;

    private NetWorker() {}
}
