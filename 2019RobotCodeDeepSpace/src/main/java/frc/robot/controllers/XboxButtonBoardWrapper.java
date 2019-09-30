package frc.robot.controllers;

import static frc.robot.utils.Utils.*;

/**
 * A class to drive the robot using the Xbox Controller for driving and arm
 * control and the F310 controller for wrist and intake control
 * 
 * @author Jordan
 */
public class XboxButtonBoardWrapper extends ControllerBase {

  @Override
  public double getWristSpeed() {
    double wristspeed = buttonBoard.getRawAxis(1);
    wristspeed = clip(wristspeed, -1, 1);
    return wristspeed;
  }

  @Override
  public boolean getIntakeCargo() {
    return buttonBoard.getButton(12);
  }

  @Override
  public boolean getShootCargo() {
    return buttonBoard.getButton(11);
  }

  @Override
  public boolean getGrabHatch() {
    return buttonBoard.getButton(10);
  }

  @Override
  public boolean getKick() {
    return buttonBoard.getButton(8);
  }

  @Override
  public boolean getSelectPresetHatchGround() {
    return buttonBoard.getButton(9);
  }

  @Override
  public boolean getSelectPresetHatch1() {
    return buttonBoard.getButton(6);
  }

  @Override
  public boolean getSelectPresetHatch2() {
    return buttonBoard.getButton(3);
  }

  @Override
  public boolean getSelectPresetCargoGround() {
    return buttonBoard.getButton(7);
  }

  @Override
  public boolean getSelectPresetCargo1() {
    return buttonBoard.getButton(4);
  }

  @Override
  public boolean getSelectPresetCargo2() {
    return buttonBoard.getButton(1);
  }

  @Override
  public boolean getSelectPresetCargoBay() {
    return buttonBoard.getButton(5);
  }

}