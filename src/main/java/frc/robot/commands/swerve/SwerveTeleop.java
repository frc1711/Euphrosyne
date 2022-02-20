// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.subsystems.SwerveDrive;
import frc.team1711.swerve.util.InputHandler;

public class SwerveTeleop extends CommandBase {
	
	private static final InputHandler swerveInputHandler = new InputHandler(0.10, InputHandler.Curve.squareCurve);
	
	public static final SwerveDrive.ControlsConfig
		normalConfig = new SwerveDrive.ControlsConfig(0.5, 0.5, swerveInputHandler),
		fastConfig = new SwerveDrive.ControlsConfig(1, 1, swerveInputHandler),
		slowConfig = new SwerveDrive.ControlsConfig(0.1, 0.1, swerveInputHandler);
	
	private final Swerve swerveDrive;
	private final DoubleSupplier
		strafeX,
		strafeY,
		steering;
	private final BooleanSupplier
		fastMode,
		slowMode;
	
	public SwerveTeleop (
			Swerve swerveDrive,
			DoubleSupplier strafeX,
			DoubleSupplier strafeY,
			DoubleSupplier steering,
			BooleanSupplier fastMode,
			BooleanSupplier slowMode) {
		
		this.swerveDrive = swerveDrive;
		
		this.strafeX = strafeX;
		this.strafeY = strafeY;
		this.steering = steering;
		this.fastMode = fastMode;
		this.slowMode = slowMode;
		
		// SmartDashboard commands
		addRequirements(swerveDrive);
	}
	
	@Override
	public void initialize () {
		swerveDrive.stop();
	}
	
	@Override
	public void execute () {
		// Sets the current controls configuration
		SwerveDrive.ControlsConfig controlsConfig = getControlsConfig();
		
		// Performs field-relative driving for the swerve system with input deadbands turned on
		swerveDrive.fieldRelativeUserInputDrive(strafeX.getAsDouble(), strafeY.getAsDouble(), steering.getAsDouble(), controlsConfig);
		
		// Update SmartDashboard
		swerveDrive.displayOrientation();
	}
	
	private SwerveDrive.ControlsConfig getControlsConfig () {
		if (slowMode.getAsBoolean()) return slowConfig;
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