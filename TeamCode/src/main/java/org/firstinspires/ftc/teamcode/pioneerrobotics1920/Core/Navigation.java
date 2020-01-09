package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

/** Todo: Revamp the navigaton class -> a lot of it's capabilities are met in the Driving class, we need something to seperate it */
public class Navigation {
    private double x, y, angleDiff;
    private Driving driving;
    public double power = 1;
    public Navigation(Driving driver) {
        driving = driver;
    }
    private double Kp = 1;
    private double Ki = 0;
    private double Kd = 0;

    public double turnAngle;

    public void currPos(double x0, double y0, double angle0) {
        x = x0;
        y = y0;
        angleDiff = angle0; //driving.gyro.getValueContinuous();
    }

    public void moveTo(double x1, double y1) {
        moveTo(x1, y1, power);
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
        driving.linearOpMode.telemetry.update();
    }
    public void moveToX(double x1) {
        moveTo(x1,y);
    }
    public void moveToX(double x1, double power) {
        moveTo(x1,y,power);
    }

    public void moveToY(double y1) {
        moveTo(x,y1);
    }

    public void backTo(double x1, double y1) { backTo(x1, y1, power);}

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

    public void backToY(double y1,double power) {
        backTo(x,y1,power);
    }

    public void turnTo(double angle1) { turnTo(angle1, 1); }

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

    public void turnTo(double angle1, double thresh) {
        // angle is 0 to 359
        final double TURN_POWER = 1; // positive numbers only, please
        final double CORRECT_POWER = .25;
        //int timeout = 4;
        double diff = getDiff(angle1);
        final double RAW_THRESH = 10 + Math.abs(diff) / 30.0;
        // now, diff is the angle we would pass to the old ActuatorLibrary.turn method

        while (driving.linearOpMode.opModeIsActive() && Math.abs(getDiff(angle1)) > RAW_THRESH) {
            double factor = Math.abs(getDiff(angle1))/180;
            factor = (Math.abs(factor)<0.4)? ((factor<0)? -0.4:0.4):factor;//this is ugly but oh well
            //if (factor<1) {
                if (diff > 0)
                    driving.libertyDrive(0, TURN_POWER*factor, 0);
                else
                    driving.libertyDrive(0, -TURN_POWER*factor, 0);

            driving.linearOpMode.telemetry.addData("factor", factor);
            driving.linearOpMode.telemetry.addData("difference", diff);
            driving.linearOpMode.telemetry.update();
        }

        driving.stopDriving();

        if (Math.abs(diff)>90){
            driving.sleep(500);
        }
        // no updating of member variables necessary

        // precise turn

        while ((getDiff(angle1) > thresh || getDiff(angle1) < -thresh) && driving.linearOpMode.opModeIsActive()) {
            //driving.opMode.telemetry.addData("getDiff", getDiff(angle1));
            diff = getDiff(angle1);
            driving.libertyDrive(0, driving.sgn(diff) * CORRECT_POWER, 0);
            driving.linearOpMode.idle();
            driving.linearOpMode.telemetry.addData("difference", diff);
            driving.linearOpMode.telemetry.update();
        }
        driving.stopDriving();
    }

    private double getDiff(double target) {
        double diff = target - getAngle();
        if (diff > 180)
            diff = diff - 360;
        else if (diff < -180)
            diff = diff + 360;
       // driving.opMode.telemetry.addData("diff", diff);
        //driving.opMode.telemetry.update();
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

