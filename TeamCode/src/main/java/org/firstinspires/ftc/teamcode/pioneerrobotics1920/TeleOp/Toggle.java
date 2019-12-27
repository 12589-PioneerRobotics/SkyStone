package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Toggle {

    private OneShot oneShot;
    private boolean oldValue;

    public Toggle() {
        oneShot = new OneShot();
    }

    public boolean update(boolean newValue) {
        if(oneShot.update(newValue)) {
            oldValue = !oldValue;
        }
        return oldValue;
    }


    public static class OneShot {
        private boolean oldValue;
        public boolean update(boolean newValue) {
            if(newValue) {
                if(!oldValue) {
                    oldValue = true;
                    return true;
                }
            }
            else {
                oldValue = false;
            }
            return false;
        }
    }

    public static class TimeToggle {
        private final Toggle toggle = new Toggle();
        OneShot oneShot = new OneShot();
        private final OpMode opMode;
        private final double targetDeltaTime;
        private boolean wasPressed;
        private double startTime;

        public TimeToggle(OpMode opMode, double time) {
            this.opMode = opMode;
            targetDeltaTime = time;
        }
        public boolean update(boolean newValue) {
            if (oneShot.update(newValue)) {
                startTime = opMode.getRuntime();
            }
            return toggle.update(newValue && opMode.getRuntime() - startTime > targetDeltaTime);
            /*
            if (wasPressed) {
                if (newValue) {
                    return toggle.update(opMode.getRuntime() - startTime > targetDeltaTime);
                }
                else {
                    wasPressed = false;
                }
            }
            else {
                if (newValue) {
                    wasPressed = true;
                    startTime = opMode.getRuntime();
                }
                else {

                }
            }
            return false;
            */
        }
    }
}