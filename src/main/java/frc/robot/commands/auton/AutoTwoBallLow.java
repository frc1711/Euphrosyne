package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoCargoHandler;
import frc.robot.commands.auton.base.AutoIntakeMove;
import frc.robot.commands.auton.base.AutoMove;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.FrameOfReference;
import frc.team1711.swerve.util.Vector;
import frc.team1711.swerve.util.odometry.Position;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;

public class AutoTwoBallLow extends SequentialCommandGroup {
    
    private static final double
        Y_BALL_DIST_TO_HUB = 116.17,
        X_BALL_DIST_TO_HUB = -24,
        ROBOT_LENGTH = 30;
    
    public AutoTwoBallLow (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler) {
        super(
            // new AutoShooterSequence(shooter, cargoHandler),
            new AutoIntakeMove(swerve, cargoHandler, new RobotMovement(
                new Vector(X_BALL_DIST_TO_HUB, Y_BALL_DIST_TO_HUB - ROBOT_LENGTH),
                FrameOfReference.ROBOT, AutoMove.MOVEMENT_MANNER), RobotTurn.NONE),
            new AutoCargoHandler(cargoHandler, 0.5, true, true),
            new AutoMove(swerve, new Position(Vector.ZERO, 0)));
    }
    
}