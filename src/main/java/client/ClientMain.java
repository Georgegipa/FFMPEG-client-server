package client;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class ClientMain {


    public static void main(String[] args) {
        ClientFrame frame = new ClientFrame();
        frame.prepareUI();
        //check download speed using  JSpeedTest
        SpeedTest.performSpeedTest();
        while (!SpeedTest.speedTestDone()) {
            //check if progress has changed and update the progress bar
            frame.updateBar((int)SpeedTest.getPercent());
        }
        frame.speedTestDone(SpeedTest.getSpeedKbps());

    }


}
