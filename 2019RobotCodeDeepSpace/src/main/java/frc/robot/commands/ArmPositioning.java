package frc.robot.commands;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    String getName() {
      return name;
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

  private class PresetGroup {
    private final String groupName;
    private int currentIx = 0;
    private ArrayList<Preset> presets;

    PresetGroup(String groupName, String... presetNames) {
      this.groupName = groupName;
      presets = new ArrayList<Preset>();
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
      return presets.get(currentIx);
    }
  }

  private class GamepieceType {
    private PresetGroup presets[] = new PresetGroup[2];
    private int currentIx = 0;

    GamepieceType() {
      presets[0] = new PresetGroup("Hatch", "Ground", "1", "2");
      presets[1] = new PresetGroup("Cargo", "Ground", "1", "2", "Bay");
    }

    void selectType(int ix) {
      if (ix > 0 && ix < presets.length)
        currentIx = ix;
    }

    PresetGroup getPresetGroup() {
      return presets[currentIx];
    }
  }

  GamepieceType gamepieceType = new GamepieceType();

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
    boolean gamepieceType0Selected = controller.getGamepieceType0Pressed();
    boolean gamepieceType1Selected = controller.getGamepieceType1Pressed();
    boolean presetIncreased = controller.getPresetIncreasedPressed();
    boolean presetDecreased = controller.getPresetDecreasedPressed();
    boolean savePreset = controller.getSavePreset();

    boolean manualOverride = Math.abs(manualArmSpeed) > manualDeadzone || Math.abs(manualWristSpeed) > manualDeadzone;
    boolean presetRequested = presetIncreased || presetDecreased;

    if (gamepieceType0Selected)
      gamepieceType.selectType(0);
    if (gamepieceType1Selected)
      gamepieceType.selectType(1);

    PresetGroup presetGroup = gamepieceType.getPresetGroup();

    Preset preset = presetGroup.getCurrentPreset();
    if (presetRequested) {
      if (presetIncreased) {
        // Select higher preset
        presetGroup.nextPreset();
      } else if (presetDecreased) {
        // Select lower preset
        presetGroup.prevPreset();
      }
      preset = presetGroup.getCurrentPreset();
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

      // Can only save presets from manual mode while not moving anything
      if (savePreset && !manualOverride) {
        preset.setPreset(arm.getArmPos(), wrist.getWristPos());
      }

    } else {
      // Apply preset
      arm.setSmartPosition(preset.getArmPos());
      wrist.setSmartPosition(preset.getWristPos());
    }

    SmartDashboard.putBoolean("manualMode", manualMode);
    SmartDashboard.putString("presetName", presetGroup.getGroupName() + "_" + preset.getName());
    SmartDashboard.putBoolean("presetValid", preset.isValid());
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void interrupted() {
    arm.stop();
    wrist.stopWrist();
  }

  @Override
  protected void end() {
  }
}