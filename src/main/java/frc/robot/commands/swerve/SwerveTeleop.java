package frc.robot.commands.swerve;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Dashboard;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.subsystems.SwerveDrive;
import frc.team1711.swerve.util.InputHandler;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class SwerveTeleop extends CommandBase {
	
	private static final InputHandler swerveInputHandler = new InputHandler(0.10, InputHandler.Curve.squareCurve);
	
	public static final SwerveDrive.ControlsConfig
		normalConfig = new SwerveDrive.ControlsConfig(0.5, 0.5, swerveInputHandler),
		fastConfig = new SwerveDrive.ControlsConfig(1, 1, swerveInputHandler),
		slowConfig = new SwerveDrive.ControlsConfig(0.1, 0.1, swerveInputHandler),
		outreachSafetyConfig = new SwerveDrive.ControlsConfig(0.15, 0.15, swerveInputHandler);
	
	private final Swerve swerveDrive;
	private final DoubleSupplier
		strafeX,
		strafeY,
		steering;
	private final BooleanSupplier
		fastMode,
		slowMode,
		resetGyro,
		toggleFieldRelative,
		outreachSafetyBrake;
	
	private boolean fieldRelative = true;
	
	public SwerveTeleop (
			Swerve swerveDrive,
			DoubleSupplier strafeX,
			DoubleSupplier strafeY,
			DoubleSupplier steering,
			BooleanSupplier fastMode,
			BooleanSupplier slowMode,
			BooleanSupplier resetGyro,
			BooleanSupplier toggleFieldRelative,
			BooleanSupplier outreachSafetyBrake) {
		
		this.swerveDrive = swerveDrive;
		
		this.strafeX = strafeX;
		this.strafeY = strafeY;
		this.steering = steering;
		this.fastMode = fastMode;
		this.slowMode = slowMode;
		this.resetGyro = resetGyro;
		this.toggleFieldRelative = toggleFieldRelative;
		this.outreachSafetyBrake = outreachSafetyBrake;
		
		addRequirements(swerveDrive);
	}
	
	@Override
	public void initialize () {
		swerveDrive.stop();
	}
	
	@Override
	public void execute () {
		// The field relativity cannot be toggled in outreach safety mode (to prevent confusion)
		if (toggleFieldRelative.getAsBoolean() && !Dashboard.OUTREACH_SAFETY_MODE.get())
			fieldRelative = !fieldRelative;
		Dashboard.IS_FIELD_RELATIVE.put(fieldRelative);
		
		// Gyro cannot be reset during outreach safety mode (to prevent confusion)
		if (resetGyro.getAsBoolean() && !Dashboard.OUTREACH_SAFETY_MODE.get())
			swerveDrive.resetGyro();
		
		// Sets the current controls configuration
		SwerveDrive.ControlsConfig controlsConfig = getControlsConfig();
		
		if (outreachSafetyBrake.getAsBoolean()) {
			swerveDrive.stop();
		} else {
			// Performs driving for the swerve system with input deadbands turned on
			if (fieldRelative) {
				swerveDrive.fieldRelativeUserInputDrive(
					strafeX.getAsDouble(),
					strafeY.getAsDouble(),
					steering.getAsDouble(),
					controlsConfig);
			} else {
				swerveDrive.userInputDrive(
					strafeX.getAsDouble(),
					strafeY.getAsDouble(),
					steering.getAsDouble(),
					controlsConfig);
			}
		}
	}
	
	private SwerveDrive.ControlsConfig getControlsConfig () {
		if (Dashboard.OUTREACH_SAFETY_MODE.get()) return outreachSafetyConfig;
		else if (slowMode.getAsBoolean()) return slowConfig;
		else if (fastMode.getAsBoolean()) return fastConfig;
		else return normalConfig;
	}
	
	@Override
	public void end (boolean interrupted) {
		swerveDrive.stop();
	}
	
	@Override
	public boolean isFinished () {
		return false;
	}
	
}