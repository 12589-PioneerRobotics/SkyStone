package org.firstinspires.ftc.teamcode.pioneerrobotics1920.AutonomousBlue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.AutonomousBlue;

@Autonomous(name = "Blue Loading Left", group = "Blue")
@Disabled
public class AutonBlueLoadLeft extends AutonomousBlue {
    public AutonBlueLoadLeft() {
        super();
        auto.blue = true;
        auto.startBuilding = false;
        auto.left = true;

    }
}
