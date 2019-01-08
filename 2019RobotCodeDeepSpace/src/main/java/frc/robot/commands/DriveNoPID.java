package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.*;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;

public class DriveNoPID extends Command {

    private DriveSubsystem drive = Robot.driveSystem;
    private ControlChooser chooser = Robot.controlChooser;

    private boolean reversed;

    public DriveNoPID(){
        requires(drive);
    }

    protected void initialize() {
    }

    protected void execute() {
     	DriveController controller = chooser.getSelected();
    	if(controller.getReversedDirection())
    		reversed = !reversed;
        drive.arcadeDrive((reversed)?-controller.getMoveRequest()
                                    : controller.getMoveRequest(), controller.getTurnRequest(), controller.getSpeedLimiter());
        
        /*if(controller.getReversedDirection())
    		reversed = !reversed;
        drive.driveCartesian((reversed) ? -controller.getMoveRequest()
                                    :controller.getMoveRequest(), controller.getStrafeRequest(), controller.getTurnRequest(), controller.getSpeedLimiter());
        
        TODO: Uncomment this if we decide on Mecanum Drive and delete the other block*/
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    public void end() {
        drive.stop();
    } 

    public void interrupted(){
        end();
    }
    
}