package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Navigation {
    private double x, y, angleDiff;
    private Driving driving;
    final private double NAV_POWER = 1;
    private double turnAngle;

    public Navigation(Driving driver) {
        driving = driver;
    }

    public void IamAt(double x0, double y0) {
        x = x0;
        y = y0;
    }

    public void currPos(double x0, double y0, double angle0) {
        x = x0;
        y = y0;
        angleDiff = angle0;//driving.gyro.getValueContinuous();
    }

    public void moveTo(double x1, double y1) {
        moveTo(x1, y1, NAV_POWER);
    }
    public void moveTo(double x1, double y1, double power) {
        double diffX = x1 - x;
        double diffY = y1 - y;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        turnAngle = 90 - Math.toDegrees(Math.atan2(diffY, diffX));
        turnTo(turnAngle);

        driving.forward(distance, power);

        x = x1;
        y = y1;
    }

    public void moveToX(double x1) {
        moveTo(x1,y);
    }

    public void moveToY(double y1) {
        moveTo(x,y1);
    }

    public void backTo(double x1, double y1) { backTo(x1, y1, NAV_POWER);}
    public void backTo(double x1, double y1, double power) {
        double diffX = x1 - x;
        double diffY = y1 - y;
        double distance = - Math.sqrt(diffX * diffX + diffY * diffY);

        turnAngle = -90 - Math.toDegrees(Math.atan2(diffY, diffX));

        turnTo(turnAngle, 1.5);

        driving.forward(distance, power);

        x = x1;
        y = y1;
    }

    public void backToX(double x1) {
        backTo(x1,y);
    }

    public void backToY(double y1) {
        backTo(x,y1);
    }

    public void turnToP(double angle1, double kp, double ki){
        double diff;
        double turnPower;
        double steadyState = 0;
        double turn;

        while ((driving.linearOpMode.opModeIsActive()) && (Math.abs(getDiff(angle1)) > 4)){
            diff = getDiff(angle1);
            turnPower = diff / 90;
            steadyState += diff;
            turn = (kp * turnPower) + (ki * steadyState);

            driving.libertyDrive(0,turn,0);
        }
        driving.stopDriving();
    }

    public void arc(double angle1, double thresh, double turnPower, double drivePower) {
        double diff = getDiff(angle1);
        double RAW_THRESH = thresh;
        double TURN_POWER = turnPower;
        while (driving.linearOpMode.opModeIsActive() && Math.abs(getDiff(angle1)) > RAW_THRESH) {
            if (diff > 0)
                driving.libertyDrive(drivePower, TURN_POWER, 0);
            else
                driving.libertyDrive(drivePower, -TURN_POWER, 0);
            driving.linearOpMode.telemetry.addData("difference", diff);
            driving.linearOpMode.telemetry.addData("nav angle: ", getAngle());
            driving.linearOpMode.telemetry.update();
        }
        driving.stopDriving();

        while ((getDiff(angle1) > thresh || getDiff(angle1) < -thresh) && driving.linearOpMode.opModeIsActive()) {
            diff = getDiff(angle1);
            driving.libertyDrive(0, Operations.sgn(diff) * .3, 0);
            driving.linearOpMode.idle();
            driving.linearOpMode.telemetry.addData("difference", diff);

            driving.linearOpMode.telemetry.update();
        }
        driving.stopDriving();
    }

    public void turnToCorrectPower(double angle1, double correctPower) {
        turnTo(angle1, 1, correctPower);
    }

    public void turnTo(double angle1, double thresh) {
        turnTo(angle1, thresh, .25);
    }

    public void turnTo(double angle1) {
        turnTo(angle1, 1, .275);
    }

    public void turnTo(double angle1, double thresh, double correctPower) {
        ElapsedTime time = new ElapsedTime();
        time.reset();
        // angle is 0 to 359
        final double TURN_POWER = 1; // positive numbers only, please
        final double CORRECT_POWER = correctPower;
        double diff = getDiff(angle1);
        final double RAW_THRESH = 5 + Math.abs(diff) / 30.0;
        // now, diff is the angle we would pass to the old ActuatorLibrary.turn method
        while (driving.linearOpMode.opModeIsActive() && Math.abs(getDiff(angle1)) > RAW_THRESH) {
            double factor = Math.abs(getDiff(angle1)) / 105; // denominator is arbitrary. Make smaller for higher powers (and faster turns) or decrease for smaller power.
            factor = (Math.abs(factor)<0.45)? ((factor<0)? -0.45:0.45):factor;//this is ugly but oh well
            //if (factor<1) {
            if (diff > 0)
                driving.libertyDrive(0, TURN_POWER*factor, 0);
            else
                driving.libertyDrive(0, -TURN_POWER*factor, 0);
            //driving.linearOpMode.telemetry.addData("factor", factor);
            driving.linearOpMode.telemetry.addData("difference", diff);
            driving.linearOpMode.telemetry.addData("nav angle: ", getAngle());
            driving.linearOpMode.telemetry.addData("Turn To", angle1);
            driving.linearOpMode.telemetry.update();
        }
        driving.stopDriving();
        driving.sleep(100);
        // no updating of member variables necessary
        // precise turn
        while (Math.abs(getDiff(angle1)) > thresh && driving.linearOpMode.opModeIsActive()) {
            diff = getDiff(angle1);
            driving.libertyDrive(0, Operations.sgn(diff) * CORRECT_POWER, 0);
            driving.linearOpMode.idle();
            driving.linearOpMode.telemetry.addData("difference", diff);
            driving.linearOpMode.telemetry.addData("nav angle: ", getAngle());
            driving.linearOpMode.telemetry.addData("Turn To", angle1);
            driving.linearOpMode.telemetry.update();
        }
        driving.linearOpMode.telemetry.addData("Time elapsed: ", time.milliseconds());
        time.reset();
        driving.stopDriving();
    }

    public double getDiff(double target) {
        double diff = target - getAngle();
        if (diff > 180)
            diff = diff - 360;
        else if (diff < -180)
            diff = diff + 360;
        return diff;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        double result = driving.gyro.getValueContinuous() + angleDiff;
        if (result < 0) {
            result += (int) (1 - result / 360) * 360;
        }
        else if (result > 360) {
            result = result % 360;
        }
        return result;
    }
}

