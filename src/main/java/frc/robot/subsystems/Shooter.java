package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
	private final CANSparkMax shooter;
	
	public Shooter (int shooterID) {
		shooter = new CANSparkMax(shooterID, MotorType.kBrushless);
		shooter.setIdleMode(IdleMode.kCoast);
	}
	
	public void setSpeed (double speed) {
		shooter.set(speed);
	}
	
	public void stop () {
		shooter.set(0);
	}
}