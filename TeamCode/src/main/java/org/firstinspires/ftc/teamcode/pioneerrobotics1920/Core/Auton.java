package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.FoundationDetection;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.SkystoneCVTest;

import java.util.ArrayList;

@Autonomous(name = "Auton blue loading")

public class Auton extends LinearOpMode {
    //instantiations
    //final double angleLeft = 72.2553282;

    public boolean blue = true;
    public boolean startBuilding = false;
    public boolean left = true;
    private int round = 0;
    private int gap = 5;


    FoundationDetection foundationDetection;
    //ServoControl servo;
    SkystoneCVTest detector;
    Driving drive;
    SkystoneCVTest.Position skystonePos;
    Navigation nav;
    //MoacV_2 moac;
    ArrayList<Stones> blueStones = new ArrayList<>();
    ArrayList<Stones> redStones = new ArrayList<>();


    @Override
    public void runOpMode() throws InterruptedException {
        //servo = new ServoControl(this);
        foundationDetection = new FoundationDetection();
        detector = new SkystoneCVTest();
        detector.changeCrop(blue);
        drive = new Driving(this);
        nav = new Navigation(drive);

        //moac = new MoacV_2(true, this.hardwareMap);

        //all coordinate positions of skystones on blue side
        blueStones.add(new Stones(37,8));
        blueStones.add(new Stones(37,12));
        blueStones.add(new Stones(37,24));
        blueStones.add(new Stones(37,32));
        blueStones.add(new Stones(37,40));
        blueStones.add(new Stones(37,48));
        //red: left = 5, center = 4, right = 3
        //all coordinate positions of skystones on red side
        redStones.add(new Stones(110,21)); //Change the values so that we align the collector with the blocks for red like we did in blue
        redStones.add(new Stones(110,12));
        redStones.add(new Stones(110,8));
        redStones.add(new Stones(110,48)); //right
        redStones.add(new Stones(110,40)); //center
        redStones.add(new Stones(110,32)); //left




        //servo.setServo(ServoControl.SERVOS.PIVOT,);

        //*******************THE PART THAT RUNS*************************
        if (blue) {
            if (startBuilding){
                nav.currPos(9,108, 270);

                while(!drive.gyro.isGyroReady()) {
                    telemetry.addData("Sensors not calibrated", null);
                    telemetry.update();
                }
                if(drive.gyro.isGyroReady()) {
                    telemetry.addData("Sensors calibrated", null);
                }
                telemetry.update();

                waitForStart();

                nav.backToX(14);
                park();
            }
            else {
                //start blue loading autonomous
                nav.currPos(9, 36, 270);

                detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
                detector.enable();

                while(!drive.gyro.isGyroReady()) {
                    telemetry.addData("Sensors not calibrated", null);
                    telemetry.update();
                }

                if(drive.gyro.isGyroReady()) {
                    telemetry.addData("Sensors calibrated", null);
                }
                telemetry.update();

                waitForStart();

//                detector.disable();

                //moac.autoGrab.blueInitialize();

                skystonePos = detector.getSkystonePos();

                detector.disable();

                telemetry.addData("skystone Pos", detector.getPosition());
                telemetry.update();

                //first movement
                nav.backToX(12);

                //detect skystone
                getBlueSkystone(skystonePos);

                //next movement
                nav.moveToX(9);

                telemetry.addData("getBlueSkystone", null);
                telemetry.update();
                nav.moveTo(9,85);

                drive.sleep(1000);
                getBlueSkystone(skystonePos);
                nav.moveToX(12);
                park();

            }
        }
        else { //starting red
            if (startBuilding){
                nav.currPos(135,108,90);

                telemetry.addData("init finished", null);
                telemetry.update();

                waitForStart();

                nav.backToX(130);
                park();
            }
            else {
                //red loading
                nav.currPos(135, 36, 90);


                while(!drive.gyro.isGyroReady()) {
                    telemetry.addData("Sensors not calibrated", null);
                    telemetry.update();
                }
                if(drive.gyro.isGyroReady()) {
                    telemetry.addData("Sensors calibrated", null);
                }
                telemetry.update();

                detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
                detector.enable();

                telemetry.addData("init finished", null);
                telemetry.update();
                waitForStart();

                skystonePos = detector.getSkystonePos();

                detector.disable();

                telemetry.addData("Skystone Pos", detector.getPosition());
                telemetry.update();

                nav.backToX(126);

                getRedSkystone(skystonePos);
                nav.moveToX(125);
                telemetry.addData("getRedSkystone", null);
                telemetry.update();
                nav.moveToY(85);
                nav.backTo(125,28.9);
                getRedSkystone(skystonePos); //
                nav.moveToX(122);
                park();

            }
        }
    }

    //*********************END OF PART THAT RUNS**************************

    public void park(){
        if (!startBuilding) {
            if(left) {
                if (blue) nav.moveTo(15, 72);
                else nav.moveTo(120, 72);
            }
            else{
                if(blue) nav.moveTo(36, 72);
                else nav.moveTo(110, 72);
            }
        }
        else
            nav.moveToY(72);

    }

    public void goToFoundationBlue() {
        if (round == 1) {
            nav.backTo(nav.getX(), 66);
        }
        nav.backTo(30,80);
        // turn on camera for foundation detection -> detect foundation
        nav.backTo(30, 90); // replace with where detection is
        //moac.autoGrab.blueOpen();
        // drop off the block on the foundation

    }

    public void getBlueSkystone(SkystoneCVTest.Position pos) {
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    //moac.autoGrab.blueArmDown();
                    //nav.backTo(blueStones.get(5).x - gap, blueStones.get(5).y);
                    drive.moveClose("left", 37.4, .35, 3.5f);//move parallel to the block

                    nav.turnTo(270, 1);//front faces the block

                    drive.moveClose("front", 26, .25,3.5f);//move up to the block.

                    nav.currPos(drive.frontDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(5).y, 270);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    blueStones.remove(5);
                    round++;
                } else {
                    //nav.moveTo(blueStones.get(2).x-gap, blueStones.get(2).y);
                    nav.backToY(blueStones.get(2).y);

//                    nav.turnTo(270, 1);
                    sleep(200);

                    drive.moveClose("left", 14.6, .45, 3.5f);

                    drive.moveClose("front", 26, .4,3.5f);
                    nav.turnTo(270);

                    nav.currPos(drive.frontDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(2).y, 270);

                    blueStones.remove(2);
                }break;

            case CENTER:
                if (round == 0) {
                    //nav.backTo(blueStones.get(4).x-gap, blueStones.get(4).y);
                    drive.moveClose("left", 28.7, .35, 3.5f);

                    nav.turnTo(270, 1);
                    //moac.autoGrab.blueArmDown();

                    drive.moveClose("front", 26, .25,3.5f);
                    nav.turnTo(270);

                    nav.currPos(drive.frontDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(4).y, 270);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    blueStones.remove(4);
                    round++;
                } else {
                    //nav.moveTo(blueStones.get(1).x-gap, blueStones.get(1).y);
                    nav.backToY(blueStones.get(1).y);

//                    nav.turnTo(270,1);
                    sleep(200);

                    drive.moveClose("left", 7.8,.45, 3.5f);

                    drive.moveClose("front", 26, .4,3.5f);
                    nav.turnTo(270);

                    nav.currPos(drive.frontDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(1).y, 270);

                    blueStones.remove(1);
                }break;

            case RIGHT:
                if (round == 0) {
                    //nav.backTo(blueStones.get(3).x-gap, blueStones.get(3).y);
                    drive.moveClose("left", 21.6, .35, 3.5f);
                    nav.turnTo(270, 1);
                    //moac.autoGrab.blueArmDown();

                    drive.moveClose("front", 26, .25,3);
                    nav.turnTo(270);

                    nav.currPos(drive.frontDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(3).y, 270);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    blueStones.remove(3);
                    round++;
                } else {
                    //nav.moveTo(blueStones.get(0).x-gap, blueStones.get(0).y);
                    nav.backToY(blueStones.get(0).y);

//                    nav.turnTo(270, 1);
                    sleep(200);

                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    drive.moveClose("front", 26, .4, 3);
                    nav.turnTo(270);

                    nav.currPos(drive.frontDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(0).y, 270);

                    blueStones.remove(0);
                }break;
            default: break;
        }
        round++;
    }

    public void getRedSkystone(SkystoneCVTest.Position pos) {
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    //moac.autoGrab.blueArmDown();
                    //nav.backTo(redStones.get(5).x - gap, redStones.get(5).y);
                    drive.moveClose("right", 21.6, .35, 3.5f);

                    nav.turnTo(90, 1);

                    drive.moveClose("front", 26, .25,3.5f);

                    nav.currPos(144 - drive.frontDistance.getDistance(DistanceUnit.INCH), redStones.get(5).y, 90);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    redStones.remove(5);
                    round++;
                } else {
                    //nav.moveTo(redStones.get(2).x-gap, redStones.get(2).y);
                    nav.moveToY(redStones.get(2).y);

                    nav.turnTo(90, 1);

                    drive.moveClose("front", 26, .4,3.5f);
                    nav.turnTo(90);

                    sleep(1000);

                    nav.currPos(144- drive.frontDistance.getDistance(DistanceUnit.INCH), redStones.get(2).y, 90);

                    redStones.remove(2);
                }break;

            case CENTER:
                if (round == 0) {
                    //nav.backTo(redStones.get(4).x-gap, redStones.get(4).y);
                    drive.moveClose("right", 28.7, .35, 3.5f);

                    nav.turnTo(90, 1);
                    //moac.autoGrab.blueArmDown();

                    drive.moveClose("front", 26, .25,3.5f);
                    nav.turnTo(90);

                    nav.currPos(144-drive.frontDistance.getDistance(DistanceUnit.INCH), redStones.get(4).y, 90);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    redStones.remove(4);
                    round++;
                } else {
                    //nav.moveTo(redStones.get(1).x-gap, redStones.get(1).y);
                    nav.moveToY(redStones.get(1).y);

                    nav.turnTo(90,1);
                    sleep(200);

                    drive.moveClose("right", 7.8,.45, 3.5f);

                    drive.moveClose("front", 26, .4,3.5f);
                    nav.turnTo(90);

                    sleep(1000);

                    nav.currPos(144 - drive.frontDistance.getDistance(DistanceUnit.INCH), redStones.get(1).y, 90);

                    redStones.remove(1);
                }break;

            case RIGHT:
                if (round == 0) {
                    //nav.backTo(redStones.get(3).x-gap, redStones.get(3).y);
                    drive.moveClose("right", 37.4, .35, 3.5f);
                    nav.turnTo(90, 1);
                    //moac.autoGrab.blueArmDown();

                    drive.moveClose("front", 26, .25,3);
                    nav.turnTo(90);

                    nav.currPos(144 - drive.frontDistance.getDistance(DistanceUnit.INCH), redStones.get(3).y, 90);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    redStones.remove(3);
                    round++;
                } else {
                    //nav.moveTo(redStones.get(0).x-gap, redStones.get(0).y);
                    nav.backToY(redStones.get(0).y);

                    nav.turnTo(90, 1);
                    sleep(200);

                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    drive.moveClose("right", 14.4, .35, 3.5f);
//                    nav.turnTo(90);
                    drive.moveClose("front", 26, .25, 3);
                    nav.turnTo(90);

                    sleep(1000);

                    nav.currPos(144 - drive.frontDistance.getDistance(DistanceUnit.INCH), redStones.get(0).y, 90);

                    redStones.remove(0);
                }break;
            default: break;
        }
        round++;
    }


    public void getStones(){
        if (blue) {
            nav.backTo(blueStones.get(round - 2).x, blueStones.get(round - 2).y);
            nav.turnTo(270, 1);
        }
        round++;
    }
}


class Stones {
    public int x, y;
    public Stones (int x, int y) {
        this.x = x;
        this.y = y;
    }
}


