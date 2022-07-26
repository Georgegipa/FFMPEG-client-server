# FFMPEG Server-Client

This is a simple server-client application that uses the [FFMPEG](https://www.ffmpeg.org/) stream a video and play it on a client using ffplay.

## Features

* Supported video files :  mkv, mp4, avi
* Supported streaming protocols :  tcp, udp, rtp

## Techonologies used

* Java Swing for the client's GUI
* Java Socket for the communication between the server and the client
* [FFMPEG wrapper](https://github.com/bramp/ffmpeg-cli-wrapper)
* [JSpeedTest](https://github.com/bertrandmartel/speed-test-lib)

## How to use

* First run the ServerMain file to start the server which will do the following:
    * Check all the video files in the videos directory
    * Create a list of all the videos based on the name
    * Create the missing videos (resolutions and format) based on the max resolution of the scanned video.
    * Finally a port will be opened in which the client can connect to.
* Then run the ClientMain file to start the client which will do the following:
    * A swing frame will display the progress of the JSpeedTest
    * Then a pop up will ask the user to select his preferred video format.
    * The client will connect to the server and receive all the available videos of the specified format.
    * After selecting the video and the protocol, and clicking stream video a new instance of ffplay will be started.
        * If a protocol is not specified, the server will choose the best one based on the client's internet connection.