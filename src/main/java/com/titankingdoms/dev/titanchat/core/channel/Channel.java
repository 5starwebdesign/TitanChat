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

package com.titankingdoms.dev.titanchat.core.channel;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.titankingdoms.dev.titanchat.addon.ChatAddon;
import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.core.Message;
import com.titankingdoms.dev.titanchat.core.channel.setting.Range;
import com.titankingdoms.dev.titanchat.core.channel.setting.Status;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.util.Debugger;
import com.titankingdoms.dev.titanchat.util.loading.Loadable;

public abstract class Channel extends Loadable implements EndPoint {
	
	private final Debugger db = new Debugger(3, "Channel");
	
	private final String type;
	private final Status status;
	
	private final Set<String> blacklist;
	private final Set<String> operators;
	private final Set<String> whitelist;
	
	private final Map<String, EndPoint> endpoints;
	
	public Channel(String name, String type, Status status) {
		super(name);
		this.type = type;
		this.status = status;
		this.blacklist = new HashSet<String>();
		this.operators = new HashSet<String>();
		this.whitelist = new HashSet<String>();
		this.endpoints = new HashMap<String, EndPoint>();
	}
	
	public abstract String[] getAliases();
	
	public final Set<String> getBlacklist() {
		return blacklist;
	}
	
	@Override
	public final File getConfigFile() {
		return new File (getDataFolder(), getName() + ".yml");
	}
	
	@Override
	public final File getDataFolder() {
		return plugin.getChannelManager().getChannelDirectory();
	}
	
	@Override
	public InputStream getDefaultConfigStream() {
		return plugin.getResource("channel.yml");
	}
	
	public abstract String getDisplayColour();
	
	public abstract String getFormat();
	
	public final Set<EndPoint> getLinkedPoints() {
		return new HashSet<EndPoint>(endpoints.values());
	}
	
	public final Set<String> getOperators() {
		return operators;
	}
	
	public final Set<Participant> getParticipants() {
		Set<Participant> participants = new HashSet<Participant>();
		
		for (EndPoint endpoint : getLinkedPoints())
			if (endpoint instanceof Participant)
				participants.add((Participant) endpoint);
		
		return participants;
	}
	
	public final String getPointType() {
		return "Channel";
	}
	
	public abstract Range getRange();
	
	public final Status getStatus() {
		return status;
	}
	
	public abstract String getTag();
	
	public final String getType() {
		return type;
	}
	
	public final Set<String> getWhitelist() {
		return whitelist;
	}
	
	public Message handleMessage(EndPoint sender, String message) {
		return new Message(sender, new HashSet<EndPoint>(), "", message);
	}
	
	public void init() {
		
	}
	
	public final boolean isLinked(EndPoint endpoint) {
		if (endpoint == null)
			return false;
		
		return endpoints.containsKey((endpoint.getPointType() + ":" + endpoint.getName()).toLowerCase());
	}
	
	public final void link(EndPoint endpoint) {
		if (endpoint == null || endpoint instanceof Channel)
			return;
		
		if (!isLinked(endpoint))
			endpoints.put((endpoint.getPointType() + ":" + endpoint.getName()).toLowerCase(), endpoint);
		
		if (!endpoint.isLinked(this))
			endpoint.link(this);
	}
	
	public final void messageIn(EndPoint sender, String format, String message) {
		if (sender == null || format == null || format.isEmpty() || message == null || message.isEmpty())
			return;
		
		for (EndPoint endpoint : getLinkedPoints())
			endpoint.messageIn(sender, getFormat(), message);
	}
	
	public final void messageOut(EndPoint recipient, String message) {}
	
	public final void notice(String... messages) {
		for (EndPoint endpoint : getLinkedPoints())
			if (endpoint instanceof Participant)
				endpoint.notice(messages);
	}
	
	public final void register(ChatAddon... addons) {
		plugin.getAddonManager().registerAddons(addons);
	}
	
	public final void register(ChannelLoader... loaders) {
		plugin.getChannelManager().registerLoaders(loaders);
	}
	
	public void reload() {
		
	}
	
	public void save() {
		
	}
	
	public final void unlink(EndPoint endpoint) {
		if (endpoint == null || endpoint instanceof Channel)
			return;
		
		if (isLinked(endpoint))
			endpoints.remove((endpoint.getPointType() + ":" + endpoint.getName()).toLowerCase());
		
		if (endpoint.isLinked(this))
			endpoint.unlink(this);
	}
}