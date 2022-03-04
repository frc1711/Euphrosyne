package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.IDMap;
import frc.robot.util.LinearInterpolator;

public class Climber extends SubsystemBase {
	
	private static Climber climberInstance;
	
	public static Climber getInstance () {
		if (climberInstance == null) climberInstance = new Climber();
		return climberInstance;
	}
	
	// The maximum offsets from the fully-wrapped spindle encoder position
	// so that it doesn't wrap in the wrong direction
	private static final double rotationEncoderMaxOffset = -39;
	private static final double extensionEncoderMaxOffset = 147;
	
	// Positive extension is extending upwards, negative is retracting downwards
	// Positive rotation is pulling into limit switch, negative is pushing away
	private final CANSparkMax extender, rotator;
	private final RelativeEncoder rotationEncoder, extensionEncoder;
	private final DigitalInput
		leftRotationLimitSwitch,
		rightRotationLimitSwitch,
		leftExtensionLimitSwitch,
		rightExtensionLimitSwitch;
	
	private Climber () {
		extender = new CANSparkMax(IDMap.CAN.CLIMBER_EXTENDER.ID, MotorType.kBrushless);
		rotator = new CANSparkMax(IDMap.CAN.CLIMBER_ROTATOR.ID, MotorType.kBrushless);
		leftRotationLimitSwitch = new DigitalInput(IDMap.DIO.LEFT_ROTATION_LIMIT_SWITCH.ID);
		rightRotationLimitSwitch = new DigitalInput(IDMap.DIO.RIGHT_ROTATION_LIMIT_SWITCH.ID);
		leftExtensionLimitSwitch = new DigitalInput(IDMap.DIO.LEFT_EXTENSION_LIMIT_SWITCH.ID);
		rightExtensionLimitSwitch = new DigitalInput(IDMap.DIO.RIGHT_EXTENSION_LIMIT_SWITCH.ID);
		rotationEncoder = rotator.getEncoder();
		extensionEncoder = extender.getEncoder();
	}
	
	/**
	 * Positive extension is extending upwards, negative is retracting downwards
	 */
	public void setExtensionSpeed (double speed) {
		if (speed > 0)	extender.set(checkCanExtendPositive() ? speed : 0);
		else			extender.set(checkCanExtendNegative() ? speed : 0);
	}
	
	public void setExtensionSpeedOverride (double speed) {
		extender.set(speed);
	}
	
	private boolean checkCanExtendPositive () {		
		// Can only extend further in the positive direction if the extension encoder reads a value
		// lesser than the maximum allowed extension value
		return getExtensionPosition() < extensionEncoderMaxOffset;
	}
	
	private boolean checkCanExtendNegative () {
		// Can only retract further down (negative extension speed) if the limit switch isn't tripped
		return !getExtensionLimitSwitch();
	}
	
	/**
	 * Positive rotation is pulling into limit switch, negative is pushing away
	 */
	public void setRotationSpeed (double speed) {
		if (speed > 0)	rotator.set(checkCanRotatePositive() ? speed : 0);
		else			rotator.set(checkCanRotateNegative() ? speed : 0);
	}
	
	public void setRotationSpeedOverride (double speed) {
		rotator.set(speed);
	}
	
	private boolean checkCanRotatePositive () {
		// Can only rotate further in the positive direction if the rotation limit switch isn't tripped
		return !getRotationLimitSwitch();
	}
	
	private boolean checkCanRotateNegative () {
		// Can only rotate further in the negative direction if the rotation encoder reads a value
		// greater than the minimum allowed rotational value
		return getRotationPosition() > rotationEncoderMaxOffset;
	}
	
	/**
	 * Should be called when the rotation limit switch is first hit
	 */
	private double rotationEncoderZero;
	public void rotationEncoderReset () {
		rotationEncoderZero = rotationEncoder.getPosition();
	}
	
	private double getRotationPosition () {
		return rotationEncoder.getPosition() - rotationEncoderZero;
	}
	
	/**
	 * Should be called when the extension limit switch is first hit
	 */
	private double extensionEncoderZero;
	public void extensionEncoderReset () {
		extensionEncoderZero = extensionEncoder.getPosition();
	}
	
	private double getExtensionPosition () {
		return extensionEncoder.getPosition() - extensionEncoderZero;
	}
	
	// When the rotation limit switch is being tripped (encoder position of 0), the climber is at 92 degrees.
	// When the rotation encoder is at its maximum offset value, the climber is at 63 degrees.
	private static final LinearInterpolator rotationEncoderToAngle = new LinearInterpolator(0, 92.5, rotationEncoderMaxOffset, 64);
	public double getRotationDegrees () {
		return rotationEncoderToAngle.interpolate(getRotationPosition());
	}
	
	// When the extension limit switch is being tripped (encoder position of 0), the climber is extended to 40
	// When the extension encoder is at its maximum offset va\][plue, the climber is extended to 63.5
	private static final LinearInterpolator extensionEncoderToInches = new LinearInterpolator(0, 40, extensionEncoderMaxOffset, 64);
	public double getExtensionHeightInches () {
		return extensionEncoderToInches.interpolate(getExtensionPosition());
	}
	
	/**
	 * Returns whether or not the rotation limit switches are being pressed.
	 * @return {@code true} if one of the limit switches is being pressed; {@code false} if
	 * both limit switches are not being pressed.
	 */
	public boolean getRotationLimitSwitch () {
		return leftRotationLimitSwitch.get() || rightRotationLimitSwitch.get();
	}
	
	public boolean getExtensionLimitSwitch () {
		return leftExtensionLimitSwitch.get() || rightExtensionLimitSwitch.get();
	}
	
	public void stop (){
		extender.set(0);
		rotator.set(0);
	}
	
}