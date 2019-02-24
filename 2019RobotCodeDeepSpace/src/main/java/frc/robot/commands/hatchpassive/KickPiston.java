package frc.robot.commands.hatchpassive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.HatchPassive;

public class KickPiston extends Command {
    HatchPassive hatch = Robot.hatchPassive;

    public KickPiston() {
        requires(hatch);
    }

    protected void initialize() {
    }

    protected void execute() {
        hatch.kickHatch();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}