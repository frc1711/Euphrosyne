package frc.robot.commands.auton.base;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.AutonDrive;
import frc.team1711.swerve.commands.FrameOfReference;

/**
 * Drives in the direction of the intake
 */
public class AutoDrive extends AutonDrive {
	
	public AutoDrive (Swerve swerveDrive, double distance) {
		super(swerveDrive, 0, distance, 0.1, 5, 0.01, FrameOfReference.ROBOT);
	}
	
}