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
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import org.usfirst.frc.team4999.controllers.LogitechF310;

import frc.robot.controllers.ButtonBoard;
import frc.robot.utils.MoPDP;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class RobotMap {
  // DRIVE //
  public static final SpeedController rightBackMotor = new PWMVictorSPX(0); // leftFront
  public static final SpeedController rightFrontMotor = new PWMVictorSPX(1); // leftBack
  public static final SpeedController leftBackMotor = new PWMVictorSPX(2); // rightFront
  public static final SpeedController leftFrontMotor = new PWMVictorSPX(3); // rightBack

  // ENCODERS //
  public static final Encoder rightDriveEncoder = new Encoder(0, 1, true); // left, false
  public static final Encoder leftDriveEncoder = new Encoder(2, 3, false); // right, true

  // CONTROLLERS //
  public static final XboxController xbox = new XboxController(0);
  public static final LogitechF310 f310 = new LogitechF310(2);
  public static final ButtonBoard buttonBoard = new ButtonBoard(3);

  // ARM //
  public static final CANSparkMax armMotor = new CANSparkMax(12, MotorType.kBrushless);
  public static final boolean armInverted = true;

  // WRIST //
  public static final CANSparkMax wristMotor = new CANSparkMax(13, MotorType.kBrushless);
  public static final boolean wristInverted = false;

  // CARGO INTAKE //
  public static final SpeedController intakeMotorTop = new VictorSP(4);
  public static final SpeedController intakeMotorBottom = new VictorSP(5);

  // HATCH VELCRO KICKERS //
  public static final DoubleSolenoid hatchKicker = new DoubleSolenoid(0, 1);

  // HATCH FLOOR FLIPPER //
  public static final DoubleSolenoid hatchFlipper = new DoubleSolenoid(2, 3);

  // PDP //
  public static final MoPDP pdp = new MoPDP();

  public static final int LF_DRIVE_MOTOR_PDP = 0;
  public static final int LB_DRIVE_MOTOR_PDP = 1;
  public static final int RF_DRIVE_MOTOR_PDP = 15;
  public static final int RB_DRIVE_MOTOR_PDP = 14;

  public static final int ARM_PDP = 3;
  public static final int WRIST_PDP = 2;
  public static final int INTAKE_UPPER_PDP = 4;
  public static final int INTAKE_LOWER_PDP = 5;

  // SHUFFLEBOARD //
  public static final ShuffleboardTab matchTab = Shuffleboard.getTab("Match");
  public static final ShuffleboardTab outreachTab = Shuffleboard.getTab("Outreach");
  public static final ShuffleboardTab testTab = Shuffleboard.getTab("Test Tune");
}
