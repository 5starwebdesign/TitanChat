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

package com.titankingdoms.dev.titanchat.channel.standard;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.channel.Factory;

public final class StandardFactory extends Factory<StandardChannel> {
	
	public StandardFactory() {
		super("Standard");
	}
	
	@Override
	public StandardChannel loadChannel(String name, FileConfiguration config) {
		if (name == null || config == null || name.isEmpty() || !StringUtils.isAlphanumeric(name))
			return null;
		
		StandardChannel channel = new StandardChannel(name);
		
		channel.setRange(config.getString("range", "standard"));
		channel.setRadius(config.getInt("radius", 15));
		channel.setPassword(config.getString("password", ""));
		
		if (config.get("blacklist", null) != null)
			channel.getBlacklist().addAll(config.getStringList("blacklist"));
		
		if (config.get("whitelist", null) != null)
			channel.getWhitelist().addAll(config.getStringList("whitelist"));
		
		if (config.get("operators", null) != null)
			channel.getOperators().addAll(config.getStringList("operators"));
		
		return channel;
	}
	
	@Override
	public void saveChannel(StandardChannel channel) {
		if (channel == null || !channel.getFactory().equals("Standard"))
			return;
		
		channel.getConfig().set("range", channel.getRange().getName());
		channel.getConfig().set("radius", channel.getRadius());
		channel.getConfig().set("password", channel.getPassword());
		channel.getConfig().set("blacklist", new ArrayList<String>(channel.getBlacklist()));
		channel.getConfig().set("whitelist", new ArrayList<String>(channel.getWhitelist()));
		channel.getConfig().set("operators", new ArrayList<String>(channel.getOperators()));
	}
}