package ru.sbtqa.tag.videorecorder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoRecorder {


    private static final String DEFAULT_EXTENSION = ".avi";

    private static VideoRecorderService service;
    private static String videoFolder = "";
    private static String videoFileName;
    private static boolean isVideoRecording = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoRecorder.class);

    public static void startRecording(String providedVideoPath) {
        if (isVideoRecording) {
            LOGGER.info("Video is already being recorded");
            return;
        }



        if(!providedVideoPath.equals("")) {
            int lastIndexOfSlash = providedVideoPath.lastIndexOf('/');
            String providedVideoFolder = providedVideoPath.substring(0,lastIndexOfSlash);

            Path providedVideoFolderPath = Paths.get(providedVideoFolder);
            if(Files.exists(providedVideoFolderPath)) {

                videoFolder = providedVideoFolder;
                isVideoRecording = true;

                videoFileName = providedVideoPath.substring(lastIndexOfSlash+1);

                VideoRecorderModule videoRecorderModule = new VideoRecorderModule(videoFolder);
                Recorder provideScreenRecorder = videoRecorderModule.provideScreenRecorder();
                service = new VideoRecorderService(provideScreenRecorder);

                service.start();
            } else {
                LOGGER.error("The provided Folder doesn't exist");
            }

        } else {
            LOGGER.error("The parameter videoFolder is invalid");
        }

    }

    public static String stopRecording() {
        if (isVideoRecording) {
            service.stop();
            isVideoRecording = false;
            return service.save(videoFolder, videoFileName);
        } else {
            LOGGER.warn("Сan't stop the video. It is not recorded now");
            return "";
        }
    }

    public static String getVideoFolder() {
        return videoFolder;
    }

    public static void setVideoFolder(String videoFolder) {
        if (!videoFolder.equals("")) {
            VideoRecorder.videoFolder = videoFolder;
        }
    }

    public String getVideoName() {
        return videoFileName + DEFAULT_EXTENSION;
    }

    public static boolean isVideoRecording() {
        return isVideoRecording;
    }
}
