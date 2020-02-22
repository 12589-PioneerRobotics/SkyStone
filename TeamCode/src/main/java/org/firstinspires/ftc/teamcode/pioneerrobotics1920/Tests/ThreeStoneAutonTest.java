package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;

import java.util.ArrayList;

@Autonomous(name = "three stone auton test", group = "test")
public class ThreeStoneAutonTest extends Auton {

    private ArrayList<Stones> blueStones = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        detector = new SkystoneCVTest();
        detector.changeCrop(blue);
        drive = new Driving(this);
        nav = new Navigation(drive);
        moac = new MoacV_2(this.hardwareMap);

        {
            blueStones.add(new Stones(37, 8));
            blueStones.add(new Stones(37, 15));
            blueStones.add(new Stones(37, 24));
            blueStones.add(new Stones(37, 32));
            blueStones.add(new Stones(37, 40));
            blueStones.add(new Stones(37, 48));
        }

        nav.currPos(9, 36, 90);

        waitForStart();

        skystonePos = detector.getSkystonePos();

        telemetry.addData("skystone Pos", detector.getPosition());
        telemetry.update();

        {
            switch (skystonePos) {
                case LEFT:
                    drive.strafeClose(true, false, 24, 14, 1);
                    nav.turnTo(90);
                    takeStone(25, blue);
                    nav.turnTo(180);
                    break;
                case CENTER:
                    drive.strafeClose(true, false, 24, 10, 1);
                    nav.turnTo(90);
                    takeStone(25, blue);
                    nav.turnTo(180);
                    break;
                case RIGHT:
                    nav.arc(180, 5, .6, .3);
                    drive.strafeClose(true, true, 38, 15, 1);
                    nav.turnTo(180);
                    takeStoneAgainstWall();
                    drive.moveClose("right", 25, .6, 1);
                    break;
            }
            nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7);
        }

        {
            moac.intake.stopIntake();
            moac.stacker.close();
        }

        if (!Operations.approximatelyEquals(drive.getAccurateDistanceSensorReading(drive.rightDistance), 25, 1))
            drive.moveClose("right", 25, .4, 1);

        nav.backToY(114);
        sleep(300);
        if (!Operations.approximatelyEquals(drive.getAccurateDistanceSensorReading(drive.backDistance), 20, 1))
            drive.moveClose("back", 20, 1, 0f);
        nav.turnTo(270);

        {
            drive.moveClose("front", 31, 5, 0f);
            moac.foundationGrabber.grabFoundation(true);
            nav.IamAt(drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7, 144 - 7 - drive.getAccurateDistanceSensorReading(drive.rightDistance));
            sleep(500);
            while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                moac.linearSlide.lifterPosition(500);
                moac.linearSlide.horizPosition(-2050);
                nav.arc(180, 1, .8, .65);
            }
            moac.stacker.open();
            while (moac.linearSlide.slideHoriz.getCurrentPosition() > 1350) {
                moac.linearSlide.lifterPosition(0);
                moac.linearSlide.horizPosition(0);
            }
            moac.stacker.close();
            moac.foundationGrabber.grabFoundation(false);
            nav.turnTo(180);
            double curTime = getRuntime();
            while (getRuntime() < curTime + .5)
                drive.libertyDrive(-.6, 0, 0);
            drive.stopDriving();
            if (!Operations.approximatelyEquals(drive.getAccurateDistanceSensorReading(drive.rightDistance), 25, 1))
                drive.moveClose("right", 25, .6, 1f);
            nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance), 30);
        }

        //Todo: DON"T turnTo 90 degree and then turn
        {
            switch (skystonePos) {
                case LEFT:
                    nav.moveToY(36);
                    nav.turnTo(90);
                    drive.strafeClose(true, false, 24, 36, 1);
                    nav.turnTo(90);
                    takeStone();
                    nav.turnTo(180);
                    break;
                case CENTER:
                    nav.moveToY(29);
                    nav.turnTo(90);
                    drive.strafeClose(true, false, 24, 29, 1);
                    nav.turnTo(90);
                    takeStone();
                    nav.turnTo(180);
                    break;
                case RIGHT:
                    nav.moveToY(20);
                    nav.turnTo(90);
                    drive.strafeClose(true, false, 24, 20, 1);
                    nav.turnTo(90);
                    takeStone();
                    nav.turnTo(180);
                    break;
            }
            nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7);
        }

        nav.turnTo(180);

        int x = 0;
        while (moac.linearSlide.slideHoriz.getCurrentPosition() > -1950) {
            if (x == 0)
                customizedForward(-(115 - nav.getY()), 1, .25, 42);
            x++;
        }

        nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance) + 8, 110);
        moac.stacker.open();
        moac.linearSlide.horizPosition(0);
        moac.linearSlide.lifterPosition(0);
        while (moac.linearSlide.slideHoriz.getCurrentPosition() < -1400) {
        }
        moac.stacker.close();
        if (!Operations.approximatelyEquals(drive.getAccurateDistanceSensorReading(drive.rightDistance), 25, 1))
            drive.moveClose("right", 25, .6, 1f);

        {
            switch (skystonePos) {
                case LEFT:
                    nav.moveToY(20);
                    nav.turnTo(90);
                    drive.strafeClose(true, false, 24, 20, 1);
                    nav.turnTo(90);
                    takeStone();
                    break;
                case RIGHT:
                    nav.moveToY(36);
                    nav.turnTo(90);
                    drive.strafeClose(true, false, 24, 36, 1);
                    nav.turnTo(90);
                    takeStone();
                    break;
                case CENTER:
                    break;
            }
        }

        if (!skystonePos.equals(SkystoneCVTest.Position.CENTER)) {
            nav.turnTo(180);

            x = 0;
            while (moac.linearSlide.slideHoriz.getCurrentPosition() > -1950) {
                if (x == 0)
                    customizedForward(-(115 - nav.getY()), 1, .25, 42);
                x++;
            }
            nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance) + 8, 110);
            moac.stacker.open();
            moac.linearSlide.horizPosition(0);
            moac.linearSlide.lifterPosition(0);
            while (moac.linearSlide.slideHoriz.getCurrentPosition() < -1400) {
            }
            moac.stacker.close();
            if (!Operations.approximatelyEquals(drive.getAccurateDistanceSensorReading(drive.rightDistance), 25, 1))
                drive.moveClose("right", 25, .6, 1f);
        }

        park();
    }

    class Stones {
        public int x, y;

        Stones(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
