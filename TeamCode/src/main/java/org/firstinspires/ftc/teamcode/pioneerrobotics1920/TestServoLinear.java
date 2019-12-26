package org.firstinspires.ftc.teamcode.pioneerrobotics1920;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestServoLinear")
@Disabled
public class TestServoLinear extends LinearOpMode {
    MoacV_2 moacV_2;
    @Override
    public void runOpMode() throws InterruptedException {
        moacV_2 = new MoacV_2(true, hardwareMap);
        waitForStart();
        while(this.opModeIsActive()) {
            if (gamepad1.a)
                moacV_2.autoGrab.blueInitialize();
            if (gamepad1.b) {
                moacV_2.autoGrab.blueArmDown();
                sleep(200);
                moacV_2.autoGrab.blueClose();
                sleep(200);
                moacV_2.autoGrab.blueLift();
            }
        }
    }
}
