package frc.robot.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Arm;

public class MaintainArmPosition extends Command {
  private Arm arm = Robot.arm;

  public MaintainArmPosition() {
    requires(arm);
  }

  @Override
  protected void initialize() {
    arm.pid_arm.enable();
    arm.pid_arm.setSetpoint((0));
  }

  @Override
  protected void execute() {
    arm.drivePID();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {

  }

  @Override
  protected void interrupted() {

  }

}