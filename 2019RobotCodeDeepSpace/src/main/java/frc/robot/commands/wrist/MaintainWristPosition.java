package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Wrist;

public class MaintainWristPosition extends Command {
    private Wrist wrist = Robot.wrist;
    
    public MaintainWristPosition(){
        requires(wrist);
    }

    protected void initialize() {
        wrist.pid.enable();
        wrist.pid.setSetpoint(wrist.getWristPos());
    }

    protected void execute(){
        wrist.drivePID();
    }
    @Override
    protected boolean isFinished() {
        return false;
    }

    protected void end(){

    }

    protected void interrupted(){

    }


}