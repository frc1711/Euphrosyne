package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;

/**
 * Drives in the direction of intake, running the intake and cargo handler, stopping once the cargo handler
 * senses there's a cargo ball or once the maximum time is reached.
 */
public class AutoIntakeDriveCollect extends ParallelCommandGroup {
	
	private final AutoDrive autoDrive;
	private final AutoIntake autoIntake;
	private final AutoSensitiveCargoHandler autoSensitiveCargoHandler;
	
	private final Timer timer = new Timer();
	private final double maxTime;
	
	private boolean gotCargo = false;
	
	public AutoIntakeDriveCollect (Swerve swerveDrive, CargoHandler cargoHandler, Intake intake, double maxTime) {
		autoDrive = new AutoDrive(swerveDrive, Double.POSITIVE_INFINITY);
		autoIntake = new AutoIntake(intake, Double.POSITIVE_INFINITY, -0.7);
		autoSensitiveCargoHandler = new AutoSensitiveCargoHandler(cargoHandler, Double.POSITIVE_INFINITY, -0.5, () -> gotCargo = true);
		
		this.maxTime = maxTime;
		addCommands(
			autoDrive, // Drives in direction of intake until stopped
			autoIntake, // Runs intake
			autoSensitiveCargoHandler); // Runs cargo handler, waiting for it to sense cargo
	}
	
	@Override
	public void initialize () {
		timer.start();
		super.initialize();
	}
	
	@Override
	public void end (boolean interrupted) {
		autoDrive.end(interrupted);
		autoIntake.end(interrupted);
		autoSensitiveCargoHandler.end(interrupted);
	}
	
	@Override
	public boolean isFinished () {
		// Finishes if either the cargo is retrieved or if the max time is exceeded
		return gotCargo || timer.get() >= maxTime;
	}
	
}