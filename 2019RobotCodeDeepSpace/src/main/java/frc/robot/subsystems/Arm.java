package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.MoPrefs;


public class Arm extends Subsystem {

    private static final int DeviceID = 1; 

    private CANSparkMax m_Arm = RobotMap.armMotor;
    private CANEncoder e_Arm = RobotMap.armEncoder;
    private CANPIDController pid_Arm = RobotMap.armPID;

    private static final double GEAR_RATIO = 1 / 100; // TODO set ratio once gearbox is chosen 

    public double rotArmMotor = e_Arm.getPosition(); //Number of rotations the motor has done 
    public double rotArm = rotArmMotor * GEAR_RATIO; //Number of rotatins the arm has done
    public double posArm = rotArm *360;

    private double motorTemp = m_Arm.getMotorTemperature();

    private static final int MIN_POS = 1;// TODO: Set these angles

    public int kP_arm, kI_arm, kD_arm, kF_arm;{
        //PID Contstants
        kP_arm = 0;
        kI_arm = 0;
        kD_arm = 0;
        kF_arm = 0;
    }

    public void ArmPID(){
        pid_Arm.getP(kP_arm);
        pid_Arm.getI(kI_arm);
        pid_Arm.getD(kD_arm);   
        pid_Arm.getFF(kF_arm);           
    }

    public void setArmMotor(double speed){
        if(speed > 0 && posArm >= MoPrefs.getMaxArmRotation()){
            System.out.format("Arm at max rotation (%d)");
            m_Arm.set(0);
        } else if(speed < 0 && posArm <= MIN_POS){
            System.out.format("Arm at min rotation (%d)");
            m_Arm.set(0);
        } else {
    		m_Arm.set(speed);
    	}
    }

    public void stopArm(double speed){
        if(motorTemp >= MoPrefs.getMaxMotorTemp()){
            m_Arm.set(0);
        }
    }


    @Override
    protected void initDefaultCommand() {
    }

}