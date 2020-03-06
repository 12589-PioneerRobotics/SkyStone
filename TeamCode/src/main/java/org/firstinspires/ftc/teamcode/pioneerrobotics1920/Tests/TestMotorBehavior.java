package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@Disabled
@TeleOp (name = "Test Motor Behavior")
public class TestMotorBehavior extends LinearOpMode {

    DcMotor testMotor;
    Toggle.OneShot motorOneShot;

    @Override
    public void runOpMode() throws InterruptedException {
        testMotor = hardwareMap.dcMotor.get("testMotor");
        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorPos(0);

        motorOneShot = new Toggle.OneShot();


        waitForStart();

        while(this.opModeIsActive()){

            if (gamepad1.dpad_up)
                motorPower(0.5);
            else if (gamepad1.dpad_down)
                motorPower(-0.5);
            else{
                if (gamepad1.a)
                    motorPos(1000);
                else if (gamepad1.b)
                    motorPos(0);
            }

            if (motorOneShot.update(!(gamepad1.dpad_up || gamepad1.dpad_down)))
                testMotor.setPower(0);

            telemetry.addData("motorPos", testMotor.getCurrentPosition());
            telemetry.update();
        }
    }

    public void motorPower(double pow){
        testMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        testMotor.setPower(pow);
    }
    public void motorPos(int clicks){
        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        testMotor.setTargetPosition(clicks);
        testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        testMotor.setPower(0.5);
    }
}
