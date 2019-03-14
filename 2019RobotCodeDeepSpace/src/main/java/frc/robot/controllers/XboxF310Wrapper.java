package frc.robot.controllers;

import frc.robot.RobotMap;
import frc.robot.utils.Utils;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import org.usfirst.frc.team4999.controllers.LogitechF310;

public class XboxF310Wrapper implements DriveController {

  private XboxController xbox = RobotMap.xbox;
  private LogitechF310 logitech = RobotMap.f310;

  private static final double MOVE_CURVE = 2.5;
  private static final double TURN_CURVE = 2.5;

  private static final double DEADZONE = 0.1;

  private static final double[] SPEEDS = { .125, .25, .375, .5, .625, .875, 1 };
  private int currentSpeed = SPEEDS.length - 1;

  private static final double MAX_ARM_SPEED = .8;
  private static final double MAX_WRIST_SPEED = .8;

  // XBOX CONTROLS//
  @Override
  public double getMoveRequest() {
    double moveRequest = xbox.getY(XboxController.Hand.kLeft);
    moveRequest = Utils.deadzone(moveRequest, DEADZONE);
    moveRequest = Utils.curve(moveRequest, MOVE_CURVE);
    return moveRequest;
  }

  @Override
  public double getTurnRequest() {
    double turnRequest = xbox.getX(XboxController.Hand.kRight);
    turnRequest = Utils.deadzone(turnRequest, DEADZONE);
    turnRequest = Utils.curve(turnRequest, TURN_CURVE);
    return turnRequest;
  }

  @Override
  public double getSpeedLimiter() {
    if (xbox.getYButtonPressed() && currentSpeed < SPEEDS.length - 1) {
      ++currentSpeed;
    } else if (xbox.getXButtonPressed() && currentSpeed > 0) {
      --currentSpeed;
    }

    return SPEEDS[currentSpeed];
  }

  @Override
  public boolean getReverseDirectionPressed() {
    return xbox.getBButtonPressed();
  }

  @Override
  public double getArmSpeed() {
    double armSpeed = Utils.map(xbox.getTriggerAxis(Hand.kLeft), -1, 1, -MAX_ARM_SPEED, 0)
        + Utils.map(xbox.getTriggerAxis(Hand.kRight), -1, 1, 0, MAX_ARM_SPEED);
    return armSpeed;
  }

  @Override
  public boolean getPresetIncreasedPressed() {
    return xbox.getBumperPressed(Hand.kRight);
  }

  @Override
  public boolean getPresetDecreasedPressed() {
    return xbox.getBumperPressed(Hand.kLeft);
  }

  @Override
  public boolean getHatchGamepiecePressed() {
    return xbox.getBButtonPressed();
  }

  @Override
  public boolean getCargoGamepiecePressed() {
    return xbox.getYButtonPressed();
  }

  @Override
  public boolean getIntakeCargo() {
    return false;
  }

  @Override
  public boolean getShootCargo() {
    return false;
  }

  @Override
  public boolean getGrabHatch() {
    return false;
  }

  @Override
  public boolean getKick() {
    return false;
  }

  @Override
  public boolean getSavePreset() {
    return xbox.getStartButton() && xbox.getBackButton();
  }

  // F310 CONTROLS//
  @Override
  public double getWristSpeed() {
    double val = Utils.clip(logitech.getY(Hand.kLeft) + logitech.getY(Hand.kRight), -1, 1);
    return Utils.map(val, -1, 1, -MAX_WRIST_SPEED, MAX_WRIST_SPEED);
  }

}
