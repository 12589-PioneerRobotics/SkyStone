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
    public boolean grabFoundation = false;
    public boolean useDistanceSensors = true;
    public int round = 0;
    public MoacV_2 moac;

    public SkystoneCVTest detector;
    public Driving drive;
    public SkystoneCVTest.Position skystonePos;
    public Navigation nav;
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
                //nav.moveToX(14);
                //detect skystone, includes all movements for getting stones
                getBlueSkystone(skystonePos);

                //next movement
                nav.turnTo(180);
                nav.backToY(115);

                nav.turnTo(270);
                getFoundation(blue, startBuilding);

                getBlueSkystone(skystonePos);

                nav.turnTo(180);

                nav.backToY(110);

                drop();

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

                getRedSkystone(skystonePos);

                nav.backToY(120);
                nav.turnTo(90);

                getFoundation(blue, startBuilding);

                getRedSkystone(skystonePos);

                nav.turnTo(180);
                moac.intake.stopIntake();
                nav.backTo(nav.getX(), 85);

                drop();

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
                if (blue) nav.backTo(36, 72);
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
                        drive.moveClose("back", 24, .6, 3.5f);//move up to the block.
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
                    nav.moveToY(blueStones.get(2).y);

                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 14, .6, 2f);
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .6, 3.5f);
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
                        drive.moveClose("back", 26, .6, 3.5f);//move forward
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
                    nav.moveToY(blueStones.get(1).y + 3);

                    nav.turnTo(facingBlue);

                    if (useDistanceSensors) {
                        drive.moveClose("right", 6, .6, 2f);//moves to within 6 inches from the right wall
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 26, .6, 3.5f);//moves to 28 inches forward
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
                    nav.moveToX(18);
                    if (useDistanceSensors) {
                        drive.moveClose("right", 20, .6, 2f);
                        nav.turnTo(facingBlue);
                        drive.moveClose("back", 24, .6, 3);
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
                    nav.moveTo(nav.getX(), blueStones.get(0).y + 10);

                    moac.intake.stopIntake();

                    nav.turnTo(facingBlue);

                    drive.moveClose("right", 13, .6, 1f);//moves to within 6 inches from the right wall
                    nav.turnTo(facingBlue);
                    drive.moveClose("back", 18, .6, 1.5f);//moves to 28 inches forward
                    nav.turnTo(180);//added to straighten out
                    drive.moveClose("right", 36, .6, 2f);
                    takeStoneAgainstWall();

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
                    drive.moveClose("left", 21, .6, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .6, 3f);

                    takeStone();

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(5);

                    round++;
                } else {
                    nav.moveToY(redStones.get(2).y + 12);

                    moac.intake.stopIntake();

                    nav.turnTo(facingRed);

                    drive.moveClose("left", 14, .6, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 23, .6, 2f);

                    nav.turnTo(180);
                    drive.moveClose("right", 38, .6, 2f);
                    takeStone();

                    drive.moveClose("right", 25, .6, 1.5f);

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(2);
                }
                break;

            case CENTER:
                if (round == 0) {
                    drive.moveClose("left", 28.5, .6, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .6, 3f);

//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();
                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(4);

                    round++;
                } else {
//                    nav.backToY(redStones.get(1).y + 12, .8);
                    nav.moveTo(nav.getX(), redStones.get(1).y + 10);

                    moac.intake.stopIntake();

                    nav.turnTo(facingRed);

                    drive.moveClose("left", 5.5, .6, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 28, .6, 3f);

                    takeStone();

                    nav.turnTo(facingRed);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7, facingRed);

                    redStones.remove(1);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("left", 36, .6, 2f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 27, .6, 3f);
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
                        drive.moveClose("left", 14, .6, 2f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 28, .6, 3);
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

    public void takeStone() {
        final double DISTANCE = 16;
        moac.stacker.open();
        moac.intake.takeIn();
        drive.forward(DISTANCE, 1);
        drive.forward(-DISTANCE - 6, 1);
        moac.stacker.close();
        moac.intake.stopIntake();
    }

    public void drop() {
        while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
            moac.linearSlide.lifterPosition(400);
            moac.linearSlide.horizPosition(-2050);
        }
        moac.stacker.open();
        sleep(100);
        moac.stacker.close();
        while (moac.linearSlide.slideHoriz.getCurrentPosition() < -20 && moac.linearSlide.slideVertical.getCurrentPosition() > 20) {
            moac.linearSlide.lifterPosition(0);
            moac.linearSlide.horizPosition(0);
        }
    }

    public void takeStoneAgainstWall() {
        moac.intake.takeIn();
        drive.forward(10, 0.6);
        drive.forward(-10, 1);
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
                drive.moveClose("back", 1, 0.3, 1f);
                moac.foundationGrabber.grabFoundation(true);
                nav.IamAt(40 - drive.backDistance.getDistance(DistanceUnit.INCH), 136 - drive.rightDistance.getDistance(DistanceUnit.INCH));
                //drive.forward(-2,1,.7);
                sleep(500);
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                    moac.linearSlide.lifterPosition(500);
                    moac.linearSlide.horizPosition(-2050);
                    nav.arc(180, 3, 1, 1);
                }
                moac.stacker.open();
                nav.IamAt(drive.rightDistance.getDistance(DistanceUnit.INCH) + 8, 100);
                sleep(100);
                moac.stacker.close();
                moac.linearSlide.lifterPosition(0);
                moac.linearSlide.horizPosition(0);
                moac.foundationGrabber.grabFoundation(false);

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
                drive.moveClose("back", 1, 0.3, 1f);
                moac.foundationGrabber.grabFoundation(true);
                nav.IamAt(104 - drive.backDistance.getDistance(DistanceUnit.INCH), 136 - drive.rightDistance.getDistance(DistanceUnit.INCH));
                sleep(200);
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                    moac.linearSlide.lifterPosition(500);
                    moac.linearSlide.horizPosition(-2050);
                    nav.arc(180, 3, 1, .8);
                }
                moac.stacker.open();
                nav.IamAt(drive.leftDistance.getDistance(DistanceUnit.INCH) - 8, 100);
                sleep(100);
                moac.stacker.close();
                moac.linearSlide.lifterPosition(0);
                moac.linearSlide.horizPosition(0);
                moac.foundationGrabber.grabFoundation(false);

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




