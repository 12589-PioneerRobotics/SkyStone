package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousRed;
@Disabled
@Autonomous(name = "Red building right", group = "Red")
public class AutonRedBuildRight extends AutonomousRed {
    public AutonRedBuildRight(){
        super(false, true);
        auto.blue = false;
        auto.startBuilding = true;
        auto.left = false;
    }
}