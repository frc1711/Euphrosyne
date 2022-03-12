package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class AutoLowGoalTaxi extends SequentialCommandGroup {
	
	public AutoLowGoalTaxi (Swerve swerveDrive, Shooter shooter, CargoHandler cargoHandler) {
		super(
			new AutoShooterSequence(shooter, cargoHandler, 1.5, false),
			new AutoTaxi(swerveDrive));
	}
	
}