package frc.robot.controllers;

public interface DriveController {

  /**
   * Gets the current speed the robot should move at
   * 
   * @return The current desired speed of the robot, between [-1, 1]
   */
  abstract public double getMoveRequest();

  /**
   * Gets the current rate the robot should turn at
   * 
   * @return The current desired turn rate of the robot, between [-1, 1]
   */
  abstract public double getTurnRequest();

  /**
   * Gets the maximum desired speed of the robot
   * 
   * @return The maximum desired speed of the robot, between [0, 1]
   */
  abstract public double getSpeedLimiter();

  /**
   * Gets if the robot should reverse direction
   * 
   * @return Wether or not the robot should reverse front and back
   */
  abstract public boolean getReverseDirectionPressed();

  /**
   * Get desired manual speed of the arm
   * 
   * @return The desired manual speed of the arm
   */
  abstract public double getArmSpeed();

  /**
   * Get the desired manual speed of the wrist
   * 
   * @return The desired manual speed of the wrist
   */
  abstract public double getWristSpeed();

  /**
   * Get if the robot should start or continue intaking cargo
   * 
   * @return If the robot should start or continue intaking cargo
   */
  abstract public boolean getIntakeCargo();

  /**
   * Get if the robot should start shooting cargo
   * 
   * @return If the robot should start shooting cargo
   */
  abstract public boolean getShootCargo();

  /**
   * Get if the robot should flip down the hatch flipper to grab a hatch
   * 
   * @return If the robot should flip down the hatch flipper
   */
  abstract public boolean getGrabHatch();

  /**
   * Get if the robot should extend the hatch kicker
   * 
   * @return If the robot should extend the hatch kicker
   */
  abstract public boolean getKick();

  /**
   * Get if the arm should move to the next preset up
   * 
   * @return If the arm should move to the next preset up
   */
  public abstract boolean getPresetIncreasedPressed();

  /**
   * Get if the arm should move to the next preset down
   * 
   * @return If the arm should move to the next preset down
   */
  public abstract boolean getPresetDecreasedPressed();

  /**
   * Get if the arm should overwrite the current preset with the current position
   * of the arm
   * 
   * @return If the arm should overwrite the current preset with the current
   *         position of the arm
   */
  public abstract boolean getSavePreset();

  /**
   * Get if the arm should switch to presets for manipulating hatch gamepeices
   * 
   * @return If the arm should switch to hatch presets
   */
  public abstract boolean getHatchGamepiecePressed();

  /**
   * Get if the arm should switch to presets for manipulating cargo gamepeices
   * 
   * @return If the arm should switch to cargo presets
   */
  public abstract boolean getCargoGamepiecePressed();

}