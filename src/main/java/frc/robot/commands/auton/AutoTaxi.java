package frc.robot.commands.auton;

import frc.robot.commands.auton.base.AutoMove;
import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.FrameOfReference;
import frc.team1711.swerve.util.Vector;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;

public class AutoTaxi extends AutoMove {
    
    public AutoTaxi (Swerve swerve) {
        super(swerve, new RobotMovement(new Vector(0, 90), FrameOfReference.ROBOT, AutoMove.MOVEMENT_MANNER), RobotTurn.NONE);
    }
    
}