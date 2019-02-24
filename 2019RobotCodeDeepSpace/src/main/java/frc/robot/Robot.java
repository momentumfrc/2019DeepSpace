/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.choosers.ControlChooser;
import frc.robot.choosers.SandstormChooser;
import frc.robot.commands.*;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HatchActive;
import frc.robot.subsystems.HatchPassive;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Arm;
import frc.robot.sandstorm.SandstormMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static DriveSubsystem driveSystem = new DriveSubsystem();
  public static ControlChooser controlChooser = new ControlChooser();
  public static Arm arm = new Arm();
  public static Wrist wrist = new Wrist();
  public static CargoIntake cargoIntake = new CargoIntake();
  public static HatchActive hatchActive = new HatchActive();
  public static HatchPassive hatchPassive = new HatchPassive();
  public static TestMax testMax = new TestMax();
  public static OI m_oi;

  private SendableChooser<SandstormMode> sandstormChooser = new SandstormChooser();

  private Command driveCommand = new DriveNoPID();

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_oi = new OI();
    SmartDashboard.putData("Auto mode", sandstormChooser);
    SmartDashboard.putData("Control Chooser", controlChooser);

    VideoSource lifecam = new UsbCamera("Driver Camera", 0);
    // TODO: Experiment with these numbers
    lifecam.setResolution(640, 480);
    lifecam.setFPS(15);
    CameraServer.getInstance().startAutomaticCapture(lifecam);

    testMax.init();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You
   * can use it to reset any subsystem information you want to clear when the
   * robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString code to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons to
   * the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    Scheduler.getInstance().removeAll();

    switch (sandstormChooser.getSelected()) {
    case DEFAULT_PATH:
      // Do path stuff
      // break; // Commented so that it will just fall through to the teleop mode
    case TELEOP:
    default:
      driveCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    Scheduler.getInstance().removeAll();
    driveCommand.start();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    testMax.periodic();
  }
}
