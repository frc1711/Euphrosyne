package frc.robot.commands.auton;

import frc.robot.subsystems.Swerve;
import frc.team1711.swerve.commands.AutonDrive;
import frc.team1711.swerve.commands.FrameOfReference;
import frc.team1711.swerve.util.Vector;
import frc.team1711.swerve.util.odometry.MovementManner;
import frc.team1711.swerve.util.odometry.Position;
import frc.team1711.swerve.util.odometry.RobotMovement;
import frc.team1711.swerve.util.odometry.RobotTurn;
import frc.team1711.swerve.util.odometry.TurnManner;

public class AutoMove extends AutonDrive {
    
    private static final MovementManner.MovementSpeedSupplier moveSpeedSupplier = MovementManner.MovementSpeedSupplier.speedWithSlowdown(0.8, 5*12, 0.08);
    private static final TurnManner.TurnSpeedSupplier turnSpeedSupplier = TurnManner.TurnSpeedSupplier.speedWithSlowdown(0.4, 40, 0.08);
    
    private static final MovementManner MOVEMENT_MANNER = new MovementManner(1, moveSpeedSupplier);
    private static final TurnManner TURN_MANNER = new TurnManner(3, turnSpeedSupplier);
    
    private static final RobotMovement MOVEMENT = new RobotMovement(new Vector(2*12, 2*12), FrameOfReference.FIELD, MOVEMENT_MANNER);
    private static final RobotTurn TURN = new RobotTurn(-30, FrameOfReference.FIELD, TURN_MANNER);
    
    public AutoMove (Swerve swerve) {
        // super(swerve, MOVEMENT, TURN);
        super(swerve, new Position(Vector.ZERO, 0), MOVEMENT_MANNER, TURN_MANNER);
    }
    
}