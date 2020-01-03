package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class Driving {
    //instantiations
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor stacker;

    public GyroWrapper gyro;

    private final double CLICKS_PER_INCH = 30.6748466; // for Direct Drive as of 10/8/19 used the data for power = 0.4, R^2=1

    DcMotor[] drivingMotors;

    public ModernRoboticsI2cRangeSensor frontDistance, backDistance, leftDistance, rightDistance;

    private void initHardware(HardwareMap hardwareMap) {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        //stacker = hardwareMap.dcMotor.get("stacker");


        gyro = new GyroWrapper(hardwareMap.get(BNO055IMU.class, "imu"));
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        //stacker.setDirection((DcMotor.Direction.FORWARD));

        drivingMotors = new DcMotor[]{frontRight, frontLeft, backRight, backLeft};
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


/*
        frontDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontDistance");
        backDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "backDistance");
        leftDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "leftDistance");
        rightDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rightDistance");

        PIDCoefficients pidCoefficients = new PIDCoefficients(0.80651438729, 0.049988, 0);
        for (DcMotor motor : drivingMotors) {
            ((DcMotorEx) motor).setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidCoefficients);
        }
*/
    }

    //if distance sensors are going to be used
    public void initDistanceSensors(HardwareMap hardwareMap) {
        frontDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontDistance");
        backDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "backDistance");
        leftDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "leftDistance");
        rightDistance = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rightDistance");
    }

    public LinearOpMode linearOpMode;

    public Driving(OpMode opMode) {
        initHardware(opMode.hardwareMap);
    }

    public Driving(LinearOpMode opMode) {
        initHardware(opMode.hardwareMap);
        initDistanceSensors(opMode.hardwareMap);
        linearOpMode = opMode;
    }

    // The following methods are movement methods
    //brake method
    public void brake() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
    //turn based of a given angle
    public void turn(int angle) {    //Turns in place positive power = right; Negative power = left.
        double power = .7;
        double turnSign = Math.signum(angle);
        double correctSign;
        final double THRESHOLD = 3; //3.25 is optimal
        double target = angle + gyro.getValueContinuous(); //need getValueContinuous in case start position is not 0
        PIDCoefficients pidCoefficients = new PIDCoefficients(.4, .45, .7); //.5, .45, .7


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


    public int getEncoderPosition() {
        return frontRight.getCurrentPosition();
    }

    public int getFrontLeftPos() {
        return frontLeft.getCurrentPosition();
    }

    public int getFrontRightPos() {
        return frontRight.getCurrentPosition();
    }

    public int getBackLeftPos() {
        return backLeft.getCurrentPosition();
    }

    public int getBackRightPos() {
        return backRight.getCurrentPosition();
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

    public void setAllDrivingPowers(double power) {
        for (DcMotor motor : drivingMotors) {
            motor.setPower(power);
        }
    }

    //move forward method based on inches
    public void forward(double inches, double power) {
        int clicks = (int) (inches * CLICKS_PER_INCH);
        stopDriving();

        setAllDrivingPositions(clicks);
        setDrivingModes(DcMotor.RunMode.RUN_TO_POSITION);

        //wait while opmode is active and left motor is busy running to position.
        while (linearOpMode.opModeIsActive() && frontRight.isBusy() && backRight.isBusy() && frontLeft.isBusy() && backLeft.isBusy()) {
            linearOpMode.idle();
            double factor = Math.abs(clicks - frontLeft.getCurrentPosition()) / 1800;
            double newPower = power * factor * factor;
            setAllDrivingPowers((newPower<0.25)? 0.25:newPower);
        }
        stopDriving();
    }

    //main drive method
    public void libertyDrive(double drive, double turn, double strafe) {
        setDrivingModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double factor = Math.abs(drive) + Math.abs(turn) + Math.abs(strafe);
        if (factor <= 1)
            factor = 1;
        backLeft.setPower((drive - strafe + turn) / factor);
        frontLeft.setPower((drive + strafe + turn) / factor);
        backRight.setPower((drive + strafe - turn) / factor);
        frontRight.setPower((drive - strafe - turn) / factor);
    }

    public void straightStrafe(double strafe){
        libertyDrive(0,strafe*0.1, strafe);
    }

    public void stopDriving() {
        setDrivingModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setAllDrivingPowers(0);
        setDrivingModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearOpMode.idle();
    }

 /*   public void stackerUp(int power){
        stacker.setPower(power);

        stacker.setPower(0);
    }*/

    public void angleCorrection(double target) {
        double power = .2;
        double turnSign = Math.signum(target - gyro.getValueContinuous());
        double correctSign;
        final double THRESHOLD = 3.25; //3.25 is optimal
        //double target = target + gyro.getValueContinuous(); //need getValueContinuous in case start position is not 0

        frontLeft.setPower(power * turnSign);
        backLeft.setPower(power * turnSign);
        frontRight.setPower(-power * turnSign);
        backRight.setPower(-power * turnSign);

        while (Math.abs(gyro.getValueContinuous() - target) > THRESHOLD && linearOpMode.opModeIsActive()) {
            correctSign = Math.signum(target - gyro.getValueContinuous());
            frontLeft.setPower(power * correctSign);
            backLeft.setPower(power * correctSign);
            frontRight.setPower(-power * correctSign);
            backRight.setPower(-power * correctSign);
            linearOpMode.idle();
        }
    }

    public boolean motorsBusy() {
        boolean busy = true;
        for (DcMotor motor : drivingMotors) {
            busy = motor.isBusy();
        }
        return busy;
    }

     final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Position getPosition() {
        return gyro.reportPosition();
    }

    public void PIDTurn(double Kp, double Ki, double Kd, int angle) {
        stopDriving();
        double target = angle + gyro.getValueContinuous();
        //error needs to be scaled
        double control;
        double error;
        double scaled_error;
        double summed_error = 0;
        double previous_error = 0;
        double delta_error;
        linearOpMode.telemetry.addData("not in the loop", null);
        while (linearOpMode.opModeIsActive() && Math.abs(target - gyro.getValueContinuous()) > 0.5) {
            linearOpMode.telemetry.addData("in the loop", null);
            //setting up Kp
            error = target - gyro.getValueContinuous();
            scaled_error = error / angle;
            //if(scaled_error > 1)scaled_error = 1;
            //if(scaled_error < -1)scaled_error = -1;
            //setting up Ki
            summed_error = summed_error + scaled_error;
            if (summed_error > 1) summed_error = 1;
            if (summed_error < -1) summed_error = -1;

            //setting up Kd
            delta_error = (previous_error - error);
            if (delta_error > 1) delta_error = 1;
            if (delta_error < -1) delta_error = -1;

            control = ((Kp * scaled_error) + (Ki * summed_error) + (Kd * delta_error));
            // make sure control is scaled
            if (control > 1) control = 1;
            if (control < -1) control = -1;

            frontLeft.setPower(control);
            backLeft.setPower(control);
            frontRight.setPower(-control);
            backRight.setPower(-control);

            previous_error = error;
        }
        linearOpMode.telemetry.addData("exit the loop", null);
        stopDriving();
        linearOpMode.sleep(1000);
    }

     int sgn(double x) {
        if (x < 0) return -1;
        return 1;
    }

    public void forwardMotor(DcMotor motor, int clicks, double power) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(clicks);

        while (linearOpMode.opModeIsActive() && motor.isBusy()) {
            linearOpMode.idle();
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(power);
        }
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (motor.getCurrentPosition() > 999 || motor.getCurrentPosition() < -999)
            motor.setPower(0);
    }

    public void getPowers() {
        linearOpMode.telemetry.addData("FrontLeft Power", frontLeft.getPower());
        linearOpMode.telemetry.addData("FrontRight Power", frontRight.getPower());
        linearOpMode.telemetry.addData("BackLeft Power", backLeft.getPower());
        linearOpMode.telemetry.addData("BackRight Power", backRight.getPower());
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
    //48 inches from wall to stones

//    public

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

    public void getPos() {
        linearOpMode.telemetry.addData("frontLeft",frontLeft.getCurrentPosition()/CLICKS_PER_INCH);
        linearOpMode.telemetry.addData("frontRight", frontRight.getCurrentPosition() / CLICKS_PER_INCH);
        linearOpMode.telemetry.addData("backLeft", backLeft.getCurrentPosition() / CLICKS_PER_INCH);
        linearOpMode.telemetry.addData("backRight", backRight.getCurrentPosition() / CLICKS_PER_INCH);
    }

    public int sweepCheck(Coordinates coordinates) { //For smarter parking
        //TODO: Find angle to start at. Find turn increment (should roughly be length of 22 in)
        final int START_ANGLE = 30;
        final int BRIDGE_Y = 99;
        final int VERTICAL_DIST = (int) coordinates.y - BRIDGE_Y; //should be around 30-40 in away
        final int MARGIN_OF_ERROR = 4;
        turn(START_ANGLE);
        int turnCounts = 0;

        while (turnCounts < 4) { // there was an && here
            int a = (int) frontDistance.getDistance(DistanceUnit.INCH);
            turn((int) gyro.getValueContinuous() + 30); //width of robot is approx 22 inches, counting mechanum wheels
            if (a > VERTICAL_DIST / MARGIN_OF_ERROR + Math.sin(gyro.getValueContinuous()) && turnCounts % 2 == 0) {
                turn((int) gyro.getValueContinuous() - 15);
                turnCounts++;
            }
        }
        return 1;
    }

     void moveClose(String direction, double distance, double power, float thresh){
        if (direction.equals("back")) {
            double diff = backDistance.getDistance(DistanceUnit.INCH) - distance;
            while(Math.abs(backDistance.getDistance(DistanceUnit.INCH) - distance) > thresh) {
                if (diff>=0)
                    libertyDrive(-power, 0, 0);
                else
                    libertyDrive(power, 0, 0);
            }
        }
        if (direction.equals("front")) {
            double diff = frontDistance.getDistance(DistanceUnit.INCH) - distance;
            while(Math.abs(frontDistance.getDistance(DistanceUnit.INCH) - distance) > thresh) {
                if (diff >= 0)
                    libertyDrive(power, 0, 0);
                else
                    libertyDrive(-power, 0, 0);
            }
        }
        if (direction.equals("left")) {
            double diff = leftDistance.getDistance(DistanceUnit.INCH) - distance;
            while(Math.abs(leftDistance.getDistance(DistanceUnit.INCH) - distance) > thresh) {
                if (diff >= 0)
                    libertyDrive(0, 0, power);
                else
                    libertyDrive(0, 0, -power);
            }
        }
        if (direction.equals("right")) {
            double diff = rightDistance.getDistance(DistanceUnit.INCH) - distance;
            while(Math.abs(rightDistance.getDistance(DistanceUnit.INCH) - distance) > thresh) {
                if (diff >= 0)
                    libertyDrive(0, 0, -power);
                else
                    libertyDrive(0, 0, power);
            }
        }
        stopDriving();
    }
}
