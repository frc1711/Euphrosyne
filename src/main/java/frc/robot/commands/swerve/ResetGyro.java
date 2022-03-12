package frc.robot.commands.swerve;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Swerve;

public class ResetGyro extends InstantCommand {
	
	public ResetGyro (Swerve swerveDrive) {
		super(swerveDrive::resetGyro, swerveDrive);
	}
	
}