package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Swerve;

public class ResetGyro extends CommandBase {
	
	private final Swerve swerveDrive;
	private boolean finished = false;
	
	public ResetGyro (Swerve swerveDrive) {
		this.swerveDrive = swerveDrive;
		addRequirements(swerveDrive);
	}
	
	@Override
	public void initialize () {
		swerveDrive.stop();
		System.out.println("ZEROING GYRO");
		swerveDrive.resetGyro();
		System.out.println("GYRO ZEROED");
		finished = true;
	}
	
	@Override
	public void execute () { }
	
	@Override
	public void end (boolean interrupted) {
		swerveDrive.stop();
	}
	
	@Override
	public boolean isFinished () {
		return finished;
	}
	
}