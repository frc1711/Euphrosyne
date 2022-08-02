package frc.robot.commands.central;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.HoodedShooter;

public class HoodedShooterTest extends CommandBase {
	
	private final HoodedShooter hoodedShooter;
	
	private final BooleanSupplier shoot;
	private final DoubleSupplier upperSpeed, lowerSpeed;
	
	public HoodedShooterTest (
		HoodedShooter hoodedShooter,
		BooleanSupplier shoot,
		DoubleSupplier upperSpeed,
		DoubleSupplier lowerSpeed) {
		
		this.hoodedShooter = hoodedShooter;
		this.shoot = shoot;
		this.upperSpeed = upperSpeed;
		this.lowerSpeed = lowerSpeed;
		
		addRequirements(hoodedShooter);
	}
	
	@Override
	public void initialize () {
		hoodedShooter.stop();
	}
	
	@Override
	public void execute () {
		double upperSpeedVal = shoot.getAsBoolean() ? upperSpeed.getAsDouble() : 0;
		double lowerSpeedVal = shoot.getAsBoolean() ? lowerSpeed.getAsDouble() : 0;
		hoodedShooter.setSpeed(upperSpeedVal, lowerSpeedVal);
	}
	
	@Override
	public void end (boolean interrupted) {
		hoodedShooter.stop();
	}
	
	@Override
	public boolean isFinished () {
		return false;
	}
	
}