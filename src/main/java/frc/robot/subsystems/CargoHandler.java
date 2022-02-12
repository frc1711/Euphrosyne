// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CargoHandler extends SubsystemBase {
	private final CANSparkMax pulley;
	
	public CargoHandler (int ballHandlerID) {
		pulley = new CANSparkMax(ballHandlerID, MotorType.kBrushless);
	}
	
	public void setSpeed (double speed) {
		pulley.set(speed);
	}
	
	public void stop () {
		pulley.set(0);
	}
}