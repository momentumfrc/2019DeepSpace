package frc.robot.subsystems;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Subsystem;
 
public class Limelight extends Subsystem{
  public double validTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0); //Valid Target
  public double xOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0); //X offset 
  public double yOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0); //Y offset
  public double targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0); //Area of the target
   
  //Estimating distance//
  private static final double DESIRED_TARGET_AREA = 0;
  private static final double CAMERA_ANGLE = 0;  //a1
  private static final double CAMERA_HEIGHT = 0; //h1
  private static final double TARGET_HEIGHT = 0; //h2
  private static final double TARGET_ANGLE = 0;  //a2
  private double height = TARGET_HEIGHT - CAMERA_HEIGHT;
  private double angle = java.lang.Math.tan(CAMERA_ANGLE + TARGET_ANGLE);
  public double targetDistance = (height) / (angle);

  //http://docs.limelightvision.io/en/latest/cs_estimating_distance.html


	@Override
	protected void initDefaultCommand() {
		
  }
  
  public boolean hasValidTarget(){
    if(validTarget == 1){
      return true;
    } else{
      return false;
    }
  }

  public double[] targetPos(){
    double[] pos = {xOffset, yOffset, targetDistance};
    return pos;
  }

}