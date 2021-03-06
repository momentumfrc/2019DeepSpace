package frc.robot.utils;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * MoPID is a PIDF implementation that automatically saves its parameters and
 * allows them to be tuned via a Shuffleboard tab.
 */
public class MoPID {
  private static final int WIDTH = 3;
  private static final int HEIGHT = 2;

  private NetworkTableEntry wkP, wkI, wkD, wiErrZone, wkFF;

  private double kP, kI, kD, kF, iErrZone;
  private String name;

  private double totalErr;
  private double lastErr;
  private double lastTime;

  /**
   * MoPID is a PIDF implementation that automatically saves its parameters and
   * allows them to be tuned via a Shuffleboard tab.
   * 
   * @param tab      Reference to the Shuffleboard tab to hold the tuning box
   * @param name     Name of this PIDF. Used to save preferences and label the
   *                 tuning box.
   * @param col      Which column of Shuffleboard to position the tuning box
   * @param row      Which row of Shuffleboard to position the tuning box
   * @param kP       Initial P coefficient (may be edited by tuner)
   * @param kI       Initial I coefficient (may be edited by tuner)
   * @param kD       Initial D coefficient (may be edited by tuner)
   * @param kF       Initial F coefficient (may be edited by tuner)
   * @param iErrZone Initial I error zone (may be edited by tuner)
   */
  public MoPID(ShuffleboardTab tab, String name, int col, int row, double kP, double kI, double kD, double kF,
      double iErrZone) {
    System.out.format("Constructing MoPID name=%s kP=%f kI=%f kD=%f kF=%f iErrZone=%f\n", name, kP, kI, kD, kF,
        iErrZone);
    this.name = name;
    this.iErrZone = iErrZone;
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;

    ShuffleboardLayout layout = tab.getLayout(name, BuiltInLayouts.kGrid).withPosition(col, row).withSize(WIDTH,
        HEIGHT);
    wkP = layout.add("kP", kP).withPosition(0, 0).getEntry();
    wkI = layout.add("kI", kI).withPosition(0, 1).getEntry();
    wkD = layout.add("kD", kD).withPosition(0, 2).getEntry();
    wkFF = layout.add("kF", kF).withPosition(1, 0).getEntry();
    wiErrZone = layout.add("Integral Error Zone", iErrZone).withPosition(1, 1).getEntry();

    wkP.addListener(notice -> setP(notice.value.getDouble()), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    wkI.addListener(notice -> setI(notice.value.getDouble()), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    wkD.addListener(notice -> setD(notice.value.getDouble()), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    wkFF.addListener(notice -> setF(notice.value.getDouble()), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    wiErrZone.addListener(notice -> setErrZone(notice.value.getDouble()),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    lastTime = Timer.getFPGATimestamp() * 1000.0;
  }

  /**
   * Calculate an output value based on a specific target, the current
   * position/velocity, and the prior state of these.
   * 
   * @param target  The desired state, e.g. position or velocity. Must be in the
   *                same units and space as current.
   * @param current The current state, e.g. position or velocity. Must be in the
   *                same units and space as target.
   * @return The output of the calculation, typically a motor power.
   */
  public double calculate(double target, double current) {
    // calculate time for dT
    double now = Timer.getFPGATimestamp() * 1000.0; // FPGA time is in seconds with microsecond resolution
    double dTime = now - lastTime;
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
    return kF * target + kP * err + kI * totalErr + kD * dErr;
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
  }

  public void setI(double i) {
    kI = i;
    MoPrefs.setDouble(name + "_i", kI);
  }

  public void setD(double d) {
    kD = d;
    MoPrefs.setDouble(name + "_d", kD);
  }

  public void setF(double f) {
    kF = f;
    MoPrefs.setDouble(name + "_f", kF);
  }

  public void setErrZone(double zone) {
    iErrZone = zone;
    MoPrefs.setDouble(name + "_iErrZone", iErrZone);
  }

  public String getName() {
    return name;
  }

  public static MoPID makePIDFromPrefs(ShuffleboardTab tab, String name, int col, int row) {
    double p = MoPrefs.getDouble(name + "_p", 0);
    double i = MoPrefs.getDouble(name + "_i", 0);
    double d = MoPrefs.getDouble(name + "_d", 0);
    double f = MoPrefs.getDouble(name + "_f", 0);
    double iErrZone = MoPrefs.getDouble(name + "_iErrZone", 0);
    return new MoPID(tab, name, col, row, p, i, d, f, iErrZone);
  }

  public void savePrefs() {
    MoPrefs.setDouble(name + "_p", kP);
    MoPrefs.setDouble(name + "_i", kI);
    MoPrefs.setDouble(name + "_d", kD);
    MoPrefs.setDouble(name + "_f", kF);
    MoPrefs.setDouble(name + "_iErrZone", iErrZone);
  }
}
