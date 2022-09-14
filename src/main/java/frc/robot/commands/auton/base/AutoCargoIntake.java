package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Intake;

public class AutoCargoIntake extends ParallelRaceGroup {
    
    public AutoCargoIntake (CargoHandler cargoHandler, Intake intake, double duration, boolean preventBallPush) {
        super(
            new AutoCargoHandler(cargoHandler, true, x -> false, preventBallPush),
            new AutoIntake(intake, duration, 1));
    }
    
}