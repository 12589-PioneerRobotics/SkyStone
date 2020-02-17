package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;

@TeleOp(name = "Controller")
@Disabled
public class Controller extends OpMode {
    private Driving drive;
    private Toggle.OneShot strafeOneShot;
    boolean blue;
    //7355608
    MoacV_2 moac;
    float power;
    int countVertical, countHoriz = 0;

    double startAngle;

    public double function(double power){
        return function(power, 1);
    }
    public double function(double power, double scale){
        if (power<=1) {
            if (power < 0)
                return -(scale * power * power);
            else
                return scale * power * power;
        }
        return 1;
    }

    public void init() {
        moac = new MoacV_2(this.hardwareMap, blue);
        drive = new Driving(this);
        strafeOneShot = new Toggle.OneShot();
    }

    public void loop() {

        drive.libertyDrive(-function(gamepad1.right_stick_y), function(gamepad1.right_stick_x, 0.6), function(gamepad1.left_stick_x));


        if (gamepad1.dpad_left) {
            if (strafeOneShot.update(gamepad1.dpad_left)) {
                startAngle = drive.gyro.getValueContinuous();
            }
            drive.correctStrafe(0.6, startAngle);
        }
        if (gamepad1.dpad_right) {
            if (strafeOneShot.update(gamepad1.dpad_right)) {
                startAngle = drive.gyro.getValueContinuous();
            }
            drive.correctStrafe(-0.6, startAngle);
        }

        if(gamepad1.a) {
            moac.intake.takeIn();
        }
        else moac.intake.stopIntake();

        if(gamepad1.b) {
            moac.intake.spitOut();
        }
        else moac.intake.stopIntake();


        moac.foundationGrabber.grabFoundation(!gamepad1.left_bumper);
/*
        telemetry.addData("Left Stick y: ", -gamepad1.left_stick_y);
        telemetry.addData("Left Stick x: ", gamepad1.left_stick_x);
        telemetry.addData("angle",drive.gyro.getValueContinuous());
        telemetry.update();
        drive.getPowers();
*/
    }

}
