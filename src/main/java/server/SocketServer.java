package server;

import Generic.Config;
import Generic.VideoHelpers;
import Generic.VideoProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    private static ServerSocket server;
    private static final Logger log = LogManager.getLogger(ServerSocket.class);
    private static Socket socket;

    //create a server socket and listen for incoming connections

    public static void startServer() {

        try
        {
            server = new ServerSocket(Config.port);

            while (true) {
                socket = server.accept();
                log.debug("Server started on port " + Config.port);
                handleSocket(socket);
                socket.close();

            }
            //if a client disconnects, the server will close the socket and wait for another client to connect
        }
        catch (Exception e)
        {
            log.debug("Server stopped on port " + Config.port);
        }

    }

    private static void handleSocket(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            try {
                socket.setSoTimeout(10 * 1000);
            } catch (SocketException e) {
                System.out.println("Timeout");
                socket.close();
            }
            while (true) {
                String s = reader.readLine();
                if(s!=null) {
                    System.out.println(s);

                }
                else if(s.contains("#"))
                {
                    String[] split = s.split("#");
                    handleSpeedFormat(split[1], split[2]);
                }
                if(inputStream.read() == -1) {
                    break;
                }
            }
        } catch (IOException e) {
        }
    }

    public static void handleSpeedFormat(String speed, String format) {
       int recommendedSpeed[] = {400,750,1000,2500,4500};
       int speedInt = Integer.parseInt(speed);
       VideoProperty.VideoExtension extension = VideoProperty.convertExtension(format);
       VideoProperty.Resolution recommendedResolution = VideoProperty.Resolution.RESOLUTION_240;
       //check which resolution is the best based on the speed
        int i = 0;
        for(int bitRate:recommendedSpeed)
        {
            if(speedInt>=bitRate)
            {
                recommendedResolution = VideoProperty.Resolution.values()[i];
                i++;
            }
            else
                break;
        }
        //create a list of all supported videos
        List<String> videoList = new ArrayList<>();
        videoList = VideoToFile.getVideoFiles(extension);
        //loop through the videoList
        log.debug("Recommeneded resolution for "+speed+"Kbps is " + VideoProperty.convertResolution(recommendedResolution));

        String output = "";
        //remove the videos that have lower resolution than the recommended one
        for(String video:videoList)
        {
            SimpleVideo vid = new SimpleVideo();
            vid = VideoHelpers.getVideoDetails(video);
            //keep the video if it has the same or lower resolution as the recommended one
            if(VideoProperty.convertResolution(vid.getResolution()).ordinal() <= recommendedResolution.ordinal())
            {
                output += video + "#";//concat the video filenames to a string so that it can be sent to the client
            }
        }
        //remove the last #
        output = output.substring(0,output.length()-1);
        System.out.println(output);
    }

}
