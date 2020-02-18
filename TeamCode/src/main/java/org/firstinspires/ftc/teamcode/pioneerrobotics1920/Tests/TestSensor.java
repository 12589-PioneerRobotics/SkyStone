package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

@TeleOp(name = "Test Sensor", group = "test")
public class TestSensor extends LinearOpMode {
    private ModernRoboticsI2cRangeSensor frontDistance;
    private ModernRoboticsI2cRangeSensor backDistance;
    private ModernRoboticsI2cRangeSensor rightDistance;
    private ModernRoboticsI2cRangeSensor leftDistance;
    private Toggle.OneShot oneShotLight;
    //private MoacV_2 moac;

    //private RevColorSensorV3 test;
    private boolean lightOn = true;
    //RevBlinkinLedDriver light;

    @Override
    public void runOpMode() throws InterruptedException {
        //test = hardwareMap.get(RevColorSensorV3.class, "brickSensor");
        // moac = new MoacV_2(hardwareMap);
        oneShotLight = new Toggle.OneShot();
        frontDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontDistance");
        backDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "backDistance");
        rightDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rightDistance");
        leftDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "leftDistance");
        waitForStart();
        //test.enableLed(true);
//        light = hardwareMap.get(RevBlinkinLedDriver.class, "led");
//        light.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);


        while(this.opModeIsActive()){

            //telemetry.addData("Color values", "Red: %d, Green: %d, Blue: %, Alpha: %d", test.red(), test.green(), test.blue(), test.alpha());
            if (oneShotLight.update(gamepad1.y)) {
                lightOn = !lightOn;
                //test.enableLed(!lightOn);
            }
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


            telemetry.addData("front distance", frontDistance.getDistance(DistanceUnit.INCH));
            telemetry.addData("front distance", frontDistance.getDistance(DistanceUnit.INCH));
            telemetry.addData("back distance",backDistance.getDistance(DistanceUnit.INCH));
            telemetry.addData("back distance cmUltrasonic", backDistance.cmUltrasonic());
            telemetry.addData("back distance cmOptical", backDistance.cmOptical());
            telemetry.addData("right distance",rightDistance.getDistance(DistanceUnit.INCH));
            telemetry.addData("right distance cmUltrasonic", rightDistance.cmUltrasonic());
            telemetry.addData("right distance cmOptical", rightDistance.cmOptical());
            telemetry.addData("left distance", leftDistance.getDistance(DistanceUnit.INCH));

            telemetry.update();
        }
    }
}
