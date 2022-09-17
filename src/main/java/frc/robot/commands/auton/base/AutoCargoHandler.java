package frc.robot.commands.auton.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CargoHandler;

public class AutoCargoHandler extends CommandBase {
	
	private final CargoHandler cargoHandler;
	private final double duration, speed;
	private final boolean preventBallPush;
    
	private final Timer timer = new Timer();
	private final StopOnSensor stopOnSensor;
	
	public AutoCargoHandler (CargoHandler cargoHandler, double duration, boolean forward, boolean preventBallPush) {
		this(cargoHandler, duration, forward, x -> false, preventBallPush);
	}
	
	public AutoCargoHandler (CargoHandler cargoHandler, boolean forward, StopOnSensor stopOnSensor, boolean preventBallPush) {
		this(cargoHandler, Double.POSITIVE_INFINITY, forward, stopOnSensor, preventBallPush);
	}
	
	private AutoCargoHandler (CargoHandler cargoHandler, double duration, boolean forward, StopOnSensor stopOnSensor, boolean preventBallPush) {
		this.cargoHandler = cargoHandler;
		this.duration = duration;
		this.speed = forward ? 1 : -1;
		this.stopOnSensor = stopOnSensor;
        this.preventBallPush = preventBallPush;
		addRequirements(cargoHandler);
	}
	
	@Override
	public void initialize () {
		timer.start();
		cargoHandler.stop();
	}
	
	@Override
	public void execute () {
		cargoHandler.setSpeed(speed, preventBallPush);
	}
	
	@Override
	public void end (boolean interrupted) {
		cargoHandler.stop();
	}
	
	@Override
	public boolean isFinished () {
        return timer.get() >= duration || stopOnSensor.shouldStopCommand(cargoHandler.checkBallAtTopSensor());
	}
	
	@FunctionalInterface
	public interface StopOnSensor {
		public boolean shouldStopCommand (boolean ballTrippingSensor);
	}
	
}