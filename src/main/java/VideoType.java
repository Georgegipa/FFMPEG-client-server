import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoType implements Serializable {
    private SimpleVideo video;
    private VideoProperty.Resolution maxResolution;
    private HashMap<VideoProperty.VideoExtension, List<VideoProperty.Resolution>> properties = new HashMap<VideoProperty.VideoExtension, List<VideoProperty.Resolution>>();

    private void compareResolution(VideoProperty.Resolution res) {
        if(res.ordinal() > this.maxResolution.ordinal()) {
            setMaxResolution(res);
        }
    }

    private void setMaxResolution(VideoProperty.Resolution maxResolution) {
        this.maxResolution = maxResolution;
    }

    //setters and getters
    public void setVideo(SimpleVideo video) {
        this.video = video;
        setMaxResolution(VideoProperty.convertResolution(video.getResolution()));
    }

    public SimpleVideo getVideo() {
        return video;
    }

    private VideoProperty.Resolution getMaxResolution() {
        return maxResolution;
    }

    public void addVideoProperty(VideoProperty.VideoExtension extension, VideoProperty.Resolution resolution) {
        if (properties.containsKey(extension)) {//check if the extension exists and add the resolution
            properties.get(extension).add(resolution);
        }
        else {//extension does not exist, create a new list and add the resolution
            List<VideoProperty.Resolution> list = new ArrayList<VideoProperty.Resolution>();
            list.add(resolution);
            properties.put(extension, list);
        }
        compareResolution(resolution);//check if the new resolution is the max resolutionex
    }


    public List<VideoProperty.Resolution> getResolutions(VideoProperty.VideoExtension extension) {
        if (properties.containsKey(extension)) {//check if the extension exists and return the list
            return properties.get(extension);
        }
        else {//extension does not exist, return null
            return null;
        }
    }

    //helper methods to check if a video has a certain extension and resolution
    public boolean extensionExists(VideoProperty.VideoExtension extension) {
        return properties.containsKey(extension);
    }

    public boolean resolutionExists(VideoProperty.VideoExtension extension, VideoProperty.Resolution resolution) {
        return properties.get(extension).contains(resolution);
    }




}
