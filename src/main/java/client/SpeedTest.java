package client;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class SpeedTest {
    private static final SpeedTestSocket speedTestSocket = new SpeedTestSocket();
    private static float speed = 0;
    private static float progress = 0;
    private static boolean isSpeedTestDone = false;
    private static final String url = "http://speedtest.ftp.otenet.gr/files/test1Mb.db";
    private static final int timeout = 5000;


    public static void performSpeedTest() {
        speedTestSocket.startFixedDownload(url, timeout);
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
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
            }
        });
    }

    public static float getPercent() {
        return progress;
    }

    public static boolean speedTestDone() {
        return isSpeedTestDone;
    }

    public static float getSpeed(){
        return speed;
    }

    public static int getSpeedKbps() {
        return (int) getSpeed() / 1000;
    }

    public static String displaySpeed()
    {
        int speedKbps = (int) getSpeed() / 1000;
        if (speedKbps < 1000) {
            return speedKbps + " kbps";
        } else if (speedKbps < 1000000) {
            return speedKbps / 1000 + " Mbps";
        } else {
            return speedKbps / 1000000 + " Gbps";
        }
    }

}
