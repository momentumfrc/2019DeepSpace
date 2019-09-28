package frc.robot.controllers;

/**
 * A class to drive the robot using the Xbox Controller for driving and arm
 * control and the F310 controller for wrist and intake control
 * 
 * @author Jordan
 */
public class XboxButtonBoardWrapper extends ControllerBase {

  @Override
  public boolean getSelectPresetHatchGround() {
    return buttonBoard.getButton(0);
  }

  @Override
  public boolean getSelectPresetHatch1() {
    return buttonBoard.getButton(1);
  }

  @Override
  public boolean getSelectPresetHatch2() {
    return buttonBoard.getButton(2);
  }

  @Override
  public boolean getSelectPresetCargoGround() {
    return buttonBoard.getButton(3);
  }

  @Override
  public boolean getSelectPresetCargo1() {
    return buttonBoard.getButton(4);
  }

  @Override
  public boolean getSelectPresetCargo2() {
    return buttonBoard.getButton(5);
  }

  @Override
  public boolean getSelectPresetCargoBay() {
    return buttonBoard.getButton(6);
  }

}