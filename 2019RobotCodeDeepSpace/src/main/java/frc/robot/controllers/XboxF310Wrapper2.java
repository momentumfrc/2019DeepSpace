package frc.robot.controllers;

import static frc.robot.utils.Utils.curve;
import static frc.robot.utils.Utils.deadzone;

import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * A class to drive the robot using the Xbox Controller for driving control and
 * the F310 controller for arm, wrist, and intake control
 * 
 * @author Jordan
 */
public class XboxF310Wrapper2 extends ControllerBase {

  @Override
  public double getArmSpeed() {
    double armspeed = f310.getY(Hand.kLeft);
    armspeed = deadzone(armspeed, DEADZONE);
    armspeed = curve(armspeed, ARM_CURVE);
    armspeed = armspeed * (armspeed > 0 ? MAX_ARM_SPEED_UP : MAX_ARM_SPEED_DOWN);
    return armspeed;
  }

  @Override
  public double getWristSpeed() {
    double wristspeed = f310.getY(Hand.kRight);
    wristspeed = deadzone(wristspeed, DEADZONE);
    wristspeed = curve(wristspeed, WRIST_CURVE);
    return wristspeed;
  }

  @Override
  public boolean getPresetIncreasedPressed() {
    boolean isUp = f310.getPOV() == 0;
    boolean justPressed = isUp && !lastPresetUpPressed;
    lastPresetUpPressed = isUp;
    return justPressed;
  }

  @Override
  public boolean getPresetDecreasedPressed() {
    boolean isDown = f310.getPOV() == 180;
    boolean justPressed = isDown && !lastPresetDownPressed;
    lastPresetDownPressed = isDown;
    return justPressed;
  }

  @Override
  public boolean getSavePreset() {
    return f310.getBackButton() && f310.getStartButton();
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