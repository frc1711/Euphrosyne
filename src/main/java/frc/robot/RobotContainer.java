package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.auton.AutoLowGoalTaxi;
import frc.robot.commands.auton.AutoTwoBallLow;
import frc.robot.commands.auton.WheelDistances;
import frc.robot.commands.auton.base.AutoMove;
import frc.robot.commands.auton.base.AutoVisionReset;
import frc.robot.commands.central.CentralSystem;
import frc.robot.commands.climber.ClimberCommand;
import frc.robot.commands.climber.ClimberInitialization;
import frc.robot.commands.swerve.ResetGyro;
import frc.robot.commands.swerve.SetSwerveModulePositions;
import frc.robot.commands.swerve.SwerveTeleop;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
	
	private final XboxController
		driveController = new XboxController(0),
		centralController = new XboxController(1);
	
	private final Swerve swerveDrive = Swerve.getInstance();
	private final CargoHandler cargoHandler = CargoHandler.getInstance();
	private final Intake intake = Intake.getInstance();
	private final Climber climber = Climber.getInstance();
	private final HoodedShooter hoodedShooter = HoodedShooter.getInstance();
	
	// Swerve teleop command
	private final SwerveTeleop swerveTeleop = new SwerveTeleop(
		swerveDrive,
		() -> driveController.getLeftX(),											// Strafe X
		() -> -driveController.getLeftY(),											// Strafe Y
		() -> driveController.getRightX(),											// Steering
		() -> driveController.getRightTriggerAxis() > 0.4,							// Fast mode
		() -> driveController.getLeftTriggerAxis() > 0.4,							// Slow mode
		() -> driveController.getLeftBumper() && driveController.getRightBumper(),	// Reset gyro
		() -> driveController.getBButtonPressed(),									// Field relative toggle
		() -> centralController.getYButton());
	
	// Central system command
	private final CentralSystem centralSystem = new CentralSystem(
		cargoHandler, intake, hoodedShooter,
		() -> centralController.getAButton(),										// Cargo handler AND intake
		() -> centralController.getBButton(),										// Cargo handler
		() -> centralController.getRightTriggerAxis(),								// Intake
		() -> centralController.getLeftBumper(),								    // Low goal shooter
		() -> centralController.getRightBumper(),									// High goal shooter
		() -> centralController.getXButton(),                                       // Reverse mode
        swerveDrive); // neither added as a requirement in the command nor used except for scheduling other commands
	
	// Climber command
	private final ClimberCommand climberCommand = new ClimberCommand(
		climber,
		() -> -centralController.getRightY(),										// Extension
		() -> centralController.getLeftY(),											// Rotation
		() -> Dashboard.CLIMBER_OVERRIDE_MODE.get(),								// Climber override limits
		(x) -> centralController.setRumble(RumbleType.kLeftRumble, x ? 0.7 : 0));	// Rumble for hitting climber limit
	
	private final SendableChooser<Command> autonSelector;
	
	public RobotContainer () {
        CameraServer.startAutomaticCapture(0);
        
		// Automatically run default commands
		swerveDrive.setDefaultCommand(swerveTeleop);
		climber.setDefaultCommand(climberCommand);
		cargoHandler.setDefaultCommand(centralSystem);
		// hoodedShooter.setDefaultCommand(hoodedShooterTest);
		
		// Auton selector
		autonSelector = new SendableChooser<Command>();
		CommandWrapper[] autonCommands = getAutonCommands();
		for (CommandWrapper command : autonCommands)
			autonSelector.addOption(command.commandName, command);
		autonSelector.setDefaultOption("No Auton", null);
        
		// Put sendables to dashboard
		putSendablesToDashboard();
	}
	
	private void putSendablesToDashboard () {
		Dashboard.putSendable("Auton Selector", autonSelector);
		Dashboard.putSendable("Swerve Module Positions", new InstantCommand(() -> new SetSwerveModulePositions(swerveDrive).schedule()));
		Dashboard.putSendable("Climber Initialization", new InstantCommand(() -> new ClimberInitialization(climber).schedule()));
		Dashboard.putSendable("Reset Gyro", new InstantCommand(() -> new ResetGyro(swerveDrive).schedule()));
		Dashboard.putSendable("Swerve Drive", swerveDrive);
		Dashboard.putSendable("Gyro", swerveDrive.getGyro());
        Dashboard.putSendable("Odometry-Field2D", swerveDrive.getField());
	}
	
	private CommandWrapper[] getAutonCommands () {
		return new CommandWrapper[] {
            new CommandWrapper(() -> new AutoVisionReset(swerveDrive), "AutoVisionReset", swerveDrive),
            new CommandWrapper(() -> new WheelDistances(swerveDrive, 2.5, 0.2), "WheelDistances", swerveDrive),
            new CommandWrapper(() -> new AutoLowGoalTaxi(swerveDrive, hoodedShooter, cargoHandler), "Low goal taxi", swerveDrive),
            new CommandWrapper(() -> new AutoTwoBallLow(swerveDrive, hoodedShooter, cargoHandler, intake), "Two ball low", swerveDrive)
        };
	}
	
	private class CommandWrapper extends InstantCommand {
		
		private final String commandName;
		
		private CommandWrapper (Supplier<Command> commandSupplier, String commandName, Subsystem... requiredSubsystems) {
			super(() -> commandSupplier.get().schedule());
			addRequirements(requiredSubsystems);
			this.commandName = commandName;
		}
		
	}
	
	public Command getAutonomousCommand () {
		if (autonSelector.getSelected() == null) return null;
		return new SequentialCommandGroup(
			new WaitCommand(Dashboard.AUTON_WAIT_PERIOD.get()),
			autonSelector.getSelected());
	}
	
	public void onFirstRobotEnable () {
		CommandScheduler.getInstance().schedule(new ClimberInitialization(climber));
	}
	
}