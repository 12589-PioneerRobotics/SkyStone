package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousRed;

@Autonomous(name = "Red Autonomous", group = "Red")
public class AutonRedLoadRight extends AutonomousRed {
    public AutonRedLoadRight(){
        super(false, false);
        auto.blue = false;
        auto.startBuilding = false;
        auto.left = false;
    }
}