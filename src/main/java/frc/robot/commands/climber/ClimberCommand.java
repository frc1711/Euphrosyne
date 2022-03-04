package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.util.function.BooleanConsumer;

import frc.robot.subsystems.Climber;
import frc.team1711.swerve.util.InputHandler;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;


public class ClimberCommand extends CommandBase {
	
	private static final double maxClimberHeightFromPivot = 5*12; // 5'6" from ground; 6" from pivot to ground
	
	private static final InputHandler climberInputHandler = new InputHandler(0.10, InputHandler.Curve.squareCurve);
	
	private static final double extensionMaxSpeed = 0.75, rotationMaxSpeed = 0.5;
	
	private final Climber climber;
	
	private final DoubleSupplier extensionInput, rotationInput;
	private final BooleanSupplier overrideMode;
	private final BooleanConsumer climbLimitReached;
	
	public ClimberCommand (
			Climber climber,
			DoubleSupplier extensionInput,
			DoubleSupplier rotationInput,
			BooleanSupplier overrideMode,
			BooleanConsumer climbLimitReached) {
		this.climber = climber;
		this.extensionInput = extensionInput;
		this.rotationInput = rotationInput;
		this.overrideMode = overrideMode;
		this.climbLimitReached = climbLimitReached;
		addRequirements(climber);
	}
	
	@Override
	public void initialize () {
		climber.stop();
	}
	
	// Returns whether or not the climber length makes it reach higher than the maximum allowed height
	private boolean exceedesClimberHeight () {
		// sin(a) = h / l
		// l = h / sin(a)
		final double maxLength = maxClimberHeightFromPivot / Math.sin(climber.getRotationDegrees() * (Math.PI / 180));
		
		return climber.getExtensionHeightInches() > maxLength;
	}
	
	@Override
	public void execute () {
		
		
		// Calculate extension and rotation speeds
		double extensionSpeed = extensionMaxSpeed * climberInputHandler.apply(extensionInput.getAsDouble());
		double rotationSpeed = rotationMaxSpeed * climberInputHandler.apply(rotationInput.getAsDouble());
		
		if (overrideMode.getAsBoolean()) {
			// Override
			climber.setExtensionSpeedOverride(extensionSpeed);
			climber.setRotationSpeedOverride(rotationSpeed);
		} else {
			// Default mode (non-override)
			// Restrictions on climber height
			if (exceedesClimberHeight()) {
				// Prevent extending past height limit
				if (extensionSpeed > 0) extensionSpeed = 0;
				
				// Prevent rotating towards upright, making height exceed limit
				if (rotationSpeed < 0 && climber.getRotationDegrees() > 90) rotationSpeed = 0; // Pushing away from limit switch
				if (rotationSpeed > 0 && climber.getRotationDegrees() < 90) rotationSpeed = 0; // Pulling toward limit switch
			}
			
			final boolean limitReached =
				climber.setExtensionSpeed(extensionSpeed) ||
				climber.setRotationSpeed(rotationSpeed);
			
			climbLimitReached.accept(limitReached);
		}
		
	}
	
	@Override
	public void end (boolean interrupted) {
		climber.stop();
	}
	
	@Override
	public boolean isFinished () {
		return false;
	}
	
}