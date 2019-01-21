package frc.robot.choosers;

import frc.robot.controllers.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class ControlChooser extends SendableChooser<DriveController> {
    private final String NAME = "CONTROL CHOOSER";

    public ControlChooser(){
        super();
        addDefault("Xbox Main, F310 Secondary", new XboxF310Wrapper());

        SmartDashboard.putData(NAME,this);
    }

    
}