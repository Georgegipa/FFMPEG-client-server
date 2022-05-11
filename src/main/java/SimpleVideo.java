public class SimpleVideo {
    private String videopath;
    private String videoName;
    private String videoExtension;
    private int Resolution;

    //create setters and getters for the variables
    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoExtension(String videoExtension) {
        this.videoExtension = videoExtension;
    }

    public String getVideoExtension() {
        return videoExtension;
    }

    public void setResolution(int Resolution) {
        this.Resolution = Resolution;
    }

    public int getResolution() {
        return Resolution;
    }
}
