package frc.robot.triggers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ShuffleboardTrigger extends Trigger {

  private final ShuffleboardTab tab;
  private final NetworkTableEntry buttonWidget;

  public ShuffleboardTrigger(ShuffleboardTab tab, String name, int columnIndex, int rowIndex) {
    this.tab = tab;
    buttonWidget = tab.add(name, false).withPosition(columnIndex, rowIndex).withWidget(BuiltInWidgets.kToggleButton)
        .getEntry();
  }

  @Override
  public boolean get() {
    return buttonWidget.getBoolean(false);
  }
}