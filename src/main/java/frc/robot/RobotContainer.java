package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.central.AutoShooterSequence;
import frc.robot.commands.CameraChooser;
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
import frc.team1711.swerve.commands.AutonDrive;
import frc.team1711.swerve.commands.FrameOfReference;

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
	
	public static final ShuffleboardTab controlBoard = Shuffleboard.getTab("Control Board");
	
	private NetworkTableEntry climberOverrideMode;
	
	public RobotContainer () {
		driveController = new XboxController(0);
		centralController = new XboxController(1);
		
		// Camera System
		cameraSystem = CameraSystem.getInstance();
		cameraChooser = new CameraChooser(
			CameraSystem.getInstance(),
			() -> driveController.getAButtonPressed(),
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
			() -> -centralController.getRightY(),					// Extension
			() -> centralController.getLeftY(),						// Rotation
			() -> climberOverrideMode.getBoolean(false));			// Climber override limits
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
		
		// Control Board (Shuffleboard)
		controlBoard.add(new SetSwerveModulePositions(swerveDrive))
			.withPosition(0, 0).withSize(2, 1);
		controlBoard.add(new ResetGyro(swerveDrive))
			.withPosition(0, 1).withSize(2, 1);
		climberOverrideMode = controlBoard.add("Climber Override Mode", false)
			.withWidget(BuiltInWidgets.kField)
			.withPosition(0, 2).withSize(2, 1)
			.getEntry();
		controlBoard.add("Swerve Drive", swerveDrive)
			.withPosition(2, 0).withSize(2, 3);
		controlBoard.add("Gyro", swerveDrive.getGyro())
			.withPosition(4, 0).withSize(2, 3);
	}
	
	public Command getAutonomousCommand () {
		return new SequentialCommandGroup(
			new AutoShooterSequence(shooter, cargoHandler, 1.5),
			new AutonDrive(swerveDrive, 0, 200, 0.1, 5, 0.01, FrameOfReference.ROBOT));
	}
	
	public void onFirstRobotEnable () {
		CommandScheduler.getInstance().schedule(new ClimberInitialization(climber));
	}
	
}