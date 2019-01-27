package frc.robot.triggers;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.robot.RobotMap;

public class ArmMotorTempTrigger extends Trigger{
    private static final double TEMP_CUTOFF = 100; //Degrees Celcius
    private static final double TIME_CUTOFF = 7500; //Time in ms that the cutoff will take to activate
    


    private CANSparkMax Motor = RobotMap.armMotor;


    @Override
    public boolean get() {
        return false;
    }


}