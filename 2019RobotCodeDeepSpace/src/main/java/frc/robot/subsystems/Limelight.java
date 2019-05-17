package frc.robot.subsystems;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Limelight extends Subsystem {
  /**
   *
   */

  public double validTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
  public double xOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
  public double yOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);

  public static final double RANGE_X = 29.8;
  public static final double RANGE_Y = 24.85;
  public static final double MIN_DIST = 0;

  // Estimating distance//
  private static final double CAMERA_ANGLE = 0; // a1
  private static final double CAMERA_HEIGHT = 0; // h1
  private static final double TARGET_HEIGHT = 0; // h2
  private static final double TARGET_ANGLE = 0; // a2
  private double height = TARGET_HEIGHT - CAMERA_HEIGHT;
  private double angle = Math.tan(CAMERA_ANGLE + TARGET_ANGLE);
  public double targetDistance = (height) / (angle);

  // http://docs.limelightvision.io/en/latest/cs_estimating_distance.html

  @Override
  protected void initDefaultCommand() {

  }

  public boolean targetIsValid() {
    if (validTarget == 1 && Math.abs(xOffset) <= RANGE_X && Math.abs(yOffset) <= RANGE_Y && targetDistance > MIN_DIST) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * detects whether or not the Limelight has detected a valid target and returns
   * the position of the target in an array of [x, y, z]
   */
  public double[] targetPos() {
    if (targetIsValid()) {
      double[] pos = { xOffset, yOffset, targetDistance };
      return pos;
    } else {
      return null;
    }
  }

  public boolean atTarget() {
    if (xOffset == 0 && targetDistance == MIN_DIST) {
      return true;
    }
    return false;
  }

}