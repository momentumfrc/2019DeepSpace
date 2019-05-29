package frc.robot.utils;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.Timer;

public class MoPerfMon {
  private String name;
  private FileWriter file;

  public synchronized void start(String name) {
    try {
      this.name = String.format("/home/lvuser/%s.aat", name);
      System.err.format("Started PerfMon to file: %s\n", this.name);
      file = new FileWriter(this.name);
      file.write("1000000\n");
    } catch (IOException e) {
      System.err.format("Failed to open MoPerfMon file: %s\n", this.name);
      System.err.println(e.getMessage());
      this.name = null;
      file = null;
    }
  }

  public void start() {
    start(String.format("MoPerfMon_%d", (int) (Timer.getFPGATimestamp() * 1000000)));
  }

  public synchronized void stop() {
    if (file != null) {
      try {
        file.close();
      } catch (IOException e) {
        System.err.format("Failed to close MoPerfMon file: %s\n", name);
      }
      name = null;
      file = null;
    }
  }

  private synchronized void post(String label, int value) {
    if (file != null) {
      try {
        file.write(String.format("%s,%d,%d\n", label, (int) (Timer.getFPGATimestamp() * 1000000), value));
      } catch (IOException e) {
      }
    }
  }

  public void postValue(String label, int value) {
    post(label, value);
  }

  public Period newPeriod(String label) {
    return new Period(label);
  }

  public class Period implements Closeable {

    private String label;

    private Period(String label) {
      this.label = label;
      post(this.label, 1);
    }

    @Override
    public void close() {
      post(this.label, 0);
    }

  }
}