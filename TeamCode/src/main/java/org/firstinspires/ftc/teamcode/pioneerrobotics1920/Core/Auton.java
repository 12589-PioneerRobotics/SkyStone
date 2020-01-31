package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

import java.util.ArrayList;

@Autonomous(name = "Autonomous")
//@Disabled
public class Auton extends LinearOpMode {

    public boolean blue = true;
    protected boolean startBuilding = false;
    public boolean left = true;
    private boolean grabFoundation = false;
    private boolean useDistanceSensors = true;
    private int round = 0;
    private MoacV_2 moac;

    private SkystoneCVTest detector;
    private Driving drive;
    private SkystoneCVTest.Position skystonePos;
    private Navigation nav;
    private ArrayList<Stones> blueStones = new ArrayList<>();
    private ArrayList<Stones> redStones = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        detector = new SkystoneCVTest();
        detector.changeCrop(blue);
        drive = new Driving(this);
        nav = new Navigation(drive);
        moac = new MoacV_2(this.hardwareMap);

        //add stones into the array
        {
            //all coordinate positions of skystones on blue side
            blueStones.add(new Stones(37, 8));
            blueStones.add(new Stones(37, 15));
            blueStones.add(new Stones(37, 24));
            blueStones.add(new Stones(37, 32));
            blueStones.add(new Stones(37, 40));
            blueStones.add(new Stones(37, 48));
            //red: left = 5, center = 4, right = 3
            //all coordinate positions of skystones on red side
            redStones.add(new Stones(110, 21));
            redStones.add(new Stones(110, 12));
            redStones.add(new Stones(110, 8));
            redStones.add(new Stones(110, 48)); //right
            redStones.add(new Stones(110, 40)); //center
            redStones.add(new Stones(110, 32)); //left
        }

        //check gyro status
        {
            while (!drive.gyro.isGyroReady()) {
                telemetry.addData("Sensors not calibrated", null);
                telemetry.update();
            }
            telemetry.addData("Sensors calibrated", null);

            telemetry.update();
        }

        //*******************THE PART THAT RUNS*************************
        if (blue) {
            //TODO: pull the foundation
            if (startBuilding) {
                nav.currPos(9, 108, 90);

                telemetry.addData("init finished", null);
                telemetry.update();

                waitForStart();
                //*****************************WAIT FOR START*******************************

                nav.moveToX(14);
                nav.moveToY(72);
            }
            //startLoading
            else {
                //start blue loading autonomous
                nav.currPos(9, 36, 90);

                detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
                detector.enable();

                telemetry.addData("init finished", null);
                telemetry.update();

                waitForStart();
                //*****************************WAIT FOR START*******************************

                skystonePos = detector.getSkystonePos();

                detector.disable();

                telemetry.addData("skystone Pos", detector.getPosition());
                telemetry.update();

                //first movement
                //detect skystone, includes all movements for getting stones
                getBlueSkystone(skystonePos);

                //next movement
                if (grabFoundation) {
                    nav.moveToY(120);
                    nav.turnTo(270);

                } else {
                    nav.backToY(85);
                }

                nav.turnTo(180);

                moac.linearSlide.drop();


                getBlueSkystone(skystonePos);

                nav.backToY(85);

                moac.linearSlide.drop();

                park();
            }
        }
        //starting red
        else {
            //TODO: pull the foundation
            if (startBuilding) {
                nav.currPos(135, 108, 270);

                telemetry.addData("init finished", null);
                telemetry.update();

                waitForStart();
                //*****************************WAIT FOR START*******************************

                nav.moveToX(130);
                nav.moveToY(72);
            }
            //startLoading
            else {
                //red loading
                nav.currPos(135, 36, 270);

                detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
                detector.enable();

                telemetry.addData("init finished", null);
                telemetry.update();

                waitForStart();
                //*****************************WAIT FOR START*******************************

                skystonePos = detector.getSkystonePos();

                detector.disable();

                telemetry.addData("Skystone Pos", detector.getPosition());
                telemetry.update();

                nav.moveToX(120);

                getRedSkystone(skystonePos);

                nav.turnTo(180);
                nav.backToY(85);

                moac.linearSlide.drop();

                getRedSkystone(skystonePos);

                nav.turnTo(180);
                nav.backTo(nav.getX(), 85);

                moac.linearSlide.drop();

                sleep(200);
                nav.backTo(nav.getX() - 5, 72);

                moac.intake.stopIntake();
            }
        }
    }
    //*********************END OF PART THAT RUNS**************************

    //TODO: make park() work
    private void park() {
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

    private void getBlueSkystone(SkystoneCVTest.Position pos) {
        int facingBlue = 90;
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("right", 36, .6, 2f);//move parallel to the block
                    } else nav.moveToY(36);
                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("back", 24, .7, 3.5f);//move up to the block.
                    } else {
                        nav.moveToX(28);
                        nav.turnTo(facingBlue);
                    }
                    nav.turnTo(facingBlue);
                    takeStone();


                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    blueStones.remove(5);
                    round++;
                } else {
                    nav.moveTo(nav.getX() - 5, blueStones.get(2).y);

                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 14, .7, 2f);
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 25, .7, 3.5f);
                    } else {
                        nav.moveToY(15);
                        nav.moveToX(30);
                    }

                    nav.turnTo(facingBlue);

                    takeStone();
                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);
                    blueStones.remove(2);

                }
                break;

            case CENTER:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("right", 30, .6, 2f);//move this far away from the right wall
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .7, 3.5f);//move forward
                    } else {
                        nav.moveToY(28);
                        nav.moveToX(26);
                    }

                    nav.turnTo(facingBlue);//added to straighten out
                    takeStone();

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    blueStones.remove(4);
                    round++;
                } else {
                    nav.moveTo(nav.getX() - 5, blueStones.get(1).y + 3);

                    moac.intake.stopIntake();
                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 6, .4, 2f);//moves to within 6 inches from the right wall
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .4, 3.5f);//moves to 28 inches forward
                    } else {
                        nav.moveToY(8.5);
                        nav.moveToX(26);
                        nav.moveTo(8.5, 26);
                    }
                    nav.turnTo(facingBlue);//added to straighten out

                    takeStone();


                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    blueStones.remove(1);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    nav.moveToX(24);
                    if (useDistanceSensors) {
                        drive.moveClose("right", 22, .5, 2f);
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 25, .5, 3);
                    } else {
                        nav.moveToY(22.5);
                        nav.moveToX(26);
                    }
                    nav.turnTo(facingBlue);//added to straighten out. May or may not be necessary

                    takeStone();

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);


                    blueStones.remove(3);
                    round++;
                } else {
                    nav.moveTo(nav.getX() - 5, blueStones.get(0).y + 10);

                    moac.intake.stopIntake();

                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 14, .6, 2f);//moves to within 6 inches from the right wall
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 25, .3, 1.5f);//moves to 28 inches forward
                    } else {
                        nav.moveToY(8.5);
                        nav.moveToX(26);
                        nav.moveTo(8.5, 26);
                    }
                    nav.turnTo(180);//added to straighten out
                    drive.moveClose("right", 32, .6, 2f);
                    takeStone();

                    drive.moveClose("right", 25, .6, 1.5f);


                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7, facingBlue);

                    blueStones.remove(1);
                }
                break;
            default:
                break;
        }
        round++;
    }

    private void getRedSkystone(SkystoneCVTest.Position pos) {
        int facingRed = 270;
        useDistanceSensors = true;
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    drive.moveClose("left", 21, .5, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .5, 3f);

                    takeStone();

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(5);

                    round++;
                } else {
                    nav.backToY(redStones.get(2).y + 12);

                    moac.intake.stopIntake();

                    nav.turnTo(facingRed);

                    drive.moveClose("left", 13.5, .5, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 24, .5, 2f);

                    nav.turnTo(facingRed - 50);

                    takeStoneAgainstWall();

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(2);
                }
                break;

            case CENTER:
                if (round == 0) {
                    drive.moveClose("left", 28.5, .5, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .5, 3f);

//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();
                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(4);

                    round++;
                } else {
//                    nav.backToY(redStones.get(1).y + 12, .8);
                    nav.backTo(nav.getX() + 3, redStones.get(1).y + 10);

                    moac.intake.stopIntake();

                    nav.turnTo(facingRed);

                    drive.moveClose("left", 5.5, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 28, .4, 3f);

                    takeStone();

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(1);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("left", 36, .5, 2f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 27, .5, 3f);
                    } else {//lol this doesnt even work
                        nav.moveToY(36);
                        nav.moveToX(118);
                    }

                    nav.turnTo(facingRed);

                    takeStone();

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(3);

                    round++;
                } else {
                    nav.backToY(redStones.get(0).y + 10);

                    moac.intake.stopIntake();

                    nav.turnTo(facingRed);
                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    if (useDistanceSensors) {
                        drive.moveClose("left", 14, .5, 2f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 28, .5, 3);
                    } else {
                        nav.moveToY(15.5);
                        nav.moveToX(118);
                    }

                    takeStone();

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(0);
                }
                break;
            default:
                break;
        }
        round++;
    }

    private void takeStone() {
        final double DISTANCE = 17;
        moac.stacker.open();
        moac.intake.takeIn();
        if (blue) {
            nav.moveToX(nav.getX() + DISTANCE);
            moac.intake.stopIntake();
            moac.stacker.close();
            nav.backToX(nav.getX() - DISTANCE);
        } else {
            nav.moveToX(nav.getX() - DISTANCE);
            moac.intake.stopIntake();
            moac.stacker.close();
            nav.backToX(nav.getX() + DISTANCE);
        }
    }

    private void takeStoneAgainstWall() {
        moac.intake.takeIn();
        drive.forward(12, 0.6);
        sleep(200);
        moac.intake.stopIntake();
        drive.forward(-12, 1);
    }

    //TODO: test the method
    public void getFoundation(boolean blue, boolean startBuilding) {
        if (blue) {
            if (startBuilding) {
                nav.moveToX(nav.getX() + 5);
                drive.moveClose("left", 20, .4, 2);
                nav.turnTo(270);
                nav.backToX(40);
                moac.foundationGrabber.grabFoundation(false);
                sleep(500);
                nav.moveToX(16);
                moac.foundationGrabber.grabFoundation(true);
            } else {
                drive.moveClose("back", 1.8, 0.6, 1f);
                nav.currPos(48 - drive.backDistance.getDistance(DistanceUnit.INCH), drive.rightDistance.getDistance(DistanceUnit.INCH), 270);
                drive.forward(-10, 0.8);
                sleep(500);
                drive.forward(10, 0.8);
                nav.moveToX(nav.getX() - 15);
                nav.turnTo(180);
                nav.backToY(nav.getY() + 20);
            }
        } else {
            if (startBuilding) {
                nav.moveToX(nav.getX() - 5);
                drive.moveClose("right", 20, .4, 2);
                nav.turnTo(90);
                nav.backToX(104);
                moac.foundationGrabber.grabFoundation(false);
                sleep(500);
                nav.moveToX(128);
                moac.foundationGrabber.grabFoundation(true);
            } else {
                drive.moveClose("back", 1.8, 0.6, 1f);
                nav.currPos(96 + drive.backDistance.getDistance(DistanceUnit.INCH), drive.leftDistance.getDistance(DistanceUnit.INCH), 90);
                drive.forward(-10, 0.8);
                sleep(500);
                drive.forward(10, 0.8);
                nav.moveToX(nav.getX() + 15);
                nav.turnTo(180);
                nav.backToY(nav.getY() + 20);
            }
        }
    }

    class Stones {
        public int x, y;

        Stones(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}




