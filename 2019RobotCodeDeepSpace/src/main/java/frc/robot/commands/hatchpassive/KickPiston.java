package frc.robot.commands.hatchpassive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.HatchPassive;

public class KickPiston extends InstantCommand {
  HatchPassive hatch = Robot.hatchPassive;

  public KickPiston() {
    requires(hatch);
  }

  @Override
  protected void execute() {
    hatch.kickHatch();
  }

}