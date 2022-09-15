package frc.robot.commands.auton.base;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.AutonDrive;
import frc.team1711.swerve.util.odometry.MovementManner;
import frc.team1711.swerve.util.odometry.Position;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;
import frc.team1711.swerve.util.odometry.TurnManner;

public class AutoMove extends AutonDrive {
    
    public static final MovementManner.MovementSpeedSupplier MOVE_SPEED_SUPPLIER =
        MovementManner.MovementSpeedSupplier.speedWithSlowdown(0.8, 8*12, 0.08);
    public static final TurnManner.TurnSpeedSupplier TURN_SPEED_SUPPLIER =
        TurnManner.TurnSpeedSupplier.speedWithSlowdown(0.4, 40, 0.08);
    
    public static final MovementManner MOVEMENT_MANNER = new MovementManner(3, MOVE_SPEED_SUPPLIER);
    public static final TurnManner TURN_MANNER = new TurnManner(3, TURN_SPEED_SUPPLIER);
    
    public AutoMove (Swerve swerve, Position position) {
        super(swerve, position, MOVEMENT_MANNER, TURN_MANNER);
    }
    
    public AutoMove (Swerve swerve, RobotMovement movement, RobotTurn turn) {
        super(swerve, movement, turn);
    }
    
}