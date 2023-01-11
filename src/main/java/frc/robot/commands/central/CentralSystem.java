package frc.robot.commands.central;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.Dashboard;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.team1711.swerve.util.InputHandler;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class CentralSystem extends CommandBase {
	
	private static final InputHandler centralSystemInputHandler = new InputHandler(0.10, InputHandler.Curve.linearCurve);
	
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	
	private final BooleanSupplier runHandlerAndIntake, runCargoHandler, reverseButton, runShooterSequence;
	private final DoubleSupplier runIntake, runShooter;
	
	public CentralSystem (
			CargoHandler cargoHandler,
			Intake intake,
			Shooter shooter,
			BooleanSupplier runHandlerAndIntake,
			BooleanSupplier runCargoHandler,
			DoubleSupplier runIntake,
			DoubleSupplier runShooter,
			BooleanSupplier runShooterSequence,
			BooleanSupplier reverseButton) {
		this.cargoHandler = cargoHandler;
		this.intake = intake;
		this.shooter = shooter;
		
		this.runHandlerAndIntake = runHandlerAndIntake;
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
	}
	
	@Override
	public void execute () {
		
		int r = (reverseButton.getAsBoolean() ? -1 : 1); //r is a numerical value of true or false for reversebutton
		
		boolean runCargoHandlerBool = runCargoHandler.getAsBoolean() || runHandlerAndIntake.getAsBoolean();
		cargoHandler.setSpeed(runCargoHandlerBool ? Dashboard.CARGO_HANDLER_SPEED.get() * r : 0);
		
		double intakeSpeed = r * centralSystemInputHandler.apply(runIntake.getAsDouble()) * Dashboard.INTAKE_MAX_SPEED.get();
		if (runHandlerAndIntake.getAsBoolean()) intakeSpeed = r * Dashboard.INTAKE_MAX_SPEED.get();
		
		double shooterSpeed = r * centralSystemInputHandler.apply(runShooter.getAsDouble()) * Dashboard.SHOOTER_MAX_SPEED.get();
		
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