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
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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
	
	private final BooleanSupplier runCargoHandler, reverseButton, runShooterSequence;
	private final DoubleSupplier runIntake, runShooter;
	
	public CentralSystem (
			CargoHandler cargoHandler,
			Intake intake,
			Shooter shooter,
			BooleanSupplier runCargoHandler,
			DoubleSupplier runIntake,
			DoubleSupplier runShooter,
			BooleanSupplier runShooterSequence,
			BooleanSupplier reverseButton) {
		this.cargoHandler = cargoHandler;
		this.intake = intake;
		this.shooter = shooter;
		
		this.runCargoHandler = runCargoHandler;
		this.runIntake = runIntake;
		this.runShooter = runShooter;
		this.runShooterSequence = runShooterSequence;
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
		// Attempting to run the shooter sequence
		if (runShooterSequence.getAsBoolean())
			CommandScheduler.getInstance().schedule(new AutoShooterSequence(shooter, cargoHandler, () -> !runShooterSequence.getAsBoolean()));
		
		int r = (reverseButton.getAsBoolean() ? -1 : 1); //r is a numerical value of true or false for reversebutton

		cargoHandler.setSpeed(runCargoHandler.getAsBoolean() ? cargoHandlerSpeed * r : 0);

		maxIntakeSpeed = SmartDashboard.getNumber("Max Intake Speed", 0);
		maxShooterSpeed = SmartDashboard.getNumber("Max Shooter Speed", 0);
		double intakeSpeed = r * centralSystemInputHandler.apply(runIntake.getAsDouble()) * maxIntakeSpeed;
		double shooterSpeed = r * centralSystemInputHandler.apply(runShooter.getAsDouble()) * maxShooterSpeed;
		
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