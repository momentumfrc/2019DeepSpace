package frc.robot.choosers;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveChooser extends SendableChooser<DriveChooser.Choice> {

  private final String NAME = "Drive";

  public enum Choice {
    with_pid, no_pid
  };

  public DriveChooser() {
    super();
    setDefaultOption("With PID", Choice.with_pid);
    addOption("No PID", Choice.no_pid);

    SmartDashboard.putData(NAME, this);
  }

}