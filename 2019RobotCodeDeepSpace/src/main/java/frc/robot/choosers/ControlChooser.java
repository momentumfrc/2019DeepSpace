package frc.robot.choosers;

import frc.robot.controllers.*;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ControlChooser extends SendableChooser<DriveController> {

  private final String NAME = "Controls";

  public ControlChooser() {
    super();
    setDefaultOption("Xbox Drive/Arm, F310 Wrist/Intake", new XboxF310Wrapper());
    addOption("Xbox Drive, F310 Arm/Wrist/Intake", new XboxF310Wrapper2());

    SmartDashboard.putData(NAME, this);
  }

}