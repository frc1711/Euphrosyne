// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class CentralSystem extends CommandBase {
	
	private static final double
		cargoHandlerSpeed = 0.2,
		intakeSpeed = 0.2,
		shooterSpeed = -0.71;
	
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	
	private final BooleanSupplier runCargoHandler, runIntake, runShooter;
	
	public CentralSystem (
			CargoHandler cargoHandler,
			Intake intake,
			Shooter shooter,
			BooleanSupplier runCargoHandler,
			BooleanSupplier runIntake,
			BooleanSupplier runShooter) {
		this.cargoHandler = cargoHandler;
		this.intake = intake;
		this.shooter = shooter;
		
		this.runCargoHandler = runCargoHandler;
		this.runIntake = runIntake;
		this.runShooter = runShooter;
		
		addRequirements(cargoHandler, intake, shooter);
	}
	
	@Override
	public void initialize () {
		cargoHandler.stop();
		intake.stop();
		shooter.stop();
	}
	
	@Override
	public void execute () {
		cargoHandler.setSpeed(runCargoHandler.getAsBoolean() ? cargoHandlerSpeed : 0);
		intake.setSpeed(runIntake.getAsBoolean() ? intakeSpeed : 0);
		shooter.setSpeed(runShooter.getAsBoolean() ? shooterSpeed : 0);
	}
	
	@Override
	public void end (boolean interrupted) {
		cargoHandler.stop();
		intake.stop();
		shooter.stop();
	}
	
	@Override
	public boolean isFinished () {
		return false;
	}
}