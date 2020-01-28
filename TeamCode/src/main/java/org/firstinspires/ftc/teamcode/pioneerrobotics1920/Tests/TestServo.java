package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.android.AndroidOrientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "TestServo", group = "test")
public class TestServo extends OpMode {
    private Servo servo1, servo2, servo3;
    AndroidOrientation orientation = new AndroidOrientation();

    @Override
    public void init() {
        servo1 = hardwareMap.servo.get("grabber");
        orientation.setAngleUnit(AngleUnit.DEGREES);
        orientation.startListening();

//        servo2 = hardwareMap.servo.get("rightFoundationGrabber");   //servo3 = hardwareMap.servo.get("stacker");
        //servo1 = hardwareMap.servo.get("redPivot");
        //servo2 = hardwareMap.servo.get("redInnerGrabber");
        //servo3 = hardwareMap.servo.get("redOuterGrabber");
        //servo1 = hardwareMap.servo.get("leftFoundatileftFoundationGrabberonGrabber"); //.45 - open/not grabbed, .95 - closed/grabbed
        //servo2 = hardwareMap.servo.get("rightFoundationGrabber"); // .567 - open/not grabbed, .08  - closed/grabbed
        //servo1 = hardwareMap.servo.get("telePivot");
        //servo2 = hardwareMap.servo.get("teleInnerGrabber");
        //servo3 = hardwareMap.servo.get("teleOuterGrabber");
    }

    @Override
    public void loop() {
        //servo 2 and servo 3 have to be vertical for no gravity issue
        /*
        if(gamepad1.a)
            servo1.setPosition(.209);
        else
            servo1.setPosition(.025);
            */
//        servo2.setPosition(servo2.getPosition() + gamepad1.right_stick_x * 0.001);
        servo1.setPosition(servo1.getPosition() + gamepad1.left_stick_x * 0.0005);
/*
        if(gamepad1.a)
            servo3.setPosition(1);
        if (gamepad1.x)
            servo3.setPosition(0.8);
        if(gamepad1.y)
            servo3.setPosition(0.6);
        if(gamepad1.b)
            servo3.setPosition(0.4);
*/
        telemetry.addData("orientation.isAvailable()", orientation.isAvailable());
        telemetry.addData("servo1 position", servo1.getPosition());
        telemetry.addData("Orientation angle: ", orientation.getAngle());
//        telemetry.addData("servo2 position", servo2.getPosition());
        //telemetry.addData("servo3 position", servo3.getPosition());
        telemetry.update();
    }
}