// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.central;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class CentralSystem extends CommandBase {
	
	private static final double cargoHandlerSpeed = -0.5;
	
	private double
		maxIntakeSpeed = -1,
		maxShooterSpeed = -1;
	
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	
	private final BooleanSupplier runCargoHandler;
	private final DoubleSupplier runIntake, runShooter;
	
	public CentralSystem (
			CargoHandler cargoHandler,
			Intake intake,
			Shooter shooter,
			BooleanSupplier runCargoHandler,
			DoubleSupplier runIntake,
			DoubleSupplier runShooter) {
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
		
		SmartDashboard.putNumber("Max Intake Speed", maxIntakeSpeed);
		SmartDashboard.putNumber("Max Shooter Speed", maxShooterSpeed);
	}
	
	@Override
	public void execute () {
		cargoHandler.setSpeed(runCargoHandler.getAsBoolean() ? cargoHandlerSpeed : 0);
		
		maxIntakeSpeed = SmartDashboard.getNumber("Max Intake Speed", 0);
		maxShooterSpeed = SmartDashboard.getNumber("Max Shooter Speed", 0);
		double intakeSpeed = runIntake.getAsDouble() * maxIntakeSpeed;
		double shooterSpeed = runShooter.getAsDouble() * maxShooterSpeed;
		
		SmartDashboard.putNumber("Current Intake Speed", intakeSpeed);
		SmartDashboard.putNumber("Current Shooter Speed", shooterSpeed);
		intake.setSpeed(intakeSpeed);
		shooter.setSpeed(shooterSpeed);
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