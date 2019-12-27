package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@Autonomous(name = "Test Measurements", group = "Test")
public class TestAutonomous extends LinearOpMode {
    MoacV_2 moac;
    public Driving driving;
    Navigation nav;
    @Override
    public void runOpMode() throws InterruptedException {

        driving = new Driving(this);
        nav = new Navigation(driving);
        moac = new MoacV_2(this.hardwareMap);


        int[] distances = {6, 12, 18, 24, 30, 36}; //inches
        double[] angles = {0,30,90,135,180,270,330}; //actually angles

        //Coordinates[] coordinates = {new Coordinates(0,0), new Coordinates(0,12), new Coordinates(12,12), new Coordinates(12,0)};
        double power = 0.6;

        int curIndex = 0;
        int curIndex2 = 0;
        int count = 0;
        telemetry.addData("Make sure to set CLICKS_PER_INCH to 1", null);
        telemetry.update();
        Toggle.OneShot dpad_upOneShot = new Toggle.OneShot();
        Toggle.OneShot dpad_downOneShot = new Toggle.OneShot();
        Toggle.OneShot aOneShot = new Toggle.OneShot();

        nav.currPos(0,0,0);
        waitForStart();


        while(this.opModeIsActive()) {
            if(count == 0) {
                if (dpad_upOneShot.update(gamepad1.dpad_up) && curIndex < distances.length - 1)
                    curIndex++;
                if (dpad_downOneShot.update(gamepad1.dpad_down) && curIndex > 0)
                    curIndex--;
                if (dpad_upOneShot.update(gamepad1.dpad_right) && curIndex2 < angles.length - 1)
                    curIndex2++;
                if (dpad_downOneShot.update(gamepad1.dpad_left) && curIndex2 > 0)
                    curIndex2--;
                if (aOneShot.update(gamepad1.a)) {
                    driving.autoStrafe(distances[curIndex],0.6);
                    //driving.moveClose("front", 8, .25, 3.5f);
                    //nav.turnToP(angles[curIndex2],0.75,0.000025);
                }
                if (aOneShot.update(gamepad1.b)) {
                    //driving.moveClose("front", 8, .25, 3.5f);
                    driving.autoStrafe(-distances[curIndex], 0.6);
                }
                /*
                if (aOneShot.update(gamepad1.b))
                    driving.moveClose("back", 12,.25,3.5f);
                if (aOneShot.update(gamepad1.x))
                    driving.moveClose("right",8,.45,3.5f);
                if (aOneShot.update(gamepad1.y))
                    driving.moveClose("left",8,.45,3.5f);
                    */
//                telemetry.addData("a: front; b: back; x: right; y: left;", driving.frontDistance.getDistance(DistanceUnit.INCH) +
//                        ", " + driving.backDistance.getDistance(DistanceUnit.INCH) + ", " + driving.rightDistance.getDistance(DistanceUnit.INCH) +
//                        ", " + driving.leftDistance.getDistance(DistanceUnit.INCH));
                //telemetry.addData("a: front", driving.frontDistance.getDistance(DistanceUnit.INCH));
                //telemetry.addData("b: back", driving.backDistance.getDistance(DistanceUnit.INCH));
                //telemetry.addData("x: right", driving.rightDistance.getDistance(DistanceUnit.INCH));
                //telemetry.addData("y: left", driving.leftDistance.getDistance(DistanceUnit.INCH));
                telemetry.addData("distance going", distances[curIndex]);
                //telemetry.addData("go to angle", angles[curIndex2]);
                //telemetry.addData("nav angle", nav.getAngle());
                telemetry.addData("drive angle", driving.gyro.getValueContinuous());
            }
            telemetry.update();
        }
    }

}
