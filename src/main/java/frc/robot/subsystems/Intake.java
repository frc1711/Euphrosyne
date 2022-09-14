package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Dashboard;
import frc.robot.IDMap;

public class Intake extends SubsystemBase {
	
	private static Intake intakeInstance;
	
	public static Intake getInstance () {
		if (intakeInstance == null) intakeInstance = new Intake();
		return intakeInstance;
	}
	
	private final CANSparkMax intakeMotor;
	
	private Intake () {
		intakeMotor = new CANSparkMax(IDMap.CAN.INTAKE.ID, MotorType.kBrushless);
		intakeMotor.setIdleMode(IdleMode.kBrake);
	}
	
	public void setSpeed (double speed) {
		intakeMotor.set(speed * Dashboard.INTAKE_MAX_SPEED.get());
	}
	
	public void stop () {
		intakeMotor.set(0);
	}
	
}