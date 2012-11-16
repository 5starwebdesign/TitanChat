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

package com.titankingdoms.nodinchan.titanchat.core.addon;

import org.bukkit.event.Listener;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.loading.Loadable;

/**
 * Addon - Addon base
 * 
 * @author NodinChan
 *
 */
public class Addon extends Loadable implements Listener {
	
	protected final TitanChat plugin;
	
	public Addon(String name) {
		super(name);
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Addon)
			return ((Addon) object).getName().equals(getName());
		
		return false;
	}
	
	/**
	 * Registers the command
	 * 
	 * @param command The command to register
	 */
	public final void register(Command command) {
		plugin.getCommandManager().register(command);
	}
	
	/**
	 * Registers the channel
	 * 
	 * @param channel The channel to register
	 */
	public final void register(Channel channel) {
		plugin.getChannelManager().register(channel);
	}
	
	public final void register(ChannelLoader loader) {
		plugin.getChannelManager().register(loader);
	}
	
	/**
	 * Registers the listener
	 * 
	 * @param listener The listener to register
	 */
	public final void register(Listener listener) {
		plugin.register(listener);
	}
	
	@Override
	public String toString() {
		return "Addon:" + super.getName();
	}
}