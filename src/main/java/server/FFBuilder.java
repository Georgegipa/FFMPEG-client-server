package server;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class FFBuilder {
    private static String videopath = Paths.videoPath;
    private static Logger log = LogManager.getLogger(FFBuilder.class);

    private FFmpeg ffmpeg = null;
    private FFprobe ffprobe = null;

    FFBuilder() throws IOException {
        ffmpeg = new FFmpeg(Paths.ffmpegbinPath + "\\ffmpeg.exe");
        ffprobe = new FFprobe(Paths.ffmpegbinPath + "\\ffprobe.exe");
    }

    public void build(String path,String videoName,VideoProperty.VideoExtension ext,VideoProperty.Resolution res) throws IOException {
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
