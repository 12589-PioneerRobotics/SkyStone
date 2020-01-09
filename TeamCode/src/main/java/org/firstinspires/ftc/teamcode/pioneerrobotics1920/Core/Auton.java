package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.FoundationDetection;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

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
    MoacV_2 moac;

    FoundationDetection foundationDetection;
    SkystoneCVTest detector;
    Driving drive;
    SkystoneCVTest.Position skystonePos;
    Navigation nav;
    ArrayList<Stones> blueStones = new ArrayList<>();
    ArrayList<Stones> redStones = new ArrayList<>();

    @Override
    public void runOpMode() throws InterruptedException {
        foundationDetection = new FoundationDetection(blue);
        foundationDetection = new FoundationDetection(blue);
        detector = new SkystoneCVTest();
        detector.changeCrop(blue);
        drive = new Driving(this);
        nav = new Navigation(drive);
        moac = new MoacV_2(this.hardwareMap);

        //all coordinate positions of skystones on blue side
        blueStones.add(new Stones(37,8));
        blueStones.add(new Stones(37,15
        ));
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

        while(!drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors not calibrated", null);
            telemetry.update();
        }

        if(drive.gyro.isGyroReady()) {
            telemetry.addData("Sensors calibrated", null);
        }
        telemetry.update();

        //*******************THE PART THAT RUNS*************************
        if (blue) {
            if (startBuilding){
                nav.currPos(9,108, 90);

                waitForStart();

                nav.moveToX(14,0.5);
                park();
            }
            else {
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
                nav.moveToX(14,0.6);

                //detect skystone, includes all movements for getting stones
                getBlueSkystone(skystonePos);

                nav.backToX(20);
                nav.turnTo(0);

                moac.intake.spitOut();
                sleep(300);
                moac.intake.stopIntake();


                //next movement
                //nav.backToX(12);
                nav.moveTo(20,85);

                moac.intake.spitOut();
                sleep(500);
                moac.intake.stopIntake();

                drive.sleep(500);
                getBlueSkystone(skystonePos);
                nav.backToX(20);
                nav.turnTo(0);
                moac.intake.spitOut();
                sleep(500);
                moac.intake.stopIntake();
                nav.moveToY(90);
                moac.intake.spitOut();
                sleep(1000);
                moac.intake.stopIntake();
                park();
            }
        }
        else { //starting red
            if (startBuilding){
                nav.currPos(135,108,270);
                waitForStart();
                nav.moveToX(130, 0.5);
                park();
            }
            else {
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

                nav.moveToX(126,0.5);

                getRedSkystone(skystonePos);
                nav.backToX(125);

                nav.moveToY(85);

                getRedSkystone(skystonePos); //
                nav.backToX(125);
                park();
            }
        }
    }

    //*********************END OF PART THAT RUNS**************************

    public void park(){
        if (!startBuilding) {
            if(left) {
                nav.turnTo(180);
                if (blue) nav.moveTo(20, 80);
                else nav.moveTo(120, 72);

            }
            else{
                if(blue) nav.moveTo(36, 72);
                else nav.moveTo(110, 72);
                nav.turnTo(180);
            }
        }
        else
            nav.moveToY(72);
    }

    public void goToFoundationBlue() {
        if (round == 1) {
            nav.moveTo(nav.getX(), 66);
        }
        nav.moveTo(30,80);
        // turn on camera for foundation detection -> detect foundation
        nav.moveTo(30, 90); // replace with where detection is
        //moac.autoGrab.blueOpen();
        // drop off the block on the foundation
    }

    public void getBlueSkystone(SkystoneCVTest.Position pos) {
        int facingBlue = 90;
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    //moac.autoGrab.blueArmDown();
                    //nav.moveto(blueStones.get(5).x - gap, blueStones.get(5).y);
                    drive.moveClose("right", 36, .5, 2f);//move parallel to the block
                    nav.turnTo(facingBlue);

                    drive.moveClose("back", 28, .3,3.5f);//move up to the block.

                    takeStone();

                    //moac.intake.takeStone(drive,nav, this);

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH), blueStones.get(5).y, facingBlue);

                    //moac.autoGrab.blueClose();
                    blueStones.remove(5);
                    round++;
                } else {
                    //nav.moveTo(blueStones.get(2).x-gap, blueStones.get(2).y);
                    nav.backToY(blueStones.get(2).y+10,1);
                    nav.turnTo(facingBlue);

                    drive.moveClose("right", 15, .4, 2f);

                    drive.moveClose("back", 30, .4,3.5f);

                    nav.turnTo(facingBlue);

                    takeStone();

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH), blueStones.get(2).y, facingBlue);
                    blueStones.remove(2);

                }break;

            case CENTER:
                if (round == 0) {
                    //nav.moveto(blueStones.get(4).x-gap, blueStones.get(4).y);
                    drive.moveClose("right", 28, .4, 2f);
                    //moac.autoGrab.blueArmDown();

                    drive.moveClose("back", 26, .25,3.5f);

                    moac.intake.takeStone(drive, nav, this);

                    nav.turnTo(facingBlue);
                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(4).y, facingBlue);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    blueStones.remove(4);
                    round++;
                } else {
                    //nav.moveTo(blueStones.get(1).x-gap, blueStones.get(1).y);
                    nav.backToY(blueStones.get(1).y,0.5);

                    nav.turnTo(facingBlue);
                    sleep(200);

                    drive.moveClose("right", 8.5,.4, 2f);

                    drive.moveClose("back", 26, .4,3.5f);

                    moac.intake.takeStone(drive, nav, this);

                    nav.turnTo(facingBlue);
                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(1).y, facingBlue);

                    blueStones.remove(1);
                }break;

            case RIGHT:
                if (round == 0) {
                    drive.moveClose("right", 22.5, .4, 2f);
                    nav.turnTo(facingBlue);
                    //moac.autoGrab.blueArmDown();

                    drive.moveClose("back", 26, .25,3);

                    moac.intake.takeStone(drive, nav, this);
                    nav.turnTo(facingBlue);

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH)+8, blueStones.get(3).y, facingBlue);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    blueStones.remove(3);
                    round++;
                } else {

                    nav.backToY(blueStones.get(0).y,0.5);
                    nav.turnTo(facingBlue);

                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    drive.moveClose("right", 21.6, .4, 3.5f);
                    nav.turnTo(facingBlue);
                    drive.moveClose("back", 26, .4, 3);

                    nav.turnTo(135);

                    moac.intake.takeStone(drive, nav, this);

                    nav.turnTo(facingBlue);

                    nav.currPos(drive.backDistance.getDistance(DistanceUnit.INCH) + 8, blueStones.get(0).y, facingBlue);//angle should be nav.getAngle()

                    blueStones.remove(0);
                }break;
            default: break;
        }
        round++;
    }

    public void getRedSkystone(SkystoneCVTest.Position pos) {
        int facingRed = 270;
        switch (pos) {
            case LEFT:
                if (round == 0) {
                    //moac.autoGrab.blueArmDown();
                    //nav.moveto(redStones.get(5).x - gap, redStones.get(5).y);
                    drive.moveClose("left", 21.6, .35, 3.5f);

                    nav.turnTo(facingRed, 1);

                    drive.moveClose("back", 26, .25,3.5f);
                    moac.intake.takeStone(drive,nav, this);
                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH), redStones.get(5).y, facingRed);

                    //moac.autoGrab.blueClose();
                    sleep(1000);

                    redStones.remove(5);

                    round++;
                } else {
                    //nav.moveTo(redStones.get(2).x-gap, redStones.get(2).y);
                    nav.backToY(redStones.get(2).y, 0.5);

                    nav.turnTo(facingRed);
                    drive.moveClose("left", 39,.4,3.5f);
                    nav.turnTo(facingRed );
                    drive.moveClose("back", 26, .4,3.5f);

                    moac.intake.takeStone(drive,nav,this);
                    nav.turnTo(facingRed);

                    nav.currPos(144- drive.backDistance.getDistance(DistanceUnit.INCH), redStones.get(2).y, facingRed);

                    redStones.remove(2);
                }break;

            case CENTER:
                if (round == 0) {
                    //nav.moveto(redStones.get(4).x-gap, redStones.get(4).y);
                    drive.moveClose("left", 28.7, .4, 3.5f);

                    nav.turnTo(facingRed, 1);
                    //moac.autoGrab.blueArmDown();

                    drive.moveClose("back", 26, .25,3.5f);

                    moac.intake.takeStone(drive,nav,this);
                    nav.turnTo(facingRed);

                    nav.currPos(144-drive.backDistance.getDistance(DistanceUnit.INCH), redStones.get(4).y, 90);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    redStones.remove(4);
                    round++;
                } else {
                    //nav.moveTo(redStones.get(1).x-gap, redStones.get(1).y);
                    nav.moveToY(redStones.get(1).y);

                    nav.turnTo(facingRed);
                    sleep(200);

                    drive.moveClose("left", 7.8,.4, 3.5f);

                    drive.moveClose("back", 26, .4,3.5f);
                    nav.turnTo(225);
                    moac.intake.takeStone(drive,nav,this);

                    sleep(1000);
                    nav.turnTo(facingRed);
                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH), redStones.get(1).y, 90);

                    redStones.remove(1);
                }break;

            case RIGHT:
                if (round == 0) {
                    //nav.moveto(redStones.get(3).x-gap, redStones.get(3).y);
                    drive.moveClose("left", 37.4, .4, 3.5f);
                    nav.turnTo(facingRed);

                    drive.moveClose("back", 26, .25,3);
                    nav.turnTo(facingRed);
                    moac.intake.takeStone(drive,nav,this);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH), redStones.get(3).y, 90);

                    //moac.autoGrab.blueClose();
                    sleep(1000);
                    redStones.remove(3);
                    round++;
                } else {
                    //nav.moveTo(redStones.get(0).x-gap, redStones.get(0).y);
                    nav.moveToY(redStones.get(0).y);

                    nav.turnTo(facingRed);
                    sleep(200);

                    //there is no moveClose strafe correction for this case because it is so close to the wall that the
                    //sensor may go through the transparent wall
                    drive.moveClose("left", 14.4, .4, 3.5f);
//                    nav.turnTo(90);
                    drive.moveClose("back", 26, .25, 3);
                    nav.turnTo(facingRed);
                    moac.intake.takeStone(drive,nav,this);
                    sleep(1000);

                    nav.currPos(144 - drive.backDistance.getDistance(DistanceUnit.INCH), redStones.get(0).y, facingRed);

                    redStones.remove(0);

                }break;
            default: break;
        }
        round++;
    }

    public void getFoundation(boolean blue){
        if (blue) {
            nav.moveToY(120);
            nav.turnTo(270);
            nav.backToX(40);
            moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            nav.moveToX(16);
            moac.foundationGrabber.grabFoundation(true);
        }
        else {
            nav.moveToY(120);
            nav.turnTo(90);
            nav.backToX(100);
            moac.foundationGrabber.grabFoundation(false);
            sleep(500);
            nav.moveToX(128);
            moac.foundationGrabber.grabFoundation(true);
        }
    }

    public void takeStone(){
        moac.intake.takeIn();
        nav.moveToX(nav.getX()+10);
        moac.intake.stopIntake();;

    }
}


class Stones {
    public int x, y;
    public Stones (int x, int y) {
        this.x = x;
        this.y = y;
    }
}


