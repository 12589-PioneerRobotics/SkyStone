package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Test Autonomous", group = "Test")
public class TestAutonomous extends Auton {

    public void runOpMode() throws InterruptedException {
        telemetry.addData("init finished", null);
        telemetry.update();
        waitForStart();
        takeStone();
        drop();
    }
}
