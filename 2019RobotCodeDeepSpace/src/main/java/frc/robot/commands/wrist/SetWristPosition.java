package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Wrist;

public class SetWristPosition extends Command {
  private Wrist wrist = Robot.wrist;

  private double position;

  public SetWristPosition(double newPos) {
    this.position = newPos;
    requires(wrist);
  }

  @Override
  protected void initialize() {
    wrist.pid_wrist.setSetpoint(position);
    wrist.pid_wrist.enable();

  }

  @Override
  protected void execute() {
    System.out.format("Setpoint:%.2f Current:%.2f Output:%.2f\n", position, wrist.getWristPos(), wrist.pid_wrist.get());
    wrist.drivePID();
  }

  @Override
  protected boolean isFinished() {
    return wrist.pid_wrist.onTargetForTime();
  }

  @Override
  protected void end() {
    wrist.setDefaultCommand(new MaintainWristPosition());
  }

  @Override
  protected void interrupted() {

  }
}