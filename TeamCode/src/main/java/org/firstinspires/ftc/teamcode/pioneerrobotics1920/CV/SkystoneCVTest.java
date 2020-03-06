package org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV;

import org.corningrobotics.enderbots.endercv.OpenCVPipeline;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * @author Ningrui (i.e. 懒), Vinh, Krish <-(Not Important *cough cough* except he came up with the only method to detect skystones but took over two hours to try and explain it properly when Vinh did it in 2 minutes because Vinh is bad at math due to Krish's horrid explanations. Words are hard - said by the Indian. :()
 *
 * This class was made to test the CV that we wanted to use for the Skystone challenge. It contains the Stone class which will be used to create the 3 stones that we will
 * check in the beginning of the autonomous phase in order to determine where the skystone is. The main notable methods to be used from this class is calcDistance, which
 * will be used in order to compare the difference between the values of the 3 blocks and set the position of the skystone to the largest difference it finds.
 */
public class SkystoneCVTest extends OpenCVPipeline {
    // initializing of positions
    public enum Position {UNKNOWN, LEFT, CENTER, RIGHT}
    //public Position skystonePos = Position.UNKNOWN;
    private Position skystonePos = Position.UNKNOWN;
    // initializing a unless variable
    private String magnitudes = "";
    // initializing double arrays to hold the values of each block - key to the class working
    public double[] leftImageValues;
    public double[] centerImageValues;
    public double[] rightImageValues;
    // constants used in the formation of the rectangular images to check position of skystone
    private int X = 260;
    private int Y = 190;
    final private int WIDTH_AND_HEIGHT = 30;
    final private int GAP_BETWEEN_BOXES = 125;

    // creation of the frames used to calculate HSL values by using the RGB values and converting to HLS values
    public Mat processFrame(Mat rgba, Mat gray) {
        Rect cropLeft = new Rect(X, Y, WIDTH_AND_HEIGHT, WIDTH_AND_HEIGHT);
        Mat leftCropped = new Mat(rgba, cropLeft);
        Mat leftHls = new Mat();
        Imgproc.cvtColor(leftCropped, leftHls, Imgproc.COLOR_RGB2HLS);

        Rect cropCenter = new Rect(X, Y+GAP_BETWEEN_BOXES , WIDTH_AND_HEIGHT, WIDTH_AND_HEIGHT);
        Mat centerCropped = new Mat(rgba, cropCenter);
        Mat centerHls = new Mat();
        Imgproc.cvtColor(centerCropped, centerHls, Imgproc.COLOR_RGB2HLS);

        Rect cropRight = new Rect(X, Y+GAP_BETWEEN_BOXES*2, WIDTH_AND_HEIGHT, WIDTH_AND_HEIGHT);
        Mat rightCropped = new Mat(rgba, cropRight);
        Mat rightHls = new Mat();
        Imgproc.cvtColor(rightCropped, rightHls, Imgproc.COLOR_RGB2HLS);

        Imgproc.rectangle(rgba, new Point(X, Y),
                new Point(X+WIDTH_AND_HEIGHT, Y+WIDTH_AND_HEIGHT),
                new Scalar(0, 255, 0, 0));
        Imgproc.rectangle(rgba, new Point(X, Y+GAP_BETWEEN_BOXES),
                new Point(X+WIDTH_AND_HEIGHT, Y+WIDTH_AND_HEIGHT+GAP_BETWEEN_BOXES),
                new Scalar(0, 255, 0, 0));
        Imgproc.rectangle(rgba, new Point(X, Y+GAP_BETWEEN_BOXES*2),
                new Point(X+WIDTH_AND_HEIGHT, Y+WIDTH_AND_HEIGHT+GAP_BETWEEN_BOXES*2),
                new Scalar(0, 255, 0, 0));

        leftImageValues = calcValueOfPoints(leftHls);
        centerImageValues = calcValueOfPoints(centerHls);
        rightImageValues = calcValueOfPoints(rightHls);

        calcPos();

        return rgba;
    }
    // Calculating HLS values for each box
    public double[] calcValueOfPoints(Mat hls) {
        double[] hlsValues = new double[3];
        final double CONVERSION = Math.pow(WIDTH_AND_HEIGHT, 2);
        double[] data = new double[hls.channels()];

        for (int y = 0; y < hls.rows(); y++) {
            for (int x = 0; x < hls.cols(); x++) {
                data = hls.get(x, y);
                if (hls.channels() == 3 && hls.get(x, y) != null) {
                    hlsValues[0] += data[0];
                    hlsValues[1] += data[1];
                    hlsValues[2] += data[2];
                }
            }
        }
        hlsValues[0] /= CONVERSION;
        hlsValues[1] /= CONVERSION;
        hlsValues[2] /= CONVERSION;
        return hlsValues;
    }
    // converts a value to a string to make it simple to add data to telemetry
    public String value2Str(double[] arr) {
        String results = "null";
        if (arr.length == 3) {
            results = Math.round(arr[0]) + ", " + Math.round(arr[1]) + ", " + Math.round(arr[2]);
        }
        return results;
    }
    // calculates position of skystone using stone class
    public void calcPos() {
        Stone left = new Stone("Left", leftImageValues);
        Stone center = new Stone("Center", centerImageValues);
        Stone right = new Stone("Right", rightImageValues);

        /*if (leftImageValues[0] > centerImageValues[0] && leftImageValues[0] > rightImageValues[0])
            skystonePos = Position.LEFT;
        else if (centerImageValues[0] > leftImageValues[0] && centerImageValues[0] > rightImageValues[0])
            skystonePos = Position.CENTER;
        else
            skystonePos = Position.RIGHT;*/

        /*if (left.getSature()<center.getSature() && left.getSature()<right.getSature()){
            skystonePos = Position.LEFT;
        }
        else if (right.getSature()<center.getSature() && right.getSature()<left.getSature()){
            skystonePos = Position.RIGHT;
        }
        else if (center.getSature()<left.getSature() && center.getSature()<right.getSature()){
            skystonePos = Position.CENTER;
        }
        else {
            skystonePos = Position.UNKNOWN;
        }*/

        double leftCenter = Stone.calcDistance(left, center);
        double leftRight = Stone.calcDistance(left,right);
        double centerRight = Stone.calcDistance(center, right);

        if (leftCenter<leftRight && leftCenter<centerRight) {
            skystonePos = Position.LEFT;
        }
        else if(leftRight<leftCenter && leftRight<centerRight) {
            skystonePos = Position.CENTER;
        }
        else if(centerRight<leftRight && centerRight<leftCenter) {
            skystonePos = Position.RIGHT;
        }
        else {
            skystonePos = Position.UNKNOWN;
        }
    }
    // gets the position string to send to telemetry -- Only for TESTING
    public String getPosition() {
        switch (skystonePos) {
            case LEFT:
                return "left";
            case CENTER:
                return "center";
            case RIGHT:
                return "right";
            default:
                return "unknown";
        }
    }
    // gets the position to send to telemetry -- Mainly for autonomous coding -- Accessor
    public Position getSkystonePos() {
        return skystonePos;
    }
    // gets the distance for calculates
    public String getDistance() {
        Stone left = new Stone("Left", leftImageValues);
        Stone center = new Stone("Center", centerImageValues);
        Stone right = new Stone("Right", rightImageValues);
        return Stone.calcDistance(left, center)+ ", " + Stone.calcDistance(left, right) + ", " + Stone.calcDistance(center, right);
    }

    public void changeCrop(boolean blue){
        if (!blue) {
            X = 260;
            Y = 230;
        }
    }
}


class Stone {

    private String id;
    private double[] hlsValues;

    public Stone (String id, double[] hlsValues) {
        this.id  = id;
        this.hlsValues = hlsValues;
    }

    public String getID() {
        return id;
    }

    public double getHue() {
        return hlsValues[0];
    }

    public double getLight() {
        return hlsValues[1];
    }

    public double getSature() {
        return hlsValues[2];
    }

    public static double calcDistance(Stone first, Stone second) {
        double distance = 0;
        distance = Math.sqrt(Math.pow(first.getHue()-second.getHue(), 2) + Math.pow(first.getLight()-second.getLight(), 2) + Math.pow(first.getSature()-second.getSature(), 2));
        return distance;
    }
}
