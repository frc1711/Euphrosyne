package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoCargoHandler;
import frc.robot.commands.auton.base.AutoCargoIntake;
import frc.robot.commands.auton.base.AutoCargoIntakeMove;
import frc.robot.commands.auton.base.AutoIntake;
import frc.robot.commands.auton.base.AutoMove;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.FrameOfReference;
import frc.team1711.swerve.util.Vector;
import frc.team1711.swerve.util.odometry.Position;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;

public class AutoTwoBallHigh extends SequentialCommandGroup {
    
    private static final double
        Y_BALL_DIST_TO_HUB = 116.17,
        X_BALL_DIST_TO_HUB = -24,
        ROBOT_LENGTH = 30;
    
    public AutoTwoBallHigh (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler, Intake intake) {
        super(
            new AutoCargoIntakeMove(swerve, cargoHandler, intake, new RobotMovement(
                new Vector(X_BALL_DIST_TO_HUB, Y_BALL_DIST_TO_HUB - ROBOT_LENGTH),
                FrameOfReference.ROBOT, AutoMove.MOVEMENT_MANNER), RobotTurn.NONE),
            new AutoCargoIntake(cargoHandler, intake, 3, true),
            new AutoShooterSequence(swerve, shooter, cargoHandler, 2));
    }
    
}