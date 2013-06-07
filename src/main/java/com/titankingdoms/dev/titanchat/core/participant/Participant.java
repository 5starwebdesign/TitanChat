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

package com.titankingdoms.dev.titanchat.core.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.util.Debugger;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

public class Participant implements EndPoint {
	
	protected final TitanChat plugin;
	
	private final Debugger db = new Debugger(4, "Participant");
	
	private final String name;
	
	private EndPoint current;
	
	private final Map<String, EndPoint> endpoints;
	private final Map<String, Meta> meta;
	
	public Participant(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.endpoints = new HashMap<String, EndPoint>();
		this.meta = new HashMap<String, Meta>();
	}
	
	public CommandSender asCommandSender() {
		return null;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Participant)
			return ((Participant) object).getName().equals(getName());
		
		return false;
	}
	
	public final void focus(EndPoint endpoint) {
		this.current = endpoint;
		
		if (endpoint != null && !isLinked(endpoint))
			link(endpoint);
	}
	
	public final ConfigurationSection getConfiguration() {
		return plugin.getParticipantManager().getConfig().getConfigurationSection(name);
	}
	
	public final String getCurrent() {
		return (current != null) ? current.getName() : "";
	}
	
	public final EndPoint getCurrentEndPoint() {
		return current;
	}
	
	public String getDisplayColour() {
		return "";
	}
	
	public String getDisplayName() {
		return getMeta("display-name", getName()).stringValue();
	}
	
	public final Map<String, List<String>> getLinkedPointList() {
		Map<String, List<String>> listMap = new TreeMap<String, List<String>>();
		
		for (EndPoint endpoint : getLinkedPoints()) {
			if (!listMap.containsKey(endpoint.getPointType()))
				listMap.put(endpoint.getPointType(), new ArrayList<String>());
			
			listMap.get(endpoint.getPointType()).add(endpoint.getName());
		}
		
		for (List<String> list : listMap.values())
			Collections.sort(list);
		
		return listMap;
	}
	
	public final Set<EndPoint> getLinkedPoints() {
		return new HashSet<EndPoint>(endpoints.values());
	}
	
	public String getFormat() {
		return Format.getChatFormat();
	}
	
	public Map<String, Meta> getMeta() {
		return new HashMap<String, Meta>(meta);
	}
	
	public Meta getMeta(String key, Meta def) {
		if (key == null)
			return (def != null) ? def : new Meta("", new Object());
		
		return (meta.containsKey(key)) ? this.meta.get(key) : def;
	}
	
	public final Meta getMeta(String key, Object def) {
		if (key == null)
			return new Meta("", (def != null) ? def : new Object());
		
		return getMeta(key, new Meta(key, def));
	}
	
	public String getName() {
		return name;
	}
	
	public String getPointType() {
		return "Participant";
	}
	
	public String getPrefix() {
		return getMeta("prefix", "").stringValue();
	}
	
	public String getSuffix() {
		return getMeta("suffix", "").stringValue();
	}
	
	public final String getTag() {
		return "PrivMsg";
	}
	
	public final boolean hasPermission(String node) {
		return Vault.hasPermission(asCommandSender(), node);
	}
	
	public final boolean isLinked(EndPoint endpoint) {
		if (endpoint == null)
			return false;
		
		return endpoints.containsKey((endpoint.getPointType() + ":" + endpoint.getName()).toLowerCase());
	}
	
	public final boolean isOnline() {
		return asCommandSender() != null;
	}
	
	public final boolean isRegistered() {
		return plugin.getParticipantManager().hasParticipant(getName());
	}
	
	public final void link(EndPoint endpoint) {
		if (endpoint == null)
			return;
		
		if (!isLinked(endpoint))
			endpoints.put((endpoint.getPointType() + ":" + endpoint.getName()).toLowerCase(), endpoint);
		
		if (!endpoint.isLinked(this))
			endpoint.link(this);
		
		if (!endpoint.equals(current))
			focus(endpoint);
	}
	
	public void messageIn(EndPoint sender, String format, String message) {
		if (sender == null || format == null || format.isEmpty() || message == null || message.isEmpty())
			return;
		
		notice(format.replace("%message", message));
	}
	
	public void messageOut(EndPoint recipient, String message) {
		if (recipient == null || message == null || message.isEmpty())
			return;
		
		recipient.messageIn(this, recipient.getFormat(), message);
	}
	
	public final void messageOut(String message) {
		messageOut(current, message);
	}
	
	public void notice(String... messages) {
		if (isOnline())
			asCommandSender().sendMessage(Format.colourise(messages));
	}
	
	public final void reloadMeta() {
		ConfigurationSection meta = getConfiguration().getConfigurationSection("meta");
		
		if (meta == null)
			return;
		
		for (String key : getMeta().keySet())
			setMeta(key, null);
		
		for (String key : meta.getKeys(false))
			setMeta(key, meta.get(key));
	}
	
	public void saveMeta() {
		ConfigurationSection meta = getConfiguration().getConfigurationSection("meta");
		
		if (meta == null)
			return;
		
		for (String key : meta.getKeys(false))
			meta.set(key, null);
		
		for (String key : getMeta().keySet())
			meta.set(key, getMeta().get(key));
	}
	
	public void setDisplayName(String name) {
		setMeta("display-name", name);
	}
	
	public void setMeta(Meta meta) {
		if (meta == null || meta.key() == null || meta.key().isEmpty())
			return;
		
		if (meta.value() == null)
			this.meta.remove(meta.key());
		else
			this.meta.put(meta.key(), meta);
	}
	
	public final void setMeta(String key, Object meta) {
		if (key == null || key.isEmpty())
			return;
		
		setMeta(new Meta(key, meta));
	}
	
	public final void unlink(EndPoint endpoint) {
		if (endpoint == null)
			return;
		
		if (isLinked(endpoint))
			endpoints.remove((endpoint.getPointType() + ":" + endpoint.getName()).toLowerCase());
		
		if (endpoint.isLinked(this))
			endpoint.unlink(this);
		
		if (endpoint.equals(current))
			focus((!getLinkedPoints().isEmpty()) ? getLinkedPoints().iterator().next() : null);
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
		
		public Object value() {
			return value;
		}
	}
}