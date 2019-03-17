package frc.robot.choosers;

import frc.robot.sandstorm.SandstormMode;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SandstormChooser extends SendableChooser<SandstormMode> {
  private final String NAME = "Sandstorm";

  public SandstormChooser() {
    super();

    setDefaultOption("Teleop Control", SandstormMode.TELEOP);
    addOption("Default", SandstormMode.DEFAULT_PATH);

    SmartDashboard.putData(NAME, this);
  }

}