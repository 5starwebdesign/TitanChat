package com.titankingdoms.nodinchan.titanchat.core.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public final class CommandMethod {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(3, "CommandMethod");
	
	private final Object object;
	private final Method method;
	
	public CommandMethod(Object object, Method method) {
		this.plugin = TitanChat.getInstance();
		this.object = object;
		this.method = method;
	}
	
	public boolean execute(CommandSender sender, Channel channel, String[] args) {
		try {
			method.invoke(object, sender, channel, args);
			return true;
			
		} catch (IllegalAccessException e) {
			plugin.send(MessageLevel.WARNING, sender, "An error seems to have occured, please check console");
			plugin.log(Level.SEVERE, "An IllegalAccessException has occured while using command: " + method.getName());
			
			if (db.isDebugging())
				e.printStackTrace();
			
		} catch (IllegalArgumentException e) {
			plugin.send(MessageLevel.WARNING, sender, "An error seems to have occured, please check console");
			plugin.log(Level.SEVERE, "An IllegalArgumentException has occured while using command: " + method.getName());
			
			if (db.isDebugging())
				e.printStackTrace();
			
		} catch (InvocationTargetException e) {
			plugin.send(MessageLevel.WARNING, sender, "An error seems to have occured, please check console");
			plugin.log(Level.SEVERE, "An InvocationTargetException has occured while using command: " + method.getName());
			
			if (db.isDebugging())
				e.printStackTrace();
		}
		
		return false;
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		return method.getAnnotation(clazz);
	}
	
	public String getName() {
		return method.getName();
	}
	
	public <T extends Annotation> boolean hasAnnotation(Class<T> clazz) {
		return method.isAnnotationPresent(clazz);
	}
}