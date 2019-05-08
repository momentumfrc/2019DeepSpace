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
import frc.robot.utils.SparkMaxShuffleboard;

public class Wrist extends Subsystem {

  private CANSparkMax m_Wrist = RobotMap.wristMotor;
  private CANEncoder e_Wrist = m_Wrist.getEncoder();
  private CANPIDController p_Wrist = m_Wrist.getPIDController();
  private CANDigitalInput limitSwitch;

  private final NetworkTableEntry zeroWidget;
  private boolean reliableZero = false;

  private static final double GEAR_RATIO = (1.0 / 16.0) * (16.0 / 32.0); // 1:16 CIM Sport into 16:32 Sprockets

  private static final int smartMotionSlot = 0;

  // PID coefficients
  // These are based on the underlying PID engine and are not affected by the
  // GEAR_RATIO
  private static final double kP = 5e-5;
  private static final double kI = 1e-6;
  private static final double kD = 0;
  private static final double kIz = 0;
  private static final double kFF = 0.000156;
  private static final double kMaxOutput = 0.3; // 1;
  private static final double kMinOutput = -0.3; // -1;
  private static final double allowedErr = 0;

  // public final SparkMaxShuffleboard value_display;

  // Smart Motion Coefficients
  // These are affected by the GEAR_RATIO
  private static final double minVel = 0;
  private static final double maxVel = 20.0 / GEAR_RATIO;
  private static final double maxAcc = 200.0 / GEAR_RATIO;

  public Wrist() {
    super("Wrist");
    addChild(m_Wrist);
    e_Wrist.setPositionConversionFactor(GEAR_RATIO);

    // value_display = new SparkMaxShuffleboard(RobotMap.testTab, "Wrist SparkMax",
    // m_Wrist, smartMotionSlot);
    p_Wrist.setSmartMotionAccelStrategy(AccelStrategy.kTrapezoidal, smartMotionSlot);

    p_Wrist.setI(kI, smartMotionSlot);
    p_Wrist.setD(kD, smartMotionSlot);
    p_Wrist.setIZone(kIz, smartMotionSlot);
    p_Wrist.setFF(kFF, smartMotionSlot);
    p_Wrist.setOutputRange(kMinOutput, kMaxOutput, smartMotionSlot);
    p_Wrist.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    p_Wrist.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    p_Wrist.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    p_Wrist.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    limitSwitch = m_Wrist.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
    m_Wrist.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);
    limitSwitch.enableLimitSwitch(true);
    m_Wrist.setInverted(RobotMap.wristInverted);
    zeroWidget = RobotMap.matchTab.add("Wrist Has Zero", false).withPosition(0, 1).getEntry();
    /*
     * zeroWidget.addListener(notice -> { if (!notice.value.getBoolean()) {
     * reliableZero = false; } }, EntryListenerFlags.kNew |
     * EntryListenerFlags.kUpdate);
     */

    coast(); // coast at startup to allow pre-match positioning by hand
  }

  /// Allows the wrist to be controlled with raw input
  public void setWristNoLimits(double speed) {
    // System.out.format("Setting wrist: %.4f\n", speed);
    limitSwitch.enableLimitSwitch(true);
    m_Wrist.setIdleMode(IdleMode.kBrake);
    m_Wrist.set(speed);
    // p_Wrist.setReference(speed, ControlType.kVelocity, smartMotionSlot);
  }

  /// Get the current position of the Wrist relative to the offset/zero position
  public double getWristPos() {
    return e_Wrist.getPosition();
  }

  public boolean hasReliableZero() {
    return reliableZero;
  }

  /// Defines the current position of the Wrist as the specified positon
  public void setWristPos(double pos) {
    e_Wrist.setPosition(pos);
    reliableZero = true;
  }

  /// Defines the current position of the Wrist as the offset/zero positon
  public void zeroWrist() {
    setWristPos(0);
  }

  /// Request a specific position using SmartMotion
  public void setSmartPosition(double posRequest) {
    m_Wrist.setIdleMode(IdleMode.kBrake);
    p_Wrist.setReference(posRequest, ControlType.kSmartMotion, smartMotionSlot);
    limitSwitch.enableLimitSwitch(false); // must disable due to bad interaction with SmartMotion
    // System.out.format("Wrist reference: %.4f\n", posRequest);
  }

  public void setWristMotor(double speed) {
    double wrist_pos = getWristPos();
    m_Wrist.setIdleMode(IdleMode.kBrake);
    if (Double.isInfinite(wrist_pos) || Double.isNaN(wrist_pos)) {
      System.out.println("Invalid wrist_pos");
      setWristNoLimits(speed);
      return;
    }
    if (speed > 0 && wrist_pos >= MoPrefs.getMaxWristRotation()) {
      System.out.format("Wrist at max rotation (%d)", wrist_pos);
      m_Wrist.set(0);
    } else if (speed < 0 && wrist_pos <= MoPrefs.getMinWristRotation()) {
      System.out.format("Wrist at min rotation (%d)", wrist_pos);
      m_Wrist.set(0);
    } else {
      m_Wrist.set(speed);
    }
  }

  public void stopWrist() {
    m_Wrist.set(0);
  }

  public void coast() {
    m_Wrist.setIdleMode(IdleMode.kCoast);
    stopWrist();
  }

  public void brake() {
    m_Wrist.setIdleMode(IdleMode.kBrake);
    stopWrist();
  }

  @Override
  protected void initDefaultCommand() {
  }

  @Override
  public void periodic() {
    if (limitSwitch.get())
      zeroWrist();
    zeroWidget.setBoolean(hasReliableZero());
  }

}