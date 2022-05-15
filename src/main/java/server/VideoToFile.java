package server;

import Generic.Config;
import Generic.VideoHelpers;
import Generic.VideoProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VideoToFile {
    private static final Logger log = LogManager.getLogger(VideoToFile.class);
    public static void printVideoList(HashMap<String, VideoDetails> videoList) {
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

    public static void createMissingVideos(HashMap<String, VideoDetails> videoList) throws IOException {
        FFBuilder builder = new FFBuilder();

        for (Map.Entry<String, VideoDetails> entry : videoList.entrySet()) {
            VideoDetails temp = entry.getValue();
            //loop through all video extensions and resolutions
            for (VideoProperty.VideoExtension ext : VideoProperty.VideoExtension.values()) {
                //loop until the video has the max resolution
                for (VideoProperty.Resolution res : VideoProperty.Resolution.values()) {
                    String newVideo = VideoHelpers.recreateName(temp.getVideoName(), ext, res);
                    //if the video has reached the max resolution, break the loop
                    if (res.ordinal() > temp.getMaxResolution().ordinal()) {
                        log.debug("Video " + temp.getVideoName() + " has reached the max resolution");
                        break;
                    }
                    if (VideoHelpers.fileExists(Config.videoPath + "\\" + newVideo))//check if file exists
                        log.debug("Video " + newVideo + " already exists");
                    else //video does not exist
                        builder.build(temp.getVideopath(), temp.getVideoName(), ext, res);
                }
            }
        }
    }

    public static HashMap<String, VideoDetails> getVideoList(List<String> files) throws IOException {
        HashMap<String, VideoDetails> videoList = new HashMap<>();
        for (String file : files) {//Loop every file in the list
            SimpleVideo v = VideoHelpers.getVideoDetails(file);
            if (v != null) {//if the file is a video
                VideoProperty.VideoExtension ex = VideoProperty.convertExtension(v.getVideoExtension());
                VideoProperty.Resolution res = VideoProperty.convertResolution(v.getResolution());

                if (videoList.containsKey(v.getVideoName())) //video with the same name already exists
                    videoList.get(v.getVideoName()).checkVideoProperties(ex, res);//add resolution and extension to the video
                else {//new video
                    VideoDetails vt = new VideoDetails();
                    vt.setVideo(v);
                    videoList.put(v.getVideoName(), vt);
                }
            } else
                log.warn("File " + file + " is not a video");
        }
        return videoList;
    }

    public static  List<String> getFiles() throws IOException
    {
        Path path = Paths.get(Config.videoPath);
        return Files.walk(path)
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    //get all video filenames in the path
    public static List<String> getVideoFiles(VideoProperty.VideoExtension ext) {

        String endingExt = VideoProperty.convertExtension(ext);
        //remove path and any files that are not videos
        List<String> videos = new ArrayList<>();
        try {
            for(String file : getFiles())
            {
                String removedPath = VideoHelpers.getFilenameFromPath(file);
                if(VideoHelpers.isVideo(removedPath) && removedPath.endsWith(endingExt))
                    videos.add(removedPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videos;
    }
}
