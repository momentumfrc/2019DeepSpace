package frc.robot.commands.cargointake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.CargoIntake;
import frc.robot.utils.PDPWrapper;

public class GrabCargo extends Command {
    
    CargoIntake intake = Robot.cargoIntake;
	PDPWrapper currentChecker = new PDPWrapper();

	private static final double CUTOFF_CURRENT = 7.5;
	private static final int CUTOFF_TIME = 100;

    public GrabCargo() {
       requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	intake.grab();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return currentChecker.checkOvercurrent(new int[] {RobotMap.INTAKE_LOWER_PDP, RobotMap.INTAKE_UPPER_PDP}, CUTOFF_CURRENT, CUTOFF_TIME);
    }

    // Called once after isFinished returns true
    protected void end() {
        intake.hold();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        intake.stop();
    }
}
