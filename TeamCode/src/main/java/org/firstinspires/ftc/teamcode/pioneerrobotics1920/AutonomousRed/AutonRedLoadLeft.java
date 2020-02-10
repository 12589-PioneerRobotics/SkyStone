package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Red loading left", group = "Red")
@Disabled
public class AutonRedLoadLeft extends Auton {
    public AutonRedLoadLeft(){
        super();
        blue = false;
        startBuilding = false;
        left = true;
    }
}
