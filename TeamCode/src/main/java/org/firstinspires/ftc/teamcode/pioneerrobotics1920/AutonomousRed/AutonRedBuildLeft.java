package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Red building left")
public class AutonRedBuildLeft extends Auton {
    public AutonRedBuildLeft(){
        super();
        blue = false;
        startBuilding = true;
        left = true;
    }
}
