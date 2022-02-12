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
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
	
	private final Joystick driveController, centralController;
	
	private final Swerve swerveDrive;
	private final CargoHandler cargoHandler;
	private final Intake intake;
	private final Shooter shooter;
	
	private final SwerveTeleop swerveTeleop;
	private final CentralSystem centralSystem;
	
	public RobotContainer () {
		driveController = new Joystick(0);
		centralController = new Joystick(1);
		
		// Swerve Teleop
		swerveDrive = Swerve.getInstance();
		swerveTeleop = new SwerveTeleop(
			swerveDrive,
			() -> driveController.getRawAxis(0),	// strafeX
			() -> -driveController.getRawAxis(1),	// strafeY
			() -> driveController.getRawAxis(4));	// steering
		
		swerveDrive.setDefaultCommand(swerveTeleop);
		
		// Central System
		cargoHandler = new CargoHandler(Constants.ballHandler);
		intake = new Intake(Constants.intake);
		shooter = new Shooter(Constants.shooter);
		centralSystem = new CentralSystem(
			cargoHandler, intake, shooter,
			() -> centralController.getRawButton(0),
			() -> centralController.getRawButton(1),
			() -> centralController.getRawButton(2));
		
		cargoHandler.setDefaultCommand(centralSystem);
		
		// SmartDashboard
		SmartDashboard.putData("Swerve", swerveDrive);
		SmartDashboard.putData(new SetSwerveModulePositions(swerveDrive));
		SmartDashboard.putData(new ResetGyro(swerveDrive));
	}
	
	public Command getAutonomousCommand () {
		return null;
	}
	
}