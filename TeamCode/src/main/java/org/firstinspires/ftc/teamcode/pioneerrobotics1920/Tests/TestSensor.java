package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@TeleOp(name = "Test Sensor", group = "test")
public class TestSensor extends LinearOpMode {

    ColorSensor brickSensor;
    boolean led;
    Toggle.OneShot ledOneShot = new Toggle.OneShot();

    @Override
    public void runOpMode() throws InterruptedException {
        brickSensor = hardwareMap.colorSensor.get("brickSensor");
        led = true;

        waitForStart();

        while(this.opModeIsActive()){
            if (ledOneShot.update(gamepad1.a)) {
                led = !led;
                brickSensor.enableLed(led);
            }
            telemetry.addData("led: ", "" + led);
            telemetry.addData("Blue: ", brickSensor.blue());
            telemetry.addData("Red: ", brickSensor.red());
            telemetry.addData("Green: ", brickSensor.green());
            telemetry.addData("Alpha: ", brickSensor.alpha());
            telemetry.addData("Hue: ", brickSensor.argb());
            telemetry.update();
        }
    }
}
