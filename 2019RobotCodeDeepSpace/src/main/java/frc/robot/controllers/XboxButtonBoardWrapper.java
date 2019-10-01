package frc.robot.controllers;

import static frc.robot.utils.Utils.*;

import frc.robot.RobotMap;

/**
 * A class to drive the robot using the Xbox Controller for driving and arm
 * control and the F310 controller for wrist and intake control
 * 
 * @author Jordan
 */
public class XboxButtonBoardWrapper extends ControllerBase {

  protected ButtonBoard buttonBoard = RobotMap.buttonBoard;

  private static final int INTAKE_BUTTON = 12;
  private static final int SHOOT_BUTTON = 11;
  private static final int GRAB_BUTTON = 10;
  private static final int KICK_BUTTON = 8;

  private static final int PRESET_BUTTON_HATCH_GROUND = 9;
  private static final int PRESET_BUTTON_HATCH_1 = 6;
  private static final int PRESET_BUTTON_HATCH_2 = 3;
  private static final int PRESET_BUTTON_CARGO_GROUND = 7;
  private static final int PRESET_BUTTON_CARGO_1 = 4;
  private static final int PRESET_BUTTON_CARGO_2 = 1;
  private static final int PRESET_BUTTON_CARGO_BAY = 5;

  @Override
  public double getWristSpeed() {
    double wristspeed = buttonBoard.getRawAxis(1);
    wristspeed = clip(wristspeed, -1, 1);
    return wristspeed;
  }

  @Override
  public boolean getIntakeCargo() {
    return buttonBoard.getButton(INTAKE_BUTTON);
  }

  @Override
  public boolean getShootCargo() {
    return buttonBoard.getButton(SHOOT_BUTTON);
  }

  @Override
  public boolean getGrabHatch() {
    return buttonBoard.getButton(GRAB_BUTTON);
  }

  @Override
  public boolean getKick() {
    return buttonBoard.getButton(KICK_BUTTON);
  }

  @Override
  public PresetOption getSelectedPreset() {
    if (buttonBoard.getButton(PRESET_BUTTON_HATCH_GROUND)) {
      return PresetOption.HATCH_GROUND;
    } else if (buttonBoard.getButton(PRESET_BUTTON_HATCH_1)) {
      return PresetOption.HATCH_1;
    } else if (buttonBoard.getButton(PRESET_BUTTON_HATCH_2)) {
      return PresetOption.HATCH_2;
    } else if (buttonBoard.getButton(PRESET_BUTTON_CARGO_GROUND)) {
      return PresetOption.CARGO_GROUND;
    } else if (buttonBoard.getButton(PRESET_BUTTON_CARGO_1)) {
      return PresetOption.CARGO_1;
    } else if (buttonBoard.getButton(PRESET_BUTTON_CARGO_2)) {
      return PresetOption.CARGO_2;
    } else if (buttonBoard.getButton(PRESET_BUTTON_CARGO_BAY)) {
      return PresetOption.CARGO_BAY;
    } else {
      return PresetOption.NONE;
    }
  }

}