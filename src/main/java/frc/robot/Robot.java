package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
	private Command m_autonomousCommand;
	
	private RobotContainer m_robotContainer;
	private boolean robotHasBeenEnabled = false;
	
	@Override
	public void robotInit () {
		m_robotContainer = RobotContainer.getInstance();
	}
	
	@Override
	public void robotPeriodic () {
		CommandScheduler.getInstance().run();
	}
	
	@Override
	public void disabledInit () { }
	
	@Override
	public void disabledPeriodic () { }
	
	@Override
	public void autonomousInit () {
		handleRobotEnabling();
		
		m_autonomousCommand = m_robotContainer.getAutonomousCommand();
		
		if (m_autonomousCommand != null) {
			m_autonomousCommand.schedule();
		}
	}
	
	@Override
	public void autonomousPeriodic () { }
	
	@Override
	public void teleopInit () {
		handleRobotEnabling();
		
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}
	
	@Override
	public void teleopPeriodic () { }
	
	@Override
	public void testInit () {
		CommandScheduler.getInstance().cancelAll();
		handleRobotEnabling();
	}
	
	@Override
	public void testPeriodic () { }
	
	private void handleRobotEnabling () {
		if (!robotHasBeenEnabled) {
			m_robotContainer.onFirstRobotEnable();
		} robotHasBeenEnabled = true;
	}
}