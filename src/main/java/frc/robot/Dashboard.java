package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Dashboard {
	
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
		
		public T getFromDashboard () {
			return getValue.get();
		}
		
		public void putToDashboard (T newValue) {
			putValue.accept(newValue);
		}
		
		// Smart Dashboard entries
		public static Entry<Double> getDoubleEntry (String name, double defaultValue) {
			return new Entry<Double>(() -> SmartDashboard.getNumber(name, defaultValue), (x) -> SmartDashboard.putNumber(name, x));
		}
		
		public static Entry<Boolean> getBooleanEntry (String name, boolean defaultValue) {
			return new Entry<Boolean>(() -> SmartDashboard.getBoolean(name, defaultValue), (x) -> SmartDashboard.putBoolean(name, x));
		}
		
	}
	
}