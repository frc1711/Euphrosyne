package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1711.swerve.subsystems.GyroSwerveDrive;
import frc.team1711.swerve.subsystems.SwerveDrive;
import frc.team1711.swerve.util.Angles;

public class Swerve extends GyroSwerveDrive {
	
	private static Swerve swerveInstance;
	
	private static final double trackToWheelbaseRatio = 21/18.;
	
	private static final int
		frontLeftSteerID = 6,
		frontRightSteerID = 4,
		rearLeftSteerID = 8,
		rearRightSteerID = 2,
		
		frontLeftDriveID = 5,
		frontRightDriveID = 3,
		rearLeftDriveID = 7,
		rearRightDriveID = 1,
		
		frontLeftSteerEncoderID = 9,
		frontRightSteerEncoderID = 10,
		rearLeftSteerEncoderID = 11,
		rearRightSteerEncoderID = 12;
	
	private static final double
		driveRelativeSpeed = SwerveDrive.driveRelativeSpeedDefault,
		steerRelativeSpeed = SwerveDrive.steerRelativeSpeedDefault;
	
	private final AHRS gyro;
	
	private final SwerveModule
		flWheel,
		frWheel,
		rlWheel,
		rrWheel;
	
	private Swerve (SwerveModule flWheel, SwerveModule frWheel, SwerveModule rlWheel, SwerveModule rrWheel) {
		super(
			flWheel, frWheel, rlWheel, rrWheel,
			trackToWheelbaseRatio);
		this.flWheel = flWheel;
		this.frWheel = frWheel;
		this.rlWheel = rlWheel;
		this.rrWheel = rrWheel;
		
		setDriveRelativeSpeed(driveRelativeSpeed);
		setSteerRelativeSpeed(steerRelativeSpeed);
		
		gyro = new AHRS();
	}
	
	public void displayOrientation () {
		SmartDashboard.putNumber("Gyro Angle", getGyroAngle());
		SmartDashboard.putNumber("Front Left Direction", flWheel.getDirection() % 180);
		SmartDashboard.putNumber("Front Right Direction", frWheel.getDirection() % 180);
		SmartDashboard.putNumber("Rear Left Direction", rlWheel.getDirection() % 180);
		SmartDashboard.putNumber("Rear Right Direction", rrWheel.getDirection() % 180);
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
	
	public static Swerve getInstance () {
		if (swerveInstance == null)
			swerveInstance = new Swerve(
				new SwerveModule("FrontLeft", frontLeftSteerID, frontLeftDriveID, frontLeftSteerEncoderID), // Front left module
				new SwerveModule("FrontRight", frontRightSteerID, frontRightDriveID, frontRightSteerEncoderID), // Front right module
				new SwerveModule("RearLeft", rearLeftSteerID, rearLeftDriveID, rearLeftSteerEncoderID), // Rear left module
				new SwerveModule("RearRight", rearRightSteerID, rearRightDriveID, rearRightSteerEncoderID)); // Rear right module)
		return swerveInstance;
	}
	
}