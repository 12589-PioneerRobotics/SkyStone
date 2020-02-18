package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousRed;

@Autonomous(name = "Red building left", group = "Red")
public class AutonRedBuildLeft extends AutonomousRed {
    public AutonRedBuildLeft(){
        super();
        auto.blue = false;
        auto.startBuilding = true;
        auto.left = true;
    }
}
