package client;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {
    private JButton streamVideoButton;
    private JLabel speedLabel;
    private final JProgressBar bar = new JProgressBar(0,100);


    public ClientFrame() {
        super();
        streamVideoButton = new JButton("Stream Video");
        speedLabel = new JLabel("Connection Speed : 2500 Kbps");
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setString("0%");
        bar.setForeground(Color.RED);
        bar.setBackground(Color.BLACK);
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(200,20));
        bar.setVisible(true);

    }

    public void prepareUI() {


        this.add(streamVideoButton, BorderLayout.SOUTH);//add button to the bottom of the frame
        //this.add(speedLabel, BorderLayout.NORTH);//add label to the top of the frame
        this.add(bar, BorderLayout.NORTH);//add progress bar to the center of the frame

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Stream Video");
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void updateBar(int percent){
        bar.setValue(percent);
        bar.setString(percent + "%");
    }

    //update the bar when the speed test is done
    public void speedTestDone(int speed){
        bar.setForeground(Color.GREEN);
        bar.setString("Connection Speed: " + SpeedTest.getSpeedKbps() + " Kbps");
    }
}
