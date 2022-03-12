package frc.robot.commands.auton.base;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.AutonDrive;
import frc.team1711.swerve.commands.FrameOfReference;

/**
 * Drives in the direction of the intake, or in the direction of the shooter for a negative direction
 */
public class AutoDrive extends AutonDrive {
	
	private final OnCommandEnd onCommandEnd;
	private final Swerve swerveDrive;
	
	public AutoDrive (Swerve swerveDrive, double distance, double speedMult) {
		this(swerveDrive, distance, speedMult, x -> {});
	}
	
	public AutoDrive (Swerve swerveDrive, double distance, double speedMult, OnCommandEnd onCommandEnd) {
		super(swerveDrive, distance < 0 ? 180 : 0, Math.abs(distance), 0.1 * speedMult, 5, 0.01, FrameOfReference.ROBOT);
		this.swerveDrive = swerveDrive;
		this.onCommandEnd = onCommandEnd;
	}
	
	@Override
	public void execute () {
		super.execute();
	}
	
	@Override
	public void end (boolean interrupted) {
		this.onCommandEnd.onCommandEnd(swerveDrive.getDistanceTraveled());
		super.end(interrupted);
	}
	
	@FunctionalInterface
	public interface OnCommandEnd {
		public void onCommandEnd (double distanceTraveled);
	}
	
}