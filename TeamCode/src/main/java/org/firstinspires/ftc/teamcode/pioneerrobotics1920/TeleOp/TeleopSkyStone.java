package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;

@TeleOp(name = "TeleOp SkyStone")
public class TeleopSkyStone extends OpMode {
    Driving drive;
    MoacV_2 moac;
    double POWER = .6;
    Toggle.OneShot lifterOneShot;

    public void init(){
        drive = new Driving(this);
        moac = new MoacV_2(hardwareMap);
        lifterOneShot = new Toggle.OneShot();
    }

    public void loop(){
        // GamePad 1 - Main Controller
        POWER *= (1-gamepad1.right_trigger);

        drive.libertyDrive( -POWER*function(gamepad1.right_stick_y),  POWER*function(gamepad1.right_stick_x), -gamepad1.left_stick_x*POWER);

        moac.foundationGrabber.leftGrab(!gamepad1.left_bumper);
        moac.foundationGrabber.rightGrab(!gamepad1.right_bumper);

        if (gamepad1.dpad_down)
            drive.libertyDrive(POWER*-.5, 0,0);
        if (gamepad1.dpad_left)
            drive.libertyDrive(0,0,POWER*-.5);
        if (gamepad1.dpad_right)
            drive.libertyDrive(0,0,POWER*.5);
        if (gamepad1.dpad_up)
            drive.libertyDrive(POWER*.5,0,0);

        //GamePad 2 - Secondary
        /*
        int units = 0;
        if (gamepad2.dpad_up) {
            units += 50;

            moac.linearSlideConfig.goEncoder(units, POWER-.5);
        }
        if (gamepad2.dpad_down) {
            units -= 50;

            moac.linearSlideConfig.goEncoder(units, POWER-.5);
        }

        if(gamepad2.a)
            moac.linearSlideConfig.goEncoder(0, POWER);
*/
        if (gamepad2.dpad_up)
            moac.linearSlideConfig.lifterPower(0.4);
        else if (gamepad2.dpad_down)
            moac.linearSlideConfig.lifterPower(-0.1);
        if (lifterOneShot.update(!(gamepad2.dpad_up || gamepad2.dpad_down)))
            moac.linearSlideConfig.lifterPower(0);

        //if (gamepad2.a)
          //  moac.linearSlideConfig.lifterPosition(0)-
        moac.teleGrabber.setTelePivot(gamepad2.right_stick_y);

        moac.autoGrab.teleBlueGrab(gamepad2.left_bumper);

        moac.autoGrab.teleBlueArmDown(gamepad2.left_stick_y);

        moac.teleGrabber.grab(gamepad2.right_bumper);

        //TODO: Remove Auto/TeleGrabber to accommodate new hardware.
    }

    public double function(double power){
        if (power<=1) {
            if (power < 0)
                return power * power * -1;
            else
                return power * power;
        }
        return 1;
    }
}

