import java.util.ArrayList;
import java.util.List;

public class VideoRetriever {
    private static String videopath;
    private static final String supportedextensions[] = "mp4,avi,mkv".split(",");

    public static void setVideopath(String videopath) {
        VideoRetriever.videopath = videopath;
    }

    public static String getVideopath() {
        return videopath;
    }

    private static boolean isSupported(String extension) {
        for (String supportedextension : supportedextensions) {
            if (supportedextension.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    private static String getVideoName(String path) {
        //αφού το όνομα του video βρίσκεται στο τέλος του μονοπατιού δεν χρειαζόμαστε το υπόλοιπο
        String[] parts = path.split("\\\\");
        return parts[parts.length - 1];
    }

    public static SimpleVideo getVideoDetails(String path) {
        String temp = "";
        SimpleVideo details = new SimpleVideo();
        details.setVideopath(path);
        temp = getVideoName(path);

        if(isSupported(temp.split("\\.")[1])) {
            String videoName[] = temp.split("-");
            details.setVideoName(videoName[0]);
            String videodetails2[] = videoName[1].split("\\.");

            details.setVideoExtension(videodetails2[1]);
            details.setResolution(Integer.parseInt(videodetails2[0].replaceAll("[^\\d]", "")));
            return details;
        }
        return null;
    }



}

