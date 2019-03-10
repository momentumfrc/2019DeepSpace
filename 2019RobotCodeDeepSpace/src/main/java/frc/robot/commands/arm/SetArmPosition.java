package frc.robot.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Arm;

public class SetArmPosition extends Command {
  private Arm arm = Robot.arm;

  private double position;

  public SetArmPosition(double newPos) {
    this.position = newPos;
    requires(arm);
  }

  @Override
  protected void initialize() {
    arm.pid_arm.setSetpoint(position);
    arm.pid_arm.enable();
  }

  @Override
  protected void execute() {
    System.out.format("Setpoint:%.2f Current:%.2f Output:%.2f\n", position, arm.getArmPos(), arm.pid_arm.get());
  }

  @Override
  protected boolean isFinished() {
    return arm.pid_arm.onTargetForTime();
  }

  @Override
  protected void end() {
    arm.setDefaultCommand(new MaintainArmPosition());
  }

  @Override
  protected void interrupted() {

  }
}