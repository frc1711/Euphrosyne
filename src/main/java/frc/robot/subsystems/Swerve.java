package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import frc.team1711.swerve.subsystems.AutoSwerveDrive;
import frc.team1711.swerve.util.Angles;

public class Swerve extends AutoSwerveDrive {
	
	private static final double trackToWheelbaseRatio = 1/1;
	
	private final AHRS gyro;
	
	private final SwerveModule
		flWheel,
		frWheel,
		rlWheel,
		rrWheel;
	
	public Swerve (
			AHRS gyro,
			SwerveModule flWheel,
			SwerveModule frWheel,
			SwerveModule rlWheel,
			SwerveModule rrWheel) {
		super(gyro, flWheel, frWheel, rlWheel, rrWheel, trackToWheelbaseRatio);
		this.flWheel = flWheel;
		this.frWheel = frWheel;
		this.rlWheel = rlWheel;
		this.rrWheel = rrWheel;
		
		this.gyro = gyro;
	}
	
	public void configDirectionEncoders () {
		flWheel.configDirectionEncoder();
		frWheel.configDirectionEncoder();
		rlWheel.configDirectionEncoder();
		rrWheel.configDirectionEncoder();
	}
	
	@Override
	public double getGyroAngle () {
		return Angles.wrapDegrees(gyro.getAngle());
	}
	
	@Override
	public void resetGyro () {
		gyro.calibrate();
		gyro.reset();
	}
	
}