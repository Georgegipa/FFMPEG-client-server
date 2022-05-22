package client;

public class ClientMain {

    private static final String ip = "127.0.0.1";
    private static final int port = 5000;

    public static void main(String[] args) {
        SocketClient.Server(ip, port);
    }
}
