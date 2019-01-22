package frc.robot.controllers;

import frc.robot.RobotMap;
import frc.robot.utils.Utils;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class XboxF310Wrapper implements DriveController {

    private XboxController xbox = RobotMap.xbox;
    private LogitechF310 logitech = RobotMap.f310;

    private static final double MOVE_CURVE = 2.5; 
    private static final double TURN_CURVE = 2.5; 

    private static final double DEADZONE = 0.1; 

    private static final double[] SPEEDS = {.125, .25, .375, .5, .625, .875, 1};
    private int currentSpeed = SPEEDS.length - 1;


    private static final double MAX_ARM_SPEED = 1;
    private static final double MIN_ARM_SPEED = 0;



    //XBOX CONTROLS//
    @Override
    public double getMoveRequest() {
        double moveRequest = xbox.getY(XboxController.Hand.kLeft);
        moveRequest = Utils.deadzone(moveRequest, DEADZONE);
    	moveRequest = Utils.curve(moveRequest, MOVE_CURVE);
        return moveRequest;
    }

    @Override
    public double getTurnRequest() {
        double turnRequest = xbox.getX(XboxController.Hand.kRight);
        turnRequest = Utils.deadzone(turnRequest, DEADZONE);
    	turnRequest = Utils.curve(turnRequest, TURN_CURVE);
        return turnRequest;
    }

    @Override
    public double getSpeedLimiter() {
        if(xbox.getYButtonPressed() && currentSpeed < SPEEDS.length - 1) {
			currentSpeed++;
		} else if(xbox.getXButtonPressed() && currentSpeed > 0) {
			currentSpeed--;
		}
		
		return SPEEDS[currentSpeed];
    }

    @Override
    public boolean getReversedDirection() {
        return xbox.getBButtonPressed();
    }

    @Override
    public double getArmSpeed() {
        double rT = xbox.getTriggerAxis(Hand.kRight);
        double lT = xbox.getTriggerAxis(Hand.kLeft);
        if (rT >= .5) {
            return MAX_ARM_SPEED;
        } else if(lT >= .5) {
            return -MAX_ARM_SPEED;
        } else {
            return 0;
        }

    }
    //F310 CONTROLS//
    //TODO: When mechanisms decided up add the controls for the F310
}
