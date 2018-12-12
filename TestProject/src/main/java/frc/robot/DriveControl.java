package frc.robot;

public class DriveControl {
    private double frontLeft;
    private double rearLeft;
    private double frontRight;
    private double rearRight;

    public DriveControl(double fl, double rl, double fr, double rr) {
        frontLeft = fl;
        rearLeft = rl;
        frontRight = fr;
        rearRight = rr;
    }

    public double getFrontLeft() {
        return frontLeft;
    }

    public double getRearLeft() {
        return rearLeft;
    }

    public double getFrontRight() {
        return frontRight;
    }
    
    public double getRearRight() {
        return rearRight;
    }
}