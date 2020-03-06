package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests.ColorSensorCalibration;

import java.util.ArrayList;

public class MoacV_2 {
    //TODO: Add these back once hardware map is complete

    //public AutoGrab autoGrab;
    public LinearSlide linearSlide;
    public FoundationGrabber foundationGrabber;
    //public Stacker stacker;
    public Intake intake;
    public Stacker stacker;

    //public IntakeSensor intakeSensor;

    public MoacV_2(Boolean blue, HardwareMap hardwareMap) { //Autonomous Constructor
        //linearSlide = new LinearSlide(hardwareMap);
        foundationGrabber = new FoundationGrabber(hardwareMap);

    }

    public MoacV_2(HardwareMap hardwareMap, boolean blue) { //TeleOp Constructor
        intake = new Intake(hardwareMap);
        linearSlide = new LinearSlide(hardwareMap);
        foundationGrabber = new FoundationGrabber(hardwareMap);
        stacker = new Stacker(hardwareMap);

        //intakeSensor = new IntakeSensor(hardwareMap, blue);

    }
    public MoacV_2(HardwareMap hardwareMap) { //TeleOp Constructor
        intake = new Intake(hardwareMap);
        linearSlide = new LinearSlide(hardwareMap);
        foundationGrabber = new FoundationGrabber(hardwareMap);
        stacker = new Stacker(hardwareMap);

    }

    public class LinearSlide {
        public DcMotor slideVertical;
        public DcMotor slideHoriz;
        private boolean horizSwitcher;
        public final double HORIZ_OPEN = 1150;

        LinearSlide(HardwareMap hardwareMap) {
            slideVertical = hardwareMap.dcMotor.get("slideVertical");
            slideVertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slideVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lifterPosition(0);
            slideHoriz = hardwareMap.dcMotor.get("slideHorizontal");
            slideHoriz.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slideHoriz.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            horizPosition(0);
            horizSwitcher = true;
        }

        public double getPos(DcMotor noYou) {
            return noYou.getCurrentPosition();
        }


        public void lifterPower(double power) {
            slideVertical.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slideVertical.setPower(power);
        }

        public void lifterPosition(int clicks) {
            slideVertical.setTargetPosition(clicks);
            slideVertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideVertical.setPower(1);
        }

        public void horizSlidePower(double power) {
            slideHoriz.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slideHoriz.setPower(power);
        }

        public void horizPosition(int clicks) {
            slideHoriz.setTargetPosition(clicks);
            slideHoriz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideHoriz.setPower(1);
        }

        public void horiz() {
            slideHoriz.setTargetPosition((horizSwitcher) ? 1150 : 0);
            slideHoriz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideHoriz.setPower(1);
            horizSwitcher = !horizSwitcher;
        }

    }

    public class FoundationGrabber {
        Servo leftFoundationGrabber, rightFoundationGrabber; // open 0.77, close 0.21 2/3/2020

        FoundationGrabber(HardwareMap hardwareMap) {
            leftFoundationGrabber = hardwareMap.servo.get("leftFoundationGrabber"); //from the back pov, looking from front to back
            rightFoundationGrabber = hardwareMap.servo.get("rightFoundationGrabber");
            leftFoundationGrabber.setPosition(.05);
            rightFoundationGrabber.setPosition(.95);
        }

        public void grabFoundation(boolean grab) {
            leftFoundationGrabber.setPosition((grab) ? .55 : .05); //vals .21 .77
            rightFoundationGrabber.setPosition((grab) ? .35 : .95);
        }
    }

    public class Stacker {
        Servo grabber;
        boolean grabberSwitcher = false;
        final private double OPEN_POS = .86; //used to be .89
        final private double CLOSE_POS = .97; //used to be 1

        Stacker(HardwareMap hardwareMap) {
            grabber = hardwareMap.servo.get("grabber");
            grabber.setPosition(CLOSE_POS);
        }

        public void grab() {
            if (grabberSwitcher) grabber.setPosition(OPEN_POS);
            else grabber.setPosition(CLOSE_POS);
            grabberSwitcher = !grabberSwitcher;
        }

        public void open() {
            grabber.setPosition(OPEN_POS);
        }

        public void close() {
            grabber.setPosition(CLOSE_POS);
        }
    }

    public class Intake {
        DcMotor leftIntake;
        DcMotor rightIntake;
        public ColorSensor brickSensor;

        int red;
        int green;
        int blue;

        boolean hasStone;

        final double THRESH = 20;

        ArrayList<Double> differences = new ArrayList<>();

        Intake(HardwareMap hardwareMap) {
            leftIntake = hardwareMap.dcMotor.get("leftIntake");
            rightIntake = hardwareMap.dcMotor.get("rightIntake");
            brickSensor = hardwareMap.get(ColorSensor.class, "brickSensor");
            leftIntake.setDirection(DcMotorSimple.Direction.REVERSE);
            rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);

            leftIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            red = brickSensor.red();
            green = brickSensor.green();
            blue = brickSensor.blue();

            hasStone = false;
        }

        public void takeIn() {
            leftIntake.setPower(1);
            rightIntake.setPower(-1);
        }

        public boolean getStoneState() {
            if (brickSensor.alpha() < 10 || brickSensor.blue() < 2)
                return true;
            else
                return false;
        }

        public boolean detectStone() {
            ColorSensorCalibration calibration = new ColorSensorCalibration();
            /*double difference = Math.sqrt(Math.pow(brickSensor.red() - red, 2) + Math.pow(brickSensor.green() - green, 2) + Math.pow(brickSensor.blue() - blue, 2));
            if (difference > THRESH)
                hasStone = !hasStone;
            this.red = brickSensor.red();
            this.green = brickSensor.green();
            this.blue = brickSensor.blue();
            differences.add(difference);
            return hasStone;*/
            return calibration.stoneIsIn();

        }


        public void spitOut() {
            leftIntake.setPower(-1);
            rightIntake.setPower(1);

        }

        public void stopIntake() {
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }
    }

    /*public class IntakeSensor {
        public ColorSensor stoneSensor;
        public RevBlinkinLedDriver lights;

        public IntakeSensor(HardwareMap hardwareMap, boolean blue) {
            stoneSensor = hardwareMap.colorSensor.get("stoneSensor");
//            lights = hardwareMap.get(RevBlinkinLedDriver.class, "lights");
//            lights.setPattern(blue ? RevBlinkinLedDriver.BlinkinPattern.BLUE : RevBlinkinLedDriver.BlinkinPattern.RED);
        }

        public boolean stoneIn() {
            return stoneSensor.blue() < 200; //abritrary
        }
    }*/
}
