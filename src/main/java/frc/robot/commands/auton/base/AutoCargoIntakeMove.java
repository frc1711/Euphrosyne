package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;

public class AutoCargoIntakeMove extends ParallelRaceGroup {
    
    public AutoCargoIntakeMove (Swerve swerve, CargoHandler cargoHandler, Intake intake, RobotMovement movement, RobotTurn turn) {
        super(
            new AutoCargoMove(swerve, cargoHandler, movement, turn),
            new AutoIntake(intake, Double.POSITIVE_INFINITY, 1));
    }
    
}