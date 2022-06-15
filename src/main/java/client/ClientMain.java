package client;

import Generic.ProcessRunner;
import server.Config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMain {

    private static final String ip = "127.0.0.1";
    private static final int port = 5000;
    private static ServerConnection socket;
    private static ClientFrame frame;

    public static void main(String[] args) {
        frame = new ClientFrame();
        frame.prepareUI();
        SpeedTest.performSpeedTest();
        while (!SpeedTest.speedTestDone()) {
            //check if progress has changed and update the progress bar
            frame.updateBar((int) SpeedTest.getPercent());
        }
        frame.updateBar(SpeedTest.displaySpeed());
        //speed test is done, now get the preferred extension
        String preferredFormat = ClientFrame.selectedVideoType();
        if (preferredFormat == null) {//user closed the pop up
            System.exit(0);
        }
        try {
            socket = new ServerConnection(ip, port);
            //Send the speed test result and the preferred format to the server
            String response = socket.sendCommand(1, new String[]{String.valueOf(SpeedTest.getSpeedKbps()), preferredFormat});
            //display the given error message
            if (response.startsWith("ERROR")) {
                frame.dispose();
                ClientFrame.showErrorDialog(response.split("#")[1]);
                System.exit(0);
            }
            //enable the ui and fill the drop down menu with the available videos
            frame.updateMainFrame(response.split("#"));
            //wait for the user to press stream video button
            frame.registerListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] resp = frame.getSelection();
                    String serverResp = socket.sendCommand(2, resp );
                    frame.dispose();
                    socket.close();
                    startTerminal(serverResp);
                }
            });

        } catch (IOException e) {
            ClientFrame.showErrorDialog("Could not connect to the server");
            frame.dispose();
        }
    }

    private static void startTerminal(String message) {
        List<String> commands = generateCommands(ip,message.split("#")[1],Integer.parseInt(message.split("#")[2]));
        ProcessRunner processRunner = new ProcessRunner(commands);
        processRunner.start();
        processRunner.printError();
    }

    private static List<String> generateCommands(String ip , String protocol , int port )
    {
        List<String> commands = new ArrayList<>();
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
