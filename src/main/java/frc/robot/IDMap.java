package frc.robot;

public class IDMap {
	
	private IDMap () {}
	
	public enum CAN {
		FRONT_LEFT_DRIVE (1),
		FRONT_RIGHT_DRIVE (3),
		REAR_LEFT_DRIVE (5),
		REAR_RIGHT_DRIVE (7),
		
		FRONT_LEFT_STEER (2),
		FRONT_RIGHT_STEER (4),
		REAR_LEFT_STEER (6),
		REAR_RIGHT_STEER (8),
		
		FRONT_LEFT_STEER_ENCODER (9),
		FRONT_RIGHT_STEER_ENCODER (10),
		REAR_LEFT_STEER_ENCODER (11),
		REAR_RIGHT_STEER_ENCODER (12),
		
		INTAKE (13),
		
		UPPER_SHOOTER (14),
		LOWER_SHOOTER (15),
		
		CLIMBER_ROTATOR (16),
		CLIMBER_EXTENDER (17);
		
		public final int ID;
		private CAN (int id) {
			ID = id;
		}
	}
	
	public enum DIO {
		TOP_PROXIMITY_SENSOR (4),
		
		LEFT_ROTATION_LIMIT_SWITCH (1),
		RIGHT_ROTATION_LIMIT_SWITCH (0),
		
		LEFT_EXTENSION_LIMIT_SWITCH (3),
		RIGHT_EXTENSION_LIMIT_SWITCH (2);
		
		public final int ID;
		private DIO (int id) {
			ID = id;
		}
	}
	
}