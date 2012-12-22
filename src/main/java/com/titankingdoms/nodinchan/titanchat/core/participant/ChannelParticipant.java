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

package com.titankingdoms.nodinchan.titanchat.core.participant;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.util.Debugger;

public final class ChannelParticipant extends Participant {
	
	private final Debugger db = new Debugger(2, "ChannelParticipant");
	
	private final Map<String, Boolean> mutes = new HashMap<String, Boolean>();
	
	public ChannelParticipant(String name) {
		super(name);
	}
	
	@Override
	public CommandSender getCommandSender() {
		return plugin.getServer().getPlayer(getName());
	}
	
	@Override
	public boolean hasPermission(String permission) {
		if (!isOnline())
			return false;
		
		return getCommandSender().hasPermission(permission);
	}
	
	@Override
	public boolean isMuted(String channel) {
		return (mutes.containsKey(channel.toLowerCase())) ? mutes.get(channel.toLowerCase()) : false;
	}
	
	@Override
	public boolean isOnline() {
		return plugin.getServer().getOfflinePlayer(getName()).isOnline();
	}
	
	@Override
	public void mute(String channel, boolean mute) {
		this.mutes.put(channel.toLowerCase(), mute);
	}
	
	@Override
	public void send(String... messages) {
		if (!isOnline())
			return;
		
		for (String message : messages)
			db.i("@" + getName() + ": " + message);
		
		getCommandSender().sendMessage(messages);
	}
}