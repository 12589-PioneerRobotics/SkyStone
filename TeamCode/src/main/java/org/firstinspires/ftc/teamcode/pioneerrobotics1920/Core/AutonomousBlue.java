package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

import java.util.ArrayList;

public class AutonomousBlue extends LinearOpMode implements AutonomousBase {

    public AutonomousBlue(boolean left, boolean startBuilding) {
        auto = new AutonomousCore(this, true, left, startBuilding);
    }

    public AutonomousBase.AutonomousCore auto;
    int count = 0;


    private ArrayList<Stones> stones = new ArrayList<>();


    @Override
    public void park() {
        if (!auto.startBuilding) {
            if (auto.left) {
                auto.nav.moveToY(72);
                auto.nav.turnTo(180);
            } else {
                auto.nav.moveToY(72);
                auto.nav.turnTo(180);
            }
        } else
            auto.nav.backToY(72);
    }

    @Override
    public void getSkystone(SkystoneCVTest.Position pos) {
        switch (pos) {
            case LEFT:
                if (auto.round == 0) {
                    auto.drive.strafeClose(true, false, 36, 24, 1);
                    auto.takeStone();

                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.backDistance) + 7, auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance) + 7);
                } else if (auto.round == 1){
                    auto.drive.smoothTimeBasedForward(2, .5);
                    auto.drive.strafeClose(true, true, 38, 32, 2);
                    auto.takeStoneAgainstWall();
                    auto.drive.strafeClose(true, true, 24, 40, 2);
                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), auto.drive.getAccurateDistanceSensorReading(auto.drive.frontDistance));
                    auto.moac.intake.stopIntake();
                    auto.moac.stacker.close();
                } else {
                    auto.drive.smoothTimeBasedForward(2.2, .6);
                    auto.drive.strafeClose(true, true, 38, 25, 2);
                    auto.takeStoneAgainstWall();
                    auto.drive.strafeClose(true, true, 24, 33, 2);
                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), auto.drive.getAccurateDistanceSensorReading(auto.drive.frontDistance));
                    auto.moac.intake.stopIntake();
                    auto.moac.stacker.close();
                }
                break;

            case CENTER:
                if (auto.round == 0) {
                    auto.drive.strafeClose(true, false, 30, 24, 1);
                    auto.takeStone();

                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.backDistance) + 7, auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance) + 7);

                } else if (auto.round ==1){
                    auto.drive.smoothTimeBasedForward(2.2, .6);
                    auto.drive.strafeClose(true, true, 38, 25, 2);
                    auto.takeStoneAgainstWall();
                    auto.drive.strafeClose(true, true, 24, 33, 2);
                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), auto.drive.getAccurateDistanceSensorReading(auto.drive.frontDistance));
                    auto.moac.intake.stopIntake();
                    auto.moac.stacker.close();
                } else{
                    auto.drive.smoothTimeBasedForward(2.3, .6);
                    auto.drive.strafeClose(true, true, 38, 14, 2);
                    auto.takeStoneAgainstWall();
                    auto.drive.strafeClose(true, true, 24, 28, 2);
                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), auto.drive.getAccurateDistanceSensorReading(auto.drive.frontDistance));
                    auto.moac.intake.stopIntake();
                    auto.moac.stacker.close();
                }
                break;

            case RIGHT:
                if (auto.round == 0) {
                    auto.drive.strafeClose(true, false, 20, 24, 1);
                    auto.takeStone();

                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.backDistance) + 7, auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance) + 7);

                } else if (auto.round == 1){
                    auto.drive.smoothTimeBasedForward(2, .6);
                    auto.drive.strafeClose(true, true, 38, 14, 2);
                    auto.takeStoneAgainstWall();
                    auto.drive.strafeClose(true, true, 24, 28, 2);
                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), auto.drive.getAccurateDistanceSensorReading(auto.drive.frontDistance));
                    auto.moac.intake.stopIntake();
                    auto.moac.stacker.close();
                }
                else {
                    auto.drive.smoothTimeBasedForward(1.5, .6);
                    auto.drive.strafeClose(true, true, 38, 48, 2);
                    auto.takeStoneAgainstWall();
                    auto.drive.strafeClose(true, true, 24, 48, 2);
                    auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), auto.drive.getAccurateDistanceSensorReading(auto.drive.frontDistance));
                    auto.moac.intake.stopIntake();
                    auto.moac.stacker.close();
                }
                break;
            default:
                break;
        }
        auto.round++;
    }

    @Override
    public void getFoundation() {
        if (auto.startBuilding) {
            auto.nav.moveToX(auto.nav.getX() + 5);
            auto.drive.moveClose("left", 20, .4, 2);
            auto.nav.turnTo(270);
            auto.nav.backToX(40);
            auto.moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            auto.nav.moveToX(16);
            auto.moac.foundationGrabber.grabFoundation(true);
        } else {
            auto.drive.moveClose("front", 31, .5, 0f);
            //moac.foundationGrabber.grabFoundation(true);
            auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.frontDistance) + 7, 144 - 7 - auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance));
            sleep(500);
            while (auto.moac.linearSlide.slideHoriz.getCurrentPosition() > -1950) {
                auto.moac.linearSlide.lifterPosition(300);
                auto.moac.linearSlide.horizPosition(-2050);
                if (count == 0)
                    auto.nav.arc(225, 1, .8, .5);
                auto.drive.forward(5, 1);
                auto.nav.arc(180, 1, 1, 1);
                count++;
            }
            auto.moac.stacker.open();
            while (auto.moac.linearSlide.slideHoriz.getCurrentPosition() < -1400) {
                auto.moac.linearSlide.lifterPosition(0);
                auto.moac.linearSlide.horizPosition(0);
            }
            auto.moac.stacker.close();
            //auto.moac.foundationGrabber.grabFoundation(false);
            auto.nav.turnTo(180);
            auto.drive.timeBasedForward(.5, -.6);
            if (!Operations.approximatelyEquals(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), 25, 1))
                auto.drive.moveClose("right", 25, .6, 1f);
            auto.nav.IamAt(auto.drive.getAccurateDistanceSensorReading(auto.drive.rightDistance), 115);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {


        stones.add(new Stones(37, 8));
        stones.add(new Stones(37, 15));
        stones.add(new Stones(37, 24));
        stones.add(new Stones(37, 32));
        stones.add(new Stones(37, 40));
        stones.add(new Stones(37, 48));
        
        /*auto.detector = new SkystoneCVTest();
        auto.detector.changeCrop(auto.blue);
        auto.drive = new Driving(this);
        auto.nav = new Navigation(auto.drive);
        auto.moac = new MoacV_2(this.hardwareMap);*/

        while (!auto.drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors not calibrated", null);
            telemetry.update();
        }
        telemetry.addData("Sensors calibrated", null);

        telemetry.update();

        if (auto.startBuilding) {
            auto.nav.currPos(9, 108, 90);

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();

            auto.nav.moveToX(14);
            auto.nav.moveToY(72);
        } else {
            //blue loading autonomous
            auto.nav.currPos(9, 36, 90);

            auto.detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
            auto.detector.enable();

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();

            auto.skystonePos = auto.detector.getSkystonePos();

            auto.detector.disable();

            telemetry.addData("skystone Pos", auto.detector.getPosition());
            telemetry.update();

            auto.drive.smoothTimeBasedForward(.4, .5);
            getSkystone(auto.skystonePos);

            //next movement
            auto.nav.turnTo(180);
            auto.moac.intake.stopIntake();
            auto.moac.stacker.close();
            auto.nav.backToY(110);

            auto.nav.arc(270,1,1,.2);
            getFoundation();

            getSkystone(auto.skystonePos);

            auto.nav.turnTo(180);
            auto.moac.stacker.close();
            auto.moac.intake.stopIntake();
            auto.moac.linearSlide.horizPosition(-2050);
            auto.nav.backToY(112);
            auto.moac.stacker.open();
            sleep(100);
            auto.moac.stacker.close();
            auto.moac.linearSlide.horizPosition(0);
            auto.moac.linearSlide.lifterPosition(0);
            park();
        }
    }
}
