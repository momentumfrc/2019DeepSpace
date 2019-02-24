package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Wrist;

public class MaintainWristPosition extends Command {
  private Wrist wrist = Robot.wrist;

  public MaintainWristPosition() {
    requires(wrist);
  }

  @Override
  protected void initialize() {
    wrist.pid.setSetpoint(wrist.getWristPos());
    wrist.pid.enable();
  }

  @Override
  protected void execute() {
    wrist.drivePID();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    wrist.stopWrist();
  }

  @Override
  protected void interrupted() {
    wrist.stopWrist();
  }

}