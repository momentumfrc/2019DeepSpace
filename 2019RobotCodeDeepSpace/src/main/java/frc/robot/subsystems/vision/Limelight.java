package frc.robot.subsystems.vision;

import java.util.Map;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.HttpCamera.HttpCameraKind;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;

public class Limelight extends Subsystem {
  private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private final NetworkTableEntry xCoordEntry = table.getEntry("tx");
  private final NetworkTableEntry yCoordEntry = table.getEntry("ty");
  private final NetworkTableEntry validEntry = table.getEntry("tv");

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
  private static final double CAMERA_HEIGHT = 26; // inches
  private static final double TARGET_HEIGHT = 29.5; // inches
  // http://docs.limelightvision.io/en/latest/cs_estimating_distance.html

  private NetworkTableEntry wx, wy, wv, wa, wd;

  @Override
  protected void initDefaultCommand() {

  }

  public Limelight(ShuffleboardTab tab, int col, int row) {
    ShuffleboardLayout layout = tab.getLayout("Limelight", BuiltInLayouts.kGrid).withPosition(col, row).withSize(2, 3);
    wx = layout.add("X", 0).withPosition(0, 0).getEntry();
    wy = layout.add("Y", 0).withPosition(0, 1).getEntry();
    wv = layout.add("Valid", 0).withPosition(0, 2).getEntry();
    wa = layout.add("Angle", 0).withPosition(1, 0).getEntry();
    wd = layout.add("Distance", 0).withPosition(1, 1).getEntry();

    RobotMap.matchTab.add(new HttpCamera("Limelight", "http://10.49.99.11:5800/", HttpCameraKind.kMJPGStreamer))
        .withPosition(6, 0).withSize(3, 3).withProperties(Map.of("Show controls", false));
  }

  public LimelightData getData() {
    return new LimelightData(validEntry.getDouble(0), xCoordEntry.getDouble(0), yCoordEntry.getDouble(0));
  }

  public class LimelightData {

    private final double xCoord;
    private final double yCoord;
    private final boolean validTarget;
    private final double dist;

    private LimelightData(double valid, double xAngle, double yAngle) {
      this.xCoord = xAngle;
      this.yCoord = yAngle;

      double height = TARGET_HEIGHT - CAMERA_HEIGHT;
      double slope = Math.tan(CAMERA_ANGLE + yAngle);
      if (slope > 0) {
        dist = height / slope;
      } else {
        dist = 0;
        valid = 0;
      }
      this.validTarget = valid == 1;

      wx.setDouble(xAngle);
      wy.setDouble(yAngle);
      wv.setDouble(valid);
      wa.setDouble(slope);
      wv.setDouble(dist);
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
