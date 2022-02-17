// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
		
		// TODO: Implement sendable in the various subsystems and use Shuffleboard instead of SmartDashboard 
		SmartDashboard.putData("leftRotationLimitSwitch", leftRotationLimitSwitch);
		SmartDashboard.putData("rightRotationLimitSwitch", rightRotationLimitSwitch);
	}
	
	public void setExtensionSpeed (double speed) {
		extender.set(speed);
	}
	
	public void setRotationSpeed (double speed) {
		// If the fully-wrapped encoder value has been set
		if (fullyWrappedRotationEncoderValueSet) {
			// Make sure the rotation encoder doesn't move further from the fully wrapped state
			// in the positive direction than it should
			if (speed > 0 && rotationEncoder.getPosition() > fullyWrappedRotationEncoderValue + rotationEncoderMaxOffset) {
				rotator.set(0);
				return;
			}
			// and in the negative direction
			if (speed < 0 && rotationEncoder.getPosition() < fullyWrappedRotationEncoderValue - rotationEncoderMaxOffset) {
				rotator.set(0);
				return;
			}
		}
		
		// The limit switch should only be hit if it's fully wrapped (in the positive direction)
		// So if the speed is greater than zero, we shouldn't wrap further if the limit switch is tripped
		if (getRotationLimitSwitch() && speed > 0) {
			rotator.set(0);
			return;
		}
		
		// If all the checks pass, then it can be used
		rotator.set(speed);
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
	
	// TODO: Get rid of this
	public double getIntegratedEncoderValue () {
		return rotator.getEncoder().getPosition();
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