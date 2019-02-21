package frc.robot.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Arm;

public class MaintainArmPosition extends Command {
    private Arm arm = Robot.arm;
    
    public MaintainArmPosition(){
        requires(arm);
    }

    protected void initialize() {
        arm.pid.enable();
        arm.pid.setSetpoint(arm.getArmPos());
    }

    protected void execute(){
        arm.drivePID();
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