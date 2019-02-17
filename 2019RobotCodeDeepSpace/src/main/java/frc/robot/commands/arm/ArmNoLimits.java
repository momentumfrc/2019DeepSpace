package frc.robot.commands.arm;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;


public class ArmNoLimits extends Command {

    public ArmNoLimits() {
        requires(Robot.arm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.arm.setArmNoLimits(Robot.controlChooser.getSelected().getArmUp());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

}