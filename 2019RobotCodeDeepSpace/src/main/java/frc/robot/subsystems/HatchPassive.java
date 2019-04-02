package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
// import frc.robot.commands.hatchpassive.DontKick;

public class HatchPassive extends Subsystem {
  private DoubleSolenoid hatch = RobotMap.hatchKicker;

  public HatchPassive() {
    addChild("Kicker Piston", hatch);
  }

  public void kickHatch() {
    hatch.set(DoubleSolenoid.Value.kForward);
  }

  public void dontKick() {
    hatch.set(DoubleSolenoid.Value.kReverse);
  }

  public void setKick(boolean kick) {
    hatch.set(kick ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
  }

  @Override
  protected void initDefaultCommand() {
    // I wonder if having this as a default command was causing the robot to
    // constantly call dontKick() since both KickHatch and DontKick are
    // InstantCommands so when they complete the subsystem would start running the
    // default command
    // setDefaultCommand(new DontKick());
  }
}