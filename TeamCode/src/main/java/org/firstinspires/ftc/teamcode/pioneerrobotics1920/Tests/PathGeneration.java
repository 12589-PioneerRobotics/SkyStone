package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

import java.io.BufferedWriter;
import java.io.File;

//@Disabled
public class PathGeneration extends OpMode {
    DcMotor rf, rb, lf, lb;
    final double SCALE = 0.35;
    final String fileName = "pathGeneration";
    BufferedWriter bw;
    File pathMotor;
    Toggle.OneShot writeOneShot;
    int count = 0;

    @Override
    public void init() {

        pathMotor = AppUtil.getInstance().getSettingsFile(fileName);

        writeOneShot = new Toggle.OneShot();

        lf = hardwareMap.dcMotor.get("frontLeft");
        rf = hardwareMap.dcMotor.get("frontRight");
        lb = hardwareMap.dcMotor.get("backLeft");
        rb = hardwareMap.dcMotor.get("backRight");

        lf.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rb.setDirection(DcMotor.Direction.FORWARD);

        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void libertyDrive(double drive, double turn, double strafe) {
        double factor = Math.abs(drive) + Math.abs(turn) + Math.abs(strafe);
        if (factor <= 1)
            factor = 1;
        lb.setPower((drive - strafe + turn) / factor);
        lf.setPower((drive + strafe + turn) / factor);
        rb.setPower((drive + strafe - turn) / factor);
        rf.setPower((drive - strafe - turn) / factor);
    }

    @Override
    public void loop() {
        if (gamepad1.left_bumper)
            libertyDrive(-Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.35, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE + 0.25));
        else
            libertyDrive(-Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.3), gamepad1.left_stick_x);

        if (writeOneShot.update(gamepad1.a)) {
            ReadWriteFile.writeFile(pathMotor, -Operations.powerScale(gamepad1.right_stick_y) + "," + Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.3) + "," + lb.getPower() + "," + rb.getPower() + "\n");
            count++;
        }

        if (gamepad1.b) {
            int go = 0;
            String[] values = ReadWriteFile.readFile(pathMotor).split("\n");
            while (go < count) {
                String[] parameters = values[go].split(",");
                libertyDrive(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2]));
                go++;
            }
        }
    }

}
