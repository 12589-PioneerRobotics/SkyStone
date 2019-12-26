package org.firstinspires.ftc.teamcode.pioneerrobotics1920;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by user12589 on 9/10/18.
 */
@TeleOp(name = "ClubTeleOp")
@Disabled
public class ClubTeleOp extends OpMode {
    private DcMotor right;
    private DcMotor left;
    //private Servo servo;

    public void init(){
        //servo = hardwareMap.servo.get("kicker");

        left = hardwareMap.dcMotor.get("frontLeft");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        right = hardwareMap.dcMotor.get("frontRight");
        right.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void loop() {
        //gamepad1
        /*
        if(servo.getPosition() != 0.5 && !gamepad1.right_bumper) {
                servo.setPosition(0.5);
        }

        if(servo.getPosition() != 0.3 && gamepad1.right_bumper) {
            servo.setPosition(0.3);
        }

        left.setPower(-gamepad1.left_stick_y);
        right.setPower(-gamepad1.right_stick_y);
        */
        if (gamepad1.left_stick_y < 0)
        {
            left.setPower(0.5);
            right.setPower(0.5);
        }

        if (gamepad1.left_stick_y > 0)
        {
            left.setPower(-0.5);
            right.setPower(-0.5);
        }

        if (gamepad1.left_stick_y == 0)
        {
            left.setPower(0);
            right.setPower(0);
        }

        if (gamepad1.left_stick_x > 0 ) {
            left.setPower(0.5);
            right.setPower(-0.5);
        }


        if (gamepad1.left_stick_x < 0) {
            left.setPower(-0.5);
            right.setPower(0.5);
        }



    }
}
