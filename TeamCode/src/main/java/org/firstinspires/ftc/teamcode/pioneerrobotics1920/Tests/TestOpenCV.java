package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.SamplingCV;

@Autonomous(name = "TestOpenCV", group = "Test")

public class TestOpenCV extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SamplingCV algorithm = new SamplingCV();
        algorithm.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        algorithm.enable();

        waitForStart();


        while (opModeIsActive()) {
            telemetry.addData("Current gold position", algorithm.getGoldPosition());
            telemetry.addData("Current error", algorithm.getErrorString());
            telemetry.addData("Angle: ", Math.toDegrees(Math.atan2(8,25)));
            telemetry.update();
            idle();
        }
        algorithm.disable();
    }
}