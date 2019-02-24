package frc.robot.commands.hatchpassive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.HatchPassive;

public class DontKick extends Command {
    HatchPassive hatch = Robot.hatchPassive;

    public DontKick() {
        requires(hatch);
    }

    protected void initialize() {

    }

    protected void execute() {
        hatch.dontKick();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}