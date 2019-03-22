package frc.robot.controllers;

import org.usfirst.frc.team4999.controllers.LogitechF310;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.RobotMap;
import static frc.robot.utils.Utils.*;

/**
 * A class to drive the robot using the Xbox Controller for driving and arm
 * control and the F310 controller for wrist and intake control
 * 
 * @author Jordan
 */
public class XboxF310Wrapper implements DriveController {

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
    double arm_down = xbox.getTriggerAxis(Hand.kLeft);
    double arm_up = xbox.getTriggerAxis(Hand.kRight);
    double armspeed = arm_up - arm_down;
    armspeed = deadzone(armspeed, DEADZONE);
    armspeed = curve(armspeed, ARM_CURVE);
    if (armspeed < 0)
      armspeed = map(armspeed, -1, 1, -MAX_ARM_SPEED_UP, MAX_ARM_SPEED_UP);
    else
      armspeed = map(armspeed, -1, 1, -MAX_ARM_SPEED_DOWN, MAX_ARM_SPEED_DOWN);
    return armspeed;
  }

  @Override
  public double getWristSpeed() {
    double left_wrist = f310.getY(Hand.kLeft);
    left_wrist = deadzone(left_wrist, DEADZONE);
    left_wrist = curve(left_wrist, WRIST_CURVE);
    double right_wrist = f310.getY(Hand.kRight);
    right_wrist = deadzone(right_wrist, DEADZONE);
    right_wrist = curve(right_wrist, WRIST_CURVE);
    double wristspeed = left_wrist + right_wrist;
    wristspeed = clip(wristspeed, -1, 1);
    wristspeed = map(wristspeed, -1, 1, -MAX_WRIST_SPEED, MAX_WRIST_SPEED);
    return wristspeed;
  }

  @Override
  public boolean getIntakeCargo() {
    return f310.getAButton();
  }

  @Override
  public boolean getShootCargo() {
    return f310.getBButton();
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
    return xbox.getPOV() == 0;
  }

  @Override
  public boolean getPresetDecreasedPressed() {
    return xbox.getPOV() == 180;
  }

  @Override
  public boolean getSavePreset() {
    return xbox.getBackButton();
  }

  @Override
  public boolean getHatchGamepiecePressed() {
    return xbox.getPOV() == 270;
  }

  @Override
  public boolean getCargoGamepiecePressed() {
    return xbox.getPOV() == 90;
  }

}