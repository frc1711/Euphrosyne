package frc.robot.util;

import java.util.Set;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class CommandWrapper extends CommandBase {
	
	private final Command command;
	
	public CommandWrapper (Supplier<Command> commandSupplier) {
		command = commandSupplier.get();
		Set<Subsystem> requirements = command.getRequirements();
		for (Subsystem subsytem : requirements) addRequirements(subsytem);
	}
	
	@Override
	public void initialize () {
		command.initialize();
	}
	
	@Override
	public void execute () {
		command.execute();
	}
	
	@Override
	public void end (boolean interrupted) {
		command.end(interrupted);
	}
	
	@Override
	public boolean isFinished () {
		return command.isFinished();
	}
	
}