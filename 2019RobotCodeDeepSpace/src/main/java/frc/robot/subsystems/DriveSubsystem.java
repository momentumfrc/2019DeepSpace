/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.commands.DriveNoPID;
import frc.robot.RobotMap;
import frc.robot.utils.Utils;

//import org.usfirst.frc.team4999.pid;

/**
 * An example subsystem. You can replace me with your own Subsystem.
 */
public class DriveSubsystem extends Subsystem {

  private SpeedControllerGroup leftside = new SpeedControllerGroup(RobotMap.leftFrontMotor, RobotMap.leftBackMotor);
  private SpeedControllerGroup rightside = new SpeedControllerGroup(RobotMap.rightFrontMotor, RobotMap.rightBackMotor);

  private DifferentialDrive drive = new DifferentialDrive(leftside, rightside);

  public DriveSubsystem(){
    super("Drive Subsytem");
    drive.setDeadband(0);
    addChild("Left Side", leftside);
    addChild("Right Side", rightside);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveNoPID());
  }

  public void arcadeDrive(double moveRequest, double turnRequest, double speedLimiter){
    double m_r = Utils.clip(moveRequest, -1, 1) * speedLimiter;
    double t_r = Utils.clip(turnRequest, -1, 1) * speedLimiter;
	  drive.arcadeDrive(m_r, t_r, false);
  }

  public void tankDrive(double leftSide, double rightSide, double speedLimiter) {
    double l_m = Utils.clip(leftSide * speedLimiter, -1, 1);
    double r_m = Utils.clip(rightSide * speedLimiter, -1, 1);
    drive.tankDrive(l_m, r_m);
  }

  public void stop(){
    tankDrive(0,0,0);
  }

  public double get() {
    return (leftside.get() + rightside.get())/2;
  }
}
