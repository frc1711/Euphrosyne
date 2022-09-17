package frc.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionHandler {
	
	// Offsets measured in inches. +Y = forward (shooter side), +Z = upward
	// (x=0,y=0,z=0) = X center of the robot frame, on the floor, at the front of the frame in the direction of the shooter
	private static final double
		PITCH = 33, // Limelight on bar is 35 deg, bar is 10 deg backwards
		Y_OFFSET = 21,
		Z_OFFSET = 39,
		
        HUB_DIAMETER = 53.38,
		HUB_TAPE_HEIGHT = 8*12 + 7;
	
	private static VisionHandler visionHandlerInstance;
	
	private final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
	private final NetworkTableEntry
		targetX = limelightTable.getEntry("tx"),
		targetY = limelightTable.getEntry("ty"),
		targetExists = limelightTable.getEntry("tv");
	
	public static VisionHandler getInstance () {
		if (visionHandlerInstance == null) {
			visionHandlerInstance = new VisionHandler();
		} return visionHandlerInstance;
	}
	
	private VisionHandler () { }
	
	/**
	 * Gets the target's x angle relative to the camera.
	 * @return The angle the limelight would have to turn to the right, in degrees, in order for the target to have an x coordinate
	 * of 0px in the limelight's camera.
	 */
	public double getTargetX () {
		return targetX.getDouble(0);
	}
	
	/**
	 * Gets the target's y angle relative to the camera.
	 * @return The angle the limelight would have to turn upwards, in degrees, in order for the target to have a y coordinate
	 * of 0px in the limelight's camera.
	 */
	public double getTargetY () {
		return targetY.getDouble(0);
	}
	
	public double getDistanceFromHub () {
		if (!getTargetExists()) return 0; // If the target cannot be seen, return 0
		
		// Complex math stuff to get the distance (don't worry about it)
		return HUB_DIAMETER / 2 + (HUB_TAPE_HEIGHT - Z_OFFSET) / Math.tan(Math.toRadians(PITCH + getTargetY())) - Y_OFFSET;
	}
	
	public boolean getTargetExists () {
		return targetExists.getDouble(0) == 1;
	}
	
}