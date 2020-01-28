package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;

@TeleOp (name = "Linear TeleOp")
public class LinearTeleOp extends LinearOpMode {
    Driving drive;
    MoacV_2 moac;
    Toggle.OneShot vertSlideOneShot;
    Toggle.OneShot horizSlideOneShot;
    Toggle.OneShot grabOneShot;
    Toggle.OneShot modeOneShot;
    Toggle.OneShot dpadOneShot;
    Toggle.OneShot lifterOneShot;

    boolean invert;

    final double SCALE = 0.4;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        moac = new MoacV_2(hardwareMap);
        vertSlideOneShot = new Toggle.OneShot();
        horizSlideOneShot = new Toggle.OneShot();
        grabOneShot = new Toggle.OneShot();
        modeOneShot = new Toggle.OneShot();
        dpadOneShot = new Toggle.OneShot();
        lifterOneShot = new Toggle.OneShot();
        invert = false;

        telemetry.addData("init finished", null);
        telemetry.update();

        waitForStart();

        while (this.opModeIsActive()){
            if (modeOneShot.update(gamepad1.start)) invert = !invert;

            if (invert) {
                if (gamepad1.left_bumper)
                    drive.libertyDrive(Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE), -Operations.powerScale(gamepad1.right_stick_x, SCALE + 0.25));
                else
                    drive.libertyDrive(Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.left_stick_x), -gamepad1.right_stick_x);
            }
            else {
                if (gamepad1.left_bumper)
                    drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.right_stick_x, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE + 0.25));
                else
                    drive.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.right_stick_x), gamepad1.left_stick_x);
            }

            if (gamepad1.dpad_up) moac.linearSlide.lifterPower(1); //max height 5100 allen poop
            else if (gamepad1.dpad_down) moac.linearSlide.lifterPower(-0.6);
            else {
                if (gamepad1.a) moac.linearSlide.lifterPosition(0);
            }

            if (lifterOneShot.update(!(gamepad1.dpad_up || gamepad1.dpad_down))) moac.linearSlide.lifterPower(0);

            if (gamepad1.dpad_right) moac.linearSlide.horizSlidePower(-.6);
            else if (gamepad1.dpad_left) moac.linearSlide.horizSlidePower(.6);
            else {
                if (horizSlideOneShot.update(gamepad1.b)) moac.linearSlide.horiz();
            }

            if (dpadOneShot.update(!(gamepad1.dpad_right||gamepad1.dpad_left)) || moac.linearSlide.slideVertical.getCurrentPosition()>5100) moac.linearSlide.horizSlidePower(0);


            if(gamepad1.left_trigger > .5) moac.intake.spitOut();
            else if(gamepad1.right_trigger > .5) moac.intake.takeIn();
            else moac.intake.stopIntake();
            moac.foundationGrabber.grabFoundation(gamepad1.right_bumper);

            //if(grabOneShot.update(gamepad1.y)) moac.stacker.grab();
            if (gamepad1.y) moac.stacker.open();
            else if (gamepad1.right_trigger > .5) moac.stacker.open();
            else moac.stacker.close();

            telemetry.addData("invert:", (invert)? "inverted":"not inverted");
            telemetry.addData("Vertical slide clicks", moac.linearSlide.getPos(moac.linearSlide.slideVertical));
            telemetry.addData("Horizontal slide clicks", moac.linearSlide.getPos(moac.linearSlide.slideHoriz));
            telemetry.update();
        }
    }
}
