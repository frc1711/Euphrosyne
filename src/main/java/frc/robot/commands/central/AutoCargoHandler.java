// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.central;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CargoHandler;

public class AutoCargoHandler extends CommandBase {
	
	private final CargoHandler cargoHandler;
	private final double duration, speed;
	private final Timer timer;
	
	public AutoCargoHandler (CargoHandler cargoHandler, double duration, double speed) {
		this.cargoHandler = cargoHandler;
		this.duration = duration;
		this.speed = speed;
		
		timer = new Timer();
		addRequirements(cargoHandler);
	}
	
	@Override
	public void initialize () {
		timer.start();
		cargoHandler.stop();
	}
	
	@Override
	public void execute () {
		cargoHandler.setSpeed(speed);
	}
	
	@Override
	public void end (boolean interrupted) {
		cargoHandler.stop();
	}
	
	@Override
	public boolean isFinished () {
		return timer.get() >= duration;
	}
}