package server;

import Generic.VideoProperty;
import java.io.File;

public abstract class VideoHelpers {

    private static final String[] supportedextensions = VideoProperty.getSupportedExtensions();//get the supported extensions dynamically

    private static boolean isSupported(String extension) {
        for (String supportedextension : supportedextensions) {
            if (supportedextension.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    private static String getVideoName(String path) {
        //get the video name from the path
        String[] parts = path.split("\\\\");
        return parts[parts.length - 1];
    }

    //extract the video details from the filename
    public static SimpleVideo getVideoDetails(String path) {
        String temp;
        SimpleVideo details = new SimpleVideo();
        details.setVideopath(path);
        temp = getVideoName(path);

        if(isSupported(temp.split("\\.")[1])) {
            String[] videoName = temp.split("-");
            details.setVideoName(videoName[0]);
            String[] videodetails2 = videoName[1].split("\\.");

            details.setVideoExtension(videodetails2[1]);
            details.setResolution(Integer.parseInt(videodetails2[0].replaceAll("\\D", "")));
            return details;
        }
        return null;
    }

    //reconstruct the video filename from the video details
    public static String recreateName(String name,VideoProperty.VideoExtension ext,VideoProperty.Resolution res) {
        return name + "-" + VideoProperty.convertResolution(res) + "p." + VideoProperty.convertExtension(ext);
    }

    //get the path without the filename
    public static String removeFilefromPath(String path) {
        int index = path.lastIndexOf("\\");
        if (index != -1)
            return path.substring(0, index + 1);
        return path;
    }

    public static String getFilenameFromPath(String path) {
        int index = path.lastIndexOf("\\");
        if (index != -1)
            return path.substring(index + 1);
        return path;
    }

    //check if the given path is a video
    public static boolean isVideo(String fileName) {
        return isSupported(fileName.split("\\.")[1]);
    }

    //check if file exists
    public static boolean fileExists(String path) {
        File f = new File(path);
        return f.exists();
    }

    public static String getFFFormat(String videoName) {
        String format = videoName.split("\\.")[1];
        //convert the extension to the correct format
        VideoProperty.VideoExtension ext = VideoProperty.convertExtension(format);
        switch (ext)
        {
            case EXTENSION_MP4:
                return "mp4";
            case EXTENSION_MKV:
                return "mastroska";
            case EXTENSION_AVI:
                return "avi";
        }
        return format;//return the original format if it is not supported
    }

}