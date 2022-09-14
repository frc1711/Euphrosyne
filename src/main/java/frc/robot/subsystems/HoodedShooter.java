package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Dashboard;
import frc.robot.IDMap;

public class HoodedShooter extends SubsystemBase {
	
	private static HoodedShooter hoodedShooterInstance;
	
	public static HoodedShooter getInstance () {
		if (hoodedShooterInstance == null) hoodedShooterInstance = new HoodedShooter();
		return hoodedShooterInstance;
	}
	
	private final CANSparkMax upperMotor = new CANSparkMax(IDMap.CAN.UPPER_SHOOTER.ID, MotorType.kBrushless);
	private final CANSparkMax lowerMotor = new CANSparkMax(IDMap.CAN.LOWER_SHOOTER.ID, MotorType.kBrushless);
	
	private HoodedShooter () {
		upperMotor.setIdleMode(IdleMode.kCoast);
		lowerMotor.setIdleMode(IdleMode.kCoast);
	}
	
	public void setSpeed (double upper, double lower) {
        if (Dashboard.HOODED_SHOOTER_OVERRIDE_LOOKUP_SPEED.get()) {
            upperMotor.set(Dashboard.HOODED_SHOOTER_UPPER_SPEED.get());
            lowerMotor.set(Dashboard.HOODED_SHOOTER_LOWER_SPEED.get());
        } else {
            upperMotor.set(upper);
            lowerMotor.set(lower);
        }
	}
	
	public void stop () {
		upperMotor.stopMotor();
		lowerMotor.stopMotor();
	}
	
}