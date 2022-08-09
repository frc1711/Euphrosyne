package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Dashboard;
import frc.robot.subsystems.HoodedShooter;

public class AutoShooter extends CommandBase {
	
	private final HoodedShooter shooter;
	private final double duration;
	private final Timer timer;
	
	public AutoShooter (HoodedShooter shooter, double duration, double speed) {
		this.shooter = shooter;
		this.duration = duration;
		
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
		shooter.setSpeed(Dashboard.HOODED_SHOOTER_UPPER_SPEED.get(), Dashboard.HOODED_SHOOTER_LOWER_SPEED.get());
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