package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team1711.swerve.util.Vector;
import frc.team1711.swerve.util.odometry.Position;
import frc.team1711.swerve.util.odometry.TurnManner.TurnSpeedSupplier;
import frc.robot.subsystems.Swerve;
import frc.robot.util.VisionHandler;

public class AutoVisionReset extends SequentialCommandGroup {
    
    public AutoVisionReset (Swerve swerve) {
        super(new AutoAimAtVisionTarget(swerve), new WaitCommand(0.1), new AutoResetOdometry(swerve));
    }
    
    private static class AutoAimAtVisionTarget extends CommandBase {
        
        private static final TurnSpeedSupplier TURN_SPEED_SUPPLIER = TurnSpeedSupplier.speedWithSlowdown(0.15, 10, 0.02);
        private static final double MARGIN_OF_ERROR = 0.4; // This has to be really small for accurate limelight readings
        // The number of frames the direction needs to be in the margin of error before the auton stops:
        private static final int NUM_FRAMES_IN_MARGIN = 8;
        
        private final VisionHandler visionHandler = VisionHandler.getInstance();
        private final Swerve swerve;
        
        private int numFramesInMargin = 0;
        
        private AutoAimAtVisionTarget (Swerve swerve) {
            this.swerve = swerve;
            addRequirements(swerve);
        }
        
        @Override
        public void initialize () {
            swerve.stop();
        }
        
        @Override
        public void execute () {
            if (visionHandler.getTargetExists()) {
                swerve.autoDrive(0, 0, getTurnVelocity());
            } else swerve.stop();
            
            if (Math.abs(visionHandler.getTargetX()) < MARGIN_OF_ERROR) numFramesInMargin ++;
            else numFramesInMargin = 0;
        }
        
        private double getTurnVelocity () {
            double targetX = visionHandler.getTargetX();
            double speed = TURN_SPEED_SUPPLIER.getSpeed(Math.abs(targetX));
            return targetX > 0 ? speed : -speed;
        }
        
        @Override
        public void end (boolean interrupted) {
            swerve.stop();
        }
        
        @Override
        public boolean isFinished () {
            return numFramesInMargin >= NUM_FRAMES_IN_MARGIN;
        }
        
    }
    
    private static class AutoResetOdometry extends InstantCommand {
        
        private AutoResetOdometry (Swerve swerve) {
            super(() -> swerve.resetPosition(getPosition(swerve, VisionHandler.getInstance())), swerve);
        }
        
        private static Position getPosition (Swerve swerve, VisionHandler visionHandler) {
            // Direction is still correct -- assume robot is pointed directly away from the hub (i.e. the intake is pointed
            // away and the limelight is pointed straight at the hub). Use this knowledge plus distance from the hub to get
            // position of the robot on the field 
            final double direction = swerve.getPosition().getDirection();
            
            // Direction is negated because the limelight is facing AWAY from the front of the robot but TOWARD the hub
            final Vector location = Vector.fromPolarDegrees(-direction, visionHandler.getDistanceFromHub());
            
            // Return the final position
            return new Position(location, direction);
        }
        
    }
    
}