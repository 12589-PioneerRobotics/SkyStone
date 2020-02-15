package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

import java.util.ArrayList;

public class AutonomousBlue extends LinearOpMode implements AutonomousBase {

    public AutonomousBase.AutonomousCore autonCore;

    private ArrayList<Stones> stones = new ArrayList<>();

    @Override
    public void park() {

    }

    @Override
    public void getSkystone(SkystoneCVTest.Position pos) {
        int facingBlue = 90;
        switch (pos) {
            case LEFT:
                if (autonCore.round == 0) {
                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("right", 36, .6, 2f);//move parallel to the block
                    } else autonCore.nav.moveToY(36);
                    autonCore.nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("back", 24, .6, 3.5f);//move up to the block.
                    } else {
                        autonCore.nav.moveToX(28);
                        autonCore.nav.turnTo(facingBlue);
                    }
                    autonCore.nav.turnTo(facingBlue);
                    autonCore.takeStone();

                    autonCore.nav.currPos(autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    stones.remove(5);
                    autonCore.round++;
                } else {
                    autonCore.nav.moveToY(stones.get(2).y);

                    autonCore.nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("right", 14, .6, 2f);
                        autonCore.nav.turnTo(facingBlue);
                        autonCore.drive.moveClose("back", 26, .6, 3.5f);
                    } else {
                        autonCore.nav.moveToY(15);
                        autonCore.nav.moveToX(30);
                    }

                    autonCore.nav.turnTo(facingBlue);

                    autonCore.takeStone();
                    autonCore.nav.currPos(autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);
                    stones.remove(2);
                }
                break;

            case CENTER:
                if (autonCore.round == 0) {
                    autonCore.nav.moveToX(12);
                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("right", 30, .6, 2f);//move this far away from the right wall
                        autonCore.nav.turnTo(facingBlue);
                        autonCore.drive.moveClose("back", 26, .6, 3.5f);//move forward
                    } else {
                        autonCore.nav.moveToY(28);
                        autonCore.nav.moveToX(26);
                    }

                    autonCore.nav.turnTo(facingBlue);//added to straighten out
                    autonCore.takeStone();

                    autonCore.nav.currPos(autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    stones.remove(4);
                    autonCore.round++;
                } else {
                    autonCore.nav.moveToY(stones.get(1).y + 3);

                    autonCore.nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("right", 6, .6, 2f);//moves to within 6 inches from the right wall
                        autonCore.nav.turnTo(facingBlue);
                        autonCore.drive.moveClose("back", 26, .6, 3.5f);//moves to 28 inches forward
                    } else {
                        autonCore.nav.moveToY(8.5);
                        autonCore.nav.moveToX(26);
                        autonCore.nav.moveTo(8.5, 26);
                    }
                    autonCore.nav.turnTo(facingBlue);//added to straighten out

                    autonCore.takeStone();

                    autonCore.nav.currPos(autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    stones.remove(1);
                }
                break;

            case RIGHT:
                if (autonCore.round == 0) {
                    autonCore.drive.forward(6, 1, .6);
                    if (useDistanceSensors) {
                        autonCore.drive.moveClose("right", 20, .6, 1.5f);
                        autonCore.nav.turnTo(facingBlue);
                        autonCore.drive.moveClose("back", 25, .5, 1.5f);
                    } else {
                        autonCore.nav.moveToY(22.5);
                        autonCore.nav.moveToX(26);
                    }
                    autonCore.nav.turnTo(facingBlue);//added to straighten out. May or may not be necessary

                    autonCore.takeStone();

                    autonCore.nav.currPos(autonCore.drive.backDistance.getDistance(DistanceUnit.INCH) + 7, autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);


                    stones.remove(3);
                    autonCore.round++;
                } else {
                    autonCore.nav.moveTo(autonCore.nav.getX(), stones.get(0).y + 25);

                    autonCore.nav.turnTo(180);
                    sleep(350);

                    autonCore.drive.moveClose("front", 14, .4, 1f);//moves to within 6 inches from the right wall

                    autonCore.drive.moveClose("right", 38, .6, 2f);
                    autonCore.nav.turnTo(180);
                    autonCore.takeStoneAgainstWall();

                    autonCore.drive.moveClose("right", 25, .6, 1.5f);

                    autonCore.nav.turnTo(180);

                    autonCore.nav.IamAt(autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH), autonCore.drive.frontDistance.getDistance(DistanceUnit.INCH) + 6);

                    stones.remove(1);
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
            autonCore.nav.moveToX(autonCore.nav.getX() + 5);
            autonCore.drive.moveClose("left", 20, .4, 2);
            autonCore.nav.turnTo(270);
            autonCore.nav.backToX(40);
            autonCore.moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            autonCore.nav.moveToX(16);
            autonCore.moac.foundationGrabber.grabFoundation(true);
        } else {
            autonCore.drive.moveClose("back", 1, 0.3, 1f);
            autonCore.moac.foundationGrabber.grabFoundation(true);
            autonCore.nav.IamAt(40 - autonCore.drive.backDistance.getDistance(DistanceUnit.INCH), 136 - autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH));
            //autonCore.drive.forward(-2,1,.7);
            sleep(500);
            while (autonCore.moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && autonCore.moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                autonCore.moac.linearSlide.lifterPosition(500);
                autonCore.moac.linearSlide.horizPosition(-2050);
                autonCore.nav.arc(180, 5, 1, .6);
            }
            autonCore.moac.stacker.open();
            sleep(100);
            autonCore.moac.stacker.close();
            autonCore.moac.linearSlide.lifterPosition(0);
            autonCore.moac.linearSlide.horizPosition(0);
            autonCore.drive.forward(-12, 1, 1);
            autonCore.moac.foundationGrabber.grabFoundation(false);
            autonCore.nav.IamAt(autonCore.drive.rightDistance.getDistance(DistanceUnit.INCH) + 8, 120);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        autonCore = new AutonomousCore(this);

        stones.add(new Stones(37, 8));
        stones.add(new Stones(37, 15));
        stones.add(new Stones(37, 24));
        stones.add(new Stones(37, 32));
        stones.add(new Stones(37, 40));
        stones.add(new Stones(37, 48));
        
        /*autonCore.detector = new SkystoneCVTest();
        autonCore.detector.changeCrop(blue);
        autonCore.drive = new Driving(this);
        autonCore.nav = new Navigation(autonCore.drive);
        autonCore.moac = new MoacV_2(this.hardwareMap);*/

        while (!autonCore.drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors not calibrated", null);
            telemetry.update();
        }
        telemetry.addData("Sensors calibrated", null);

        telemetry.update();

        if (startBuilding) {
            autonCore.nav.currPos(9, 108, 90);

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();

            autonCore.nav.moveToX(14);
            autonCore.nav.moveToY(72);
        } else {
            //start blue loading autonomous
            autonCore.nav.currPos(9, 36, 90);

            autonCore.detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
            autonCore.detector.enable();

            telemetry.addData("init finished", null);
            telemetry.update();

            waitForStart();
            //*****************************WAIT FOR START*******************************

            autonCore.skystonePos = autonCore.detector.getSkystonePos();

            autonCore.detector.disable();

            telemetry.addData("skystone Pos", autonCore.detector.getPosition());
            telemetry.update();

            //first movement
            //autonCore.nav.moveToX(14);
            //detect skystone, includes all movements for getting stones
            getSkystone(autonCore.skystonePos);

            //next movement
            autonCore.nav.turnTo(180);
            autonCore.moac.intake.stopIntake();
            autonCore.moac.stacker.close();
            autonCore.nav.backToY(124);
            sleep(300);
            autonCore.drive.moveClose("back", 12, .5, 1.5f);

            autonCore.nav.turnTo(270);
            getFoundation(blue, startBuilding);

            getSkystone(autonCore.skystonePos);

            autonCore.nav.turnTo(180);
            autonCore.moac.stacker.close();
            autonCore.moac.intake.stopIntake();
            autonCore.moac.linearSlide.horizPosition(-2050);
            autonCore.nav.backToY(112);
            autonCore.moac.stacker.open();
            sleep(100);
            autonCore.moac.stacker.close();
            autonCore.moac.linearSlide.horizPosition(0);
            autonCore.moac.linearSlide.lifterPosition(0);
            park();
        }
    }
}
