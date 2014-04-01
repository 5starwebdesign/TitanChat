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

package com.titankingdoms.dev.titanchat.user;

import org.bukkit.command.ConsoleCommandSender;

import com.titankingdoms.dev.titanchat.api.conversation.Conversation;
import com.titankingdoms.dev.titanchat.api.user.User;
import com.titankingdoms.dev.titanchat.utility.Messaging;

public final class Console extends User {
	
	public Console(ConsoleCommandSender sender) {
		super(sender.getName());
	}
	
	public ConsoleCommandSender asConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	@Override
	public boolean sendConversation(Conversation conversation) {
		return false;
	}
	
	@Override
	public void sendRawLine(String line) {
		ConsoleCommandSender console = asConsoleSender();
		
		if (console == null)
			return;
		
		Messaging.message(console, line);
	}
}