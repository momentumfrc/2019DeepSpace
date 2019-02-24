package frc.robot.commands.hatchactive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.HatchActive;

public class GrabHatch extends InstantCommand {

  HatchActive hatch = Robot.hatchActive;

  public GrabHatch() {
    requires(hatch);
  }

  @Override
  protected void execute() {
    hatch.grabHatch();
  }

}