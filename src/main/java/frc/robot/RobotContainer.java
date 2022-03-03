package frc.robot;

import com.kauailabs.navx.frc.AHRS;

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
import frc.robot.subsystems.SwerveModule;
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
		cameraSystem = new CameraSystem();
		cameraChooser = new CameraChooser(
			cameraSystem,
			() -> driveController.getAButtonPressed(),
			() -> false);
		cameraSystem.setDefaultCommand(cameraChooser);
		
		// Swerve Teleop
		AHRS gyro = new AHRS();
		swerveDrive = new Swerve(
			gyro,
			new SwerveModule("Front Left Module",
				IDMap.CAN.FRONT_LEFT_STEER.ID,
				IDMap.CAN.FRONT_LEFT_DRIVE.ID,
				IDMap.CAN.FRONT_LEFT_STEER_ENCODER.ID),
			new SwerveModule("Front Right Module",
				IDMap.CAN.FRONT_RIGHT_STEER.ID,
				IDMap.CAN.FRONT_RIGHT_DRIVE.ID,
				IDMap.CAN.FRONT_RIGHT_STEER_ENCODER.ID),
			new SwerveModule("Rear Left Module",
				IDMap.CAN.REAR_LEFT_STEER.ID,
				IDMap.CAN.REAR_LEFT_DRIVE.ID,
				IDMap.CAN.REAR_LEFT_STEER_ENCODER.ID),
			new SwerveModule("Rear Right Module",
				IDMap.CAN.REAR_RIGHT_STEER.ID,
				IDMap.CAN.REAR_RIGHT_DRIVE.ID,
				IDMap.CAN.REAR_RIGHT_STEER_ENCODER.ID));
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
		climber = new Climber(
			IDMap.CAN.CLIMBER_EXTENDER.ID,
			IDMap.CAN.CLIMBER_ROTATOR.ID,
			IDMap.DIO.LEFT_ROTATION_LIMIT_SWITCH.ID,
			IDMap.DIO.RIGHT_ROTATION_LIMIT_SWITCH.ID,
			IDMap.DIO.LEFT_EXTENSION_LIMIT_SWITCH.ID,
			IDMap.DIO.RIGHT_EXTENSION_LIMIT_SWITCH.ID);
		climberCommand = new ClimberCommand(
			climber,
			() -> -centralController.getRightY(),					// Extension
			() -> centralController.getLeftY(),						// Rotation
			() -> climberOverrideMode.getBoolean(false));			// Climber override limits
		climber.setDefaultCommand(climberCommand);
		
		// Central System
		cargoHandler = new CargoHandler(IDMap.CAN.CARGO_HANDLER.ID);
		intake = new Intake(IDMap.CAN.INTAKE.ID);
		shooter = new Shooter(IDMap.CAN.SHOOTER.ID);
		centralSystem = new CentralSystem(
			cargoHandler, intake, shooter,
			() -> centralController.getAButton(),					// CargoHandler
			() -> centralController.getRightTriggerAxis(),			// Intake
			() -> centralController.getLeftTriggerAxis(),			// Shooter
			() -> centralController.getRightBumper(),				// Shooter sequence
			() -> centralController.getXButton());					// Reverse mode
		cargoHandler.setDefaultCommand(centralSystem);
		
		// Control Board (ShuffleBoard)
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
		controlBoard.add("Gyro", gyro)
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