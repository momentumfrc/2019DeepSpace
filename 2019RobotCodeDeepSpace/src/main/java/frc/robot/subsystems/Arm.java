package frc.robot.subsystems;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANPIDController.AccelStrategy;
import com.revrobotics.CANSparkMax.IdleMode;

//import org.usfirst.frc.team4999.pid.SendableCANPIDController;
//import frc.robot.utils.PIDFactory;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.MoPrefs;
import frc.robot.utils.SparkMaxShuffleboard;

public class Arm extends Subsystem {

  private CANSparkMax m_Arm = RobotMap.armMotor;
  private CANEncoder e_arm = m_Arm.getEncoder();
  private CANPIDController p_arm = m_Arm.getPIDController();
  private CANDigitalInput limitSwitch = m_Arm
      .getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
  // public final SendableCANPIDController pid_arm = PIDFactory.getArmPID();

  private final NetworkTableEntry zeroWidget;
  private boolean reliableZero = false; // the arm has a reliable zero setpoint

  private static final double GEAR_RATIO = (1.0 / 36.0) * (18.0 / 84.0); // 1:36 CIM Sport into a 18:84 Gear Ratio

  private static final int smartMotionSlot = 0;

  // PID coefficients
  // These are based on the underlying PID engine and are not affected by the
  // GEAR_RATIO
  private static final double kP = 5e-5;
  private static final double kI = 1e-6;
  private static final double kD = 0;
  private static final double kIz = 0;
  private static final double kFF = 0.000156;
  private static final double kMaxOutput = 1;
  private static final double kMinOutput = -1;
  private static final double allowedErr = 0;

  SparkMaxShuffleboard value_display;

  // Smart Motion Coefficients
  // These are affected by the GEAR_RATIO
  private static final double minVel = 0;
  private static final double maxVel = 100.0 / GEAR_RATIO;
  private static final double maxAcc = 1500.0 / GEAR_RATIO;

  public Arm() {
    super("Arm");
    addChild(m_Arm);
    // addChild(pid_arm);
    e_arm.setPositionConversionFactor(GEAR_RATIO);

    value_display = new SparkMaxShuffleboard(RobotMap.testTab, "Arm SparkMax", p_arm, smartMotionSlot);
    p_arm.setSmartMotionAccelStrategy(AccelStrategy.kTrapezoidal, smartMotionSlot);
    /*
     * p_arm.setP(kP, smartMotionSlot);
     * 
     * p_arm.setI(kI, smartMotionSlot);
     * 
     * p_arm.setD(kD, smartMotionSlot);
     * 
     * p_arm.setIZone(kIz, smartMotionSlot);
     * 
     * p_arm.setFF(kFF, smartMotionSlot);
     * 
     * p_arm.setOutputRange(kMinOutput, kMaxOutput, smartMotionSlot);
     * 
     * p_arm.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
     * 
     * p_arm.setSmartMotionMinOutputVelocity(minVel,smartMotionSlot);
     * 
     * p_arm.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
     * 
     * p_arm.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
     * 
     * p_arm.setSmartMotionAccelStrategy(AccelStrategy.kTrapezoidal,
     * smartMotionSlot);
     */

    limitSwitch.enableLimitSwitch(true);
    m_Arm.setInverted(RobotMap.armInverted);
    zeroWidget = RobotMap.matchTab.add("Arm Has Zero", false).withPosition(0, 0).getEntry();

    coast();
  }

  /// Allows the wrist to be controlled with raw input
  public void setArmNoLimits(double speed) {
    m_Arm.setIdleMode(IdleMode.kBrake);
    m_Arm.set(speed);
    limitSwitch.enableLimitSwitch(true);
  }

  /// Gets the current arm position
  public double getArmPos() {
    return e_arm.getPosition();
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
    m_Arm.setIdleMode(IdleMode.kBrake);
    p_arm.setReference(posRequest, ControlType.kSmartMotion, smartMotionSlot);
    limitSwitch.enableLimitSwitch(false);
  }

  public void setArmMotor(double speed) {
    double arm_pos = getArmPos();
    if (speed > 0 && arm_pos >= MoPrefs.getMaxArmRotation()) {
      System.out.format("Arm at max rotation (%d)", arm_pos);
      m_Arm.set(0);
    } else if (speed < 0 && arm_pos <= MoPrefs.getMinArmRotation()) {
      System.out.format("Arm at min rotation (%d)", arm_pos);
      m_Arm.set(0);
    } else {
      m_Arm.set(speed);
    }
  }

  public void stop() {
    m_Arm.set(0);
  }

  public void coast() {
    m_Arm.setIdleMode(IdleMode.kCoast);
    stop();
  }

  @Override
  protected void initDefaultCommand() {
  }

  @Override
  public void periodic() {
    if (limitSwitch.get())
      zeroArm();
    zeroWidget.setBoolean(hasReliableZero());
  }

}