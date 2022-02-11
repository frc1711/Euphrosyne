// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.commands.ClimberCommand;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.SetSwerveModulePositions;
import frc.robot.commands.SwerveTeleop;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
	
	private final Joystick driveController, centralController;
	
	private final Swerve swerveDrive;
	private final Climber climber;
	
	private final SwerveTeleop swerveTeleop;
	private final ClimberCommand climberCommand;
	
	public RobotContainer () {
		driveController = new Joystick(0);
		centralController = new Joystick(1);
		
		swerveDrive = Swerve.getInstance();
		swerveTeleop = new SwerveTeleop(
			swerveDrive,
			() -> driveController.getRawAxis(0),	// strafeX
			() -> -driveController.getRawAxis(1),	// strafeY
			() -> driveController.getRawAxis(4));	// steering
		
		climber = new Climber(Constants.extender, Constants.rotator);
		climberCommand = new ClimberCommand(
			climber,
			() -> applyDeadbandTEMPORARY(centralController.getRawAxis(0)),	// extension
			() -> applyDeadbandTEMPORARY(centralController.getRawAxis(4)));	// rotation
		
		swerveDrive.setDefaultCommand(swerveTeleop);
		climber.setDefaultCommand(climberCommand);
		
		// SmartDashboard
		SmartDashboard.putData("Swerve", swerveDrive);
		SmartDashboard.putData(new SetSwerveModulePositions(swerveDrive));
		SmartDashboard.putData(new ResetGyro(swerveDrive));
	}
	
	// TODO: Get a more permanent solution
	public double applyDeadbandTEMPORARY (double input) {
		if (Math.abs(input) <= 0.08) return 0;
		return input;
	}
	
	public Command getAutonomousCommand () {
		return null;
	}
	
}