package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousBlue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Blue Building left", group = "Blue")
public class AutonBlueBuildLeft extends Auton {
    public AutonBlueBuildLeft(){
        super();
        blue = true;
        startBuilding = true;
        left = true;
    }
}
