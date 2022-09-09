package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.SwerveModule;
import frc.team1711.swerve.util.odometry.Position;

public class WheelDistances extends CommandBase {
    
    private final Swerve swerve;
    private final double speed;
    
    private Position initialPosition;
    
    private int stage = 0;
    private double secondsToDrive;
    
    public WheelDistances (Swerve swerve, double secondsToDrive, double speed) {
        this.swerve = swerve;
        this.speed = speed;
        this.secondsToDrive = secondsToDrive;
        addRequirements(swerve);
    }
    
    @Override
    public void initialize () {
        swerve.stop();
    }
    
    @Override
    public void execute () {
        if (stage == 0) {
            
            // Turn all the wheels directly forward
            if (swerve.steerAllWithinRange(0, SwerveModule.DIRECTION_MARGIN_OF_ERROR)) stage ++;
            
        } else if (stage == 1) {
            
            // Get initial position if we don't already have it
            if (initialPosition == null) initialPosition = swerve.getPosition();
            
            // Drive the robot directly 
            swerve.steerAndDriveAll(0, speed);
            
            secondsToDrive -= 0.02; // around 20ms per execute frame
            if (secondsToDrive <= 0) stage ++;
            
        } else if (stage == 2) {
            
            // Wait until robot completely stops, around 1 sec
            secondsToDrive -= 0.02;
            if (secondsToDrive <= -1) stage ++;
            
            swerve.stop();
            
        } else if (stage == 3) {
            
            // Print position difference
            double dist = swerve.getPosition().distanceFrom(initialPosition);
            System.out.println("Measured distance: " + dist);
            stage ++;
            
            swerve.stop();
            
        }
    }
    
    @Override
    public void end (boolean interrupted) {
        swerve.stop();
    }
    
    @Override
    public boolean isFinished () {
        return stage == 4;
    }
    
}