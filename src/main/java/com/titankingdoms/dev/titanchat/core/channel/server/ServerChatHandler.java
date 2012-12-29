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

package com.titankingdoms.dev.titanchat.core.channel.server;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChatHandler;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public final class ServerChatHandler extends ChatHandler {
	
	private final Channel channel;
	
	public ServerChatHandler(ServerChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public String getFormat() {
		return channel.getFormat();
	}
	
	@Override
	public Set<Participant> getRecipients() {
		Set<Participant> recipients = new HashSet<Participant>();
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			recipients.add(plugin.getParticipantManager().getParticipant(player.getName()));
		
		return recipients;
 	}
}