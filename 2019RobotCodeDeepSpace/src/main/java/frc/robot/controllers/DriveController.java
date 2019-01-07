package frc.robot.controllers;

public interface  DriveController {
	
	abstract public double getMoveRequest();
	
	abstract public double getTurnRequest();
	
	abstract public double getSpeedLimiter();

	abstract public boolean getReversedDirection();
	
}