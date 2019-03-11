package frc.robot.controllers;

public interface DriveController {

  abstract public double getMoveRequest();

  abstract public double getTurnRequest();

  abstract public double getSpeedLimiter();

  abstract public boolean getReverseDirectionPressed();

  abstract public double getArmSpeed();

  abstract public boolean getKick();

  abstract public double getWristSpeed();

  public abstract boolean getPresetIncreasedPressed();

  public abstract boolean getPresetDecreasedPressed();

  public abstract boolean getSavePreset();

  public abstract boolean getGamepieceType0Pressed();

  public abstract boolean getGamepieceType1Pressed();

}