package frc.robot.utils;

import edu.wpi.first.wpilibj.Preferences;

public class MoPrefs{

    private static Preferences prefs = Preferences.getInstance();

    public static final double DRIVE_ENC_TICKS = 1; //Number of Encoder ticks in 1 foot of Travel
    //Encoder dip switch settings = 100 = 0 1 1 1

    public static final double MAX_ARM_ROTATION = 125; //Degrees
    public static final double MIN_ARM_ROTATION = 0;

    public static final double MAX_WRIST_ROTATION = 180; //Degrees
    public static final double MIN_WRIST_ROTATION = 0;

    public static final double MAX_MOTOR_TEMP = 10;

    private static double getDouble(String key, double def) {
		if(!prefs.containsKey(key)) {
			prefs.putDouble(key, def);
		}
		return prefs.getDouble(key,  def);
    }
    
    public static double getDriveEncTicks(){
        return getDouble("DRIVE_ENC_TICKS", DRIVE_ENC_TICKS);
    }

    public static double getMaxArmRotation(){
        return getDouble("MAX_ARM_ROTATION", MAX_ARM_ROTATION);
    }

    public static double getMinArmRotation() {
        return getDouble("MIN_ARM_ROTATION", MIN_ARM_ROTATION);
    }

    public static double getMaxWristRotation(){
        return getDouble("MAX_WRIST_ROTATION", MAX_WRIST_ROTATION);
    }

    public static double getMinWristRotation() {
        return getDouble("MIN_WRIST_ROTATION", MIN_WRIST_ROTATION);
    }

    public static double getMaxMotorTemp(){
        return getDouble("MAX_MOTOR_TEMP", MAX_MOTOR_TEMP);
    }
}