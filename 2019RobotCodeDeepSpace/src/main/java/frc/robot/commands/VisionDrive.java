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
  private LimelightData data;

  public static final double RANGE_X = 29.8; // The range of values given by "tx" is from -29.8 to 29.8 degrees
  public static final double RANGE_Y = 24.85; // The range of values given by "ty" is from -24.85 to 24.85 degrees

  public double turnErr;
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
    limelight.getData();
    turnErr = data.xCoord();
    dist = data.dist();

    double turnRequest = Utils.map(turnErr, -RANGE_X, RANGE_X, -1.0, 1.0);
    double moveRequest = Utils.map(dist, -RANGE_Y, RANGE_Y, -1.0, 1.0);

    drive.arcadeDrive(moveRequest, turnRequest, MAX_DRIVE);
  }

  @Override
  protected boolean isFinished() {
    return data.targetMet();
  }

  public void end() {
    drive.stop();
  }

  public void interrupted() {
    end();
  }

}