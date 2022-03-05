package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoDriveIntakeHandler;
import frc.robot.commands.auton.base.AutoIntakeDriveCollect;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class AutoTwoBall extends SequentialCommandGroup {
	
	private final Swerve swerveDrive;
	private final Intake intake;
	private final CargoHandler cargoHandler;
	private double distanceTraveled;
	
	private final AutoShooterSequence shootFirstBall, shootSecondBall;
	private final AutoIntakeDriveCollect driveIntakeCollect;
	private AutoDriveIntakeHandler driveIntakeCargoHandler;
	
	public AutoTwoBall (Swerve swerveDrive, Shooter shooter, Intake intake, CargoHandler cargoHandler, double maxTime) {
		shootFirstBall = new AutoShooterSequence(shooter, cargoHandler, 1.5, false);
		driveIntakeCollect = new AutoIntakeDriveCollect(swerveDrive, cargoHandler, intake, maxTime, x -> { distanceTraveled = x; });
		shootSecondBall = new AutoShooterSequence(shooter, cargoHandler, 1.5, true);
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
			driveIntakeCargoHandler = new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -distanceTraveled, 2);
			driveIntakeCargoHandler.andThen(shootSecondBall).schedule();
		} else {
			cargoHandler.stop();
			swerveDrive.stop();
			intake.stop();
		}
	}
	
}