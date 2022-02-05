// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.SetSwerveModulePositions;
import frc.robot.commands.SwerveTeleop;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
	
	private final Swerve swerveDrive;
	private final Joystick driveController;
	private final SwerveTeleop swerveTeleop;
	
	public RobotContainer () {
		driveController = new Joystick(0);
		
		swerveDrive = Swerve.getInstance();
		swerveTeleop = new SwerveTeleop(
			swerveDrive,
			() -> driveController.getRawAxis(0),	// strafeX
			() -> -driveController.getRawAxis(1),	// strafeY
			() -> driveController.getRawAxis(4));	// steering
		
		swerveDrive.setDefaultCommand(swerveTeleop);
		
		// SmartDashboard
		SmartDashboard.putData("Swerve", swerveDrive);
		SmartDashboard.putData(new SetSwerveModulePositions(swerveDrive));
		SmartDashboard.putData(new ResetGyro(swerveDrive));
	}
	
	public Command getAutonomousCommand () {
		return null;
	}
	
}