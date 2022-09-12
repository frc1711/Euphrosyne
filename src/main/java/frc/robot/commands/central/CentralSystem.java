package frc.robot.commands.central;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.Dashboard;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.util.InputHandler;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class CentralSystem extends CommandBase {
	
	private static final InputHandler centralSystemInputHandler = new InputHandler(0.10, InputHandler.Curve.linearCurve);
	
    /**
     * <b>DO NOT CALL METHODS ON SWERVE IN THIS COMMAND. THIS IS ONLY KEPT AS A FIELD HERE TO BE PASSED TO THE
     * AUTOSHOOTERSEQUENCE COMMAND WHEN NECESSARY.</b>
     */
    private final Swerve swerve;
    
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final HoodedShooter shooter;
	
	private final BooleanSupplier runHandlerAndIntake, runCargoHandler, reverseButton, runShooterSequence;
	private final DoubleSupplier runIntake;
	
    /**
     * NOTE: {@code swerve} is not added as a requirement for this command. This command
     * only accepts it as an argument in the constructor for use in scheduling the
     * {@link AutoShooterSequence} command.
     */
	public CentralSystem (
			CargoHandler cargoHandler,
			Intake intake,
			HoodedShooter shooter,
			BooleanSupplier runHandlerAndIntake,
			BooleanSupplier runCargoHandler,
			DoubleSupplier runIntake,
			DoubleSupplier runShooter,
			BooleanSupplier runShooterSequence,
			BooleanSupplier reverseButton,
            Swerve swerve) {
		this.cargoHandler = cargoHandler;
		this.intake = intake;
		this.shooter = shooter;
		
		this.runHandlerAndIntake = runHandlerAndIntake;
		this.runCargoHandler = runCargoHandler;
		this.runIntake = runIntake;
		this.runShooterSequence = runShooterSequence;
		this.reverseButton = reverseButton;
		
		addRequirements(cargoHandler, intake, shooter);
        
        // the swerve drive is not used in this command but is instead passed into another command to be scheduled
        this.swerve = swerve; 
	}
	
	@Override
	public void initialize () {
		cargoHandler.stop();
		intake.stop();
		shooter.stop();
	}
	
	@Override
	public void execute () {
		// Run the shooter command if necessary
		if (runShooterSequence.getAsBoolean())
			CommandScheduler.getInstance().schedule(
				new AutoShooterSequence(swerve, shooter, cargoHandler, () -> !runShooterSequence.getAsBoolean()));
		
		// Checks whether the reverse button is pressed; r represents a scalar for speed inputs depending on the reverse mode
		int r = reverseButton.getAsBoolean() ? -1 : 1;
		
		boolean runCargoHandlerBool = runCargoHandler.getAsBoolean() || runHandlerAndIntake.getAsBoolean();
		cargoHandler.setSpeed(runCargoHandlerBool ? Dashboard.CARGO_HANDLER_SPEED.get() * r : 0);
		
		double intakeSpeed = r * centralSystemInputHandler.apply(runIntake.getAsDouble()) * Dashboard.INTAKE_MAX_SPEED.get();
		if (runHandlerAndIntake.getAsBoolean()) intakeSpeed = r * Dashboard.INTAKE_MAX_SPEED.get();
		
		intake.setSpeed(intakeSpeed);
		shooter.stop();
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