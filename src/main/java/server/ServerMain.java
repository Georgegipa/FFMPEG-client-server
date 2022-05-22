package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import static server.VideoToFile.*;

public class ServerMain {
    private static final Logger log = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) throws IOException {
        log.debug("Server Starting...");
        //printVideoList(videoList);
        createMissingVideos(getVideoList(getFiles()));
        log.debug("Created all missing videos!");
        log.debug("Starting socket server...");
        SocketServer.startServer(Config.port);
    }
}

