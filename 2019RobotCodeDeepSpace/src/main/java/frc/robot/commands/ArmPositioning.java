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

/**
 * The ArmPositioning command is the main workhorse for the arm. It controls
 * both the shoulder and wrist joints.
 * 
 * There are two modes of operation: manual and preset
 * 
 * In manual mode, the driver must use analog inputs on the gamepad to
 * independently control the shoulder and wrist.
 * 
 * In preset mode, a saved location for a particular gamepiece type and target
 * will be set into the shoulder and wrist via SmartMotion.
 * 
 * Manual mode is active at startup and if an invalid preset is selected. The
 * only way to get into preset mode is to select a valid preset. It is always
 * possible to get immediately back to manual mode by moving one of the manual
 * controls.
 * 
 * Preset mode also relies on both the arm and wrist having reliable zero points
 * set. This must be accomplished either by running the arm and wrist against
 * their limit switches, or by executing a zeroing procedure, or by using the
 * known good starting position of the prop.
 * 
 * To specify the list of presets, edit the new PresetGroup lines below.
 */
public class ArmPositioning extends Command {

  private final PresetGroup hatchPresetGroup = new PresetGroup("Hatch", "Ground", "1", "2");
  private final PresetGroup cargoPresetGroup = new PresetGroup("Cargo", "Ground", "1", "2", "Bay");

  private Arm arm = Robot.arm;
  private Wrist wrist = Robot.wrist;
  private ControlChooser chooser = Robot.controlChooser;
  private boolean manualMode = true; // select between manual or preset mode
  private PresetGroup currentPresetGroup = hatchPresetGroup;

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

  public ArmPositioning() {
    requires(arm);
    requires(wrist);
  }

  @Override
  protected void initialize() {
    manualMode = true;
    currentPresetGroup = hatchPresetGroup;
  }

  @Override
  protected void execute() {
    // Get all the controller inputs
    DriveController controller = chooser.getSelected();
    double manualArmSpeed = controller.getArmSpeed();
    double manualWristSpeed = controller.getWristSpeed();
    boolean hatchGamepieceSelected = controller.getHatchGamepiecePressed();
    boolean cargoGamepieceSelected = controller.getCargoGamepiecePressed();
    boolean presetIncreased = controller.getPresetIncreasedPressed();
    boolean presetDecreased = controller.getPresetDecreasedPressed();
    boolean savePreset = controller.getSavePreset();

    // Interpret driver initiated changes
    boolean manualOverride = Math.abs(manualArmSpeed) > manualDeadzone || Math.abs(manualWristSpeed) > manualDeadzone;
    boolean presetRequested = presetIncreased || presetDecreased;

    if (hatchGamepieceSelected)
      currentPresetGroup = hatchPresetGroup;
    if (cargoGamepieceSelected)
      currentPresetGroup = cargoPresetGroup;

    // Dig into the current preset settings
    Preset preset = currentPresetGroup.getCurrentPreset();
    if (presetRequested) {
      if (presetIncreased) {
        // Select higher preset
        currentPresetGroup.nextPreset();
      } else if (presetDecreased) {
        // Select lower preset
        currentPresetGroup.prevPreset();
      }

      // The preset changed, update if necessary. (Note: if the index was already at
      // the end of the list, then the same preset will be selected again, but since
      // it's a new request, recheck and potentially re-enable preset mode)
      preset = currentPresetGroup.getCurrentPreset();
      if (preset.isValid())
        manualMode = false;
    }

    // Always check for manual override or invalid automation last
    if (manualOverride || !(preset.isValid() && arm.hasReliableZero() && wrist.hasReliableZero())) {
      // Switch to manual mode if selected by the driver or if there is any
      // problem with the automation.
      manualMode = true;
    }

    // Finally, apply the actions selected by everything above.
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

    // Keep the driver informed about what's going on inside the robot's brain.
    SmartDashboard.putBoolean("manualMode", manualMode);
    SmartDashboard.putString("presetName", currentPresetGroup.getGroupName() + "_" + preset.getName());
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