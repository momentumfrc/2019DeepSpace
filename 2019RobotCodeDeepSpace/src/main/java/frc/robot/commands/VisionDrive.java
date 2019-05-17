package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Limelight;

public class VisionDrive extends Command {
  private DriveSubsystem drive = Robot.driveSystem;
  private Limelight limelight = Robot.limelight;

  public double[] posData;
  public double posX;
  public double posY;
  public double dist;
  public double limelightTurn;
  public double limelightMove;

  // TODO Tune these values
  public static final double TURN_K = .01;
  public static final double MOVE_K = .01;
  public static final double MAX_DRIVE = .8;

  public VisionDrive() {
    requires(drive);
    requires(limelight);
  }

  protected void initialize() {

  }

  protected void execute() {
    posData = limelight.targetPos();
    posX = posData[0];
    posY = posData[1];
    dist = posData[2];

    double turnRequest = posX * TURN_K;
    double moveRequest = dist * MOVE_K;

    drive.arcadeDrive(moveRequest, turnRequest, MAX_DRIVE);
  }

  @Override
  protected boolean isFinished() {
    return limelight.atTarget();
  }

  public void end() {
    drive.stop();
  }

  public void interrupted() {
    end();
  }

}