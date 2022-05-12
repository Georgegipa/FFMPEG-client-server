import java.io.File;

public class VideoHelpers {

    private static final String supportedextensions[] = "mp4,avi,mkv".split(",");

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

    public static String recreateName(String name,VideoProperty.VideoExtension ext,VideoProperty.Resolution res) {
        return name + "-" + VideoProperty.convertResolution(res) + "p." + VideoProperty.convertExtension(ext);
    }

    public static String removeFilefromPath(String path) {
        int index = path.lastIndexOf("\\");
        if (index != -1)
            return path.substring(0, index + 1);
        return path;
    }

    //check if file exists
    public static boolean fileExists(String path) {
        File f = new File(path);
        return f.exists();
    }
}

