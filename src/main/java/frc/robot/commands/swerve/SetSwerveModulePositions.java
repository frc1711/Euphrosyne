package frc.robot.commands.swerve;

import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.Swerve;

public class SetSwerveModulePositions extends InstantCommand {
	
	public SetSwerveModulePositions (Swerve swerveDrive) {
		super(swerveDrive::configDirectionEncoders, swerveDrive);
	}
	
}