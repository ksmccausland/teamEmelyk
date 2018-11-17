package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Robot extends IterativeRobot {
  private static final int kFrontLeftChannel = 5;
  private static final int kRearLeftChannel = 2;
  private static final int kFrontRightChannel = 4;
  private static final int kRearRightChannel = 3;

  private static final int kJoystickChannel = 0;

  private XboxController joystick;

  TalonSRX frontLeft;
  TalonSRX rearLeft;
  TalonSRX frontRight;
  TalonSRX rearRight;

  /*
   * LogiTech Controller Layout:
   *         
   *       -y             
   *  +x __| 
   *        \
   *         -z
   */
  public void mecanumDrive(double driveX, double driveY, double look) {
    // for some reason... forward is negative. Because that makes sense...
    // So lets invert it.
    driveY *= -1; 

    // driveY > 0.0 = move forward
    // driveY < 0.0 = move backward
    // driveX > 0.0 = move right
    // driveX < 0.0 = move left
    // look > 0.0 = clockwise
    // look < 0.0 = counter-clockwise
    
    double leftFrontVel = driveY + driveX + look;
    double leftRearVel = -driveY + driveX - look;
    double rightFrontVel = driveY - driveX - look;
    double rightRearVel = -driveY - driveX + look;

    frontLeft.set(ControlMode.PercentOutput, leftFrontVel);
    rearLeft.set(ControlMode.PercentOutput, leftRearVel);
    frontRight.set(ControlMode.PercentOutput, rightFrontVel);
    rearRight.set(ControlMode.PercentOutput, rightRearVel);
  }

  @Override
  public void robotInit() {
    frontLeft = new TalonSRX(kFrontLeftChannel);
    rearLeft = new TalonSRX(kRearLeftChannel);
    frontRight = new TalonSRX(kFrontRightChannel);
    rearRight = new TalonSRX(kRearRightChannel);

    // invert drive train from tank drive
    frontLeft.setInverted(true);
    rearRight.setInverted(true);

    joystick = new XboxController(kJoystickChannel);
  }

  @Override
  public void teleopPeriodic() {
    mecanumDrive(joystick.getX(Hand.kRight), joystick.getY(Hand.kRight), joystick.getX(Hand.kLeft));
  }
}
