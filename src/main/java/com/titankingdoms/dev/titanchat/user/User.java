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

package com.titankingdoms.dev.titanchat.user;

import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.EndPoint;
import com.titankingdoms.dev.titanchat.vault.VaultUtils;

public abstract class User implements EndPoint {
	
	protected final TitanChat plugin;
	protected final UserManager manager;
	
	private final String name;
	
	private EndPoint current;
	
	private final Map<String, Metadata> metadata;
	
	private final Set<EndPoint> represent = new HashSet<EndPoint>();
	
	public User(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(!Pattern.compile("\\W").matcher(name).find(), "Name cannot contain non-word chars");
		Validate.isTrue(name.length() <= 16, "Name cannot be longer than 16 chars");
		
		this.plugin = TitanChat.getInstance();
		this.manager = plugin.getManager(UserManager.class);
		this.name = name;
		this.metadata = new HashMap<String, Metadata>();
		this.represent.add(this);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof User)
			return toString().equals(object.toString());
		
		return false;
	}
	
	public abstract CommandSender getCommandSender();
	
	public final ConfigurationSection getConfig() {
		FileConfiguration config = manager.getConfig();
		
		if (config.get(name.toLowerCase(), null) == null)
			return config.createSection(name.toLowerCase());
		
		return config.getConfigurationSection(name.toLowerCase());
	}
	
	public final EndPoint getCurrentEndPoint() {
		return current;
	}
	
	public String getDisplayName() {
		return getMetadata("display-name", name).getValue();
	}
	
	public final Set<Metadata> getMetadata() {
		return new HashSet<Metadata>(metadata.values());
	}
	
	public final Metadata getMetadata(String key, String def) {
		if (key == null)
			return null;
		
		return (hasMetadata(key)) ? metadata.get(key) : new Metadata(key, def);
	}
	
	public final Metadata getMetadata(String key) {
		return getMetadata(key, "");
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	public String getPrefix() {
		return getMetadata("prefix").getValue();
	}
	
	@Override
	public Set<EndPoint> getRelayPoints(EndPoint sender) {
		return new HashSet<EndPoint>(represent);
	}
	
	public String getSuffix() {
		return getMetadata("suffix").getValue();
	}
	
	@Override
	public String getType() {
		return "User";
	}
	
	public boolean hasDisplayName() {
		return name.equals(getMetadata("display-name", name).getValue());
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public final boolean hasMetadata() {
		return !metadata.isEmpty();
	}
	
	public final boolean hasMetadata(String key) {
		return (key != null) ? metadata.containsKey(key) : false;
	}
	
	public boolean hasPermission(String node) {
		return VaultUtils.hasPermission(getCommandSender(), node);
	}
	
	public boolean isCurrentEndPoint(EndPoint endpoint) {
		return (current != null) ? current.equals(endpoint) : endpoint == null;
	}
	
	public abstract boolean isOnline();
	
	@Override
	public void linkPoint(EndPoint point) {}
	
	public final void loadMetadata() {
		if (getConfig().get("metadata", null) == null)
			return;
		
		ConfigurationSection metadata = getConfig().getConfigurationSection("metadata");
		
		for (String key : metadata.getKeys(false))
			setMetadata(key, metadata.getString(key, ""));
	}
	
	public final void saveMetadata() {
		ConfigurationSection metadata = getConfig().createSection("metadata");
		
		for (Metadata kv : getMetadata())
			metadata.set(kv.getKey(), kv.getValue());
		
		manager.saveConfig();
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
		setMetadata("display-name", name);
	}
	
	public final void setMetadata(Metadata metadata) {
		if (metadata == null)
			return;
		
		this.metadata.put(metadata.getKey(), metadata);
	}
	
	public final void setMetadata(String key, String value) {
		if (key == null || key.isEmpty() || value == null)
			return;
		
		setMetadata(new Metadata(key, value));
	}
	
	@Override
	public String toString() {
		return "User: {" +
				"name: " + getName() + ", " +
				"online: " + isOnline() + ", " +
				"current: {" +
				"name: " + (((!isCurrentEndPoint(null)) ? getCurrentEndPoint().getName() : "\"\"")) + ", " +
				"type: " + (((!isCurrentEndPoint(null)) ? getCurrentEndPoint().getType() : "\"\"")) +
				"}" +
				"}";
	}
	
	@Override
	public void unlinkPoint(EndPoint point) {}
}