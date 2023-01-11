package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import frc.team1711.swerve.subsystems.AutoSwerveWheel;
import frc.team1711.swerve.util.Angles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class SwerveModule extends AutoSwerveWheel {
		
	private static final double
		steerPIDkp = 2.2,
		steerPIDki = 0,
		steerPIDkd = 0;
	
	private final String name;
	
	private double drivePositionOffset;
	private final PIDController steerPID;
	private double directionAbsoluteOffset;
	private final CANSparkMax driveController, steerController;
	
	private final int steerEncoderID;
	private final CANCoder steerEncoder;
	private final RelativeEncoder driveEncoder;
	
	public SwerveModule (String name, int steerControllerID, int driveControllerID, int steerEncoderID) {
		this.name = name;
		
		driveController = new CANSparkMax(driveControllerID, CANSparkMaxLowLevel.MotorType.kBrushless);
		steerController = new CANSparkMax(steerControllerID, CANSparkMaxLowLevel.MotorType.kBrushless);
		
		driveController.setIdleMode(IdleMode.kBrake);
		steerController.setIdleMode(IdleMode.kBrake);
		
		this.steerEncoderID = steerEncoderID;
		steerEncoder = new CANCoder(steerEncoderID);
		directionAbsoluteOffset = getDirectionAbsoluteOffset();
		steerPID = new PIDController(steerPIDkp, steerPIDki, steerPIDkd);
		
		driveEncoder = driveController.getEncoder();
		drivePositionOffset = 0;
	}
	
	// Direction methods
	@Override
	protected double getDirection () {
		return Angles.wrapDegrees(getRawDirection());
	}
	
	private double getRawDirection () {
		return steerEncoder.getAbsolutePosition() - directionAbsoluteOffset;
	}
	
	// Absolute encoder direction offset
	private double getDirectionAbsoluteOffset () {
		try {
			// Reads absolute position offset from file
			FileReader fileReader = new FileReader(getAbsolutePositionOffsetFileName());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String newOffsetStr = bufferedReader.readLine();
			bufferedReader.close();
			
			// Attempts to convert offset from String to double
			double newDouble = Double.valueOf(newOffsetStr);
			directionAbsoluteOffset = newDouble;
			return newDouble;
		} catch (Exception e) {
			System.out.println(e.getClass().getSimpleName() + ": Could not retrieve encoder absolute offset for ID " + steerEncoderID);
		} return 0;
	}
	
	// Creates a new value for the encoder's direction absolute offset
	// based on the assumption that the wheel is currently facing directly forward
	// and configures the encoders
	private double createNewDirectionAbsoluteOffset () {
		// Makes a file writer so the new value can be written to a file on the RoboRIO
		final double newOffset = steerEncoder.getAbsolutePosition();
		try {
			PrintWriter fileWriter = new PrintWriter(getAbsolutePositionOffsetFileName());
			fileWriter.print(newOffset);
			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not write absolute position offset to RoboRIO for encoder ID " + steerEncoder.getDeviceID());
		} return newOffset;
	}
	
	private String getAbsolutePositionOffsetFileName () {
		return "/home/lvuser/CANCoderID(" + steerEncoder.getDeviceID() + ")AbsolutePositionOffset.txt";
	}
	
	public void configDirectionEncoder () {
		
		directionAbsoluteOffset = createNewDirectionAbsoluteOffset();
		System.out.println(name + " new offset: " + directionAbsoluteOffset);
		
		// Create the cancoder configuration
		CANCoderConfiguration config = new CANCoderConfiguration();
		
		// Use absolute position
		config.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
		
		// Output value from 0 to 360
		config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
		
		// Clockwise is positive displacement
		config.sensorDirection = true;
		
		// Flashes the configuration
		ErrorCode errorCode = steerEncoder.configAllSettings(config, 1000);
		if (errorCode != ErrorCode.OK) {
			System.out.println("Error in configuring " + name + " CANCoder: " + errorCode.name());
		}
	}
	
	// Controlling steering
	@Override
	protected void setDirection (double targetDirection) {
		
		// Gets desired change in direction by wrapping the difference
		// between where we want to be and where we with zero as the center
		double directionChange = Angles.wrapDegreesZeroCenter(targetDirection - getDirection());
		
		// PID loop between 0 (representing current value) and the number
		// of revolutions we want to change the direction by
		double steerSpeed = -steerPID.calculate(0, directionChange / 360);
		
		// Sets steering controller
		steerController.set(steerSpeed);
	}
	
	@Override
	protected void stopSteering () {
		steerController.set(0);
	}
	
	@Override
	protected void setDriveSpeed (double speed) {
		driveController.set(speed);
	}
	
	@Override
	protected double getPositionDifference () {
		return driveEncoder.getPosition() - drivePositionOffset;
	}
	
	@Override
	protected void resetDriveEncoder () {
		drivePositionOffset = driveEncoder.getPosition();
	}
	
}