/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.titankingdoms.dev.titanchat.util;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link Debugger} - Debugs the plugin
 * 
 * @author NodinChan
 *
 */
public final class Debugger {
	
	private final Logger log = Logger.getLogger("TitanDebug");
	
	private final int id;
	
	private final String prefix;
	
	private static final Set<Integer> debuggers = new HashSet<Integer>();
	private static final Set<Integer> debugging = new HashSet<Integer>();
	
	public Debugger(int id, String prefix) {
		this.id = id;
		this.prefix = (prefix != null) ? prefix : "";
		
		if (!debuggers.contains(id))
			debuggers.add(id);
	}
	
	public Debugger(int id) {
		this(id, "");
	}
	
	/**
	 * Logs the message
	 * 
	 * @param level The {@link Level} of importance
	 * 
	 * @param message The message to log
	 */
	public void debug(Level level, String message) {
		if (isDebugging())
			log.log(level, "[TitanDebug] " + ((!prefix.isEmpty()) ? prefix + ": " : "") + message);
	}
	
	/**
	 * Gets the IDs of debugging {@link Debugger}s
	 * 
	 * @return The IDs
	 */
	public static Set<Integer> getDebugging() {
		return new HashSet<Integer>(debugging);
	}
	
	/**
	 * Gets the ID of the {@link Debugger}
	 * 
	 * @return The ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the prefix used in log messages
	 * 
	 * @return The prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Checks if the {@link Debugger} is debugging
	 * 
	 * @return True if is debugging
	 */
	public boolean isDebugging() {
		return isDebugging(id);
	}
	
	/**
	 * Checks if the {@link Debugger} with the specified ID is debugging
	 * 
	 * @param id The ID of the {@link Debugger}
	 * 
	 * @return True if is debugging
	 */
	public static boolean isDebugging(int id) {
		return debugging.contains(id);
	}
	
	/**
	 * Starts debugging with the {@link Debugger}
	 */
	public void startDebug() {
		startDebug(id);
	}
	
	/**
	 * Starts debugging with the {@link Debugger} with the specified ID
	 * 
	 * @param id The ID of the {@link Debugger}
	 */
	public static void startDebug(int id) {
		if (id < 0)
			debugging.addAll(debuggers);
		else
			debugging.add(id);
	}
	
	/**
	 * Stops debugging with the {@link Debugger}
	 */
	public void stopDebug() {
		stopDebug(id);
	}
	
	/**
	 * Stops debugging with the {@link Debugger} with the specified ID
	 * 
	 * @param id The ID of the {@link Debugger}
	 */
	public static void stopDebug(int id) {
		if (id < 0)
			debugging.clear();
		else
			debugging.remove(id);
	}
}