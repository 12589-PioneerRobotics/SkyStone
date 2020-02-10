package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;

@TeleOp(name = "TeleOp SkyStone")
@Disabled
public class TeleopSkyStone extends OpMode {
    Driving drive;
    MoacV_2 moac;
    Toggle.OneShot lifterOneShot;
    Toggle.OneShot aOneShot;
    Toggle.OneShot dpad_rightOneShot;
    final double SCALE = .4;

    public void init(){
        drive = new Driving(this);
        moac = new MoacV_2(hardwareMap);
        lifterOneShot = new Toggle.OneShot();
        telemetry.addData("init finished", null);
        aOneShot = new Toggle.OneShot();
        dpad_rightOneShot = new Toggle.OneShot();
    }

    public void loop(){
        // Movement:
        if(gamepad1.left_bumper) {
            drive.libertyDrive( -Operations.powerScale(gamepad1.right_stick_y, SCALE),  Operations.powerScale(gamepad1.right_stick_x, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE+0.25));
        }
        else drive.libertyDrive( -Operations.powerScale(gamepad1.right_stick_y),  Operations.powerScale(gamepad1.right_stick_x), gamepad1.left_stick_x);

        //Triggers (Intake)
        if(gamepad1.left_trigger > .5) {
            moac.intake.spitOut();
        }
        else if(gamepad1.right_trigger > .5) {
            moac.intake.takeIn();
        }
        else moac.intake.stopIntake();

        if(aOneShot.update(gamepad1.a))
            moac.stacker.grab();

        if (gamepad1.dpad_up)
            moac.linearSlide.lifterPower(1);
        else if (gamepad1.dpad_down)
            moac.linearSlide.lifterPower(-0.7);
        else
            moac.linearSlide.lifterPower(0);
        /*if (dpad_rightOneShot.update(gamepad1.dpad_right))
            moac.stacker.grab();*/
       if (gamepad1.dpad_right) {
            moac.linearSlide.horizSlidePower(.5);
        }
        else if (gamepad1.dpad_left)
            moac.linearSlide.horizSlidePower(-.5);
        else
            moac.linearSlide.horizSlidePower(0);

        moac.foundationGrabber.grabFoundation(gamepad1.right_bumper);
//        moac.stacker.flip(gamepad1.a);
//        moac.stacker.grabStacker(gamepad1.right_bumper);


        //Horizontal + Vertical slide controls
        /*if (lifterOneShot.update(gamepad1.dpad_down) && countVertical > 0) {
            countVertical--;
            moac.linearSlide.setVerticalPosition(countVertical);
        }
        if (lifterOneShot.update(gamepad1.dpad_up) && countVertical < moac.linearSlide.verticalPositions.length - 1) {
            countVertical++;
            moac.linearSlide.setVerticalPosition(countVertical);
        }*/

        /*
        if (gamepad1.dpad_down) moac.linearSlide.lifterPower(-.2);
        else if (gamepad1.dpad_up) moac.linearSlide.lifterPower(.6);
        else moac.linearSlide.lifterPower(0);

        if (gamepad1.dpad_right) moac.linearSlide.horizSlidePower(-.6);
        else if (gamepad1.dpad_left) moac.linearSlide.horizSlidePower(.6);
        else moac.linearSlide.horizSlidePower(0);
*/

        //if (lifterOneShot.update(gamepad1.y)) moac.stacker.rotate();
        //moac.stacker.rotateHold(gamepad1.y);
        /*
        if (lifterOneShot.update(gamepad1.dpad_left) && countHoriz > 0) {
            countHoriz--;
            moac.linearSlide.setHorizPosition(countHoriz);
        }

        if (lifterOneShot.update(gamepad1.dpad_right) && countHoriz < moac.linearSlide.horizPositions.length - 1) {
            countHoriz++;
            moac.linearSlide.setHorizPosition(countHoriz);
        }
*/

    }
}

