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
import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Point;
import com.titankingdoms.dev.titanchat.api.event.user.UserLoadEvent;
import com.titankingdoms.dev.titanchat.api.event.user.UserSaveEvent;
import com.titankingdoms.dev.titanchat.tools.util.VaultUtils;

public class User implements Point {
	
	protected final TitanChat plugin;
	protected final UserManager manager;
	
	private final String name;
	
	private Point current;
	
	private final Map<String, Metadata> metadata;
	
	private final Set<Point> represent = new HashSet<Point>();
	
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
	
	public final void converse(Point target, String format, String message) {
		plugin.converse(this, target, format, message);
	}
	
	public final void converse(String format, String message) {
		converse(current, format, message);
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof User) ? toString().equals(object.toString()) : false;
	}
	
	public CommandSender getCommandSender() {
		return null;
	}
	
	public final ConfigurationSection getConfig() {
		FileConfiguration config = manager.getConfig();
		
		if (config.get(name.toLowerCase(), null) == null)
			return config.createSection(name.toLowerCase());
		
		return config.getConfigurationSection(name.toLowerCase());
	}
	
	public final Point getCurrentPoint() {
		return current;
	}
	
	public String getDisplayName() {
		return getMetadata("display-name", name).getValue();
	}
	
	@Override
	public String getFormat() {
		return plugin.getConfig().getString("format.user", "%display whispers: %message");
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
	
	public User getOnlineUser() {
		UserManager manager = plugin.getManager(UserManager.class);
		
		if (!manager.has(getName()))
			return null;
		
		return manager.get(getName());
	}
	
	@Override
	public Collection<Point> getRecipients(Point sender) {
		return new HashSet<Point>(represent);
	}
	
	@Override
	public String getType() {
		return "User";
	}
	
	public boolean hasDisplayName() {
		return name.equals(getMetadata("display-name", name).getValue());
	}
	
	public final boolean hasCurrentPoint() {
		return current != null;
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
	
	public final boolean isCurrentPoint(Point point) {
		return (current != null) ? current.equals(point) : point == null;
	}
	
	public boolean isLinked(Point point) {
		return false;
	}
	
	public boolean isOnline() {
		return false;
	}
	
	@Override
	public void linkPoint(Point point) {}
	
	public void load() {
		loadMetadata();
		plugin.getServer().getPluginManager().callEvent(new UserLoadEvent(this));
	}
	
	private final void loadMetadata() {
		if (getConfig().get("metadata", null) == null)
			return;
		
		ConfigurationSection metadata = getConfig().getConfigurationSection("metadata");
		
		for (String key : metadata.getKeys(false))
			setMetadata(key, metadata.getString(key, ""));
	}
	
	public void save() {
		saveMetadata();
		plugin.getServer().getPluginManager().callEvent(new UserSaveEvent(this));
	}
	
	private final void saveMetadata() {
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
	
	public final void setCurrentPoint(Point point) {
		this.current = point;
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
		return "\"User\": {" +
				"\"name\": \"" + getName() + "\", " +
				"\"online\": \"" + isOnline() + "\", " +
				"\"current\": {" +
				"\"name: \"" + ((current != null) ? current.getName() : "") + "\", " +
				"\"type: \"" + ((current != null) ? current.getType() : "") + "\"" +
				"}" +
				"}";
	}
	
	@Override
	public void unlinkPoint(Point point) {}
	
	public class Metadata {
		
		private final String key;
		private final String value;
		
		public Metadata(String key, String value) {
			Validate.notEmpty(key, "Key cannot be empty");
			
			this.key = key;
			this.value = (value != null) ? value : "";
		}
		
		public final String getKey() {
			return key;
		}
		
		public final String getValue() {
			return value;
		}
	}
}