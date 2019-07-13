package frc.robot.utils;

/*
 * This is intended as a program allowing the team to easily log match data to
 * USB flash drive so that robot preformance can be tuned and monitored.
 */

import java.io.File;
import java.util.logging.*;
import edu.wpi.first.wpilibj.DriverStation;

public class MoLogging {

  // Desired File path: eventName//match + Match Number//LOGS
  public MoLogging() {
    newMatch();
  }

  private void newMatch() {
    String event = getMatchInfo().event();
    int match = getMatchInfo().matchNumber();
    String filenameMatch = Integer.toString(match);
    File eventFolder = new File(event);
    eventFolder.mkdir();
    File matchFolder = new File(eventFolder, filenameMatch);
    matchFolder.mkdir();
  }

  public MatchInfo getMatchInfo() {
    return new MatchInfo(DriverStation.getInstance().getEventName(), DriverStation.getInstance().getMatchNumber(),
        DriverStation.getInstance().getLocation());
  }

  public class MatchInfo {
    private final int matchNumber;
    private final int location;
    private final String event;

    private MatchInfo(String event, int matchNumber, int location) {
      this.matchNumber = matchNumber;
      this.location = location;
      this.event = event;
    }

    // Returns the Match Number that is being recorded
    public int matchNumber() {
      return matchNumber;
    }

    // Returns the Location for the driver station during the match
    public int location() {
      return location;
    }

    // Returns the Event name
    public String event() {
      return event;
    }
  }
}