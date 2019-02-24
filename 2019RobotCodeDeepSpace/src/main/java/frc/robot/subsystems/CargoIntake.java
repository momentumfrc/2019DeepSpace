package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.cargointake.StopIntake;
import frc.robot.utils.MoPDP;

public class CargoIntake extends Subsystem {
  private SpeedController tRoller = RobotMap.intakeMotorTop;
  private SpeedController bRoller = RobotMap.intakeMotorBottom;
  private MoPDP pdp = RobotMap.pdp;

  private static final double INTAKE_SPEED = -1;
  private static final double HOLD_SPEED = -.01;
  private static final double SHOOT_SPEED = 1;

  private static final double GRABBED_CURRENT = 7.5; // Current threshold to know when a ball is stalling the intake
  private static final int CURRENT_CUTOFF_TIME = 300; // Time in ms

  private static final int[] intakePDPChannels = { RobotMap.INTAKE_UPPER_PDP, RobotMap.INTAKE_LOWER_PDP };

  private void intakeCargo(double intakeSpeed) {
    tRoller.set(intakeSpeed);
    bRoller.set(intakeSpeed);
  }

  public void stop() {
    intakeCargo(0);
  }

  public void grab() {
    intakeCargo(INTAKE_SPEED);
    pdp.setOvercurrentThresholds(intakePDPChannels, GRABBED_CURRENT);
  }

  public void hold() {
    intakeCargo(HOLD_SPEED);
  }

  public boolean checkHeld() {
    return pdp.checkOvercurrent(intakePDPChannels, CURRENT_CUTOFF_TIME);
  }

  public void shoot() {
    intakeCargo(SHOOT_SPEED);
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new StopIntake());
  }

}