package server;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class FFBuilder {
    private final  String path = "C:\\ffmpeg\\bin";
    private static String videopath = "C:\\Users\\geoxb\\Documents\\Java\\Projects\\Projects\\FFMPEG\\videos";
    private static Logger log = LogManager.getLogger(FFBuilder.class);

    private FFmpeg ffmpeg = null;
    private FFprobe ffprobe = null;

    FFBuilder() throws IOException {
        ffmpeg = new FFmpeg(path + "\\ffmpeg.exe");
        ffprobe = new FFprobe(path + "\\ffprobe.exe");
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
