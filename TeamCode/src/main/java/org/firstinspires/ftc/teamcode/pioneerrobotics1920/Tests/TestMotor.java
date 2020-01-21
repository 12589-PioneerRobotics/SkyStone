package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@TeleOp(name = "Test motor", group = "test")
public class TestMotor extends OpMode {
    DcMotor motor1;
    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("slideVertical");
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motorPosition(0);
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up)
            motorPower(0.6);
        else if (gamepad1.dpad_down)
            motorPower(-0.3);
        else if (gamepad1.a)
            motorPosition(0);
        else
            motorPower(gamepad1.right_stick_y*0.1);
    }

    public void motorPosition(int clicks) {
        motor1.setTargetPosition(clicks);
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor1.setPower(0.3);
    }

    public void motorPower(double power) {
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor1.setPower(power);
    }

    public int getLifterPosition() {
        return motor1.getCurrentPosition();
    }

    public void resetEncoder(){
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}

