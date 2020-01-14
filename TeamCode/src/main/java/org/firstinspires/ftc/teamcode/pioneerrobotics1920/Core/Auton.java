package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.FoundationDetection;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

import java.util.ArrayList;

@Autonomous(name = "Autonomous")
//@Disabled
public class Auton extends LinearOpMode {
    //instantiations
    //final double angleLeft = 72.2553282;

    public boolean blue = true;
    public boolean startBuilding = false;
    public boolean left = true;
    public boolean useDistanceSensors = true;
    private int round = 0;
    MoacV_2 moac;

    SkystoneCVTest detector;
    Driving drive;
    SkystoneCVTest.Position skystonePos;
    Navigation nav;
    ArrayList<Stones> blueStones = new ArrayList<>();
    ArrayList<Stones> redStones = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        detector = new SkystoneCVTest();
        detector.changeCrop(blue);
        drive = new Driving(this);
        nav = new Navigation(drive);
        moac = new MoacV_2(this.hardwareMap);

        //all coordinate positions of skystones on blue side
        blueStones.add(new Stones(37, 8));
        blueStones.add(new Stones(37, 15));
        blueStones.add(new Stones(37, 24));
        blueStones.add(new Stones(37, 32));
        blueStones.add(new Stones(37, 40));
        blueStones.add(new Stones(37, 48));
        //red: left = 5, center = 4, right = 3
        //all coordinate positions of skystones on red side
        redStones.add(new Stones(110, 21)); //Change the values so that we align the collector with the blocks for red like we did in blue
        redStones.add(new Stones(110, 12));
        redStones.add(new Stones(110, 8));
        redStones.add(new Stones(110, 48)); //right
        redStones.add(new Stones(110, 40)); //center
        redStones.add(new Stones(110, 32)); //left

        while (!drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors not calibrated", null);
            telemetry.update();
        }

        if (drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors calibrated", null);
        }
        telemetry.update();

        //*******************THE PART THAT RUNS*************************
        if (blue) {
            if (startBuilding) {
                nav.currPos(9, 108, 90);

                waitForStart();

                //getFoundation(blue);
                //nav.moveToX(14, 0.5);
                //park();
                nav.moveToX(14);
                nav.moveToY(72);
            } else {
                //start blue loading autonomous
                nav.currPos(9, 36, 90);

                detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
                detector.enable();

                waitForStart();

                skystonePos = detector.getSkystonePos();

                detector.disable();

                telemetry.addData("skystone Pos", detector.getPosition());
                telemetry.update();

                //first movement
                nav.moveToX(14, 0.6);

                //detect skystone, includes all movements for getting stones
                getBlueSkystone(skystonePos);

                ///nav.backToX(28);
                nav.turnTo(0);

                //next movement
                nav.moveToY(90);


                //takeStoneOut();
                moac.intake.spitOut();

                sleep(200);

                getBlueSkystone(skystonePos);

                moac.intake.stopIntake();

                nav.backToX(28);
                nav.turnTo(0);

                nav.moveToY(90);

                //takeStoneOut();

                moac.intake.spitOut();

                sleep(200);

                park();

                moac.intake.stopIntake();
            }
        } else { //starting red
            if (startBuilding) {
                nav.currPos(135, 108, 270);
                waitForStart();

                //getFoundation(blue);
                //nav.moveToX(130, 0.5);
                //park();
                nav.moveToX(130);
                nav.moveToY(72);
            } else {
                //red loading
                nav.currPos(135, 36, 270);

                detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
                detector.enable();

                telemetry.addData("init finished", null);
                telemetry.update();

                waitForStart();

                skystonePos = detector.getSkystonePos();

                detector.disable();

                telemetry.addData("Skystone Pos", detector.getPosition());
                telemetry.update();

                nav.moveToX(120, 0.5);

                getRedSkystone(skystonePos);

//                nav.backToX(110);
                nav.turnTo(0);
                nav.moveToY(90);

                //takeStoneOut();
                moac.intake.spitOut();

                sleep(200);

                getRedSkystone(skystonePos);

                moac.intake.stopIntake();

//                nav.backToX(110);
                nav.turnTo(0);
                if(skystonePos == SkystoneCVTest.Position.CENTER) {
                    nav.moveTo(nav.getX() - 7, 85);
                }
                else nav.moveTo(nav.getX() - 5, 85);

                //takeStoneOut();

                moac.intake.spitOut();

                sleep(200);
                nav.backTo(nav.getX()-5, 72);

                moac.intake.stopIntake();
//                park();
            }
        }
    }

    //*********************END OF PART THAT RUNS**************************

    public void park() {
        if (!startBuilding) {
            if (left) {
                if (blue) nav.backTo(24, 72);
                else nav.backTo(120, 72);
                nav.turnTo(0);
            } else {
                if (blue) nav.backTo(32, 72);
                else nav.backTo(110, 72);
                nav.turnTo(0);
            }
        } else
            nav.backTo(24, 72);
    }

    public void getBlueSkystone(SkystoneCVTest.Position pos) {
        int facingBlue = 90;
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("right", 36, .5, 2f);//move parallel to the block
                    } else nav.moveToY(36);
                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("back", 26, .5, 3.5f);//move up to the block.
                    } else {
                        nav.moveToX(28);
                        nav.turnTo(facingBlue);
                    }
                    nav.turnTo(facingBlue);
                    takeStone();


                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(5).y, facingBlue);

                    blueStones.remove(5);
                    round++;
                } else {
                    nav.backTo(24, blueStones.get(2).y + 10);

                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 14, .4, 2f);
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .4, 3.5f);
                    } else {
                        nav.moveToY(15);
                        nav.moveToX(30);
                    }

                    nav.turnTo(facingBlue);

                    takeStone();
                    nav.turnTo(facingBlue);
                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(2).y, facingBlue);
                    blueStones.remove(2);

                }
                break;

            case CENTER:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("right", 30, .4, 2f);//move this far away from the right wall
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .4, 3.5f);//move forward
                    } else {
                        nav.moveToY(28);
                        nav.moveToX(26);
                    }

                    nav.turnTo(facingBlue);//added to straighten out
                    takeStone();
                    nav.turnTo(facingBlue);
                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(4).y, facingBlue);

                    blueStones.remove(4);
                    round++;
                } else {
                    nav.backTo(24, blueStones.get(1).y + 6);
                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 8, .4, 2f);//moves to within 6 inches from the right wall
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .4, 3.5f);//moves to 28 inches forward
                    } else {
                        nav.moveToY(8.5);
                        nav.moveToX(26);
                        nav.moveTo(8.5, 26);
                    }
                    nav.turnTo(facingBlue);//added to straighten out

                    takeStone();

                    nav.turnTo(facingBlue);

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(1).y, facingBlue);

                    blueStones.remove(1);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("right", 21, .4, 2f);
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .4, 3);
                    } else {
                        nav.moveToY(22.5);
                        nav.moveToX(26);
                    }
                    nav.turnTo(facingBlue);//added to straighten out. May or may not be necessary

                    moac.intake.takeStone(drive, nav, this);

                    nav.turnTo(facingBlue);

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(3).y, facingBlue);


                    blueStones.remove(3);
                    round++;
                } else {
                    //center else case
                    nav.backTo(24, blueStones.get(1).y + 6);
                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 8, .4, 2f);//moves to within 6 inches from the right wall
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .4, 3.5f);//moves to 28 inches forward
                    } else {
                        nav.moveToY(8.5);
                        nav.moveToX(26);
                        nav.moveTo(8.5, 26);
                    }
                    nav.turnTo(facingBlue);//added to straighten out

                    takeStone();

                    nav.turnTo(facingBlue);

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(1).y, facingBlue);

                    blueStones.remove(1);

                   /* nav.backToY(blueStones.get(0).y, 0.5);
                    nav.turnTo(facingBlue);

                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    if (useDistanceSensors) {
                        drive.moveClose("right", 10, .4, 2f);
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 20, .4, 3f);
                    } else {
                        nav.moveToY(21.6);
                        nav.moveToX(26);
                    }

                    nav.turnTo(135);

                    takeStone();

                    nav.turnTo(facingBlue);

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(0).y, facingBlue);//angle should be nav.getAngle()

                    blueStones.remove(0);*/
                }
                break;
            default:
                break;
        }
        round++;
    }

    public void getRedSkystone(SkystoneCVTest.Position pos) {
        int facingRed = 270;
        useDistanceSensors = true;
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    drive.moveClose("left", 18, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 28, .25, 3.5f);

                    takeStone();

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 8, redStones.get(5).y, facingRed);

                    redStones.remove(5);

                    round++;
                } else {
                    nav.backToY(redStones.get(2).y + 12, 0.8);

                    nav.turnTo(facingRed);

                    drive.moveClose("left", 7, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 24, .4, 3.5f);


//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();
                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 8, redStones.get(2).y, facingRed);

                    redStones.remove(2);
                }
                break;

            case CENTER:
                if (round == 0) {
                    drive.moveClose("left", 28, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .4, 3.5f);

//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();
                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 8, redStones.get(4).y, facingRed);

                    redStones.remove(4);

                    round++;
                } else {
//                    nav.backToY(redStones.get(1).y + 12, .8);
                    nav.backTo(nav.getX() + 3, redStones.get(1).y + 12);
                    nav.turnTo(facingRed);

                    drive.moveClose("left", 5.5, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .4, 3.5f);

//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();
                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 8, redStones.get(1).y, facingRed);

                    redStones.remove(1);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    if(useDistanceSensors) {
                        drive.moveClose("left", 36, .4, 3f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 26, .4, 3);
                    }
                    else {//lol this doesnt even work
                        nav.moveToY(36);
                        nav.moveToX(118);
                    }

                    nav.turnTo(facingRed);

//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH)-8, redStones.get(3).y, facingRed);

                    redStones.remove(3);

                    round++;
                } else {
                    nav.backToY(redStones.get(0).y+12, .8);

                    nav.turnTo(facingRed);
                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    if (useDistanceSensors) {
                        drive.moveClose("left", 15.5, .4, 3f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 26, .4, 3);
                    }
                    else {
                        nav.moveToY(15.5);
                        nav.moveToX(118);
                    }

                    nav.turnTo(facingRed);

//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();
                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 8, redStones.get(0).y, facingRed);

                    redStones.remove(0);

                }
                break;
            default:
                break;
        }
        round++;
    }

    public void getFoundation(boolean blue) {
        if (blue) {
            nav.moveToX(nav.getX() + 5);
            drive.moveClose("left", 20, .4, 2);
            nav.turnTo(270);
            nav.backToX(40);
            moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            nav.moveToX(16);
            moac.foundationGrabber.grabFoundation(true);
        } else {
            nav.moveToX(nav.getX() - 5);
            drive.moveClose("right", 20, .4, 2);
            nav.turnTo(90);
            nav.backToX(104);
            moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            nav.moveToX(128);
            moac.foundationGrabber.grabFoundation(true);
        }
    }

    public void takeStone() {
        moac.intake.takeIn();
        drive.forward(15, 0.6);
        moac.intake.stopIntake();
        drive.forward(-15, 0.6);
    }

    public void takeStoneOut() {
        moac.intake.spitOut();
        sleep(900);
        moac.intake.stopIntake();
    }

    /*public boolean distanceSensorFine(String sensor) {
        switch (sensor) {
            case "back":
        }
        return Math.abs(nav.getX() -  drive.backDistance.getDistance(DistanceUnit.INCH) > 5;

    }*/
}


class Stones {
    public int x, y;

    public Stones(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


