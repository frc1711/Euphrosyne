package frc.robot.commands.auton.base;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Dashboard;
import frc.robot.subsystems.CargoHandler;

public class AutoCargoHandler extends CommandBase {
	
	private final CargoHandler cargoHandler;
	private final double duration, speed;
	
	private final Timer timer = new Timer();
	private final StopOnSensor stopOnSensor;
	
	public AutoCargoHandler (CargoHandler cargoHandler, double duration, double speed) {
		this(cargoHandler, duration, speed, x -> false);
	}
	
	public AutoCargoHandler (CargoHandler cargoHandler, double speed, StopOnSensor stopOnSensor) {
		this(cargoHandler, Double.POSITIVE_INFINITY, speed, stopOnSensor);
	}
	
	private AutoCargoHandler (CargoHandler cargoHandler, double duration, double speed, StopOnSensor stopOnSensor) {
		this.cargoHandler = cargoHandler;
		this.duration = duration;
		this.speed = speed;
		this.stopOnSensor = stopOnSensor;
		addRequirements(cargoHandler);
	}
	
	@Override
	public void initialize () {
		timer.start();
		cargoHandler.stop();
	}
	
	@Override
	public void execute () {
		cargoHandler.setSpeed(speed);
	}
	
	@Override
	public void end (boolean interrupted) {
		cargoHandler.stop();
	}
	
	@Override
	public boolean isFinished () {
        Dashboard.putSendable("Should stop", new Sendable() {
            @Override
            public void initSendable (SendableBuilder builder) {
                builder.addBooleanProperty("s", () -> stopOnSensor.shouldStopCommand(cargoHandler.checkBallAtSensor()), a -> {});
            }
        });
        return timer.get() >= duration || stopOnSensor.shouldStopCommand(cargoHandler.checkBallAtSensor());
	}
	
	@FunctionalInterface
	public interface StopOnSensor {
		public boolean shouldStopCommand (boolean ballTrippingSensor);
	}
	
}