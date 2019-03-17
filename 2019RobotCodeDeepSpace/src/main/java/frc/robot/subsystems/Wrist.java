package frc.robot.subsystems;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import org.usfirst.frc.team4999.pid.SendableCANPIDController;
import frc.robot.utils.PIDFactory;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.MoPrefs;

public class Wrist extends Subsystem {

  private CANSparkMax m_Wrist = RobotMap.wristMotor;
  private CANEncoder e_Wrist = m_Wrist.getEncoder();
  private CANPIDController p_Wrist = m_Wrist.getPIDController();
  private CANDigitalInput limitSwitch = m_Wrist
      .getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
  public final SendableCANPIDController pid_wrist = PIDFactory.getWristPID();

  private NetworkTableEntry zeroWidget;
  private boolean reliableZero = false;

  private static final double GEAR_RATIO = (1.0 / 16.0) * (16.0 / 32.0); // 1:16 CIM Sport into 16:32 Sprockets

  public Wrist() {
    super("Wrist");
    addChild(m_Wrist);
    addChild(pid_wrist);
    e_Wrist.setPositionConversionFactor(GEAR_RATIO);
    limitSwitch.enableLimitSwitch(true);
    m_Wrist.setInverted(RobotMap.wristInverted);
    zeroWidget = RobotMap.matchTab.add("Wrist Has Zero", false).getEntry();
  }

  /// Allows the wrist to be controlled with raw input
  public void setWristNoLimits(double speed) {
    m_Wrist.set(speed);
    limitSwitch.enableLimitSwitch(true);
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
    p_Wrist.setReference(posRequest, ControlType.kSmartMotion);
    limitSwitch.enableLimitSwitch(false);
  }

  public void setWristMotor(double speed) {
    double wrist_pos = getWristPos();
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