package client;

import java.io.*;
import java.net.Socket;

import server.Config;

public class SocketClient {
    private static volatile String message;
    //create a client socket and send the message to the server
    public static void Server() {
        try {
            ClientFrame frame = new ClientFrame();
            frame.prepareUI();
            //check download speed using  JSpeedTest
            SpeedTest.performSpeedTest();
            while (!SpeedTest.speedTestDone()) {
                //check if progress has changed and update the progress bar
                frame.updateBar((int) SpeedTest.getPercent());
            }
            frame.speedTestDone(SpeedTest.displaySpeed());
            String preferredFormat = null;
            while (preferredFormat == null) {//wait for the user to select the preferred format
                preferredFormat = frame.selectedVideoType();
            }
            Socket socket = new Socket("localhost", Config.port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            writer.println("1#"+SpeedTest.getSpeedKbps() + "#" + preferredFormat);
            //wait for the server to send the response
            message = reader.readLine();
            while (message == null) {
                message = reader.readLine();
                if (message != null) {
                    System.out.println(message);
                }
            }
            frame.updateMainFrame(message.split("#"));
            String videoName;
            while (true) {
                videoName = frame.getSelectedVideoName();
                if (videoName != null)
                    break;
            }
            frame.dispose();//close the frame after the user has selected the video
            System.out.println("Video Name: " + videoName);
            writer.println("2#"+videoName);
            message = null;
            while (true) {
                message = reader.readLine();
                System.out.println(message);
                if (message != null) {
                    break;
                }
            }
            System.out.println("Response:" + message);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
