// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class CargoHandler extends SubsystemBase {
	private final CANSparkMax pulley;
	
	public CargoHandler () {
		pulley = new CANSparkMax(Constants.ballHandler, MotorType.kBrushless);
	}
	
	public void stop (){
		pulley.set(0);
	}
}