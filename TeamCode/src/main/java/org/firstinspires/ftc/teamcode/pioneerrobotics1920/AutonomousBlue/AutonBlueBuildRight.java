package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousBlue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;

@Autonomous(name = "Auton blue build right")
public class AutonBlueBuildRight extends Auton {
    public AutonBlueBuildRight(){
        super();
        blue = true;
        startBuilding = true;
        left = false;
    }
}
