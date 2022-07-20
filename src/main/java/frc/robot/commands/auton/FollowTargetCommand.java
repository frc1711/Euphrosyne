package frc.robot.commands.auton;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Swerve;

public class FollowTargetCommand extends CommandBase {
	
	private static final double
		CLOCKWISE_TURN_SPEED = -0.5,
		FORWARD_MOVE_SPEED = 0.15,
		CHASE_SPEED = 0.6,
		MAX_TURN_SPEED_DISTANCE = 20,
		TARGET_DISTANCE_MARGIN = 4;
	
	private final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
	private final NetworkTableEntry
		targetHor = limelightTable.getEntry("thor"),
		targetVert = limelightTable.getEntry("tvert"),
		targetX = limelightTable.getEntry("tx"),
		targetExists = limelightTable.getEntry("tv");
	
	private final Swerve swerveDrive;
	private int counter = 0;
	
	public FollowTargetCommand (Swerve swerveDrive) {
		addRequirements(swerveDrive);
		this.swerveDrive = swerveDrive;
	}
	
	@Override
	public void initialize () {
		swerveDrive.stop();
	}
	
	@Override
	public void execute () {
		swerveDrive.stop();
		
		// chaseTarget();
		followVisualCommands();
		// turnTowardTarget();
		writeTargetInfo();
	}
	
	private void chaseTarget () {
		if (getTargetExists()) {
			swerveDrive.autoDrive(0, CHASE_SPEED, getTurnTowardTarget());
		} else swerveDrive.stop();
	}
	
	// Vertical = move forward
	// Horizontal = turn to face the stick
	// 45 degrees = stop moving
	private void followVisualCommands () {
		if (getTargetExists()) {
			if (getTargetVerticality() > 1.8) {
				// Direction stick is oriented horizontally (retroreflective tape is vertical),
				// so turn to face the direction stick.
				turnTowardTarget();
			} else if (getTargetVerticality() < 1/1.8) {
				// Direction stick is oriented vertically (retroreflective tape is horizontal),
				// so move forward.
				swerveDrive.autoDrive(0, FORWARD_MOVE_SPEED, 0);
			} else {
				// Direction stick is oriented at roughly a 45-degree angle,
				// so stop moving.
				swerveDrive.stop();
			}
		} else swerveDrive.stop(); // Do not move if the target cannot be seen
	}
	
	private double getTurnTowardTarget () {
		if (getTargetExists()) {
			if (!withinTargetDistanceMargin()) {
				// Get the adjusted right turn speed (increases as the target is rotationally farther away,
				// until a distance at which it is maximized
				double adjRightTurnSpeed = CLOCKWISE_TURN_SPEED * Math.min(1, Math.abs(getTargetX())/MAX_TURN_SPEED_DISTANCE);
				
				// Target is not within distance margin; turn right or left to turn toward target
				if (getTargetX() > 0) return adjRightTurnSpeed; // Target on right; turn clockwise
				else return -adjRightTurnSpeed; // Target on left; turn counterclockwise
			} else return 0; // Do not turn if facing the target
		} else return 0; // Don't turn unless the target can be seen
	}
	
	private void turnTowardTarget () {
		swerveDrive.autoDrive(0, 0, getTurnTowardTarget());
	}
	
	private boolean withinTargetDistanceMargin () {
		return Math.abs(getTargetX()) < TARGET_DISTANCE_MARGIN;
	}
	
	private void writeTargetInfo () {
		if (counter <= 0) {
			counter = 50;
			System.out.println("Target exists: " + getTargetExists());
			
			// Target orientation
			double verticality = getTargetVerticality();
			System.out.println(verticality);
			if (verticality > 1.8) System.out.println("Vertical orientation");
			else if (verticality < 1/1.8) System.out.println("Horizontal orientation");
			else System.out.println("45-degree orientation");
			
			System.out.println("X: " + getTargetX());
		} counter --;
	}
	
	private double getTargetX () {
		return -targetX.getDouble(0); // limelight is mounted upside-down so targetX is negated
	}
	
	private boolean getTargetExists () {
		return targetExists.getDouble(0) == 1;
	}
	
	/**
	 * Returns the "verticality" of the target: the ratio between vertical length the target takes up
	 * and the horizontal length the target takes up. If the target is a long rectangle, the verticality
	 * will be much greater than 1 when it is upright and between 0 and 1 when it is on its side.
	 */
	private double getTargetVerticality () {
		return targetVert.getDouble(1) / targetHor.getDouble(1);
	}
	
	@Override
	public void end (boolean interrupted) {
		swerveDrive.stop();
	}
	
}