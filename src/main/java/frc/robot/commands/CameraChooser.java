package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CameraSystem;

public class CameraChooser extends CommandBase {
	
	private final CameraSystem cameraSystem;
	private final BooleanSupplier previousCamera, nextCamera;
	
	public CameraChooser (CameraSystem cameraSystem, BooleanSupplier previousCamera, BooleanSupplier nextCamera) {
		this.cameraSystem = cameraSystem;
		this.previousCamera = previousCamera;
		this.nextCamera = nextCamera;
		
		addRequirements(cameraSystem);
	}
	
	@Override
	public void execute () {
		if (previousCamera.getAsBoolean()) cameraSystem.previousCamera();
		else if (nextCamera.getAsBoolean()) cameraSystem.nextCamera();
	}
	
	@Override
	public boolean isFinished () {
		return false;
	}
	
}