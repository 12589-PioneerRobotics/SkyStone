package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

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
//    public FoundationGrabber foundationGrabber;
//    public Stacker teleGrabber;
    public Intake intake;

    public MoacV_2(Boolean blue, HardwareMap hardwareMap) { //Autonomous Constructor
        //autoGrab = new AutoGrab(blue, hardwareMap);
        //foundationGrabber = new FoundationGrabber(hardwareMap);
    }

    public MoacV_2(HardwareMap hardwareMap) { //TeleOp Constructor
        intake = new Intake(hardwareMap);
        linearSlide = new LinearSlide(hardwareMap);
//        foundationGrabber = new FoundationGrabber(hardwareMap);
//        teleGrabber = new Stacker(hardwareMap);
        //autoGrab = new AutoGrab(true, hardwareMap);//temporary
    }

    public class LinearSlide {
        DcMotor slideVertical;
        DcMotor slideHoriz;
        int[] horizPositions = {50,100,200,300,400};//TODO: Figure out positions for x stones stacked
        public int[] verticalPositions = {50,100,200,300,400};

       public LinearSlide(HardwareMap hardwareMap) {
            slideVertical = hardwareMap.dcMotor.get("slideVertical");
            slideVertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slideVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideVertical.setDirection(DcMotorSimple.Direction.REVERSE);
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
        }

        public void grab(boolean lift) {
            leftFoundationGrabber.setPosition((lift) ? .95 : .45);
            rightFoundationGrabber.setPosition((lift) ? .08 : 0.567);
        }
    }

    /*public class Stacker {

        Servo grabber, rotate;
        boolean switcher = true;

        public Stacker(HardwareMap hardwareMap) {
            grabber = hardwareMap.servo.get("grabber");
            rotate = hardwareMap.servo.get("rotate");
            initialize();
        }

        public void initialize() {
            grabber.setPosition();
            rotate.setPosition();
        }

        *//*public void setTelePivot(float stickPos) {
            if (stickPos < -0.5) {
                telePivot.setPosition(0.393); //pivot up
            } else if (stickPos > 0.5) {
                telePivot.setPosition(0.848); //pivot down
                teleInnerGrabber.setPosition(0.0439); //inner grabber 90 degree
            }
        }*//*

        public void grab(boolean grab) {
            grabber.setPosition((grab) ? 0 : 0.521);
        }

        public void grab() {
            if (switcher) teleOuterGrabber.setPosition(0);//outer 90 degree
            else teleOuterGrabber.setPosition(0.521);//outer 0 degree
            switcher = !switcher;
        }

        public void release() {
            grabber.setPosition(0.521); //outer 0 degree
        }
    }*/

    public class Intake {
        public DcMotor leftIntake;
        public DcMotor rightIntake;

        public Intake(HardwareMap hardwareMap) {
            leftIntake = hardwareMap.dcMotor.get("leftIntake");
            rightIntake = hardwareMap.dcMotor.get("rightIntake");

            leftIntake.setDirection(DcMotorSimple.Direction.FORWARD);
            rightIntake.setDirection(DcMotorSimple.Direction.FORWARD);

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
        public void takeStone(Driving driving, Navigation nav) { //experimental method for autonomous grabbing stone
            takeIn();
            nav.moveTo(nav.getX() + 5,nav.getY() + 5);
            stopIntake();
        }

    }

}
