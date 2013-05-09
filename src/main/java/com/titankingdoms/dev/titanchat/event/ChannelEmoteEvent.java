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
	
	public ChannelEmoteEvent(Participant sender, Channel channel, String emote) {
		this(sender, new HashSet<Participant>(), channel, "", emote);
	}
	
	public ChannelEmoteEvent(Participant sender, Channel channel, String format, String emote) {
		this(sender, new HashSet<Participant>(), channel, format, emote);
	}
	
	public ChannelEmoteEvent(Participant sender, Set<Participant> recipients, Channel channel, String emote) {
		this(sender, recipients, channel, "", emote);
	}
	
	public ChannelEmoteEvent(Participant sender, Set<Participant> recipients, Channel channel, String format, String emote) {
		super(channel);
		this.sender = sender;
		this.recipients = recipients;
		this.format = format;
		this.emote = emote;
	}
	
	/**
	 * Adds the {@link Participant} to the recipients
	 * 
	 * @param recipient The {@link Participant} to add
	 * 
	 * @return True if successfully added
	 */
	public boolean addRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (!this.recipients.contains(recipient)) ? this.recipients.add(recipient) : false;
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
		return new HashSet<Participant>(recipients);
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
	 * Removes the {@link Participant} from the recipients
	 * 
	 * @param recipient The {@link Participant} to remove
	 * 
	 * @return True if successfully removed
	 */
	public boolean removeRecipient(Participant recipient) {
		if (recipient == null)
			return false;
		
		return (this.recipients.contains(recipient)) ? this.recipients.remove(recipient) : false;
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