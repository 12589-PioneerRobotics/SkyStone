package org.firstinspires.ftc.teamcode.pioneerrobotics1920;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoControl {

    public ServoControl(OpMode opMode) {
        initServos(opMode.hardwareMap);
    }

    private Servo topGrabber, bottomGrabber, pivotGrabber;

    public void initServos(HardwareMap hardwareMap) {
        topGrabber = hardwareMap.servo.get("topGrabber");
        bottomGrabber = hardwareMap.servo.get("bottomGrabber");
        pivotGrabber = hardwareMap.servo.get("pivotGrabber");

        topGrabber.setDirection(Servo.Direction.FORWARD);
        bottomGrabber.setDirection(Servo.Direction.FORWARD);
        pivotGrabber.setDirection(Servo.Direction.REVERSE);
    }
    enum SERVOS {TOP, BOTTOM, PIVOT, TOPANDBOTTOM}

    public void setServo(SERVOS servo, double val) {
        switch(servo) {
            case TOP:
                topGrabber.setPosition(val);
                break;
            case BOTTOM:
                bottomGrabber.setPosition(val);
                break;
            case TOPANDBOTTOM:
                topGrabber.setPosition(val);
                bottomGrabber.setPosition(val);
                break;
            case PIVOT:
                pivotGrabber.setPosition(val);
                break;
        }
    }

    public double getServoPosition(ServoControl.SERVOS servos) {
        double pos = 0;
        switch(servos) {
            case TOP:
                pos = topGrabber.getPosition();
                break;
            case BOTTOM:
                pos = bottomGrabber.getPosition();
                break;
            case PIVOT:
                pos = pivotGrabber.getPosition();
                break;
        }
            return pos;
        }

        public void grabSkystone() {
            setServo(SERVOS.PIVOT, .9);
            setServo(SERVOS.TOPANDBOTTOM,.9);
            setServo(SERVOS.PIVOT,.2);
        }


    /*useless stuff
    public void setServoPairs(SERVOS servos, double val) {
        double min = .1;
        switch (servos) {
            case TOP:
                if(topLeft.getPosition() + val < min && topRight.getPosition() + val < min) {
                    topLeft.setPosition(min);
                    topRight.setPosition(min);
                }
                else {
                    topLeft.setPosition(val + topLeft.getPosition());
                    topRight.setPosition(val + topRight.getPosition());
                }
                break;
            case BOTTOM:
                if(bottomLeft.getPosition() + val < min && bottomRight.getPosition() + val < min) {
                    topRight.setPosition(min);
                    topLeft.setPosition(min);
                }
                else {
                    bottomLeft.setPosition(val + bottomLeft.getPosition());
                    bottomRight.setPosition(val + bottomRight.getPosition());
                }
                break;
        }
    }*/
    /*public void setAllServos(double val) {
        topRight.setPosition(val);
        topLeft.setPosition(val);
        bottomRight.setPosition(val);
        bottomLeft.setPosition(val);
    }*/
}
