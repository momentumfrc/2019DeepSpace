package frc.robot.commands.arm;

import frc.robot.RobotMap;
import frc.robot.subsystems.Arm;
import frc.robot.Robot;
import frc.robot.utils.PDPWrapper;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ZeroArm extends Command {
    private static final double CUTOFF_TIME = 5;//cut off if taking too long aka something is going on 

    private static final double ZERO_SPEED = .125;
    private static final double ZERO_CUTOFF_CURRENT = 3;//Amps
    private static final int ZERO_CUTTOFF_TIME = 1000;//Milliseconds

    private Arm arm = Robot.arm;
    private PDPWrapper pdp = new PDPWrapper();
    private Timer time = new Timer();

    public ZeroArm() {
        requires(Robot.arm);
    }

    protected void intitialized(){
        time.start();
    }

    protected void execute(){
        arm.ArmNoLimits(-ZERO_SPEED);
    }

    protected void isFinished(){
        return pdp.checkOvercurrent(RobotMap.ARM_PDP, ZERO_CUTOFF_CURRENT, ZERO_CUTTOFF_TIME) || time.hasPeriodPassed(CUTOFF_TIME);
    }

    protected void end(){
        arm.ArmPosZero();
    }
}