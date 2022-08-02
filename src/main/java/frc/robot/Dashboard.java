package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Dashboard {
	
	public static final Entry<Boolean>
		CLIMBER_OVERRIDE_MODE = Entry.getBooleanEntry("Climber Override Mode", false),
		IS_FIELD_RELATIVE = Entry.getBooleanEntry("Field Relative", true),
		OUTREACH_SAFETY_MODE = Entry.getBooleanEntry("Outreach Safety Mode", false);
	public static final Entry<Double>
		AUTON_WAIT_PERIOD = Entry.getDoubleEntry("Auton Wait Period", 0),
		CARGO_HANDLER_SPEED = Entry.getDoubleEntry("Cargo Handler Speed", -0.5),
		SHOOTER_MAX_SPEED = Entry.getDoubleEntry("Max Shooter Speed", -0.75),
		INTAKE_MAX_SPEED = Entry.getDoubleEntry("Max Intake Speed", -0.7),
		
		HOODED_SHOOTER_UPPER_SPEED = Entry.getDoubleEntry("Hooded Shooter Upper Speed", -0.42),
		HOODED_SHOOTER_LOWER_SPEED = Entry.getDoubleEntry("Hooded Shooter Lower Speed", 0.42);
	
	private Dashboard () { }
	
	public static void putSendable (String name, Sendable sendable) {
		SmartDashboard.putData(name, sendable);
	}
	
	public static class Entry<T> {
		
		private final Supplier<T> getValue;
		private final Consumer<T> putValue;
		
		private Entry (Supplier<T> getValue, Consumer<T> putValue) {
			this.getValue = getValue;
			this.putValue = putValue;
		}
		
		public T get () {
			return getValue.get();
		}
		
		public void put (T newValue) {
			putValue.accept(newValue);
		}
		
		// Smart Dashboard entries
		public static Entry<Double> getDoubleEntry (String name, double defaultValue) {
			SmartDashboard.putNumber(name, defaultValue);
			return new Entry<Double>(() -> SmartDashboard.getNumber(name, defaultValue), (x) -> SmartDashboard.putNumber(name, x));
		}
		
		public static Entry<Boolean> getBooleanEntry (String name, boolean defaultValue) {
			SmartDashboard.putBoolean(name, defaultValue);
			return new Entry<Boolean>(() -> SmartDashboard.getBoolean(name, defaultValue), (x) -> SmartDashboard.putBoolean(name, x));
		}
		
	}
	
}