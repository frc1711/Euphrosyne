// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.swerve;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.util.InputHandler;

public class SwerveTeleop extends CommandBase {
	
	private static final InputHandler swerveInputHandler = new InputHandler(0.10, InputHandler.Curve.squareCurve);
	
	private final Swerve swerveDrive;
	private final DoubleSupplier
		strafeX,
		strafeY,
		steering;
	
	public SwerveTeleop (
			Swerve swerveDrive,
			DoubleSupplier strafeX,
			DoubleSupplier strafeY,
			DoubleSupplier steering) {
		
		this.swerveDrive = swerveDrive;
		
		this.strafeX = strafeX;
		this.strafeY = strafeY;
		this.steering = steering;
		
		// SmartDashboard commands
		addRequirements(swerveDrive);
	}
	
	@Override
	public void initialize () {
		swerveDrive.stop();
	}
	
	@Override
	public void execute () {
		// Performs field-relative driving for the swerve system with input deadbands turned on
		swerveDrive.fieldRelativeUserInputDrive(strafeX.getAsDouble(), strafeY.getAsDouble(), steering.getAsDouble(), swerveInputHandler);
		
		// Update SmartDashboard
		swerveDrive.displayOrientation();
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