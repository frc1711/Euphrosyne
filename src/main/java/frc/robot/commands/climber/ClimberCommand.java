// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;
import frc.team1711.swerve.util.InputHandler;

import java.util.function.DoubleSupplier;

public class ClimberCommand extends CommandBase {
	
	private static final double maxClimberHeightFromPivot = 5*12; // 5'6" from ground; 6" from pivot to ground
	
	private static final InputHandler climberInputHandler = new InputHandler(0.10, InputHandler.Curve.squareCurve);
	
	private static final double extensionMaxSpeed = 0.4, rotationMaxSpeed = 0.2;
	
	private final Climber climber;
	private final DoubleSupplier extensionInput, rotationInput;
	
	public ClimberCommand (
			Climber climber,
			DoubleSupplier extensionInput,
			DoubleSupplier rotationInput) {
		this.climber = climber;
		this.extensionInput = extensionInput;
		this.rotationInput = rotationInput;
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
		
		// l = h / sin(a)
		double maxLength = maxClimberHeightFromPivot / Math.sin(climber.getRotationDegrees() * (Math.PI / 180));
		double currentLength = climber.getExtensionHeightInches();
		
		SmartDashboard.putNumber("Max length at angle", maxLength);
		SmartDashboard.putNumber("Current length", currentLength);
		
		// Calculate extension and rotation speeds
		double extensionSpeed = extensionMaxSpeed * climberInputHandler.apply(extensionInput.getAsDouble());
		double rotationSpeed = rotationMaxSpeed * climberInputHandler.apply(rotationInput.getAsDouble());
		
		// Restrictions on climber height
		if (exceedesClimberHeight()) {
			// Prevent extending past height limit
			if (extensionSpeed > 0) extensionSpeed = 0;
			
			// Prevent rotating towards upright, making height exceed limit
			if (rotationSpeed < 0 && climber.getRotationDegrees() > 90) rotationSpeed = 0; // Pushing away from limit switch
			if (rotationSpeed > 0 && climber.getRotationDegrees() < 90) rotationSpeed = 0; // Pulling toward limit switch
		}
		
		climber.setExtensionSpeed(extensionSpeed);
		climber.setRotationSpeed(rotationSpeed);
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