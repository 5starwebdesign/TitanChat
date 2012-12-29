/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

public final class Debugger {
	
	private final Logger log = Logger.getLogger("TitanDebug");
	
	private final int id;
	private final String name;
	
	private static final Set<Integer> debuggers = new HashSet<Integer>();
	private static final Set<Integer> debugging = new HashSet<Integer>();
	
	public Debugger(int id) {
		this(id, "");
	}
	
	public Debugger(int id, String name) {
		this.id = id;
		this.name = (name != null) ? name : "";
		
		if (!debuggers.contains(id))
			debuggers.add(id);
	}
	
	public void debug(Level level, String message) {
		if (isDebugging())
			log.log(level, "[TitanDebug] " + ((!name.isEmpty()) ? name + ": " : "") + message);
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void i(String message) {
		debug(Level.INFO, message);
	}
	
	public boolean isDebugging() {
		return isDebugging(id);
	}
	
	public boolean isDebugging(int id) {
		return debugging.contains(id);
	}
	
	public void s(String message) {
		debug(Level.SEVERE, message);
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
	
	public void w(String message) {
		debug(Level.WARNING, message);
	}
}