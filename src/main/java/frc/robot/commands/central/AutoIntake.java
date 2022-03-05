package frc.robot.commands.central;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;

public class AutoIntake extends CommandBase {
	
	private final Intake intake;
	private final double duration, speed;
	private final Timer timer;
	
	public AutoIntake (Intake intake, double duration, double speed) {
		this.intake = intake;
		this.duration = duration;
		this.speed = speed;
		
		timer = new Timer();
		addRequirements(intake);
	}
	
	@Override
	public void initialize () {
		timer.start();
		intake.stop();
	}
	
	@Override
	public void execute () {
		intake.setSpeed(speed);
	}
	
	@Override
	public void end (boolean interrupted) {
		intake.stop();
	}
	
	@Override
	public boolean isFinished () {
		return timer.get() >= duration;
	}
	
}