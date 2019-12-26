package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Auton red building right")
public class AutonRedBuildRight extends Auton {
    public AutonRedBuildRight(){
        super();
        blue = false;
        startBuilding = true;
        left = false;
    }
}