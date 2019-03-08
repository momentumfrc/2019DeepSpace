package frc.robot.utils;

import edu.wpi.first.wpilibj.Preferences;

public class MoPrefs {

  private static Preferences prefs = Preferences.getInstance();

  public static final double DRIVE_ENC_TICKS = 1; // Number of Encoder ticks in 1 foot of Travel
  // Encoder dip switch settings = 100 = 0 1 1 1

  public static final double MAX_ARM_ROTATION = 125; // Degrees
  public static final double MIN_ARM_ROTATION = 0;

  public static final double MAX_WRIST_ROTATION = 180; // Degrees
  public static final double MIN_WRIST_ROTATION = 0;

  // ARM PRESETS //
  // TODO set all of these positions to the correct angles
  public static final double ARM_GROUND_POS = 0;
  public static final double ARM_LOADINGSTATION_POS = 1;

  public static final double ARM_LEVELONE_HATCH = 2;
  public static final double ARM_LEVELONE_CARGO = 2;

  public static final double ARM_LEVELTWO_HATCH = 3;
  public static final double ARM_LEVELTWO_CARGO = 3;

  // WRIST PRESETS //
  // TODO set all of these positions to the correct angles
  public static final double WRIST_GROUND_POS = 0;
  public static final double WRIST_LOADINGSTATION_POS = 1;

  public static final double WRIST_LEVELONE_HATCH = 2;
  public static final double WRIST_LEVELONE_CARGO = 2;

  public static final double WRIST_LEVELTWO_HATCH = 3;
  public static final double WRIST_LEVELTWO_CARGO = 3;

  public static final double MAX_MOTOR_TEMP = 10;

  public static double getDouble(String key, double def) {
    if (!prefs.containsKey(key)) {
      prefs.putDouble(key, def);
    }
    return prefs.getDouble(key, def);
  }

  public static void setDouble(String key, double value) {
    prefs.putDouble(key, value);
  }

  public static double getDriveEncTicks() {
    return getDouble("DRIVE_ENC_TICKS", DRIVE_ENC_TICKS);
  }

  public static double getMaxArmRotation() {
    return getDouble("MAX_ARM_ROTATION", MAX_ARM_ROTATION);
  }

  public static double getMinArmRotation() {
    return getDouble("MIN_ARM_ROTATION", MIN_ARM_ROTATION);
  }

  public static double getMaxWristRotation() {
    return getDouble("MAX_WRIST_ROTATION", MAX_WRIST_ROTATION);
  }

  public static double getMinWristRotation() {
    return getDouble("MIN_WRIST_ROTATION", MIN_WRIST_ROTATION);
  }

  public static double getMaxMotorTemp() {
    return getDouble("MAX_MOTOR_TEMP", MAX_MOTOR_TEMP);
  }
}