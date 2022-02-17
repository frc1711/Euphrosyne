// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
	private final CANSparkMax intakeMotor;
	private double s = 0;
	
	public Intake (int intakeID) {
		intakeMotor = new CANSparkMax(intakeID, MotorType.kBrushless);
	}
	
	public void setSpeed (double speed) {
		intakeMotor.set(speed);
		s = speed;
	}
	
	public void stop () {
		intakeMotor.set(0);
		s = 0;
	}
	
	// TODO: Temporary method
	public double getSpeedTemp () {
		return s;
	}
}