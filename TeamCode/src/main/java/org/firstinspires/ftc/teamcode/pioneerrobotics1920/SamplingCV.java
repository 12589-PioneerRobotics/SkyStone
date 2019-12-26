package org.firstinspires.ftc.teamcode.pioneerrobotics1920;

import org.corningrobotics.enderbots.endercv.OpenCVPipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class SamplingCV extends OpenCVPipeline {
    private Position goldPosition = Position.UNKNOWN;
    private String errorString = "";

    public enum Position {UNKNOWN, LEFT, CENTER, RIGHT}
    private enum Color {UNKNOWN, YELLOW, WHITE}

    public Mat processFrame(Mat rgba, Mat gray) {
        // rgba is 480x640
        Rect cropRect = new Rect(100, 0, 80, 640); // x, y, width, height
        Mat cropped = new Mat(rgba, cropRect);
        // cropped refers to a certain area of rgba
        Mat hls = new Mat();
        Imgproc.cvtColor(cropped, hls, Imgproc.COLOR_RGB2HLS); // create an HLS version of cropped

        // modify next line to adjust min and max color thresholds for white (HLS)
        Point[] whitePoints = detect(hls, new Scalar(0, 192, 0), new Scalar(255, 255, 255)); // brighter than 75%

        // modify next line to adjust min and max color thresholds for yellow (HLS)
        Point[] yellowPoints = detect(hls, new Scalar(28/2, 0, 128), new Scalar(62/2, 192, 255)); // certain hue range, at least 50% saturated, at least 75% bright
        errorString = "White objects found: " + whitePoints.length + ". Yellow objects found: " + yellowPoints.length + ". ";

        for (Point point : yellowPoints) {
            Imgproc.rectangle(cropped, point, new Point(point.x + 5, point.y + 5), new Scalar(255, 0, 0, 0));
        }

        for (Point point : whitePoints) {
            Imgproc.drawMarker(cropped, point, new Scalar(255, 0, 0, 0));
        }

        calculateGoldPosition(whitePoints, yellowPoints);

        Imgproc.rectangle(rgba, new Point(cropRect.x, cropRect.y),
                new Point(cropRect.x + cropRect.width, cropRect.y + cropRect.height),
                new Scalar(0, 255, 0, 0)); // R G B A

        return rgba;
    }

    public static Point[] detect(Mat hls, Scalar minHLS, Scalar maxHLS) {
        Mat bw = new Mat();
        Core.inRange(hls, minHLS, maxHLS, bw); // filter the given image through the given colors
        Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10));
        Imgproc.erode(bw, bw, structuringElement);
        Imgproc.dilate(bw, bw, structuringElement);
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(bw, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        int n = contours.size();
        Point[] centers = new Point[n];
        for (int i = 0; i < n; i++) {
            org.opencv.imgproc.Moments moments = Imgproc.moments(contours.get(i));
            centers[i] = new Point(moments.get_m10()/moments.get_m00(),
                    moments.get_m01()/moments.get_m00());
        }
        return centers;
    }

    public void calculateGoldPosition(Point[] whitePoints, Point[] yellowPoints) {
        final int LEFT_SEPARATOR = 213;
        final int RIGHT_SEPARATOR = 426;

        boolean whiteLeft = false;
        boolean whiteCenter = false;
        boolean whiteRight = false;
        boolean yellowLeft = false;
        boolean yellowCenter = false;
        boolean yellowRight = false;

        for (Point point : whitePoints) {
            if (point.y < LEFT_SEPARATOR) whiteLeft = true;
            else if (point.y > RIGHT_SEPARATOR) whiteRight = true;
            else whiteCenter = true;
        }
        for (Point point : yellowPoints) {
            if (point.y < LEFT_SEPARATOR) yellowLeft = true;
            else if (point.y > RIGHT_SEPARATOR) yellowRight = true;
            else yellowCenter = true;
        }

        Color leftColor = Color.UNKNOWN;
        Color centerColor = Color.UNKNOWN;
        Color rightColor = Color.UNKNOWN;

        if (whiteLeft && !yellowLeft)
            leftColor = Color.WHITE;
        if (yellowLeft && !whiteLeft)
            leftColor = Color.YELLOW;

        if (whiteCenter && !yellowCenter)
            centerColor = Color.WHITE;
        if (yellowCenter && !whiteCenter)
            centerColor = Color.YELLOW;

        if (whiteRight && !yellowRight)
            rightColor = Color.WHITE;
        if (yellowRight && !whiteRight)
            rightColor = Color.YELLOW;

        if (leftColor == Color.WHITE && centerColor == Color.WHITE && !(rightColor == Color.WHITE)
                || !(leftColor == Color.YELLOW) && !(centerColor == Color.YELLOW) && rightColor == Color.YELLOW)
            goldPosition = Position.RIGHT;
        else if (leftColor == Color.WHITE && !(centerColor == Color.WHITE) && rightColor == Color.WHITE
                || !(leftColor == Color.YELLOW) && centerColor == Color.YELLOW && !(rightColor == Color.YELLOW))
            goldPosition = Position.CENTER;
        else if (!(leftColor == Color.WHITE) && centerColor == Color.WHITE && rightColor == Color.WHITE
                || leftColor == Color.YELLOW && !(centerColor == Color.YELLOW) && !(rightColor == Color.YELLOW))
            goldPosition = Position.LEFT;
        else
            goldPosition = Position.UNKNOWN;
    }

    public Position getGoldPosition() {
        return goldPosition;
    }
    public String getErrorString() {
        return errorString;
    }
}