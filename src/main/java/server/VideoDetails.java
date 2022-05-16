package server;

import Generic.VideoProperty;

import java.io.Serializable;

public class VideoDetails implements Serializable {
    private String videopath;
    private String videoName;
    private VideoProperty.Resolution maxResolution;
    private VideoProperty.VideoExtension maxResolutionExtension;

    private void updatePath() {
        videopath = VideoHelpers.removeFilefromPath(videopath) + VideoHelpers.recreateName(videoName, maxResolutionExtension, maxResolution);
    }

    public void setVideo(SimpleVideo video) {
        this.videoName = video.getVideoName();
        this.videopath = video.getVideopath();
        this.maxResolution = VideoProperty.convertResolution(video.getResolution());
        this.maxResolutionExtension = VideoProperty.convertExtension(video.getVideoExtension());
    }

    public void checkVideoProperties(VideoProperty.VideoExtension extension, VideoProperty.Resolution resolution) {
        //if the resolution of the given extension is higher than the current max resolution
        if (resolution.ordinal() > this.maxResolution.ordinal()) {
            this.maxResolutionExtension = extension;
            this.maxResolution = resolution;
            updatePath();
        }
    }

    public String getVideoName() {
        return videoName;
    }

    public VideoProperty.VideoExtension getMaxResolutionExtension() {
        return maxResolutionExtension;
    }

    public String getMaxResolutionExtensionString() {
        return VideoProperty.convertExtension(maxResolutionExtension);
    }

    public VideoProperty.Resolution getMaxResolution() {
        return maxResolution;
    }

    public String getMaxResolutionString() {
        return VideoProperty.convertResolution(maxResolution);
    }

    public String getVideopath() { return videopath;}
}
