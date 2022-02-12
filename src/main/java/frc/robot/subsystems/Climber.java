// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
	public final CANSparkMax extender, rotator;
	
	public Climber (int extenderID, int rotatorID) {
		extender = new CANSparkMax(extenderID, MotorType.kBrushless);
		rotator = new CANSparkMax(rotatorID, MotorType.kBrushless);
	}
	
	public void stop (){
		extender.set(0);
		rotator.set(0);
	}
}