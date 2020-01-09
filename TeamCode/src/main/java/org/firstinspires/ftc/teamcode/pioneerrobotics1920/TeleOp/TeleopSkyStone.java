package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;

@TeleOp(name = "TeleOp SkyStone")
public class TeleopSkyStone extends OpMode {
    Driving drive;
    MoacV_2 moac;
    Toggle.OneShot lifterOneShot;
    int countVertical, countHoriz = 0;
    final double SCALE = .2;

    public void init(){
        drive = new Driving(this);
        moac = new MoacV_2(hardwareMap);
        lifterOneShot = new Toggle.OneShot();
    }

    public void loop(){
        // Movement:
        if(gamepad1.left_bumper) {
            drive.libertyDrive( -function(gamepad1.right_stick_y, SCALE),  function(gamepad1.right_stick_x, SCALE), -gamepad1.left_stick_x);
        }
        else drive.libertyDrive( -function(gamepad1.right_stick_y),  function(gamepad1.right_stick_x), -gamepad1.left_stick_x);



        //Triggers
        if(gamepad1.left_trigger > .5) {
            moac.intake.spitOut();
        }
        else if(gamepad1.right_trigger > .5) {
            moac.intake.takeIn();
        }
        else moac.intake.stopIntake();

        moac.foundationGrabber.grabFoundation(gamepad1.b);
        moac.stacker.flip(gamepad1.a);
        moac.stacker.grabStacker(gamepad1.right_bumper);


        //Horizontal + Vertical slide controls
        if (lifterOneShot.update(gamepad1.dpad_down) && countVertical > 0) {
            countVertical--;
            moac.linearSlide.setVerticalPosition(countVertical);
        }

        if (lifterOneShot.update(gamepad1.dpad_left) && countHoriz > 0) {
            countHoriz--;
            moac.linearSlide.setHorizPosition(countHoriz);
        }

        if (lifterOneShot.update(gamepad1.dpad_right) && countHoriz < moac.linearSlide.horizPositions.length - 1) {
            countHoriz++;
            moac.linearSlide.setHorizPosition(countHoriz);
        }

        if (lifterOneShot.update(gamepad1.dpad_up) && countVertical < moac.linearSlide.verticalPositions.length - 1) {
            countVertical++;
            moac.linearSlide.setVerticalPosition(countVertical);
        }
    }

    public double function(double power){
        return function(power, 1);
    }
    public double function(double power, double scale){
//        if(power < .3 || power > -.3 ) {
//            return 0;
//        }
        if (power<=1) {
            if (power < 0)
                return -(scale * power * power);
            else
                return scale * power * power;
        }
        return 1;
    }
}

