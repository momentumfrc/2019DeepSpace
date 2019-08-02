/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.DriveSubsystem;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class FollowPath extends Command {

  private static final double POWER_LIMIT = 0.4;

  private static final double TRAJECTORY_DT = 0.04; // seconds
  private static final double MAX_PLANNED_ACCELERATION = 2.0; // feet per second ^ 2
  private static final double MAX_PLANNED_JERK = 60.0; // feet per second ^ 3

  // TODO: Tune these values
  private static final double MAX_ROBOT_VELOCITY = 12.0; // feet per second
  private static final double ROBOT_WHEELBASE_WIDTH = 1.88541667; // feet

  private static final double KP = 1.0;
  private static final double KD = 0.0;
  private static final double K_ACCELERATION = 0;

  // This is the kP multiplied by the heading error to turn the robot
  // Not sure why it's broken up like this, but it's what's in the wiki
  private static final double K_TURN = 0.30 * (-1.0 / 80.0);

  private Encoder left_encoder = RobotMap.leftDriveEncoder;
  private Encoder right_encoder = RobotMap.rightDriveEncoder;
  private AHRS navx = RobotMap.navx;

  private DriveSubsystem drive = Robot.driveSystem;

  private double left_initial_distance, right_initial_distance;
  private double gyro_initial_heading;

  private Waypoint[] points;
  private double max_planned_velocity;
  private Trajectory trajectory;
  private TankModifier modifier;
  private DistanceFollower left_follower, right_follower;

  /**
   * Sets up the robot to follow the given path
   * 
   * @param points       Waypoints to interpolate to generate the path. The robot
   *                     is assumed to already be at the first waypoint
   * @param max_velocity The maximum velocity with which the robot should move
   */
  public FollowPath(Waypoint[] points, double max_velocity) {
    requires(Robot.driveSystem);
    this.points = points;
    max_planned_velocity = max_velocity;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST,
        TRAJECTORY_DT, max_planned_velocity, MAX_PLANNED_ACCELERATION, MAX_PLANNED_JERK);
    trajectory = Pathfinder.generate(points, config);
    modifier = new TankModifier(trajectory).modify(ROBOT_WHEELBASE_WIDTH);
    left_follower = new DistanceFollower(modifier.getLeftTrajectory());
    right_follower = new DistanceFollower(modifier.getRightTrajectory());

    left_follower.configurePIDVA(KP, 0.0, KD, 1 / MAX_ROBOT_VELOCITY, K_ACCELERATION);
    right_follower.configurePIDVA(KP, 0.0, KD, 1 / MAX_ROBOT_VELOCITY, K_ACCELERATION);

    left_initial_distance = left_encoder.getDistance();
    right_initial_distance = right_encoder.getDistance();
    gyro_initial_heading = navx.getAngle();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double left_value = left_follower.calculate(left_encoder.getDistance() - left_initial_distance);
    double right_value = right_follower.calculate(right_encoder.getDistance() - right_initial_distance);

    // According to the wiki, the above "doesn't account for heading of your robot,
    // meaning it won't track a curved path." So to fix this, we need to "use your
    // Gyroscope and the desired heading of the robot to create a simple,
    // proportional gain that will turn your tracks"

    // Heading is relative to the robot heading when the path following started
    double gyro_heading = (navx.getAngle() - gyro_initial_heading) % 360;

    // This could use either the left_follower or the right_follower. Since the left
    // and right sides are always parallel, they always have the same desired
    // heading
    double desired_heading = Pathfinder.r2d(left_follower.getHeading());
    double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);

    double turn = K_TURN * angleDifference;

    drive.tankDriveNoPID(left_value + turn, right_value - turn, POWER_LIMIT);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
