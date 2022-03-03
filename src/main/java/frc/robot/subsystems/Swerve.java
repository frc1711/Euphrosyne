package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.IDMap;
import frc.team1711.swerve.subsystems.AutoSwerveDrive;
import frc.team1711.swerve.util.Angles;

public class Swerve extends AutoSwerveDrive {
	
	private static Swerve swerveInstance;
	
	public static Swerve getInstance () {
		if (swerveInstance == null) swerveInstance = new Swerve(
			new AHRS(),
			new SwerveModule("Front Left Module",
				IDMap.CAN.FRONT_LEFT_STEER.ID,
				IDMap.CAN.FRONT_LEFT_DRIVE.ID,
				IDMap.CAN.FRONT_LEFT_STEER_ENCODER.ID),
			new SwerveModule("Front Right Module",
				IDMap.CAN.FRONT_RIGHT_STEER.ID,
				IDMap.CAN.FRONT_RIGHT_DRIVE.ID,
				IDMap.CAN.FRONT_RIGHT_STEER_ENCODER.ID),
			new SwerveModule("Rear Left Module",
				IDMap.CAN.REAR_LEFT_STEER.ID,
				IDMap.CAN.REAR_LEFT_DRIVE.ID,
				IDMap.CAN.REAR_LEFT_STEER_ENCODER.ID),
			new SwerveModule("Rear Right Module",
				IDMap.CAN.REAR_RIGHT_STEER.ID,
				IDMap.CAN.REAR_RIGHT_DRIVE.ID,
				IDMap.CAN.REAR_RIGHT_STEER_ENCODER.ID));
		return swerveInstance;
	}
	
	private static final double trackToWheelbaseRatio = 1/1;
	
	private final AHRS gyro;
	
	private final SwerveModule
		flWheel,
		frWheel,
		rlWheel,
		rrWheel;
	
	private Swerve (
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
	
	public Gyro getGyro () {
		return gyro;
	}
	
}