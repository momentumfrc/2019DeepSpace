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
      this.name = name + ".aat";
      file = new FileWriter(this.name);
      file.write("1000");
    } catch (IOException e) {
      System.err.format("Failed to open MoPerfMon file: %s\n", this.name);
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
        file.write(String.format("%s,%d,%d\n", label, (int) (Timer.getFPGATimestamp() * 1000), value));
      } catch (IOException e) {
      }
    }
  }

  public void postValue(String label, int value) {
    post(label, value);
  }

  public class Period implements Closeable {

    private String label;

    Period(String label) {
      this.label = label;
      post(this.label, 1);
    }

    @Override
    public void close() throws IOException {
      post(this.label, 0);
    }

  }
}