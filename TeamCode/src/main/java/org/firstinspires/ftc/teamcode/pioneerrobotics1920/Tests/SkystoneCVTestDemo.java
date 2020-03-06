package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

@Disabled
@Autonomous(name = "SkystoneCVTestDemo", group = "Test")
public class SkystoneCVTestDemo extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SkystoneCVTest algorithm = new SkystoneCVTest();
        algorithm.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        algorithm.enable();

        waitForStart();

        while (opModeIsActive()) {
            //telemetry.addData("Current gold position", algorithm.getGoldPosition());
            //telemetry.addData("Current error", algorithm.getErrorString());
            telemetry.addData("Left Values", algorithm.value2Str(algorithm.leftImageValues));
            telemetry.addData("Center Values", algorithm.value2Str(algorithm.centerImageValues));
            telemetry.addData("Right Values", algorithm.value2Str(algorithm.rightImageValues));
            telemetry.addData("Test Get Pos", algorithm.getPosition());
            telemetry.addData("Distances: ", algorithm.getDistance());
            telemetry.update();
            idle();
        }
        algorithm.disable();
    }
}