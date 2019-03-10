package frc.robot.commands;

import java.util.Vector;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Wrist;
import frc.robot.utils.MoPrefs;

public class ArmPositioning extends Command {

  private Arm arm = Robot.arm;
  private Wrist wrist = Robot.wrist;
  private ControlChooser chooser = Robot.controlChooser;
  private boolean manualMode = true; // select between manual or preset mode

  private final double manualDeadzone = 0.05; // Manual inputs greater than the deadzone kill the preset mode

  private class Preset {
    private final String name;
    private double armPos;
    private double wristPos;

    Preset(String name) {
      this.name = name;
      this.armPos = MoPrefs.getDouble(name + "_arm", -1);
      this.wristPos = MoPrefs.getDouble(name + "_wrist", -1);
    }

    boolean isValid() {
      return armPos >= 0 && wristPos >= 0;
    }

    double getArmPos() {
      return armPos;
    }

    double getWristPos() {
      return wristPos;
    }

    void setPreset(double armPos, double wristPos) {
      this.armPos = armPos;
      this.wristPos = wristPos;
      MoPrefs.setDouble(name + "_arm", armPos);
      MoPrefs.setDouble(name + "_wrist", wristPos);
    }
  }

  private class Presets {
    private final String groupName;
    private int currentIx = 0;
    private Vector<Preset> presets;

    Presets(String groupName, String... presetNames) {
      this.groupName = groupName;
      presets = new Vector<Preset>();
      for (String name : presetNames) {
        presets.add(new Preset(groupName + "_" + name));
      }
    }

    String getGroupName() {
      return groupName;
    }

    boolean nextPreset() {
      if (currentIx + 1 < presets.size()) {
        ++currentIx;
        return true;
      }
      return false;
    }

    boolean prevPreset() {
      if (currentIx > 0) {
        --currentIx;
        return true;
      }
      return false;
    }

    Preset getCurrentPreset() {
      return presets.elementAt(currentIx);
    }

    void savePreset(double armPos, double wristPos) {
      presets.elementAt(currentIx).setPreset(armPos, wristPos);
    }
  }

  private class PresetGroup {
    private Presets presets[] = new Presets[2];
    private int currentIx = 0;

    PresetGroup() {
      presets[0] = new Presets("Hatch", "Ground", "1", "2");
      presets[1] = new Presets("Cargo", "Ground", "1", "2", "Bay");
    }

    void selectGroup(int ix) {
      if (ix > 0 && ix < presets.length)
        currentIx = ix;
    }

    Presets getPresets() {
      return presets[currentIx];
    }
  }

  PresetGroup presetGroup = new PresetGroup();

  public ArmPositioning() {
    requires(arm);
    requires(wrist);
  }

  @Override
  protected void initialize() {
    manualMode = true;
  }

  @Override
  protected void execute() {
    DriveController controller = chooser.getSelected();
    double manualArmSpeed = controller.getArmSpeed();
    double manualWristSpeed = controller.getWristSpeed();
    boolean presetGroup0Selected = controller.getPresetGroup0Pressed();
    boolean presetGroup1Selected = controller.getPresetGroup1Pressed();
    boolean presetIncreased = controller.getPresetIncreasedPressed();
    boolean presetDecreased = controller.getPresetDecreasedPressed();
    boolean savePreset = controller.getSavePreset();

    boolean manualOverride = Math.abs(manualArmSpeed) > manualDeadzone || Math.abs(manualWristSpeed) > manualDeadzone;
    boolean presetRequested = presetIncreased || presetDecreased;

    if (presetGroup0Selected)
      presetGroup.selectGroup(0);
    if (presetGroup1Selected)
      presetGroup.selectGroup(1);

    Presets presets = presetGroup.getPresets();

    Preset preset = presets.getCurrentPreset();
    if (presetRequested) {
      if (presetIncreased) {
        // Select higher preset
        presets.nextPreset();
      } else if (presetDecreased) {
        // Select lower preset
        presets.prevPreset();
      }
      preset = presets.getCurrentPreset();
      if (preset.isValid())
        manualMode = false;
    }

    if (manualOverride || !preset.isValid()) {
      // Switch to manual mode
      manualMode = true;
    }

    if (manualMode) {
      arm.setArmNoLimits(manualArmSpeed);
      wrist.setWristNoLimits(manualWristSpeed);

      // Can only save presets from manual mode
      if (savePreset) {
        preset.setPreset(arm.getArmPos(), wrist.getWristPos());
      }

    } else {
      // Apply preset
      arm.setSmartPosition(preset.getArmPos());
      wrist.setSmartPosition(preset.getWristPos());
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void interrupted() {

  }

  @Override
  protected void end() {

  }
}