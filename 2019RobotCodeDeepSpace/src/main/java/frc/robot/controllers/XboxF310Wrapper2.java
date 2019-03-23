package frc.robot.controllers;

import org.usfirst.frc.team4999.controllers.LogitechF310;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.RobotMap;
import static frc.robot.utils.Utils.*;

/**
 * A class to drive the robot using the Xbox Controller for driving control and
 * the F310 controller for arm, wrist, and intake control
 * 
 * @author Jordan
 */
public class XboxF310Wrapper2 implements DriveController {

  private XboxController xbox = RobotMap.xbox;
  private LogitechF310 f310 = RobotMap.f310;

  private static final double MOVE_CURVE = 2.5;
  private static final double TURN_CURVE = 2.5;
  private static final double ARM_CURVE = 2.5;
  private static final double WRIST_CURVE = 2.5;

  private static final double DEADZONE = 0.1;

  private static final double[] SPEEDS = { .125, .25, .375, .5, .625, .875, 1 };
  private int currentSpeed = SPEEDS.length - 1;

  private static final double MAX_ARM_SPEED_UP = .4;
  private static final double MAX_ARM_SPEED_DOWN = .4;
  private static final double MAX_WRIST_SPEED = .2;

  @Override
  public double getMoveRequest() {
    double moveRequest = xbox.getY(XboxController.Hand.kLeft);
    moveRequest = deadzone(moveRequest, DEADZONE);
    moveRequest = curve(moveRequest, MOVE_CURVE);
    return moveRequest;
  }

  @Override
  public double getTurnRequest() {
    double turnRequest = xbox.getX(XboxController.Hand.kRight);
    turnRequest = deadzone(turnRequest, DEADZONE);
    turnRequest = curve(turnRequest, TURN_CURVE);
    return turnRequest;
  }

  @Override
  public double getSpeedLimiter() {
    if (xbox.getYButtonPressed() && currentSpeed < SPEEDS.length - 1)
      currentSpeed++;
    else if (xbox.getXButtonPressed() && currentSpeed > 0)
      currentSpeed--;
    return SPEEDS[currentSpeed];
  }

  @Override
  public boolean getReverseDirectionPressed() {
    return xbox.getBButtonPressed();
  }

  @Override
  public double getArmSpeed() {
    double armspeed = f310.getY(Hand.kLeft);
    deadzone(armspeed, DEADZONE);
    curve(armspeed, ARM_CURVE);
    if (armspeed < 0)
      armspeed = map(armspeed, -1, 1, -MAX_ARM_SPEED_UP, MAX_ARM_SPEED_UP);
    else
      armspeed = map(armspeed, -1, 1, -MAX_ARM_SPEED_DOWN, MAX_ARM_SPEED_DOWN);
    return armspeed;
  }

  @Override
  public double getWristSpeed() {
    double wristspeed = f310.getY(Hand.kRight);
    deadzone(wristspeed, DEADZONE);
    curve(wristspeed, WRIST_CURVE);
    map(wristspeed, -1, 1, -MAX_WRIST_SPEED, MAX_WRIST_SPEED);
    return -1 * wristspeed;
  }

  @Override
  public boolean getIntakeCargo() {
    return f310.getBumper(Hand.kLeft);
  }

  @Override
  public boolean getShootCargo() {
    return f310.getBumper(Hand.kRight);
  }

  @Override
  public boolean getGrabHatch() {
    return f310.getYButton();
  }

  @Override
  public boolean getKick() {
    return f310.getXButton();
  }

  @Override
  public boolean getPresetIncreasedPressed() {
    return f310.getPOV() == 0;
  }

  @Override
  public boolean getPresetDecreasedPressed() {
    return f310.getPOV() == 180;
  }

  @Override
  public boolean getSavePreset() {
    return f310.getBackButton();
  }

  @Override
  public boolean getHatchGamepiecePressed() {
    return f310.getPOV() == 270;
  }

  @Override
  public boolean getCargoGamepiecePressed() {
    return f310.getPOV() == 90;
  }

}