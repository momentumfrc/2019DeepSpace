package frc.robot.choosers;

import org.usfirst.frc.team4999.lights.animations.Animation;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class LEDAnimationChooser extends SendableChooser<Animation> {

  private Animation currentlySet;

  public LEDAnimationChooser() {
    super();

    if (Robot.neoPixels == null) {
      System.out.println("Robot.neoPixels must be initialized before the LEDAnimationChooser!");
      return;
    }

    setDefaultOption("Rainbow", Robot.neoPixels.rainbow);
    addOption("Momentum", Robot.neoPixels.momentum);
    addOption("LED Indexer", Robot.neoPixels.ledIndexer);
    addOption("Socket Listener", Robot.neoPixels.socketListener);

    RobotMap.outreachTab.add("LED Animation", this).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 1)
        .withSize(2, 1);

    currentlySet = getSelected();
  }

  public void poll() {
    Animation selected = getSelected();
    if (selected != currentlySet) {
      currentlySet = selected;
      Robot.neoPixels.setBaseAnimation(selected);
    }
  }
}