package frc.robot;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;

public class TestMax {
  public static CANSparkMax testMax = new CANSparkMax(14, MotorType.kBrushless);
  public static CANPIDController testMax_PID = testMax.getPIDController();


  public void robotInit(){
   SmartDashboard.putNumber("Max P", testMax_PID.getP());
   SmartDashboard.putNumber("Max I", testMax_PID.getI());
   SmartDashboard.putNumber("Max D", testMax_PID.getD());
   SmartDashboard.putNumber("Max F", testMax_PID.getFF());
   SmartDashboard.putNumber("Max I Zone", testMax_PID.getIZone());   
  }
    
}