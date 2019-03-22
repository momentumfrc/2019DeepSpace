package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.hatchactive.ReleaseHatch;

public class HatchFlipper extends Subsystem {
  private DoubleSolenoid hatch = RobotMap.hatchFlipper;

  public void grabHatch() {
    hatch.set(DoubleSolenoid.Value.kForward);
  }

  public void releaseHatch() {
    hatch.set(DoubleSolenoid.Value.kReverse);
  }

  @Override
  protected void initDefaultCommand() {
    // I wonder if having this as a default command was causing the robot to
    // constantly call releaseHatch() since both GrabHatch and ReleaseHatch are
    // InstantCommands so when they complete the subsystem would start running the
    // default command
    // setDefaultCommand(new ReleaseHatch());
  }

}