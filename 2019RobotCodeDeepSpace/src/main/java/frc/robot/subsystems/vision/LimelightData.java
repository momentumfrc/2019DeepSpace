package frc.robot.subsystems.vision;

public class LimelightData {

  private final double xCoord;
  private final double yCoord;
  private final double valid;
  private final boolean validTarget;
  private final double dist;

  // Estimating distance Constants//
  private static final double CAMERA_ANGLE = 0;
  private static final double CAMERA_HEIGHT = 0;
  private static final double TARGET_HEIGHT = 0;
  // http://docs.limelightvision.io/en/latest/cs_estimating_distance.html

  LimelightData(double valid, double xCoord, double yCoord) {
    this.valid = valid;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    validTarget = valid == 1;

    double height = TARGET_HEIGHT - CAMERA_HEIGHT;
    double angle = Math.tan(CAMERA_ANGLE + yCoord);
    dist = (height) / (angle);
  }

  public double dist() {
    return dist;
  }

  public double xCoord() {
    return xCoord;
  }

  public double yCoord() {
    return yCoord;
  }

  public boolean valid() {
    return validTarget;
  }
}
