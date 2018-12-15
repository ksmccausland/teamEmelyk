package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Robot extends IterativeRobot {
  private static final int kFrontLeftChannel = 3;
  private static final int kRearLeftChannel = 2;
  private static final int kFrontRightChannel = 4;
  private static final int kRearRightChannel = 1;
  private static final int kArmChannel = 5;

  private TalonSRX frontLeft;
  private TalonSRX rearLeft;
  private TalonSRX frontRight;
  private TalonSRX rearRight;
  private TalonSRX arm;

  private static final int kJoystickChannel = 0;
  private XboxController joystick;

  // TODO: Check the ping and echo channels
  private static final int kPingChannel = 1;
  private static final int kEchoChannel = 1;
  private Ultrasonic ultrasonic;

  // state for arm
  private static boolean currentlyLifting;
  private static boolean currentlyLowering;

  // We need to measure what this height is
  private static final double kRaiseHeight_MM = 150; // ?
  private static final double kLowerHeight_MM = 20; // ?

  @Override
  public void robotInit() {
    frontLeft = new TalonSRX(kFrontLeftChannel);
    rearLeft = new TalonSRX(kRearLeftChannel);
    frontRight = new TalonSRX(kFrontRightChannel);
    rearRight = new TalonSRX(kRearRightChannel);
    arm = new TalonSRX(kArmChannel);

    // invert drive train from tank drive
    frontLeft.setInverted(true);
    rearRight.setInverted(true);

    // joystick
    joystick = new XboxController(kJoystickChannel);

    // ultrasonic
    ultrasonic = new Ultrasonic(kPingChannel, kEchoChannel);
    ultrasonic.setAutomaticMode(true); // Do we want the automatic pinging algorithm?
  }

  @Override
  public void teleopPeriodic() {
    // drive
    DriveControl driveControl = MecanumDrive.drive(joystick.getX(Hand.kRight), joystick.getY(Hand.kRight), joystick.getX(Hand.kLeft));
    
    frontLeft.set(ControlMode.PercentOutput, driveControl.getFrontLeft());
    rearLeft.set(ControlMode.PercentOutput, driveControl.getRearLeft());
    frontRight.set(ControlMode.PercentOutput, driveControl.getFrontRight());
    rearRight.set(ControlMode.PercentOutput, driveControl.getRearRight());

    // Check if we are moving the arm
    boolean aButton = joystick.getAButton();
    boolean bButton = joystick.getBButton();

    if (aButton || bButton) {
     handleArm(aButton, bButton);
    }

    

  }

  private void handleArm(boolean aButton, boolean bButton) {
    // make sure we aren't trying to lift and lower at the same time
    if (aButton) {
      currentlyLifting = true;
      currentlyLowering = false;
    } else if (bButton) {
      currentlyLowering = true;
      currentlyLifting = false;
    }

    // only turn on ultrasonic if we are lifting/lowering
    double armPower = 0;
    if (currentlyLifting || currentlyLowering) {
      double range = ultrasonic.getRangeMM();
      if (currentlyLifting && range < kRaiseHeight_MM) {
        armPower = 1;
      }

      if (currentlyLowering && range > kLowerHeight_MM) {
        armPower = -1;
      }
    }

    // move arm
    arm.set(ControlMode.PercentOutput, armPower);

    // reset if no longer need power
    if (armPower == 0) {
      currentlyLifting = false;
      currentlyLowering = false;
    }
  }
}
