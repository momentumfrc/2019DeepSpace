package frc.robot.controllers;

import frc.robot.RobotMap;
import frc.robot.utils.Utils;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class XboxF310Wrapper implements DriveController {

    private XboxController xbox = RobotMap.xbox;
    private LogitechF310 logitech = RobotMap.f310;

    private static final double MOVE_CURVE = 2.5;
    private static final double TURN_CURVE = 2.5;

    private static final double DEADZONE = 0.1;

    private static final double[] SPEEDS = { .125, .25, .375, .5, .625, .875, 1 };
    private int currentSpeed = SPEEDS.length - 1;

    private static final double MAX_ARM_SPEED = .8;
    private static final double MAX_WRIST_SPEED = .8;

    // XBOX CONTROLS//
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
        if (xbox.getYButtonPressed() && currentSpeed < SPEEDS.length - 1) {
            ++currentSpeed;
        } else if (xbox.getXButtonPressed() && currentSpeed > 0) {
            --currentSpeed;
        }

        return SPEEDS[currentSpeed];
    }

    @Override
    public boolean getReverseDirectionPressed() {
        return xbox.getBButtonPressed();
    }

    @Override
    public double getArmUp() {
        double rT = xbox.getTriggerAxis(Hand.kRight);
        double RightT = Utils.map(rT, 0, 1, 0, MAX_ARM_SPEED);
        return RightT;
    }

    @Override
    public double getArmDown() {
        double lT = xbox.getTriggerAxis(Hand.kLeft);
        double LeftT = Utils.map(lT, 0, 1, 0, -MAX_ARM_SPEED);
        return LeftT;
    }

    // F310 CONTROLS//
    @Override
    public double getWristSpeed() {
        double val = Utils.clip(logitech.getY(Hand.kLeft) + logitech.getY(Hand.kRight), -1, 1);
        return Utils.map(val, -1, 1, -MAX_WRIST_SPEED, MAX_WRIST_SPEED);
    }

}
