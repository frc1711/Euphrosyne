package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Dashboard;

public class Shooter extends SubsystemBase {
	
	/**
	 * TODO: FIX THIS
	 * For now, Shooter just redirects inputs to the HoodedShooter singleton.
	 * DO NOT USE SHOOTER AND HOODEDSHOOTER AT THE SAME TIME
	 */
	
	private static Shooter shooterInstance;
	
	public static Shooter getInstance () {
		if (shooterInstance == null) shooterInstance = new Shooter();
		return shooterInstance;
	}
	
	public double getSpeed () {
		return 0;
	}
	
	public void setSpeed (double speed) {
		HoodedShooter.getInstance().setSpeed(Dashboard.HOODED_SHOOTER_UPPER_SPEED.get(), Dashboard.HOODED_SHOOTER_LOWER_SPEED.get());
	}
	
	public void stop () {
		HoodedShooter.getInstance().stop();
	}
	
}