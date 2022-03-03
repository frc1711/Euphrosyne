package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CargoHandler extends SubsystemBase {
	private final CANSparkMax pulley;
	
	public CargoHandler (int ballHandlerID) {
		pulley = new CANSparkMax(ballHandlerID, MotorType.kBrushless);
		pulley.setIdleMode(IdleMode.kBrake);
	}
	
	public void setSpeed (double speed) {
		pulley.set(speed);
	}
	
	public void stop () {
		pulley.set(0);
	}
}