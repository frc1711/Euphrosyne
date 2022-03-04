package frc.robot.commands.auton;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.AutonDrive;
import frc.team1711.swerve.commands.FrameOfReference;

public class AutoTaxi extends AutonDrive {
	
	public AutoTaxi (Swerve swerveDrive) {
		super(swerveDrive, 0, 200, 0.1, 5, 0.01, FrameOfReference.ROBOT);
	}
	
}