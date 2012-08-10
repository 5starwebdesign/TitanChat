package com.titankingdoms.nodinchan.titanchat.util.variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.event.chat.MessageFormatEvent;
import com.titankingdoms.nodinchan.titanchat.event.chat.MessageReceiveEvent;
import com.titankingdoms.nodinchan.titanchat.event.chat.MessageSendEvent;
import com.titankingdoms.nodinchan.titanchat.util.variable.VariableHandler.Variable.VarType;

/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

public final class VariableHandler implements Listener {
	
	private final List<Variable> variables;
	
	public VariableHandler() {
		this.variables = new ArrayList<Variable>();
		TitanChat.getInstance().getServer().getPluginManager().registerEvents(this, TitanChat.getInstance());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMessageFormat(MessageFormatEvent event) {
		for (Variable variable : variables)
			if (event.getClass().isAssignableFrom(variable.getEvent()))
				event.setFormat(variable.replace(event.getFormat(), event.getSender()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMessageReceive(MessageReceiveEvent event) {
		for (Variable variable : variables)
			if (event.getClass().isAssignableFrom(variable.getEvent())) {
				if (variable.getVarType().equals(VarType.FORMAT))
					for (Player recipant : event.getRecipants())
						event.setFormat(recipant, variable.replace(event.getFormat(recipant), event.getSender(), recipant));
				else if (variable.getVarType().equals(VarType.MESSAGE))
					for (Player recipant : event.getRecipants())
						event.setMessage(recipant, variable.replace(event.getMessage(recipant), event.getSender(), recipant));
			}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMessageSend(MessageSendEvent event) {
		for (Variable variable : variables) {
			if (event.getClass().isAssignableFrom(variable.getEvent())) {
				if (variable.getVarType().equals(VarType.FORMAT))
					event.setFormat(variable.replace(event.getFormat(), event.getSender(), event.getRecipants().toArray(new Player[0])));
				else if (variable.getVarType().equals(VarType.MESSAGE))
					event.setMessage(variable.replace(event.getMessage(), event.getSender(), event.getRecipants().toArray(new Player[0])));
			}
		}
	}
	
	public void register(Variable... variables) {
		this.variables.addAll(Arrays.asList(variables));
	}
	
	public void unload() {
		variables.clear();
	}
	
	public static abstract class Variable {
		
		public abstract Class<? extends Event> getEvent();
		
		public abstract String getReplacement(Player sender, Player... recipants);
		
		public abstract String getVariable();
		
		public abstract VarType getVarType();
		
		public final String replace(String line, Player sender, Player... recipants) {
			return line.replace(getVariable(), getReplacement(sender, recipants));
		}
		
		public enum VarType { FORMAT, MESSAGE }
	}
}