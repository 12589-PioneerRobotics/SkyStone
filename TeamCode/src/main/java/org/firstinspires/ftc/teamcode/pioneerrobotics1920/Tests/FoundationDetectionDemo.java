package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.CV.FoundationDetection;

@Disabled
@Autonomous(name = "FoundationDetectionDemo", group = "Test")
public class FoundationDetectionDemo extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        FoundationDetection algorithm = new FoundationDetection(false);
        algorithm.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        algorithm.enable();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Center Values", algorithm.value2Str(algorithm.centerImageValues));
            telemetry.addData("center", "("+algorithm.centerX+","+algorithm.centerY+")");
            telemetry.addData("foundation pos", algorithm.calcFieldX(algorithm.centerY));
            if(algorithm.angle < 20) {
                telemetry.addData("Foundation Orientation: ", algorithm.horiz ? "Horizontally aligned" : "Vertically aligned");
            }
            else telemetry.addData("Foundation angle: ", algorithm.angle);
            telemetry.addData("Estimated Coordinates: ","(" + algorithm.position.x + ", " + algorithm.position.y + ")");
            telemetry.update();
            idle();
        }
        algorithm.disable();
    }
}
