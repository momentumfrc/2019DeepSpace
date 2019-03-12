package frc.robot.commands.cargointake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.CargoIntake;

public class ShootCargo extends Command {
  private static final double SHOOT_TIME = 1;

  CargoIntake intake = Robot.cargoIntake;
  Timer time = new Timer();

  public ShootCargo() {
    requires(intake);
  }

  // Called just after starting command
  @Override
  protected void initialize() {
    time.start();
    time.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    intake.shoot();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return time.hasPeriodPassed(SHOOT_TIME);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    intake.setDefaultCommand(new StopIntake());
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    intake.stop();
  }
}
