package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Coordinates;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;

@Disabled
@Autonomous(name = "Test Measurements")
public class TestMeasurements extends LinearOpMode {

    private Driving drive;
    private Navigation navigation;
    private MoacV_2.LinearSlide linearSlide;
    private MoacV_2 moac;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Driving(this);
        navigation = new Navigation(drive);
        moac = new MoacV_2(this.hardwareMap);

        int[] encoders = {100, 200, 300, 400, 500, 1000, 2000, 3000, 4000, 5000};
        int[] inches = {5,10,15,20,25,30,35,40};
        Toggle.OneShot dpad_upOneShot = new Toggle.OneShot();
        Toggle.OneShot dpad_downOneShot = new Toggle.OneShot();
        Toggle.OneShot aOneShot = new Toggle.OneShot();

        int curIndex = 0;
        int count = 0;

        Coordinates[] coordinates = {new Coordinates(9, 36),
                                     new Coordinates(34, 45),
                                     new Coordinates(34, 36),
                                     new Coordinates(34, 28)};

        double[] angles = {0, 90, 180, 270};

        navigation.currPos(9, 36, 270);

        waitForStart();

        while(this.opModeIsActive()) {
            if (count == 0) {
                if (dpad_upOneShot.update(gamepad1.dpad_up) && curIndex < inches.length - 1)
                    curIndex++;
                if (dpad_downOneShot.update(gamepad1.dpad_down) && curIndex > 0)
                    curIndex--;
                if (aOneShot.update(gamepad1.a)) {
                    drive.moveClose("right", inches[curIndex], 0.5, 3);
                }
                if (aOneShot.update(gamepad1.b)) {
                    drive.moveClose("back", inches[curIndex], 0.5, 3);
                }
                /*telemetry.addData("Current location", "("+navigation.getX()+","+navigation.getY()+")");
                telemetry.addData("Location going to", coordinates[curIndex]);
                telemetry.addData("Angle turning to", angles[curIndex]);
                telemetry.addData("turn angle", navigation.turnAngle);*/
                telemetry.addData("Press a for moveClose(right), b for moveClose(back)", null);
                telemetry.addData("Going to: ", inches[curIndex]);
//                telemetry.addData("Right Distance: ", drive.rightDistance.getDistance(DistanceUnit.INCH));
//                telemetry.addData("Back distance: ", drive.backDistance.getDistance(DistanceUnit.INCH));

            }
            telemetry.update();

            /*telemetry.addData("Encoder unit set to: ", encoders[curIndex]);
            telemetry.addData("Current encoder distance: " , 0);*/

        }
    }
}

