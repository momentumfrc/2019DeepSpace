package frc.robot.subsystems;

import java.util.Optional;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.hatchactive.ReleaseHatch;

public class HatchFlipper extends Subsystem {
  private Optional<DoubleSolenoid> hatch = RobotMap.hatchFlipper;

  public void grabHatch() {
    hatch.ifPresent(hatch -> hatch.set(DoubleSolenoid.Value.kForward));
  }

  public void releaseHatch() {
    hatch.ifPresent(hatch -> hatch.set(DoubleSolenoid.Value.kReverse));
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ReleaseHatch());
  }

}