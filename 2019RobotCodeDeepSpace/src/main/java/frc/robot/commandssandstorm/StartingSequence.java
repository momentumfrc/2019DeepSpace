package frc.robot.commandssandstorm;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Arm;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;

/*
*This is to release the prop at the start of the match so we can zero the elbow 
*/
public class StartingSequence extends Command{
    
    private Arm arm = Robot.arm;
    private DriveSubsystem drive = Robot.driveSystem;
    private static final double CUTOFF_TIME = .5;

    Timer time = new Timer();
    

    public StartingSequence(){
        requires(arm);
    }

    @Override
    protected void initialize(){
        time.reset();
        time.start();
    }

    @Override
    protected void execute(){
        arm.setArmMotor(-.1);
    }

    @Override
    protected boolean isFinished(){
        return time.hasPeriodPassed(CUTOFF_TIME);
    }

    @Override
    protected void end(){
        arm.stop();
    }

    @Override
    protected void interrupted(){
        end();
    }
}