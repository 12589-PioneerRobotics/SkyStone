package org.firstinspires.ftc.teamcode.pioneerrobotics1920.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Red TeleOp", group = "Red")
public class TeleOpBlue extends LinearTeleOp {
    public TeleOpBlue(){
        super();
        blue = false;
    }
}