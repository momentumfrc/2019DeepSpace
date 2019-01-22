package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import org.usfirst.frc.team4999.pid.SendableCANPIDController;
import frc.robot.utils.PIDFactory;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.MoPrefs;


public class Arm extends Subsystem {

    private CANSparkMax m_Arm = RobotMap.armMotor;
    private CANEncoder e_Arm = m_Arm.getEncoder();
    private SendableCANPIDController pid_Arm = PIDFactory.getArmPID();

    private static final double GEAR_RATIO = 1 / 100; // TODO set ratio once gearbox is chosen 

    
    // This will only run once. So posArm will only contain the arm's position when it first starts
    // public double rotArmMotor = e_Arm.getPosition(); //Number of rotations the motor has done 
    // public double rotArm = rotArmMotor * GEAR_RATIO; //Number of rotatins the arm has done
    // public double posArm = rotArm *360;

    // If you define the variable motorTemp like this, it will only be set once. So it'll only ever hold the temperature from when the motor first turns on
    // private double motorTemp = m_Arm.getMotorTemperature();

    public Arm() {
        super("Arm");
        addChild(m_Arm);
        addChild(pid_Arm);
    }

    public void setArmMotor(double speed){
        double arm_pos = calculateArmDegrees();
        if(speed > 0 && arm_pos >= MoPrefs.getMaxArmRotation()){
            System.out.format("Arm at max rotation (%d)", arm_pos);
            m_Arm.set(0);
        } else if(speed < 0 && arm_pos <= MoPrefs.getMinArmRotation()){
            System.out.format("Arm at min rotation (%d)", arm_pos);
            m_Arm.set(0);
        } else {
    		m_Arm.set(speed);
    	}
    }

    // This name is misleading. It makes it sound like the method stops the arm, when really it checks the motor temperature
    public void stopArm(double speed){
        // This way we check the motor temperature using the most up-to-date value
        // But this check will only run whenever Arm.stopArm() is called
        // So really, a trigger should be set up so that it becomes true whenever RobotMap.armMotor.getMotorTemperature() > MoPrefs.getMaxMotorTemp()
        // And then that trigger would be set up in OI to turn the motor off when it triggers
        double motorTemp = m_Arm.getMotorTemperature();
        if(motorTemp >= MoPrefs.getMaxMotorTemp()){
            m_Arm.set(0);
        }
    }

    private double calculateArmDegrees() {
        return e_Arm.getPosition() * GEAR_RATIO * 360;
    }


    @Override
    protected void initDefaultCommand() {
    }

}