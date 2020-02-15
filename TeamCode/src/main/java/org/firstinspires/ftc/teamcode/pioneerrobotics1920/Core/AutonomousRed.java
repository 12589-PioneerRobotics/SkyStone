package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

import java.util.ArrayList;

public class AutonomousRed extends LinearOpMode implements AutonomousBase {
    AutonomousCore autonCore;

    private ArrayList<Stones> stones = new ArrayList<>();
    MoacV_2 moac;

    @Override
    public void runOpMode() throws InterruptedException {
        stones.add(new Stones(110, 21));
        stones.add(new Stones(110, 12));
        stones.add(new Stones(110, 8));
        stones.add(new Stones(110, 48)); //right
        stones.add(new Stones(110, 40)); //center
        stones.add(new Stones(110, 32)); //left

        autonCore.detector = new SkystoneCVTest();
        autonCore.detector.changeCrop(blue);
        autonCore.drive = new Driving(this);
        autonCore.nav = new Navigation(autonCore.drive);
        moac = new MoacV_2(this.hardwareMap);

        while (!autonCore.drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors not calibrated", null);
            telemetry.update();
        }
        telemetry.addData("Sensors calibrated", null);

        telemetry.update();

        if (startBuilding) {
            autonCore.nav.currPos(135, 108, 270);

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();
            //*****************************WAIT FOR START*******************************

            autonCore.nav.moveToX(130);
            autonCore.nav.moveToY(72);
        }
        //startLoading
        else {
            //red loading


            //************************************************
            autonCore.nav.currPos(135, 36, 270);

            autonCore.detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
            autonCore.detector.enable();

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();
            //*****************************WAIT FOR START*******************************
            autonCore.drive.forward(6, 1, .5);
            autonCore.skystonePos = autonCore.detector.getSkystonePos();

            autonCore.detector.disable();

            telemetry.addData("Skystone Pos", autonCore.detector.getPosition());
            telemetry.update();

            getSkystone(autonCore.skystonePos);

            autonCore.nav.turnTo(180);
            moac.intake.stopIntake();
            moac.stacker.close();

            autonCore.nav.backToY(120);
            sleep(300);
            autonCore.drive.moveClose("back", 12, .5, 1.5f);

            autonCore.nav.turnTo(90);
            getFoundation(blue, startBuilding);

            getSkystone(autonCore.skystonePos);

            autonCore.nav.turnTo(180);

            moac.intake.stopIntake();
            moac.stacker.close();

            moac.linearSlide.horizPosition(-2050);
            autonCore.nav.backToY(115);

            moac.stacker.open();
            sleep(100);
            moac.stacker.close();
            moac.linearSlide.horizPosition(0);

            park();
        }

    }

    @Override
    public void park() {

    }

    @Override
    public void getSkystone(SkystoneCVTest.Position pos) {
        int facingRed = 270;
        switch (pos) {
            case LEFT:
                if (autonCore.round == 0) {
                    autonCore.drive.moveClose("left", 20, .6, 2f);

                    autonCore.nav.turnTo(facingRed);

                    autonCore.drive.moveClose("back", 25, .6, 3f);

                    autonCore.takeStone();

                    autonCore.nav.turnTo(facingRed);

                    autonCore.nav.currPos(144 - autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(5);

                    autonCore.round++;
                } else {
                    autonCore.nav.moveToY(stones.get(2).y + 20);

                    autonCore.nav.turnTo(180);

                    sleep(350);

                    autonCore.drive.moveClose("front", 13, .5, 2f);

                    autonCore.drive.moveClose("left", 38, .6, 2f);
                    autonCore.nav.turnTo(180);
                    autonCore.takeStone();

                    autonCore.drive.moveClose("left", 25, .6, 1.5f);

                    autonCore.nav.turnTo(180);

                    autonCore.nav.IamAt(144 - autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH) - 7, autonCore.drive.frontDistance.getDistance(DistanceUnit.INCH) + 6);

                    stones.remove(2);
                }
                break;

            case CENTER:
                if (autonCore.round == 0) {
                    autonCore.drive.moveClose("left", 30, .6, 2f);

                    autonCore.nav.turnTo(facingRed);

                    autonCore.drive.moveClose("back", 26, .6, 3f);

//                    moac.intake.takeStone(autonCore.drive,autonCore.nav,this);
                    autonCore.takeStone();
                    autonCore.nav.turnTo(facingRed);

                    autonCore.nav.currPos(144 - autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(4);

                    autonCore.round++;
                } else {
//                    autonCore.nav.backToY(stones.get(1).y + 12, .8);
                    autonCore.nav.moveTo(autonCore.nav.getX(), stones.get(1).y + 10);

                    moac.intake.stopIntake();

                    autonCore.nav.turnTo(facingRed);

                    autonCore.drive.moveClose("left", 5.5, .6, 2f);

                    autonCore.nav.turnTo(facingRed);

                    autonCore.drive.moveClose("back", 28, .6, 3f);

                    autonCore.takeStone();

                    autonCore.nav.turnTo(facingRed);

                    autonCore.nav.currPos(144 - autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(1);
                }
                break;

            case RIGHT:
                if (autonCore.round == 0) {
                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("left", 36, .6, 2f);

                        autonCore.nav.turnTo(facingRed);

                        autonCore.drive.moveClose("back", 24, .6, 3f);
                    } else {//lol this doesnt even work
                        autonCore.nav.moveToY(36);
                        autonCore.nav.moveToX(118);
                    }

                    autonCore.nav.turnTo(facingRed);

                    autonCore.takeStone();

                    autonCore.nav.currPos(144 - autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(3);

                    autonCore.round++;
                } else {
                    autonCore.nav.moveToY(stones.get(0).y + 10);

                    moac.intake.stopIntake();

                    autonCore.nav.turnTo(facingRed);
                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("left", 14, .6, 2f);

                        autonCore.nav.turnTo(facingRed);

                        autonCore.drive.moveClose("back", 28, .6, 3);
                    } else {
                        autonCore.nav.moveToY(15.5);
                        autonCore.nav.moveToX(118);
                    }

                    autonCore.takeStone();

                    autonCore.nav.turnTo(facingRed);

                    autonCore.nav.currPos(144 - autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) - 7, autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    stones.remove(0);
                }
                break;
            default:
                break;
        }
        autonCore.round++;
    }

    @Override
    public void getFoundation(boolean blue, boolean startBuilding) {
        if (startBuilding) {
            autonCore.nav.moveToX(autonCore.nav.getX() - 5);
            autonCore.drive.moveClose("right", 20, .4, 2);
            autonCore.nav.turnTo(90);
            autonCore.nav.backToX(104);
            moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            autonCore.nav.moveToX(128);
            moac.foundationGrabber.grabFoundation(true);
        } else {
            autonCore.drive.moveClose("back", 1, 0.3, 1f);
            moac.foundationGrabber.grabFoundation(true);
            autonCore.nav.IamAt(144 - autonCore.drive.frontDistance.getDistance(DistanceUnit.INCH) - 6, 136 - autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH));
            //autonCore.drive.forward(-2,1,.7);
            sleep(500);
            while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                moac.linearSlide.lifterPosition(500);
                moac.linearSlide.horizPosition(-2050);
                autonCore.nav.arc(180, 5, 1, .6);
            }
            moac.stacker.open();
            sleep(100);
            moac.stacker.close();
            moac.linearSlide.lifterPosition(0);
            moac.linearSlide.horizPosition(0);
            autonCore.drive.forward(-12, 1, 1);
            moac.foundationGrabber.grabFoundation(false);
            autonCore.nav.IamAt(autonCore.drive.leftDistance.getDistance(DistanceUnit.INCH) + 8, 120);
        }
    }
}

