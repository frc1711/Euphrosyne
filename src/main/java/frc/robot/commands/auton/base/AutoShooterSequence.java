package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.Dashboard;
import frc.robot.commands.auton.AutoVisionReset;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Swerve;
import frc.robot.util.ShooterSpeedsLookup;
import frc.robot.util.VisionHandler;
import frc.robot.util.ShooterSpeedsLookup.ShooterSpeed;

import java.util.function.BooleanSupplier;

public class AutoShooterSequence extends SequentialCommandGroup {
	
	private final HoodedShooter shooter;
	private final CargoHandler cargoHandler;
	private final BooleanSupplier stopCommand;
	
	/**
	 * Creates the shooter sequence, where all cargo balls are prepared and shot, stopping once the command is finished.
     * @param swerve
	 * @param shooter
	 * @param cargoHandler
	 * @param shooterRunLength
	 */
	public AutoShooterSequence (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler, double shooterRunLength) {
		this(swerve, shooter, cargoHandler, shooterRunLength, () -> false);
	}
	
	/**
	 * Creates the shooter sequence, where all cargo balls are prepared and shot (with the shooter and cargo handler running
	 * until {@code stopCommand} first returns {@code true}).
     * @param swerve
	 * @param shooter
	 * @param cargoHandler
	 * @param stopCommand
	 */
	public AutoShooterSequence (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler, BooleanSupplier stopCommand) {
		this(swerve, shooter, cargoHandler, Double.POSITIVE_INFINITY, stopCommand);
	}
	
	private AutoShooterSequence (
            Swerve swerve,
			HoodedShooter shooter,
			CargoHandler cargoHandler,
			double shooterRunLength,
			BooleanSupplier stopCommand) {
		super(
            // Manage index and orient robot to shoot:
            new ParallelCommandGroup(
                // Manage the ball indexer:
                new SequentialCommandGroup(
                    // Intake until sensor is tripped (ball at top of pulley)
                    new AutoCargoHandler(cargoHandler, Dashboard.CARGO_HANDLER_SPEED.get(), sensorTripped -> sensorTripped),
                    
                    // Intake backwards until sensor is not tripped (ball just below the kicker so the shooter can get up to speed)
                    new AutoCargoHandler(cargoHandler, -Dashboard.CARGO_HANDLER_SPEED.get(), sensorTripped -> !sensorTripped)
                ),
                // At the same time, orient the robot to shoot and get vision info:
                new AutoVisionReset(swerve)
            ),
			
            // Get shooter up to speed and shoot
			new ParallelRaceGroup(
				// Run shooter (will run until stopped)
				new AutoShooter(shooter, VisionHandler.getInstance().getDistanceFromHub()),
				new SequentialCommandGroup(
					// Wait before running cargo handler
					new AutoCargoHandler(cargoHandler, 0.5, 0),
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
    
    private static class AutoShooter extends CommandBase {
        
        private final HoodedShooter shooter;
        private final ShooterSpeed shooterSpeed;
        
        public AutoShooter (HoodedShooter shooter, double distanceFromHub) {
            this.shooter = shooter;
            this.shooterSpeed = ShooterSpeedsLookup.getShooterSpeed(distanceFromHub);
            addRequirements(shooter);
        }
        
        @Override
        public void initialize () {
            shooter.stop();
        }
        
        @Override
        public void execute () {
            shooter.setSpeed(shooterSpeed.upperSpeed, shooterSpeed.lowerSpeed);
        }
        
        @Override
        public void end (boolean interrupted) {
            shooter.stop();
        }
        
        @Override
        public boolean isFinished () {
            return false;
        }
        
    }
	
}