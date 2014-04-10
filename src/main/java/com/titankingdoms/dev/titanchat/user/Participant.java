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

import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.user.User;
import com.titankingdoms.dev.titanchat.utility.Messaging;

public final class Participant extends User {
	
	public Participant(Player player) {
		super(player.getName());
	}
	
	public Player asPlayer() {
		return plugin.getServer().getPlayerExact(getName());
	}
	
	@Override
	public boolean isConversable(Node sender, String message, String type) {
		return true;
	}
	
	@Override
	public void sendRawLine(String line) {
		Player player = asPlayer();
		
		if (player == null)
			return;
		
		Messaging.message(player, line);
	}
}