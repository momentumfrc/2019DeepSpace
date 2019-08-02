/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.choosers.ControlChooser;
import frc.robot.choosers.LEDAnimationChooser;
import frc.robot.choosers.SandstormChooser;
import frc.robot.commands.*;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HatchFlipper;
import frc.robot.subsystems.HatchPassive;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.vision.Limelight.LimelightData;
import frc.robot.subsystems.CargoIntake;
import frc.robot.subsystems.Wrist;
import frc.robot.utils.MoPerfMon;
import frc.robot.utils.MoPrefs;
import frc.robot.utils.NeoPixels;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;
import frc.robot.subsystems.Arm;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static final MoPerfMon perfMon = new MoPerfMon();

  public static ControlChooser controlChooser;
  public static DriveSubsystem driveSystem;
  public static Command visionDrive;
  public static Command pathFollow;
  public static Arm arm;
  public static Wrist wrist;
  public static CargoIntake cargoIntake;
  public static HatchFlipper hatchActive;
  public static HatchPassive hatchPassive;
  public static Limelight limelight;
  public static LimelightData limelightData;
  // public static TestMax testMax;
  public static OI m_oi;

  public static NeoPixels neoPixels;
  public static LEDAnimationChooser animationChooser;

  private SandstormChooser sandstormChooser;

  private Command driveCommand;
  private Command armCommand;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    MoPrefs.safeForPrefs();

    controlChooser = new ControlChooser();
    driveSystem = new DriveSubsystem();
    arm = new Arm();
    wrist = new Wrist();
    cargoIntake = new CargoIntake();
    hatchActive = new HatchFlipper();
    hatchPassive = new HatchPassive();
    m_oi = new OI();
    sandstormChooser = new SandstormChooser();
    driveCommand = new DriveCommand();
    armCommand = new ArmPositioning();
    limelight = new Limelight();

    visionDrive = new VisionDrive();
    pathFollow = new FollowPath(
        new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(4, 0, 0), new Waypoint(4.943, 0.943, Pathfinder.d2r(90)),
            new Waypoint(4, 1.886, Pathfinder.d2r(180)), new Waypoint(0, 0, Pathfinder.d2r(180)) },
        1);

    neoPixels = new NeoPixels();
    animationChooser = new LEDAnimationChooser();

    UsbCamera lifecam = CameraServer.getInstance().startAutomaticCapture();
    lifecam.setResolution(320, 240);
    lifecam.setFPS(15);
    RobotMap.matchTab.add("Drive Camera", lifecam).withPosition(3, 0).withSize(3, 3)
        .withProperties(Map.of("Show controls", false));

    // testMax = new TestMax();
    // testMax.init();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want run during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    animationChooser.poll();
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You
   * can use it to reset any subsystem information you want to clear when the
   * robot is disabled.
   */
  @Override
  public void disabledInit() {
    perfMon.stop();
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
    // perfMon.start();
    try (MoPerfMon.Period period = perfMon.newPeriod("autonomousInit")) {
      Scheduler.getInstance().removeAll();

      switch (sandstormChooser.getSelected()) {
      case VISION_DRIVE:
        visionDrive.start();
        break;
      case PATH_FOLLOW:
        pathFollow.start();
        break;
      case TELEOP:
      default:
        driveCommand.start();
        armCommand.start();
      }
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    try (MoPerfMon.Period period = perfMon.newPeriod("autonomousPeriodic")) {
      Scheduler.getInstance().run();
    }
  }

  @Override
  public void teleopInit() {
    // perfMon.start();
    try (MoPerfMon.Period period = perfMon.newPeriod("teleopInit")) {
      Scheduler.getInstance().removeAll();
      driveCommand.start();
      armCommand.start();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    try (MoPerfMon.Period period = perfMon.newPeriod("teleopPeriodic")) {
      Scheduler.getInstance().run();
      // arm.value_display.update();
      // wrist.value_display.update();
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

    System.out.format("Yaw: %.2f LDist:%.2f RDist:%.2f\n", RobotMap.navx.getAngle(),
        RobotMap.leftDriveEncoder.getDistance(), RobotMap.rightDriveEncoder.getDistance());

    // testMax.periodic();
    /*
     * boolean kick = controlChooser.getSelected().getKick();
     * hatchPassive.setKick(kick); SmartDashboard.putBoolean("kick", kick);
     */
    // arm.value_display.update();
    // wrist.value_display.update();
  }
}
