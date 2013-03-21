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

package com.titankingdoms.dev.titanchat.addon;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.loading.Loadable;

public class ChatAddon extends Loadable {
	
	protected final TitanChat plugin;
	
	public ChatAddon(String name) {
		super(name);
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ChatAddon)
			return ((ChatAddon) object).getName().equals(getName());
		
		return false;
	}
	
	public final void register(ChatAddon... addons) {
		plugin.getAddonManager().registerAddons(addons);
	}
	
	public final void register(Channel... channels) {
		plugin.getChannelManager().registerChannels(channels);
	}
	
	public final void register(ChannelLoader... loaders) {
		plugin.getChannelManager().registerLoaders(loaders);
	}
	
	public final void register(Command... commands) {
		plugin.getCommandManager().registerCommands(commands);
	}
	
	@Override
	public String toString() {
		return "ChatAddon:" + getName();
	}
}