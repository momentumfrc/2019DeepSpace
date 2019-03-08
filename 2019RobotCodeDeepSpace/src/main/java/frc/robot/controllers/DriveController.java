package frc.robot.controllers;

public interface DriveController {

  abstract public double getMoveRequest();

  abstract public double getTurnRequest();

  abstract public double getSpeedLimiter();

  abstract public boolean getReverseDirectionPressed();

  abstract public double getArmSpeed();

  abstract public double getNextPos();

  abstract public double getPrevPos();

  abstract public boolean getKick();

  /*
   * abstract public double getArmUp();
   * 
   * abstract public double getArmDown();
   */

  abstract public double getWristSpeed();

}