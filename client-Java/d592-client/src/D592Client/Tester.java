package D592Client;

import D592Client.NetUtils.NetWorker;

import java.net.InetSocketAddress;

public class Tester {
    public static void main(String[] args) throws Exception {
        NetWorker nw = NetWorker.getInstance();
        InetSocketAddress sa = new InetSocketAddress("127.0.0.1", 59200);
        nw.connect(sa);
        System.out.println("Connection successful");
        nw.disconnect();
    }
}
