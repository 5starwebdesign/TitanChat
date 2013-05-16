package com.titankingdoms.dev.titanchat.event;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link ChannelEmoteEvent} - Called when {@link Participant}s emote in {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public final class ChannelEmoteEvent extends ChannelEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Participant sender;
	private final Set<Participant> recipients;
	
	private String format;
	private String emote;
	
	public ChannelEmoteEvent(Participant sender, Channel channel, String format, String emote) {
		super(channel);
		this.sender = sender;
		this.recipients = new HashSet<Participant>();
		this.format = format;
		this.emote = emote;
	}
	
	/**
	 * Gets the emote to be sent
	 * 
	 * @return The emote
	 */
	public String getEmote() {
		return emote;
	}
	
	/**
	 * Gets the format to be used
	 * 
	 * @return The format
	 */
	public String getFormat() {
		return format;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/**
	 * Gets the recipients of the message
	 * 
	 * @return The recipients
	 */
	public Set<Participant> getRecipients() {
		return recipients;
	}
	
	/**
	 * Gets the sender of the message
	 * 
	 * @return The sender
	 */
	public Participant getSender() {
		return sender;
	}
	
	/**
	 * Sets the emote to be sent
	 * 
	 * @param emote The new emote
	 */
	public void setEmote(String emote) {
		this.emote = emote;
	}
	
	/**
	 * Sets the format to be used
	 * 
	 * @param format The new format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}