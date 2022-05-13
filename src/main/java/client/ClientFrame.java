package client;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {
    private JButton streamVideoButton;
    private JLabel speedLabel;


    public ClientFrame() {
        super();
        streamVideoButton = new JButton("Stream Video");
        speedLabel = new JLabel("Connection Speed : 2500 Kbps");

    }

    public void prepareUI() {


        this.add(streamVideoButton, BorderLayout.SOUTH);//add button to the bottom of the frame
        this.add(speedLabel, BorderLayout.NORTH);//add label to the top of the frame

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Stream Video");
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
