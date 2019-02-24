package frc.robot;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;

public class TestMax {
  private static CANSparkMax testMax = new CANSparkMax(14, MotorType.kBrushless);
  private static CANPIDController testMax_PID = testMax.getPIDController();
  private ControlChooser chooser = Robot.controlChooser;

  private double p;
  private double i;
  private double d;
  private double f;
  private double iZone;

  public void init() {
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

    testMax.getEncoder().setPositionConversionFactor(16); // Motor is on 16:1 gearbox
  }

  public void periodic() {
    updateFromDashboard();

    servoJoystick();
    // tunePID();
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

  private void updateFromDashboard() {
    double t;

    t = SmartDashboard.getNumber("Max P", p);
    if (t != p) {
      p = t;
      testMax_PID.setP(p);
    }

    t = SmartDashboard.getNumber("Max I", i);
    if (t != i) {
      i = t;
      testMax_PID.setI(i);
    }

    t = SmartDashboard.getNumber("Max D", d);
    if (t != d) {
      d = t;
      testMax_PID.setD(d);
    }

    t = SmartDashboard.getNumber("Max P", f);
    if (t != f) {
      f = t;
      testMax_PID.setFF(f);
    }

    t = SmartDashboard.getNumber("Max I Zone", iZone);
    if (t != iZone) {
      iZone = t;
      testMax_PID.setIZone(iZone);
    }
  }
}