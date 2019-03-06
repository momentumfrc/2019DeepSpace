package frc.robot.commands;

import org.usfirst.frc.team4999.pid.MomentumPID;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.*;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;

public class DrivePID extends Command {

  private DriveSubsystem drive = Robot.driveSystem;
  private ControlChooser chooser = Robot.controlChooser;
  private MomentumPID movePID, turnPID;

  private double headTailDirection = 1.0; // positive 1 for forward, -1 for reverse

  public DrivePID() {
    requires(drive);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    SmartDashboard.putData(RobotMap.leftDriveEncoder);
    SmartDashboard.putData(RobotMap.rightDriveEncoder);

    DriveController controller = chooser.getSelected();
    if (controller.getReverseDirectionPressed()) // "Pressed" methods only return true on the first query after the
                                                 // button was pressed
      headTailDirection = -headTailDirection;

    // Gets user controll inputs
    double moveRequest = controller.getMoveRequest() * headTailDirection;
    double turnRequest = controller.getTurnRequest();

    // Calculates the error
    double moveError = moveRequest - drive.getMoveRate();
    double turnError = turnRequest - drive.getTurnRate();

    // THe PID thing

    drive.arcadeDrive(moveRequest, controller.getTurnRequest(), controller.getSpeedLimiter());
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