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

package com.titankingdoms.dev.titanchat.core.channel.standard;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.core.channel.Range;
import com.titankingdoms.dev.titanchat.core.channel.Type;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public final class StandardChannel extends Channel {
	
	private final ChannelLoader loader;
	
	public StandardChannel(String name, Type type, StandardLoader loader) {
		super(name, type);
		this.loader = loader;
	}
	
	@Override
	public String[] getAliases() {
		return getConfig().getStringList("aliases").toArray(new String[0]);
	}

	@Override
	public ChannelLoader getChannelLoader() {
		return loader;
	}
	
	@Override
	public String getFormat() {
		return getSetting("format", "");
	}
	
	@Override
	public String getPassword() {
		return getSetting("password", "");
	}
	
	@Override
	public Range getRange() {
		Range range = Range.fromName(getSetting("range", "channel"));
		return (range != null) ? range : Range.CHANNEL;
	}
	
	@Override
	public Set<Participant> getRecipients() {
		Set<Participant> recipients = new HashSet<Participant>();
		
		for (String name : getParticipants())
			recipients.add(plugin.getParticipantManager().getParticipant(name));
		
		return recipients;
	}
	
	@Override
	public String getTag() {
		return getSetting("tag", "");
	}
	
	@Override
	public void reload() {
		reloadConfig();
		getConfig().set("admins", getAdmins());
		getConfig().set("blacklist", getBlacklist());
		getConfig().set("whitelist", getWhitelist());
		saveConfig();
	}
}