package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1711.swerve.subsystems.GyroSwerveDrive;
import frc.team1711.swerve.subsystems.SwerveDrive;
import frc.team1711.swerve.util.Angles;

public class Swerve extends GyroSwerveDrive {
	
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
			SwerveModule rrWheel,
			SwerveDrive.SwerveDrivingSpeeds drivingSpeeds) {
		super(gyro, flWheel, frWheel, rlWheel, rrWheel, trackToWheelbaseRatio, drivingSpeeds);
		this.flWheel = flWheel;
		this.frWheel = frWheel;
		this.rlWheel = rlWheel;
		this.rrWheel = rrWheel;
		
		this.gyro = gyro;
	}
	
	public void displayOrientation () {
		// // TODO: Use Sendable swerve modules for this
		// SmartDashboard.putNumber("Front Left Direction", (flWheel.getDirection() + 90) % 180 - 90);
		// SmartDashboard.putNumber("Front Right Direction", (frWheel.getDirection() + 90) % 180 - 90);
		// SmartDashboard.putNumber("Rear Left Direction", (rlWheel.getDirection() + 90) % 180 - 90);
		// SmartDashboard.putNumber("Rear Right Direction", (rrWheel.getDirection() + 90) % 180 - 90);
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