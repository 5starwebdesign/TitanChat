/*
 *     Copyright (C) 2013  Nodin Chan
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
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.core.channel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;

public final class ChannelManager implements Manager<Channel> {
	
	private final TitanChat plugin;
	
	private final Map<String, Channel> channels;
	
	public ChannelManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getChannelDirectory().mkdirs())
			plugin.log(Level.INFO, "Creating channel directory...");
		
		this.channels = new TreeMap<String, Channel>();
	}
	
	@Override
	public Channel get(String name) {
		return (name != null) ? channels.get(name.toLowerCase()) : null;
	}
	
	@Override
	public List<Channel> getAll() {
		return new ArrayList<Channel>(channels.values());
	}
	
	public File getChannelDirectory() {
		return new File(plugin.getDataFolder(), "channels");
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? channels.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(Channel channel) {
		if (channel == null)
			return false;
		
		if (!has(channel.getName()))
			return false;
		
		return get(channel.getName()).equals(channel);
	}
	
	@Override
	public void load() {
		
	}
	
	@Override
	public void registerAll(Channel... channels) {
		if (channels == null)
			return;
		
		for (Channel channel : channels) {
			if (channel == null)
				continue;
			
			if (has(channel.getName())) {
				plugin.log(Level.INFO, "Duplicate: " + channel);
				continue;
			}
		}
	}
	
	@Override
	public void reload() {
		
	}
	
	@Override
	public void unload() {
		
	}
	
	@Override
	public void unregister(Channel channel) {
		if (channel == null || !has(channel))
			return;
	}
}