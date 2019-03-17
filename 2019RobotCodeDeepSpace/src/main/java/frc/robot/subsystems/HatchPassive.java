package frc.robot.subsystems;

import java.util.Optional;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.hatchpassive.DontKick;

public class HatchPassive extends Subsystem {
  private Optional<DoubleSolenoid> hatch = RobotMap.hatchKicker;

  public HatchPassive() {
    hatch.ifPresent(hatch -> addChild("Kicker Piston", hatch));
  }

  public void kickHatch() {
    hatch.ifPresent(hatch -> hatch.set(DoubleSolenoid.Value.kForward));
  }

  public void dontKick() {
    hatch.ifPresent(hatch -> hatch.set(DoubleSolenoid.Value.kReverse));
  }

  public void setKick(boolean kick) {
    hatch.ifPresent(hatch -> hatch.set(kick ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse));
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DontKick());
  }
}