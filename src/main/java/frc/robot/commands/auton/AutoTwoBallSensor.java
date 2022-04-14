package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoDriveIntakeHandler;
import frc.robot.commands.auton.base.AutoIntakeDriveCollect;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class AutoTwoBallSensor extends SequentialCommandGroup {
	
	private static final double MAX_DRIVE_TIME = 8;
	
	private final Swerve swerveDrive;
	private final Intake intake;
	private final CargoHandler cargoHandler;
	private double distanceTraveled;
	
	private final AutoShooterSequence shootFirstBall, shootSecondBall;
	private final AutoIntakeDriveCollect driveIntakeCollect;
	private AutoDriveIntakeHandler driveIntakeCargoHandler;
	
	public AutoTwoBallSensor (Swerve swerveDrive, Shooter shooter, Intake intake, CargoHandler cargoHandler) {
		shootFirstBall = new AutoShooterSequence(shooter, cargoHandler, 0.5);
		driveIntakeCollect = new AutoIntakeDriveCollect(swerveDrive, cargoHandler, intake, MAX_DRIVE_TIME, x -> { distanceTraveled = x; }, 1.5);
		shootSecondBall = new AutoShooterSequence(shooter, cargoHandler, 0.5);
		addCommands(
			shootFirstBall,
			driveIntakeCollect);		// Drives in direction of intake, intaking and running cargo handler, stopping once we get a ball
		
		this.swerveDrive = swerveDrive;
		this.intake = intake;
		this.cargoHandler = cargoHandler;
	}
	
	@Override
	public void end (boolean interrupted) {
		if (distanceTraveled != 0) {
			driveIntakeCargoHandler = new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -distanceTraveled, 3);
			driveIntakeCargoHandler.andThen(shootSecondBall).schedule();
		} else {
			cargoHandler.stop();
			swerveDrive.stop();
			intake.stop();
		}
	}
	
}