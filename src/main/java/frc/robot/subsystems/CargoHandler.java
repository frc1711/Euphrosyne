package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Dashboard;
import frc.robot.IDMap;

public class CargoHandler extends SubsystemBase {
	
	private static CargoHandler cargoHandlerInstance;
	
	public static CargoHandler getInstance () {
		if (cargoHandlerInstance == null) cargoHandlerInstance = new CargoHandler();
		return cargoHandlerInstance;
	}
	
	private final CANSparkMax pulley;
	private final DigitalInput
        topProximitySensor = new DigitalInput(IDMap.DIO.TOP_PROXIMITY_SENSOR.ID),
	    bottomProximitySensor = new DigitalInput(IDMap.DIO.BOTTOM_PROXIMITY_SENSOR.ID);
	
	private CargoHandler () {
		pulley = new CANSparkMax(IDMap.CAN.CARGO_PULLEY.ID, MotorType.kBrushless);
		pulley.setIdleMode(IdleMode.kBrake);
	}
	
	public boolean checkBallAtTopSensor () {
		return !topProximitySensor.get();
	}
    
    public boolean checkBallAtBottomSensor () {
        return !bottomProximitySensor.get();
    }
	
    /**
     * Sets the speed of the cargo handler on its default speed range. 
     * @param speed The speed to run the cargo handler at on the interval [-1, 1]. A provided {@code speed} of {@code 1}
     * will run at the default speed for the cargo handler, as specified on the dashboard.
     * @param preventBallPush If the speed is positive (moving forward) then the bottom sensor's reading will be used
     * to prevent a second cargo ball from pushing the first cargo ball out through the shooter. This functionality
     * is controlled by {@code preventBallPush}. If {@code preventBallPush} is {@code false}, then no check will be
     * done.
     */
    public void setSpeed (double speed, boolean preventBallPush) {
        if (preventBallPush && speed > 0 && checkBallAtBottomSensor() && checkBallAtTopSensor()) setSpeedOverride(0);
        else setSpeedOverride(Dashboard.CARGO_HANDLER_SPEED.get() * speed);
    }
    
    /**
     * Sets the speed of the cargo handler, ignoring the default speed on the dashboard or any possible checks.
     * @param speed
     */
	public void setSpeedOverride (double speed) {
		pulley.set(speed);
	}
	
	public void stop () {
		pulley.set(0);
	}
	
	public double getCurrent () {
		return pulley.getOutputCurrent();
	}
	
}