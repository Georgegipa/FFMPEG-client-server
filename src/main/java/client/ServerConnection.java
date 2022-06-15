package client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerConnection {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public ServerConnection(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    //send the message to the server and wait for the response
    public String sendCommand(int messageID, String[] messages) {
        StringBuilder message = new StringBuilder();
        for (String mes : messages) {
            message.append(mes).append("#");
        }
        //remove the last #
        message.deleteCharAt(message.length() - 1);
        writer.println(messageID + "#" + message);
        String response;
        try {
            response = reader.readLine();
            while (response == null) {
                response = reader.readLine();
                if (response != null) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
