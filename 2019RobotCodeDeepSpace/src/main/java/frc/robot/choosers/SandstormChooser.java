package frc.robot.choosers;

import frc.robot.sandstorm.*;
import frc.robot.sandstorm.SandstormMode;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SandstormChooser extends SendableChooser<SandstormMode> {
  private final String NAME = "SANDSTORM CHOOSER";

  public SandstormChooser() {
    super();
    setDefaultOption("Default", SandstormMode.DEFAULT_PATH);

    SmartDashboard.putData(NAME, this);
  }

}