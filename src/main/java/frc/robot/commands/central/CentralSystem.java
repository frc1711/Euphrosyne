package frc.robot.commands.central;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.Dashboard;
import frc.robot.commands.auton.AutoShooterSequence;
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
     * <b>DO NOT CALL METHODS ON SWERVE OR SHOOTER IN THIS COMMAND. THESE ARE ONLY KEPT AS FIELDS HERE TO BE PASSED TO THE
     * AUTOSHOOTERSEQUENCE COMMAND WHEN NECESSARY.</b>
     */
    private final Swerve swerve;
    /**
     * <b>DO NOT CALL METHODS ON SWERVE OR SHOOTER IN THIS COMMAND. THESE ARE ONLY KEPT AS FIELDS HERE TO BE PASSED TO THE
     * AUTOSHOOTERSEQUENCE COMMAND WHEN NECESSARY.</b>
     */
    private final HoodedShooter shooter;
    
	private final CargoHandler cargoHandler;
	private final Intake intake;
	
	private final BooleanSupplier runHandlerAndIntake, runCargoHandler, reverseButton, runLowGoalShooter, runHighGoalShooter;
	private final DoubleSupplier runIntake;
	
    /**
     * NOTE: Neither {@code swerve} nor {@code shooter} are added as requirements for this command. This command
     * only accepts them as arguments in the constructor for use in scheduling the {@link AutoShooterSequence} command.
     */
	public CentralSystem (
			CargoHandler cargoHandler,
			Intake intake,
			HoodedShooter shooter,
			BooleanSupplier runHandlerAndIntake,
			BooleanSupplier runCargoHandler,
			DoubleSupplier runIntake,
			BooleanSupplier runLowGoalShooter,
            BooleanSupplier runHighGoalShooter,
			BooleanSupplier reverseButton,
            Swerve swerve) {
		this.cargoHandler = cargoHandler;
		this.intake = intake;
		this.shooter = shooter;
		
		this.runHandlerAndIntake = runHandlerAndIntake;
		this.runCargoHandler = runCargoHandler;
		this.runIntake = runIntake;
		this.runLowGoalShooter = runLowGoalShooter;
		this.runHighGoalShooter = runHighGoalShooter;
		this.reverseButton = reverseButton;
		
		addRequirements(cargoHandler, intake);
        
        // the swerve drive is not used in this command but is instead passed into another command to be scheduled
        this.swerve = swerve; 
	}
	
	@Override
	public void initialize () {
		cargoHandler.stop();
		intake.stop();
	}
	
	@Override
	public void execute () {
        // Check whether to run the high goal or low goal shooter sequences
		checkRunShooterSequences();
		
		// Checks whether the reverse button is pressed; r represents a scalar for speed inputs depending on the reverse mode
		int r = reverseButton.getAsBoolean() ? -1 : 1;
		
        // Control the cargo handler
		boolean runCargoHandlerBool = runCargoHandler.getAsBoolean() || runHandlerAndIntake.getAsBoolean();
		cargoHandler.setSpeed(runCargoHandlerBool ? r : 0, true);
		
        // Control the intake
		double intakeSpeed = r * centralSystemInputHandler.apply(runIntake.getAsDouble());
		if (runHandlerAndIntake.getAsBoolean()) intakeSpeed = r;
		intake.setSpeed(intakeSpeed);
	}
    
    private void checkRunShooterSequences () {
        // Run the low goal shooter command if necessary
        if (runLowGoalShooter.getAsBoolean())
            CommandScheduler.getInstance().schedule(
                new AutoShooterSequence(shooter, cargoHandler, () -> !runLowGoalShooter.getAsBoolean()));
        
        // Run the high goal shooter command if necessary
		if (runHighGoalShooter.getAsBoolean())
            CommandScheduler.getInstance().schedule(
                new AutoShooterSequence(swerve, shooter, cargoHandler, () -> !runHighGoalShooter.getAsBoolean()));
    }
	
	@Override
	public void end (boolean interrupted) {
		cargoHandler.stop();
		intake.stop();
	}
	
	@Override
	public boolean isFinished () {
		return false;
	}
}