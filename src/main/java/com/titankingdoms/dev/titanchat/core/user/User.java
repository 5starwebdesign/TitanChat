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

package com.titankingdoms.dev.titanchat.core.user;

import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.util.VaultUtils;

public abstract class User implements EndPoint {
	
	protected final TitanChat plugin;
	private final UserManager manager;
	
	private final String name;
	
	private EndPoint current;
	
	private final Metadata metadata;
	
	private final Set<EndPoint> represent = new HashSet<EndPoint>();
	
	public User(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(!Pattern.compile("\\W").matcher(name).find(), "Name cannot contain non-word characters");
		Validate.isTrue(name.length() <= 16, "Name cannot be longer than 16 characters");
		
		this.plugin = TitanChat.getInstance();
		this.manager = plugin.getManager(UserManager.class);
		this.name = name;
		this.metadata = new Metadata();
		this.represent.add(this);
		
		loadMetadata();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof User)
			return toString().equals(object.toString());
		
		return false;
	}
	
	public abstract CommandSender getCommandSender();
	
	public final EndPoint getCurrentEndPoint() {
		return current;
	}
	
	public String getDisplayName() {
		return getMetadata().getString("display-name", name);
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	public String getPrefix() {
		return getMetadata().getString("prefix");
	}
	
	@Override
	public Set<EndPoint> getRelayPoints() {
		return new HashSet<EndPoint>(represent);
	}
	
	public String getSuffix() {
		return getMetadata().getString("suffix");
	}
	
	@Override
	public String getType() {
		return "User";
	}
	
	public boolean hasDisplayName() {
		return name.equals(getDisplayName());
	}
	
	public boolean hasPermission(String node) {
		return VaultUtils.hasPermission(getCommandSender(), node);
	}
	
	public boolean isCurrentEndPoint(EndPoint endpoint) {
		return (current != null) ? current.equals(endpoint) : endpoint == null;
	}
	
	public abstract boolean isOnline();
	
	public final void loadMetadata() {
		String path = name.toLowerCase() + ".metadata";
		
		ConfigurationSection metadata = null;
		
		if (manager.getConfig().get(path, null) != null)
			metadata = manager.getConfig().getConfigurationSection(path);
		else
			metadata = manager.getConfig().createSection(path);
		
		this.metadata.setMetadata(metadata);
	}
	
	public final void saveMetadata() {
		if (this.metadata.isEmpty())
			manager.getConfig().set(name.toLowerCase() + ".metadata", null);
		
		this.manager.saveConfig();
	}
	
	@Override
	public void sendNotice(String... messages) {
		for (String message : messages)
			sendRawLine("[TitanChat] " + message);
	}
	
	@Override
	public void sendRawLine(String line) {
		CommandSender sender = getCommandSender();
		
		if (sender == null)
			return;
		
		sender.sendMessage(line);
	}
	
	public final void setCurrentEndPoint(EndPoint endpoint) {
		this.current = endpoint;
	}
	
	public void setDisplayName(String name) {
		getMetadata().set("display-name", name);
	}
	
	@Override
	public String toString() {
		return "User: {" +
				"name: " + getName() + ", " +
				"current: {" +
				"name: " + (((!isCurrentEndPoint(null)) ? getCurrentEndPoint().getName() : "\"\"")) + ", " +
				"type: " + (((!isCurrentEndPoint(null)) ? getCurrentEndPoint().getType() : "\"\"")) +
				"}" +
				"}";
	}
}