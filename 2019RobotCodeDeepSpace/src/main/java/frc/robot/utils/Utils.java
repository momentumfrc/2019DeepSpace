package frc.robot.utils;

public class Utils {

  public static double clip(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  public static double map(double val, double inmin, double inmax, double outmin, double outmax) {
    return (((val - inmin) / (inmax - inmin)) * (outmax - outmin)) + outmin;
  }

  public static double deadzone(double val, double deadzone) {
    if (val < -deadzone)
      return Utils.map(val, -1, -deadzone, -1, 0);
    else if (val > deadzone)
      return Utils.map(val, deadzone, 1, 0, 1);
    else
      return 0;
  }

  public static double curve(double val, double curve) {
    if (curve == 0)
      return val;
    double powed = Math.pow(Math.abs(val), curve);
    // because the power is applied to the absolute value of val, powed is
    // guaranteed to be non-negative
    return val >= 0 ? powed : -powed;
  }

}
