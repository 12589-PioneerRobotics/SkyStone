package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Red building right", group = "Red")
public class AutonRedBuildRight extends Auton {
    public AutonRedBuildRight(){
        super();
        blue = false;
        startBuilding = true;
        left = false;
    }
}