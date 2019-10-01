package frc.robot.controllers;

import edu.wpi.first.wpilibj.GenericHID;

public class ButtonBoard extends GenericHID {

  public ButtonBoard(int port) {
    super(port);
  }

  public boolean getButton(int ix) {
    return getRawButton(ix);
  }

  @Override
  public double getX(Hand hand) {
    return getRawAxis(2);
  }

  @Override
  public double getY(Hand hand) {
    return getRawAxis(1);
  }

}
