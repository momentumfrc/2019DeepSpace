package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Limelight extends Subsystem {
  private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private final NetworkTableEntry xCoordEntry = table.getEntry("tx");
  private final NetworkTableEntry yCoordEntry = table.getEntry("ty");
  private final NetworkTableEntry validEntry = table.getEntry("tv");

  private final NetworkTableEntry ledMode = table.getEntry("ledMode");

  public static final double RANGE_X = 29.8; // The range of values given by "tx" is from -29.8 to 29.8 degrees
  public static final double RANGE_Y = 24.85; // The range of values given by "ty" is from -24.85 to 24.85 degrees

  public static final double TARGET_DIST = 3; // This is the distance that the robot should be at for the target to be
                                              // met
  public static final double DIST_ERR = .1; // This is an arbitrary number(for now) that the distance value of the robot
                                            // should be off by when at the target
  public static final double X_ERR = 1; // This is an arbitrary number(for now) that the x value of the robot should be
                                        // off by when at the target
  public static final double Y_ERR = 1; // This is an arbitrary number(for now) that the y value of the robot should be
                                        // off by when at the target

  // Estimating distance Constants//
  private static final double CAMERA_ANGLE = 0;
  private static final double CAMERA_HEIGHT = 0;
  private static final double TARGET_HEIGHT = 0;
  // http://docs.limelightvision.io/en/latest/cs_estimating_distance.html

  public static enum LED_MODE {
    DEFAULT(0), OFF(1), BLINK(2), ON(3);

    private int value;

    public int getValue() {
      return value;
    }

    private LED_MODE(int value) {
      this.value = value;
    }
  };

  public void setLEDMode(LED_MODE mode) {
    ledMode.setNumber(mode.getValue());
  }

  @Override
  protected void initDefaultCommand() {

  }

  public LimelightData getData() {
    return new LimelightData(validEntry.getDouble(0), xCoordEntry.getDouble(0), yCoordEntry.getDouble(0));
  }

  public class LimelightData {

    private final double xCoord;
    private final double yCoord;
    private final double valid;
    private final boolean validTarget;
    private final double dist;

    private LimelightData(double valid, double xCoord, double yCoord) {
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

    public boolean targetMet() {
      if (valid()) {
        double x = Math.abs(xCoord());
        double y = Math.abs(yCoord());
        double d = Math.abs(dist() - DIST_ERR);
        if (x <= X_ERR && y <= Y_ERR && d <= TARGET_DIST) {
          return true;
        }
      }
      return false;
    }
  }

}
