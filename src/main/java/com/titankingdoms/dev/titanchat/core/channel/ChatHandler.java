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

package com.titankingdoms.dev.titanchat.core.channel;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.event.ChannelChatEvent;
import com.titankingdoms.dev.titanchat.format.FormatUtils;

public abstract class ChatHandler {
	
	protected final TitanChat plugin;
	
	public ChatHandler() {
		this.plugin = TitanChat.getInstance();
	}
	
	public final String colourise(String text) {
		return FormatUtils.colourise(text);
	}
	
	public final String decolourise(String text) {
		return FormatUtils.decolourise(text);
	}
	
	public abstract String getFormat();
	
	public abstract Set<Participant> getRecipients();
	
	public final void processChat(Participant sender, Channel channel, String message) {
		String format = colourise(getFormat());
		
		if (format == null || format.isEmpty())
			format = colourise(plugin.getFormatHandler().getFormat());
		
		Set<Participant> recipients = getRecipients();
		
		if (recipients == null)
			recipients = new HashSet<Participant>();
		
		ChannelChatEvent event = new ChannelChatEvent(sender, recipients, channel, format, message);
		plugin.getServer().getPluginManager().callEvent(event);
		
		message = colourise(event.getMessage());
		format = plugin.getFormatHandler().getVariableManager().parse(sender, channel, event.getFormat());
		
		for (Participant participant : event.getRecipients())
			participant.send(FormatUtils.splitAndFormat(format, "%message", event.getMessage()));
	}
}