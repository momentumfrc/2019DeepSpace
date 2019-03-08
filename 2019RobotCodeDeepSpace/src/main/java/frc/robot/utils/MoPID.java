package frc.robot.utils;

import org.usfirst.frc.team4999.pid.PIDConstantUpdateListener;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class MoPID implements Sendable {
  private double kP, kI, kD, kF, iErrZone;
  private double result = 0;
  private String name, subsystem = "Ungrouped";

  private double totalErr;
  private double lastErr;
  private long lastTime;

  private PIDConstantUpdateListener updateListener = () -> {
  };

  public MoPID(String name, double kP, double kI, double kD, double kF, double iErrZone) {
    this.name = name;
    this.iErrZone = iErrZone;
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;
    lastTime = System.currentTimeMillis();
  }

  public double calculate(double target, double current) {
    // calculate time for dT
    long now = System.currentTimeMillis();
    long dTime = now - lastTime;
    lastTime = now;

    // Calculate error
    double err = target - current;

    // Calculate totalErr
    if (Math.abs(err) > iErrZone) {
      totalErr = 0;
    } else {
      totalErr += err * dTime;
    }

    // Calculate dErr
    double dErr = dTime > 0 ? (err - lastErr) / dTime : 0.0;
    lastErr = err;

    // Combine all the parts
    // kF * result is feedforward used for velocity PID
    // kF is usually 0 or 1
    result = kF * result + kP * err + kI * totalErr + kD * dErr;

    // Return the result
    return result;
  }

  public double getP() {
    return kP;
  }

  public double getI() {
    return kI;
  }

  public double getD() {
    return kD;
  }

  public double getF() {
    return kF;
  }

  public double getErrZone() {
    return iErrZone;
  }

  public void setP(double p) {
    kP = p;
    MoPrefs.setDouble(name + "_p", kP);
    updateListener.update();
  }

  public void setI(double i) {
    kI = i;
    MoPrefs.setDouble(name + "_i", kI);
    updateListener.update();
  }

  public void setD(double d) {
    kD = d;
    MoPrefs.setDouble(name + "_d", kD);
    updateListener.update();
  }

  public void setF(double f) {
    kF = f;
    MoPrefs.setDouble(name + "_f", kF);
    updateListener.update();
  }

  public void setErrZone(double zone) {
    iErrZone = zone;
    MoPrefs.setDouble(name + "_iErrZone", iErrZone);
    updateListener.update();
  }

  public void setListener(PIDConstantUpdateListener listener) {
    updateListener = listener;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getSubsystem() {
    return subsystem;
  }

  @Override
  public void setSubsystem(String subsystem) {
    this.subsystem = subsystem;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("MomentumPIDController");
    builder.addDoubleProperty("p", this::getP, this::setP);
    builder.addDoubleProperty("i", this::getI, this::setI);
    builder.addDoubleProperty("d", this::getD, this::setD);
    builder.addDoubleProperty("f", this::getF, this::setF);
    builder.addDoubleProperty("errZone", this::getErrZone, this::setErrZone);
  }

  public static MoPID makePIDFromPrefs(String name) {
    double p = MoPrefs.getDouble(name + "_p", 0);
    double i = MoPrefs.getDouble(name + "_i", 0);
    double d = MoPrefs.getDouble(name + "_d", 0);
    double f = MoPrefs.getDouble(name + "_f", 0);
    double iErrZone = MoPrefs.getDouble(name + "_iErrZone", 0);
    return new MoPID(name, p, i, d, f, iErrZone);
  }

  public void savePrefs() {
    MoPrefs.setDouble(name + "_p", kP);
    MoPrefs.setDouble(name + "_i", kI);
    MoPrefs.setDouble(name + "_d", kD);
    MoPrefs.setDouble(name + "_f", kF);
    MoPrefs.setDouble(name + "_iErrZone", iErrZone);
  }
}
