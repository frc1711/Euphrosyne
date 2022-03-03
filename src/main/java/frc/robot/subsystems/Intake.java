package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
	private final CANSparkMax intakeMotor;
	
	public Intake (int intakeID) {
		intakeMotor = new CANSparkMax(intakeID, MotorType.kBrushless);
		intakeMotor.setIdleMode(IdleMode.kBrake);
	}
	
	public void setSpeed (double speed) {
		intakeMotor.set(speed);
	}
	
	public void stop () {
		intakeMotor.set(0);
	}
}