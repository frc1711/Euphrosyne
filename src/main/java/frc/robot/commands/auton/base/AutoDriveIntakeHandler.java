package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.Dashboard;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;

/**
 * Runs the swerve drive, intake, and cargo handler at the same time.
 */
public class AutoDriveIntakeHandler extends ParallelRaceGroup {
	
	private final AutoDrive autoDrive;
	private final AutoIntake autoIntake;
	private final AutoCargoHandler autoCargoHandler;
	
	public AutoDriveIntakeHandler (Swerve swerveDrive, Intake intake, CargoHandler cargoHandler, double distance, double speedMult) {
		autoDrive = new AutoDrive(swerveDrive, distance, speedMult);
		autoIntake = new AutoIntake(intake, Double.POSITIVE_INFINITY, Dashboard.INTAKE_MAX_SPEED.get());
		autoCargoHandler = new AutoCargoHandler(cargoHandler, Double.POSITIVE_INFINITY, Dashboard.CARGO_HANDLER_SPEED.get());
		
		addCommands(
			autoDrive,
			autoIntake,
			autoCargoHandler);
	}
	
}