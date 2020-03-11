package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import android.net.MacAddress;

import org.opencv.core.Point;

import java.util.ArrayList;

import java.awt.*;

import static java.lang.Math.*;

public class Operations {

    public enum AccelerationAlgorithms {LINEAR, EXPONENTIAL, RATIONAL}
    public enum DeccelerationAlgorithms {LINEAR, PARABOLIC}

    public static double chooseAlgorithm(AccelerationAlgorithms accel, DeccelerationAlgorithms deccel, double endPos, double curPos) {

        curPos = Math.abs(curPos);
        endPos = Math.abs(endPos);

        double accelFactor = 0, deccelFactor = 0;
        switch (accel) {
            case LINEAR:
                accelFactor = curPos / 400;
                break;
            case RATIONAL:
                accelFactor = 1 - 300 / (5 * curPos + 300);
                break;
            case EXPONENTIAL:
                accelFactor = pow(1.5, .04 * curPos) / 200 + .35;
                break;
        }
        switch (deccel) {
            case LINEAR:
                deccelFactor = (endPos - curPos) / 800;
                break;
            case PARABOLIC:
                deccelFactor = pow(Math.abs(endPos - curPos) / 1000, 2);
                break;
        }
        return Math.min(accelFactor, deccelFactor);
    }

    public static double cmToInch(double cm) {
        return cm / 2.54;
    }

    public static int sgn(double x) {
        if (x < 0) return -1;
        return 1;
    }

    public static void approximatelyEquals(double value, double target) {
        approximatelyEquals(value, target, target * 0.06);
    }

    public static boolean approximatelyEquals(double value, double target, double thresh) {
        return Math.abs(value - target) < thresh;
    }

    public static double power(double powerValue, double floor, double min, double max) {
        if (powerValue > max)
            return max;
        if (powerValue < min)
            return min;
        if (Math.abs(powerValue) < Math.abs(floor))
            return sgn(powerValue) * floor;
        return powerValue;
    }
    public static double powerScale(double power){
        return powerScale(power, 1);
    }

    public static double powerScale(double power, double scale){
        if (power<=1) {
            if (power < 0)
                return -(scale * power * power);
            else
                return scale * power * power;
        }
        return 1;
    }

    public static int roundNearest90(float val){
        return Math.round(val/90)*90;
    }

    public static ArrayList<Point>  lineCircleIntersection(Point circleCenter, double radius, Point linePoint1,Point linePoint2){
        if(Math.abs(linePoint1.y - linePoint2.y)< .003)
            linePoint1.y = linePoint2.y + .003;
        if(Math.abs(linePoint1.x - linePoint2.x) < .003)
            linePoint1.x = linePoint2.x + 0.003;

        double m1 = (linePoint2.y-linePoint1.y)/(linePoint2.x - linePoint1.x);

        double quadraticA = 1.0 + pow(m1,2);

        double x1 = linePoint1.x - circleCenter.x;
        double y1 = linePoint1.y - circleCenter.y;

        double quadraticB = (2.0 * m1 * y1) - (2.0 * pow(m1,2) * x1);
        double quadraticC = pow(m1,2) * pow(x1,2) - (2.0*y1*m1*x1) + pow(y1,2) - pow(radius,2);

        ArrayList<Point> allPoints = new ArrayList<>();

        try{
            double xRoot1 = (-quadraticB + sqrt(pow(quadraticB,2) - (4.0 * quadraticA * quadraticC)))/(2.0*quadraticA);

            double yRoot1 = m1 * (xRoot1 - x1) +y1;

            xRoot1 += circleCenter.x;
            yRoot1 = circleCenter.y;

            double minX = linePoint1.x < linePoint2.x ? linePoint1.x : linePoint2.x;
            double maxX = linePoint1.x > linePoint2.x ? linePoint1.x : linePoint2.x;

            if(xRoot1 > minX && xRoot1 < maxX)
                allPoints.add(new Point(xRoot1,yRoot1));

            double xRoot2 = (-quadraticB - sqrt(pow(quadraticB,2) - (4.0 * quadraticA * quadraticC)))/(2.0*quadraticA);
            double yRoot2 = m1 * (xRoot2 - x1) + y1;

            xRoot2 += circleCenter.x;
            yRoot2 += circleCenter.y;

            if(xRoot2 > minX && xRoot2 < maxX)
                allPoints.add(new Point(xRoot2,yRoot2));
        }catch (Exception e){

        }
        return allPoints;
    }

    public static double AngleWrap(double angle) {
        while(angle< - PI)
            angle += 2 * PI;
        while(angle > PI)
            angle -= 2 * PI;

        return angle;

    }
}
