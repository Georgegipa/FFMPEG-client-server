package client;

import Generic.ProcessRunner;
import server.Config;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketClient {
    //create a client socket and send the message to the server
    public static void Server(String ip, int port) {
        try {
            ClientFrame frame = new ClientFrame();
            frame.prepareUI();
            //check download speed using  JSpeedTest
            SpeedTest.performSpeedTest();
            while (!SpeedTest.speedTestDone()) {
                //check if progress has changed and update the progress bar
                frame.updateBar((int) SpeedTest.getPercent());
            }
            frame.updateBar(SpeedTest.displaySpeed());
            String preferredFormat = null;
            while (preferredFormat == null) {//wait for the user to select the preferred format
                preferredFormat = frame.selectedVideoType();
            }
            Socket socket = new Socket(ip, port);
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
            //display the given error message
            if (message.startsWith("ERROR")) {
                frame.dispose();
                ClientFrame.showErrorDialog(message.split("#")[1]);
                System.exit(0);
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

            socket.close();

            List<String> commands = generateCommands(ip,message.split("#")[1],Integer.parseInt(message.split("#")[2]));
            ProcessRunner processRunner = new ProcessRunner(commands);
            processRunner.start();
            processRunner.printError();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> generateCommands(String ip , String protocol , int port )
    {
        List<String> commands = new ArrayList<>();
        commands.add("cmd.exe");
        commands.add("/c");
        commands.add("ffplay");
        if (protocol.equals("rtp")) {
            //add the following flags -protocol_whitelist file,rtp,udp -i saved_sdp_file
            commands.add("-protocol_whitelist");
            commands.add("file,rtp,udp");
            commands.add("-i");
            commands.add(Config.videoPath + "\\" + "rtpfile" + ".sdp");
        } else {
            commands.add("-autoexit");//auto exit after the video is played
            commands.add(protocol+ "://" + ip + ":" + port);
        }
        return commands;
    }
}
