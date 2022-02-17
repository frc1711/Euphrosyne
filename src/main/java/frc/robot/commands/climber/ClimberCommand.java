// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimberCommand extends CommandBase {
	
	private static final double extensionSpeed = 0.4, rotationSpeed = 0.2;
	
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
	
	@Override
	public void execute () {
		climber.setExtensionSpeed(extensionInput.getAsDouble() * extensionSpeed);
		climber.setRotationSpeed(rotationInput.getAsDouble() * rotationSpeed);
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