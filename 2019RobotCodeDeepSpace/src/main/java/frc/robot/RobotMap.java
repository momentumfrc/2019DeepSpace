/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.controllers.LogitechF310;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class RobotMap {
  // DRIVE//
  public static final SpeedController leftFrontMotor = new PWMVictorSPX(0);
  public static final SpeedController leftBackMotor = new PWMVictorSPX(1);
  public static final SpeedController rightFrontMotor = new PWMVictorSPX(2);
  public static final SpeedController rightBackMotor = new PWMVictorSPX(3);

  // ENCODERS//
  // TODO One of the encoders is reversed on the robot, set that encoder to
  // reversed here.
  public static final Encoder leftDriveEncoder = new Encoder(0, 1, false);
  public static final Encoder rightDriveEncoder = new Encoder(2, 3, false);

  // CONTROLLERS//
  public static final XboxController xbox = new XboxController(0);
  public static final LogitechF310 f310 = new LogitechF310(2);

  // GYROS//
  public static final AHRS navx = new AHRS(SerialPort.Port.kMXP);

  // ARM//
  public static final CANSparkMax armMotor = new CANSparkMax(12, MotorType.kBrushless);

  // WRIST//
  public static final CANSparkMax wristMotor = new CANSparkMax(13, MotorType.kBrushless);

  // CARGO INTAKE//
  public static final SpeedController intakeMotorTop = new VictorSP(4);
  public static final SpeedController intakeMotorBottom = new VictorSP(5);

  // HATCH GROUND INTAKE //
  public static final Solenoid hatchKicker = new Solenoid(0);

  // HATCH ACTIVE //
  public static final DoubleSolenoid hatchActive = new DoubleSolenoid(1, 2);

  // PDP//
  public static final PowerDistributionPanel pdp = new PowerDistributionPanel();

  public static final int LF_DRIVE_MOTOR_PDP = 0;
  public static final int LB_DRIVE_MOTOR_PDP = 1;
  public static final int RF_DRIVE_MOTOR_PDP = 15;
  public static final int RB_DRIVE_MOTOR_PDP = 14;

  public static final int ARM_PDP = 13;
  public static final int WRIST_PDP = 14;
  public static final int INTAKE_UPPER_PDP = 2;
  public static final int INTAKE_LOWER_PDP = 3;
}
