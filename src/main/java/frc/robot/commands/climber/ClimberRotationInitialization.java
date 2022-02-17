// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;

/**
 * Rotates the climber in the positive direction until the limit switches are hit,
 * then updates the climber centering the rotation encoder so that it doesn't allow
 * for movement in the incorrect direction.
 */
public class ClimberRotationInitialization extends CommandBase {
	private static final double rotationSpeed = 0.12; // Must be positive
	
	private final Climber climber;
	private boolean trippedLimitSwitch = false;
	
	public ClimberRotationInitialization (Climber climber) {
		this.climber = climber;
		addRequirements(climber);
	}
	
	// 1. Slowly rotate climber (direction) until limit switch is tripped
	// 2. That direction is canceled by the limit switch
	// 3. There is an encoder stop in the opposite direction a number of rotations
	
	@Override
	public void initialize () {
		climber.stop();
		climber.unsetFullyWrappedRotationMarker();
	}
	
	@Override
	public void execute () {
		// Step 1: Slowly rotate climber in positive direction until limit switch is tripped
		if (!climber.getRotationLimitSwitch()) {
			climber.setRotationSpeed(rotationSpeed);
		} else trippedLimitSwitch = true;
	}
	
	@Override
	public void end (boolean interrupted) {
		climber.stop();
		
		// Step 2: Set the fully-wrapped rotation spindle state for the climber so that it
		// doesn't unwrap all the way and start wrapping in the opposite direction
		climber.setFullyWrappedRotationMarker();
	}
	
	@Override
	public boolean isFinished () {
		return trippedLimitSwitch;
	}
}