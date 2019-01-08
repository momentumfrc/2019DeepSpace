package frc.robot.utils;

import frc.robot.pid.*;
import frc.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDFactory extends MomentumPIDFactoryBase {
	
	private static final String MOVE_FILE = "movePID.properties";
	private static final String TURN_FILE = "turnPID.properties";
	
	public static MomentumPID getMovePID() {
		String path = LOCATION + MOVE_FILE;
		PIDSource source = RobotMap.leftDriveEncoder;
		source.setPIDSourceType(PIDSourceType.kDisplacement);
		return loadPID("MovePID", path, source, null);
	}
	
	public static MomentumPID getTurnPID() {
		String path = LOCATION + TURN_FILE;
		PIDSource source = new PIDSource() {

			PIDSourceType type= PIDSourceType.kDisplacement;
			
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
				if(type == PIDSourceType.kDisplacement) {
					return RobotMap.navx.getAngle();
				}else{
					return RobotMap.navx.getRate(); 
				}
			}
			
		};
		source.setPIDSourceType(PIDSourceType.kDisplacement);
		return loadPID("TurnPID",path, source, null);
	}

}