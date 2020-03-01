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
    int count = 0;

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
                drive.forward(10,.5);

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

                drive.smoothTimeBasedForward(.4, .5);
                getBlueSkystone(skystonePos);

                //next movement


                nav.backToY(113);

                moac.intake.stopIntake();
                moac.stacker.close();
                nav.turnTo(270, .5);
                getFoundation(blue, startBuilding);

                getBlueSkystone(skystonePos);

                int x = 0;
                while (moac.linearSlide.slideHoriz.getCurrentPosition() < 1100) {
                    if (x == 0)
                        customizedForward(-(113 - nav.getY()), 1, .25, 48);
                    x++;
                }
                nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 8, 110);

                moac.stacker.open();
                sleep(500);
                moac.stacker.close();

                moac.linearSlide.horizPosition(0);
                moac.linearSlide.lifterPosition(0);
                /*getBlueSkystone(skystonePos);

                x = 0;
                while (moac.linearSlide.slideHoriz.getCurrentPosition() > -1950) {
                    if (x == 0)
                        customizedForward(-(115 - nav.getY()), 1, .25, 48);
                    x++;
                }
                nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 8, 110);

                moac.stacker.open();
                sleep(500);
                moac.stacker.close();

                moac.linearSlide.horizPosition(0);
                moac.linearSlide.lifterPosition(0);*/
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
                skystonePos = detector.getSkystonePos();
                telemetry.addData("Skystone Pos", detector.getPosition());
                telemetry.update();

                detector.disable();
                drive.smoothTimeBasedForward(.4, .5);




                getRedSkystone(skystonePos);



                nav.backToY(113);

                nav.turnTo(90);
                getFoundation(blue, startBuilding);

                getRedSkystone(skystonePos);


                int x = 0;
                while (moac.linearSlide.slideHoriz.getCurrentPosition() < 1100) {
                    if (x == 0)
                        customizedForward(-(113 - nav.getY()), 1, .25, 42);
                    x++;
                }
                nav.IamAt(137-drive.getAccurateDistanceSensorReading(drive.leftDistance), 110);
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
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    drive.strafeClose(true, false, 34, 26, 2, false);
                    takeStone();

                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, 66);
                } else if (round == 1){
                    drive.smoothTimeBasedForward(1, .8);
                    drive.strafeClose(true, true, 40, 30, 2, false);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 40, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);

                } else {
                    drive.smoothTimeBasedForward(1, .7);
                    drive.strafeClose(true, true, 38, 25, 2);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 33, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);
                    moac.intake.stopIntake();
                    moac.stacker.close();
                }
                break;

            case CENTER:
                if (round == 0) {
                    drive.smoothTimeBasedForward(.4, .4);
                    takeStone();

                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, 59);

                } else if (round ==1){
                    drive.smoothTimeBasedForward(1.15, .8);
                    drive.strafeClose(true, true, 40, 24, 2);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 33, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);

                } else{
                    drive.smoothTimeBasedForward(1.4, 1);
                    drive.strafeClose(true, true, 38, 14, 2);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 28, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);
                    moac.intake.stopIntake();
                    moac.stacker.close();
                }
                break;

            case RIGHT:
                if (round == 0) {
                    drive.strafeClose(true, false, 20, 24, 1);
                    takeStone();

                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 7, 52);

                } else if (round == 1){
                    drive.smoothTimeBasedForward(1.4, .7);
                    drive.strafeClose(true, true, 40, 14, 2);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 28, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);

                }
                else {
                    drive.smoothTimeBasedForward(1.5, 1);
                    drive.strafeClose(true, true, 38, 48, 2);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 48, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);
                    moac.intake.stopIntake();
                    moac.stacker.close();
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
                    drive.strafeClose(false, false, 20, 24, 1);
                    takeStone();

                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7, 52);

                } else if (round==1) {
                    drive.smoothTimeBasedForward(1.4, .7);
                    drive.strafeClose(false, true, 40, 14, 2);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 28, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);
                }
                break;

            case CENTER:
                if (round == 0) {
                    drive.smoothTimeBasedForward(.4, .4);
                    takeStone();

                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7, 59);

                } else if (round == 1){
//                    nav.backToY(redStones.get(1).y + 12, .8);
                    drive.smoothTimeBasedForward(1.15, .8);
                    drive.strafeClose(false, true, 40, 24, 2);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 33, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);
                }
                break;

            case RIGHT:
                if (round == 0) {
                    drive.strafeClose(true, false, 34, 26, 2, false);
                    takeStone();

                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance) + 7, 66);

                } else if (round ==1){
                    drive.smoothTimeBasedForward(1, .8);
                    drive.strafeClose(true, true, 40, 30, 2, false);
                    takeStoneAgainstWall();
                    drive.strafeClose(true, true, 24, 40, 2);
                    nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance)+7, drive.getAccurateDistanceSensorReading(drive.frontDistance)+7);
                }
                break;
            default:
                break;
        }
        round++;
    }

    public void takeStone(double backDistance, boolean blue) {
        moac.stacker.open();
        moac.intake.takeIn();
        while (!moac.intake.getStoneState()) {
            drive.indefiniteForward(.3);
        }
        drive.stopDriving();

        if (!Operations.approximatelyEquals(drive.getAccurateDistanceSensorReading(drive.backDistance), 45, 2))
            drive.moveClose("back", 45, 1, 0f);


        nav.arc(180, 1, .5, -.7);

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
        drive.stopDriving();
        while (!moac.intake.getStoneState()) {
            drive.indefiniteForward(.2);
        }

    }

    //TODO: test the method
    public void getFoundation(boolean blue, boolean startBuilding) {
        if (blue) {
            if (startBuilding) {

            } else {
                //drive.moveClose("back", -1, 1, 1f);
                drive.moveClose("front", 34, .25, 0f);
                moac.foundationGrabber.grabFoundation(true);
                sleep(250);
                while (moac.linearSlide.slideHoriz.getCurrentPosition() < 900) {
                    if (count == 0) {
                        moac.linearSlide.lifterPosition(300);
                        moac.linearSlide.horizPosition(1150);
                        drive.linearOpMode.telemetry.addData("Vert Slide Pos", moac.linearSlide.slideVertical.getCurrentPosition());

                        nav.arc(250, 1, 1, -.5);
                        drive.forward(5, 1);
                        nav.arc(180, 3, 1, .8);
                        count++;
                    }
                }
                moac.stacker.open();
                sleep(250);
                moac.stacker.close();
                moac.linearSlide.lifterPosition(0);
                moac.linearSlide.horizPosition(0);
                double curTime = getRuntime();
                while (getRuntime() < curTime + .6)
                    drive.libertyDrive(-.6, 0, 0);
                drive.stopDriving();
                moac.foundationGrabber.grabFoundation(false);

                sleep(250);
                nav.turnTo(180);

                nav.IamAt(drive.getAccurateDistanceSensorReading(drive.rightDistance) + 8, 117);
            }
        } else {
            if (startBuilding) {

            } else {
                //drive.moveClose("back", -1, 1, 0f);

                drive.moveClose("front", 34, .25, 0f);
                moac.foundationGrabber.grabFoundation(true);

                sleep(250);

                while (moac.linearSlide.slideHoriz.getCurrentPosition() < 900) {
                    if (count == 0) {
                        moac.linearSlide.lifterPosition(300);
                        moac.linearSlide.horizPosition(1150);

                        nav.arc(110, 1, 1, -.5);
                        drive.forward(5, 1);
                        nav.arc(180, 3, 1, .8);
                        count++;
                    }
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
                sleep(250);

                nav.turnTo(180);

                nav.IamAt(drive.getAccurateDistanceSensorReading(drive.leftDistance) + 8, 117);
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
                moac.intake.stopIntake();
                moac.stacker.close();
                moac.linearSlide.horizPosition(1150);


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




