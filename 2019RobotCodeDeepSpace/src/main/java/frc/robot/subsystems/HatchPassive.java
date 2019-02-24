package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class HatchPassive extends Subsystem{
private Solenoid hatch = RobotMap.hatchKicker;

public void kickHatch(){
    hatch.set(true);
}

public void dontKick(){
    hatch.set(false);
}

@Override
protected void initDefaultCommand() {
	
}
}