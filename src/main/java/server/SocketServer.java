package server;

import Generic.ProcessRunner;
import Generic.VideoProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    private static ServerSocket server;
    private static final Logger log = LogManager.getLogger(ServerSocket.class);
    private static Socket socket;
    private static VideoProperty.Resolution recommendedResolution;
    private static List<String> commands = new ArrayList<>();
    //create a server socket and listen for incoming connections

    public static void startServer(int port) {

        try {
            server = new ServerSocket(port);

            while (true) {
                socket = server.accept();
                log.debug("Server started on port " + port);
                handleSocket(socket);
                ProcessRunner processRunner = new ProcessRunner(commands);
                processRunner.start();
                processRunner.printError();
                socket.close();
                log.debug("Restarting");
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
                log.info(received);
                if (received.startsWith("1#")) {
                    String[] split = received.split("#");
                    String response = handleSpeedFormat(split[1], split[2]);
                    writer.println(response);
                    writer.flush();
                    if(response.startsWith("ERROR"))//bitrate is too low
                        return;
                } else if (received.startsWith("2#")) {
                    String[] split = received.split("#");
                    String selectedProtocol = handleVideoPlayback(split[1], split[2]);
                    writer.println("PLAY#" + selectedProtocol + "#" + Config.streamport);
                    writer.flush();
                    handleStream(split[1], VideoProperty.convertProtocol(selectedProtocol));
                    break;
                }
            }
            inputStream.close();
            outputStream.close();
            reader.close();
            writer.close();
        } catch (IOException ignored) {
            log.debug("Client disconnected");
        }
    }

    private static String handleSpeedFormat(String speed, String format) {
        int speedInt = Integer.parseInt(speed);
        VideoProperty.VideoExtension extension = VideoProperty.convertExtension(format);
        recommendedResolution = VideoProperty.Resolution.RESOLUTION_240;
        //check which resolution is the best based on the speed
        int i = 0;
        for (int bitRate : Config.BitRates) {
            //if the speed is more than the minimum bitrate and lower than the recommended bitrate
            if (speedInt > Config.MinBitRate && speedInt < bitRate) {
                recommendedResolution = VideoProperty.Resolution.values()[i];
                break;
            }
            //if speed is lower than the minimum bitrate
            else if (speedInt < Config.MinBitRate)
                return "ERROR#" + "Bitrate too low!";

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
        log.debug("Selected protocol is " + protocolType);
        return VideoProperty.convertProtocol(protocolType);
    }

    private static void handleStream(String selectedName, VideoProperty.Protocol protocolType) {
        commands.clear();
//        commands.add("cmd.exe");
//        commands.add("/c");
        commands.add("ffmpeg");

        switch (protocolType) {
            case PROTOCOL_TCP:
                commands.add("-i");
                commands.add(Config.videoPath + "\\" + selectedName);
                commands.add("-f");
                commands.add("avi");
                commands.add(VideoProperty.convertProtocol(protocolType) + "://" + socket.getInetAddress().getHostAddress() + ":" + Config.streamport + "?listen");
                break;
            case PROTOCOL_UDP:
                commands.add("-re");
                commands.add("-i");
                commands.add(Config.videoPath + "\\" + selectedName);
                commands.add("-f");
                commands.add("avi");
                commands.add(VideoProperty.convertProtocol(protocolType) + "://" + socket.getInetAddress().getHostAddress() + ":" + Config.streamport);
                break;
            case PROTOCOL_RTP:
                commands.add("-re");
                commands.add("-thread_queue_size");
                commands.add("4");
                commands.add("-i");
                commands.add(Config.videoPath + "\\" + selectedName);
                commands.add("-strict");
                commands.add("2");
                commands.add("-vcodec");
                commands.add("copy");
                commands.add("-an");
                commands.add("-f");
                commands.add("rtp");
                commands.add(VideoProperty.convertProtocol(protocolType) + "://" + socket.getInetAddress().getHostAddress() + ":" + Config.streamport);
                commands.add("-acodec");
                commands.add("copy");
                commands.add("-vn");
                commands.add("-sdp_file");
                commands.add(Config.videoPath + "\\" + "rtpfile" + ".sdp");
                commands.add("-f");
                commands.add("rtp");
                commands.add(VideoProperty.convertProtocol(protocolType) + "://" + socket.getInetAddress().getHostAddress() + ":" + (Config.streamport+1000));
                break;
        }

        for (String command : commands) {
            System.out.print(" " + command);
        }
    }
}
