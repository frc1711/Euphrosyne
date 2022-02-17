// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimberCommand extends CommandBase {
	
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
		// TODO: Make these actual constants
		climber.setExtensionSpeed(extensionInput.getAsDouble() * 0.4);
		climber.setRotationSpeed(rotationInput.getAsDouble() * 0.2);
		
		// TODO: Remove this after fixing the controls-loss bug
		SmartDashboard.putNumber("Rotation Input", rotationInput.getAsDouble());
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