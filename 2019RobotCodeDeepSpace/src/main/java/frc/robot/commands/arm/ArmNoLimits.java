package frc.robot.commands.arm;

import frc.robot.Robot;
import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj.command.Command;

public class ArmNoLimits extends Command {
  Arm arm = Robot.arm;

  public ArmNoLimits() {
    requires(arm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.arm.setArmNoLimits(Robot.controlChooser.getSelected().getArmSpeed());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

}