package client;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpeedTest {
    private static final SpeedTestSocket speedTestSocket = new SpeedTestSocket();
    private static float speed = 0;
    private static float progress = 0;
    private static boolean isSpeedTestDone = false;


    public static void performSpeedTest() {

        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
                //System.out.println("Rate in bit/s   : " + report.getTransferRateBit());
                speed = report.getTransferRateBit().floatValue();
                isSpeedTestDone = true;
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                // called when a download/upload error occur
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                progress = percent;
                //System.out.println("Progress : " + percent + "%");
            }
        });
        //speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");
        speedTestSocket.startDownload("ftp://speedtest:speedtest@ftp.otenet.gr/test1Mb.db");
    }

    public static float getPercent() {
        return progress;
    }

    public static boolean speedTestDone() {
        return isSpeedTestDone;
    }

    public static float getSpeed() {
        return speed;
    }

    public static int getSpeedKbps() {
        return (int) getSpeed() / 1000;
    }

}