package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;

@Autonomous(name = "Test Autonomous", group = "Test")
public class TestAutonomous extends LinearOpMode {
    Driving drive;
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        telemetry.addData("init finished", null);
        telemetry.update();

        waitForStart();
        drive.timeBasedStrafe(.8, 1.5);

    }
}
