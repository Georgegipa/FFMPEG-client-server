package client;

import Generic.VideoProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends JFrame {
    private JButton streamVideoButton;
    private final JProgressBar bar = new JProgressBar(0, 100);
    private JComboBox<String> videoNames;
    private JRadioButton autoJradio,udpJradio,tcpJradio,rtpJradio;
    private ButtonGroup group;
    //these is required to avoid compiller optimization and make sure the state of button is always up to date
    private static volatile String selectedVideoName;


    public ClientFrame() {
        super();
        streamVideoButton = new JButton("Stream Video");
        streamVideoButton.setEnabled(false);
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

        autoJradio = new JRadioButton("AUTO");
        autoJradio.setSelected(true);
        udpJradio = new JRadioButton("UDP");
        tcpJradio = new JRadioButton("TCP");
        rtpJradio = new JRadioButton("RTP");
        autoJradio.setActionCommand("AUTO");
        udpJradio.setActionCommand("UDP");
        tcpJradio.setActionCommand("TCP");
        rtpJradio.setActionCommand("RTP");
        autoJradio.setEnabled(false);
        udpJradio.setEnabled(false);
        tcpJradio.setEnabled(false);
        rtpJradio.setEnabled(false);
        group = new ButtonGroup();
        group.add(autoJradio);
        group.add(tcpJradio);
        group.add(udpJradio);
        group.add(rtpJradio);
    }

    public void prepareUI() {

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(2, 1));


        this.add(streamVideoButton, BorderLayout.SOUTH);//add button to the bottom of the frame
        this.add(selectionPanel,BorderLayout.CENTER);
        this.add(bar, BorderLayout.NORTH);//add progress bar to the center of the frame
        JPanel firstRow = new JPanel();
        firstRow.setLayout(new GridLayout(1, 2));
        firstRow.add(new JLabel("Video Name:"));
        firstRow.add(videoNames);
        selectionPanel.add(firstRow);

        JPanel secondRow = new JPanel();
        secondRow.setLayout(new GridLayout(1, 5));

        //add 4 radio buttons to the selection panel
        secondRow.add(new JLabel("Select Protocol:"));
        secondRow.add(autoJradio);
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
                String protocol= null;
                if(autoJradio.isSelected()) {
                    protocol = "AUTO";
                }
                else if(tcpJradio.isSelected()) {
                    protocol = "TCP";
                }
                else if(udpJradio.isSelected()) {
                    protocol = "UDP";
                }
                else if(rtpJradio.isSelected()) {
                    protocol = "RTP";
                }
                selectedVideoName = videoName + "#" + protocol;
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Stream Video");
        this.setSize(500, 200);
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

    public void updateMainFrame(String[] videos) {
        videoNames.removeAllItems();
        for (String video : videos) {
            videoNames.addItem(video);
        }
        autoJradio.setEnabled(true);
        udpJradio.setEnabled(true);
        tcpJradio.setEnabled(true);
        rtpJradio.setEnabled(true);
        videoNames.setEnabled(true);
        streamVideoButton.setEnabled(true);
    }

    public void resetUI() {
        autoJradio.setEnabled(false);
        udpJradio.setEnabled(false);
        tcpJradio.setEnabled(false);
        rtpJradio.setEnabled(false);
        videoNames.setEnabled(false);
        streamVideoButton.setEnabled(false);
        selectedVideoName = null;
    }

    public String getSelectedVideoName() {
        return selectedVideoName;
    }

}
