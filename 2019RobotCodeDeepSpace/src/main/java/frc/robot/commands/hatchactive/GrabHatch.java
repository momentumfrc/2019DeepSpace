package frc.robot.commands.hatchactive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.HatchActive;

public class GrabHatch extends Command{

    HatchActive hatch = Robot.hatchActive;

    public GrabHatch() {
        requires(hatch);
    }

    protected void initialize(){
        
    }

    protected void execute() {
        hatch.grabHatch();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}