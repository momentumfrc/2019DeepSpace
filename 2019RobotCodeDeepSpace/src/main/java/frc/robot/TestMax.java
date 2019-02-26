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

    sendablepid = new SendableCANPIDController("TestMax", testMax, 0, 0);

    LiveWindow.add(sendablepid);

    p = testMax_PID.getP();
    i = testMax_PID.getI();
    d = testMax_PID.getD();
    f = testMax_PID.getFF();
    iZone = testMax_PID.getIZone();

    SmartDashboard.putNumber("Max P", p);
    SmartDashboard.putNumber("Max I", i);
    SmartDashboard.putNumber("Max D", d);
    SmartDashboard.putNumber("Max F", f);
    SmartDashboard.putNumber("Max I Zone", iZone);

    SmartDashboard.getEntry("Max P").addListener(notification -> {
      double t = notification.getEntry().getDouble(p);
      if (t != p) {
        p = t;
        testMax_PID.setP(p);
      }
    }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
    SmartDashboard.getEntry("Max i").addListener(notification -> {
      double t = notification.getEntry().getDouble(i);
      if (t != i) {
        i = t;
        testMax_PID.setI(i);
      }
    }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
    SmartDashboard.getEntry("Max D").addListener(notification -> {
      double t = notification.getEntry().getDouble(d);
      if (t != d) {
        d = t;
        testMax_PID.setD(d);
      }
    }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
    SmartDashboard.getEntry("Max F").addListener(notification -> {
      double t = notification.getEntry().getDouble(f);
      if (t != f) {
        f = t;
        testMax_PID.setFF(f);
      }
    }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);
    SmartDashboard.getEntry("Max F").addListener(notification -> {
      double t = notification.getEntry().getDouble(iZone);
      if (t != iZone) {
        iZone = t;
        testMax_PID.setFF(iZone);
      }
    }, TableEntryListener.kUpdate | TableEntryListener.kImmediate);

    testMax.getEncoder().setPositionConversionFactor(1.0 / 16.0); // Motor is on 16:1 gearbox
    testMax_PID.setSmartMotionMaxAccel(60.0, 0);
    testMax_PID.setSmartMotionMaxVelocity(60.0, 0);
  }

  public void periodic() {
    servoJoystick();
    // tunePID();
    // smartMotion();
  }

  /// Connect the joystick to the motor like a servo
  private void servoJoystick() {
    DriveController controller = chooser.getSelected();
    testMax_PID.setReference(controller.getMoveRequest(), ControlType.kPosition);
  }

  /// Wave the motor back and forth every second for PID tuning
  private void tunePID() {
    long now = System.currentTimeMillis() / 1000;
    double posRequest = (now % 2) == 0 ? 0 : 0.25; // choose 0 or 1/4 rotation, alternating every second
    testMax_PID.setReference(posRequest, ControlType.kPosition);
  }

  /// Wave the motor back and forth every second with smart motion
  private void smartMotion() {
    long now = System.currentTimeMillis() / 1000;
    double posRequest = (now % 2) == 0 ? 0 : 0.25; // choose 0 or 1/4 rotation, alternating every second
    testMax_PID.setReference(posRequest, ControlType.kSmartMotion);
  }
}