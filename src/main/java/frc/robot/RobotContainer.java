package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.auton.AutoLowGoalTaxi;
import frc.robot.commands.auton.AutoTaxi;
import frc.robot.commands.auton.AutoTrifecta;
import frc.robot.commands.auton.AutoTwoBallSensor;
import frc.robot.commands.auton.AutoTwoBallWall;
import frc.robot.commands.central.CentralSystem;
import frc.robot.commands.climber.ClimberCommand;
import frc.robot.commands.climber.ClimberInitialization;
import frc.robot.commands.swerve.ResetGyro;
import frc.robot.commands.swerve.SetSwerveModulePositions;
import frc.robot.commands.swerve.SwerveTeleop;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
	
	private final XboxController
		driveController = new XboxController(0),
		centralController = new XboxController(1);
	
	private final Swerve swerveDrive = Swerve.getInstance();
	private final CargoHandler cargoHandler = CargoHandler.getInstance();
	private final Intake intake = Intake.getInstance();
	private final Shooter shooter = Shooter.getInstance();
	private final Climber climber = Climber.getInstance();
	
	// Swerve teleop command
	private final SwerveTeleop swerveTeleop = new SwerveTeleop(
		swerveDrive,
		() -> driveController.getLeftX(),											// Strafe X
		() -> -driveController.getLeftY(),											// Strafe Y
		() -> driveController.getRightX(),											// Steering
		() -> driveController.getRightTriggerAxis() > 0.4,							// Fast mode
		() -> driveController.getLeftTriggerAxis() > 0.4,							// Slow mode
		() -> driveController.getLeftBumper() && driveController.getRightBumper());	// Reset gyro
	
	// Central system command
	private final CentralSystem centralSystem = new CentralSystem(
		cargoHandler, intake, shooter,
		() -> centralController.getAButton(),										// Cargo handler AND intake
		() -> centralController.getBButton(),										// Cargo handler
		() -> centralController.getRightTriggerAxis(),								// Intake
		() -> centralController.getLeftTriggerAxis(),								// Shooter
		() -> centralController.getRightBumper(),									// Shooter sequence
		() -> centralController.getXButton());										// Reverse mode
	
	// Climber command
	private final ClimberCommand climberCommand = new ClimberCommand(
		climber,
		() -> -centralController.getRightY(),										// Extension
		() -> centralController.getLeftY(),											// Rotation
		() -> Dashboard.CLIMBER_OVERRIDE_MODE.get(),								// Climber override limits
		(x) -> centralController.setRumble(RumbleType.kLeftRumble, x ? 0.7 : 0));	// Rumble for hitting climber limit
	
	private final SendableChooser<Command> autonSelector;
	
	public RobotContainer () {
		// Instantiate cameras
		CameraServer.startAutomaticCapture(0);
		CameraServer.startAutomaticCapture(1);
		
		// Automatically run default commands
		swerveDrive.setDefaultCommand(swerveTeleop);
		climber.setDefaultCommand(climberCommand);
		cargoHandler.setDefaultCommand(centralSystem);
		
		// Auton selector
		autonSelector = new SendableChooser<Command>();
		Command[] autonCommands = getAutonCommands();
		for (Command command : autonCommands)
			autonSelector.addOption(command != null ? command.getName() : "No Auton", command);
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
	}
	
	private Command[] getAutonCommands () {
		return new Command[] {
			new AutoTaxi(swerveDrive),
			new AutoLowGoalTaxi(swerveDrive, shooter, cargoHandler),
			new AutoTwoBallSensor(swerveDrive, shooter, intake, cargoHandler, 8),
			new AutoTwoBallWall(swerveDrive, shooter, intake, cargoHandler),
			new AutoTrifecta(swerveDrive, shooter, intake, cargoHandler)
		};
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