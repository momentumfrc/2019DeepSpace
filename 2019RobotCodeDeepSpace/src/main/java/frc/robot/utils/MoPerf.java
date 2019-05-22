package frc.robot.utils;

import edu.wpi.first.wpilibj.Timer;

public class MoPerf implements AutoCloseable {

  private final Timer timer = new Timer();
  private final String name;

  public MoPerf(String name) {
    this.name = name;
    timer.start();
  }

  @Override
  public void close() throws Exception {
    timer.stop();
    if (timer.get() > 0.02) {
      System.out.format("overrun in %s: %f\n", name, timer.get());
    }
  }
}