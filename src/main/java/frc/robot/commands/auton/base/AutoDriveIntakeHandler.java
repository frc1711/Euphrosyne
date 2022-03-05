package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;

/**
 * Runs the swerve drive, intake, and cargo handler at the same time.
 */
public class AutoDriveIntakeHandler extends ParallelCommandGroup {
	
	private final AutoDrive autoDrive;
	private final AutoIntake autoIntake;
	private final AutoCargoHandler autoCargoHandler;
	
	private boolean finished = false;
	
	public AutoDriveIntakeHandler (Swerve swerveDrive, Intake intake, CargoHandler cargoHandler, double distance) {
		autoDrive = new AutoDrive(swerveDrive, distance, d -> finished = true);
		autoIntake = new AutoIntake(intake, Double.POSITIVE_INFINITY, -0.7);
		autoCargoHandler = new AutoCargoHandler(cargoHandler, Double.POSITIVE_INFINITY, -0.5);
		
		addCommands(
			autoDrive,
			autoIntake,
			autoCargoHandler);
	}
	
	@Override
	public void end (boolean interrupted) {
		autoIntake.end(interrupted);
		autoCargoHandler.end(interrupted);
	}
	
	@Override
	public boolean isFinished () {
		return finished;
	}
	
}