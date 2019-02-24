package frc.robot.utils;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;

public class MoPDP extends PowerDistributionPanel {

  private final int NUM_CHANNELS = 16; // 16 channels on the PDP
  private final long[] overtime = new long[NUM_CHANNELS];
  private final double[] overThresh = new double[NUM_CHANNELS];
  private final long[] undertime = new long[NUM_CHANNELS];
  private final double[] underThresh = new double[NUM_CHANNELS];

  private long undervoltTimer;
  private double undervoltThreshold;

  private long getTimeMillis() {
    return (long) (Timer.getFPGATimestamp() * 1000);
  }

  public MoPDP() {
    for (int i = 0; i < NUM_CHANNELS; ++i) {
      overtime[i] = undertime[i] = getTimeMillis();
      overThresh[i] = 120; // Main breaker limit
      underThresh[i] = 0;
    }

    undervoltTimer = getTimeMillis();
    undervoltThreshold = 8; // 8 volts is a very low voltage for a robot
  }

  public void periodic() {
    for (int i = 0; i < NUM_CHANNELS; ++i) {
      double current = getCurrent(i);
      if (current <= overThresh[i])
        overtime[i] = getTimeMillis();
      if (current >= underThresh[i])
        undertime[i] = getTimeMillis();
    }

    if (getVoltage() >= undervoltThreshold)
      undervoltTimer = getTimeMillis();
  }

  public void setOvercurrentThreshold(int channel, double current) {
    overThresh[channel] = current;
    overtime[channel] = getTimeMillis();
  }

  public void setOvercurrentThresholds(int[] channels, double current) {
    for (int channel : channels)
      setOvercurrentThreshold(channel, current);
  }

  public void setundercurrentThreshold(int channel, double current) {
    underThresh[channel] = current;
    undertime[channel] = getTimeMillis();
  }

  public void setundervoltageThreshold(double voltage) {
    undervoltThreshold = voltage;
    undervoltTimer = getTimeMillis();
  }

  /**
   * Checks if a channel of the PDP has been above the set current threshold for
   * the specified period of time
   * 
   * @param channel    Channel of the PDP to check
   * @param cutofftime Cutoff time in milliseconds
   * @return If the channel is over the current limit
   */
  public boolean checkOvercurrent(int channel, long cutofftime) {
    return getTimeMillis() - overtime[channel] > cutofftime;
  }

  /**
   * Checks if some channels of the PDP has been above the set current threshold
   * for the specified period of time
   * 
   * @param channels   Channels of the PDP to check
   * @param cutofftime Cutoff time in milliseconds
   * @return If any of the channels are over the current limit
   */
  public boolean checkOvercurrent(int[] channels, int cutofftime) {
    for (int channel : channels) {
      if (checkOvercurrent(channel, cutofftime))
        return true;
    }
    return false;
  }

  /**
   * Checks if a channel of the PDP has been below the set current threshold for
   * the specified period of time
   * 
   * @param channel    Channel of the PDP to check
   * @param cutofftime Cutoff time in milliseconds
   * @return If the channel is under the current limit
   */
  public boolean checkUndercurrent(int channel, int cutofftime) {
    return getTimeMillis() - undertime[channel] > cutofftime;
  }

  /**
   * Checks if some channels of the PDP has been below the set current threshold
   * for the specified period of time
   * 
   * @param channels   Channels of the PDP to check
   * @param cutofftime Cutoff time in milliseconds
   * @return If any of the channels are below the current limit
   */
  public boolean checkUndercurrent(int[] channels, int cutofftime) {
    for (int channel : channels) {
      if (checkUndercurrent(channel, cutofftime))
        return true;
    }
    return false;
  }

  /**
   * Checks if the battery voltage has been below the set voltage threshold for
   * the specified period of time
   * 
   * @param cutofftime Cutoff time in milliseconds
   * @return If the voltage is below the voltage limit
   */
  public boolean checkUndervoltage(int cutofftime) {
    return getTimeMillis() - undervoltTimer < cutofftime;
  }

}