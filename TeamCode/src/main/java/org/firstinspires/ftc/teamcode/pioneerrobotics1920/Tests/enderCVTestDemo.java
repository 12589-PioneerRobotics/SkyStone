package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.SamplingCV;

@TeleOp(name="Example: EnderCVTestDemo")
@Disabled
public class enderCVTestDemo extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SamplingCV Vision;
        Vision = new SamplingCV();

        Vision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        //Vision.setShowCountours(false);
        Vision.enable();
    }

        /*Vision.setShowCountours(true);

        List<MatOfPoint> contours = Vision.getContours();
        for (int i = 0; i < contours.size(); i++) {
            Rect boundingRect = Imgproc.boundingRect(contours.get(i));
            telemetry.addData("contour" + Integer.toString(i),
                    String.format(Locale.getDefault(), "(%d, %d)", (boundingRect.x + boundingRect.width) / 2, (boundingRect.y + boundingRect.height) / 2));
        }
    }

    public void stop() {
        Vision.disable();
    }*/
}
