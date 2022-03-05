package frc.robot.subsystems;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.IDMap;
import frc.robot.RobotContainer;

public class CargoHandler extends SubsystemBase {
	
	private static CargoHandler cargoHandlerInstance;
	
	public static CargoHandler getInstance () {
		if (cargoHandlerInstance == null) cargoHandlerInstance = new CargoHandler();
		return cargoHandlerInstance;
	}
	
	private final CANSparkMax pulley;
	private final ArrayList<Double> currentOverFrames;
	private final int currentOverFramesNumFrames = 5;
	
	private CargoHandler () {
		currentOverFrames = new ArrayList<Double>();
		for (int i = 0; i < currentOverFramesNumFrames; i ++)
			currentOverFrames.add(0.);
		
		pulley = new CANSparkMax(IDMap.CAN.CARGO_HANDLER.ID, MotorType.kBrushless);
		pulley.setIdleMode(IdleMode.kBrake);
		RobotContainer.controlBoard.addNumber("CargoHandler.getCurrent()", this::getAverageCurrent).withPosition(5, 3);
	}
	
	// TODO: TEMPORARY
	public void tempUpdateCurrentOverFrames () {
		currentOverFrames.add(pulley.getOutputCurrent());
		currentOverFrames.remove(0);
	}
	
	public double getAverageCurrent () {
		double total = 0;
		for (Double current : currentOverFrames) total += current;
		return total / currentOverFrames.size();
	}
	
	public void setSpeed (double speed) {
		pulley.set(speed);
	}
	
	public void stop () {
		pulley.set(0);
	}
	
}