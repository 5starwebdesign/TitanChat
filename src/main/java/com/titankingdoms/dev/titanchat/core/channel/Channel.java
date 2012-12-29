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

package com.titankingdoms.dev.titanchat.core.channel;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.titankingdoms.dev.titanchat.core.addon.Addon;
import com.titankingdoms.dev.titanchat.core.channel.setting.SettingModifier;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.format.variable.FormatVariable;
import com.titankingdoms.dev.titanchat.loading.Loadable;
import com.titankingdoms.dev.titanchat.util.Debugger;

public abstract class Channel extends Loadable implements Listener {
	
	protected final Debugger db = new Debugger(2, "Channel");
	
	private File configFile;
	private FileConfiguration config;
	
	private final Type type;
	
	private final SettingModifier settings;
	
	private final Set<String> admins;
	private final Set<String> blacklist;
	private final Set<String> participants;
	private final Set<String> whitelist;
	
	public Channel(String name, Type type) {
		super(name);
		this.type = type;
		this.settings = new SettingModifier();
		this.admins = new HashSet<String>();
		this.blacklist = new HashSet<String>();
		this.participants = new HashSet<String>();
		this.whitelist = new HashSet<String>();
	}
	
	public Set<String> getAdmins() {
		return admins;
	}
	
	public abstract String[] getAliases();
	
	public Set<String> getBlacklist() {
		return blacklist;
	}
	
	public abstract ChannelLoader getChannelLoader();
	
	public abstract ChatHandler getChatHandler();
	
	public final FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public abstract String getFormat();
	
	public Set<String> getParticipants() {
		return Collections.unmodifiableSet(participants);
	}
	
	public abstract String getPassword();
	
	public abstract Range getRange();
	
	public final boolean getSetting(String path, boolean def) {
		return getConfig().getBoolean(path, def);
	}
	
	public final double getSetting(String path, double def) {
		return getConfig().getDouble(path, def);
	}
	
	public final int getSetting(String path, int def) {
		return getConfig().getInt(path, def);
	}
	
	public final ItemStack getSetting(String path, ItemStack def) {
		return getConfig().getItemStack(path, def);
	}
	
	public final List<?> getSetting(String path, List<?> def) {
		return getConfig().getList(path, def);
	}
	
	public final long getSetting(String path, long def) {
		return getConfig().getLong(path, def);
	}
	
	public final OfflinePlayer getSetting(String path, OfflinePlayer def) {
		return getConfig().getOfflinePlayer(path, def);
	}
	
	public final String getSetting(String path, String def) {
		return getConfig().getString(path, def);
	}
	
	public final Vector getSetting(String path, Vector def) {
		return getConfig().getVector(path, def);
	}
	
	public final SettingModifier getSettingModifier() {
		return settings;
	}
	
	public abstract String getTag();
	
	public final Type getType() {
		return type;
	}
	
	public Set<String> getWhitelist() {
		return whitelist;
	}
	
	public boolean isAdmin(String name) {
		return admins.contains(name);
	}
	
	public boolean isAdmin(Participant participant) {
		return isAdmin(participant.getName());
	}
	
	public boolean isAdmin(OfflinePlayer player) {
		return isAdmin(player.getName());
	}
	
	public boolean isBlacklisted(String name) {
		return blacklist.contains(name);
	}
	
	public boolean isBlacklisted(Participant participant) {
		return isBlacklisted(participant.getName());
	}
	
	public boolean isBlacklisted(OfflinePlayer player) {
		return isBlacklisted(player.getName());
	}
	
	public boolean isParticipating(String name) {
		return participants.contains(name);
	}
	
	public boolean isParticipating(Participant participant) {
		return isParticipating(participant.getName());
	}
	
	public boolean isParticipating(OfflinePlayer player) {
		return isParticipating(player.getName());
	}
	
	public boolean isWhitelisted(String name) {
		return whitelist.contains(name);
	}
	
	public boolean isWhitelisted(Participant participant) {
		return isWhitelisted(participant.getName());
	}
	
	public boolean isWhitelisted(OfflinePlayer player) {
		return isWhitelisted(player.getName());
	}
	
	public void join(Participant participant) {
		if (participant == null)
			return;
		
		if (!participants.contains(participant.getName()))
			participants.add(participant.getName());
		
		if (!participant.isParticipating(this))
			participant.join(this);
	}
	
	public void leave(Participant participant) {
		if (participant == null)
			return;
		
		if (participants.contains(participant.getName()))
			participants.remove(participant.getName());
		
		if (participant.isParticipating(this))
			participant.leave(this);
	}
	
	public final void register(Addon... addons) {
		plugin.getAddonManager().register(addons);
	}
	
	public final void register(Command... commands) {
		plugin.getCommandManager().register(commands);
	}
	
	public final void register(FormatVariable... variables) {
		plugin.getFormatHandler().getVariableManager().register(variables);
	}
	
	public abstract void reload();
	
	@Override
	public final void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getChannelManager().getChannelDirectory(), getName() + ".yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("channel.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	@Override
	public final void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
}