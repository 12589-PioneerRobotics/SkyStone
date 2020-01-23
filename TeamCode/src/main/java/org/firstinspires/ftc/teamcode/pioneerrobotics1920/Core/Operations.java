package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

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
                accelFactor = Math.pow(1.8, .02 * curPos) / 200 + .35;
                break;
        }
        switch (deccel) {
            case LINEAR:
                deccelFactor = (endPos - curPos) / 800;
                break;
            case PARABOLIC:
                deccelFactor = Math.pow(Math.abs(endPos - curPos) / 1000, 2);
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
}
