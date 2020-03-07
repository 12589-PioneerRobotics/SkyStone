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
    Toggle.OneShot gamepad1A;
    Toggle.OneShot gamepad1B;
    Toggle.OneShot gamepad1X;
    final double SCALE = 0.35;
    boolean stone;
    boolean writing;
    boolean finished;
    boolean calibrated;
    File colorSensorCalibrationValue;
    File emptyValues;
    File stoneValues;
    ColorSensor colorSensor;
    String stoneInCollectedRaw;
    String stoneOutCollectedRaw;
    String stoneBuffer = "";
    String emptyBuffer = "";
    double stoneRadius;
    int counter = 0;

    double timer = 0;

    Point[] stoneInPoints;
    Point[] stoneOutPoints;

    Point emptyCenter = new Point(0, 0, 0);
    Point stoneCenter = new Point(0, 0, 0);

    @Override
    public void runOpMode() throws InterruptedException {
        driving = new Driving(this);
        moacV_2 = new MoacV_2(hardwareMap);
        gamepad1A = new Toggle.OneShot();
        gamepad1B = new Toggle.OneShot();
        gamepad1X = new Toggle.OneShot();
        stone = false;
        writing = false;
        finished = false;
        calibrated = false;
        colorSensorCalibrationValue = AppUtil.getInstance().getSettingsFile("colorSensorCalibrationValue.txt");
        emptyValues = AppUtil.getInstance().getSettingsFile("emptyValues.txt");
        stoneValues = AppUtil.getInstance().getSettingsFile("stoneValues.txt");
        colorSensor = hardwareMap.colorSensor.get("brickSensor");
        colorSensor.enableLed(true);

        telemetry.addData("init finished", null);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            moacV_2.stacker.open();

            if (gamepad1.left_bumper)
                driving.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y, SCALE), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.35, SCALE), Operations.powerScale(gamepad1.left_stick_x, SCALE + 0.25));
            else
                driving.libertyDrive(-Operations.powerScale(gamepad1.right_stick_y), Operations.powerScale(gamepad1.right_stick_x + gamepad1.left_stick_x * -.3), gamepad1.left_stick_x);

            if (gamepad1.left_trigger > .5) moacV_2.intake.spitOut();
            else if (gamepad1.right_trigger > .5) moacV_2.intake.takeIn();
            else moacV_2.intake.stopIntake();

            if (gamepad1A.update(gamepad1.a))
                stone = !stone;
            if (gamepad1B.update(gamepad1.b))
                writing = !writing;
            if (gamepad1X.update(gamepad1.x))
                finished = true;

            if (gamepad1.left_trigger > .5) moacV_2.intake.spitOut();
            else if (gamepad1.right_trigger > .5) moacV_2.intake.takeIn();
            else moacV_2.intake.stopIntake();


            if (writing && getRuntime() > timer + .2) {
                if (stone)
                    stoneBuffer += getColorValues();
                else
                    emptyBuffer += getColorValues();
                timer = getRuntime();
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

                stoneInPoints = new Point[valueStringStoneIn.length];
                stoneOutPoints = new Point[valueStringStoneOut.length];

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

                setcalibratedPoints();
                setStoneRadius();

                calibrated = true;

                counter++;
            }

            telemetry.addData("calibrated", calibrated);
            if (calibrated) {
                telemetry.addData("we have a stone", stoneIsIn());
                telemetry.addData("color sensor value", "" + colorSensor.red() + "," + colorSensor.green() + "," + colorSensor.blue());
                telemetry.addData("stone center", stoneCenter);
                telemetry.addData("empty center", emptyCenter);
                telemetry.addData("radius", stoneRadius);
                telemetry.addData("Current distance between stoneCenter", distance(currentColor(), stoneCenter));
                telemetry.addData("Current distance between empty stone", distance(currentColor(), emptyCenter));
            } else {
                telemetry.addData("stone", stone);
                telemetry.addData("writing", writing);
                telemetry.addData("finished", finished);
            }
            if (distance(emptyCenter, stoneCenter) < stoneRadius)
                telemetry.addData("Empty center is in stone sphere", null);
            telemetry.update();
        }
    }

    public Point currentColor() {
        return new Point(colorSensor.red(), colorSensor.green(), colorSensor.blue());
    }


    private boolean stoneIsIn() {
        Point colorPoint = currentColor();
        double diff = distance(colorPoint, stoneCenter);
        if ((diff < distance(colorPoint, emptyCenter)) && diff <= stoneRadius)
            return true;
        return false;
    }

    public boolean checkStone() {
        colorSensorCalibrationValue = AppUtil.getInstance().getSettingsFile("colorSensorCalibrationValue.txt");
        Point colorPoint = currentColor();
        setcalibratedPoints();
        setStoneRadius();
        double diff = distance(colorPoint, stoneCenter);
        if ((diff < distance(colorPoint, emptyCenter)) && diff <= stoneRadius)
            return true;
        return false;
    }

    private double longestDistance(Point[] points, Point center) {
        double result = 0;
        for (Point point : points)
            result = Math.max(result, distance(point, center));
        return result;
    }

    private void setStoneRadius() {
        stoneRadius = Math.max(longestDistance(stoneInPoints, stoneCenter), 1.5);
    }

    private void setcalibratedPoints() {
        String s = ReadWriteFile.readFile(colorSensorCalibrationValue);
        String[] twoPoints = s.split("\n");
        String[] stonePoint = twoPoints[0].split(",");
        String[] emptyPoint = twoPoints[1].split(",");
        stoneCenter = new Point(Integer.parseInt(stonePoint[0]), Integer.parseInt(stonePoint[1]), Integer.parseInt(stonePoint[2]));
        emptyCenter = new Point(Integer.parseInt(emptyPoint[0]), Integer.parseInt(emptyPoint[1]), Integer.parseInt(emptyPoint[2]));
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

    public double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));
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


