package frc.robot.commands.central;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.team1711.swerve.util.InputHandler;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class CentralSystem extends CommandBase {
	
	private static final InputHandler centralSystemInputHandler = new InputHandler(0.10, InputHandler.Curve.linearCurve);
	
	private static final double cargoHandlerSpeed = -0.5;
	
	private final double
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
	}
	
	@Override
	public void execute () {
		// Attempting to run the shooter sequence
		if (runShooterSequence.getAsBoolean())
			CommandScheduler.getInstance().schedule(
				new AutoShooterSequence(shooter, cargoHandler, () -> !runShooterSequence.getAsBoolean(), true));
		
		int r = (reverseButton.getAsBoolean() ? -1 : 1); //r is a numerical value of true or false for reversebutton
		
		cargoHandler.setSpeed(runCargoHandler.getAsBoolean() ? cargoHandlerSpeed * r : 0);
		
		double intakeSpeed = r * centralSystemInputHandler.apply(runIntake.getAsDouble()) * maxIntakeSpeed;
		double shooterSpeed = r * centralSystemInputHandler.apply(runShooter.getAsDouble()) * maxShooterSpeed;
		
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