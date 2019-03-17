package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.*;
import frc.robot.choosers.ControlChooser;
import frc.robot.controllers.DriveController;

public class DriveCommand extends Command {

  private DriveSubsystem drive = Robot.driveSystem;
  private ControlChooser chooser = Robot.controlChooser;

  private double headTailDirection = 1.0; // positive 1 for forward, -1 for reverse

  public DriveCommand() {
    requires(drive);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    DriveController controller = chooser.getSelected();
    if (controller.getReverseDirectionPressed()) // "Pressed" methods only return true on the first query after the
                                                 // button was pressed
      headTailDirection = -headTailDirection;
    double moveRequest = controller.getMoveRequest() * headTailDirection;
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