/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.channel.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;

/**
 * Participant - Represents a participant
 * 
 * @author NodinChan
 *
 */
public final class Participant {
	
	private final TitanChat plugin;
	
	private final String name;
	
	private Channel currentChannel;
	
	private final Map<String, Channel> channels;
	
	private final Invitation invitation;
	
	private final Map<String, Boolean> muted;
	
	public Participant(Player player) {
		this.plugin = TitanChat.getInstance();
		this.name = player.getName();
		this.channels = new HashMap<String, Channel>();
		this.invitation = new Invitation(player);
		this.muted = new HashMap<String, Boolean>();
	}
	
	/**
	 * Gets all the channels
	 * 
	 * @return All the channels the participant is participating in
	 */
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	/**
	 * Gets the current channel
	 * 
	 * @return The current channel of the participant
	 */
	public Channel getCurrentChannel() {
		return currentChannel;
	}
	
	/**
	 * Gets the invitation holder
	 * 
	 * @return The invitation storage
	 */
	public Invitation getInvitation() {
		return invitation;
	}
	
	/**
	 * Gets the name of the participant
	 * 
	 * @return The participant name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the player that the participant is representing
	 * 
	 * @return The player
	 */
	public Player getPlayer() {
		return plugin.getPlayer(name);
	}
	
	/**
	 * Checks if the participant is muted in the channel
	 * 
	 * @param channel The channel to check with
	 * 
	 * @return True if the participant is muted in the channel
	 */
	public boolean isMuted(Channel channel) {
		if (!muted.containsKey(channel.getName().toLowerCase()))
			return false;
		
		return muted.get(channel.getName().toLowerCase());
	}
	
	/**
	 * Joins the channel
	 * 
	 * @param channel The channel to join
	 */
	public void join(Channel channel) {
		if (channel == null)
			return;
		
		if (currentChannel == null || !currentChannel.equals(channel))
			currentChannel = channel;
		
		if (!channels.containsKey(channel.getName()))
			channels.put(channel.getName(), channel);
	}
	
	/**
	 * Leaves the channel
	 * 
	 * @param channel The channel to leave
	 */
	public void leave(Channel channel) {
		if (channel == null)
			return;
		
		if (channels.containsKey(channel.getName()))
			channels.remove(channel.getName());
		
		if (currentChannel != null && currentChannel.equals(channel)) {
			if (channels.isEmpty())
				currentChannel = null;
			else
				currentChannel = getChannels().get(0);
		}
	}
	
	/**
	 * Mutes or unmutes the participant in the channel
	 * 
	 * @param channel The channel to mute or unmute in
	 * 
	 * @param mute Whether to mute the participant
	 */
	public void mute(Channel channel, boolean mute) {
		muted.put(channel.getName().toLowerCase(), mute);
	}
}