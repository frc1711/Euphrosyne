package frc.robot.util;

public class LinearInterpolator {
	
	private final double m, b;
	
	public LinearInterpolator (double x1, double y1, double x2, double y2) {
		m = (y1 - y2) / (x1 - x2);
		b = y1 - m * x1;
	}
	
	/**
	 * Converts an input x value to an output y value
	 */
	public double interpolate (double x) {
		return m*x + b;
	}
	
	/**
	 * Converts an output y value to an input x value
	 */
	public double invInterpolate (double y) {
		return (y - b) / m;
	}
	
}