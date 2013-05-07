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

package com.titankingdoms.dev.titanchat.core.channel.conversation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.format.Format;

public final class Conversation extends Channel {
	
	private final Map<String, Participant> participants;
	
	public Conversation() {
		super("Conversation", "Conversation", Status.CONVERSATION);
		this.participants = new HashMap<String, Participant>();
	}
	
	@Override
	public String[] getAliases() {
		return new String[0];
	}
	
	@Override
	public String getDisplayColour() {
		return plugin.getConfig().getString("pm.display-colour", "");
	}
	
	@Override
	public String getFormat() {
		return plugin.getConfig().getString("pm.format", Format.getFormat());
	}
	
	@Override
	public Set<Participant> getParticipants() {
		return new HashSet<Participant>(participants.values());
	}
	
	@Override
	public Range getRange() {
		return Range.CHANNEL;
	}
	
	@Override
	public boolean isParticipating(String name) {
		return participants.containsKey(name.toLowerCase());
	}
	
	@Override
	public void join(Participant participant) {
		if (participant != null && !isParticipating(participant))
			participants.put(participant.getName().toLowerCase(), participant);
	}
	
	@Override
	public void leave(Participant participant) {
		if (participant != null && isParticipating(participant))
			participants.remove(participant.getName().toLowerCase());
	}
}