package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.Dashboard;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;

import java.util.function.BooleanSupplier;

public class AutoShooterSequence extends SequentialCommandGroup {
	
	private final HoodedShooter shooter;
	private final CargoHandler cargoHandler;
	private final BooleanSupplier stopCommand;
	
	/**
	 * Creates the shooter sequence, where all cargo balls are prepared and shot, stopping once the command is finished.
	 * @param shooter
	 * @param cargoHandler
	 * @param shooterRunLength
	 */
	public AutoShooterSequence (HoodedShooter shooter, CargoHandler cargoHandler, double shooterRunLength) {
		this(shooter, cargoHandler, shooterRunLength, () -> false);
	}
	
	/**
	 * Creates the shooter sequence, where all cargo balls are prepared and shot (with the shooter and cargo handler running
	 * until {@code stopCommand} first returns {@code true}).
	 * @param shooter
	 * @param cargoHandler
	 * @param stopCommand
	 */
	public AutoShooterSequence (HoodedShooter shooter, CargoHandler cargoHandler, BooleanSupplier stopCommand) {
		this(shooter, cargoHandler, Double.POSITIVE_INFINITY, stopCommand);
	}
	
	private AutoShooterSequence (
			HoodedShooter shooter,
			CargoHandler cargoHandler,
			double shooterRunLength,
			BooleanSupplier stopCommand) {
		super(
			// Intake until sensor is tripped (ball at top of pulley)
			new AutoCargoHandler(cargoHandler, Dashboard.CARGO_HANDLER_SPEED.get(), sensorTripped -> sensorTripped),
			
			// Intake backwards until sensor is not tripped (ball just below the kicker so the shooter can get up to speed)
            new AutoCargoHandler(cargoHandler, -Dashboard.CARGO_HANDLER_SPEED.get(), sensorTripped -> !sensorTripped),
			
			new ParallelRaceGroup(
				// Run shooter (will run until stopped)
				new AutoShooter(shooter, Double.POSITIVE_INFINITY, Dashboard.SHOOTER_MAX_SPEED.get()),
				new SequentialCommandGroup(
					// Wait before running cargo handler
					new AutoCargoHandler(cargoHandler, 0.2, 0),
					// Run cargo handler
					new AutoCargoHandler(cargoHandler, shooterRunLength, Dashboard.CARGO_HANDLER_SPEED.get())
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