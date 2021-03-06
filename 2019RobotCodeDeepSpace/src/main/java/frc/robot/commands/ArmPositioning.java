package frc.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team4999.utils.Utils;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;
import frc.robot.controllers.DriveController.PresetOption;
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

  private final NetworkTableEntry presetModeWidget, presetNameWidget, presetValidWidget;

  /**
   * A Preset is a saved position of both the arm and the wrist.
   * 
   * Every Preset has a name so it can be easily identified on the Dashboard.
   * 
   * The units of armPos and wristPos are relative to the scaling setting on the
   * SparkMAX. They do not necessarily relate to real world scales (e.g. degrees)
   * 
   * The only restriction on the positions is that negative values indicate
   * invalid settings. Non-negative values are valid.
   * 
   * Position values are automatically saved and retrieved from Prefs.
   */
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

    void invalidate() {
      setPreset(-1, -1);
    }
  }

  /**
   * A PresetGroup is a named collection of Presets
   */
  private class PresetGroup {
    private int currentIx = 0;
    private ArrayList<Preset> presets;
    private String groupName;

    PresetGroup(String groupName, String... presetNames) {
      this.groupName = groupName;
      presets = new ArrayList<Preset>();
      for (String name : presetNames) {
        presets.add(new Preset(groupName + "_" + name));
      }
    }

    void nextPreset() {
      if (currentIx + 1 < presets.size()) {
        ++currentIx;
      }
    }

    void prevPreset() {
      if (currentIx > 0) {
        --currentIx;
      }
    }

    @SuppressWarnings("unused")
    void selectPreset(int ix) {
      if (ix >= 0 && ix < presets.size())
        currentIx = ix;
    }

    void selectPreset(String name) {
      String fullname = groupName + "_" + name;
      for (int i = 0; i > presets.size(); i++) {
        Preset p = presets.get(i);
        if (p.getName().equals(fullname)) {
          currentIx = i;
          return;
        }
      }
    }

    /// Search for the next higher preset from the current arm position.
    /// Assumes that arm positions are sorted
    void findNextBestPreset(double armPos) {
      for (int i = 0; i < presets.size(); ++i) {
        Preset p = presets.get(i);
        if (p.isValid() && p.getArmPos() <= armPos) {
          currentIx = i;
          return;
        }
      }
      // Couldn't find a better valid preset, so just go to the next one.
      nextPreset();
    }

    /// Search for the next lower preset from the current arm position
    /// Assumes that arm positions are sorted
    void findPrevBestPreset(double armPos) {
      for (int i = presets.size(); --i >= 0;) {
        Preset p = presets.get(i);
        if (p.isValid() && p.getArmPos() >= armPos) {
          currentIx = i;
          return;
        }
      }
      // Couldn't find a better valid preset, so just go to the prev one.
      prevPreset();
    }

    Preset getCurrentPreset() {
      return presets.get(currentIx);
    }
  }

  public ArmPositioning() {
    requires(arm);
    requires(wrist);

    ShuffleboardTab tab = RobotMap.matchTab;
    presetModeWidget = tab.add("Preset Active", false).withPosition(2, 0).withSize(1, 2).getEntry();
    presetNameWidget = tab.add("Preset Name", "").withPosition(1, 0).getEntry();
    presetValidWidget = tab.add("Preset Valid", false).withPosition(1, 1).withWidget(BuiltInWidgets.kToggleButton)
        .getEntry();
    presetValidWidget.addListener(notice -> {
      if (!notice.value.getBoolean()) {
        currentPresetGroup.getCurrentPreset().invalidate();
      }
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

  }

  @Override
  protected void initialize() {
    manualMode = true;
    currentPresetGroup = hatchPresetGroup;
  }

  private void selectPreset(PresetGroup group, String name) {
    currentPresetGroup = group;
    currentPresetGroup.selectPreset(name);
  }

  @Override
  protected void execute() {
    // Get all the controller inputs
    DriveController controller = chooser.getSelected();
    double manualArmSpeed = Utils.deadzone(controller.getArmSpeed(), manualDeadzone);
    double manualWristSpeed = Utils.deadzone(controller.getWristSpeed(), manualDeadzone);
    boolean hatchGamepieceSelected = controller.getHatchGamepiecePressed();
    boolean cargoGamepieceSelected = controller.getCargoGamepiecePressed();
    boolean presetIncreased = controller.getPresetIncreasedPressed();
    boolean presetDecreased = controller.getPresetDecreasedPressed();
    PresetOption selectedPreset = controller.getSelectedPreset();
    boolean savePreset = controller.getSavePreset();

    // Interpret driver initiated changes
    boolean manualOverride = manualArmSpeed != 0.0 || manualWristSpeed != 0.0;
    boolean presetRequested = presetIncreased || presetDecreased || selectedPreset != PresetOption.NONE;

    if (hatchGamepieceSelected) {
      currentPresetGroup = hatchPresetGroup;
      manualMode = true;
    }
    if (cargoGamepieceSelected) {
      currentPresetGroup = cargoPresetGroup;
      manualMode = true;
    }

    // Dig into the current preset settings
    Preset preset = currentPresetGroup.getCurrentPreset();
    if (presetRequested) {
      if (presetIncreased) {
        // Select higher preset
        if (manualMode)
          currentPresetGroup.findNextBestPreset(arm.getArmPos());
        else
          currentPresetGroup.nextPreset();
      } else if (presetDecreased) {
        // Select lower preset
        if (manualMode)
          currentPresetGroup.findPrevBestPreset(arm.getArmPos());
        else
          currentPresetGroup.prevPreset();
      } else {
        switch (selectedPreset) {
        case HATCH_GROUND:
          selectPreset(hatchPresetGroup, "Ground");
          break;
        case HATCH_1:
          selectPreset(hatchPresetGroup, "1");
          break;
        case HATCH_2:
          selectPreset(hatchPresetGroup, "2");
          break;
        case CARGO_GROUND:
          selectPreset(cargoPresetGroup, "Ground");
          break;
        case CARGO_1:
          selectPreset(cargoPresetGroup, "1");
          break;
        case CARGO_2:
          selectPreset(cargoPresetGroup, "2");
          break;
        case CARGO_BAY:
          selectPreset(cargoPresetGroup, "Bay");
          break;
        default:
          break;
        }
      }

      /*
       * The preset may have changed, update if necessary. (Note: if the index was
       * already at the end of the list, then the same preset will be selected again,
       * but since it's a new request, recheck and potentially re-enable preset mode)
       */
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
      if (arm.hasReliableZero())
        arm.setArmMotor(manualArmSpeed); // Use soft limited open-loop control
      else
        arm.setArmNoLimits(manualArmSpeed); // Use unlimited open-loop control
      wrist.setWristSpeed(manualWristSpeed);

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
    presetModeWidget.setBoolean(!manualMode);
    presetNameWidget.setString(preset.getName());
    presetValidWidget.setBoolean(preset.isValid());

    // Set LEDs
    if (manualMode) {
      Robot.neoPixels.disablePresetIndicator();
    } else {
      if (currentPresetGroup == hatchPresetGroup)
        Robot.neoPixels.selectHatchPresetMode();
      else if (currentPresetGroup == cargoPresetGroup)
        Robot.neoPixels.selectCargoPresetMode();
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void end() {
    arm.brake();
    wrist.brake();
  }
}
