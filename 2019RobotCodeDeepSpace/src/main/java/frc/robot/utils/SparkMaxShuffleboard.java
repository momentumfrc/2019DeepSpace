package frc.robot.utils;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANPIDController.AccelStrategy;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class SparkMaxShuffleboard {

  private static final int WIDTH = 1;
  private static final int HEIGHT = 13;

  private int slotid = 0;

  private NetworkTableEntry kP, kI, kD, iErrZone, kFF, kMinOutput, kMaxOutput, maxVel, minVel, maxAcc, allowedErr;

  public SparkMaxShuffleboard(ShuffleboardTab tab, String name, CANPIDController pid, int slotid) {
    this.slotid = slotid;
    pid.setSmartMotionAccelStrategy(AccelStrategy.kTrapezoidal, slotid);
    ShuffleboardLayout layout = tab.getLayout(name, BuiltInLayouts.kList).withSize(WIDTH, HEIGHT);
    kP = layout.add("kP", pid.getP(slotid)).getEntry();
    kI = layout.add("kI", pid.getI(slotid)).getEntry();
    kD = layout.add("kD", pid.getD(slotid)).getEntry();
    kFF = layout.add("kF", pid.getFF(slotid)).getEntry();
    iErrZone = layout.add("Integral Error Zone", pid.getIZone(slotid)).getEntry();
    kMinOutput = layout.add("Minimum Output", pid.getOutputMin(slotid)).getEntry();
    kMaxOutput = layout.add("Maximum Output", pid.getOutputMax(slotid)).getEntry();
    maxVel = layout.add("Maximum Velocity", pid.getSmartMotionMaxVelocity(slotid)).getEntry();
    minVel = layout.add("Minimum Velocity", pid.getSmartMotionMinOutputVelocity(slotid)).getEntry();
    maxAcc = layout.add("Maximum Acceleration", pid.getSmartMotionMaxAccel(slotid)).getEntry();
    allowedErr = layout.add("Allowed Closed Loop Error", pid.getSmartMotionAllowedClosedLoopError(slotid)).getEntry();

    kP.addListener(notice -> pid.setP(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    kI.addListener(notice -> pid.setI(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    kD.addListener(notice -> pid.setD(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    kFF.addListener(notice -> pid.setFF(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    iErrZone.addListener(notice -> pid.setIZone(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    kMinOutput.addListener(
        notice -> pid.setOutputRange(notice.value.getDouble(), kMaxOutput.getDouble(pid.getOutputMax()), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    kMaxOutput.addListener(
        notice -> pid.setOutputRange(kMinOutput.getDouble(pid.getOutputMin()), notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    maxVel.addListener(notice -> pid.setSmartMotionMaxVelocity(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    minVel.addListener(notice -> pid.setSmartMotionMinOutputVelocity(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    maxAcc.addListener(notice -> pid.setSmartMotionMaxAccel(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    allowedErr.addListener(notice -> pid.setSmartMotionAllowedClosedLoopError(notice.value.getDouble(), slotid),
        EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

  }

}