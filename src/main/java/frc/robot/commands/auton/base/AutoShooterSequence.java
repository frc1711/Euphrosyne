package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.auton.AutoVisionReset;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Swerve;
import frc.robot.util.ShooterSpeedsLookup;
import frc.robot.util.VisionHandler;
import frc.robot.util.ShooterSpeedsLookup.ShooterSpeed;

import java.util.function.BooleanSupplier;

public class AutoShooterSequence extends SequentialCommandGroup {
	
    public static final double SHOOTER_RUN_LENGTH = 0.5;
    
    private final Swerve swerve;
	private final HoodedShooter shooter;
	private final CargoHandler cargoHandler;
	private final BooleanSupplier stopCommand;
    
    /**
     * Creates a {@link AutoShooterSequence} command where the robot aims for the high goal using vision and the shooter runs for
     * a set amount of time ({@link #SHOOTER_RUN_LENGTH}).
     * @param swerve        The {@link Swerve} drive
     * @param shooter       The {@link HoodedShooter}
     * @param cargoHandler  The {@link CargoHandler}
     */
	public AutoShooterSequence (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler) {
		this(swerve, shooter, cargoHandler, SHOOTER_RUN_LENGTH, () -> false, true);
	}
    
    /**
     * Creates a {@link AutoShooterSequence} command where the robot aims for the low goal and the shooter runs for
     * a set amount of time ({@link #SHOOTER_RUN_LENGTH}).
     * @param shooter       The {@link HoodedShooter}
     * @param cargoHandler  The {@link CargoHandler}
     */
    public AutoShooterSequence (HoodedShooter shooter, CargoHandler cargoHandler) {
        this(null, shooter, cargoHandler, SHOOTER_RUN_LENGTH, () -> false, false);
    }
	
    /**
     * Creates a {@link AutoShooterSequence} command where the robot aims for the high goal using vision and the shooter runs
     * until stopped by {@code stopCommand}.
     * @param swerve        The {@link Swerve} drive
     * @param shooter       The {@link HoodedShooter}
     * @param cargoHandler  The {@link CargoHandler}
     * @param stopCommand   A {@code BooleanSupplier} which stops the command (and could stop the command prematurely,
     * before the shooter even starts running). {@code stopCommand} should return {@code true} when the command is
     * supposed to stop.
     */
	public AutoShooterSequence (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler, BooleanSupplier stopCommand) {
		this(swerve, shooter, cargoHandler, Double.POSITIVE_INFINITY, stopCommand, true);
	}
    
    /**
     * Creates a {@link AutoShooterSequence} command where the robot aims for the low goal and the shooter runs
     * until stopped by {@code stopCommand}.
     * @param shooter       The {@link HoodedShooter}
     * @param cargoHandler  The {@link CargoHandler}
     * @param stopCommand   A {@code BooleanSupplier} which stops the command (and could stop the command prematurely,
     * before the shooter even starts running). {@code stopCommand} should return {@code true} when the command is
     * supposed to stop.
     */
    public AutoShooterSequence (HoodedShooter shooter, CargoHandler cargoHandler, BooleanSupplier stopCommand) {
        this(null, shooter, cargoHandler, Double.POSITIVE_INFINITY, stopCommand, false);
    }
    
    /**
     * The private constructors which the other constructors all call.
     * @param swerve            The robot's {@link Swerve} drive. This can/should be {@code null} if {@code aimHigh} is {@code false}.
     * @param shooter           The {@link HoodedShooter}
     * @param cargoHandler      The {@link CargoHandler}
     * @param shooterRunLength  The length, in seconds, to run the shooter for after it's been given enough time to get up to speed.
     * This is the same amount of time given to the cargo handler to feed all the cargo into the shooter after the shooter is up to
     * speed.
     * @param stopCommand       A {@link BooleanSupplier} representing whether or not to stop the command prematurely.
     * @param aimHigh           A {@code boolean} representing whether the robot should aim for the high goal. If this is {@code false},
     * then {@code swerve} should be {@code null} and will be unused.
     */
	private AutoShooterSequence (
            Swerve swerve,
			HoodedShooter shooter,
			CargoHandler cargoHandler,
			double shooterRunLength,
			BooleanSupplier stopCommand,
            boolean aimHigh) {
        
        if (aimHigh) {
            addCommands(
                new ParallelCommandGroup(
                    new AutoBallIndexer(cargoHandler),
                    new AutoVisionReset(swerve)), // At the same time, orient the robot to shoot and get vision info
                
                new AutoShoot(shooter, cargoHandler, shooterRunLength, aimHigh));
        } else {
            addCommands(
                new AutoBallIndexer(cargoHandler),
                new AutoShoot(shooter, cargoHandler, shooterRunLength, aimHigh));
        }
		
        this.swerve = swerve;
		this.shooter = shooter;
		this.cargoHandler = cargoHandler;
		this.stopCommand = stopCommand;
	}
	
	@Override
	public void end (boolean interrupted) {
        if (swerve != null) swerve.stop();
		shooter.stop();
		cargoHandler.stop();
		super.end(interrupted);
	}
	
	@Override
	public boolean isFinished () {
		if (stopCommand.getAsBoolean()) return true;
		return super.isFinished();
	}
    
    
    
    // THE FOLLOWING COMMANDS ARE PRIVATE AND USED ONLY IN THE FINAL AUTOSHOOTERSEQUENCE COMMAND
    
    
    
    /**
     * Indexes the ball in the ball handler (moves the ball just below the shooter so that starting the shooter won't affect it).
     */
    private static class AutoBallIndexer extends SequentialCommandGroup {
        private AutoBallIndexer (CargoHandler cargoHandler) {
            super(
                // Intake until sensor is tripped (ball at top of pulley)
                new AutoCargoHandler(cargoHandler, true, sensorTripped -> sensorTripped, false),
                
                // Intake backwards until sensor is not tripped (ball just below the kicker so the shooter can get up to speed)
                new AutoCargoHandler(cargoHandler, false, sensorTripped -> !sensorTripped, false));
        }
    }
    
    /**
     * Runs the shooter and cargo handler at the same time. Assumes the cargo has already been indexed.
     */
    private static class AutoShoot extends ParallelRaceGroup {
        private AutoShoot (HoodedShooter shooter, CargoHandler cargoHandler, double shooterRunLength, boolean aimHigh) {
            // Run shooter (will run until stopped)
            super(
                new AutoRunShooter(shooter, aimHigh),
				new SequentialCommandGroup(
					// Wait before running cargo handler
					new WaitCommand(0.5),
					// Run cargo handler
					new AutoCargoHandler(cargoHandler, shooterRunLength, true, false)));
        }
    }
    
    /**
     * Simply runs the shooter at a given speed.
     */
    private static class AutoRunShooter extends CommandBase {
        
        private final HoodedShooter shooter;
        private final boolean aimHigh;
        private ShooterSpeed shooterSpeed;
        
        /**
         * Shoots in either the low or high goal.
         * @param shooter   The {@link HoodedShooter}
         * @param aimHigh   {@code true} if the shooter should aim for the high goal, {@code false} if the shooter
         * should aim for the low goal.
         */
        private AutoRunShooter (HoodedShooter shooter, boolean aimHigh) {
            this.shooter = shooter;
            this.aimHigh = aimHigh;
            addRequirements(shooter);
        }
        
        @Override
        public void initialize () {
            if (aimHigh) shooterSpeed = ShooterSpeedsLookup.getShooterSpeed(VisionHandler.getInstance().getDistanceFromHub());
            else shooterSpeed = ShooterSpeedsLookup.LOW_GOAL_SPEED;
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