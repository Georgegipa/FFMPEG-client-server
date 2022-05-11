import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class FFMpegConnector {
    static Logger log = LogManager.getLogger(FFMpegConnector.class);

    public static void main(String[] args) {
        String inputDir = System.getProperty("user.dir") + "/input/";
        String outDir = System.getProperty("user.dir") + "/output/";
        FFmpeg ffmpeg = null;
        FFprobe ffprobe = null;
        try {
            log.debug("Initialising FFMpegClient");
            ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe");
            ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Creating the transcoding");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputDir + "input_bach01.wav")
                .addOutput(outDir + "output_bach01.mp3")
                .done();
        log.debug("Creating the executor");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        log.debug("Starting the transcoding");
// Run a one-pass encode
        executor.createJob(builder).run();
        log.debug("Transcoding finished");
    }
}
