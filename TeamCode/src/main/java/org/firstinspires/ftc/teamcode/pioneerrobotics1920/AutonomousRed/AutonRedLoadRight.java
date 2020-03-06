package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Red Autonomous", group = "Red")
public class AutonRedLoadRight extends Auton {
    public AutonRedLoadRight(){
        super();
        blue = false;
        startBuilding = false;
        left = false;
    }
}