package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;

public class MoacV_2 {
    public AutoGrab autoGrab;
    public LinearSlideConfig linearSlideConfig;
    public FoundationGrabber foundationGrabber;
    public TeleGrabber teleGrabber;
    public Intake intake;

    public MoacV_2(Boolean blue, HardwareMap hardwareMap) { //Autonomous Constructor
        autoGrab = new AutoGrab(blue, hardwareMap);
        foundationGrabber = new FoundationGrabber(hardwareMap);
    }

    public MoacV_2(HardwareMap hardwareMap) { //TeleOp Constructor
        intake = new Intake(hardwareMap);
        linearSlideConfig = new LinearSlideConfig(hardwareMap);
        foundationGrabber = new FoundationGrabber(hardwareMap);
        teleGrabber = new TeleGrabber(hardwareMap);
        autoGrab = new AutoGrab(true, hardwareMap);//temporary
    }


    public class AutoGrab {

        private Servo pivot, innerGrabber, outerGrabber;
        boolean switcher = true; //use for temporary teleop
        double[][] pivotPos = {{0, 0.7978, 0.3867}, {0.796, 0, 0.419}};
        // first array is the blue grabber (right grabber)
        //      Initial, Out, Retracted position
        // second array is the red grabber (left grabber)

        double[][] outerPos = {{0.8067, 0.275}, {0.126, 0.704}};
        //first array: Open, Closed
        final double INNERGRABBER_POSITION = 0.406;

        public AutoGrab(Boolean blue, HardwareMap hardwareMap) {
            //if (blue) {
            pivot = hardwareMap.servo.get("bluePivot");
            innerGrabber = hardwareMap.servo.get("blueInnerGrabber");
            outerGrabber = hardwareMap.servo.get("blueOuterGrabber");
            blueInitialize();
        /*else {
            pivot = hardwareMap.servo.get("redPivot");
            innerGrabber = hardwareMap.servo.get("redInnerGrabber");
            outerGrabber = hardwareMap.servo.get("redOuterGrabber");
            redInitialize();
        }*/
            innerGrabber.setPosition(INNERGRABBER_POSITION);
        }

        public void blueInitialize() {
            pivot.setPosition(pivotPos[0][0]);
            outerGrabber.setPosition(outerPos[0][0]);
        }

        public void blueArmDown() {
            pivot.setPosition(pivotPos[0][1]);
        }

        public void blueClose() {
            outerGrabber.setPosition(outerPos[0][1]);
        }

        public void blueOpen() {
            pivot.setPosition(0.5);
            outerGrabber.setPosition(outerPos[0][0]);
        }

        public void blueLift() {
            pivot.setPosition(pivotPos[0][2]);
        }

        public void redInitialize() {
            pivot.setPosition(pivotPos[1][0]);
            outerGrabber.setPosition(outerPos[1][0]);
        }

        public void redGrab() {
            pivot.setPosition(pivotPos[1][1]);
            outerGrabber.setPosition(outerPos[1][1]);
            pivot.setPosition(pivotPos[1][2]);
        }

        //temporary teleOp
        public void teleBlueGrab() {
            outerGrabber.setPosition(outerPos[0][1]);
        }

        public void teleBlueRelease() {
            outerGrabber.setPosition(outerPos[0][0]);
        }

        public void teleBlueGrab(Boolean grab) {
            outerGrabber.setPosition((grab) ? outerPos[0][1] : outerPos[0][0]);
        }

        public void teleBlueArmDown(float stick) {
            if (stick < -.5)
                pivot.setPosition(pivotPos[0][2]);
            else if (stick > .5)
                pivot.setPosition((pivotPos[0][1]));
        }
    }

    public class LinearSlideConfig {
        DcMotor linearSlide;

        public LinearSlideConfig(HardwareMap hardwareMap) {
            linearSlide = hardwareMap.dcMotor.get("linearSlide");
            linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linearSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        public void lifterPower(double power) {
            linearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            linearSlide.setPower(power);
        }

        public void lifterPosition(int clicks) {
            linearSlide.setTargetPosition(clicks);
            linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlide.setPower(.3);
        }
    }

    public class FoundationGrabber {
        public Servo leftFoundationGrabber, rightFoundationGrabber;

        public FoundationGrabber(HardwareMap hardwareMap) {
            leftFoundationGrabber = hardwareMap.servo.get("leftFoundationGrabber");
            rightFoundationGrabber = hardwareMap.servo.get("rightFoundationGrabber");
        }

        public void leftGrab(boolean lift) {
            leftFoundationGrabber.setPosition((lift) ? .95 : .45);
        }

        public void rightGrab(boolean lift) {
            rightFoundationGrabber.setPosition((lift) ? .08 : 0.567);
        }
    }

    public class TeleGrabber {

        Servo telePivot, teleInnerGrabber, teleOuterGrabber;

        boolean switcher = true;

        public TeleGrabber(HardwareMap hardwareMap) {
            telePivot = hardwareMap.servo.get("telePivot");
            teleInnerGrabber = hardwareMap.servo.get("teleInnerGrabber");
            teleOuterGrabber = hardwareMap.servo.get("teleOuterGrabber");
            initialize();
        }

        public void initialize() {
            telePivot.setPosition(0.393); //pivot up
            teleInnerGrabber.setPosition(0.556); //inner 0 degree
            teleOuterGrabber.setPosition(0.521); //outer 0 degree
        }

        public void setTelePivot(float stickPos) {
            if (stickPos < -0.5) {
                telePivot.setPosition(0.393); //pivot up
            } else if (stickPos > 0.5) {
                telePivot.setPosition(0.848); //pivot down
                teleInnerGrabber.setPosition(0.0439); //inner grabber 90 degree
            }
        }

        public void grab(boolean grab) {
            teleOuterGrabber.setPosition((grab) ? 0 : 0.521);
        }

        public void grab() {
            if (switcher) teleOuterGrabber.setPosition(0);//outer 90 degree
            else teleOuterGrabber.setPosition(0.521);//outer 0 degree
            switcher = !switcher;
        }

        public void release() {
            teleOuterGrabber.setPosition(0.521); //outer 0 degree
        }
    }

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
            leftIntake.setPower(.75);
            rightIntake.setPower(-.75); //set powers to 1 if everything goes fine
        }

        public void spitOut() {
            leftIntake.setPower(-.75);
            rightIntake.setPower(.75);
        }

        public void stopIntake() {
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }
        public void takeStone(Driving driving, Navigation nav) {
            takeIn();
            nav.moveTo(nav.getX() + 5,nav.getY() + 5);
            stopIntake();
        }
    }

}
