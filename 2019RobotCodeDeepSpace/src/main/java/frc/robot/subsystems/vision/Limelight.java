package frc.robot.subsystems.vision;

import java.util.Map;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import frc.robot.RobotMap;

public class Limelight extends Subsystem {
  private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private final NetworkTableEntry xCoordEntry = table.getEntry("tx");
  private final NetworkTableEntry yCoordEntry = table.getEntry("ty");
  private final NetworkTableEntry validEntry = table.getEntry("tv");

  private NetworkTableEntry txWidget, tyWidget, validWidget, distanceWidget;

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

  // CAMREA_ANGLE = Math.atan((TARGET_HEIGHT - CAMERA_HEIGHT) / distance ) -
  // yCoordEntry.getNumber();

  public Limelight() {
    ShuffleboardLayout layout = RobotMap.testTab.getLayout("Limelight", BuiltInLayouts.kGrid).withSize(4, 4)
        .withPosition(8, 0);
    // This needs to be invesitgated. I'm not sure if the roborio is inefficiently
    // re-streaming the limelight's stream
    layout.add(new HttpCamera("Limelight", "10.49.99.11:5800", HttpCamera.HttpCameraKind.kMJPGStreamer))
        .withPosition(0, 0).withSize(4, 3).withProperties(Map.of("Show controls", false));
    txWidget = layout.add("tx", -1).withPosition(0, 3).withSize(1, 1).getEntry();
    tyWidget = layout.add("ty", -1).withPosition(1, 3).withSize(1, 1).getEntry();
    distanceWidget = layout.add("distance", -1).withPosition(2, 3).withSize(1, 1).getEntry();
    validWidget = layout.add("valid", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(3, 3).withSize(1, 1)
        .getEntry();

  }

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

  private double calculateDistance(double yCoord) {
    double height = TARGET_HEIGHT - CAMERA_HEIGHT;
    double angle = Math.tan(CAMERA_ANGLE + yCoord);
    double distance = (height) / (angle);
    return distance;
  }

  public LimelightData getData() {
    double xCoord = validEntry.getDouble(0);
    double yCoord = yCoordEntry.getDouble(0);
    double distance = calculateDistance(yCoord);
    boolean valid = validEntry.getDouble(0) == 1;

    txWidget.setNumber(xCoord);
    tyWidget.setNumber(yCoord);
    distanceWidget.setNumber(distance);
    validWidget.setBoolean(valid);

    return new LimelightData(valid, xCoord, yCoord, distance);
  }

  public class LimelightData {

    private final double xCoord;
    private final double yCoord;
    private final boolean validTarget;
    private final double dist;

    private LimelightData(boolean valid, double xCoord, double yCoord, double dist) {
      this.xCoord = xCoord;
      this.yCoord = yCoord;
      this.validTarget = valid;
      this.dist = dist;
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

    @Override
    public String toString() {
      return String.format("X:%.4f Y:%.4f dist:%.2f valid:%d", xCoord, yCoord, dist, validTarget ? 1 : 0);
    }
  }

}
