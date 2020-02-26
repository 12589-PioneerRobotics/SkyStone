package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;


public class AutonomousCore {
    public void setBlue(boolean blue) {
        this.blue = blue;
    }

    public boolean blue;
    public boolean startBuilding;
    public boolean left;
    public boolean grabFoundation = false;
    public boolean useDistanceSensors = true;

    public int round = 0;
    public SkystoneCVTest detector;
    public Driving drive;
    public SkystoneCVTest.Position skystonePos;
    public Navigation nav;
    public MoacV_2 moac;


    public AutonomousCore(LinearOpMode opMode, boolean blue, boolean startBuilding, boolean left) {

        this.blue = blue;
        this.left = left;
        this.startBuilding = startBuilding;


        drive = new Driving(opMode);
        nav = new Navigation(drive);
        moac = new MoacV_2(opMode.hardwareMap, this.blue);
        detector = new SkystoneCVTest();
        detector.changeCrop(this.blue);
    }


    void takeStoneAgainstWall() {
        moac.intake.takeIn();
        moac.stacker.open();
        drive.forward(8, 0.6);
        drive.forward(-8, 1);

    }

    public void takeStone() {
        final double DISTANCE = 20;
        moac.stacker.open();
        moac.intake.takeIn();
        drive.forward(DISTANCE, .8, .5);
        drive.forward(-15, .8, .5);
        nav.arc(180, 3, .6, -.6);
    }

    public class Stones {
        public int x, y;

        public Stones(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}

interface AutonomousBase {

    void park();

    void getSkystone(SkystoneCVTest.Position pos);

    void getFoundation();

}
