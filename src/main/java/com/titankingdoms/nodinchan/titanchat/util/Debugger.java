package com.titankingdoms.nodinchan.titanchat.util;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Debugger {
	
	private final Logger log = Logger.getLogger("TitanDebug");
	
	private final int id;
	private final String name;
	
	private static final Set<Integer> debuggers = new HashSet<Integer>();
	private static final Set<Integer> debugging = new HashSet<Integer>();
	
	public Debugger(int id, String name) {
		this.id = id;
		this.name = (name != null) ? name : "";
		
		if (!debuggers.contains(id))
			debuggers.add(id);
	}
	
	public void debug(DebugLevel level, String message) {
		if (isDebugging())
			log.log(level.getLevel(), "[TitanDebug] " + ((!name.isEmpty()) ? name + ": " : "") + message);
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isDebugging() {
		return isDebugging(id);
	}
	
	public boolean isDebugging(int id) {
		return debugging.contains(id);
	}
	
	public void startDebug() {
		startDebug(id);
	}
	
	public static void startDebug(int id) {
		if (id < 0)
			debugging.addAll(debuggers);
		else
			debugging.add(id);
	}
	
	public void stopDebug() {
		stopDebug(id);
	}
	
	public static void stopDebug(int id) {
		if (id < 0)
			debugging.clear();
		else
			debugging.remove(id);
	}
	
	public enum DebugLevel {
		I(Level.INFO),
		S(Level.SEVERE),
		W(Level.WARNING);
		
		private Level level;
		
		private DebugLevel(Level level) {
			this.level = level;
		}
		
		public Level getLevel() {
			return level;
		}
	}
}