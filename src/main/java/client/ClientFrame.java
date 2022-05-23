package client;

import Generic.VideoProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends JFrame {
    private JButton streamVideoButton;
    private JProgressBar bar;
    private JComboBox<String> videoNames;
    private JRadioButton udpJradio, tcpJradio, rtpJradio;
    private ButtonGroup group;
    //these is required to avoid compiller optimization and make sure the state of button is always up to date
    private static volatile String selectedVideoName;


    public ClientFrame() {
        super();
        streamVideoButton = new JButton("Stream Video");
        streamVideoButton.setEnabled(false);
        bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setString("0%");
        bar.setForeground(Color.RED);
        bar.setBackground(Color.BLACK);
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(200, 20));
        bar.setVisible(true);

        videoNames = new JComboBox<>();
        videoNames.setEnabled(false);


        udpJradio = new JRadioButton("UDP");
        tcpJradio = new JRadioButton("TCP");
        rtpJradio = new JRadioButton("RTP");
        udpJradio.setActionCommand("UDP");
        tcpJradio.setActionCommand("TCP");
        rtpJradio.setActionCommand("RTP");
        udpJradio.setEnabled(false);
        tcpJradio.setEnabled(false);
        rtpJradio.setEnabled(false);
        group = new ButtonGroup();
        group.add(tcpJradio);
        group.add(udpJradio);
        group.add(rtpJradio);
    }

    public void prepareUI() {

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(2, 1));
        this.add(streamVideoButton, BorderLayout.SOUTH);//add button to the bottom of the frame
        this.add(selectionPanel, BorderLayout.CENTER);
        this.add(bar, BorderLayout.NORTH);//add progress bar to the center of the frame
        JPanel firstRow = new JPanel();
        firstRow.setLayout(new GridLayout(1, 2));
        firstRow.add(new JLabel("Video Name:"));
        firstRow.add(videoNames);
        selectionPanel.add(firstRow);

        JPanel secondRow = new JPanel();
        secondRow.setLayout(new GridLayout(1, 4));

        //add 4 radio buttons to the selection panel
        secondRow.add(new JLabel("Select Protocol:"));
        secondRow.add(tcpJradio);
        secondRow.add(udpJradio);
        secondRow.add(rtpJradio);
        selectionPanel.add(secondRow);
        //add the grid layout to the center of the frame

        streamVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the selected video name
                String videoName = (String) videoNames.getSelectedItem();
                //get the selected protocol
                //get the selection from the group
                System.out.println(group.getSelection().getActionCommand());
                String protocol = null;
                if (tcpJradio.isSelected())
                    protocol = "TCP";
                else if (udpJradio.isSelected())
                    protocol = "UDP";
                else if (rtpJradio.isSelected())
                    protocol = "RTP";
                else//none of the radio buttons is selected
                    protocol = "AUTO";
                selectedVideoName = videoName + "#" + protocol;
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Stream Video");
        this.setSize(500, 200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //repaint the progress bar with the given value
    public void updateBar(int percent) {
        bar.setValue(percent);
        bar.setString("SpeedTest progress:" + percent + "%");
    }

    //update the bar when the speed test is done
    public void updateBar(String speed) {
        bar.setForeground(Color.GREEN);
        bar.setString("Connection Speed: " + speed);
    }

    //when the information about the video is received, update the video names combo box
    public void updateMainFrame(String[] videos) {
        videoNames.removeAllItems();
        for (String video : videos) {
            videoNames.addItem(video);
        }
        udpJradio.setEnabled(true);
        tcpJradio.setEnabled(true);
        rtpJradio.setEnabled(true);
        videoNames.setEnabled(true);
        streamVideoButton.setEnabled(true);
    }

    public String getSelectedVideoName() {
        return selectedVideoName;
    }

    //create a pop up window to show the supported video formats
    public static String selectedVideoType() {
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

    //create a error dialog box
    public static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
