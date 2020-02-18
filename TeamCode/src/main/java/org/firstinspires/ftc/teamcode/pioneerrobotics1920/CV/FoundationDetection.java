package org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV;

import org.corningrobotics.enderbots.endercv.OpenCVPipeline;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Coordinates;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * @author Ningrui
 */

//Start position: (36, 72, 0)

public class FoundationDetection extends OpenCVPipeline {

    public double[] centerImageValues;
    public int centerX, centerY;
    public boolean blue = true;
    public double angle;
    public boolean horiz;
    public Coordinates position = new Coordinates(0.0,0.0);

    public FoundationDetection(boolean blue) {
        this.blue = blue;
    }


    public Mat processFrame(Mat rgba, Mat gray) {
        // rgba is 480x640
        Rect cropRect = new Rect(0, 0, 480, 640); // x, y, width, height
        Mat cropped = new Mat(rgba, cropRect);
        // cropped refers to a certain area of rgba
        Mat hls = new Mat();
        Imgproc.cvtColor(cropped, hls, Imgproc.COLOR_RGB2HLS); // create an HLS version of cropped

        Mat bw = new Mat();
        // filter the given image through the given colors. Based on which side we are on, will use either range.
        if(blue) {
            Core.inRange(hls, new Scalar(110, 0, 100), new Scalar(122, 175, 255), bw);
        }
        else {//The spectrum of red hues on the HSV scale is split on the edges (0-5, 350-360). So, we need to take inRange() twice and combine the resulting Mat objects.
            Mat bw1 = new Mat();
            Mat bw2 = new Mat();
            Core.inRange(hls, new Scalar(0, 15,40), new Scalar(12, 255, 255), bw1);
            Core.inRange(hls, new Scalar(170, 70, 50), new Scalar(180, 255, 255), bw2);
            Core.add(bw1, bw2, bw);
        }

        Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10));
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.erode(bw, bw, structuringElement);
        Imgproc.dilate(bw, bw, structuringElement);

        Mat hierarchy = new Mat();
        Imgproc.findContours(bw, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int maxIndex = 0;

        if (contours.size()>1) {
            for (int i = 1; i < contours.size(); i++) {
                if (Imgproc.contourArea(contours.get(i)) > Imgproc.contourArea(contours.get(maxIndex)))
                    maxIndex = i;
            }
            if(blue) {
                Imgproc.drawContours(cropped, contours, maxIndex, new Scalar(255, 0, 0));
            }
            else {
                Imgproc.drawContours(cropped, contours, maxIndex, new Scalar(0, 255, 0));
            }

            Point center = new Point();
            org.opencv.imgproc.Moments moments = Imgproc.moments(contours.get(maxIndex));
            center = new Point(moments.get_m10() / moments.get_m00(),
                    moments.get_m01() / moments.get_m00());

            //experimental code for finding orientation
            MatOfPoint2f convertBoi = new MatOfPoint2f(contours.get(maxIndex).toArray());
            RotatedRect rect = Imgproc.minAreaRect(convertBoi);

            if(rect.angle < 20) {
                if (rect.size.height * 1.4 > rect.size.width * 1.05) {
                    horiz  = false;
                    position.setY(96);
                }
                else {
                    horiz = true;
                    position.setY(122);
                }
            }
            else angle = rect.angle;
            position.setX(calcFieldX((int)center.y));


            centerX = (int) center.x;
            centerY = (int) center.y;

            Imgproc.rectangle(cropped, center, new Point(center.x + 5, center.y + 5), new Scalar(0, 255, 0, 0));

            Rect cropCenter = new Rect(237, 317, 6, 6);
            Mat centerCropped = new Mat(rgba, cropCenter);
            Mat centerHls = new Mat();
            Imgproc.cvtColor(centerCropped, centerHls, Imgproc.COLOR_RGB2HLS);

            Imgproc.rectangle(rgba, new Point(237, 317),
                    new Point(243, 323),
                    new Scalar(0, 255, 0, 0));

            centerImageValues = calcValueOfPoints(centerHls);
        }
        return rgba;
    }

    public int calcFieldX (int centerY) {
        double x = 0.0748*centerY + 9.24;
        if(blue) {
            return (int) x;
        }
        else return 144 - (int)x;
    }

    public double[] calcValueOfPoints(Mat hls) {
        double[] hlsValues = new double[3];
        final double CONVERSION = Math.pow(6, 2);
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

    public String value2Str(double[] arr) {
        String results = "null";
        if (arr.length == 3) {
            results = Math.round(arr[0]) + ", " + Math.round(arr[1]) + ", " + Math.round(arr[2]);
        }
        return results;
    }
}
