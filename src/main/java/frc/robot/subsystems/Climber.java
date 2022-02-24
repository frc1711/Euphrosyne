// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
	
	// The maximum offsets from the fully-wrapped spindle encoder position
	// so that it doesn't wrap in the wrong direction
	private static final double rotationEncoderMaxOffset = 39;
	private static final double extensionEncoderMaxOffset = 149.2;
	
	// Positive extension is extending upwards, negative is retracting downwards
	// Positive rotation is pulling into limit switch, negative is pushing away
	private final CANSparkMax extender, rotator;
	private final RelativeEncoder rotationEncoder, extensionEncoder;
	private final DigitalInput
		leftRotationLimitSwitch,
		rightRotationLimitSwitch,
		leftExtensionLimitSwitch,
		rightExtensionLimitSwitch;
	
	// Whether or not fullyWrappedEncoderValues have been set
	private boolean
		fullyWrappedRotationEncoderValueSet = false,
		fullyWrappedExtensionEncoderValueSet = false;
	
	// The encoder position values for when the spindles are fully reeled in
	// Used to make sure the rotation and extension spindles don't fully unwrap
	// then wrap around in the other direction
	private double
		fullyWrappedRotationEncoderValue,
		fullyWrappedExtensionEncoderValue;
	
	public Climber (
			int extenderID,
			int rotatorID,
			int leftRotationLimitSwitchID,
			int rightRotationLimitSwitchID,
			int leftExtensionLimitSwitchID,
			int rightExtensionLimitSwitchID) {
		extender = new CANSparkMax(extenderID, MotorType.kBrushless);
		rotator = new CANSparkMax(rotatorID, MotorType.kBrushless);
		leftRotationLimitSwitch = new DigitalInput(leftRotationLimitSwitchID);
		rightRotationLimitSwitch = new DigitalInput(rightRotationLimitSwitchID);
		leftExtensionLimitSwitch = new DigitalInput(leftExtensionLimitSwitchID);
		rightExtensionLimitSwitch = new DigitalInput(rightExtensionLimitSwitchID);
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
	
	private boolean checkCanExtendPositive () {
		// If the fully wrapped extension encoder value is not set, then it can extend in the positive
		// direction (it should be set though)
		if (!fullyWrappedExtensionEncoderValueSet) return true;
		
		// Can only extend further in the positive direction if the extension encoder reads a value
		// lesser than the maximum allowed extension value
		return extensionEncoder.getPosition() < fullyWrappedExtensionEncoderValue + extensionEncoderMaxOffset;
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
	
	private boolean checkCanRotatePositive () {
		// Can only rotate further in the positive direction if the rotation limit switch isn't tripped
		return !getRotationLimitSwitch();
	}
	
	private boolean checkCanRotateNegative () {
		// If the fully wrapped rotaton encoder value is not set, then it can rotate in the negative
		// direction (it should be set though)
		if (!fullyWrappedRotationEncoderValueSet) return true;
		
		// Can only rotate further in the negative direction if the rotation encoder reads a value
		// greater than the minimum allowed rotational value
		return rotationEncoder.getPosition() > fullyWrappedRotationEncoderValue - rotationEncoderMaxOffset;
	}
	
	/**
	 * Used to mark when the climber is reeled in all the way (in the positive direction)
	 * to the point where it hits the limit switch, so that the encoder value can be used
	 * to prevent the climber moving too far in the negative direction such that it fully
	 * unspools and begins to rewrap in the wrong direction.
	 */
	public void setFullyWrappedRotationMarker () {
		fullyWrappedRotationEncoderValue = rotationEncoder.getPosition();
		fullyWrappedRotationEncoderValueSet = true;
	}
	
	public void unsetFullyWrappedRotationMarker () {
		fullyWrappedRotationEncoderValueSet = false;
	}
	
	public void setFullyWrappedExtensionMarker () {
		fullyWrappedExtensionEncoderValue = extensionEncoder.getPosition();
		fullyWrappedExtensionEncoderValueSet = true;
	}
	
	public void unsetFullyWrappedExtensionMarker () {
		fullyWrappedExtensionEncoderValueSet = false;
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