// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Sequence for the shooter (reverse ball handler, start shooter, then user can hit ball handling), shooter controlled by toggle
// Ball handling, intake, shooter reverse mode
// Slow mode, turbo mode
// Cameras, switchable

package frc.robot.commands.central;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.team1711.swerve.util.InputHandler;

public class CentralSystem extends CommandBase {
	
	private static final InputHandler centralSystemInputHandler = new InputHandler(0.10, InputHandler.Curve.linearCurve);
	
	private static final double cargoHandlerSpeed = -0.5;
	
	private double
		maxIntakeSpeed = -0.7,
		maxShooterSpeed = -0.7;
	
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	
	private final BooleanSupplier runCargoHandler, reverseButton;
	private final DoubleSupplier runIntake, runShooter;
	
	public CentralSystem (
			CargoHandler cargoHandler,
			Intake intake,
			Shooter shooter,
			BooleanSupplier runCargoHandler,
			DoubleSupplier runIntake,
			DoubleSupplier runShooter, 
			BooleanSupplier reverseButton) {
		this.cargoHandler = cargoHandler;
		this.intake = intake;
		this.shooter = shooter;
		
		this.runCargoHandler = runCargoHandler;
		this.runIntake = runIntake;
		this.runShooter = runShooter;
		this.reverseButton = reverseButton;
		
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
		int r = (reverseButton.getAsDouble() ? -1 : 1);

		cargoHandler.setSpeed(runCargoHandler.getAsBoolean() ? cargoHandlerSpeed : 0);

		maxIntakeSpeed = SmartDashboard.getNumber("Max Intake Speed", 0);
		maxShooterSpeed = SmartDashboard.getNumber("Max Shooter Speed", 0);
		double intakeSpeed = centralSystemInputHandler.apply(runIntake.getAsDouble()) * maxIntakeSpeed;
		double shooterSpeed = centralSystemInputHandler.apply(runShooter.getAsDouble()) * maxShooterSpeed;
		cargoHandlerSpeed *= r;
		intakeSpeed *= r;
		shooterSpeed *= r;
		
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