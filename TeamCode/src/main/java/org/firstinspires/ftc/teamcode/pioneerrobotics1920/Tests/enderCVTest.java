package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import org.corningrobotics.*;
import org.corningrobotics.enderbots.endercv.OpenCVPipeline;
import org.opencv.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class enderCVTest extends OpenCVPipeline {

    private boolean showContours = true;

    private Mat hls = new Mat();
    private Mat thresholded = new Mat();

    private List<MatOfPoint> contours = new ArrayList<>();

    public synchronized void setShowCountours(boolean enabled) {
        showContours = enabled;
    }
    public synchronized List<MatOfPoint> getContours() {
        return contours;
    }

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        Imgproc.cvtColor(rgba, hls, Imgproc.COLOR_RGB2HLS);
        Core.inRange(hls, new Scalar(181, 166, 66), new Scalar(255, 255, 240), thresholded);
        Imgproc.blur(hls, thresholded, new Size(3,3));
        contours = new ArrayList<>();
        Imgproc.findContours(thresholded, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        if (showContours) {
            Imgproc.drawContours(rgba, contours, -1, new Scalar(0, 255, 0), 2, 8);
        }
        return rgba;
    }
}

