package server;

import Generic.VideoProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SocketServer {
    private static ServerSocket server;
    private static final Logger log = LogManager.getLogger(ServerSocket.class);
    private static Socket socket;
    private static VideoProperty.Resolution recommendedResolution;

    //create a server socket and listen for incoming connections

    public static void startServer(int port) {

        try {
            server = new ServerSocket(port);

            while (true) {
                socket = server.accept();
                log.debug("Server started on port " + port);
                handleSocket(socket);
                //handle(socket);
                socket.close();
                log.debug("Socket closed");
            }
            //if a client disconnects, the server will close the socket and wait for another client to connect
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handleSocket(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
            while (true) {
                String received = reader.readLine();
                System.out.print(received);
                if (received.startsWith("1#")) {//τερματισμός του server
                    String[] split = received.split("#");
                    String response = handleSpeedFormat(split[1], split[2]);
                    writer.println(response);
                    writer.flush();
                } else if (received.startsWith("2#")) {
                    String[] split = received.split("#");
                    String response = handleVideoPlayback(split[1], split[2]);
                    System.out.println(response);
                    writer.println("PLAY#"+Config.streamport);
                    writer.flush();
                    break;
                }
            }
        } catch (IOException ignored) {
            log.debug("Client disconnected");
        }
    }

    private static String handleSpeedFormat(String speed, String format) {
        int[] recommendedSpeed = {400, 750, 1000, 2500, 4500};
        int speedInt = Integer.parseInt(speed);
        VideoProperty.VideoExtension extension = VideoProperty.convertExtension(format);
        recommendedResolution = VideoProperty.Resolution.RESOLUTION_240;
        //check which resolution is the best based on the speed
        int i = 0;
        for (int bitRate : recommendedSpeed) {
            if (speedInt >= bitRate) {
                recommendedResolution = VideoProperty.Resolution.values()[i];
                i++;
            } else
                break;
        }
        //create a list of all supported videos
        List<String> videoList = VideoToFile.getVideoFiles(extension);
        //loop through the videoList
        log.debug("Recommeneded resolution for " + speed + "Kbps is " + VideoProperty.convertResolution(recommendedResolution) + "p");

        String output = "";
        //remove the videos that have lower resolution than the recommended one
        for (String video : videoList) {
            SimpleVideo vid;
            vid = VideoHelpers.getVideoDetails(video);
            //keep the video if it has the same or lower resolution as the recommended one
            if (VideoProperty.convertResolution(vid.getResolution()).ordinal() <= recommendedResolution.ordinal()) {
                output += video + "#";//concat the video filenames to a string so that it can be sent to the client
            }
            //to avoid meaningless loops if the resolution is higher no need to check
            else
                break;
        }
        //remove the last #
        output = output.substring(0, output.length() - 1);
        return output;
    }

    private static String handleVideoPlayback(String selectedName, String protocol) {
        //convert output to lower case
        String temp = protocol.toLowerCase();
        VideoProperty.Protocol protocolType = null;
        if (temp.equals("auto")) {
            switch (recommendedResolution) {
                case RESOLUTION_240:
                    protocolType = VideoProperty.Protocol.PROTOCOL_TCP;
                    break;
                case RESOLUTION_360:
                case RESOLUTION_480:
                    protocolType = VideoProperty.Protocol.PROTOCOL_UDP;
                    break;
                case RESOLUTION_720:
                case RESOLUTION_1080:
                    protocolType = VideoProperty.Protocol.PROTOCOL_RTP;
                    break;
            }
        } else
            protocolType = VideoProperty.convertProtocol(temp);

        String input = " -i " + Config.videoPath + "\\" + selectedName;
        String format = " -f " + selectedName.split("\\.")[1];
        String ip = " " + VideoProperty.convertProtocol(protocolType) + "://" + socket.getInetAddress().getHostAddress() + ":" + Config.streamport;
        switch (protocolType) {
            case PROTOCOL_TCP:
                return "ffmpeg" + input + format + ip + "?listen";
            case PROTOCOL_UDP:
                return "ffmpeg -re" + input + format + ip;
            case PROTOCOL_RTP:
                return new Exception("Not implemented yet").toString();
        }
        return temp;
    }

}
