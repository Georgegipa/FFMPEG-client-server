import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    private static String videopath = "C:\\Users\\geoxb\\Documents\\Java\\Projects\\Projects\\FFMPEG\\videos";


    private static void fileToVideoList(List<String> files) {
        HashMap<String, VideoType> videoList = new HashMap<String, VideoType>();

        for (String file : files) {
            SimpleVideo v = VideoRetriever.getVideoDetails(file);
            //print the video details
            if (v != null) {//null if the file is not a video
                System.out.println(v.getVideoName());
                System.out.println(v.getResolution());
                System.out.println(v.getVideoExtension());
                System.out.println(v.getVideopath());

                VideoProperty.VideoExtension ex = VideoProperty.convertExtension(v.getVideoExtension());
                VideoProperty.Resolution res = VideoProperty.convertResolution(v.getResolution());

                if (videoList.containsKey(v.getVideoName())) //video with the same name already exists
                    videoList.get(v.getVideoName()).addVideoProperty(ex, res);//add resolution and extension to the video
                else {//new video
                    VideoType vt = new VideoType();
                    vt.setVideo(v);
                    vt.addVideoProperty(ex, res);
                    videoList.put(v.getVideoName(), vt);
                }
            }
        }
    }


    private static void printVideoList(HashMap<String, VideoDetails> videoList) {
        for (Map.Entry<String, VideoDetails> entry : videoList.entrySet()) {
            VideoDetails temp = entry.getValue();
            System.out.println(temp.getVideoName());
            String ext = temp.getMaxResolutionExtensionString();
            System.out.println(ext);
            String res = temp.getMaxResolutionString();
            System.out.println(res);
            System.out.println(temp.getVideopath());
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> files =
                Files.list(Paths.get(videopath))
                        .filter(path -> !Files.isDirectory(path))
                        .map(Path::toString)
                        .collect(Collectors.toList());

        HashMap<String, VideoDetails> videoList = new HashMap<String, VideoDetails>();

        for (String file : files) {//Loop για κάθε αρχείο που βρέθηκε
            SimpleVideo v = VideoRetriever.getVideoDetails(file);
            if (v != null) {//null επιστρέφεται αν το αρχείο δεν είναι βίντεο
                VideoProperty.VideoExtension ex = VideoProperty.convertExtension(v.getVideoExtension());
                VideoProperty.Resolution res = VideoProperty.convertResolution(v.getResolution());

                if (videoList.containsKey(v.getVideoName())) //video with the same name already exists
                    videoList.get(v.getVideoName()).checkVideoProperties(ex, res);//add resolution and extension to the video
                else {//new video
                    VideoDetails vt = new VideoDetails();
                    vt.setVideo(v);
                    videoList.put(v.getVideoName(), vt);
                }
            }
        }
        printVideoList(videoList);


    }

}

