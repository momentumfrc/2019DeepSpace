package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Limelight extends Subsystem {
  public LimelightData data;

  public double validTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);

  public static final double RANGE_X = 29.8; // The range of values given by "tx" is from -29.8 to 29.8 degrees
  public static final double RANGE_Y = 24.85; // The range of values given by "ty" is from -24.85 to 24.85 degrees

  public static final double TARGET_DIST = 1; // This is the distance that the robot should be at for the target to be
                                              // met
  public static final double DIST_ERR = .1; // This is an arbitrary number(for now) that the distance value of the robot
                                            // should be off by when at the target
  public static final double X_ERR = 1; // This is an arbitrary number(for now) that the x value of the robot should be
                                        // off by when at the target
  public static final double Y_ERR = 1; // This is an arbitrary number(for now) that the y value of the robot should be
                                        // off by when at the target

  @Override
  protected void initDefaultCommand() {

  }

  public boolean targetMet() {
    if (data.valid()) {
      double x = Math.abs(data.xCoord());
      double y = Math.abs(data.yCoord());
      double d = data.dist();
      if (x <= X_ERR && y <= Y_ERR && d + DIST_ERR <= TARGET_DIST || d - DIST_ERR >= TARGET_DIST) {
        return true;
      }
    }
    return false;
  }

}