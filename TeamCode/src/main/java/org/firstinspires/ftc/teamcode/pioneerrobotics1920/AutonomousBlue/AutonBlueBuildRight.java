package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousBlue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousBlue;

@Autonomous(name = "Blue build right", group = "Blue")
public class AutonBlueBuildRight extends AutonomousBlue {
    public AutonBlueBuildRight(){
        super();
        auto.blue = true;
        auto.startBuilding = true;
        auto.left = false;
    }
}
