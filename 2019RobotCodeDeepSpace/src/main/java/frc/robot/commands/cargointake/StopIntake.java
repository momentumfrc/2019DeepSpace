package frc.robot.commands.cargointake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.CargoIntake;

public class StopIntake extends Command {
  CargoIntake intake = Robot.cargoIntake;

  public StopIntake() {
    requires(intake);
  }

  // Called just before this Command runs the first time
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
    intake.stop();
  }

  // Make this return true when this Command no longer needs to run execute()
  protected boolean isFinished() {
    return false; // TODO Should this quit after setting the stop once?
  }

  // Called once after isFinished returns true
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {
  }
}
