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

    setDefaultOption("Rainbow", Robot.neoPixels.rainbow);
    addOption("Momentum", Robot.neoPixels.momentum);

    RobotMap.outreachTab.add("LED Animation", this).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 1)
        .withSize(1, 2);

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