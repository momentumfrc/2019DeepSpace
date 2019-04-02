package frc.robot.utils;

import edu.wpi.first.wpilibj.Preferences;

public class MoPrefs {

  private static Preferences prefs = Preferences.getInstance();

  // Number of Encoder ticks in 1 foot of Travel:
  // Encoder dip switch settings (0 1 1 1)
  // = 100 ticks/revolution = pi * 6" = 18.85" = 1.57'
  public static final double DRIVE_ENC_TICKS_PER_FOOT = 63.66;

  public static final double MAX_ARM_ROTATION = 125; // Degrees TODO MAX encoder is in units of rotations
  public static final double MIN_ARM_ROTATION = 0;

  public static final double MAX_WRIST_ROTATION = 180; // Degrees TODO MAX encoder is in units of rotations
  public static final double MIN_WRIST_ROTATION = 0;

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

  public static double getDriveEncTicksPerFoot() {
    return getDouble("DRIVE_ENC_TICKS_PER_FOOT", DRIVE_ENC_TICKS_PER_FOOT);
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