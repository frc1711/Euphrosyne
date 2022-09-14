package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoMove;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.FrameOfReference;
import frc.team1711.swerve.util.Vector;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;

public class AutoLowGoalTaxi extends SequentialCommandGroup {
    
    public AutoLowGoalTaxi (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler) {
        super(
            new AutoShooterSequence(shooter, cargoHandler),
            new AutoMove(swerve, new RobotMovement(new Vector(0, 90), FrameOfReference.ROBOT, AutoMove.MOVEMENT_MANNER), RobotTurn.NONE));
    }
    
}