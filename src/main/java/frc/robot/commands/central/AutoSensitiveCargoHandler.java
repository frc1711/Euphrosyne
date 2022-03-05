package frc.robot.commands.central;

import java.util.ArrayList;

import frc.robot.subsystems.CargoHandler;

public class AutoSensitiveCargoHandler extends AutoCargoHandler {
	
	private static final int
		FRAME_COUNT = 5, // Number of frames averaged in finding the current
		DEADZONE_FRAMES = 8; // Number of frames that aren't counted at the beginning (current is unnaturally high at start)
	private static final double CURRENT_CUTOFF = 5; // Mininum current (in amps) for tripping the sensing of the cargo ball
	
	private final ArrayList<Double> currents = new ArrayList<Double>();
	private final CargoHandler cargoHandler;
	private final OnCargoBall onCargoBall;
	private int frameNum = 0;
	
	public AutoSensitiveCargoHandler (CargoHandler cargoHandler, double duration, double speed, OnCargoBall onCargoBall) {
		super(cargoHandler, duration, speed);
		this.cargoHandler = cargoHandler;
		this.onCargoBall = onCargoBall;
		
		// Fills the currents list with zeroes
		for (int i = 0; i < FRAME_COUNT; i ++)
			currents.add(0.);
	}
	
	@Override
	public void execute () {
		super.execute();
		frameNum ++;
		
		// Add another current for this frame, removing the last one
		currents.add(cargoHandler.getCurrent());
		currents.remove(0);
		
		// If the frame deadzone has passed and the current cutoff has been exceeded
		if (frameNum > DEADZONE_FRAMES && getAverageCurrent() > CURRENT_CUTOFF)
			onCargoBall.onCargoBall();
	}
	
	private double getAverageCurrent () {
		double total = 0;
		for (Double current : currents) total += current;
		return total / currents.size();
	}
	
	@FunctionalInterface
	public interface OnCargoBall {
		public void onCargoBall ();
	}
	
}