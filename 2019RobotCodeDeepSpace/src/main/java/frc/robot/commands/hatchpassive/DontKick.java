package frc.robot.commands.hatchpassive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.HatchPassive;

public class DontKick extends InstantCommand {
  HatchPassive hatch = Robot.hatchPassive;

  public DontKick() {
    requires(hatch);
  }

  @Override
  protected void execute() {
    hatch.dontKick();
  }

}