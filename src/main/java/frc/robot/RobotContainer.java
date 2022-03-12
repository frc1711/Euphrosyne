package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.CameraChooser;
import frc.robot.commands.auton.AutoLowGoalTaxi;
import frc.robot.commands.auton.AutoTaxi;
import frc.robot.commands.central.CentralSystem;
import frc.robot.commands.climber.ClimberCommand;
import frc.robot.commands.climber.ClimberInitialization;
import frc.robot.commands.swerve.ResetGyro;
import frc.robot.commands.swerve.SetSwerveModulePositions;
import frc.robot.commands.swerve.SwerveTeleop;
import frc.robot.subsystems.CameraSystem;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
	
	private final XboxController driveController, centralController;
	
	private final Swerve swerveDrive;
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	private final Climber climber;
	private final CameraSystem cameraSystem;
	
	private final SwerveTeleop swerveTeleop;
	private final CentralSystem centralSystem;
	private final ClimberCommand climberCommand;
	private final CameraChooser cameraChooser;
	
	private SendableChooser<Command> autonSelector;
	
	public RobotContainer () {
		driveController = new XboxController(0);
		centralController = new XboxController(1);
		
		// Camera System
		cameraSystem = CameraSystem.getInstance();
		cameraChooser = new CameraChooser(
			CameraSystem.getInstance(),
			() -> driveController.getAButtonPressed() || centralController.getYButtonPressed(),		// Next camera button
			() -> false);
		cameraSystem.setDefaultCommand(cameraChooser);
		
		// Swerve Teleop
		swerveDrive = Swerve.getInstance();
		swerveTeleop = new SwerveTeleop(
			swerveDrive,
			() -> driveController.getLeftX(),											// Strafe X
			() -> -driveController.getLeftY(),											// Strafe Y
			() -> driveController.getRightX(),											// Steering
			() -> driveController.getRightTriggerAxis() > 0.4,							// Fast mode
			() -> driveController.getLeftTriggerAxis() > 0.4,							// Slow mode
			() -> driveController.getLeftBumper() && driveController.getRightBumper());	// Reset gyro
		swerveDrive.setDefaultCommand(swerveTeleop);
		
		// Climber Command
		climber = Climber.getInstance();
		climberCommand = new ClimberCommand(
			climber,
			() -> -centralController.getRightY(),										// Extension
			() -> centralController.getLeftY(),											// Rotation
			() -> Dashboard.CLIMBER_OVERRIDE_MODE.get(),								// Climber override limits
			(x) -> centralController.setRumble(RumbleType.kLeftRumble, x ? 0.7 : 0));	// Rumble for hitting climber limit
		climber.setDefaultCommand(climberCommand);
		
		// Central System
		cargoHandler = CargoHandler.getInstance();
		intake = Intake.getInstance();
		shooter = Shooter.getInstance();
		centralSystem = new CentralSystem(
			cargoHandler, intake, shooter,
			() -> centralController.getAButton(),					// CargoHandler
			() -> centralController.getRightTriggerAxis(),			// Intake
			() -> centralController.getLeftTriggerAxis(),			// Shooter
			() -> centralController.getRightBumper(),				// Shooter sequence
			() -> centralController.getXButton());					// Reverse mode
		cargoHandler.setDefaultCommand(centralSystem);
		
		// Auton selector
		autonSelector = new SendableChooser<Command>();
		Command[] autonCommands = getAutonCommands();
		for (Command command : autonCommands)
			autonSelector.addOption(command != null ? command.getName() : "No Auton", command);
		autonSelector.setDefaultOption("No Auton", null);
		Dashboard.putSendable("Auton Selector", autonSelector);
		
		// Control Board (Shuffleboard)
		Dashboard.putSendable("Swerve Module Positions", new SetSwerveModulePositions(swerveDrive));
		Dashboard.putSendable("Climber Initialization", new ClimberInitialization(climber));
		Dashboard.putSendable("Reset Gyro", new ResetGyro(swerveDrive));
		Dashboard.putSendable("Swerve Drive", swerveDrive);
		Dashboard.putSendable("Gyro", swerveDrive.getGyro());
	}
	
	private Command[] getAutonCommands () {
		return new Command[] {
			new AutoTaxi(swerveDrive),
			new AutoLowGoalTaxi(swerveDrive, shooter, cargoHandler),
		};
	}
	
	public Command getAutonomousCommand () {
		return new SequentialCommandGroup(
			new WaitCommand(Dashboard.AUTON_WAIT_PERIOD.get()),
			autonSelector.getSelected());
	}
	
	public void onFirstRobotEnable () {
		CommandScheduler.getInstance().schedule(new ClimberInitialization(climber));
	}
	
}