package frc.robot.commands.auton.base;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.AutonTurn;
import frc.team1711.swerve.commands.FrameOfReference;

public class AutoTurn extends AutonTurn {
	public AutoTurn (Swerve swerveDrive, double direction, double speedMult) {
		super(swerveDrive, direction, speedMult * 0.1, 5, FrameOfReference.ROBOT);
	}
}