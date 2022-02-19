// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.commands.central.CentralSystem;
import frc.robot.commands.climber.ClimberCommand;
import frc.robot.commands.climber.ClimberRotationInitialization;
import frc.robot.commands.swerve.ResetGyro;
import frc.robot.commands.swerve.SetSwerveModulePositions;
import frc.robot.commands.swerve.SwerveTeleop;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.SwerveModule;
import frc.team1711.swerve.subsystems.SwerveDrive;

public class RobotContainer {
	
	private static final int
		frontLeftDriveID = 1,
		frontRightDriveID = 3,
		rearLeftDriveID = 5,
		rearRightDriveID = 7,
		
		frontLeftSteerID = 2,
		frontRightSteerID = 4,
		rearLeftSteerID = 6,
		rearRightSteerID = 8,
		
		frontLeftSteerEncoderID = 9,
		frontRightSteerEncoderID = 10,
		rearLeftSteerEncoderID = 11,
		rearRightSteerEncoderID = 12;
	
	private static final int
		intakeID = 13,
		
		cargoHandlerID = 14,
		
		shooterID = 15,
		
		rotatorID = 16,
		extenderID = 17,
		
		leftRotationLimitSwitchID = 1,
		rightRotationLimitSwitchID = 0;
	
	private final XboxController driveController, centralController;
	
	private final Swerve swerveDrive;
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	private final Climber climber;
	
	private final SwerveTeleop swerveTeleop;
	private final CentralSystem centralSystem;
	private final ClimberCommand climberCommand;
	
	private final SwerveDrive.SwerveDrivingSpeeds swerveDrivingSpeeds = new SwerveDrive.SwerveDrivingSpeeds(0.7, 0.3);
	
	public RobotContainer () {
		driveController = new XboxController(0);
		centralController = new XboxController(1);
		
		// Swerve Teleop
		AHRS gyro = new AHRS();
		swerveDrive = new Swerve(
			gyro,
			new SwerveModule("Front Left Module", frontLeftSteerID, frontLeftDriveID, frontLeftSteerEncoderID),
			new SwerveModule("Front Right Module", frontRightSteerID, frontRightDriveID, frontRightSteerEncoderID),
			new SwerveModule("Rear Left Module", rearLeftSteerID, rearLeftDriveID, rearLeftSteerEncoderID),
			new SwerveModule("Rear Right Module", rearRightSteerID, rearRightDriveID, rearRightSteerEncoderID),
			swerveDrivingSpeeds);
		swerveTeleop = new SwerveTeleop(
			swerveDrive,
			() -> driveController.getLeftX(),			// Strafe X
			() -> -driveController.getLeftY(),			// Strafe Y
			() -> driveController.getRightX());			// Steering
		swerveDrive.setDefaultCommand(swerveTeleop);
		
		// Climber Command
		climber = new Climber(
			extenderID,
			rotatorID,
			leftRotationLimitSwitchID,
			rightRotationLimitSwitchID);
		climberCommand = new ClimberCommand(
			climber,
			() -> -centralController.getRightY(),	// Extension
			() -> centralController.getLeftY());	// Rotation
		climber.setDefaultCommand(climberCommand);
		
		// Central System
		cargoHandler = new CargoHandler(cargoHandlerID);
		intake = new Intake(intakeID);
		shooter = new Shooter(shooterID);
		centralSystem = new CentralSystem(
			cargoHandler, intake, shooter,
			() -> centralController.getAButton(),					// CargoHandler
			() -> centralController.getRightTriggerAxis(),			// Intake
			() -> centralController.getLeftTriggerAxis());			// Shooter
		cargoHandler.setDefaultCommand(centralSystem);
		
		// SmartDashboard
		SmartDashboard.putData(new SetSwerveModulePositions(swerveDrive));
		SmartDashboard.putData(new ResetGyro(swerveDrive));
		SmartDashboard.putData(swerveDrive);
		SmartDashboard.putData(gyro);
	}
	
	public Command getAutonomousCommand () {
		return null;
	}
	
	public void onFirstRobotEnable () {
		CommandScheduler.getInstance().schedule(new ClimberRotationInitialization(climber));
	}
	
}