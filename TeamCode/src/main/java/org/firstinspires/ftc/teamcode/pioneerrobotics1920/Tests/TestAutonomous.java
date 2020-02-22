package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;

@Autonomous(name = "Test Autonomous", group = "Test")
public class TestAutonomous extends Auton {
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        nav = new Navigation(drive);
        moac = new MoacV_2(hardwareMap);
        int count = 0;
        nav.currPos(9, 36, 90);
        waitForStart();

        drive.smoothTimeBasedForward(.4, .5);

        drive.strafeClose(true, false, 36, 24, 2);
        //drive.forward(-20, 1);
        takeStone();
        nav.turnTo(180);
        telemetry.addData("frontDistance", drive.getAccurateDistanceSensorReading(drive.frontDistance));
        telemetry.update();
        nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 6);

        nav.backToY(110);
        moac.intake.stopIntake();
        moac.stacker.close();
        nav.turnTo(270);
        {
            drive.moveClose("front", 31, .5, 0f);
            //moac.foundationGrabber.grabFoundation(true);
            nav.IamAt(drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7, 144 - 7 - drive.getAccurateDistanceSensorReading(drive.rightDistance));
            sleep(500);
            while (moac.linearSlide.slideHoriz.getCurrentPosition() > -1950) {
                moac.linearSlide.lifterPosition(300);
                moac.linearSlide.horizPosition(-2050);
                if (count == 0)
                    nav.arc(225, 1, .8, .5);
                drive.forward(5, 1);
                nav.arc(180, 1, 1, 1);
                count++;
            }
            moac.stacker.open();
            while (moac.linearSlide.slideHoriz.getCurrentPosition() < -1400) {
                moac.linearSlide.lifterPosition(0);
                moac.linearSlide.horizPosition(0);
            }
            moac.stacker.close();
            //moac.foundationGrabber.grabFoundation(false);
            nav.turnTo(180);
            drive.timeBasedForward(.5, -.6);
            if (!Operations.approximatelyEquals(drive.getAccurateDistanceSensorReading(drive.rightDistance), 25, 1))
                drive.moveClose("right", 25, .6, 1f);
            nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance), 115);
        }

        drive.smoothTimeBasedForward(2, .5);
        drive.strafeClose(true, true, 38, 32, 2);
        takeStoneAgainstWall();
        drive.strafeClose(true, true, 24, 40, 2);
        nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance), drive.getAccurateDistanceSensorReading(drive.frontDistance));
        moac.intake.stopIntake();
        moac.stacker.close();

    }
}
