// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {
  /** Creates a new Climber. */
  public final CANSparkMax
  extender,
  rotator;
  public Climber() {
    extender = new CANSparkMax(Constants.extender, MotorType.kBrushless);
    rotator = new CANSparkMax(Constants.rotator, MotorType.kBrushless);
  }
  public void stop(){
    extender.set(0);
    rotator.set(0);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
