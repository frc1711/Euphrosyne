package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.central.AutoShooterSequence;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.AutonDrive;
import frc.team1711.swerve.commands.FrameOfReference;

public class AutoShootAndDriveBack extends SequentialCommandGroup {
	
	public AutoShootAndDriveBack (Swerve swerveDrive, Shooter shooter, CargoHandler cargoHandler) {
		super(
			new AutoShooterSequence(shooter, cargoHandler, 1.5),
			new AutonDrive(swerveDrive, 0, 200, 0.1, 5, 0.01, FrameOfReference.ROBOT));
	}
	
}