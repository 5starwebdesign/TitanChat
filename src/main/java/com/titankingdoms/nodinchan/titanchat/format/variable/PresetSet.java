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

package com.titankingdoms.nodinchan.titanchat.format.variable;

import java.util.HashMap;
import java.util.Map;

import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;

public final class PresetSet implements Comparable<PresetSet> {
	
	private final int priority;
	private final String permission;
	
	private final Map<String, PresetVariable> variables;
	
	protected PresetSet() {
		this("", 10240, new HashMap<String, PresetVariable>());
	}
	
	protected PresetSet(Map<String, PresetVariable> variables) {
		this("", 10240, variables);
	}
	
	protected PresetSet(String group, int priority) {
		this(group, priority, new HashMap<String, PresetVariable>());
	}
	
	protected PresetSet(String group, int priority, Map<String, PresetVariable> variables) {
		this.priority = priority;
		this.permission = (group != null && !group.isEmpty()) ? "TitanChat.preset." + group : "";
		this.variables = variables;
	}
	
	public int compareTo(PresetSet set) {
		return ((Integer) priority).compareTo(set.getPriority());
	}
	
	public boolean existingVariable(String formatTag) {
		return variables.containsKey(formatTag);
	}
	
	public int getPriority() {
		return priority;
	}
	
	public PresetVariable getVariable(String formatTag) {
		return variables.get(formatTag);
	}
	
	public boolean hasPermission(Participant sender) {
		return permission.isEmpty() || sender.hasPermission(permission);
	}
	
	public void registerVariable(PresetVariable variable) {
		if (!existingVariable(variable.getFormatTag()))
			variables.put(variable.getFormatTag(), variable);
	}
}