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
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.util.VaultUtils;

public abstract class User implements EndPoint {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private EndPoint current;
	
	private final Map<String, Meta> meta;
	
	private final Set<EndPoint> represent = new HashSet<EndPoint>();
	
	public User(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(!Pattern.compile("\\W").matcher(name).find(), "Name cannot contain non-word characters");
		Validate.isTrue(name.length() <= 16, "Name cannot be longer than 16 characters");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.meta = new HashMap<String, Meta>();
		this.represent.add(this);
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
	
	public String getDiplayName() {
		return getMeta("display-name", getName()).stringValue();
	}
	
	public Meta getMeta(String key) {
		return (key != null) ? getMeta(key, new Meta(key, new Object())) : new Meta("", new Object());
	}
	
	public Meta getMeta(String key, Object def) {
		return (key != null) ? getMeta(key, new Meta(key, def)) : new Meta("", def);
	}
	
	public Meta getMeta(String key, Meta def) {
		return (hasMeta(key)) ? meta.get(key) : def;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public Set<EndPoint> getRelayPoints() {
		return new HashSet<EndPoint>(represent);
	}
	
	@Override
	public String getType() {
		return "User";
	}
	
	public boolean hasMeta(String key) {
		return (key != null) ? meta.containsKey(key) : false;
	}
	
	public boolean hasPermission(String node) {
		return VaultUtils.hasPermission(getCommandSender(), node);
	}
	
	public boolean isCurrentEndPoint(EndPoint endpoint) {
		return (current != null) ? current.equals(endpoint) : endpoint == null;
	}
	
	public abstract boolean isOnline();
	
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
		setMeta("display-name", name);
	}
	
	public void setMeta(Meta meta) {
		if (meta == null)
			return;
		
		if (meta.value() == null)
			this.meta.remove(meta.key());
		else
			this.meta.put(meta.key(), meta);
	}
	
	public void setMeta(String key, Object value) {
		setMeta(new Meta(key, value));
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
	
	public static final class Meta {
		
		private final String key;
		private final Object value;
		
		public Meta(String key, Object value) {
			this.key = key;
			this.value = value;
		}
		
		public boolean booleanValue() {
			return Boolean.valueOf(stringValue());
		}
		
		public double doubleValue() {
			return NumberUtils.toDouble(stringValue(), 0.0D);
		}
		
		public float floatValue() {
			return NumberUtils.toFloat(stringValue(), 0.0F);
		}
		
		public int intValue() {
			return NumberUtils.toInt(stringValue(), 0);
		}
		
		public String key() {
			return key;
		}
		
		public long longValue() {
			return NumberUtils.toLong(stringValue(), 0L);
		}
		
		public String stringValue() {
			return (value != null) ? value.toString() : "";
		}
		
		@Override
		public String toString() {
			return "Meta: {key: " + key() + ", value: " + stringValue() + "}";
		}
		
		public Object value() {
			return value;
		}
	}
}