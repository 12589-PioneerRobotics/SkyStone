package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousBlue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Blue Loading Left", group = "Blue")
public class AutonBlueLoadLeft extends Auton {
    public AutonBlueLoadLeft() {
        super();
        blue = true;
        startBuilding = false;
        left = true;

    }
}
