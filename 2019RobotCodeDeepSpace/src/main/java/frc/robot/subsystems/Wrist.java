package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import org.usfirst.frc.team4999.pid.SendableCANPIDController;
import frc.robot.utils.PIDFactory;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.utils.MoPrefs;


public class Wrist extends Subsystem {

    private CANSparkMax m_Wrist = RobotMap.wristMotor;
    private CANEncoder e_Wrist = m_Wrist.getEncoder();
    private SendableCANPIDController pid_Wrist = PIDFactory.getWristPID();
    public double WristPos_Zero;

    private static final double GEAR_RATIO = 1 / 32; // 16:1 CIM Sport into a 32:16 Chain and Sprocket

    public Wrist() {
        super("Wrist");
        addChild(m_Wrist);
        addChild(pid_Wrist);
    }

    public void setWristNoLimits(double speed){
        m_Wrist.set(speed);
    }

    public void WristPosZero(){
        //e_Wrist.getPosition() == WristPos_Zero;
    }

    public void setArmMotor(double speed){
        double wrist_pos = calculateWristDegrees();
        if(speed > 0 && wrist_pos >= MoPrefs.getMaxWristRotation()){
            System.out.format("Wrist at max rotation (%d)", wrist_pos);
            m_Wrist.set(0);
        } else if(speed < 0 && wrist_pos <= MoPrefs.getMinWristRotation()){
            System.out.format("Wrist at min rotation (%d)", wrist_pos);
            m_Wrist.set(0);
        } else {
    		m_Wrist.set(speed);
        }
    }

    
    public void stopWrist(double speed){
        m_Wrist.set(0);    
    }

    private double calculateWristDegrees() {
        return e_Wrist.getPosition() * GEAR_RATIO * 360;
    }


    @Override
    protected void initDefaultCommand() {
    }

}