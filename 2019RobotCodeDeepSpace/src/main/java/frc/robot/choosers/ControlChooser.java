package frc.robot.choosers;

import frc.robot.RobotMap;
import frc.robot.controllers.*;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class ControlChooser extends SendableChooser<DriveController> {

  private final String NAME = "Controls";

  public ControlChooser() {
    super();
    setDefaultOption("Xbox Drive/Arm, F310 Wrist/Intake", new XboxF310Wrapper());
    addOption("Xbox Drive, F310 Arm/Wrist/Intake", new XboxF310Wrapper2());

    RobotMap.matchTab.add(NAME, this).withWidget(BuiltInWidgets.kSplitButtonChooser).withPosition(6, 0).withSize(2, 1);
  }
}