package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

                drive.forward(8, 1);
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

                getBlueSkystone(skystonePos);

                //next movement


                moac.intake.stopIntake();
                moac.stacker.close();

                nav.backToY(120);
                sleep(300);
                if (Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 12, 2))
                    drive.moveClose("back", 14, 1, 0f);

                nav.turnTo(270);
                getFoundation(blue, startBuilding);

                getBlueSkystone(skystonePos);

                nav.turnTo(180);

                moac.intake.stopIntake();
                moac.stacker.close();

                //moac.linearSlide.horizPosition(-2050);
                //nav.backToY(112);
                int x = 0;
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > -1950) {
                    if (x == 0)
                        customizedForward(-(115 - nav.getY()), 1, .25, 48);
                    x++;
                }
                nav.IamAt(drive.rightDistance.getDistance(DistanceUnit.INCH) + 8, 110);

                moac.stacker.open();
                sleep(500);
                moac.stacker.close();

                moac.linearSlide.horizPosition(0);
                moac.linearSlide.lifterPosition(0);
                park();
                telemetry.addData("finish time", getRuntime());
                telemetry.update();
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

                drive.forward(8, 1);
            }
            //startLoading
            else {
                //red loading
                //************************************************
                nav.currPos(135, 36, 270);

                detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
                detector.enable();

                telemetry.addData("init finished", null);
                telemetry.update();

                waitForStart();
                //*****************************WAIT FOR START*******************************
                drive.forward(6, 1, .5);
                skystonePos = detector.getSkystonePos();

                detector.disable();

                telemetry.addData("Skystone Pos", detector.getPosition());
                telemetry.update();

                getRedSkystone(skystonePos);

                nav.turnTo(180);
                moac.intake.stopIntake();
                moac.stacker.close();

                nav.backToY(120);
                sleep(300);
                if (Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 12, 2))
                    drive.moveClose("back", 12, 1, 0f);

                nav.turnTo(90);
                getFoundation(blue, startBuilding);

                getRedSkystone(skystonePos);

                nav.turnTo(180);

                sleep(100);

                moac.intake.stopIntake();
                moac.stacker.close();

//                nav.backToY(110);
//                while()
//                drive.forwardPos(, .);

                int x = 0;
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > -1950) {
                    if (x == 0)
                        customizedForward(-(115 - nav.getY()), 1, .25, 42);
                    x++;
                }
                nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance) + 8, 110);
                moac.stacker.open();
                sleep(800);
                moac.stacker.close();

                moac.linearSlide.horizPosition(0);
                moac.linearSlide.lifterPosition(0);

                park();
                telemetry.addData("finish time", getRuntime());
                telemetry.update();
            }
        }
    }
    //*********************END OF PART THAT RUNS**************************

    //TODO: make park() work
    public void park() {
        if (!startBuilding) {
            if (left) {
                if (blue) nav.moveToY(72);
                else nav.moveToY(72);
                nav.turnTo(180);
            } else {
                if (blue) nav.moveToY(72);
                else nav.moveToY(72);
                nav.turnTo(180);
            }
        } else
            nav.backToY(72);
    }

    //TODO: test right case
    public void getBlueSkystone(SkystoneCVTest.Position pos) {
        int facingBlue = 90;
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    drive.strafeClose(blue, 36, 24);
                    nav.turnTo(facingBlue);

                    takeStone();

//
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7);

                    blueStones.remove(5);
                    round++;
                } else {
                    nav.moveToY(blueStones.get(2).y);

                    nav.turnTo(facingBlue);

                    drive.strafeClose(blue, 26, 14);


                    nav.turnTo(facingBlue);

                    takeStone();

//                    if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
//                        drive.moveClose("back", 25, 1, 0f);

                    //nav.IamAt(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7);

                    blueStones.remove(2);
                }
                break;

            case CENTER:
                if (round == 0) {
                    drive.strafeClose(blue, 25, 29);

                    nav.turnTo(facingBlue);//added to straighten out
                    takeStone();
//                    if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
//                        drive.moveClose("back", 25, 1, 0f);


                    //nav.IamAt(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7);
                    blueStones.remove(4);
                    round++;
                } else {
                    nav.moveToY(blueStones.get(1).y + 10);

                    nav.turnTo(facingBlue);

                    drive.strafeClose(blue, 26, 10);

                    nav.turnTo(facingBlue);//added to straighten out

                    takeStone();
//                    if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
//                        drive.moveClose("back", 25, 1, 0f);


                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance));

                    blueStones.remove(1);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    drive.strafeClose(blue, 26, 20);

                    nav.turnTo(facingBlue);//added to straighten out. May or may not be necessary

                    takeStone();

//                    if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
//                        drive.moveClose("back", 25, 1, 0f);

                    //nav.IamAt(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.rightDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance));

                    blueStones.remove(3);
                    round++;
                } else {
                    nav.moveTo(nav.getX(), blueStones.get(0).y + 25);

                    nav.turnTo(180);
                    sleep(150);

                    drive.moveClose("front", 15, .8, 0f);//moves to within 6 inches from the right wall

                    nav.turnTo(180);

                    drive.moveClose("right", 38, .7, 2f);
                    nav.turnTo(180);
                    takeStoneAgainstWall();

                    drive.moveClose("right", 25, .7, 1.5f);

                    nav.turnTo(180);

//                    if (!Operations.approximatelyEquals(drive.rightDistance.getDistance(DistanceUnit.INCH), 25, 1))
//                        drive.moveClose("right", 25, .7, 1f);

                    //nav.IamAt(drive.rightDistance.getDistance(DistanceUnit.INCH)+7, drive.frontDistance.getDistance(DistanceUnit.INCH) + 6);
                    nav.IamAt(drive.backDistance.getDistance(DistanceUnit.INCH) + 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 6);

                    blueStones.remove(1);
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
                    drive.moveClose("left", 19, .6, 2f);
                    drive.moveClose("left", 19, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 27, .6, 0f);

                    takeStone();

                    nav.turnTo(facingRed);

//                    if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
//                        drive.moveClose("back", 25, 1, 0f);

                    //nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7);

                    redStones.remove(5);

                    round++;
                } else {
                    nav.moveToY(redStones.get(2).y + 20);

                    sleep(500);

                    nav.turnTo(180);

                    drive.moveClose("front", 14, .8, 0f);

                    drive.moveClose("left", 38, .6, 2f);
                    nav.turnTo(180);
                    takeStoneAgainstWall();

                    drive.moveClose("left", 25, .6, 1.5f);

                    nav.turnTo(180);

//                    if (!Operations.approximatelyEquals(drive.leftDistance.getDistance(DistanceUnit.INCH), 25, 1.5)) {
//                        drive.moveClose("left", 25, 1, 0f);
//                        nav.turnTo(180);
//                    }

                    //nav.IamAt(144 - drive.leftDistance.getDistance(DistanceUnit.INCH) - 7, drive.frontDistance.getDistance(DistanceUnit.INCH) + 6);
                    nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.getAccurateDistanceSensorReading(drive.frontDistance) + 7);

                    redStones.remove(2);
                }
                break;

            case CENTER:
                if (round == 0) {
                    drive.moveClose("left", 26, .6, 2f);
                    drive.moveClose("left", 26, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .6, 3f);

//                    moac.intake.takeStone(drive,nav,this);
                    takeStone();
                    nav.turnTo(facingRed);

//                    if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
//                        drive.moveClose("back", 25, 1, 0f);

                    //nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7);

                    redStones.remove(4);

                    round++;
                } else {
//                    nav.backToY(redStones.get(1).y + 12, .8);
                    nav.moveTo(nav.getX(), redStones.get(1).y + 10);

                    moac.intake.stopIntake();

                    nav.turnTo(facingRed);

                    drive.moveClose("left", 5.5, .6, 2f);
                    drive.moveClose("left", 5.5, .4, 2f);

                    nav.turnTo(facingRed);

                    drive.moveClose("back", 28, .6, 3f);

                    takeStone();

                    nav.turnTo(facingRed);

//                    if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
//                        drive.moveClose("back", 25, 1, 0f);

                    //nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7);

                    redStones.remove(1);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    if (useDistanceSensors) {
                        drive.moveClose("left", 36, .6, 2f);
                        drive.moveClose("left", 36, .4, 2f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 26, 1, 3f);
                    } else {//lol this doesnt even work
                        nav.moveToY(36);
                        nav.moveToX(118);
                    }

                    nav.turnTo(facingRed);

                    takeStone();

                    /*if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
                        drive.moveClose("back", 25, 1, 0f);*/

                    //nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7);

                    redStones.remove(3);

                    round++;
                } else {
                    nav.moveTo(nav.getX() + 5, redStones.get(0).y + 15);

                    moac.intake.stopIntake();

                    nav.turnTo(facingRed);
                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    if (useDistanceSensors) {
                        drive.moveClose("left", 9, .6, 1.5f);
                        drive.moveClose("left", 9, .4, 1.5f);

                        nav.turnTo(facingRed);

                        drive.moveClose("back", 26, .6, 0f);
                    } else {
                        nav.moveToY(15.5);
                        nav.moveToX(118);
                    }

                    takeStone();

                    nav.turnTo(facingRed);

                    /*if (!Operations.approximatelyEquals(drive.backDistance.getDistance(DistanceUnit.INCH), 25, 1.5))
                        drive.moveClose("back", 25, 1, 0f);*/

                    //nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.leftDistance.getDistance(DistanceUnit.INCH) + 7);
                    nav.IamAt(144 - drive.backDistance.getDistance(DistanceUnit.INCH) - 7, drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7);

                    redStones.remove(0);
                }
                break;
            default:
                break;
        }
        round++;
    }

    public void takeStone(double backDistance, boolean blue) {
        final double DISTANCE = 20;
        moac.stacker.open();
        moac.intake.takeIn();
        drive.forward(DISTANCE, .8, .5);
        nav.turnTo(90);

        nav.arc(180, 1, .5, -.6);

        //drive.moveClose("back", backDistance, 1, 0f);
    }

    public void takeStone() {
        takeStone(25, blue);
    }

    public void drop() {
        while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
            moac.linearSlide.lifterPosition(400);
            moac.linearSlide.horizPosition(-2050);
        }
        moac.stacker.open();
        sleep(150);
        moac.stacker.close();
        while (moac.linearSlide.slideHoriz.getCurrentPosition() < -100 && moac.linearSlide.slideVertical.getCurrentPosition() > 30) {
            moac.linearSlide.lifterPosition(0);
            moac.linearSlide.horizPosition(0);
        }
    }

    public void takeStoneAgainstWall() {
        moac.intake.takeIn();
        moac.stacker.open();
        drive.forward(8, 0.6);
        drive.forward(-8, 1);

    }

    //TODO: test the method
    public void getFoundation(boolean blue, boolean startBuilding) {
        if (blue) {
            if (startBuilding) {
                /*
                nav.moveToX(nav.getX() + 5);
                drive.moveClose("left", 20, .4, 2);
                nav.turnTo(270);
                nav.backToX(40);
                moac.foundationGrabber.grabFoundation(false);
                sleep(500);
                nav.moveToX(16);
                moac.foundationGrabber.grabFoundation(true);
                */
            } else {
                //drive.moveClose("back", -1, 1, 1f);

                drive.moveClose("front", 32, .8, 0f);
                moac.foundationGrabber.grabFoundation(true);
                nav.IamAt(40 - drive.backDistance.getDistance(DistanceUnit.INCH), 136 - drive.rightDistance.getDistance(DistanceUnit.INCH));
                //drive.forward(-2,1,.7);
                sleep(500);
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                    moac.linearSlide.lifterPosition(500);
                    moac.linearSlide.horizPosition(-2050);
                    nav.arc(180, 5, 1, .7);
                }
                moac.stacker.open();
                sleep(150);
                moac.stacker.close();
                moac.linearSlide.lifterPosition(0);
                moac.linearSlide.horizPosition(0);
                double curTime = getRuntime();
                while (getRuntime() < curTime + .6)
                    drive.libertyDrive(-.6, 0, 0);
                drive.stopDriving();
                moac.foundationGrabber.grabFoundation(false);

//                if (!Operations.approximatelyEquals(drive.rightDistance.getDistance(DistanceUnit.INCH), 26, 2))
//                    drive.moveClose("right", 25, .5, 1f);

                nav.turnTo(180);

                nav.IamAt(drive.rightDistance.getDistance(DistanceUnit.INCH) + 8, 117);
            }
        } else {
            if (startBuilding) {
                /*
                nav.moveToX(nav.getX() - 5);
                drive.moveClose("right", 20, .4, 2);
                nav.turnTo(90);
                nav.backToX(104);
                moac.foundationGrabber.grabFoundation(false);
                sleep(500);
                nav.moveToX(128);
                moac.foundationGrabber.grabFoundation(true);
                */
            } else {
                //drive.moveClose("back", -1, 1, 0f);

                drive.moveClose("front", 32, .7, 0f);
                moac.foundationGrabber.grabFoundation(true);
                nav.IamAt(144 - drive.frontDistance.getDistance(DistanceUnit.INCH) - 6, 136 - drive.leftDistance.getDistance(DistanceUnit.INCH));
                //drive.forward(-2,1,.7);
                sleep(500);
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > -2000 && moac.linearSlide.slideVertical.getCurrentPosition() < 490) {
                    moac.linearSlide.lifterPosition(500);
                    moac.linearSlide.horizPosition(-2050);
                    nav.arc(180, 5, 1, .6);
                }
                moac.stacker.open();
                sleep(200);
                moac.stacker.close();
                moac.linearSlide.lifterPosition(0);
                moac.linearSlide.horizPosition(0);

                double curTime = getRuntime();
                while (getRuntime() < curTime + .5)
                    drive.libertyDrive(-.6, 0, 0);
                drive.stopDriving();

                moac.foundationGrabber.grabFoundation(false);

//                if (!Operations.approximatelyEquals(drive.leftDistance.getDistance(DistanceUnit.INCH), 24, 2))
//                    drive.moveClose("left", 24, .5, 1f);

                nav.turnTo(180);

                nav.IamAt(drive.leftDistance.getDistance(DistanceUnit.INCH) + 8, 117);
            }
        }
    }

    public void customizedForward(double inches, double power, double powerFloor, double value) {
        int clicks = (int) (inches * drive.CLICKS_PER_INCH);
        drive.stopDriving();
        drive.setAllDrivingPositions(clicks);
        drive.setDrivingModes(DcMotor.RunMode.RUN_TO_POSITION);
        while (drive.linearOpMode.opModeIsActive() && drive.motorsBusy()) {
            drive.linearOpMode.idle();
            double factor = Operations.chooseAlgorithm(Operations.AccelerationAlgorithms.EXPONENTIAL, Operations.DeccelerationAlgorithms.PARABOLIC, clicks, drive.averageEncoderPositions());
            if (factor > 1) factor = 1;
            double newPower = power * factor;
            drive.setAllDrivingPowers(Math.max(newPower, powerFloor));
            if (Math.abs(clicks - drive.averageEncoderPositions()) < (value * drive.CLICKS_PER_INCH)) {
                moac.linearSlide.horizPosition(-2050);
                moac.linearSlide.lifterPosition(600);
            }
        }
        drive.stopDriving();
    }

    class Stones {
        public int x, y;

        Stones(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}




