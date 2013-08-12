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

package com.titankingdoms.dev.titanchat.event;

import org.bukkit.event.HandlerList;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

/**
 * {@link ChannelJoinEvent} - Called when a {@link Participant} joins a {@link Channel}
 * 
 * @author NodinChan
 *
 */
public final class ChannelJoinEvent extends ChannelEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Participant participant;
	
	public ChannelJoinEvent(Participant participant, Channel channel) {
		super(channel);
		this.participant = participant;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/**
	 * Gets the {@link Participant} invloved
	 * 
	 * @return The {@link Participant}
	 */
	public Participant getParticipant() {
		return participant;
	}
}