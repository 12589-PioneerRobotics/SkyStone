package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Toggle;

@TeleOp(name = "Controller")
public class Controller extends OpMode {
    private Driving drive;
    private Toggle.OneShot strafeOneShot;
    //7355608
    float power;

    double startAngle;

    public double function(double power){
        if (power<=1) {
            if (power < 0)
                return -(0.5 * power * power + 0.1);
            else
                return 0.5 * power * power + 0.1;
        }
        return 1;
    }

    public void init() {

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

        //drive.servoControl.setServoPairs(ServoControl.SERVOS.TOP, gamepad2.left_stick_x);
        //drive.servoControl.setServoPairs(ServoControl.SERVOS.BOTTOM, gamepad2.right_stick_x);
        //telemetry.addData("Pivot servo: ", servo.getServoPosition(ServoControl.SERVOS.PIVOT));
        //telemetry.addData("Bottom Servo: ", servo.getServoPosition(ServoControl.SERVOS.BOTTOM));
        //telemetry.addData("Top Servo: ", servo.getServoPosition(ServoControl.SERVOS.TOP));
        telemetry.addData("Left Stick y: ", -gamepad1.left_stick_y);
        telemetry.addData("Left Stick x: ", gamepad1.left_stick_x);
        telemetry.addData("angle",drive.gyro.getValueContinuous());
        telemetry.update();
        //drive.getPowers();
    }

}
