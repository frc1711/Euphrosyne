package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoDriveIntakeHandler;
import frc.robot.commands.auton.base.AutoIntakeDriveCollect;
import frc.robot.commands.auton.base.AutoShooterSequence;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.robot.util.CommandWrapper;

public class AutoTwoBall extends SequentialCommandGroup {
	
	private final AutoShooterSequence shootFirstBall, shootSecondBall;
	private final AutoIntakeDriveCollect driveIntakeCollect;
	private double distanceTraveled;
	private final CommandWrapper driveIntakeCargoHandler;
	
	public AutoTwoBall (Swerve swerveDrive, Shooter shooter, Intake intake, CargoHandler cargoHandler, double maxTime) {
		shootFirstBall = new AutoShooterSequence(shooter, cargoHandler, 1.5);
		driveIntakeCollect = new AutoIntakeDriveCollect(swerveDrive, cargoHandler, intake, maxTime, x -> distanceTraveled = x);
		driveIntakeCargoHandler = new CommandWrapper(() -> new AutoDriveIntakeHandler(swerveDrive, intake, cargoHandler, -distanceTraveled));
		shootSecondBall = new AutoShooterSequence(shooter, cargoHandler, 1.5);
		addCommands(
			shootFirstBall,				// Shoots the first cargo ball
			driveIntakeCollect,			// Drives in direction of intake, intaking and running cargo handler, stopping once we get a ball
			driveIntakeCargoHandler,	// Drives in the opposite direction the same distance, running the cargo handler
			shootSecondBall);			// Gets the second ballw
	}
	
}