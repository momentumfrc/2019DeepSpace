/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.commands.DrivePID;
import frc.robot.RobotMap;
import frc.robot.utils.Utils;

//import org.usfirst.frc.team4999.pid;
public class DriveSubsystem extends Subsystem {

  private SpeedControllerGroup leftside = new SpeedControllerGroup(RobotMap.leftFrontMotor, RobotMap.leftBackMotor);
  private SpeedControllerGroup rightside = new SpeedControllerGroup(RobotMap.rightFrontMotor, RobotMap.rightBackMotor);

  private Encoder leftEnc = RobotMap.leftDriveEncoder;
  private Encoder rightEnc = RobotMap.rightDriveEncoder;

  private DifferentialDrive drive = new DifferentialDrive(leftside, rightside);

  public DriveSubsystem() {
    super("Drive Subsytem");
    drive.setDeadband(0);
    addChild("Left Side", leftside);
    addChild("Right Side", rightside);

    addChild("Left Encoder", leftEnc);
    addChild("Right Encoder", rightEnc);
    leftEnc.setDistancePerPulse(1.0 / 2000.0);
    rightEnc.setDistancePerPulse(1.0 / 2000.0);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DrivePID());
  }

  public void arcadeDrive(double moveRequest, double turnRequest, double speedLimiter) {
    double m_r = Utils.clip(moveRequest, -1, 1) * speedLimiter;
    double t_r = Utils.clip(turnRequest, -1, 1) * speedLimiter;
    drive.arcadeDrive(m_r, t_r, false);
  }

  public void resetEncoders() {
    leftEnc.reset();
    rightEnc.reset();
  }

  public double getMoveRate() {
    return (rightEnc.getRate() + leftEnc.getRate()) / 2.0;
  }

  public double getTurnRate() {
    return (leftEnc.getRate() - rightEnc.getRate());
  }

  public void tankDrive(double leftSide, double rightSide, double speedLimiter) {
    double l_m = Utils.clip(leftSide * speedLimiter, -1, 1);
    double r_m = Utils.clip(rightSide * speedLimiter, -1, 1);
    drive.tankDrive(l_m, r_m, false);
  }

  public void stop() {
    tankDrive(0, 0, 0);
  }

  public double get() {
    return (leftside.get() + rightside.get()) / 2;
  }
}
