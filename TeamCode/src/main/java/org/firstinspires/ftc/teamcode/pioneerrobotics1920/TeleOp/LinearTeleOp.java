package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;

@TeleOp (name = "Linear TeleOp")
public class LinearTeleOp extends LinearOpMode {
    private Driving drive;
    private MoacV_2 moac;
    private Toggle.OneShot vertSlideOneShot;
    private Toggle.OneShot horizSlideOneShot;
    private Toggle.OneShot grabOneShot;
    private Toggle.OneShot modeOneShot;
    private Toggle.OneShot dpadOneShot;
    private Toggle.OneShot lifterOneShot;
    private Toggle.OneShot game2DpadUpOneShot;
    private Toggle.OneShot game2DpadDownOneShot;
    private Toggle.OneShot roleOneShot;
    public boolean blue = true;

    View relativeLayout;
    private boolean invert;
    int count;
    private boolean pushbot;

    private final double SCALE = 0.35;

    private final int[] LIFTER_PRESETS = {0, 700, 1450, 2200, 2950, 3700, 4450, 5200};


    double xVal;
    double yVal;


    /**
     * CONTROLS:
     * <p>
     * GAMEPAD1:
     * <p>
     * Start: Inverts direction of liberty drive movements
     * <p>
     * Y: Open Stacker Grabber
     * A: Preset for rest position of vertical slide
     * <p>
     * Left Trigger: Intake OUT
     * Right Trigger: Intake IN
     * <p>
     * Left Bumper: Slow mode for liberty drive movements
     * Right Bumper: Move Foundation Grabbers
     * <p>
     * DPad Up: Moves Vertical Slide up
     * DPad Down: Moves Vertical Slide down
     * <p>
     * DPad Left: Moves Horizontal Slide In
     * DPad Right: Moves Horizontal Slide Out
     * <p>
     * GamePad2: Nothing (yet)
     */

    @Override
    public void runOpMode() throws InterruptedException {

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });

        drive = new Driving(this);
        Navigation navigation = new Navigation(drive);
        moac = new MoacV_2(hardwareMap);
        vertSlideOneShot = new Toggle.OneShot();
        horizSlideOneShot = new Toggle.OneShot();
        grabOneShot = new Toggle.OneShot();
        modeOneShot = new Toggle.OneShot();
        roleOneShot = new Toggle.OneShot();
        dpadOneShot = new Toggle.OneShot();
        lifterOneShot = new Toggle.OneShot();
        game2DpadUpOneShot = new Toggle.OneShot();
        game2DpadDownOneShot = new Toggle.OneShot();
        invert = false;
        pushbot = false;
        int counter = 0;

        telemetry.addData("init finished", null);
        telemetry.update();

        navigation.currPos(0, 0, 0);

        waitForStart();

        while (this.opModeIsActive()){
            if (modeOneShot.update(gamepad1.start)) invert = !invert;
            if (roleOneShot.update(gamepad1.right_stick_button)) pushbot = !pushbot;

            if (invert) {
                if (gamepad1.left_bumper)
                    drive.libertyDrive(Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE), -Operations.powerScale(gamepad1.right_stick_x, SCALE + 0.25));
                else
                    drive.libertyDrive(Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.left_stick_x), -gamepad1.right_stick_x);
            } else {
                if (gamepad1.left_bumper)
                    drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.35, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE + 0.25));
                else
                    drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.3), gamepad1.left_stick_x);
            }

            if (gamepad1.dpad_up) moac.linearSlide.lifterPower(1); //max height 5100
            else if (gamepad1.dpad_down) moac.linearSlide.lifterPower(-0.6);
            else {
                if (vertSlideOneShot.update(gamepad1.a)) {
                    if (blue) {
                        moac.linearSlide.lifterPosition(LIFTER_PRESETS[counter]);
                        teleStrafeClose(true, false, xVal, yVal, 1, false);
                    }


                }
            }

            if (lifterOneShot.update(!(gamepad1.dpad_up || gamepad1.dpad_down)))
                if (moac.linearSlide.slideVertical.getCurrentPosition() > 500)
                    moac.linearSlide.lifterPower(.1);
                else
                    moac.linearSlide.lifterPower(0);

            if (gamepad2.right_bumper) {
                recordValues();
                if (count == 0) {
                    drive.resetGyro(this);
                    count++;
                }
            }

            if (gamepad1.dpad_right) moac.linearSlide.horizSlidePower(-.6);
            else if (gamepad1.dpad_left) moac.linearSlide.horizSlidePower(.6);
            else {
                if (horizSlideOneShot.update(gamepad1.b)) moac.linearSlide.horiz();
            }

            if (dpadOneShot.update(!(gamepad1.dpad_right || gamepad1.dpad_left)) || moac.linearSlide.slideVertical.getCurrentPosition() > 8000) {
                moac.linearSlide.lifterPower(0);
                moac.linearSlide.horizSlidePower(0);
            }

            if(gamepad1.left_trigger > .5) moac.intake.spitOut();
            else if (gamepad1.right_trigger > .5) {
                if (pushbot && moac.intake.getStoneState()) {
                    moac.intake.stopIntake();
                    relativeLayout.post(new Runnable() {
                        public void run() {
                            relativeLayout.setBackgroundColor(Color.GREEN);
                        }
                    });
                }
                else {
                    moac.intake.takeIn();
                    relativeLayout.post(new Runnable() {
                        public void run() {
                            relativeLayout.setBackgroundColor(Color.WHITE);
                        }
                    });
                }
            }
            else {
                moac.intake.stopIntake();
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.WHITE);
                    }
                });
            }

            if (gamepad1.back) moac.stacker.open();
            else if (gamepad1.right_trigger > .5) moac.stacker.open();
            else moac.stacker.close();



            if (gamepad1.x) {
                moac.stacker.open();
                int currentHeight = moac.linearSlide.slideVertical.getCurrentPosition();
                while (moac.linearSlide.slideVertical.getCurrentPosition() < currentHeight + 290)
                    moac.linearSlide.lifterPosition(moac.linearSlide.slideVertical.getCurrentPosition() + 300);
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > 100) {
                    moac.linearSlide.horizPosition(0);
                    if (moac.linearSlide.slideHoriz.getCurrentPosition() < 400)
                        moac.stacker.close();
                }
                moac.linearSlide.lifterPosition(0);

            }

            if (gamepad1.y)
                moac.linearSlide.lifterPosition(LIFTER_PRESETS[counter]);

            moac.foundationGrabber.grabFoundation(gamepad1.right_bumper);


            //gamepad22
            if (game2DpadUpOneShot.update(gamepad2.dpad_up) && counter < LIFTER_PRESETS.length - 1)
                counter++;
            else if (game2DpadDownOneShot.update(gamepad2.dpad_down) && counter > 0)
                counter--;

            if (gamepad2.a) {
                drive.resetGyro(this);
            }

            //drive.getMotorPosTelemetry();
            telemetry.addData("Role", (pushbot) ? "Pushbot" : "Stackbot");
            telemetry.addData("invert:", (invert)? "inverted":"not inverted");
            telemetry.addData("STONE LEVEL", "" + counter);
            telemetry.addData("Vertical slide clicks", moac.linearSlide.getPos(moac.linearSlide.slideVertical));
            telemetry.addData("Horizontal slide clicks", moac.linearSlide.getPos(moac.linearSlide.slideHoriz));
            telemetry.addData("xVal", xVal);
            telemetry.update();
        }
    }

    public void recordValues() {
        if (blue) {
            xVal = drive.getAccurateDistanceSensorReading(drive.rightDistance);
            yVal = 5;
        }
    }
    public void teleStrafeClose(boolean right, boolean front, double x, double y, float thresh, boolean stop) {
        int sgnX;
        int sgnY;

        ModernRoboticsI2cRangeSensor sensorX;
        ModernRoboticsI2cRangeSensor sensorY;

        if (right) {
            sensorX = drive.rightDistance; //right is positive direction
            sgnX = 1;
        } else {
            sensorX = drive.leftDistance; //left is negative direction
            sgnX = -1;
        }

        if (front) {
            sensorY = drive.frontDistance;
            sgnY = 1;
        } else {
            sensorY = drive.backDistance;
            sgnY = -1;
        }

        double angle0 = Operations.roundNearest90(drive.gyro.getValueContinuous());
        double angleDiff = angle0 - drive.gyro.getValueContinuous();
        double turnCorrectThresh = 5;

        double dx = drive.getAccurateDistanceSensorReading(sensorX) - x;
        double dy = drive.getAccurateDistanceSensorReading(sensorY) - y;

        double dxi = dx;
        double dyi = dy;

        double strafePower;
        double drivePower;
        double turnPower;

        double correctionPower = 0.1;

        while ((Math.abs(dx) > thresh || Math.abs(dy) > (thresh) || Math.abs(angleDiff) > turnCorrectThresh) && !gamepad1.left_stick_button) {
            angleDiff = angle0 - drive.gyro.getValueContinuous();
            if (Math.abs(dx) > thresh)
                strafePower = Range.clip(1.7 * dx * sgnX / Math.abs(dxi), -.7, .7);
            else strafePower = 0;
            if (Math.abs(dy) > thresh)
                drivePower = Range.clip(Math.pow(dy / Math.abs(dyi), 1.7) * sgnY, -.5, .5);
            else drivePower = 0;
            turnPower = (Math.abs(angleDiff) > turnCorrectThresh) ? angleDiff * correctionPower : 0;
            drive.libertyDrive((drivePower != 0) ? Operations.power(drivePower, .15, -1, 1) : 0, turnPower, (strafePower != 0) ? Operations.power(strafePower, .15, -1, 1) : 0);
            dx = drive.getAccurateDistanceSensorReading(sensorX) - x;
            dy = drive.getAccurateDistanceSensorReading(sensorY) - y;
            drive.linearOpMode.telemetry.addData("dx", dx);
            drive.linearOpMode.telemetry.addData("dy", dy);
            drive.linearOpMode.telemetry.addData("angleDiff", angleDiff);
            drive.linearOpMode.telemetry.update();
        }

        if (stop) drive.stopDriving();
    }


}
