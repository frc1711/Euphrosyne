package frc.robot.subsystems;

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
	
	private CargoHandler () {
		pulley = new CANSparkMax(IDMap.CAN.CARGO_HANDLER.ID, MotorType.kBrushless);
		pulley.setIdleMode(IdleMode.kBrake);
		RobotContainer.controlBoard.addNumber("CargoHandler.getCurrent()", () -> pulley.getOutputCurrent()).withPosition(5, 3);
	}
	
	public void setSpeed (double speed) {
		pulley.set(speed);
	}
	
	public void stop () {
		pulley.set(0);
	}
	
}