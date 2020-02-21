package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;

@Autonomous(name = "Test Autonomous", group = "Test")
public class TestAutonomous extends Auton {
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        nav = new Navigation(drive);
        moac = new MoacV_2(hardwareMap);
        nav.currPos(30, 30, 90);
        waitForStart();
        drive.strafeClose(true, false, 36, 24, 1);
        takeStone();
        //drive.strafeClose(true,24,48);
//        drive.stopDriving();

    }
}
