package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;

@TeleOp(name = "Controller")
public class Controller extends OpMode {
    private Driving drive;
    private Toggle.OneShot strafeOneShot;
    //7355608
    MoacV_2 moac;
    float power;
    int countVertical, countHoriz = 0;

    double startAngle;

    public double function(double power){
        if (power<=1) {
            if (power < 0)
                return -(0.5 * power * power);
            else
                return 0.5 * power * power;
        }
        return 1;
    }

    public void init() {
        moac = new MoacV_2(this.hardwareMap);
        drive = new Driving(this);
        strafeOneShot = new Toggle.OneShot();
    }

    public void loop() {

        drive.libertyDrive(-function(gamepad1.right_stick_y), function(gamepad1.right_stick_x), -function(gamepad1.left_stick_x));


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

        if(gamepad1.b) {
            moac.intake.spitOut();
        }
        if(gamepad1.left_bumper) {
            if(countVertical == 0) {
                countVertical = 4;//4 is highest position for now
            }
            countVertical--;
            moac.linearSlide.setVerticalPosition(countVertical);
        }

        telemetry.addData("Left Stick y: ", -gamepad1.left_stick_y);
        telemetry.addData("Left Stick x: ", gamepad1.left_stick_x);
        telemetry.addData("angle",drive.gyro.getValueContinuous());
        telemetry.update();
        drive.getPowers();
    }

}
