package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.FoundationDetection;

@Autonomous(name = "FoundationDetectionDemo", group = "Test")
public class FoundationDetectionDemo extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        FoundationDetection algorithm = new FoundationDetection();
        algorithm.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        algorithm.enable();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Center Values", algorithm.value2Str(algorithm.centerImageValues));
            telemetry.addData("center", "("+algorithm.centerX+","+algorithm.centerY+")");
            telemetry.addData("foundation pos", algorithm.calcFieldX(algorithm.centerY));
            telemetry.update();
            idle();
        }
        algorithm.disable();
    }
}
