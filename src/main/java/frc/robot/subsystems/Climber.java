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
	// The maximum offset from the fully-wrapped spindle encoder position
	// so that it doesn't wrap in the wrong direction
	private static final double rotationEncoderMaxOffset = 44;
	
	private final CANSparkMax extender, rotator;
	private final RelativeEncoder rotationEncoder;
	private final DigitalInput leftRotationLimitSwitch, rightRotationLimitSwitch;
	
	private boolean fullyWrappedRotationEncoderValueSet = false;
	private double fullyWrappedRotationEncoderValue;
	
	public Climber (
			int extenderID,
			int rotatorID,
			int leftRotationLimitSwitchID,
			int rightRotationLimitSwitchID) {
		extender = new CANSparkMax(extenderID, MotorType.kBrushless);
		rotator = new CANSparkMax(rotatorID, MotorType.kBrushless);
		leftRotationLimitSwitch = new DigitalInput(leftRotationLimitSwitchID);
		rightRotationLimitSwitch = new DigitalInput(rightRotationLimitSwitchID);
		rotationEncoder = rotator.getEncoder();
	}
	
	public void setExtensionSpeed (double speed) {
		extender.set(speed);
	}
	
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
	
	/**
	 * Returns whether or not the rotation limit switches are being pressed.
	 * @return {@code true} if one of the limit switches is being pressed; {@code false} if
	 * both limit switches are not being pressed.
	 */
	public boolean getRotationLimitSwitch () {
		return (!leftRotationLimitSwitch.get()) || (!rightRotationLimitSwitch.get());
	}
	
	public void stop (){
		extender.set(0);
		rotator.set(0);
	}
}