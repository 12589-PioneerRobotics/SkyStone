package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MoacV_2 {
    //TODO: Add these back once hardware map is complete

    //public AutoGrab autoGrab;
    public LinearSlide linearSlide;
    public FoundationGrabber foundationGrabber;
    //public Stacker stacker;
    public Intake intake;
    public Driving drive;
    public Stacker stacker;

    public MoacV_2(Boolean blue, HardwareMap hardwareMap) { //Autonomous Constructor
        //linearSlide = new LinearSlide(hardwareMap);
        foundationGrabber = new FoundationGrabber(hardwareMap);
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

        public LinearSlide(HardwareMap hardwareMap) {
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
        public double getPos(DcMotor noYou){
            return noYou.getCurrentPosition();
        }

        public void drop(){
            lifterPosition(500);
            horizPosition(2000);
            stacker.open();
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

        public void horizSlidePower(double power){
           slideHoriz.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
           slideHoriz.setPower(power);
        }

        public void horizPosition(int clicks) {
            slideHoriz.setTargetPosition(clicks);
            slideHoriz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideHoriz.setPower(1);
        }

        public void horiz() {
            slideHoriz.setTargetPosition((horizSwitcher) ? 2000: 0);
            slideHoriz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideHoriz.setPower(1);
            horizSwitcher = !horizSwitcher;
        }
/*
        public void setHorizPosition(int count) {
            slideVertical.setTargetPosition(verticalPositions[count]);
        }
        public void setVerticalPosition(int count) {
            slideHoriz.setTargetPosition(horizPositions[count]);
        }
*/
    }

    public class FoundationGrabber {
        public Servo leftFoundationGrabber, rightFoundationGrabber;

        public FoundationGrabber(HardwareMap hardwareMap) {
            leftFoundationGrabber = hardwareMap.servo.get("leftFoundationGrabber"); //from the back pov, looking from front to back
            rightFoundationGrabber = hardwareMap.servo.get("rightFoundationGrabber");
            leftFoundationGrabber.setPosition(0);//previous: .567
            rightFoundationGrabber.setPosition(1);//previous: .46
        }

        public void grabFoundation(boolean grab) {
            leftFoundationGrabber.setPosition((grab) ? .1 : .9);//locked in: 0, open:.4
            rightFoundationGrabber.setPosition((grab) ? .9 : .1); //locked in:.34 , open:0
        }
    }

    public class Stacker {

        Servo grabber;
        boolean grabberSwitcher = false;


        public Stacker(HardwareMap hardwareMap) {
            grabber = hardwareMap.servo.get("grabber");
            grabber.setPosition(0.22);
        }

        public void grab(){
            if (grabberSwitcher) grabber.setPosition(0.22);
            else grabber.setPosition(0.05);
            grabberSwitcher = !grabberSwitcher;
        }

        public void open(){
            grabber.setPosition(0.06);
        }

        public void close(){
            grabber.setPosition(0.22);
        }
    }

    public class Intake {
        public DcMotor leftIntake;
        public DcMotor rightIntake;

        public Intake(HardwareMap hardwareMap) {
            leftIntake = hardwareMap.dcMotor.get("leftIntake");
            rightIntake = hardwareMap.dcMotor.get("rightIntake");

            leftIntake.setDirection(DcMotorSimple.Direction.REVERSE);
            rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);

            leftIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        public void takeIn() {
            leftIntake.setPower(1);
            rightIntake.setPower(-1);
            stacker.open();
        }

        public void spitOut() {
            leftIntake.setPower(-1);
            rightIntake.setPower(1);

        }

        public void stopIntake() {
            leftIntake.setPower(0);
            rightIntake.setPower(0);
            stacker.close();
        }
    }
}
