//this file contains all the video properties for the videos in the videos folder

public class VideoProperty {

    public enum Resolution {
        RESOLUTION_240,
        RESOLUTION_360,
        RESOLUTION_480,
        RESOLUTION_720,
        RESOLUTION_1080
    }

    public enum VideoExtension {
        EXTENSION_MP4,
        EXTENSION_MKV,
        EXTENSION_AVI
    }

    //convert int to Resolution
    public static Resolution convertResolution(int resolution) {
        switch (resolution) {
            case 240:
                return Resolution.RESOLUTION_240;
            case 360:
                return Resolution.RESOLUTION_360;
            case 480:
                return Resolution.RESOLUTION_480;
            case 720:
                return Resolution.RESOLUTION_720;
            case 1080:
                return Resolution.RESOLUTION_1080;
        }
        return null;
    }

    //convert string to VideoExtension
    public static VideoExtension convertExtension(String extension) {
        switch (extension) {
            case "mp4":
                return VideoExtension.EXTENSION_MP4;
            case "mkv":
                return VideoExtension.EXTENSION_MKV;
            case "avi":
                return VideoExtension.EXTENSION_AVI;
        }
        return  null;
    }

    //convert VideoExtension to string
    public static String convertExtension(VideoExtension extension) {
        switch (extension) {
            case EXTENSION_MP4:
                return "mp4";
            case EXTENSION_MKV:
                return "mkv";
            case EXTENSION_AVI:
                return "avi";
        }
        return null;
    }

    //convert Resolution to string
    public static String convertResolution(Resolution resolution) {
        switch (resolution) {
            case RESOLUTION_240:
                return "240";
            case RESOLUTION_360:
                return "360";
            case RESOLUTION_480:
                return "480";
            case RESOLUTION_720:
                return "720";
            case RESOLUTION_1080:
                return "1080";
        }
        return null;
    }

}
