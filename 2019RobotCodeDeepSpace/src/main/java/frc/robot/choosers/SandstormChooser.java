package frc.robot.choosers;

import frc.robot.RobotMap;
import frc.robot.sandstorm.SandstormMode;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class SandstormChooser extends SendableChooser<SandstormMode> {
  private final String NAME = "Sandstorm";

  public SandstormChooser() {
    super();

    setDefaultOption("Teleop Control", SandstormMode.TELEOP);
    addOption("Vision Drive", SandstormMode.VISION_DRIVE);
    addOption("Default", SandstormMode.DEFAULT_PATH);

    RobotMap.matchTab.add(NAME, this).withWidget(BuiltInWidgets.kSplitButtonChooser).withPosition(0, 3).withSize(2, 1);
  }
}