package client;

import Generic.VideoProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends JFrame {
    private final JButton streamVideoButton;
    private final JProgressBar bar;
    private final JComboBox<String> videoNames;
    private final JRadioButton udpJradio, tcpJradio, rtpJradio;
    private String[] selection;


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
        ButtonGroup group = new ButtonGroup();
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


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Stream Video");
        this.setSize(500, 200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void registerListener(ActionListener listener) {
        streamVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the selected video name
                String videoName = (String) videoNames.getSelectedItem();
                //get the selected protocol
                String protocol = "AUTO"; //default behavior is to use AUTO
                if (tcpJradio.isSelected())
                    protocol = "TCP";
                else if (udpJradio.isSelected())
                    protocol = "UDP";
                else if (rtpJradio.isSelected())
                    protocol = "RTP";
                selection = new String[]{videoName, protocol};
                //print selection
                System.out.println("Video Name: " + videoName + " Protocol: " + protocol);
                listener.actionPerformed(e);
            }
        });
    }

    //repaint the progress bar with the given value
    public void updateBar(int percent) {
        bar.setValue(percent);
        bar.setString("SpeedTest progress:" + percent + "%");
    }

    //update the bar when the speed test is done
    public void updateBar(String speed) {
        bar.setValue(100);
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

    public String[] getSelection() {
        return selection;
    }

    //create a pop up window to show the supported video formats
    public static String selectedVideoType() {
        String[] optionsToChoose = VideoProperty.getSupportedExtensions();

        return (String) JOptionPane.showInputDialog(
                null,
                "Which video format do you prefer?",
                "Choose Video Format",
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionsToChoose,
                optionsToChoose[optionsToChoose.length - 1]);
    }

    //create a error dialog box
    public static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
