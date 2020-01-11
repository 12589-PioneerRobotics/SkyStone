package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@TeleOp(name = "Test motor", group = "test")
public class TestMotor extends OpMode {
    DcMotor motor1, motor2;
    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("slideVertical");
        motor2 = hardwareMap.dcMotor.get("slideHoriz");
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorPosition(0);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        if (gamepad1.a)
            resetEncoder();
        if (gamepad1.b)
            motorPosition(0);
        motorPower(gamepad1.left_stick_y);
        motor2.setPower(gamepad1.right_stick_y);
        telemetry.addData("motor1: ", motor1.getCurrentPosition());
        telemetry.addData("motor2: ", motor2.getCurrentPosition());
        telemetry.update();
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
    public void runToPos(int clicks) {
        motor1.setTargetPosition(clicks);
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor1.setPower(0.3);
    }

    public void resetEncoder(){
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}

