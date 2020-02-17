package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

import java.util.ArrayList;

public class AutonomousRed extends LinearOpMode implements AutonomousBase {
    public AutonomousCore auto;

    private ArrayList<Stones> stones = new ArrayList<>();
    private MoacV_2 moac;

    @Override
    public void runOpMode() throws InterruptedException {
        stones.add(new Stones(110, 21));
        stones.add(new Stones(110, 12));
        stones.add(new Stones(110, 8));
        stones.add(new Stones(110, 48)); //right
        stones.add(new Stones(110, 40)); //center
        stones.add(new Stones(110, 32)); //left

        auto.detector = new SkystoneCVTest();
        auto.detector.changeCrop(auto.blue);
        auto.drive = new Driving(this);
        auto.nav = new Navigation(auto.drive);
        moac = new MoacV_2(this.hardwareMap);

        while (!auto.drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors not calibrated", null);
            telemetry.update();
        }
        telemetry.addData("Sensors calibrated", null);

        telemetry.update();

        if (auto.startBuilding) {
            auto.nav.currPos(135, 108, 270);

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();
            //*****************************WAIT FOR START*******************************

            auto.nav.moveToX(130);
            auto.nav.moveToY(72);
        }
        //startLoading
        else {
            //red loading


            //************************************************
            auto.nav.currPos(135, 36, 270);

            auto.detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
            auto.detector.enable();

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();
            //*****************************WAIT FOR START*******************************
            auto.drive.forward(6, 1, .5);
            auto.skystonePos = auto.detector.getSkystonePos();

            auto.detector.disable();

            telemetry.addData("Skystone Pos", auto.detector.getPosition());
            telemetry.update();

            getSkystone(auto.skystonePos);

            auto.nav.turnTo(180);
            moac.intake.stopIntake();
            moac.stacker.close();

            auto.nav.backToY(120);
            sleep(300);
            auto.drive.moveClose("back", 12, .5, 1.5f);

            auto.nav.turnTo(90);
            getFoundation();

            getSkystone(auto.skystonePos);

            auto.nav.turnTo(180);

            moac.intake.stopIntake();
            moac.stacker.close();

            moac.linearSlide.horizPosition(-2050);
            auto.nav.backToY(115);

            moac.stacker.open();
            sleep(100);
            moac.stacker.close();
            moac.linearSlide.horizPosition(0);

            park();
        }

    }

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
        int facingRed = 270;
        switch (pos) {
            case LEFT:
                if (auto.round == 0) {
                    auto.drive.moveClose("left", 20, .6, 2f);

                    auto.nav.turnTo(facingRed);

                    auto.drive.moveClose("back", 25, .6, 3f);

                    auto.takeStone();

                    auto.nav.turnTo(facingRed);

                    auto.nav.currPos(144 - auto.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, auto.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(5);

                    auto.round++;
                } else {
                    auto.nav.moveToY(stones.get(2).y + 20);

                    auto.nav.turnTo(180);

                    sleep(350);

                    auto.drive.moveClose("front", 13, .5, 2f);

                    auto.drive.moveClose("left", 38, .6, 2f);
                    auto.nav.turnTo(180);
                    auto.takeStone();

                    auto.drive.moveClose("left", 25, .6, 1.5f);

                    auto.nav.turnTo(180);

                    auto.nav.IamAt(144 - auto.drive.leftDistance.getDistance(DistanceUnit.INCH) - 7, auto.drive.frontDistance.getDistance(DistanceUnit.INCH) + 6);

                    stones.remove(2);
                }
                break;

            case CENTER:
                if (auto.round == 0) {
                    auto.drive.moveClose("left", 30, .6, 2f);

                    auto.nav.turnTo(facingRed);

                    auto.drive.moveClose("back", 26, .6, 3f);

//                    moac.intake.takeStone(auto.drive,auto.nav,this);
                    auto.takeStone();
                    auto.nav.turnTo(facingRed);

                    auto.nav.currPos(144 - auto.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, auto.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(4);

                    auto.round++;
                } else {
//                    auto.nav.backToY(stones.get(1).y + 12, .8);
                    auto.nav.moveTo(auto.nav.getX(), stones.get(1).y + 10);

                    moac.intake.stopIntake();

                    auto.nav.turnTo(facingRed);

                    auto.drive.moveClose("left", 5.5, .6, 2f);

                    auto.nav.turnTo(facingRed);

                    auto.drive.moveClose("back", 28, .6, 3f);

                    auto.takeStone();

                    auto.nav.turnTo(facingRed);

                    auto.nav.currPos(144 - auto.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, auto.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(1);
                }
                break;

            case RIGHT:
                if (auto.round == 0) {
                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("left", 36, .6, 2f);

                        auto.nav.turnTo(facingRed);

                        auto.drive.moveClose("back", 24, .6, 3f);
                    } else {//lol this doesnt even work
                        auto.nav.moveToY(36);
                        auto.nav.moveToX(118);
                    }

                    auto.nav.turnTo(facingRed);

                    auto.takeStone();

                    auto.nav.currPos(144 - auto.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, auto.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(3);

                    auto.round++;
                } else {
                    auto.nav.moveToY(stones.get(0).y + 10);

                    moac.intake.stopIntake();

                    auto.nav.turnTo(facingRed);
                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    if (auto.useDistanceSensors) {
                        auto.drive.moveClose("left", 14, .6, 2f);

                        auto.nav.turnTo(facingRed);

                        auto.drive.moveClose("back", 28, .6, 3);
                    } else {
                        auto.nav.moveToY(15.5);
                        auto.nav.moveToX(118);
                    }

                    auto.takeStone();

                    auto.nav.turnTo(facingRed);

                    auto.nav.currPos(144 - auto.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, auto.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(0);
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
            auto.nav.moveToX(auto.nav.getX() - 5);
            auto.drive.moveClose("right", 20, .4, 2);
            auto.nav.turnTo(90);
            auto.nav.backToX(104);
            moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            auto.nav.moveToX(128);
            moac.foundationGrabber.grabFoundation(true);
        } else {
            auto.drive.moveClose("back", 1, 0.3, 1f);
            moac.foundationGrabber.grabFoundation(true);
            auto.nav.IamAt(144 - auto.drive.frontDistance.getDistance(DistanceUnit.INCH) - 6, 136 - auto.drive.leftDistance.getDistance(DistanceUnit.INCH));
            //auto.drive.forward(-2,1,.7);
            sleep(500);
            while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                moac.linearSlide.lifterPosition(500);
                moac.linearSlide.horizPosition(-2050);
                auto.nav.arc(180, 5, 1, .6);
            }
            moac.stacker.open();
            sleep(100);
            moac.stacker.close();
            moac.linearSlide.lifterPosition(0);
            moac.linearSlide.horizPosition(0);
            auto.drive.forward(-12, 1, 1);
            moac.foundationGrabber.grabFoundation(false);
            auto.nav.IamAt(auto.drive.leftDistance.getDistance(DistanceUnit.INCH) + 8, 120);
        }
    }
}

