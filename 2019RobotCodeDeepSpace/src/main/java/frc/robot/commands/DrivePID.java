package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utils.MoPID;
import frc.robot.*;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;

public class DrivePID extends Command {

  private DriveSubsystem drive = Robot.driveSystem;
  private ControlChooser chooser = Robot.controlChooser;
  private MoPID movePID, turnPID;

  private double headTailDirection = 1.0; // positive 1 for forward, -1 for reverse

  public DrivePID() {
    requires(drive);
  }

  @Override
  protected void initialize() {
    movePID = MoPID.makePIDFromPrefs("MoveRatePID");
    turnPID = MoPID.makePIDFromPrefs("TurnRatePID");

    SmartDashboard.putData(movePID);
    SmartDashboard.putData(turnPID);
  }

  @Override
  protected void execute() {
    DriveController controller = chooser.getSelected();
    if (controller.getReverseDirectionPressed()) // "Pressed" methods only return true on the first query after the
                                                 // button was pressed
      headTailDirection = -headTailDirection;

    // Gets user control inputs
    double moveRequest = controller.getMoveRequest() * headTailDirection;
    double turnRequest = controller.getTurnRequest();

    // Get the PID corrections
    double moveCorrection = movePID.calculate(moveRequest, drive.getMoveRate());
    double turnCorrection = turnPID.calculate(turnRequest, drive.getTurnRate());

    // Calculate final drive
    double move = moveRequest + moveCorrection;
    double turn = turnRequest + turnCorrection;

    drive.arcadeDrive(move, turn, controller.getSpeedLimiter());
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  public void end() {
    drive.stop();
  }

  @Override
  public void interrupted() {
    end();
  }

}