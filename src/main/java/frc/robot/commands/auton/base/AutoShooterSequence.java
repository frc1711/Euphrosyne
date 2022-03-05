package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Shooter;

import java.util.function.BooleanSupplier;

public class AutoShooterSequence extends SequentialCommandGroup {
	
	private static final double cargoHandlerSpeed = -0.5;
	private static final double shooterSpeed = -0.7;
	
	private final Shooter shooter;
	private final CargoHandler cargoHandler;
	private final BooleanSupplier stopCommand;
	
	/**
	 * Creates the shooter sequence, where all cargo balls are prepared and shot, stopping once the command is finished.
	 * @param shooter
	 * @param cargoHandler
	 * @param shooterRunLength
	 */
	public AutoShooterSequence (Shooter shooter, CargoHandler cargoHandler, double shooterRunLength, boolean runInitialReverse) {
		this(shooter, cargoHandler, shooterRunLength, () -> false, runInitialReverse);
	}
	
	/**
	 * Creates the shooter sequence, where all cargo balls are prepared and shot (with the shooter and cargo handler running
	 * until {@code stopCommand} first returns {@code true}).
	 * @param shooter
	 * @param cargoHandler
	 * @param stopCommand
	 */
	public AutoShooterSequence (Shooter shooter, CargoHandler cargoHandler, BooleanSupplier stopCommand, boolean runInitialReverse) {
		this(shooter, cargoHandler, Double.POSITIVE_INFINITY, stopCommand, runInitialReverse);
	}
	
	private AutoShooterSequence (
			Shooter shooter,
			CargoHandler cargoHandler,
			double shooterRunLength,
			BooleanSupplier stopCommand,
			boolean runInitialReverse) {
		super(
			new AutoCargoHandler(cargoHandler, runInitialReverse ? 0.24 : 0, -cargoHandlerSpeed),	// Pull back to avoid hitting shooter
			new ParallelCommandGroup(
				new AutoShooter(shooter, shooterRunLength + 0.2, shooterSpeed),						// Run shooter (will run until stopped)
				new SequentialCommandGroup(
					new AutoCargoHandler(cargoHandler, 0.2, 0),										// Wait before running cargo handler
					new AutoCargoHandler(cargoHandler, shooterRunLength, cargoHandlerSpeed)			// Run cargo handler
				)
			)
		);
		
		this.shooter = shooter;
		this.cargoHandler = cargoHandler;
		this.stopCommand = stopCommand;
	}
	
	@Override
	public void end (boolean interrupted) {
		shooter.stop();
		cargoHandler.stop();
		super.end(interrupted);
	}
	
	@Override
	public boolean isFinished () {
		if (stopCommand.getAsBoolean()) return true;
		return super.isFinished();
	}
	
}