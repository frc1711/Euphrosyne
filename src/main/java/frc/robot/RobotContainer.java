// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.commands.CentralSystem;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.SetSwerveModulePositions;
import frc.robot.commands.SwerveTeleop;
import frc.robot.commands.ClimberCommand;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
	
	private static final int
		intakeID = 13,
		
		cargoHandlerID = 14,
		
		shooterID = 15,
		
		rotatorID = 16,
		extenderID = 17;
	
	private final Joystick driveController, centralController;
	
	private final Swerve swerveDrive;
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	private final Climber climber;
	
	private final SwerveTeleop swerveTeleop;
	private final CentralSystem centralSystem;
	private final ClimberCommand climberCommand;
	
	public RobotContainer () {
		driveController = new Joystick(0);
		centralController = new Joystick(1);
		
		// Swerve Teleop
		swerveDrive = Swerve.getInstance();
		swerveTeleop = new SwerveTeleop(
			swerveDrive,
			() -> driveController.getRawAxis(0),								// Strafe X:		Left joystick X
			() -> -driveController.getRawAxis(1),								// Strafe Y:		Left joystick Y
			() -> driveController.getRawAxis(4));								// Steering:		Right joystick X
		swerveDrive.setDefaultCommand(swerveTeleop);
		
		// Climber Command
		climber = new Climber(extenderID, rotatorID);
		climberCommand = new ClimberCommand(
			climber,
			() -> applyDeadbandTEMPORARY(centralController.getRawAxis(4)),		// Extension:		Right joystick X
			() -> applyDeadbandTEMPORARY(-centralController.getRawAxis(0)));	// Rotation:		Left joystick X
		climber.setDefaultCommand(climberCommand);
		
		// Central System
		cargoHandler = new CargoHandler(cargoHandlerID);
		intake = new Intake(intakeID);
		shooter = new Shooter(shooterID);
		centralSystem = new CentralSystem(
			cargoHandler, intake, shooter,
			() -> centralController.getRawButton(1),							// CargoHandler:	A
			() -> centralController.getRawButton(2),							// Intake:			B
			() -> centralController.getRawButton(3));							// Shooter:			X
		cargoHandler.setDefaultCommand(centralSystem);
		
		// SmartDashboard
		SmartDashboard.putData("Swerve", swerveDrive);
		SmartDashboard.putData(new SetSwerveModulePositions(swerveDrive));
		SmartDashboard.putData(new ResetGyro(swerveDrive));
	}
	
	// TODO: Get a more permanent solution
	public double applyDeadbandTEMPORARY (double input) {
		if (Math.abs(input) <= 0.12) return 0;
		return input;
	}
	
	public Command getAutonomousCommand () {
		return null;
	}
	
}