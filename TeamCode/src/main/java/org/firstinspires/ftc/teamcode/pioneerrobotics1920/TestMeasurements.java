package org.firstinspires.ftc.teamcode.pioneerrobotics1920;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Coordinates;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;

@Autonomous(name = "Autonomous Test")
@Disabled
public class TestMeasurements extends LinearOpMode {

    Driving drive;
    Navigation navigation;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        navigation = new Navigation(drive);
        LinearSlideConfig linearSlide = new LinearSlideConfig(hardwareMap);

        int[] encoders = {100, 200, 300, 400, 500, 1000, 2000, 3000, 4000, 5000};
        Toggle.OneShot dpad_upOneShot = new Toggle.OneShot();
        Toggle.OneShot dpad_downOneShot = new Toggle.OneShot();
        Toggle.OneShot aOneShot = new Toggle.OneShot();

        int curIndex = 0;
        //int count = 0;

        Coordinates[] coordinates = {new Coordinates(9, 36),
                                     new Coordinates(34, 45),
                                     new Coordinates(34, 36),
                                     new Coordinates(34, 28)};

        double[] angles = {0, 90, 180, 270};

        navigation.currPos(9, 36, 270);

        waitForStart();

        while(this.opModeIsActive()) {
            /*if (count == 0) {
                if (dpad_upOneShot.update(gamepad1.dpad_up) && curIndex < coordinates.length - 1)
                    curIndex++;
                if (dpad_downOneShot.update(gamepad1.dpad_down) && curIndex > 0)
                    curIndex--;
                if (aOneShot.update(gamepad1.a)) {
                    navigation.backTo(coordinates[curIndex].x, coordinates[curIndex].y);
                }
                if (aOneShot.update(gamepad1.b)) {
                    navigation.moveTo(coordinates[curIndex].x, coordinates[curIndex].y);
                }
                telemetry.addData("Current location", "("+navigation.getX()+","+navigation.getY()+")");
                telemetry.addData("Location going to", coordinates[curIndex]);
                telemetry.addData("Angle turning to", angles[curIndex]);
                telemetry.addData("turn angle", navigation.turnAngle);
            }
            telemetry.update();*/
            /*if(count == 0) {
                if (dpad_upOneShot.update(gamepad1.dpad_up) && curIndex < encoders.length - 1)
                    curIndex++;
                if (dpad_downOneShot.update(gamepad1.dpad_down) && curIndex > 0)
                    curIndex--;
                if (aOneShot.update(gamepad1.a)) {
                   //linearSlide.goEncoder(encoders[curIndex], 1);
                }

            }*/


            telemetry.addData("Encoder unit set to: ", encoders[curIndex]);
            telemetry.addData("Current encoder distance: " , 0);

        }
    }
}

