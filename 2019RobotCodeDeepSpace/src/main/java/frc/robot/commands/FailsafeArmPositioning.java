/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import org.usfirst.frc.team4999.utils.Utils;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.controllers.DriveController;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Wrist;

public class FailsafeArmPositioning extends Command {

  private Arm arm = Robot.arm;
  private Wrist wrist = Robot.wrist;

  private static final double ARM_DEADZONE = 0.05;
  private static final double ARM_CURVE = 2.5;

  private static final double WRIST_DEADZONE = 0.05;
  private static final double WRIST_CURVE = 2.5;

  public FailsafeArmPositioning() {
    requires(arm);
    requires(wrist);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    DriveController controller = Robot.controlChooser.getSelected();
    double arm_req = controller.getArmSpeed();
    arm_req = Utils.clip(arm_req, -1, 1);
    arm_req = Utils.deadzone(arm_req, ARM_DEADZONE);
    arm_req = Utils.curve(arm_req, ARM_CURVE);
    double wrist_req = controller.getWristSpeed();
    wrist_req = Utils.clip(wrist_req, -1, 1);
    wrist_req = Utils.deadzone(wrist_req, WRIST_DEADZONE);
    wrist_req = Utils.curve(wrist_req, WRIST_CURVE);
    arm.setArmNoLimits(arm_req);
    wrist.setWristNoLimits(wrist_req);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    arm.coast();
    wrist.coast();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
