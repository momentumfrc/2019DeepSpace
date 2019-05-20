package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Limelight extends Subsystem {
  public LimelightData data;

  public double validTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);

  public static final double RANGE_X = 29.8; // The range of values given by "tx" is from -29.8 to 29.8 degrees
  public static final double RANGE_Y = 24.85; // The range of values given by "ty" is from -24.85 to 24.85 degrees
  public static final double MIN_DIST = 0; // This is the closest that something could get to the camera

  @Override
  protected void initDefaultCommand() {

  }

  /* detects whether at target is valid or not */
  public boolean targetIsValid() {
    if (validTarget == 1 && Math.abs(data.xCoord()) <= RANGE_X && Math.abs(data.yCoord()) <= RANGE_Y
        && data.dist() > MIN_DIST) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * checks whether or not the Limelight has detected a valid target and returns
   * the position of the target in an array of [x, y, z]
   */

  /*
   * public double[] targetPos() { if (targetIsValid()) { double[] pos = {
   * Data.xCoord(), Data.yCoord(), targetDistance }; return pos; } else { return
   * null; } }
   */

  public boolean atTarget() {
    if (data.xCoord() == 0 && data.dist() == MIN_DIST) {
      return true;
    }
    return false;
  }
}