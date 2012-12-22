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

package com.titankingdoms.nodinchan.titanchat.core.channel.standard;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChatHandler;
import com.titankingdoms.nodinchan.titanchat.core.participant.Participant;

public final class StandardChatHandler extends ChatHandler {
	
	private final Channel channel;
	
	public StandardChatHandler(StandardChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public String getFormat() {
		return channel.getFormat();
	}
	
	@Override
	public Set<Participant> getRecipients() {
		Set<Participant> recipients = new HashSet<Participant>();
		
		for (String name : channel.getParticipants())
			recipients.add(plugin.getParticipantManager().getParticipant(name));
		
		return recipients;
	}
}