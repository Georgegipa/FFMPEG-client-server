package server;

//This file contains all the configuration of the server
public abstract class Config {
    public static final String videoPath = "C:\\Users\\geoxb\\Documents\\Java\\Projects\\Projects\\FFMPEG\\videos";
    public static final String ffmpegbinPath = "C:\\ffmpeg\\bin";
    public static final int port = 5000;
    public static final int streamport = 6000;
    public static final int[] BitRates = {400, 750, 1000, 2500, 4500};
    public static final int MinBitRate = 300;
}
