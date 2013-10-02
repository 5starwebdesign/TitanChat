/*
 *     Copyright (C) 2013  Nodin Chan
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
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.tools;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Debugger {
	
	private final Logger log = Logger.getLogger("TitanDebug");
	
	private final int id;
	
	private static Set<Integer> debuggers = new HashSet<Integer>();
	private static Set<Integer> debugging = new HashSet<Integer>();
	
	public Debugger(int id) {
		this.id = id;
		
		if (!debuggers.contains(id))
			debuggers.add(id);
	}
	
	public void debug(Level level, String message) {
		if (isDebugging())
			log.log(level, "[TitanDebug] " + message);
	}
	
	public static Set<Integer> getDebugging() {
		return new HashSet<Integer>(debugging);
	}
	
	public int getId() {
		return id;
	}
	
	public static boolean isDebugging(int id) {
		return debugging.contains(id);
	}
	
	public boolean isDebugging() {
		return isDebugging(id);
	}
	
	public static void startDebug(int id) {
		if (id == 1024)
			debugging.addAll(debuggers);
		else
			debugging.add(id);
	}
	
	public void startDebug() {
		startDebug(id);
	}
	
	public static void stopDebug(int id) {
		if (id == 1024)
			debugging.clear();
		else
			debugging.remove(id);
	}
	
	public void stopDebug() {
		stopDebug(id);
	}
}