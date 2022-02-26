// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;

/**
 * Rotates the climber in the positive direction until the limit switches are hit,
 * then updates the climber centering the rotation encoder so that it doesn't allow
 * for movement in the incorrect direction.
 * <br><br>Does essentially the same for the extension of the climber.
 */
public class ClimberInitialization extends CommandBase {
	private static final double rotationSpeed = 0.12; // Must be positive, so climber is rotated toward limit switch
	private static final double extensionSpeed = -0.15; // Must be negative, so climber is retracted toward limit switch
	
	private final Climber climber;
	private boolean trippedRotationLimitSwitch = false, trippedExtensionLimitSwitch = false;
	
	public ClimberInitialization (Climber climber) {
		this.climber = climber;
		addRequirements(climber);
	}
	
	@Override
	public void initialize () {
		climber.stop();
	}
	
	private void rotateClimberUntilSwitched () {
		if (!climber.getRotationLimitSwitch()) {
			climber.setRotationSpeedOverride(rotationSpeed);
		} else {
			climber.setRotationSpeedOverride(0);
			trippedRotationLimitSwitch = true;
		}
	}
	
	private void retractClimberUntilSwitched () {
		if (!climber.getExtensionLimitSwitch()) {
			climber.setExtensionSpeedOverride(extensionSpeed);
		} else {
			climber.setExtensionSpeedOverride(0);
			trippedExtensionLimitSwitch = true;
		}
	}
	
	@Override
	public void execute () {
		rotateClimberUntilSwitched();
		retractClimberUntilSwitched();
	}
	
	@Override
	public void end (boolean interrupted) {
		climber.stop();
		
		// Step 2: Set the fully-wrapped spindle states for the climber so that it
		// doesn't unwrap all the way and start wrapping in the opposite direction
		climber.rotationEncoderReset();
		climber.extensionEncoderReset();
	}
	
	@Override
	public boolean isFinished () {
		return trippedRotationLimitSwitch && trippedExtensionLimitSwitch;
	}
}