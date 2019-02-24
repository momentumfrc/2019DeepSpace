package frc.robot.commands.wrist;

import frc.robot.RobotMap;
import frc.robot.subsystems.Wrist;
import frc.robot.Robot;
import frc.robot.utils.MoPDP;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ZeroWrist extends Command {
  private static final double CUTOFF_TIME = 1000;// Zeroing the wrist is taking too long or something is going wrong
  private static final double ZERO_SPEED = .125;
  private static final double ZERO_CUTOFF_CURRENT = 3;// Amps
  private static final int ZERO_CUTTOFF_TIME = 1000;// Milliseconds

  private Wrist wrist = Robot.wrist;
  private MoPDP.OvercurrentMonitor ocMon = RobotMap.pdp.MakeOvercurrentMonitor(RobotMap.WRIST_PDP, ZERO_CUTOFF_CURRENT,
      ZERO_CUTTOFF_TIME);
  private Timer time = new Timer();

  public ZeroWrist() {
    requires(Robot.wrist);
  }

  @Override
  protected void initialize() {
    time.start();
  }

  @Override
  protected void execute() {
    // FIXME This zeroing strategy may be flawed and needs review
    wrist.setWristNoLimits(-ZERO_SPEED);
  }

  @Override
  protected boolean isFinished() {
    return ocMon.check() || time.hasPeriodPassed(CUTOFF_TIME);
  }

  @Override
  protected void end() {
    wrist.ZeroWrist();
  }
}