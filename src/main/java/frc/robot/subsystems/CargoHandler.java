package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.IDMap;

public class CargoHandler extends SubsystemBase {
	
	private static CargoHandler cargoHandlerInstance;
	
	public static CargoHandler getInstance () {
		if (cargoHandlerInstance == null) cargoHandlerInstance = new CargoHandler();
		return cargoHandlerInstance;
	}
	
	private final DigitalInput topProximitySensor = new DigitalInput(IDMap.DIO.TOP_PROXIMITY_SENSOR.ID);
	
	private CargoHandler () {
		// TODO: FIX THIS
		// pulley = new CANSparkMax(IDMap.CAN.CARGO_HANDLER.ID, MotorType.kBrushless);
		// pulley.setIdleMode(IdleMode.kBrake);
	}
	
	public boolean checkBallAtSensor () {
		return !topProximitySensor.get();
	}
	
	public void setSpeed (double speed) {
		// pulley.set(speed);
	}
	
	public void stop () {
		// pulley.set(0);
	}
	
	public double getCurrent () {
		return 0; // pulley.getOutputCurrent();
	}
	
}