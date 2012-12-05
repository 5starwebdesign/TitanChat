package com.titankingdoms.nodinchan.titanchat.core.channel;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.event.ChannelChatEvent;
import com.titankingdoms.nodinchan.titanchat.format.FormatUtils;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

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
	}
}