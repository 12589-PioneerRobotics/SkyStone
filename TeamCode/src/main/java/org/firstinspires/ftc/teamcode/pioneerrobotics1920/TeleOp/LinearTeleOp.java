package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
    public boolean blue = true;

    private boolean invert;

    private final double SCALE = 0.35;

    private final int[] LIFTER_PRESETS = {0, 600, 1350, 2100, 2850, 3600, 4350};

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
        drive = new Driving(this);
        Navigation navigation = new Navigation(drive);
        moac = new MoacV_2(hardwareMap);
        vertSlideOneShot = new Toggle.OneShot();
        horizSlideOneShot = new Toggle.OneShot();
        grabOneShot = new Toggle.OneShot();
        modeOneShot = new Toggle.OneShot();
        dpadOneShot = new Toggle.OneShot();
        lifterOneShot = new Toggle.OneShot();
        game2DpadUpOneShot = new Toggle.OneShot();
        game2DpadDownOneShot = new Toggle.OneShot();
        invert = false;
        int counter = 0;

        telemetry.addData("init finished", null);
        telemetry.update();

        navigation.currPos(0, 0, 0);

        waitForStart();

        while (this.opModeIsActive()){
            if (modeOneShot.update(gamepad1.start)) invert = !invert;

            if (invert) {
                if (gamepad1.left_bumper)
                    drive.libertyDrive(Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE), -Operations.powerScale(gamepad1.right_stick_x, SCALE + 0.25));
                else
                    drive.libertyDrive(Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.left_stick_x), -gamepad1.right_stick_x);
            } else {
                if (gamepad1.left_bumper)
                    drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.right_stick_x, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE + 0.25));
                else
                    drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.right_stick_x), gamepad1.left_stick_x);
            }

            if (gamepad1.dpad_up) moac.linearSlide.lifterPower(1); //max height 5100
            else if (gamepad1.dpad_down) moac.linearSlide.lifterPower(-0.6);
            else {
                if (vertSlideOneShot.update(gamepad1.a)) moac.linearSlide.lifterPosition(0);
            }

            if (lifterOneShot.update(!(gamepad1.dpad_up || gamepad1.dpad_down)))
                moac.linearSlide.lifterPower(.1);

            if (gamepad1.dpad_right) moac.linearSlide.horizSlidePower(-.6);
            else if (gamepad1.dpad_left) moac.linearSlide.horizSlidePower(.6);
            else {
                if (horizSlideOneShot.update(gamepad1.b)) moac.linearSlide.horiz();
            }

            if (dpadOneShot.update(!(gamepad1.dpad_right || gamepad1.dpad_left)) || moac.linearSlide.slideVertical.getCurrentPosition()>5100) moac.linearSlide.horizSlidePower(0);

            if(gamepad1.left_trigger > .5) moac.intake.spitOut();
            else if(gamepad1.right_trigger > .5) moac.intake.takeIn();
            else moac.intake.stopIntake();

            if (gamepad1.back) moac.stacker.open();
            else if (gamepad1.right_trigger > .5) moac.stacker.open();
            else moac.stacker.close();

            if (gamepad1.x) {
                moac.stacker.open();
                int currentHeight = moac.linearSlide.slideVertical.getCurrentPosition();
                while (moac.linearSlide.slideVertical.getCurrentPosition() < currentHeight + 290)
                    moac.linearSlide.lifterPosition(moac.linearSlide.slideVertical.getCurrentPosition() + 300);
                while (moac.linearSlide.slideHoriz.getCurrentPosition() < -100) {
                    moac.linearSlide.horizPosition(0);
                    if (moac.linearSlide.slideHoriz.getCurrentPosition() > -1200)
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
            if (gamepad2.right_bumper)
                navigation.turnTo(0);

            /*if (gamepad2.a) {
                drive.
            }*/

            telemetry.addData("invert:", (invert)? "inverted":"not inverted");
            telemetry.addData("STONE LEVEL", "" + counter);
            telemetry.addData("Vertical slide clicks", moac.linearSlide.getPos(moac.linearSlide.slideVertical));
            telemetry.addData("Horizontal slide clicks", moac.linearSlide.getPos(moac.linearSlide.slideHoriz));
            telemetry.update();
        }
    }
}
