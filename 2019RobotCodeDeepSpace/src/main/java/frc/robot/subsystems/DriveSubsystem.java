/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import frc.robot.commands.DriveCommand;
import frc.robot.RobotMap;
import frc.robot.utils.MoPID;
import frc.robot.utils.MoPrefs;
import frc.robot.utils.Utils;

public class DriveSubsystem extends Subsystem {

  private SpeedControllerGroup leftside = new SpeedControllerGroup(RobotMap.leftFrontMotor, RobotMap.leftBackMotor);
  private SpeedControllerGroup rightside = new SpeedControllerGroup(RobotMap.rightFrontMotor, RobotMap.rightBackMotor);

  private Encoder leftEnc = RobotMap.leftDriveEncoder;
  private Encoder rightEnc = RobotMap.rightDriveEncoder;

  private DifferentialDrive drive;

  private final MoPID movePID, turnPID;
  private final NetworkTableEntry pidWidget;
  private boolean pidEnabled = true;

  public DriveSubsystem() {
    super("Drive Subsytem");
    drive = new DifferentialDrive(leftside, rightside);
    drive.setDeadband(0);
    addChild("Left Side", leftside);
    addChild("Right Side", rightside);

    addChild("Left Encoder", leftEnc);
    addChild("Right Encoder", rightEnc);
    leftEnc.setDistancePerPulse(1.0 / MoPrefs.getDriveEncTicksPerFoot());
    rightEnc.setDistancePerPulse(1.0 / MoPrefs.getDriveEncTicksPerFoot());

    movePID = MoPID.makePIDFromPrefs(RobotMap.testTab, "MoveRatePID", 0, 0);
    turnPID = MoPID.makePIDFromPrefs(RobotMap.testTab, "TurnRatePID", 4, 0);

    pidWidget = RobotMap.matchTab.add("Drive PID Enabled", pidEnabled).withPosition(8, 0)
        .withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    pidWidget.addListener(notice -> {
      pidEnabled = notice.value.getBoolean();
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveCommand());
  }

  public void arcadeDrive(double moveRequest, double turnRequest, double speedLimiter) {
    // Scale requests to speedLimit
    moveRequest *= speedLimiter;
    turnRequest *= speedLimiter;

    // Scale getMoveRate to match moveRequest. Likewise for turn.
    double moveRate = getMoveRate() / 12.0; // TODO measure actual max rate
    double turnRate = getTurnRate() / 12.0; // TODO measure actual max rate

    // Calculate drive
    double move = pidEnabled ? movePID.calculate(moveRequest, moveRate) : moveRequest;
    double turn = pidEnabled ? turnPID.calculate(turnRequest, turnRate) : turnRequest;

    double m_r = Utils.clip(move, -speedLimiter, speedLimiter);
    double t_r = Utils.clip(turn, -speedLimiter, speedLimiter);
    drive.arcadeDrive(m_r, t_r, false);
  }

  public void resetEncoders() {
    leftEnc.reset();
    rightEnc.reset();
  }

  public double getMoveRate() {
    return (rightEnc.getRate() + leftEnc.getRate()) / 2.0;
  }

  public double getTurnRate() {
    return (leftEnc.getRate() - rightEnc.getRate());
  }

  public void stop() {
    arcadeDrive(0, 0, 0);
  }
}
