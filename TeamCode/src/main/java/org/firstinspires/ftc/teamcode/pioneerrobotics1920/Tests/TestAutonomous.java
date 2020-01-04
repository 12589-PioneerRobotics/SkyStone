package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@Autonomous(name = "Test Autonomous", group = "Test")
public class TestAutonomous extends LinearOpMode {
    MoacV_2 moac;
    private Driving driving;
    private Navigation nav;

    @Override
    public void runOpMode() throws InterruptedException {

        driving = new Driving(this);
        nav = new Navigation(driving);
        moac = new MoacV_2(this.hardwareMap);

        int[] distances = {6, 12, 18, 24, 30, 36, 48, 60}; //inches
        int[] angles = {0, 30, 90, 135, 180, 270, 330}; //actually angles

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

        nav.currPos(0, 0, 0);
        waitForStart();

        while (this.opModeIsActive()) {
                if (dpad_upOneShot.update(gamepad1.dpad_up) && curIndex < distances.length - 1)
                    curIndex++;
                if (dpad_downOneShot.update(gamepad1.dpad_down) && curIndex > 0)
                    curIndex--;
                if (dpad_downOneShot.update(gamepad1.dpad_right) && curIndex2 < angles.length - 1)
                    curIndex2++;
                if (dpad_downOneShot.update(gamepad1.dpad_left) && curIndex2 > 0)
                    curIndex2--;
                if (aOneShot.update(gamepad1.a)) {
                    driving.autoStrafe(distances[curIndex], 0.6);

                    //driving.moveClose("front", 8, .25, 3.5f);
                    //nav.turnToP(angles[curIndex2],0.75,0.000025);
                }
                if (aOneShot.update(gamepad1.b)) {
                    //driving.moveClose("front", 8, .25, 3.5f);
                    driving.autoStrafe(-distances[curIndex], 0.6);
                }
                if(aOneShot.update(gamepad1.x)) {
//                    nav.turnToP(angles[curIndex2],.75,0.00001);
                    nav.turnTo(angles[curIndex2],5);
                }
                if(aOneShot.update(gamepad1.y)) {
                    driving.forward(distances[curIndex],.2);
                }

                telemetry.addData("distance going", distances[curIndex]);
                //telemetry.addData("go to angle", angles[curIndex2]);
                //telemetry.addData("nav angle", nav.getAngle());
                telemetry.addData("Turn angle set to: ", angles[curIndex2]);
                telemetry.addData("Distance set to: ", distances[curIndex]);
                telemetry.addData("drive angle", driving.gyro.getValueContinuous());
                telemetry.update();
        }
    }

}
