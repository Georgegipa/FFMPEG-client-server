package server;

import Generic.Config;
import Generic.VideoHelpers;
import Generic.VideoProperty;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class FFBuilder {
    private static Logger log = LogManager.getLogger(FFBuilder.class);

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    FFBuilder() throws IOException {
        ffmpeg = new FFmpeg(Config.ffmpegbinPath + "\\ffmpeg.exe");
        ffprobe = new FFprobe(Config.ffmpegbinPath + "\\ffprobe.exe");
    }

    public void build(String path, String videoName, VideoProperty.VideoExtension ext, VideoProperty.Resolution res) {
        String destination = VideoHelpers.removeFilefromPath(path);
        String fileName = VideoHelpers.recreateName(videoName,ext,res);
        log.debug("Creating New Video:"+ fileName);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(path)
                .addOutput(destination +fileName)
                .setVideoResolution(VideoProperty.getResolution(res)[0],VideoProperty.getResolution(res)[1])//change resolution
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
}
