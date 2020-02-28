package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousBlue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Auton;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousBlue;

@Autonomous(name = "Park", group = "Blue")
public class AutonPark extends Auton {
    public AutonPark(){
        super();
        blue = true;
        startBuilding = true;
        left = false;
    }
}
