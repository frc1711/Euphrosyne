package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.IDMap;
import frc.team1711.swerve.subsystems.AutoSwerveDrive;
import frc.team1711.swerve.util.Angles;
import frc.team1711.swerve.util.odometry.Position;

public class Swerve extends AutoSwerveDrive {
	
    private static final double INCHES_TO_METERS = 0.0254;
    private final Field2d field = new Field2d();
    
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
    
    @Override
    public void autoDrive (double strafeX, double strafeY, double steering) {
        field.setRobotPose(getPositionAsPose());
        super.autoDrive(strafeX, strafeY, steering);
    }
	
	public void configDirectionEncoders () {
		flWheel.configDirectionEncoder();
		frWheel.configDirectionEncoder();
		rlWheel.configDirectionEncoder();
		rrWheel.configDirectionEncoder();
	}
    
    @Override
    public void initSendable (SendableBuilder builder) {
        builder.addDoubleProperty("Front Left Distance", () -> flWheel.getEncoderDistance(), s -> {});
        builder.addDoubleProperty("Front Right Distance", () -> frWheel.getEncoderDistance(), s -> {});
        builder.addDoubleProperty("Rear Left Distance", () -> rlWheel.getEncoderDistance(), s -> {});
        builder.addDoubleProperty("Rear Right Distance", () -> rrWheel.getEncoderDistance(), s -> {});
        super.initSendable(builder);
    }
	
	public AHRS getGyro () {
		return gyro;
	}
    
    public Pose2d getPositionAsPose () {
        return positionToPose(getPosition());
    }
    
    public Field2d getField () {
        return field;
    }
    
    private static Pose2d positionToPose (Position position) {
        final double
            x = position.getLocation().getX(),
            y = position.getLocation().getY();
        
        return new Pose2d(x*INCHES_TO_METERS, y*INCHES_TO_METERS, new Rotation2d(Angles.degreesToRadians(position.getDirection())));
    }
	
}