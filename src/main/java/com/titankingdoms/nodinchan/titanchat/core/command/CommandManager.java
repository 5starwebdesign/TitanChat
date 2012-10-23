package com.titankingdoms.nodinchan.titanchat.core.command;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.command.annotation.Command;
import com.titankingdoms.nodinchan.titanchat.core.command.execution.AnnotationExecutor;
import com.titankingdoms.nodinchan.titanchat.core.command.execution.Executor;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public class CommandManager {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(3, "CommandManager");
	
	private final Map<String, String> aliases;
	private final Map<String, CommandBase> bases;
	private final Map<String, Executor> executors;
	
	public CommandManager() {
		this.plugin = TitanChat.getInstance();
		this.aliases = new HashMap<String, String>();
		this.bases = new HashMap<String, CommandBase>();
		this.executors = new HashMap<String, Executor>();
	}
	
	public boolean existingCommandBase(String name) {
		return bases.containsKey(name.toLowerCase());
	}
	
	public boolean existingCommandBase(CommandBase base) {
		return existingCommandBase(base.getName());
	}
	
	public boolean existingExecutor(String name) {
		return executors.containsKey(name.toLowerCase());
	}
	
	public boolean existingExecutor(Executor executor) {
		return existingExecutor(executor.getName());
	}
	
	public boolean existingExecutorAlias(String alias) {
		return aliases.containsKey(alias.toLowerCase());
	}
	
	public CommandBase getCommandBase(String name) {
		return bases.get(name.toLowerCase());
	}
	
	public File getCommandDirectory() {
		return new File(plugin.getAddonManager().getAddonDirectory(), "commands");
	}
	
	public Executor getExecutor(String name) {
		return executors.get(name.toLowerCase());
	}
	
	public Executor getExecutorByAlias(String alias) {
		return (existingExecutorAlias(alias)) ? getExecutor(aliases.get(alias.toLowerCase())) : null;
	}
	
	public void register(CommandBase... bases) {
		for (CommandBase base : bases) {
			if (existingCommandBase(base))
				return;
			
			this.bases.put(base.getName().toLowerCase(), base);
			
			for (Method method : base.getClass().getDeclaredMethods()) {
				if (!method.isAnnotationPresent(Command.class))
					continue;
				
				register(new AnnotationExecutor(base, new CommandMethod(base, method)));
			}
		}
	}
	
	public void register(Executor... executors) {
		for (Executor executor : executors) {
			if (existingExecutor(executor))
				return;
			
			this.executors.put(executor.getName().toLowerCase(), executor);
			
			for (String alias : executor.getAliases())
				if (!existingExecutorAlias(alias))
					aliases.put(alias.toLowerCase(), executor.getName());
		}
	}
	
	private void sortCommandBases() {
		List<CommandBase> bases = new ArrayList<CommandBase>(this.bases.values());
		Collections.sort(bases);
		this.bases.clear();
		
		for (CommandBase base : bases)
			this.bases.put(base.getName().toLowerCase(), base);
	}
	
	private void sortExecutors() {
		List<Executor> executors = new ArrayList<Executor>(this.executors.values());
		Collections.sort(executors);
		this.executors.clear();
		
		for (Executor executor : executors)
			this.executors.put(executor.getName().toLowerCase(), executor);
	}
}