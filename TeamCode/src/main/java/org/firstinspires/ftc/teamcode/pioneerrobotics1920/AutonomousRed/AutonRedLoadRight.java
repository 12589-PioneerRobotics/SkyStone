package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousRed;

@Autonomous(name = "Red loading right", group = "Red")
public class AutonRedLoadRight extends AutonomousRed {
    public AutonRedLoadRight(){
        super();
        auto.blue = false;
        auto.startBuilding = false;
        auto.left = false;
    }
}