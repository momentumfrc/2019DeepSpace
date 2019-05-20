package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.*;

public class LimelightData {
  private double xCoord = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
  private double yCoord = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
  private double targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
  private double skew = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);

  // Estimating distance Constants//
  private static final double CAMERA_ANGLE = 0;
  private static final double CAMERA_HEIGHT = 0;
  private static final double TARGET_HEIGHT = 0;
  // http://docs.limelightvision.io/en/latest/cs_estimating_distance.html

  public double dist() {
    double targetAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double height = TARGET_HEIGHT - CAMERA_HEIGHT;
    double angle = Math.tan(CAMERA_ANGLE + targetAngle);
    double dist = (height) / (angle);
    return dist;
  }

  public double xCoord() {
    xCoord = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    return xCoord;
  }

  public double yCoord() {
    yCoord = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    return yCoord;
  }

  public double targetArea() {
    targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    return targetArea;
  }

  public double skew() {
    skew = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);
    return skew;
  }

}
