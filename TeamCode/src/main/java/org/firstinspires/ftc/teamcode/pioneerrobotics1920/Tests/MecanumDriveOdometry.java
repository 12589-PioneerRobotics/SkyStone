package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.CurvePoint;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;

import java.util.ArrayList;

@TeleOp(name = "MecanumDriveOdometry")
public class MecanumDriveOdometry extends LinearOpMode {
    private Driving drive;
    private Navigation navigation;
    double verticalRightEncoderWheelPosition = 0, verticalLeftEncoderWheelPosition = 0, normalEncoderWheelPosition = 0, changeInRobotOrientation = 0, gyroVal;
    public static double robotGlobalXCoordinatePosition = 0, robotGlobalYCoordinatePosition = 0, robotOrientationRadians = 0;
    public static double previousVerticalRightEncoderWheelPosition = 0, previousVerticalLeftEncoderWheelPosition = 0, prevNormalEncoderWheelPosition = 0, previousGyroValue = 0;

    private double SCALE = .35;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        navigation = new Navigation(drive);
        telemetry.addData("init finished", null);
        telemetry.update();
        drive.stopDriving();

        waitForStart();
//        ----------------------------------------------------------------------
        drive.stopDriving();

        ArrayList<CurvePoint> allPoints = new ArrayList<>();
        allPoints.add(new CurvePoint(0.0,0.0,.5,.5,20.0,Math.toRadians(20), 1.0));
        allPoints.add(new CurvePoint(0.0,22.0,.5,.5,20.0,Math.toRadians(20), 1.0));
        allPoints.add(new CurvePoint(0.0,44.0,.5,.5,20.0,Math.toRadians(20), 1.0));
        allPoints.add(new CurvePoint(0.0,88.0,.5,.5,20.0,Math.toRadians(20), 1.0));
        allPoints.add(new CurvePoint(40.0,88.0,.5,.5,20.0,Math.toRadians(20), 1.0));
        allPoints.add(new CurvePoint(80.0,88.0,.5,.5,20.0,Math.toRadians(20), 1.0));
        allPoints.add(new CurvePoint(87.0,10,.5,.5,20.0,Math.toRadians(20), 1.0));
        //allPoints.add(new CurvePoint(0,10.0,.5,.5,20.0,Math.toRadians(20), 1.0));
        while (this.opModeIsActive()){
            coordinateUpdate();
            if(gamepad1.a) {
                navigation.goToPosition(20, 100, .5, 0, .3);
            }
            else{
                //drive.setAllDrivingPowers(0);
            }


            navigation.followCurve(allPoints,Math.toRadians(0));


            telemetry.addData("globalX", robotGlobalXCoordinatePosition);
            telemetry.addData("globalY", robotGlobalYCoordinatePosition);
            telemetry.addData("distanceToTarget", navigation.distanceToTarget);
            telemetry.addData("relativeAngle", navigation.relativeTurnAngle);
            telemetry.update();

            /*if (gamepad1.left_bumper)
                drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.35, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE + 0.25));
            else
                drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.3), gamepad1.left_stick_x);


            telemetry.addData("globalX", robotGlobalXCoordinatePosition);
            telemetry.addData("globalY", robotGlobalYCoordinatePosition);
            telemetry.update();*/
            }

    }

    private void coordinateUpdate(){
        verticalLeftEncoderWheelPosition = (((double) drive.frontLeft.getCurrentPosition()  + (double) drive.backLeft.getCurrentPosition())/ drive.CLICKS_PER_INCH ) / 2;
        verticalRightEncoderWheelPosition = (((double) drive.frontRight.getCurrentPosition() + (double) drive.backRight.getCurrentPosition()) / drive.CLICKS_PER_INCH) / 2;

        gyroVal = drive.gyro.getValueContinuous();

        double leftChange = verticalLeftEncoderWheelPosition - previousVerticalLeftEncoderWheelPosition;
        double rightChange = verticalRightEncoderWheelPosition - previousVerticalRightEncoderWheelPosition;


        double p = ((rightChange + leftChange) / 2);

        robotGlobalXCoordinatePosition = robotGlobalXCoordinatePosition + (Math.sin(robotOrientationRadians)*p);
        robotGlobalYCoordinatePosition = robotGlobalYCoordinatePosition + (p * Math.cos(robotOrientationRadians));


        robotOrientationRadians = Operations.AngleWrap(Math.toRadians(gyroVal));

        previousVerticalLeftEncoderWheelPosition = verticalLeftEncoderWheelPosition;
        previousVerticalRightEncoderWheelPosition = verticalRightEncoderWheelPosition;

    }
}
