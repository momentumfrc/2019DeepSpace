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
    public double armOffset;
    public SendableCANPIDController pid;

    private static final double GEAR_RATIO = 1 / 168; // 36:1 CIM Sport into a 18:84 Gear Ratio

    public Arm() {
        super("Arm");
        pid = pid_Arm;
        addChild(m_Arm);
        addChild(pid);
    }

    /*
     * Allows the wrist to be controlled with raw input
     */
    public void setArmNoLimits(double speed) {
        m_Arm.set(speed);
    }

    public double getArmPos() {
        return e_Arm.getPosition();
    }

    /*
     * Defines the current position of the Arm as the offset/zero positon
     */
    public void zeroArm() {
        e_Arm.setPosition(0);
    }

    public void setArmMotor(double speed) {
        double arm_pos = calculateArmDegrees();
        if (speed > 0 && arm_pos >= MoPrefs.getMaxArmRotation()) {
            System.out.format("Arm at max rotation (%d)", arm_pos);
            m_Arm.set(0);
        } else if (speed < 0 && arm_pos <= MoPrefs.getMinArmRotation()) {
            System.out.format("Arm at min rotation (%d)", arm_pos);
            m_Arm.set(0);
        } else {
            m_Arm.set(speed);
        }
    }

    public void stop() {
        m_Arm.set(0);
    }

    /*
     * Define the Arm's position in degrees instead of the native rotations
     */
    public double calculateArmDegrees() {
        return getArmPos() * GEAR_RATIO * 360;
    }

    public void drivePID() {
        setArmMotor(pid.get());
    }

    @Override
    protected void initDefaultCommand() {
    }

}