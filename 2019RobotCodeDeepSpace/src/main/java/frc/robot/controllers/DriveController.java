package frc.robot.controllers;

public interface DriveController {

  public enum PresetOption {
    NONE, HATCH_GROUND, HATCH_1, HATCH_2, CARGO_GROUND, CARGO_1, CARGO_2, CARGO_BAY
  };

  /**
   * Gets the current speed the robot should move at
   * 
   * @return The current desired speed of the robot, between [-1, 1]
   */
  public double getMoveRequest();

  /**
   * Gets the current rate the robot should turn at
   * 
   * @return The current desired turn rate of the robot, between [-1, 1]
   */
  public double getTurnRequest();

  /**
   * Gets the maximum desired speed of the robot
   * 
   * @return The maximum desired speed of the robot, between [0, 1]
   */
  public double getSpeedLimiter();

  /**
   * Gets if the robot should reverse direction
   * 
   * @return Whether or not the robot should reverse front and back
   */
  public boolean getReverseDirectionPressed();

  /**
   * Get desired manual speed of the arm
   * 
   * @return The desired manual speed of the arm
   */
  public double getArmSpeed();

  /**
   * Get the desired manual speed of the wrist
   * 
   * @return The desired manual speed of the wrist
   */
  public double getWristSpeed();

  /**
   * Get if the robot should start or continue intaking cargo
   * 
   * @return If the robot should start or continue intaking cargo
   */
  public boolean getIntakeCargo();

  /**
   * Get if the robot should start shooting cargo
   * 
   * @return If the robot should start shooting cargo
   */
  public boolean getShootCargo();

  /**
   * Get if the robot should flip down the hatch flipper to grab a hatch
   * 
   * @return If the robot should flip down the hatch flipper
   */
  public boolean getGrabHatch();

  /**
   * Get if the robot should extend the hatch kicker
   * 
   * @return If the robot should extend the hatch kicker
   */
  public boolean getKick();

  /**
   * Get if the arm should move to the next preset up
   * 
   * @return If the arm should move to the next preset up
   */
  public boolean getPresetIncreasedPressed();

  /**
   * Get if the arm should move to the next preset down
   * 
   * @return If the arm should move to the next preset down
   */
  public boolean getPresetDecreasedPressed();

  /**
   * Get if the arm should overwrite the current preset with the current position
   * of the arm
   * 
   * @return If the arm should overwrite the current preset with the current
   *         position of the arm
   */
  public boolean getSavePreset();

  /**
   * Get if the arm should switch to presets for manipulating hatch gamepeices
   * 
   * @return If the arm should switch to hatch presets
   */
  public boolean getHatchGamepiecePressed();

  /**
   * Get if the arm should switch to presets for manipulating cargo gamepeices
   * 
   * @return If the arm should switch to cargo presets
   */
  public boolean getCargoGamepiecePressed();

  /**
   * Get if the arm/wrist should select a specific preset by name
   * 
   * @return The preset to select
   */
  public abstract PresetOption getSelectedPreset();
}
