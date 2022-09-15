package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auton.base.AutoIntake;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.HoodedShooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Swerve;

public class AutoHighGoalTaxi extends SequentialCommandGroup {
    
    public AutoHighGoalTaxi (Swerve swerve, HoodedShooter shooter, CargoHandler cargoHandler, Intake intake) {
        super(
            new AutoTaxi(swerve),
            new AutoShooterSequence(swerve, shooter, cargoHandler, 2),
            new AutoIntake(intake, 1, 1));
    }
    
}