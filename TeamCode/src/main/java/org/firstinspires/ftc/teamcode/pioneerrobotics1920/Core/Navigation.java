package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests.MecanumDriveOdometry;
import org.opencv.core.Point;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests.MecanumDriveOdometry.robotGlobalXCoordinatePosition;
import static org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests.MecanumDriveOdometry.robotGlobalYCoordinatePosition;
import static org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests.MecanumDriveOdometry.robotOrientationRadians;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;

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
    public void followCurve (ArrayList<CurvePoint> allPoints, double followAngle){
        CurvePoint followMe = getFollowPointPath(allPoints, new Point(robotGlobalXCoordinatePosition, robotGlobalYCoordinatePosition),allPoints.get(0).followDistance);

        // Need to figure out how to get robot to follow the generated points
        if (followMe.x == 0.0 && followMe.y == 0.0) {
            driving.linearOpMode.telemetry.addData("StopCase", "STOPPP");
            driving.linearOpMode.telemetry.update();
            driving.setAllDrivingPowers(0);
        } else{
            goToPosition(followMe.x, followMe.y, followMe.moveSpeed, followAngle, followMe.turnSpeed);
            driving.linearOpMode.telemetry.addData("goToPosition", String.valueOf(followMe.x) + " | " + String.valueOf(followMe.y));
        }


    }

    public double distanceToTarget;
    public double relativeAngleToPoint;
    public double relativeTurnAngle;

    public void goToPosition(double x, double y , double movementSpeed, double preferredAngle, double turnSpeed){
        distanceToTarget = Math.hypot(x-robotGlobalXCoordinatePosition,y-robotGlobalYCoordinatePosition);

        double absoluteAngleToTarget = Operations.AngleWrap(Math.toRadians(90)-Math.atan2(y-robotGlobalYCoordinatePosition, x-robotGlobalXCoordinatePosition));

        relativeAngleToPoint = absoluteAngleToTarget-robotOrientationRadians;

        double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
        double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;

        //double movementXPower = relativeXToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
        //double movementYPower = relativeYToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));

        //double movement_x = movementXPower * movementSpeed;
        //double movement_y = movementYPower * movementSpeed;

        relativeTurnAngle = relativeAngleToPoint - Math.toRadians(0)+ preferredAngle;
        double movement_turn = Range.clip(relativeTurnAngle/Math.toRadians(30),-1,1) * turnSpeed;

        if (false){
            movement_turn = 0;
            driving.setAllDrivingPowers(0);
        }else {
            driving.libertyDrive(movementSpeed, movement_turn, 0);
        }

    }

    public static CurvePoint getFollowPointPath (ArrayList<CurvePoint> pathPoints, Point robotLocation, double followRadius){
        CurvePoint followMe = new CurvePoint((pathPoints.get(0)));

        for(int i= 0; i < pathPoints.size() - 1; i++){
            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endline = pathPoints.get(i+1);

            ArrayList<Point> intersections = Operations.lineCircleIntersection(robotLocation, followRadius, startLine.toPoint(),
                    endline.toPoint());

            double closestAngle = 1000000000;
            for (Point thisIntersection : intersections) {
                double angle = Math.atan2(thisIntersection.y - robotGlobalYCoordinatePosition, thisIntersection.x - robotGlobalXCoordinatePosition);
                double deltaAngle = Math.abs(Operations.AngleWrap(angle - MecanumDriveOdometry.robotOrientationRadians));

                if (deltaAngle < closestAngle) {
                    closestAngle = deltaAngle;
                    followMe.setPoint(thisIntersection);
                }
            }
        }
        return followMe;
    }

    public void arc(double angle1, double thresh, double turnPower, double drivePower) {
        double diff = getDiff(angle1);
        double initDiff = getDiff(angle1);
        double RAW_THRESH = 5 + Math.abs(diff) / 15;
        double TURN_POWER = turnPower;
        while (driving.linearOpMode.opModeIsActive() && Math.abs(getDiff(angle1)) > RAW_THRESH) {
            double factor = Math.abs(getDiff(angle1)) / initDiff;
            factor = (Math.abs(factor) > 0.25) ? 1 : factor;
            if (diff > 0)
                driving.libertyDrive(drivePower, TURN_POWER*factor, 0);
            else
                driving.libertyDrive(drivePower, -TURN_POWER*factor, 0);
            diff = getDiff(angle1);
            driving.linearOpMode.telemetry.addData("difference", diff);
            driving.linearOpMode.telemetry.addData("nav angle: ", getAngle());
            driving.linearOpMode.telemetry.update();
        }

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
        turnTo(angle1, 2, .25);
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
            double factor = Math.abs(getDiff(angle1)) / 90; // denominator is arbitrary. Make smaller for higher powers (and faster turns) or decrease for smaller power.
            factor = (Math.abs(factor)<0.45)? ((factor<0)? -0.45:0.45):factor;//this is ugly but oh well
            //if (factor<1) {
            if (diff > 0)
                driving.libertyDrive(0, TURN_POWER*factor, 0);
            else
                driving.libertyDrive(0, -TURN_POWER*factor, 0);
            //driving.linearOpMode.telemetry.addData("factor", factor);
            driving.linearOpMode.telemetry.addData("difference", diff);
            driving.linearOpMode.telemetry.addData("nav angle: ", getAngle());
            driving.linearOpMode.telemetry.update();
        }
        driving.stopDriving();
        // no updating of member variables necessary
        // precise turn
        while ((getDiff(angle1) > thresh || getDiff(angle1) < -thresh) && driving.linearOpMode.opModeIsActive()) {
            diff = getDiff(angle1);
            driving.libertyDrive(0, Operations.sgn(diff) * CORRECT_POWER, 0);
            driving.linearOpMode.idle();
            driving.linearOpMode.telemetry.addData("difference", diff);
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

