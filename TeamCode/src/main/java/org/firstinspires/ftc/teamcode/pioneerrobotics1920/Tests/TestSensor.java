package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@Disabled
@TeleOp(name = "Test Sensor", group = "test")
public class TestSensor extends LinearOpMode {
    private ModernRoboticsI2cRangeSensor frontDistance;
    private ModernRoboticsI2cRangeSensor backDistance;
    private ModernRoboticsI2cRangeSensor rightDistance;
    private ModernRoboticsI2cRangeSensor leftDistance;

    private ColorSensor brickSensor;
    private Toggle.OneShot oneShotLight;
    private MoacV_2 moac;

    //    private ColorSensor test;
    private boolean lightOn = true;
    //RevBlinkinLedDriver light;

    Driving driving;

    @Override
    public void runOpMode() throws InterruptedException {
//        test = hardwareMap.colorSensor.get("brickSensor");
        moac = new MoacV_2(hardwareMap);
        driving = new Driving(this);
        oneShotLight = new Toggle.OneShot();

        brickSensor = this.hardwareMap.get(ColorSensor.class, "brickSensor");

        frontDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontDistance");
        backDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "backDistance");
        rightDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rightDistance");
        leftDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "leftDistance");
        waitForStart();
//        test.enableLed(true);
//        light = hardwareMap.get(RevBlinkinLedDriver.class, "led");
//        light.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);


        while(this.opModeIsActive()){

//            telemetry.addData("Color values", "Red: %d, Green: %d, Blue: %, Alpha: %d", test.red(), test.green(), test.blue(), test.alpha());
            /*if (oneShotLight.update(gamepad1.y)) {
                lightOn = !lightOn;
                //test.enableLed(!lightOn);
            }*/
           /* if(gamepad1.a) {
                moac.intake.takeIn();
            }
            else moac.intake.stopIntake();*/

            /*telemetry.addData("Color sensor distance getDistance()", test.getDistance(DistanceUnit.INCH));
            telemetry.addData("Color sensor light level", test.getLightDetected());
            telemetry.addData("Raw light level", test.getRawLightDetected());
            telemetry.addData("Raw distance", test.rawOptical());*/


           /*if(gamepad1.x) {
               light.setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_HEARTBEAT_FAST);
           }
           else {
               light.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
           }*/
            telemetry.addData("Alpha", brickSensor.alpha());
            telemetry.addData("ARGB", brickSensor.argb());
            telemetry.addData("Blue", brickSensor.blue());
            telemetry.addData("Red", brickSensor.red());
            telemetry.addData("Green", brickSensor.green());

            telemetry.addData("right distance", driving.getAccurateDistanceSensorReading(driving.rightDistance));
            telemetry.addData("back distance", driving.getAccurateDistanceSensorReading(driving.backDistance));
//            telemetry.addData("back distance cmUltrasonic", backDistance.cmUltrasonic());
//            telemetry.addData("back distance cmOptical", backDistance.cmOptical());
//            telemetry.addData("right distance",rightDistance.getDistance(DistanceUnit.INCH));
            /*telemetry.addData("right distance cmUltrasonic", rightDistance.cmUltrasonic());
            telemetry.addData("right distance cmOptical", rightDistance.cmOptical());*/
            telemetry.addData("left distance", driving.getAccurateDistanceSensorReading(driving.leftDistance));
            telemetry.addData("right distance status", rightDistance.status());
            telemetry.addData("left distance status", leftDistance.status());
            telemetry.addData("back distance status", backDistance.status());
            telemetry.addData("front distance status", frontDistance.status());

            telemetry.update();
        }
    }
}
