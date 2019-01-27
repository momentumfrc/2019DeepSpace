package frc.robot.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class BooleanTrigger extends Trigger {
	
	private BooleanTriggerInterface trigger;
	
	public BooleanTrigger(BooleanTriggerInterface trigger) {
		this.trigger = trigger;
	}
	

	@Override
	public boolean get() {
		return trigger.get();
	}
}