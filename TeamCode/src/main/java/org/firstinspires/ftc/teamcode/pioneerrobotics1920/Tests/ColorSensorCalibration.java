package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.MoacV_2;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Operations;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp.Toggle;

import java.io.File;

@TeleOp(name = "ColorSensorCalibration")
public class ColorSensorCalibration extends LinearOpMode {

    Driving driving;
    MoacV_2 moacV_2;
    Toggle.OneShot hasStone;
    Toggle.OneShot startWriting;
    Toggle.OneShot finish;
    final double SCALE = 0.35;
    boolean stone;
    boolean writing;
    boolean finished;
    File colorSensorCalibrationValue;
    File emptyValues;
    File stoneValues;
    ColorSensor colorSensor;
    String stoneInCollectedRaw;
    String stoneOutCollectedRaw;
    String stoneBuffer = "";
    String emptyBuffer = "";
    int counter = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        driving = new Driving(this);
        moacV_2 = new MoacV_2(hardwareMap);
        hasStone = new Toggle.OneShot();
        startWriting = new Toggle.OneShot();
        finish = new Toggle.OneShot();
        stone = false;
        writing = false;
        finished = false;
        colorSensorCalibrationValue = AppUtil.getInstance().getSettingsFile("colorSensorCalibrationValue.txt");
        emptyValues = AppUtil.getInstance().getSettingsFile("emptyValues.txt");
        stoneValues = AppUtil.getInstance().getSettingsFile("stoneValues.txt");
        colorSensor = hardwareMap.colorSensor.get("brickSensor");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.left_bumper)
                driving.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.35, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE + 0.25));
            else
                driving.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.3), gamepad1.left_stick_x);

            if (gamepad1.left_trigger > .5) moacV_2.intake.spitOut();
            else if (gamepad1.right_trigger > .5) moacV_2.intake.takeIn();
            else moacV_2.intake.stopIntake();

            if (hasStone.update(gamepad1.a))
                stone = !stone;
            if (startWriting.update(gamepad1.b))
                writing = !writing;
            if (finish.update(gamepad1.x))
                finished = true;


            if (writing) {
                if (stone)
                    stoneBuffer += getColorValues();
                else
                    emptyBuffer += getColorValues();
            }

            if (finished && counter == 0) {
                ReadWriteFile.writeFile(stoneValues, stoneBuffer);
                ReadWriteFile.writeFile(emptyValues, emptyBuffer);

                telemetry.addData("wrote in stone and empty values", null);
                telemetry.update();

                stoneInCollectedRaw = ReadWriteFile.readFile(stoneValues);
                stoneOutCollectedRaw = ReadWriteFile.readFile(emptyValues);

                telemetry.addData("read from the files", null);
                telemetry.update();

                String[] valueStringStoneIn = stoneInCollectedRaw.split("\n");
                String[] valueStringStoneOut = stoneOutCollectedRaw.split("\n");

                telemetry.addData("copy to string arrays", null);
                telemetry.update();

                Point[] stoneInPoints = new Point[valueStringStoneIn.length];
                Point[] stoneOutPoints = new Point[valueStringStoneOut.length];

                telemetry.addData("initialized point arrays", null);
                telemetry.update();

                String[] rgbValues;
                for (int i = 0; i < stoneInPoints.length; i++) {
                    rgbValues = valueStringStoneIn[i].split(",");
                    stoneInPoints[i] = new Point(Integer.parseInt(rgbValues[0]), Integer.parseInt(rgbValues[1]), Integer.parseInt(rgbValues[2]));
                }

                for (int i = 0; i < stoneOutPoints.length; i++) {
                    rgbValues = valueStringStoneOut[i].split(",");
                    stoneOutPoints[i] = new Point(Integer.parseInt(rgbValues[0]), Integer.parseInt(rgbValues[1]), Integer.parseInt(rgbValues[2]));

                }

                telemetry.addData("copy data into point arrays", null);
                telemetry.update();

                ReadWriteFile.writeFile(colorSensorCalibrationValue, getCenter(stoneInPoints).toString() + "\n" + getCenter(stoneOutPoints).toString());

                telemetry.addData("write in to calibratioin files", null);
                telemetry.update();

                counter++;
            }
            telemetry.addData("hasStone", stone);
            telemetry.addData("writing", writing);
            telemetry.update();
        }
    }

    private String getColorValues() {
        return colorSensor.red() + "," + colorSensor.green() + "," + colorSensor.blue() + "\n";
    }

    public Point getCenter(Point[] points) {
        int sumX = 0, sumY = 0, sumZ = 0;
        for (Point p : points) {
            sumX += p.x;
            sumY += p.y;
            sumZ += p.z;
        }
        return new Point(sumX / points.length, sumY / points.length, sumZ / points.length);
    }

    class Point {
        int x, y, z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() {
            return "" + x + "," + y + "," + z;
        }
    }
}


