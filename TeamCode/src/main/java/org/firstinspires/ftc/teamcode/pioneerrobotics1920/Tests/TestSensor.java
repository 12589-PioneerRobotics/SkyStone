package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Test Sensor", group = "test")
public class TestSensor extends LinearOpMode {
//    private ModernRoboticsI2cRangeSensor frontDistance;
    private ModernRoboticsI2cRangeSensor backDistance;
    private ModernRoboticsI2cRangeSensor rightDistance;
    private ModernRoboticsI2cRangeSensor leftDistance;


    @Override
    public void runOpMode() throws InterruptedException {
//        frontDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontDistance");
        backDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "backDistance");
        rightDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rightDistance");
        leftDistance = this.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "leftDistance");
        waitForStart();

        while(this.opModeIsActive()){
//            telemetry.addData("front distance",frontDistance.getDistance(DistanceUnit.INCH));
            telemetry.addData("back distance",backDistance.getDistance(DistanceUnit.INCH));
            telemetry.addData("right distance",rightDistance.getDistance(DistanceUnit.INCH));
            telemetry.addData("left distance",leftDistance.getDistance(DistanceUnit.INCH));
            telemetry.update();
        }
    }
}
