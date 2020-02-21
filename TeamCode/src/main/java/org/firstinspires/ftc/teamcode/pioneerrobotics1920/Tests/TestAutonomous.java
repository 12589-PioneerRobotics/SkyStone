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
        drive.strafeClose(true, false, 36, 24, 2);
//        drive.stopDriving();

    }
}
