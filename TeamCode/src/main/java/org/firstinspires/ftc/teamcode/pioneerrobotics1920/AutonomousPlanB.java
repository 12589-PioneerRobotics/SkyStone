package org.firstinspires.ftc.teamcode.pioneerrobotics1920;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;

@Autonomous(name = "AutonPlanB")
@Disabled
public class AutonomousPlanB extends LinearOpMode {
    final double angleLeft = 72.2553282;

    FoundationDetection foundationDetection;
    SkystoneCVTest detector;
    Driving drive;
    SkystoneCVTest.Position skystonePos;
    ServoControl servo;
    private int round = 1;
    //private int skyStoneY = 0;
    //final private int skyStoneRedX = 34;

    @Override
    public void runOpMode() throws InterruptedException {
        foundationDetection = new FoundationDetection();
        detector = new SkystoneCVTest();
        drive = new Driving(this);



        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.enable();


        waitForStart();

        skystonePos = detector.getSkystonePos();

        telemetry.addData("skystone Pos", detector.getPosition());
        telemetry.update();
        sleep(500);
//--------------------------------------------------
        getSkyStone(skystonePos);
        sleep(500);
        goToFoundation(SkystoneCVTest.Position.UNKNOWN);
    }

    public void getSkyStone(SkystoneCVTest.Position pos) {
        // backwards method used - phone facing the back ... nice job builders
        // not anymore, we switched the
        switch (pos) {
            case LEFT:
                //move to block, pick it up
                drive.turn(-90);
                drive.forward(12, .7);
                drive.turn(90);
                drive.forward(36, .7);
                getStones();
                break;
            case CENTER:
                //move clicks
                drive.forward(36, .7);
                getStones();
            case RIGHT:
                //move clicks
                drive.turn(90);
                drive.forward(12, .7);
                drive.turn(-90);
                drive.forward(36, .7);
                getStones();
            default:
                break;
        }
        round++;
    }

    public void goToFoundation(SkystoneCVTest.Position pos) {
        // backwards method used - phone facing the back ... nice job builders
        // not anymore, we switched the
        switch (pos) {
            case LEFT:
                //move to block, pick it up
                drive.forward(-36, .7);
                drive.turn(-90);
                drive.forward(72,.7);
                drive.turn(90);
                drive.forward(24, .7);
                dropStones();
                drive.forward(-24, .7);
                drive.turn(90);
                drive.forward(48, .7);
                break;
            case CENTER:
                //move clicks
                drive.forward(-36, .7);
                drive.turn(-90);
                drive.forward(84,.7);
                drive.turn(90);
                drive.forward(24, .7);
                dropStones();
                drive.forward(-24, .7);
                drive.turn(90);
                drive.forward(48, .7);
            case RIGHT:
                //move clicks
                drive.forward(-36, .7);
                drive.turn(-90);
                drive.forward(96,.7);
                drive.turn(90);
                drive.forward(24, .7);
                dropStones();
                drive.forward(-24, .7);
                drive.turn(90);
                drive.forward(48, .7);
            default:
                break;
        }
        round++;
    }


    public void getStones(){

    }

    public void dropStones(){

    }

}

