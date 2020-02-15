package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousRed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousRed;

@Autonomous(name = "Red loading left", group = "Red")
@Disabled
public class AutonRedLoadLeft extends AutonomousRed {
    public AutonRedLoadLeft(){
        super();
        auto.blue = false;
        auto.startBuilding = false;
        auto.left = true;
    }
}
