package frc.robot.commands.hatchactive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.HatchActive;

public class ReleaseHatch extends Command{

    HatchActive hatch = Robot.hatchActive;

    public ReleaseHatch() {
        requires(hatch);
    }

    protected void initialize(){
        
    }

    protected void execute() {
        hatch.releaseHatch();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}