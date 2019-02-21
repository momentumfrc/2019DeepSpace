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
    public double wristOffset;
    public SendableCANPIDController pid;

    private static final double GEAR_RATIO = 1 / 32; // 16:1 CIM Sport into a 32:16 Chain and Sprocket

    public Wrist() {
        super("Wrist");
        pid = pid_Wrist;
        addChild(m_Wrist);
        addChild(pid);
    }

    /*
     * Allows the wrist to be controlled with raw input
     */
    public void setWristNoLimits(double speed) {
        m_Wrist.set(speed);
    }

    /*
     * Get the current position of the Wrist relative to the offset/zero position
     */
    public double getWristPos() {
        return e_Wrist.getPosition() - wristOffset;
    }

    /*
     * Defines the current position of the Wrist as the offset/zero positon
     */
    public void ZeroWrist() {
        wristOffset = e_Wrist.getPosition();
    }

    public void setWristMotor(double speed) {
        double wrist_pos = calculateWristDegrees();
        if (speed > 0 && wrist_pos >= MoPrefs.getMaxWristRotation()) {
            System.out.format("Wrist at max rotation (%d)", wrist_pos);
            m_Wrist.set(0);
        } else if (speed < 0 && wrist_pos <= MoPrefs.getMinWristRotation()) {
            System.out.format("Wrist at min rotation (%d)", wrist_pos);
            m_Wrist.set(0);
        } else {
            m_Wrist.set(speed);
        }
    }

    public void stopWrist(double speed) {
        m_Wrist.set(0);
    }

    /*
     * Define the Wrist's position in degrees instead of the native rotations
     */
    private double calculateWristDegrees() {
        return getWristPos() * GEAR_RATIO * 360;
    }

    public void drivePID(){
        setWristMotor(pid.get());
    }
    @Override
    protected void initDefaultCommand() {
    }

}