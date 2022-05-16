package server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.*;

import static server.VideoToFile.*;

public class ServerMain {
    private static final Logger log = LogManager.getLogger(ServerMain.class);

    //get all video filenames in the path
    private static List<String> getVideoFiles() throws IOException {

        //remove path and any files that are not videos
        List<String> videos = new ArrayList<>();
        for(String file : getFiles())
        {
            String removedPath = VideoHelpers.getFilenameFromPath(file);
            if(VideoHelpers.isVideo(removedPath))
                videos.add(removedPath);
        }
        return videos;
    }

    public static void main(String[] args) throws IOException {
        log.debug("Server Starting...");
        //printVideoList(videoList);
        createMissingVideos(getVideoList(getFiles()));
        log.debug("Created all missing videos!");
        log.debug("Starting socket server...");
        SocketServer.startServer(5000);
    }
}

