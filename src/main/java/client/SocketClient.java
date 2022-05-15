package client;

import java.io.*;
import java.net.Socket;
import Generic.Config;

public class SocketClient {
    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;

    public static void startSocket() {

        try {
            socket = new Socket("localhost", Config.port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  void endSocket() {
        try {
            writer.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create a method that will send a String message to the server
    public static void sendMessage(String message) {
        writer.flush();
        writer.print(message);
        writer.print("\r\n");
        writer.flush();
    }

    //create a method that will receive a String message from the server
    public static String receiveMessage() {
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
