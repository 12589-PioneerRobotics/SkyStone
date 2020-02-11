package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class Driving {
    //instantiations
    LinearOpMode linearOpMode;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public GyroWrapper gyro;


    public final double CLICKS_PER_INCH = 29.021876534;

    private DcMotor[] drivingMotors;

    public ModernRoboticsI2cRangeSensor frontDistance, backDistance, leftDistance, rightDistance;

    private void initHardware(HardwareMap hardwareMap) {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        gyro = new GyroWrapper(hardwareMap.get(BNO055IMU.class, "imu"));
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        drivingMotors = new DcMotor[]{frontRight, frontLeft, backLeft, backRight};
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    //if distance sensors are going to be used
    private void initDistanceSensors(HardwareMap hardwareMap) {
        frontDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontDistance");
        backDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "backDistance");
        leftDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "leftDistance");
        rightDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rightDistance");
    }

    public Driving(OpMode opMode) {
        initHardware(opMode.hardwareMap);
    }

    public Driving(LinearOpMode opMode) {
        initHardware(opMode.hardwareMap);
        initDistanceSensors(opMode.hardwareMap);
        linearOpMode = opMode;
    }

    public void setAllDrivingPositions(int clicks) {
        for (DcMotor motor : drivingMotors) {
            motor.setTargetPosition(clicks);
        }
    }

    public void setDrivingModes(DcMotor.RunMode mode) {
        for (DcMotor motor : drivingMotors) {
            motor.setMode(mode);
        }
    }

    void setAllDrivingPowers(double power) {
        for (DcMotor motor : drivingMotors) {
            motor.setPower(power);
        }
    }

    int averageEncoderPositions() {
        return (frontLeft.getCurrentPosition() + frontRight.getCurrentPosition() +
                backLeft.getCurrentPosition() + backRight.getCurrentPosition()) / 4;
    }

    public void stopDriving() {
        setAllDrivingPowers(0);
        setDrivingModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearOpMode.idle();
    }

    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Position getPosition() {
        return gyro.reportPosition();
    }

    public boolean motorsBusy() {
        return frontRight.isBusy() && frontLeft.isBusy() && backRight.isBusy() && backLeft.isBusy();
    }

    public double getAccurateDistanceSensorReading(ModernRoboticsI2cRangeSensor distanceSensor) {
        double result = distanceSensor.getDistance(DistanceUnit.INCH);
        while (result > 50 || result < 0)
            result = distanceSensor.getDistance(DistanceUnit.INCH);
        return result;
    }

    public void forwardPos(double inches, double power) {
        int clicks = (int) (inches * CLICKS_PER_INCH);

        /*
        setDrivingModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setAllDrivingPositions(clicks);
        setDrivingModes(DcMotor.RunMode.RUN_TO_POSITION);
        setAllDrivingPowers(power)
        */

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setTargetPosition(clicks);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setPower(power);
    }

    //move forward method based on inches
    public void forward(double inches, double power) {
        forward(inches, power, 0.25);
    }
    public void forward(double inches, double power, double powerFloor) {
        int clicks = (int) (inches * CLICKS_PER_INCH);
        stopDriving();
        setAllDrivingPositions(clicks);
        setDrivingModes(DcMotor.RunMode.RUN_TO_POSITION);
        //wait while opmode is active and left motor is busy running to position.
        while (linearOpMode.opModeIsActive() && motorsBusy()) {
            linearOpMode.idle();
            double factor = Operations.chooseAlgorithm(Operations.AccelerationAlgorithms.EXPONENTIAL, Operations.DeccelerationAlgorithms.PARABOLIC, clicks, averageEncoderPositions());
            if (factor>1) factor = 1;
            double newPower = power * factor;
            setAllDrivingPowers(Math.max(newPower, powerFloor));
        }
        stopDriving();
    }

    public void moveClose(String direction, double distance, double power, float thresh){
        double dx = 5;
        setAllDrivingPowers(0);
        if (direction.equals("front")) {
            Log.i("FrontDistance dist", frontDistance.getDistance(DistanceUnit.INCH) + "");
            Log.i("Frontdistance cmultra", frontDistance.cmUltrasonic() + "");
            double diff = frontDistance.getDistance(DistanceUnit.INCH) - distance;
            while (Math.abs(diff) > 50) {
                diff = frontDistance.getDistance(DistanceUnit.INCH) - distance;
                Log.v("Front distance value", Operations.cmToInch(frontDistance.cmUltrasonic()) + "");
                Log.v("diff in loopy", diff + "");
            }
            /*
            while ((Math.abs(diff)) > thresh) {
                linearOpMode.telemetry.addData("Front Distance: ", frontDistance.getDistance(DistanceUnit.INCH));
                linearOpMode.telemetry.update();
                if (diff >= 0)
                    libertyDrive(power, 0, 0);
                else
                    libertyDrive(-power, 0, 0);
                //diff = frontDistance.getDistance(DistanceUnit.INCH)- distance;
                diff = ((frontDistance.getDistance(DistanceUnit.INCH)- distance) > 100)? diff : frontDistance.getDistance(DistanceUnit.INCH) - distance;
            }
            */
            forward(diff, power, .6);
        }

        if (direction.equals("back")) {
            double value = backDistance.cmUltrasonic();
            Log.i("BackDistance dist", backDistance.getDistance(DistanceUnit.INCH) + "");
            Log.i("BackDistance cmUltra", backDistance.cmUltrasonic() + "");
            double diff = Operations.cmToInch(value) - distance;
            while (Math.abs(diff) > 50) {
                diff = Operations.cmToInch(backDistance.cmUltrasonic()) - distance;
                Log.v("Back distance value", Operations.cmToInch(backDistance.cmUltrasonic()) + "");
                Log.v("diff in loopy", diff + "");
            }
            /*
            while (Math.abs(diff) > thresh) {
                linearOpMode.telemetry.addData("Back Distance: ", backDistance.getDistance(DistanceUnit.INCH));
                linearOpMode.telemetry.update();
                if (diff>=0)
                    libertyDrive(-power, 0, 0);
                else
                    libertyDrive(power, 0, 0);
                diff = ((Operations.cmToInch(backDistance.cmUltrasonic()) - distance)>100)? diff:(Operations.cmToInch(backDistance.cmUltrasonic()) - distance);
            }
            */
            forward(-diff, power, .6);
        }
        if (direction.equals("left")) {
            double diff = leftDistance.getDistance(DistanceUnit.INCH) - distance;
            while (Math.abs(diff) > 50)
                diff = leftDistance.getDistance(DistanceUnit.INCH) - distance;
            sleep(10);
            while(Math.abs(diff) > thresh) {
                linearOpMode.telemetry.addData( "Left Distance: ", leftDistance.getDistance(DistanceUnit.INCH));
                linearOpMode.telemetry.update();
                if (diff >= 0)
                    libertyDrive(0, 0, -power);
                else
                    libertyDrive(0, 0, power);

                diff = (Math.abs(((leftDistance.getDistance(DistanceUnit.INCH) - distance)) - diff) > dx) ? diff : (leftDistance.getDistance(DistanceUnit.INCH) - distance);
            }
        }
        if (direction.equals("right")) {
            double diff = Operations.cmToInch(rightDistance.cmUltrasonic()) - distance;
            while (Math.abs(diff) > 50)
                diff = Operations.cmToInch(rightDistance.cmUltrasonic()) - distance;
            while(Math.abs(diff) > thresh) {
                linearOpMode.telemetry.addData("Right Distance: ", Operations.cmToInch(rightDistance.cmUltrasonic()));
                linearOpMode.telemetry.update();
                if (diff >= 0)
                    libertyDrive(0, 0, power);
                else
                    libertyDrive(0, 0, -power);
                diff = (Math.abs((Operations.cmToInch(rightDistance.cmUltrasonic()) - distance) - diff) > dx) ? diff : Operations.cmToInch(rightDistance.cmUltrasonic()) - distance;
            }
        }
        stopDriving();
    }

    public void strafeClose(boolean blue, double x, double y) {
        double deltaY;
        double deltaX = x - Operations.cmToInch(backDistance.cmUltrasonic());
        if (blue) {
            deltaY = y - Operations.cmToInch(rightDistance.cmUltrasonic());

            while (Math.abs(deltaX) > 2 || Math.abs(deltaY) > 2) {
                while (Math.abs(deltaY) > 50 || Math.abs(deltaX) > 50) {
                    deltaY = y - Operations.cmToInch(rightDistance.cmUltrasonic());
                    deltaX = x - Operations.cmToInch(backDistance.cmUltrasonic());
                }
                libertyDrive(deltaX / 24, 0, -deltaY / 10);
                linearOpMode.telemetry.addData("deltaX", deltaX);
                linearOpMode.telemetry.addData("deltaY", deltaY);
                linearOpMode.telemetry.update();
                deltaX = x - Operations.cmToInch(backDistance.cmUltrasonic());
                deltaY = y - Operations.cmToInch(rightDistance.cmUltrasonic());
            }

        } else {
            deltaY = Operations.cmToInch(leftDistance.cmUltrasonic()) - y;
            while (Math.abs(deltaY) > 50 || Math.abs(deltaX) > 50) {
                deltaY = Operations.cmToInch(leftDistance.cmUltrasonic()) - y;
                deltaX = x - Operations.cmToInch(backDistance.cmUltrasonic());
            }

            while (Math.abs(deltaX) > 2 || Math.abs(deltaY) > 2) {
                //libertyDrive(deltaX/4,0,deltaY/4);
                deltaX = x - Operations.cmToInch(backDistance.cmUltrasonic());
                deltaY = Operations.cmToInch(leftDistance.cmUltrasonic()) - y;
            }

        }
        stopDriving();
    }

    public void timeBasedForward(double seconds, double power) {
        double curTime = linearOpMode.getRuntime();
        while (linearOpMode.getRuntime() < curTime + seconds)
            libertyDrive(power, 0, 0);
        stopDriving();
    }

    public void timeBasedStrafe(double power, double seconds) {
        double currentTime = linearOpMode.getRuntime();
        while (linearOpMode.getRuntime() < currentTime + seconds)
            libertyDrive(0, 0, power);
        stopDriving();
    }

    public void libertyDrive(double drive, double turn, double strafe) {
        setDrivingModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (strafe != 0) {
            if (strafe < 0)
                turn += (1 - strafe) * 0.03;
            else
                turn += (-1 - strafe) * 0.03;
        }
        double factor = Math.abs(drive) + Math.abs(turn) + Math.abs(strafe);
        if (factor <= 1)
            factor = 1;
        backLeft.setPower((drive - strafe + turn) / factor);
        frontLeft.setPower((drive + strafe + turn) / factor);
        backRight.setPower((drive + strafe - turn) / factor);
        frontRight.setPower((drive - strafe - turn) / factor);
    }

    public void correctStrafe(double power, double startAngle){
        final double THRESH = 1;

        libertyDrive(0,0,power);

        double diff = Math.abs(startAngle-gyro.getValueContinuous());

        double POWER_CHANGE = diff*diff*0.01*power;

        if (gyro.getValueContinuous() > startAngle + THRESH)
            libertyDrive(0, -POWER_CHANGE, power);
        else if (gyro.getValueContinuous() < startAngle - THRESH)
            libertyDrive(0, POWER_CHANGE,power);
    }

    //TELEMETRY*************************************************************************************
    public void getMotorPosTelemetry() {
        linearOpMode.telemetry.addData("frontLeft",frontLeft.getCurrentPosition()/CLICKS_PER_INCH);
        linearOpMode.telemetry.addData("frontRight", frontRight.getCurrentPosition() / CLICKS_PER_INCH);
        linearOpMode.telemetry.addData("backLeft", backLeft.getCurrentPosition() / CLICKS_PER_INCH);
        linearOpMode.telemetry.addData("backRight", backRight.getCurrentPosition() / CLICKS_PER_INCH);
    }

    public void getPowersTelemetry() {
        linearOpMode.telemetry.addData("FrontLeft Power", frontLeft.getPower());
        linearOpMode.telemetry.addData("FrontRight Power", frontRight.getPower());
        linearOpMode.telemetry.addData("BackLeft Power", backLeft.getPower());
        linearOpMode.telemetry.addData("BackRight Power", backRight.getPower());
    }

    //turn based of a given angle
    public void turn(int angle) {    //Turns in place positive power = right; Negative power = left.
        double power = .7;
        double turnSign = Math.signum(angle);
        double correctSign;
        final double THRESHOLD = 3; //3.25 is optimal
        double target = angle + gyro.getValueContinuous(); //need getValueContinuous in case start position is not 0
        //PIDCoefficients pidCoefficients = new PIDCoefficients(.4, .45, .7); //.5, .45, .7

        setDrivingModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setPower(power * turnSign);
        backLeft.setPower(power * turnSign);
        frontRight.setPower(-power * turnSign);
        backRight.setPower(-power * turnSign);

        while ((target - gyro.getValueContinuous()) > 25 && linearOpMode.opModeIsActive()) {
            linearOpMode.idle();
        }
        if (target - gyro.getValueContinuous() < (25)) {
            frontLeft.setPower(0.1 * turnSign);
            backLeft.setPower(0.1 * turnSign);
            frontRight.setPower(-0.1 * turnSign);
            backRight.setPower(-0.1 * turnSign);
        }
        sleep(100);

        while (Math.abs(gyro.getValueContinuous() - target) > THRESHOLD && linearOpMode.opModeIsActive()) {
            correctSign = Math.signum(target - gyro.getValueContinuous());
            power = .18; //.2 is optimal
            frontLeft.setPower(power * correctSign);
            backLeft.setPower(power * correctSign);
            frontRight.setPower(-power * correctSign);
            backRight.setPower(-power * correctSign);
            linearOpMode.idle();
        }
        stopDriving();
    }
    //Muneeb SSN: 034878341 -- what...

    //TODO: make these methods work!!!**************************************************************
    public void autoStrafe(double inches, double power) {
        final double THRESH = 1;
        double startAngle = gyro.getValueContinuous();
        int clicks = (int) (inches * CLICKS_PER_INCH);

        frontLeft.setTargetPosition(clicks);
        frontRight.setTargetPosition(-clicks);
        backLeft.setTargetPosition(-clicks);
        backRight.setTargetPosition(clicks);

        setDrivingModes(DcMotor.RunMode.RUN_TO_POSITION);

        final double POWER_CHANGE = power*0.2;
        while (linearOpMode.opModeIsActive() && frontRight.isBusy() && backRight.isBusy() && frontLeft.isBusy() && backLeft.isBusy()) {
            linearOpMode.idle();

            if (gyro.getValueContinuous() > startAngle + THRESH) {
                frontRight.setPower(power+POWER_CHANGE);
                frontLeft.setPower(power+POWER_CHANGE);
                backRight.setPower(power-POWER_CHANGE);
                backLeft.setPower(power-POWER_CHANGE);
            } else if (gyro.getValueContinuous() < startAngle - THRESH) {
                frontRight.setPower(power-POWER_CHANGE);
                frontLeft.setPower(power-POWER_CHANGE);
                backRight.setPower(power+POWER_CHANGE);
                backLeft.setPower(power+POWER_CHANGE);
            } else {
                setAllDrivingPowers(power);
            }
        }
        stopDriving();
    }

    public void frontRightStrafe(double inches, double power) {
        int clicks = (int) (inches * CLICKS_PER_INCH);

        stopDriving();

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight.setTargetPosition(clicks);
        backLeft.setTargetPosition(clicks);

        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setPower(power);
        backLeft.setPower(power);

        while (linearOpMode.opModeIsActive() && frontRight.isBusy() && backLeft.isBusy()) {
            linearOpMode.idle();

            frontRight.setPower(power);
            backLeft.setPower(power);
        }
        stopDriving();

        linearOpMode.sleep(100);
    }

    public void frontLeftStrafe(double inches, double power) {
        int clicks = (int) (inches * CLICKS_PER_INCH);

        stopDriving();

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setTargetPosition(clicks);
        backRight.setTargetPosition(clicks);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        backRight.setPower(power);

        while (linearOpMode.opModeIsActive() && frontLeft.isBusy() && backRight.isBusy()) {
            linearOpMode.idle();

            frontLeft.setPower(power);
            backRight.setPower(power);
        }
        stopDriving();

        linearOpMode.sleep(100);
    }

    //The most stupid method ever

}
