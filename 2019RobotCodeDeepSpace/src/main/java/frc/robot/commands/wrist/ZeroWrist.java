package frc.robot.commands.wrist;

import frc.robot.RobotMap;
import frc.robot.subsystems.Wrist;
import frc.robot.Robot;
import frc.robot.utils.PDPWrapper;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ZeroWrist extends Command {
    private static final double CUTOFF_TIME = 1000;//Zeroing the wrist is taking too long or something is going wrong  
    private static final double ZERO_SPEED = .125;
    private static final double ZERO_CUTOFF_CURRENT = 3;//Amps
    private static final int ZERO_CUTTOFF_TIME = 1000;//Milliseconds

    private Wrist wrist = Robot.wrist;
    private PDPWrapper pdp = new PDPWrapper();
    private Timer time = new Timer();

    public ZeroWrist() {
        requires(Robot.wrist);
    }

    protected void intitialized(){
        time.start();
    }

    protected void execute(){
        wrist.setWristNoLimits(-ZERO_SPEED);
    }
    protected boolean isFinished(){
        return pdp.checkOvercurrent(RobotMap.ARM_PDP, ZERO_CUTOFF_CURRENT, ZERO_CUTTOFF_TIME) || time.hasPeriodPassed(CUTOFF_TIME);
    }

    protected void end(){
        wrist.ZeroWrist();
    }
}