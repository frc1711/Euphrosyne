package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Shooter;

public class AutoShooter extends CommandBase {
	
	private final Shooter shooter;
	private final double duration, speed;
	private final Timer timer;
	
	public AutoShooter (Shooter shooter, double duration, double speed) {
		this.shooter = shooter;
		this.duration = duration;
		this.speed = speed;
		
		timer = new Timer();
		addRequirements(shooter);
	}
	
	@Override
	public void initialize () {
		timer.start();
		shooter.stop();
	}
	
	@Override
	public void execute () {
		shooter.setSpeed(speed);
	}
	
	@Override
	public void end (boolean interrupted) {
		shooter.stop();
	}
	
	@Override
	public boolean isFinished () {
		return timer.get() >= duration;
	}
}