package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Swerve;

public class AutoLowGoalTaxi extends SequentialCommandGroup {
    
    public AutoLowGoalTaxi (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler) {
        super(
            new AutoShooterSequence(shooter, cargoHandler, 1),
            new AutoTaxi(swerve));
    }
    
}