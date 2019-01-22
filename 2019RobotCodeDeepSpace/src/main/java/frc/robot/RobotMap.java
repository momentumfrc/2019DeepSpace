/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
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
	public static VictorSP leftFrontMotor = new VictorSP(1);
	public static VictorSP rightFrontMotor = new VictorSP(2);
	public static VictorSP leftBackMotor = new VictorSP(3);
	public static VictorSP rightBackMotor = new VictorSP(4);
		
	//CONTROLLERS//
	public static XboxController xbox = new XboxController(0);
	public static LogitechF310 f310 = new LogitechF310(1);

	//GYROS//
	public static AHRS navx = new AHRS(SerialPort.Port.kMXP);
	public static Encoder leftDriveEncoder = new Encoder(0, 1);
	public static Encoder rightDriveEncoder = new Encoder(2,3);

	//EXAMPLE ARM MOTOR//
	public static CANSparkMax armMotor = new CANSparkMax(0, MotorType.kBrushless);

	//MISC//
	public static PowerDistributionPanel pdp = new PowerDistributionPanel(1);
}
