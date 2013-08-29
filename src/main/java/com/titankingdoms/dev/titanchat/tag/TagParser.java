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

package com.titankingdoms.dev.titanchat.tag;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;

public final class TagParser implements Manager<Tag> {
	
	private final TitanChat plugin;
	
	private final Pattern pattern = Pattern.compile("(?i)(%)([a-z0-9]+)");
	
	private File configFile;
	private FileConfiguration config;
	
	private final Map<String, Tag> tags;
	
	public TagParser() {
		this.plugin = TitanChat.getInstance();
		this.tags = new HashMap<String, Tag>();
	}
	
	@Override
	public Tag get(String name) {
		return (has(name)) ? tags.get(name.toLowerCase()) : null;
	}
	
	@Override
	public List<Tag> getAll() {
		return new ArrayList<Tag>(tags.values());
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public String getDefaultFormat() {
		return getConfig().getString("format.default", "%tag%");
	}
	
	public String getFormat(String name) {
		return getConfig().getString("format.tags." + name.toLowerCase(), getDefaultFormat());
	}
	
	@Override
	public String getName() {
		return "TagParser";
	}
	
	public Tag getTag(String name) {
		return get(name);
	}
	
	public List<Tag> getTags() {
		return getAll();
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? tags.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(Tag tag) {
		if (tag == null || !has(tag.getTag()))
			return false;
		
		return get(tag.getTag()).equals(tag);
	}
	
	public boolean hasTag(String name) {
		return has(name);
	}
	
	public boolean hasTag(Tag tag) {
		return has(tag);
	}
	
	@Override
	public void load() {}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(tags.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String tag : tags.keySet()) {
			if (!tag.startsWith(name.toLowerCase()))
				continue;
			
			matches.add(tag);
		}
		
		Collections.sort(matches);
		
		return matches;
	}
	
	public String parse(ConverseEvent event) {
		StringBuffer format = new StringBuffer();
		Matcher match = pattern.matcher(event.getFormat());
		
		while (match.find()) {
			String replacement = match.group();
			
			if (has(replacement)) {
				replacement = get(replacement).getValue(event.clone());
				
				if (replacement == null)
					replacement = "";
				
				if (!replacement.isEmpty())
					replacement = getFormat(match.group()).replace("%tag%", replacement);
			}
			
			match.appendReplacement(format, replacement);
		}
		
		return match.appendTail(format).toString();
	}
	
	@Override
	public void registerAll(Tag... tags) {
		if (tags == null)
			return;
		
		for (Tag tag : tags) {
			if (tag == null)
				continue;
			
			if (has(tag)) {
				plugin.log(Level.WARNING, "Duplicate: " + tag);
				continue;
			}
			
			this.tags.put(tag.getTag().toLowerCase(), tag);
		}
	}
	
	@Override
	public void reload() {
		unload();
		load();
	}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "tag.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("tag.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	@Override
	public void unload() {
		for (Tag tag : getAll())
			unregister(tag);
	}
	
	@Override
	public void unregister(Tag tag) {
		if (tag == null || !has(tag))
			return;
		
		this.tags.remove(tag.getTag().toLowerCase());
	}
}