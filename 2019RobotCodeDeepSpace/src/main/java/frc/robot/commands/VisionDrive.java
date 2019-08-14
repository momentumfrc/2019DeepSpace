package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.vision.Limelight.LimelightData;
import frc.robot.utils.Utils;

public class VisionDrive extends Command {
  private DriveSubsystem drive = Robot.driveSystem;
  private Limelight limelight = Robot.limelight;

  public double limelightTurn;
  public double limelightMove;
  public boolean met;

  // TODO Tune these values
  public static final double TURN_K = .01;
  public static final double MOVE_K = .01;
  public static final double MAX_DRIVE = .3;

  public VisionDrive() {
    requires(drive);
    requires(limelight);
  }

  protected void initialize() {

  }

  protected void execute() {
    LimelightData data = limelight.getData();
    double turnRequest;
    double moveRequest;
    double distance;

    if (data.valid()) {
      turnRequest = 0; // Utils.map(data.xCoord(), -Limelight.RANGE_X, Limelight.RANGE_X, -1.0, 1.0);
      moveRequest = 0; // Utils.map(data.dist(), -Limelight.RANGE_Y, Limelight.RANGE_Y, -1.0, 1.0);
      distance = data.dist();
      met = false; // data.targetMet();

    } else {
      turnRequest = 0;
      moveRequest = 0;
      distance = 0;
      met = false;
    }
    System.out.format("Target Distance:%.02f\n", distance);
    drive.arcadeDrive(moveRequest, turnRequest, MAX_DRIVE);
  }

  @Override
  protected boolean isFinished() {
    return met;
  }

  public void end() {
    drive.stop();
  }

  public void interrupted() {
    end();
  }

}