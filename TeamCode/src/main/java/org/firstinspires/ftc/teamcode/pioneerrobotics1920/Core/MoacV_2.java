package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;

public class MoacV_2 {
    //TODO: Add these back once hardware map is complete
    //public AutoGrab autoGrab;
    public LinearSlide linearSlide;
    public FoundationGrabber foundationGrabber;
//    public Stacker stacker;
    public Intake intake;
    public Driving drive;
    public Stacker stacker;

    public MoacV_2(Boolean blue, HardwareMap hardwareMap) { //Autonomous Constructor

        foundationGrabber = new FoundationGrabber(hardwareMap);
    }

    public MoacV_2(HardwareMap hardwareMap) { //TeleOp Constructor
        intake = new Intake(hardwareMap);
        linearSlide = new LinearSlide(hardwareMap);
        foundationGrabber = new FoundationGrabber(hardwareMap);
        stacker = new Stacker(hardwareMap);
    }

    public class LinearSlide {
        DcMotor slideVertical;
        DcMotor slideHoriz;
        public int[] horizPositions = {50,100,200,300,400};//TODO: Figure out positions for x stones stacked
        public int[] verticalPositions = {50,100,200,300,400};

       public LinearSlide(HardwareMap hardwareMap) {
            slideVertical = hardwareMap.dcMotor.get("slideVertical");
            slideVertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slideVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideVertical.setDirection(DcMotorSimple.Direction.REVERSE);
            lifterPosition(0);
            slideHoriz = hardwareMap.dcMotor.get("slideHoriz");
            slideHoriz.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slideVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            horizPosition(0);
        }

        public void lifterPower(double power) {
           slideVertical.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
           slideVertical.setPower(power);
        }

        public void lifterPosition(int clicks) {
           slideVertical.setTargetPosition(clicks);
           slideVertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           slideVertical.setPower(.3);
        }

        public void horizSlidePower(double power){
           slideHoriz.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
           slideHoriz.setPower(power);
        }

        public void horizPosition(int clicks) {
            slideHoriz.setTargetPosition(clicks);
            slideHoriz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideHoriz.setPower(.3);
        }

        public void setHorizPosition(int count) {
            slideVertical.setTargetPosition(verticalPositions[count]);
        }
        public void setVerticalPosition(int count) {
            slideHoriz.setTargetPosition(horizPositions[count]);
        }
    }

   public class FoundationGrabber {
        public Servo leftFoundationGrabber, rightFoundationGrabber;

        public FoundationGrabber(HardwareMap hardwareMap) {
            leftFoundationGrabber = hardwareMap.servo.get("leftFoundationGrabber"); //from the back pov, looking from front to back
            rightFoundationGrabber = hardwareMap.servo.get("rightFoundationGrabber");
            leftFoundationGrabber.setPosition(.4);//previous: .567
            rightFoundationGrabber.setPosition(0);//previous: .46
        }

        public void grabFoundation(boolean lift) {
            leftFoundationGrabber.setPosition((lift) ? .045 : .4);//locked in: 0, open:.4
            rightFoundationGrabber.setPosition((lift) ? .46 : 0); //locked in:.34 , open:0
        }

    }

    public class Stacker {

        Servo grabber, rotate;
        boolean grabberSwitcher = true;
        boolean rotateSwitcher = true;

        public Stacker(HardwareMap hardwareMap) {
            grabber = hardwareMap.servo.get("stacker");
            rotate = hardwareMap.servo.get("rotate");
            //initialize();
        }

        public void initialize() {
            grabber.setPosition(.7);
            rotate.setPosition(1);
        }

        public void grab(){
            if (grabberSwitcher) grabber.setPosition(0.7);
            else grabber.setPosition(0.23);
            grabberSwitcher = (grabberSwitcher)? false:true;
        }

        public void rotate(){
            if (rotateSwitcher) rotate.setPosition(0.05);
            else rotate.setPosition(0.95);
            rotateSwitcher = (rotateSwitcher)? false:true;
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
            rightIntake.setPower(-1); //set powers to 1 if everything goes fine
        }

        public void spitOut() {
            leftIntake.setPower(-1);
            rightIntake.setPower(1);
        }

        public void stopIntake() {
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }
        public void takeStone(Driving driving, Navigation nav, LinearOpMode opMode) { //experimental method for autonomous grabbing stone
            int vert = 12;
            takeIn();
            nav.moveToX(nav.getX() + vert,0.15);
//            opMode.sleep(500);
            //linearSlide.setVerticalPosition(50);
            stopIntake();
            nav.backToX(nav.getX() - vert + 2);
        }

    }

}
