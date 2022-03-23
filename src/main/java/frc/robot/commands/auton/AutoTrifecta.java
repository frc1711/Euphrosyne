package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoDrive;
import frc.robot.commands.auton.base.AutoDriveIntakeHandler;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.commands.auton.base.AutoTurn;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class AutoTrifecta extends SequentialCommandGroup {
	
	public AutoTrifecta (Swerve swerveDrive, Shooter shooter, Intake intake, CargoHandler cargoHandler) {
		super(
			// Shoot initial ball
			new AutoShooterSequence(shooter, cargoHandler, 0.5),
			
			// Get first ball
			new AutoDrive(swerveDrive, 300, 3),
			new AutoTurn(swerveDrive, -28, 2.5),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, 215, 3),
			
			// Get second ball
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -215, 3),
			new AutoTurn(swerveDrive, 100, 2.5),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, 450, 3),
			
			// Go back
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -450, 3),
			new AutoTurn(swerveDrive, -67, 2.5),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -300, 3),
			
			// Shoot
			new AutoShooterSequence(shooter, cargoHandler, 1.5));
	}
	
}