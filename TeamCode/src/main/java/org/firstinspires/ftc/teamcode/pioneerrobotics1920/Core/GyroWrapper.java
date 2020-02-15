package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class GyroWrapper {
    /*
    Usage: GyroWrapper gyro = new GyroWrapper(hardwareMap.get("imu"));
    float angle = gyro.getValueContinuous();
     */
    private final BNO055IMU imu;
    private float offset = 0;
    private float lastSensorValue;

    public GyroWrapper(BNO055IMU imu) {
        this.imu = imu;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);
    }

    public float getValueContinuous () {
        final int TOLERANCE = 180;
        float sensorValue = imu.getAngularOrientation().firstAngle;
        offset += ((int) ((lastSensorValue - sensorValue) / TOLERANCE)) * 360;
        lastSensorValue = sensorValue;
        return -(lastSensorValue + offset); // make right positive and left negative
    }

    public boolean isGyroReady() {
        return imu.isGyroCalibrated();
    }
    public boolean isSystemReady() {return imu.isSystemCalibrated();}

    public void setInitPosition() {
        Position position = new Position(DistanceUnit.INCH, 0, 0, 0, System.nanoTime());
    }

    public Position reportPosition() {
        return imu.getPosition();
    }

}