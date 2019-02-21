package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Intake extends Subsystem{
    public SpeedControllerGroup intakeRollers = new SpeedControllerGroup(RobotMap.intakeMotorTop, RobotMap.intakeMotorBottom);

    @Override
    protected void initDefaultCommand() {

    }

}