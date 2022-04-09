package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoDrive;
import frc.robot.commands.auton.base.AutoDriveIntakeHandler;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.commands.auton.base.AutoTurn;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class AutoTwoBallWall extends SequentialCommandGroup {
	
	public AutoTwoBallWall (Swerve swerveDrive, Shooter shooter, Intake intake, CargoHandler cargoHandler) {
		super(
			new AutoShooterSequence(shooter, cargoHandler, 0.5),
			new AutoDrive(swerveDrive, 300, 2),
			new AutoTurn(swerveDrive, -28, 2, false),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, 230, 2),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -230, 2),
			new AutoTurn(swerveDrive, 28, 2, false),
			new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -300, 2),
			new AutoShooterSequence(shooter, cargoHandler, 0.5));
	}
	
}