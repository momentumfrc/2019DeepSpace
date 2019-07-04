package frc.robot.subsystems;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANPIDController.AccelStrategy;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.MoPrefs;
//import frc.robot.utils.SparkMaxShuffleboard;
import frc.robot.utils.Utils;

/*
Positive away from limit switch
Wire to reverse limit switch if motor is not inverted.
Wire to forward limit switch if motor is inverted. <--

Invert motor in code in RobotMap.
*/

public class Arm extends Subsystem {

  private CANSparkMax m_Arm = RobotMap.armMotor;
  private CANEncoder e_arm = m_Arm.getEncoder();
  private CANPIDController p_arm = m_Arm.getPIDController();
  private CANDigitalInput limitSwitch;

  private final NetworkTableEntry zeroWidget;
  private boolean reliableZero = false; // the arm has a reliable zero setpoint
  private double armPos = 0;
  private IdleMode idleMode = IdleMode.kCoast;
  private boolean enableLimit = true;

  private static final double GEAR_RATIO = (1.0 / 100.0) * (12.0 / 60.0); // 1:100 CIM Sport into a 12:60 Gear Ratio

  private static final int smartMotionSlot = 0;

  // PID coefficients
  // These are based on the underlying PID engine and are not affected by the
  // GEAR_RATIO
  // DON'T TOUCH
  private static final double kP = 5e-5;
  private static final double kI = 1e-6;
  private static final double kD = 0;
  private static final double kIz = 0;
  private static final double kFF = 0.000156;

  // Adjust maxoutput & minoutput
  private static final double kMaxOutput = 0.6; // adjust this
  private static final double kMinOutput = -0.6; // adjust this
  private static final double allowedErr = 0;

  // public final SparkMaxShuffleboard value_display;

  // Smart Motion Coefficients
  // These are affected by the GEAR_RATIO
  private static final double minVel = 0;
  // maxvel = (output RPM) / GEAR RATIO
  private static final double maxVel = 10.0 / GEAR_RATIO; // adjust this
  private static final double maxAcc = 100.0 / GEAR_RATIO; // adjust this

  private static final double MAX_POWER_DELTA = 0.03;

  public Arm() {
    super("Arm");
    addChild(m_Arm);
    e_arm.setPositionConversionFactor(GEAR_RATIO);

    // value_display = new SparkMaxShuffleboard(RobotMap.testTab, "Arm SparkMax",
    // m_Arm, smartMotionSlot);
    p_arm.setP(kP, smartMotionSlot);
    p_arm.setI(kI, smartMotionSlot);
    p_arm.setD(kD, smartMotionSlot);
    p_arm.setIZone(kIz, smartMotionSlot);
    p_arm.setFF(kFF, smartMotionSlot);
    p_arm.setOutputRange(kMinOutput, kMaxOutput, smartMotionSlot);
    p_arm.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    p_arm.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    p_arm.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    p_arm.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
    p_arm.setSmartMotionAccelStrategy(AccelStrategy.kTrapezoidal, smartMotionSlot);

    limitSwitch = m_Arm.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
    limitSwitch.enableLimitSwitch(enableLimit);
    m_Arm.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);
    m_Arm.setInverted(RobotMap.armInverted);
    zeroWidget = RobotMap.matchTab.add("Arm Has Zero", false).withPosition(0, 0).getEntry();
    /*
     * zeroWidget.addListener(notice -> { if (!notice.value.getBoolean()) {
     * reliableZero = false; } }, EntryListenerFlags.kNew |
     * EntryListenerFlags.kUpdate);
     */

    // Coast at startup to enable hand positioning of arm
    m_Arm.setIdleMode(IdleMode.kCoast);
  }

  /// Allows the wrist to be controlled with raw input
  public void setArmNoLimits(double speed) {
    enableLimit = true;
    brake();

    // Enforce a power ramp on the arm to limit acceleration
    double curr = m_Arm.get();
    double delta = speed - curr;
    delta = Utils.clip(delta, -MAX_POWER_DELTA, MAX_POWER_DELTA);
    m_Arm.set(curr + delta);
  }

  /// Gets the current arm position
  public double getArmPos() {
    return armPos;
  }

  public boolean hasReliableZero() {
    return reliableZero;
  }

  /// Sets the encoder such that the current arm position is the specified number
  public void setArmPosition(double pos) {
    e_arm.setPosition(pos);
    reliableZero = true;
  }

  /// Defines the current position of the Arm as the offset/zero positon
  public void zeroArm() {
    setArmPosition(0);
  }

  /// Request a specific position using SmartMotion
  public void setSmartPosition(double posRequest) {
    brake();
    p_arm.setReference(posRequest, ControlType.kSmartMotion, smartMotionSlot);
    enableLimit = false; // must disable limit switches due to bad interaction with SmartMotion
  }

  public void setArmMotor(double speed) {
    brake();
    double arm_pos = getArmPos();
    if (!Double.isFinite(arm_pos)) {
      System.out.println("Invalid arm_pos");
      setArmNoLimits(speed);
      return;
    }
    if (speed > 0 && arm_pos >= MoPrefs.getMaxArmRotation()) {
      System.out.format("Arm at max rotation (%f)\n", arm_pos);
      m_Arm.set(0);
    } else if (speed < 0 && arm_pos <= MoPrefs.getMinArmRotation()) {
      System.out.format("Arm at min rotation (%f)", arm_pos);
      m_Arm.set(0);
    } else {
      double curr = m_Arm.get();
      double delta = speed - curr;
      delta = Utils.clip(delta, -MAX_POWER_DELTA, MAX_POWER_DELTA);
      // System.out.format("Arm: %.2f\n", curr + delta);
      m_Arm.set(curr + delta);
    }
  }

  public void stop() {
    m_Arm.set(0);
  }

  public void coast() {
    idleMode = IdleMode.kCoast;
  }

  public void brake() {
    idleMode = IdleMode.kBrake;
  }

  @Override
  protected void initDefaultCommand() {
  }

  @Override
  public void periodic() {
    armPos = e_arm.getPosition();
    m_Arm.setIdleMode(idleMode);
    limitSwitch.enableLimitSwitch(enableLimit);

    if (limitSwitch.get()) {
      zeroArm();
    }
    zeroWidget.setBoolean(hasReliableZero());
  }

}