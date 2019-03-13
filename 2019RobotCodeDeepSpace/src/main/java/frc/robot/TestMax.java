package frc.robot;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.usfirst.frc.team4999.pid.SendableCANPIDController;

import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;

public class TestMax {
  private static CANSparkMax testMax = new CANSparkMax(14, MotorType.kBrushless);
  private static CANPIDController testMax_PID = testMax.getPIDController();

  private SendableCANPIDController sendablepid;

  private ControlChooser chooser = Robot.controlChooser;

  private double p;
  private double i;
  private double d;
  private double f;
  private double iZone;

  public void init() {

    try {
      sendablepid = new SendableCANPIDController("TestMax", testMax, 0, 0);

      LiveWindow.add(sendablepid);

      p = testMax_PID.getP(0);
      i = testMax_PID.getI(0);
      d = testMax_PID.getD(0);
      f = testMax_PID.getFF(0);
      iZone = testMax_PID.getIZone(0);

      SmartDashboard.putNumber("Max P", p);
      SmartDashboard.putNumber("Max I", i);
      SmartDashboard.putNumber("Max D", d);
      SmartDashboard.putNumber("Max F", f);
      SmartDashboard.putNumber("Max I Zone", iZone);

      SmartDashboard.getEntry("Max P").addListener(notification -> {
        double t = notification.getEntry().getDouble(p);
        if (t != p) {
          p = t;
          testMax_PID.setP(p, 0);
        }
      }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
      SmartDashboard.getEntry("Max i").addListener(notification -> {
        double t = notification.getEntry().getDouble(i);
        if (t != i) {
          i = t;
          testMax_PID.setI(i, 0);
        }
      }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
      SmartDashboard.getEntry("Max D").addListener(notification -> {
        double t = notification.getEntry().getDouble(d);
        if (t != d) {
          d = t;
          testMax_PID.setD(d, 0);
        }
      }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
      SmartDashboard.getEntry("Max F").addListener(notification -> {
        double t = notification.getEntry().getDouble(f);
        if (t != f) {
          f = t;
          testMax_PID.setFF(f, 0);
        }
      }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
      SmartDashboard.getEntry("Max F").addListener(notification -> {
        double t = notification.getEntry().getDouble(iZone);
        if (t != iZone) {
          iZone = t;
          testMax_PID.setFF(iZone, 0);
        }
      }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
    } catch (Throwable e) {
      System.out.format("Exception caught: %s\n", e.getMessage());
    }

    testMax.getEncoder().setPositionConversionFactor(1.0); // / 16.0); // Motor is on
    // 16:1 gearbox
    testMax_PID.setSmartMotionMaxAccel(15000.0, 0);
    testMax_PID.setSmartMotionMaxVelocity(2000.0, 0);
    testMax_PID.setOutputRange(-1, 1);
  }

  public void periodic() {
    // servoJoystick();
    // tunePID();
    smartMotion();
  }

  /// Connect the joystick to the motor like a servo
  private void servoJoystick() {
    DriveController controller = chooser.getSelected();
    testMax_PID.setReference(controller.getMoveRequest(), ControlType.kPosition, 0);
    System.out.format("Xbox Value: %f\n", controller.getMoveRequest());
  }

  /// Wave the motor back and forth every second for PID tuning
  private void tunePID() {
    long now = System.currentTimeMillis() / 1000;
    double posRequest = (now % 2) == 0 ? 0 : 1.0; // choose 0 or 1/4 rotation, alternating every second
    testMax_PID.setReference(posRequest, ControlType.kPosition, 0);
  }

  /// Wave the motor back and forth every second with smart motion
  private void smartMotion() {
    long now = System.currentTimeMillis() / 2000;
    double posRequest = (now % 2) == 0 ? 0 : 1.0; // choose 0 or 1 rotation, alternating every 2 seconds
    testMax_PID.setReference(posRequest, ControlType.kSmartMotion, 0);
  }
}