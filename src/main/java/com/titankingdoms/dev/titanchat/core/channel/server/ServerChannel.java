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
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.core.channel.Range;
import com.titankingdoms.dev.titanchat.core.channel.Type;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public final class ServerChannel extends Channel {
	
	private final ChannelLoader loader;
	
	public ServerChannel() {
		super("Server", Type.DEFAULT);
		this.loader = new ServerLoader(this);
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
		return plugin.getFormatHandler().getFormat();
	}
	
	@Override
	public String getPassword() {
		return "";
	}
	
	@Override
	public Range getRange() {
		return Range.GLOBAL;
	}
	
	@Override
	public Set<Participant> getRecipients() {
		Set<Participant> recipients = new HashSet<Participant>();
		
		for (Player player : plugin.getServer().getOnlinePlayers())
			recipients.add(plugin.getParticipantManager().getParticipant(player.getName()));
		
		return recipients;
	}
	
	@Override
	public String getTag() {
		return "";
	}
	
	@Override
	public void reload() {}
}