package client;

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
        frame.speedTestDone(SpeedTest.displaySpeed());
        String preferredFormat = frame.selectedVideoType();
        SocketClient.startSocket();
        SocketClient.sendMessage(SpeedTest.getSpeedKbps()+"#"+preferredFormat);
        SocketClient.endSocket();

    }


}
