package frc.robot.utils;

import org.usfirst.frc.team4999.pid.*;
import frc.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import com.revrobotics.CANSparkMax;

public class PIDFactory extends MomentumPIDFactoryBase {

  private static final String MOVE_FILE = "movePID.properties";
  private static final String TURN_FILE = "turnPID.properties";

  private static final String ARM_FILE = "armPID.properties";
  private static final String WRIST_FILE = "wristPID.properties";

  public static MomentumPID getMovePID() {
    String path = LOCATION + MOVE_FILE;
    PIDSource source = RobotMap.leftDriveEncoder;
    source.setPIDSourceType(PIDSourceType.kDisplacement);
    return loadPID("MovePID", path, source, null);
  }

  public static MomentumPID getTurnPID() {
    String path = LOCATION + TURN_FILE;
    PIDSource source = new PIDSource() {

      PIDSourceType type = PIDSourceType.kDisplacement;

      @Override
      public void setPIDSourceType(PIDSourceType pidSource) {
        type = pidSource;
      }

      @Override
      public PIDSourceType getPIDSourceType() {
        return type;
      }

      @Override
      public double pidGet() {
        if (type == PIDSourceType.kDisplacement) {
          return RobotMap.navx.getAngle();
        } else {
          return RobotMap.navx.getRate();
        }
      }

    };
    source.setPIDSourceType(PIDSourceType.kDisplacement);
    return loadPID("TurnPID", path, source, null);
  }

  public static SendableCANPIDController getArmPID() {
    String path = LOCATION + ARM_FILE;
    CANSparkMax max = RobotMap.armMotor;
    return loadCANPID("ArmPID", path, max);
  }

  public static SendableCANPIDController getWristPID() {
    String path = LOCATION + WRIST_FILE;
    CANSparkMax max = RobotMap.wristMotor;
    return loadCANPID("wristPID", path, max);
  }

}
