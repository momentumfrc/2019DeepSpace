package frc.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.intake.StopIntake;
import frc.robot.utils.PDPWrapper;

public class Intake extends Subsystem {
    private VictorSP tRoller = RobotMap.intakeMotorTop;
    private VictorSP bRoller = RobotMap.intakeMotorBottom;
    private PDPWrapper currentCheck = new PDPWrapper();

    private static final double INTAKE_SPEED = -1;
    private static final double HOLD_SPEED = -.01;
    private static final double SHOOT_SPEED = 1;

    private static final double HOLD_CURRENT = 10; // How many amps to hold the ball
    private static final int CURRENT_CUTOFF_TIME = 500; // Time in ms

    public void intakeCargo(double intakeSpeed) {
        tRoller.set(intakeSpeed);
        bRoller.set(intakeSpeed);
    }

    public void stop() {
        intakeCargo(0);
    }

    public void grab() {
        System.out.format("LC: %.2f RC:%.2f LS:%.2f RS:%.2f\n", RobotMap.pdp.getCurrent(RobotMap.INTAKE_UPPER_PDP),
                RobotMap.pdp.getCurrent(RobotMap.INTAKE_LOWER_PDP), tRoller.get(), bRoller.get());
        intakeCargo(INTAKE_SPEED);
    }

    public void hold() {
        intakeCargo(HOLD_SPEED);
    }

    public void hunt() { //TODO Clarify if this is even needed
        intakeCargo(INTAKE_SPEED);
    }

    public boolean checkHeld() {
        return !currentCheck.checkOvercurrent(new int[] { RobotMap.INTAKE_UPPER_PDP, RobotMap.INTAKE_LOWER_PDP },
                HOLD_CURRENT, CURRENT_CUTOFF_TIME);
    }
    
    public void shoot() {
        intakeCargo(SHOOT_SPEED);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new StopIntake());
    }

}