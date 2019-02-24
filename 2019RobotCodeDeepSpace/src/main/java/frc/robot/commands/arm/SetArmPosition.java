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
    arm.pid.setSetpoint(position);
    arm.pid.enable();
  }

  @Override
  protected void execute() {
    System.out.format("Setpoint:%.2f Current:%.2f Output:%.2f\n", position, arm.getArmPos(), arm.pid.get());
    arm.drivePID();
  }

  @Override
  protected boolean isFinished() {
    return arm.pid.onTargetForTime();
  }

  @Override
  protected void end() {
    arm.setDefaultCommand(new MaintainArmPosition());
  }

  @Override
  protected void interrupted() {

  }
}