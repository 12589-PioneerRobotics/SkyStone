package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Driving;
import org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core.Navigation;

@Autonomous(name = "Turn test", group = "Test")
public class TurnTest extends LinearOpMode {
    Driving drive = new Driving(this);
    Navigation navigation = new Navigation(drive);

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
    }
}
