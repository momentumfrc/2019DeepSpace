package frc.robot.commands.arm;

import frc.robot.RobotMap;
import frc.robot.subsystems.Arm;
import frc.robot.Robot;
import frc.robot.utils.MoPDP;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ZeroArm extends Command {
  private static final double CUTOFF_TIME = 5;// cut off if taking too long aka something is going on

  private static final double ZERO_SPEED = .125;
  private static final double ZERO_CUTOFF_CURRENT = 3;// Amps
  private static final int ZERO_CUTTOFF_TIME = 1000;// Milliseconds

  private Arm arm = Robot.arm;
  private MoPDP.OvercurrentMonitor ocMon = RobotMap.pdp.MakeOvercurrentMonitor(RobotMap.ARM_PDP, ZERO_CUTOFF_CURRENT,
      ZERO_CUTTOFF_TIME);
  private Timer time = new Timer();

  public ZeroArm() {
    requires(arm);
  }

  @Override
  protected void initialize() {
    time.start();
  }

  @Override
  protected void execute() {
    // FIXME This zeroing strategy is flawed. Needs a lot of rework.
    arm.setArmNoLimits(-ZERO_SPEED);
  }

  @Override
  protected boolean isFinished() {
    return ocMon.check() || time.hasPeriodPassed(CUTOFF_TIME);
  }

  @Override
  protected void end() {
    arm.zeroArm();
  }
}