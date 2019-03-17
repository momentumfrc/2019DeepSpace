package frc.robot.subsystems;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import org.usfirst.frc.team4999.pid.SendableCANPIDController;
import frc.robot.utils.PIDFactory;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.utils.MoPrefs;

public class Arm extends Subsystem {

  private CANSparkMax m_Arm = RobotMap.armMotor;
  private CANEncoder e_arm = m_Arm.getEncoder();
  private CANPIDController p_arm = m_Arm.getPIDController();
  private CANDigitalInput limitSwitch = m_Arm
      .getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
  public final SendableCANPIDController pid_arm = PIDFactory.getArmPID();
  private boolean reliableZero = false; // the arm has a reliable zero setpoint

  private static final double GEAR_RATIO = (1.0 / 36.0) * (18.0 / 84.0); // 1:36 CIM Sport into a 18:84 Gear Ratio

  public Arm() {
    super("Arm");
    addChild(m_Arm);
    addChild(pid_arm);
    e_arm.setPositionConversionFactor(GEAR_RATIO);
    limitSwitch.enableLimitSwitch(true);
    m_Arm.setInverted(RobotMap.armInverted);
  }

  /// Allows the wrist to be controlled with raw input
  public void setArmNoLimits(double speed) {
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
    p_arm.setReference(posRequest, ControlType.kSmartMotion);
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

  @Override
  protected void initDefaultCommand() {
  }

  @Override
  public void periodic() {
    if (limitSwitch.get())
      zeroArm();
    SmartDashboard.putBoolean("armHasZero", hasReliableZero());
  }

}