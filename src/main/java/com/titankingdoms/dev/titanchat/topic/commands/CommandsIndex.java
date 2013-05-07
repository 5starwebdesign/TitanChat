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

package com.titankingdoms.dev.titanchat.topic.commands;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.topic.Index;
import com.titankingdoms.dev.titanchat.topic.Topic;

/**
 * {@link CommandsIndex} - Index of {@link Command}s
 * 
 * @author NodinChan
 *
 */
public final class CommandsIndex extends Index {
	
	public CommandsIndex() {
		super("Commands");
	}
	
	/**
	 * Indexes all existing {@link Command}s
	 * 
	 * @return This {@link CommandsIndex}
	 */
	public CommandsIndex index() {
		for (Topic topic : getTopics())
			removeTopic(topic);
		
		FileConfiguration config = plugin.getTopicManager().getConfig();
		
		for (Command command : plugin.getCommandManager().getCommands()) {
			CommandTopic topic = new CommandTopic(command);
			String information = config.getString("commands." + topic.getName() + ".information", "");
			topic.setInformation(information);
			addTopic(topic);
		}
		
		return this;
	}
}