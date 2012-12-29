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

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.core.channel.Type;

public class StandardLoader extends ChannelLoader {
	
	public StandardLoader() {
		super("Standard");
	}
	
	@Override
	public Channel create(CommandSender sender, String name, Type type) {
		StandardChannel channel = new StandardChannel(name, type, this);
		
		channel.getConfig().options().copyDefaults(true);
		channel.saveConfig();
		
		return channel;
	}
	
	@Override
	public Channel load(String name, Type type) {
		StandardChannel channel = new StandardChannel(name, type, this);
		
		if (channel.getConfig().get("admins") != null)
			channel.getAdmins().addAll(channel.getConfig().getStringList("admins"));
		
		if (channel.getConfig().get("blacklist") != null)
			channel.getBlacklist().addAll(channel.getConfig().getStringList("blacklist"));
		
		if (channel.getConfig().get("whitelist") != null)
			channel.getWhitelist().addAll(channel.getConfig().getStringList("whitelist"));
		
		return channel;
	}
	
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}
}