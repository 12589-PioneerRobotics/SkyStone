package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.SkystoneCVTest;

public interface AutonomousBase {

    void park();

    void getSkystone(SkystoneCVTest.Position pos);

    void getFoundation();

    class Stones {
        public int x, y;

        Stones(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class AutonomousCore {
        public boolean blue = true;
        public boolean startBuilding = false;
        public boolean left = true;
        public boolean grabFoundation = false;
        public boolean useDistanceSensors = true;

        public AutonomousCore(OpMode opMode) {
            drive = new Driving(opMode);
            nav = new Navigation(drive);
            detector.changeCrop(blue);
            moac = new MoacV_2(opMode.hardwareMap, blue);
            detector = new SkystoneCVTest();
        }

        public int round = 0;
        public SkystoneCVTest detector;
        public Driving drive;
        public SkystoneCVTest.Position skystonePos;
        public Navigation nav;
        public MoacV_2 moac;

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
            nav.arc(180, 3, .6, -.6);
        }
    }

}