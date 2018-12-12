package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Robot extends IterativeRobot {
  private static final int kFrontLeftChannel = 3;
  private static final int kRearLeftChannel = 2;
  private static final int kFrontRightChannel = 4;
  private static final int kRearRightChannel = 1;

  private static final int kJoystickChannel = 0;
  
  private XboxController joystick;

  private TalonSRX frontLeft;
  private TalonSRX rearLeft;
  private TalonSRX frontRight;
  private TalonSRX rearRight;

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
    DriveControl driveControl = MecanumDrive.drive(joystick.getX(Hand.kRight), joystick.getY(Hand.kRight), joystick.getX(Hand.kLeft));
    
    frontLeft.set(ControlMode.PercentOutput, driveControl.getFrontLeft());
    rearLeft.set(ControlMode.PercentOutput, driveControl.getRearLeft());
    frontRight.set(ControlMode.PercentOutput, driveControl.getFrontRight());
    rearRight.set(ControlMode.PercentOutput, driveControl.getRearRight());
  }
}
