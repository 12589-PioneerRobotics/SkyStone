package org.firstinspires.ftc.teamcode.pioneerrobotics1920.Core;

public class Coordinates {
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double x,y,angle;
    public Coordinates(double x, double y){
        this.x = x;
        this.y = y;
        this.angle = 0;
    }
    public Coordinates(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
}
