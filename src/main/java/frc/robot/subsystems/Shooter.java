package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.IDMap;

public class Shooter extends SubsystemBase {
	
	private static Shooter shooterInstance;
	
	public static Shooter getInstance () {
		if (shooterInstance == null) shooterInstance = new Shooter();
		return shooterInstance;
	}
	
	private final CANSparkMax shooter = new CANSparkMax(IDMap.CAN.SHOOTER.ID, MotorType.kBrushless);
	private final RelativeEncoder shooterEncoder = shooter.getEncoder();
	
	public Shooter () {
		shooter.setIdleMode(IdleMode.kCoast);
	}
	
	public double getSpeed () {
		return shooterEncoder.getVelocity();
	}
	
	public void setSpeed (double speed) {
		shooter.set(speed);
	}
	
	public void stop () {
		shooter.set(0);
	}
	
}