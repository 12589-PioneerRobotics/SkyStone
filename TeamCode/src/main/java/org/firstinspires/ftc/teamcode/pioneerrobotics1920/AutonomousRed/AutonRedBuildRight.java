package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousRed;

@Autonomous(name = "Red building right", group = "Red")
public class AutonRedBuildRight extends AutonomousRed {
    public AutonRedBuildRight(){
        super();
        auto.blue = false;
        auto.startBuilding = true;
        auto.left = false;
    }
}