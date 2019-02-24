package frc.robot.commands.cargointake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.CargoIntake;

public class GrabCargo extends Command {

  CargoIntake intake = Robot.cargoIntake;

  public GrabCargo() {
    requires(intake);
  }

  // Called just after starting command
  @Override
  protected void initialize() {
    intake.grab();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // DEBUG
    System.out.format("TC: %.2f BC:%.2f TS:%.2f BS:%.2f\n", RobotMap.pdp.getCurrent(RobotMap.INTAKE_UPPER_PDP),
        RobotMap.pdp.getCurrent(RobotMap.INTAKE_LOWER_PDP), RobotMap.intakeMotorTop.get(),
        RobotMap.intakeMotorBottom.get());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return intake.checkHeld();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    intake.hold();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    intake.stop();
  }
}
