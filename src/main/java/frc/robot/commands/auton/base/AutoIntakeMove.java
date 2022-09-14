package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;

public class AutoIntakeMove extends ParallelRaceGroup {
    
    public AutoIntakeMove (Swerve swerve, CargoHandler cargoHandler, RobotMovement movement, RobotTurn turn) {
        super(
            new AutoMove(swerve, movement, turn),
            new AutoCargoHandler(cargoHandler, true, x -> false, true));
    }
    
}