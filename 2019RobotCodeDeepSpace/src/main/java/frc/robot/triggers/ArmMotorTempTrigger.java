package frc.robot.triggers;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.robot.RobotMap;

public class ArmMotorTempTrigger extends Trigger {
  private static final double TEMP_CUTOFF = 100; // Degrees Celcius
  private static final double TIME_CUTOFF = 7500; // Time in ms that the cutoff will take to activate

  private long start_time;

  private long getTimeMillis() {
    return (long) (1000 * Timer.getFPGATimestamp());
  }

  @Override
  public boolean get() {
    if (RobotMap.armMotor.getMotorTemperature() > TEMP_CUTOFF) {
      return getTimeMillis() - start_time > TIME_CUTOFF;
    } else {
      start_time = getTimeMillis();
      return false;
    }
  }

}