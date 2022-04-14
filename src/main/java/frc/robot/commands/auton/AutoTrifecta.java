package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoDrive;
import frc.robot.commands.auton.base.AutoDriveIntakeHandler;
import frc.robot.commands.auton.base.AutoIntake;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.commands.auton.base.AutoTurn;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.util.Angles;
import frc.robot.Dashboard;

public class AutoTrifecta extends SequentialCommandGroup {
	
	private final double startDir;
	
	public AutoTrifecta (Swerve swerveDrive, Shooter shooter, Intake intake, CargoHandler cargoHandler) {
		startDir = swerveDrive.getGyroAngle();
		
		addCommands(
			// Shoot initial ball
			new AutoShooterSequence(shooter, cargoHandler, 0.5),
			
			// Deploy the intake
			new AutoIntake(intake, 0.5, Dashboard.INTAKE_MAX_SPEED.get()),
			
			// Get first balldelpo
			new AutoDrive(swerveDrive, 350, 3),
			autoTurn(swerveDrive, -20),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, 215, 3),
			
			// Get second ball
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -215, 3),
			autoTurn(swerveDrive, 61),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, 515, 3),
			
			// Go back
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -500, 3),
			autoTurn(swerveDrive, 4),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -300, 3),
			
			// Shoot
			new AutoShooterSequence(shooter, cargoHandler, 1.5));
	}
	
	private AutoTurn autoTurn (Swerve swerveDrive, double startDirOffset) {
		return new AutoTurn(swerveDrive, Angles.wrapDegreesZeroCenter(startDirOffset + startDir), 2.5, true);
	}
	
}