package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

import java.util.ArrayList;

public class AutonomousBlue extends LinearOpMode implements AutonomousBase {

    public AutonomousBase.AutonomousCore auto;

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
        int facingBlue = 90;
        switch (pos) {
            case LEFT:
                if (auto.round == 0) {
                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("right", 36, .6, 2f);//move parallel to the block
                    } else auto.nav.moveToY(36);
                    auto.nav.turnTo(facingBlue);

                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("back", 24, .6, 3.5f);//move up to the block.
                    } else {
                        auto.nav.moveToX(28);
                        auto.nav.turnTo(facingBlue);
                    }
                    auto.nav.turnTo(facingBlue);
                    auto.takeStone();

                    auto.nav.currPos(auto.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, auto.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    stones.remove(5);
                    auto.round++;
                } else {
                    auto.nav.moveToY(stones.get(2).y);

                    auto.nav.turnTo(facingBlue);

                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("right", 14, .6, 2f);
                        auto.nav.turnTo(facingBlue);
                        auto.drive.moveClose("back", 26, .6, 3.5f);
                    } else {
                        auto.nav.moveToY(15);
                        auto.nav.moveToX(30);
                    }

                    auto.nav.turnTo(facingBlue);

                    auto.takeStone();
                    auto.nav.currPos(auto.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, auto.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);
                    stones.remove(2);
                }
                break;

            case CENTER:
                if (auto.round == 0) {
                    auto.nav.moveToX(12);
                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("right", 30, .6, 2f);//move this far away from the right wall
                        auto.nav.turnTo(facingBlue);
                        auto.drive.moveClose("back", 26, .6, 3.5f);//move forward
                    } else {
                        auto.nav.moveToY(28);
                        auto.nav.moveToX(26);
                    }

                    auto.nav.turnTo(facingBlue);//added to straighten out
                    auto.takeStone();

                    auto.nav.currPos(auto.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, auto.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    stones.remove(4);
                    auto.round++;
                } else {
                    auto.nav.moveToY(stones.get(1).y + 3);

                    auto.nav.turnTo(facingBlue);

                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("right", 6, .6, 2f);//moves to within 6 inches from the right wall
                        auto.nav.turnTo(facingBlue);
                        auto.drive.moveClose("back", 26, .6, 3.5f);//moves to 28 inches forward
                    } else {
                        auto.nav.moveToY(8.5);
                        auto.nav.moveToX(26);
                        auto.nav.moveTo(8.5, 26);
                    }
                    auto.nav.turnTo(facingBlue);//added to straighten out

                    auto.takeStone();

                    auto.nav.currPos(auto.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, auto.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    stones.remove(1);
                }
                break;

            case RIGHT:
                if (auto.round == 0) {
                    auto.drive.forward(6, 1, .6);
                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("right", 20, .6, 1.5f);
                        auto.nav.turnTo(facingBlue);
                        auto.drive.moveClose("back", 25, .5, 1.5f);
                    } else {
                        auto.nav.moveToY(22.5);
                        auto.nav.moveToX(26);
                    }
                    auto.nav.turnTo(facingBlue);//added to straighten out. May or may not be necessary

                    auto.takeStone();

                    auto.nav.currPos(auto.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, auto.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);


                    stones.remove(3);
                    auto.round++;
                } else {
                    auto.nav.moveTo(auto.nav.getX(), stones.get(0).y + 25);

                    auto.nav.turnTo(180);
                    sleep(350);

                    auto.drive.moveClose("front", 14, .4, 1f);//moves to within 6 inches from the right wall

                    auto.drive.moveClose("right", 38, .6, 2f);
                    auto.nav.turnTo(180);
                    auto.takeStoneAgainstWall();

                    auto.drive.moveClose("right", 25, .6, 1.5f);

                    auto.nav.turnTo(180);

                    auto.nav.IamAt(auto.drive.rightDistance.getDistance(DistanceUnit.INCH), auto.drive.frontDistance.getDistance(DistanceUnit.INCH) + 6);

                    stones.remove(1);
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
            auto.drive.moveClose("back", 1, 0.3, 1f);
            auto.moac.foundationGrabber.grabFoundation(true);
            auto.nav.IamAt(40 - auto.drive.backDistance.getDistance(DistanceUnit.INCH), 136 - auto.drive.rightDistance.getDistance(DistanceUnit.INCH));
            //auto.drive.forward(-2,1,.7);
            sleep(500);
            while (auto.moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && auto.moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                auto.moac.linearSlide.lifterPosition(500);
                auto.moac.linearSlide.horizPosition(-2050);
                auto.nav.arc(180, 5, 1, .6);
            }
            auto.moac.stacker.open();
            sleep(100);
            auto.moac.stacker.close();
            auto.moac.linearSlide.lifterPosition(0);
            auto.moac.linearSlide.horizPosition(0);
            auto.drive.forward(-12, 1, 1);
            auto.moac.foundationGrabber.grabFoundation(false);
            auto.nav.IamAt(auto.drive.rightDistance.getDistance(DistanceUnit.INCH) + 8, 120);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        auto = new AutonomousCore(this);

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

            //first movement
            //auto.nav.moveToX(14);
            //detect skystone, includes all movements for getting stones
            getSkystone(auto.skystonePos);

            //next movement
            auto.nav.turnTo(180);
            auto.moac.intake.stopIntake();
            auto.moac.stacker.close();
            auto.nav.backToY(124);
            sleep(300);
            auto.drive.moveClose("back", 12, .5, 1.5f);

            auto.nav.turnTo(270);
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
