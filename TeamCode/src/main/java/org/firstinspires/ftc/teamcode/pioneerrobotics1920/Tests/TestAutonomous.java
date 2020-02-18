package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;

@Autonomous(name = "Test Autonomous", group = "Test")
public class TestAutonomous extends Auton {
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        nav = new Navigation(drive);
        nav.currPos(30, 30, 0);
        telemetry.addData("init finished", null);
        telemetry.update();
        waitForStart();
        double curTime = getRuntime();
        while (getRuntime() < curTime + .5)
            drive.libertyDrive(-.4, 0, 0);
//        drive.stopDriving();

    }
}
