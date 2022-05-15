package client;

import Generic.VideoProperty;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {
    private JButton streamVideoButton;
    private final JProgressBar bar = new JProgressBar(0, 100);
    private JComboBox<String> videoTypes;
    private JComboBox<String> resolutions;
    private JComboBox<String> videoNames;


    public ClientFrame() {
        super();
        streamVideoButton = new JButton("Stream Video");
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setString("0%");
        bar.setForeground(Color.RED);
        bar.setBackground(Color.BLACK);
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(200, 20));
        bar.setVisible(true);

        videoTypes = new JComboBox<String>(VideoProperty.getSupportedExtensions());


    }

    public void prepareUI() {

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(3, 1));


        this.add(streamVideoButton, BorderLayout.SOUTH);//add button to the bottom of the frame
        this.add(selectionPanel,BorderLayout.CENTER);
        this.add(bar, BorderLayout.NORTH);//add progress bar to the center of the frame

        //add combo boxes to the center panel
        //add a label then a combo box and a button to a row
        JPanel firstRow = new JPanel();
        firstRow.setLayout(new GridLayout(1, 3));//create a grid layout with 3 columns and 3 rows
        selectionPanel.add(firstRow);
        firstRow.add(new JLabel("Select Video Type:"));
        firstRow.add(videoTypes);
        firstRow.add(new JButton("ok"));



        //add the grid layout to the center of the frame


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Stream Video");
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void updateBar(int percent) {
        bar.setValue(percent);
        bar.setString("SpeedTest progress:" + percent + "%");
    }

    //update the bar when the speed test is done
    public void speedTestDone(String speed) {
        bar.setForeground(Color.GREEN);
        bar.setString("Connection Speed: " + speed);
    }

    public String selectedVideoType() {
        String[] optionsToChoose = VideoProperty.getSupportedExtensions();
        String videoFormat = (String) JOptionPane.showInputDialog(
                null,
                "Which video format do you prefer?",
                "Choose Video Format",
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionsToChoose,
                optionsToChoose[optionsToChoose.length - 1]);

        return videoFormat;
    }

}
