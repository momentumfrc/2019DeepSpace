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
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.controllers.LogitechF310;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class RobotMap {
	//DRIVE//
	/*
	public static VictorSP leftFrontMotor = new VictorSP(0);
	public static VictorSP leftBackMotor = new VictorSP(1);
	public static VictorSP rightFrontMotor = new VictorSP(2);
	public static VictorSP rightBackMotor = new VictorSP(3);
	*/
	public static PWMVictorSPX leftFrontMotor = new PWMVictorSPX(0);
	public static PWMVictorSPX leftBackMotor = new PWMVictorSPX(1);
	public static PWMVictorSPX rightFrontMotor = new PWMVictorSPX(2);
	public static PWMVictorSPX rightBackMotor = new PWMVictorSPX(3);


	public static Encoder leftDriveEncoder = new Encoder(0, 1);
	public static Encoder rightDriveEncoder = new Encoder(2,3);
	

	//CONTROLLERS//
	public static XboxController xbox = new XboxController(0);
	public static LogitechF310 f310 = new LogitechF310(2);

	//GYROS//
	public static AHRS navx = new AHRS(SerialPort.Port.kMXP);

	//ARM// 
	public static CANSparkMax armMotor = new CANSparkMax(1, MotorType.kBrushless);

	//WRIST//
	public static CANSparkMax wristMotor = new CANSparkMax(1, MotorType.kBrushless);

	//INTAKE//
	public static VictorSP intakeMotorTop = new VictorSP(5);
	public static VictorSP intakeMotorBottom = new VictorSP(6);

	public static DoubleSolenoid hatchKicker = new DoubleSolenoid(1, 2);

	//PDP//
	public static PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	public static final int LF_DRIVE_MOTOR_PDP = 0;
	public static final int LB_DRIVE_MOTOR_PDP = 1;
	public static final int RF_DRIVE_MOTOR_PDP = 15;
	public static final int RB_DRIVE_MOTOR_PDP = 14;

	public static final int ARM_PDP = 4;
}
