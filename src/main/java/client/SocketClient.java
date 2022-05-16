package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.Config;

public class SocketClient {
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
            writer.println("1#" + SpeedTest.getSpeedKbps() + "#" + preferredFormat);
            //wait for the server to send the response
            String message = reader.readLine();
            while (message == null) {
                message = reader.readLine();
                if (message != null) {
                    break;
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
            writer.println("2#" + videoName);
            do {
                message = reader.readLine();
                System.out.println(message);
                if (message != null) {
                    break;
                }
            } while (message == null);
            System.out.println("Response:" + message);
            List<String> commands = new ArrayList<>();
            commands.add("cmd.exe");
            commands.add("/c");
            commands.add("ffplay");
            commands.add("-autoexit");//auto exit after the video is played
            commands.add(message.split("#")[1]+"://"+socket.getLocalAddress().getHostAddress()+":"+message.split("#")[2]);
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.start();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
