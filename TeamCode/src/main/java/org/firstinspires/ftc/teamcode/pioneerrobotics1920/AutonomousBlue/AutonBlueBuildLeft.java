package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousBlue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Auton blue building left")
public class AutonBlueBuildLeft extends Auton {
    public AutonBlueBuildLeft(){
        super();
        blue = true;
        startBuilding = true;
        left = true;
    }
}
