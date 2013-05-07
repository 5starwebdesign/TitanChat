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

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.topic.Topic;

/**
 * {@link CommandTopic} - Topics about {@link Command}s
 * 
 * @author NodinChan
 *
 */
public final class CommandTopic extends Topic {
	
	public CommandTopic(Command command) {
		super(command.getName(), command.getDescription());
	}
}