package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "DirectChasisTeleopTest", group = "test")
@Disabled
public class DirectChassisTeleopTest extends OpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
    }

    public void loop() {
        if (gamepad1.a) {
            frontLeft.setPower(1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(1);
        }
        if (gamepad1.b) {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
    }
}
