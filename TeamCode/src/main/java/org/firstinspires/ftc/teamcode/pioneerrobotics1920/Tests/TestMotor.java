package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;

@TeleOp(name = "Test motor", group = "test")
public class TestMotor extends OpMode {
    DcMotor slideVertical;
    DcMotor slideHoriz;
    DcMotor intakeLeft;
    DcMotor intakeRight;
    Driving drive;

    @Override
    public void init() {
        drive = new Driving(this);
        //slideVertical = hardwareMap.dcMotor.get("slideVertical");
        //slideHoriz = hardwareMap.dcMotor.get("slideHorizontal");
        //slideVertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //slideVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //slideVertical.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeLeft = hardwareMap.dcMotor.get("leftIntake");
        intakeRight = hardwareMap.dcMotor.get("rightIntake");
        //slideHoriz.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //slideHoriz.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motorPosition(0);

    }

    @Override
    public void loop() {
        if (gamepad1.right_trigger > .5) {
            intakeLeft.setPower(1);
            intakeRight.setPower(-1);
        } else {
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }
        /*if (gamepad1.dpad_up)
            motorPower(1, slideVertical);
        else if (gamepad1.dpad_down)
            motorPower(-0.5, slideVertical);
        else if (gamepad1.a)
            motorPosition(500);
        else
            motorPower(gamepad1.right_stick_y*0.5, slideVertical);//

        if(gamepad1.dpad_left)
            motorPower(0.8, slideHoriz);
        else if(gamepad1.dpad_right)
            motorPower(-.8,slideHoriz);
        else
            motorPower(gamepad1.left_stick_x*.5, slideHoriz);

        if (gamepad1.left_stick_button) {
            drive.frontLeft.setTargetPosition(1000);
            drive.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            drive.frontLeft.setPower(1);

        }

        if (gamepad1.b) {
            drive.frontRight.setTargetPosition(1000);
            drive.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            drive.frontRight.setPower(1);

        }
        if (gamepad1.y) {
            drive.backLeft.setTargetPosition(1000);
            drive.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            drive.backLeft.setPower(1);

        }
        if (gamepad1.x) {
            drive.backRight.setTargetPosition(1000);
            drive.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            drive.backRight.setPower(1);

        }
        telemetry.addData("Front Right Clicks", drive.frontRight.getCurrentPosition());
        telemetry.addData("Front Left Clicks", drive.frontLeft.getCurrentPosition());
        telemetry.addData("Back_Left Clicks", drive.backLeft.getCurrentPosition());
        telemetry.addData("Back Right Clicks", drive.backRight.getCurrentPosition());
        telemetry.addData("motor click: ", slideVertical.getCurrentPosition());
        telemetry.update();*/
    }

    /*public void motorPosition(int clicks) {
        slideVertical.setTargetPosition(clicks);
        slideVertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideVertical.setPower(0.5);
    }

    public void motorPower(double power, DcMotor motor) {
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(power);
    }

    public int getLifterPosition() {
        return slideVertical.getCurrentPosition();
    }

    public void resetEncoder(){
        slideVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }*/
}

