package frc.robot.commands.hatchactive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.HatchFlipper;

public class GrabHatch extends InstantCommand {

  HatchFlipper hatch = Robot.hatchActive;

  public GrabHatch() {
    requires(hatch);
  }

  @Override
  protected void execute() {
    hatch.grabHatch();
  }

}