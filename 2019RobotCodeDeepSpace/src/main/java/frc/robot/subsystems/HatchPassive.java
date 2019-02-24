package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.hatchpassive.DontKick;

public class HatchPassive extends Subsystem {
  private DoubleSolenoid hatch = RobotMap.hatchKicker;

  public void kickHatch() {
    hatch.set(DoubleSolenoid.Value.kForward);
  }

  public void dontKick() {
    hatch.set(DoubleSolenoid.Value.kReverse);
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DontKick());
  }
}