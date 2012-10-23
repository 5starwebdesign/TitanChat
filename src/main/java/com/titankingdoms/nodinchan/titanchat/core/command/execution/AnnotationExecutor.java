package com.titankingdoms.nodinchan.titanchat.core.command.execution;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.command.CommandBase;
import com.titankingdoms.nodinchan.titanchat.core.command.CommandMethod;
import com.titankingdoms.nodinchan.titanchat.core.command.annotation.Aliases;
import com.titankingdoms.nodinchan.titanchat.core.command.annotation.Description;
import com.titankingdoms.nodinchan.titanchat.core.command.annotation.Usage;

public class AnnotationExecutor extends Executor {
	
	private final CommandBase base;
	private final CommandMethod method;
	
	private String[] aliases = new String[0];
	private String description = "";
	private String usage = "";
	
	public AnnotationExecutor(CommandBase base, CommandMethod method) {
		super(method.getName());
		this.base = base;
		this.method = method;
		
		if (method.hasAnnotation(Aliases.class))
			this.aliases = method.getAnnotation(Aliases.class).value();
		
		if (method.hasAnnotation(Description.class))
			this.description = method.getAnnotation(Description.class).value();
		
		if (method.hasAnnotation(Usage.class))
			this.usage = method.getAnnotation(Usage.class).value();
	}

	@Override
	public boolean execute(CommandSender sender, Channel channel, String[] args) {
		return method.execute(sender, channel, args);
	}
	
	@Override
	public String[] getAliases() {
		return aliases;
	}
	
	public CommandBase getCommandBase() {
		return base;
	}
	
	public CommandMethod getCommandMethod() {
		return method;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getUsage() {
		return usage;
	}
}